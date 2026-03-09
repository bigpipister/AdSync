package com.cht.directory.connector.security;

import java.io.File;
import java.security.KeyStore;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import com.cht.directory.connector.security.utils.DataUtils;
import com.cht.kms.client.util.CryptoUtil;
import com.cht.org.jose4j.jwk.JsonWebKey;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KmsClientService {

    @Value("${kms.client.jceks.name}")
    private String jceksName;

    @Value("${kms.client.jceks.password}")
    private String jceksPass;

    private KeyStore jceks;
    private JsonWebKey jsonWebKey;

    @PostConstruct
    public void init() throws Exception {

        jceks = CryptoUtil.loadKeyStoreFromFile(new File(jceksName), jceksPass.toCharArray(),
                "JCEKS");

        jsonWebKey = DataUtils.loadKeyFromFile(new File(jceksName), jceksPass.toCharArray());
    }

    public String encrypt(String cn, byte[] password) throws Exception {

        return DataUtils.encrypt(jsonWebKey, cn, password);
    }

    public byte[] decrypt(String cn, String encryptedPassword) throws Exception {

        return DataUtils.decrypt(jceks, jceksPass.toCharArray(), cn, encryptedPassword);
    }
}

package com.cht.directory.connector.filter.security;

import com.cht.kms.client.util.CryptoUtil;
import com.cht.org.jose4j.jwa.AlgorithmConstraints;
import com.cht.org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import com.cht.org.jose4j.jwe.JsonWebEncryption;
import com.cht.org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import com.cht.org.jose4j.jwk.JsonWebKey;
import com.cht.org.jose4j.lang.JoseException;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;

public class DataUtils {

    public static KeyStore loadKeyStoreFromFile(File file, char[] passphrase)
            throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
        KeyStore jceks = CryptoUtil.loadKeyStoreFromFile(file, passphrase, "JCEKS");
        return jceks;
    }

    public static JsonWebKey loadKeyFromFile(File file, char[] passphrase)
            throws CertificateException, IOException, JoseException, KeyStoreException,
            NoSuchAlgorithmException, UnrecoverableKeyException {

        KeyStore jceks = CryptoUtil.loadKeyStoreFromFile(file, passphrase, "JCEKS");
        String kekIdentifier = CryptoUtil.getLatestCreationKeyFromStore(jceks, passphrase);

        JsonWebKey jwk = JsonWebKey.Factory.newJwk(jceks.getKey(kekIdentifier, passphrase));
        jwk.setKeyId(kekIdentifier);

        return jwk;
    }

    public static String encrypt(JsonWebKey jwk, String cn, byte[] password) throws JoseException {

        JsonWebEncryption jwe = new JsonWebEncryption();

        jwe.setPlaintext(password);

        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.DIRECT);

        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);
        jwe.setKeyIdHeaderValue(jwk.getKeyId());
        jwe.setHeader("cn", cn);

        jwe.setKey(jwk.getKey());

        return jwe.getCompactSerialization();
    }

    public static byte[] decrypt(KeyStore jceks, char[] passphrase, String expectedCN,
                                 String compactSerialization) throws JoseException, UnrecoverableKeyException,
            NoSuchAlgorithmException, KeyStoreException {

        JsonWebEncryption receiverJwe = new JsonWebEncryption();

        AlgorithmConstraints algConstraints = new AlgorithmConstraints(
                AlgorithmConstraints.ConstraintType.WHITELIST,
                KeyManagementAlgorithmIdentifiers.DIRECT);
        receiverJwe.setAlgorithmConstraints(algConstraints);
        AlgorithmConstraints encConstraints = new AlgorithmConstraints(
                AlgorithmConstraints.ConstraintType.WHITELIST,
                ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);
        receiverJwe.setContentEncryptionAlgorithmConstraints(encConstraints);

        receiverJwe.setCompactSerialization(compactSerialization);
        if (!receiverJwe.getHeader("cn").equalsIgnoreCase(expectedCN)) {
            throw new JoseException("CN claim value (" + receiverJwe.getHeader("cn")
                    + ") doesn't match expected value of " + expectedCN);
        }

        SecretKey keyEncryptionKey = (SecretKey) jceks.getKey(receiverJwe.getKeyIdHeaderValue(),
                passphrase);
        JsonWebKey jwk = JsonWebKey.Factory.newJwk(keyEncryptionKey);

        receiverJwe.setKey(jwk.getKey());

        return receiverJwe.getPlaintextBytes();
    }

    public static String hash(byte[] data) throws Exception {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(data);

        return byte2Hex(digest.digest());
    }

    public static String hash(String data) throws Exception {

        return hash(data.getBytes());
    }

    public static String byte2Hex(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++)
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        return result;
    }

    public static String string2Hex(String plainText, String charset)
            throws UnsupportedEncodingException {
        return String.format("%040x", new BigInteger(1, plainText.getBytes(charset)));
    }

    public static String fromQuoteUnicode(byte[] unicodePwd) throws Exception {

        String utf16String = new String(unicodePwd, "UTF-16LE");

        return StringUtils.remove(new String(utf16String.getBytes("UTF-8"), "UTF-8"), "\"");
    }

    public static byte[] toQuoteUnicode(String password) {

        String quotedPassword = "\"" + password + "\"";
        char unicodePwd[] = quotedPassword.toCharArray();
        byte pwdArray[] = new byte[unicodePwd.length * 2];
        for (int i = 0; i < unicodePwd.length; i++) {
            pwdArray[i * 2 + 1] = (byte) (unicodePwd[i] >>> 8);
            pwdArray[i * 2 + 0] = (byte) (unicodePwd[i] & 0xff);
        }
        return pwdArray;
    }
}

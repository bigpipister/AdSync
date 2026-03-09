package com.cht.directory.connector.filter.controller;

import com.cht.directory.connector.filter.security.DataUtils;
import com.cht.directory.connector.filter.service.model.DecryptRequestModel;
import com.cht.directory.connector.filter.service.model.DecryptResultModel;
import com.cht.directory.connector.security.KmsClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DecryptPwdController {

    private static final Logger log = LoggerFactory.getLogger(DecryptPwdController.class);

    @Autowired
    private KmsClientService kmsClientService;

    @RequestMapping(value = "/decryptPwd", method = RequestMethod.POST)
    public DecryptResultModel findAllAuditLogs(@RequestBody DecryptRequestModel request) {

        //log.info(request.toString());
        DecryptResultModel decryptResultModel = new DecryptResultModel();
        decryptResultModel.setResult("T");

        try {
            byte[] unicodePwd = kmsClientService.decrypt(request.getCn(), request.getEncryptPwd());

            decryptResultModel.setData(DataUtils.fromQuoteUnicode(unicodePwd));

        } catch (Exception e) {
            decryptResultModel.setMessage(e.getMessage());
        }
        return decryptResultModel;
    }
}

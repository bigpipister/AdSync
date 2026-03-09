package com.cht.directory.connector.filter.controller;

import com.cht.directory.connector.filter.service.PwdDiffWebService;
import com.cht.directory.connector.filter.service.model.PwdDiffRequestModel;
import com.cht.directory.connector.filter.service.model.PwdDiffResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PwdDiffController {

    private static final Logger log = LoggerFactory.getLogger(PwdDiffController.class);

    @Autowired
    private PwdDiffWebService pwdDiffWebService;

    @RequestMapping(value = "/pwdDiffList", method = RequestMethod.POST)
    public PwdDiffResultModel findAllPwdDiffList(@RequestBody PwdDiffRequestModel request) {
        return pwdDiffWebService.getInvalidPwdDiff(request.getPage(), request.getSize());
    }
}

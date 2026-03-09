package com.cht.directory.connector.filter.controller;

import com.cht.directory.connector.filter.service.PropertiesDiffWebService;
import com.cht.directory.connector.filter.service.PwdDiffWebService;
import com.cht.directory.connector.filter.service.model.PropertiesDiffRequestModel;
import com.cht.directory.connector.filter.service.model.PropertiesDiffResultModel;
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
public class PropertiesDiffController {

    private static final Logger log = LoggerFactory.getLogger(PropertiesDiffController.class);

    @Autowired
    private PropertiesDiffWebService propertiesDiffWebService;

    @RequestMapping(value = "/propertiesDiffList", method = RequestMethod.POST)
    public PropertiesDiffResultModel findAllPropertiesDiffList(@RequestBody PropertiesDiffRequestModel request) {
        return propertiesDiffWebService.getInvalidPropertiesDiff(request.getPage(), request.getSize());
    }
}

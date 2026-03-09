package com.cht.directory.connector.filter.service;

import com.cht.directory.connector.filter.web.entity.StatusLogs;
import com.cht.directory.connector.filter.web.repository.StatusLogsRepository;
import com.cht.directory.connector.filter.service.model.StatusResultModel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusLogsWebService {

    private static final Logger log = LoggerFactory.getLogger(StatusLogsWebService.class);

    @Autowired
    private StatusLogsRepository statusLogsRepository;

    public StatusResultModel getStatusLogs() {

        List<StatusLogs> statusLogsList = statusLogsRepository.findAll();
        StatusResultModel statusResultModel = new StatusResultModel();
        statusResultModel.setResult("T");
        statusResultModel.setMessage("");
        statusResultModel.setData(statusLogsList);
        statusResultModel.setTotal(statusLogsList.size());

        return statusResultModel;
    }
}

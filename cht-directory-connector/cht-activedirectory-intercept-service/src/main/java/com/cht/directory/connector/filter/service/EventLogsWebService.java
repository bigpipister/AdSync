package com.cht.directory.connector.filter.service;

import com.cht.directory.connector.filter.web.entity.EventLogs;
import com.cht.directory.connector.filter.web.repository.EventLogsRepository;
import com.cht.directory.connector.filter.service.model.EventResultModel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventLogsWebService {

    private static final Logger log = LoggerFactory.getLogger(EventLogsWebService.class);

    @Autowired
    private EventLogsRepository eventLogsRepository;

    public EventResultModel getEventLogs(int page, int size, String region, String jobname, String dn, String keyword) {

        Pageable pageable = PageRequest.of(page-1, size);

        int total =
                eventLogsRepository.findByRegionContainingAndJobnameContainingAndDnContainingAndExceptioncontentContaining(region, jobname, dn, keyword).size();
        Page<EventLogs> eventLogsList =
                eventLogsRepository.findByRegionContainingAndJobnameContainingAndDnContainingAndExceptioncontentContaining(
                        region, jobname, dn, keyword, pageable);
        EventResultModel eventResultModel = new EventResultModel();
        eventResultModel.setResult("T");
        eventResultModel.setMessage("");
        eventResultModel.setData(eventLogsList.getContent());
        eventResultModel.setTotal(total);

        return eventResultModel;
    }

}

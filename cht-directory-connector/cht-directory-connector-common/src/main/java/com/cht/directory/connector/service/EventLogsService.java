package com.cht.directory.connector.service;

import com.cht.directory.connector.type.PlaceHolder;
import com.cht.directory.domain.EventLogs;
import com.cht.directory.domain.EventLogsId;
import com.cht.directory.domain.StatusLogs;
import com.cht.directory.repository.EventLogsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class EventLogsService {

    @Value("${activedirectory.service.placeholder: inner}")
    private PlaceHolder placeHolder;

    @Autowired
    private EventLogsRepository eventLogsRepository;

    public void record(String jobName, String dn, String exceptioncontent,
            String exceptionhash) throws Exception {

        EventLogs eventLogs = new EventLogs();

        eventLogs.setRegion(placeHolder.name().toLowerCase());
        eventLogs.setJobname(jobName);
        eventLogs.setDn(dn);
        Date date = new Date();
        eventLogs.setActivitydatetime(new Timestamp(date.getTime()));
        eventLogs.setExceptioncontent(exceptioncontent);
        eventLogs.setExceptionhash(exceptionhash);

        eventLogsRepository.save(eventLogs);
    }

    public void del(String jobName, String dn) throws Exception {

        eventLogsRepository.deleteByRegionAndJobnameAndDn(placeHolder.name().toLowerCase(), jobName, dn);
    }

    public List<EventLogs> getEventLogs() {
        return eventLogsRepository.findAll();
    }
}

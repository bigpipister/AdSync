package com.cht.directory.connector.service;

import com.cht.directory.connector.type.PlaceHolder;
import com.cht.directory.domain.StatusLogs;
import com.cht.directory.repository.StatusLogsRepository;
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
public class StatusLogsService {

    @Value("${activedirectory.service.placeholder: inner}")
    private PlaceHolder placeHolder;

    @Autowired
    private StatusLogsRepository statusLogsRepository;

    public void record(String jobName, String dn, String displayName, Timestamp startDateTime,
            boolean running, String health, long durationInMilliseconds) throws Exception {

        StatusLogs statusLogs = new StatusLogs();

        statusLogs.setRegion(placeHolder.name().toLowerCase());
        statusLogs.setJobname(jobName);
        statusLogs.setDn(dn);
        statusLogs.setDisplayname(displayName);
        statusLogs.setRunning(running);
        statusLogs.setHealth(health);
        statusLogs.setStartdatetime(startDateTime);
        statusLogs.setDurationseconds(durationInMilliseconds);

        statusLogsRepository.save(statusLogs);
    }

    public List<StatusLogs> getStatusLogs() {
        return statusLogsRepository.findAll();
    }
}

package com.cht.directory.repository;

import com.cht.directory.domain.EventLogs;
import com.cht.directory.domain.EventLogsId;
import com.cht.directory.domain.StatusLogs;
import com.cht.directory.domain.StatusLogsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface EventLogsRepository
        extends JpaRepository<EventLogs, EventLogsId>, JpaSpecificationExecutor<EventLogs> {

    void deleteByRegionAndJobnameAndDn(String region, String jobName, String dn);
}

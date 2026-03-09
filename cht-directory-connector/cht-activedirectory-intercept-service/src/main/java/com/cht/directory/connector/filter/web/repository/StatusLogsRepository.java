package com.cht.directory.connector.filter.web.repository;

import com.cht.directory.connector.filter.web.entity.StatusLogs;
import com.cht.directory.connector.filter.web.entity.StatusLogsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface StatusLogsRepository
        extends JpaRepository<StatusLogs, StatusLogsId>, JpaSpecificationExecutor<StatusLogs> {

}

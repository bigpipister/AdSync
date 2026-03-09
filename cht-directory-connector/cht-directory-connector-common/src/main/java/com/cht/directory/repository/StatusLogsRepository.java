package com.cht.directory.repository;

import com.cht.directory.domain.AuditLogs;
import com.cht.directory.domain.StatusLogs;
import com.cht.directory.domain.StatusLogsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface StatusLogsRepository
        extends JpaRepository<StatusLogs, StatusLogsId>, JpaSpecificationExecutor<StatusLogs> {

}

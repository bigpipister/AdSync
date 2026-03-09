package com.cht.exchange.log.repository;

import com.cht.exchange.log.entity.AuditLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogsRepository
        extends JpaRepository<AuditLogs, String>, JpaSpecificationExecutor<AuditLogs> {

}

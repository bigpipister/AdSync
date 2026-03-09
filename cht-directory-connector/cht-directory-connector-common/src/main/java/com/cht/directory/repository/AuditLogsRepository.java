package com.cht.directory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cht.directory.domain.AuditLogs;

@SuppressWarnings("unused")
@Repository
public interface AuditLogsRepository
        extends JpaRepository<AuditLogs, String>, JpaSpecificationExecutor<AuditLogs> {

    List<AuditLogs> findByTargetresourcesAndResultAndResultcodeAndAdditionaldetailsAndRegionOrderByActivitydatetimeDesc(
            String targetResources, String result, int resultCode, String additionalDetails,
            String region);
}

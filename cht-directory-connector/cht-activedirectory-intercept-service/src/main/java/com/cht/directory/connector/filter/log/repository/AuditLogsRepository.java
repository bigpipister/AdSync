package com.cht.directory.connector.filter.log.repository;

import com.cht.directory.connector.filter.log.entity.AuditLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface AuditLogsRepository
        extends JpaRepository<AuditLogs, String>, JpaSpecificationExecutor<AuditLogs> {

    // 用來算總筆數
    List<AuditLogs> findByRegionContainingAndActivitydisplaynameContainingAndTargetresourcesContainingAndActivitydatetimeBetween(
            String region, String activitydisplayname, String dn, Timestamp startTime, Timestamp endTime);

    // 分頁結果
    Page<AuditLogs> findByRegionContainingAndActivitydisplaynameContainingAndTargetresourcesContainingAndActivitydatetimeBetween(
            String region, String activitydisplayname, String dn, Timestamp startTime, Timestamp endTime, Pageable pageable);
}

package com.cht.directory.connector.filter.web.repository;

import com.cht.directory.connector.filter.web.entity.WebAuditLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface WebAuditLogsRepository
        extends JpaRepository<WebAuditLogs, String>, JpaSpecificationExecutor<WebAuditLogs> {

    // 用來算總筆數
    List<WebAuditLogs> findByRegionContainingAndActivitydisplaynameContainingAndTargetresourcesContainingAndActivitydatetimeBetween(
            String region, String activitydisplayname, String dn, Timestamp startTime, Timestamp endTime);

    // 分頁結果
    Page<WebAuditLogs> findByRegionContainingAndActivitydisplaynameContainingAndTargetresourcesContainingAndActivitydatetimeBetween(
            String region, String activitydisplayname, String dn, Timestamp startTime, Timestamp endTime, Pageable pageable);
}

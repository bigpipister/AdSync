package com.cht.directory.connector.filter.web.repository;

import com.cht.directory.connector.filter.web.entity.EventLogs;
import com.cht.directory.connector.filter.web.entity.EventLogsId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface EventLogsRepository
        extends JpaRepository<EventLogs, EventLogsId>, JpaSpecificationExecutor<EventLogs> {

    // 用來算總筆數
    List<EventLogs> findByRegionContainingAndJobnameContainingAndDnContainingAndExceptioncontentContaining(
            String region, String jobname, String dn, String keyword);

    // 分頁結果
    Page<EventLogs> findByRegionContainingAndJobnameContainingAndDnContainingAndExceptioncontentContaining(
            String region, String jobname, String dn, String keyword, Pageable pageable);
}

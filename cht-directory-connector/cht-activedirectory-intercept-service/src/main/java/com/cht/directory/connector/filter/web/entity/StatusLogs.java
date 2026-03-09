package com.cht.directory.connector.filter.web.entity;

import com.cht.directory.connector.filter.web.entity.StatusLogsId;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@IdClass(StatusLogsId.class)
@Table(name = "status_logs")
public class StatusLogs {

    @Id
    @Column(name = "region", length = 256)
    private String region;

    @Id
    @Column(name = "jobname", length = 256)
    private String jobname;

    @Id
    @Column(name = "dn", length = 256)
    private String dn;

    @Column(name = "displayname", length = 256)
    private String displayname;

    @Column(name = "running")
    private boolean running;

    @Column(name = "health", length = 256)
    private String health;

    @Column(name = "startdatetime")
    private java.sql.Timestamp startdatetime;

    @Column(name = "durationseconds")
    private long durationseconds;
}

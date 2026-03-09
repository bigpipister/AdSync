package com.cht.directory.connector.filter.web.entity;

import com.cht.directory.connector.filter.web.entity.EventLogsId;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@IdClass(EventLogsId.class)
@Table(name = "event_logs")
public class EventLogs {

    @Id
    @Column(name = "region", length = 256)
    private String region;

    @Id
    @Column(name = "jobname", length = 256)
    private String jobname;

    @Id
    @Column(name = "dn", length = 256)
    private String dn;

    @Column(name = "activitydatetime")
    private java.sql.Timestamp activitydatetime;

    @Column(name = "exceptioncontent", length = 2048)
    private String exceptioncontent;

    @Id
    @Column(name = "exceptionhash", length = 256)
    private String exceptionhash;

}

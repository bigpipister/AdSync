package com.cht.exchange.log.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@Table(name = "audit_logs")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class AuditLogs {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name = "region", length = 256)
    private String region;

    @Column(name = "activitydatetime")
    private java.sql.Timestamp activitydatetime;

    @Column(name = "activitydisplayname", length = 256)
    private String activitydisplayname;

    @Column(name = "additionaldetails", length = 2048)
    private String additionaldetails;

    @Column(name = "category", length = 256)
    private String category;

    @Column(name = "correlationid", length = 256)
    private String correlationid;

    @Column(name = "initiatedby", length = 256)
    private String initiatedby;

    @Column(name = "loggedbyservice", length = 256)
    private String loggedbyservice;

    @Column(name = "result", length = 256)
    private String result;

    @Column(name = "resultcode")
    private int resultcode;

    @Column(name = "resultreason", length = 256)
    private String resultreason;

    @Column(name = "targetresources", length = 256)
    private String targetresources;

    @Column(name = "durationinmilliseconds")
    private long durationinmilliseconds;
}

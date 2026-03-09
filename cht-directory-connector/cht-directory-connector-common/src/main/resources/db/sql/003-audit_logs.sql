--liquibase formatted sql

--changeset tsunghsien:003

create table if not exists audit_logs
(
    region                 varchar(256),
    activitydatetime       timestamp,
    activitydisplayname    varchar(256),
    additionaldetails      text,
    category               varchar(256),
    correlationid          varchar(256),
    id                     varchar(256),
    initiatedby            varchar(256),
    loggedbyservice        varchar(256),
    result                 varchar(256),
    resultcode             integer,
    resultreason           text,
    targetresources        varchar(256),
    durationinmilliseconds integer,
    constraint audit_logs_pkey
        primary key (id)
);

--liquibase formatted sql

--changeset tsunghsien:001


create table if not exists connector_space_ad_organizationalunit_details
(
    displayname    varchar(2048),
    ou             varchar(256),
    dn             varchar(256),
    objectguid     varchar(256) not null,
    syncattrs_hash varchar(256),
    dn_hash        varchar(256),
    objectclass    varchar(256) default 'organizationalUnit'::character varying,
    objectcategory varchar(256) default 'CN=Organizational-Unit,CN=Schema,CN=Configuration,DC=fia,DC=gov,DC=tw'::character varying,
    placeholder    varchar(256) not null,
    whencreated    timestamp with time zone,
    whenchanged    timestamp with time zone,
    constraint connector_space_ad_organizationalunit_details_pkey
        primary key (ou, placeholder)
);


create table if not exists connector_space_ad_group_details
(
    placeholder          varchar(256) not null,
    dn                   varchar(256),
    objectguid           varchar(256) not null,
    cn                   varchar(256),
    samaccountname       varchar(256),
    objectclass          varchar(256) default 'group'::character varying,
    objectcategory       varchar(256) default 'CN=Group,CN=Schema,CN=Configuration,DC=fia,DC=gov,DC=tw'::character varying,
    name                 varchar(256),
    groupType            bigint,
    memberOf             varchar(2048),
    extensionattribute10 varchar(256),
    extensionattribute11 varchar(256),
    extensionattribute12 varchar(256),
    extensionattribute13 varchar(256),
    extensionattribute14 varchar(256),
    extensionattribute15 varchar(256),
    whencreated          timestamp with time zone,
    whenchanged          timestamp with time zone,
    mail_enabled         varchar(256),
    syncattrs_hash       varchar(256),
    dn_hash              varchar(256),
    constraint connector_space_ad_group_details_pkey
    primary key (cn, placeholder)
);


create table if not exists connector_space_ad_person_details
(
    employeeid           varchar(256),
    useraccountcontrol   integer,
    userparameters       varchar(256),
    sn                   varchar(256),
    ou                   varchar(256),
    extensionattribute1  varchar(256),
    extensionattribute2  varchar(256),
    accountexpires       timestamp with time zone,
    userprincipalname    varchar(256),
    samaccountname       varchar(256),
    title                varchar(256),
    department           varchar(256),
    memberOf             varchar(2048),
    extensionattribute10 varchar(256),
    extensionattribute11 varchar(256),
    extensionattribute12 varchar(256),
    extensionattribute13 varchar(256),
    extensionattribute14 varchar(256),
    extensionattribute15 varchar(256),
    displayname          varchar(256),
    cn                   varchar(256),
    unicodepwd           varchar(256),
    dn                   varchar(256),
    mail                 varchar(256),
    objectguid           varchar(256) not null,
    unicodepwd_hash      varchar(256),
    syncattrs_hash       varchar(256),
    dn_hash              varchar(256),
    pager                varchar(256),
    pwdlastset           timestamp with time zone,
    objectclass          varchar(256) default 'user'::character varying,
    objectcategory       varchar(256) default 'CN=Person,CN=Schema,CN=Configuration,DC=fia,DC=gov,DC=tw'::character varying,
    placeholder          varchar(256) not null,
    whenchanged          timestamp with time zone,
    whencreated          timestamp with time zone,
    constraint connector_space_ad_person_details_pkey
        primary key (cn, placeholder)
);

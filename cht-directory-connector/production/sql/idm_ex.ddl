
-- Drop table

-- DROP TABLE exidm.connector_space_ad_organizationalunit_details;

CREATE TABLE exidm.connector_space_ad_organizationalunit_details (
    displayname varchar(2048) NULL,
    ou varchar(256) NOT NULL,
    dn varchar(256) NULL,
    objectguid varchar(256) NOT NULL,
    syncattrs_hash varchar(256) NULL,
    dn_hash varchar(256) NULL,
    objectclass varchar(256) NULL DEFAULT 'organizationalUnit'::character varying,
    objectcategory varchar(256) NULL DEFAULT 'CN=Organizational-Unit,CN=Schema,CN=Configuration,DC=fia,DC=gov,DC=tw'::character varying,
    placeholder varchar(256) NOT NULL,
    whencreated timestamptz NULL,
    whenchanged timestamptz NULL,
    CONSTRAINT connector_space_ad_organizationalunit_details_pk PRIMARY KEY (ou, placeholder)
);

-- Drop table

-- DROP TABLE exidm.connector_space_ad_group_details;

CREATE TABLE exidm.connector_space_ad_group_details (
	placeholder varchar(256) NOT NULL,
    displayname varchar(2048) NULL,
	dn varchar(256) NOT NULL,
	objectguid varchar(256) NOT NULL,
	cn varchar(256) NOT NULL,
	samaccountname varchar(256) NULL,
	objectclass varchar(256) NULL DEFAULT 'group'::character varying,
	objectcategory varchar(256) NULL DEFAULT 'CN=Group,CN=Schema,CN=Configuration,DC=fia,DC=gov,DC=tw'::character varying,
	"name" varchar(256) NULL,
	grouptype int8 NULL,
	memberof varchar(2048) NULL,
	extensionattribute10 varchar(256) NULL,
	extensionattribute11 varchar(256) NULL,
	extensionattribute12 varchar(256) NULL,
	extensionattribute13 varchar(256) NULL,
	extensionattribute14 varchar(256) NULL,
	extensionattribute15 varchar(256) NULL,
	whencreated timestamptz NULL,
	whenchanged timestamptz NULL,
	mail_enabled varchar(256) NULL,
	syncattrs_hash varchar(256) NULL,
	dn_hash varchar(256) NULL,
	CONSTRAINT connector_space_ad_group_details_pk PRIMARY KEY (cn, placeholder)
);

-- Drop table

-- DROP TABLE exidm.connector_space_ad_person_details;

CREATE TABLE exidm.connector_space_ad_person_details (
	employeeid varchar(256) NULL,
	useraccountcontrol int4 NULL,
	userparameters varchar(256) NULL,
	sn varchar(256) NULL,
	ou varchar(256) NULL,
	extensionattribute1 varchar(256) NULL,
	extensionattribute2 varchar(256) NULL,
	accountexpires timestamptz NULL,
	userprincipalname varchar(256) NULL,
	samaccountname varchar(256) NULL,
	title varchar(256) NULL,
	department varchar(256) NULL,
    memberof varchar(2048) NULL,
	extensionattribute10 varchar(256) NULL,
	extensionattribute11 varchar(256) NULL,
	extensionattribute12 varchar(256) NULL,
	extensionattribute13 varchar(256) NULL,
	extensionattribute14 varchar(256) NULL,
	extensionattribute15 varchar(256) NULL,
	displayname varchar(256) NULL,
	cn varchar(256) NOT NULL,
	unicodepwd varchar(256) NULL,
	dn varchar(256) NULL,
	mail varchar(256) NULL,
	objectguid varchar(256) NOT NULL,
	unicodepwd_hash varchar(256) NULL,
	syncattrs_hash varchar(256) NULL,
	dn_hash varchar(256) NULL,
    pager varchar(256) NULL,
	pwdlastset timestamptz NULL,
	objectclass varchar(256) NULL DEFAULT 'user'::character varying,
	objectcategory varchar(256) NULL DEFAULT 'CN=Person,CN=Schema,CN=Configuration,DC=fia,DC=gov,DC=tw'::character varying,
	placeholder varchar(256) NOT NULL,
	whenchanged timestamptz NULL,
	whencreated timestamptz NULL,
	CONSTRAINT connector_space_ad_person_details_pk PRIMARY KEY (cn, placeholder)
);

-- Drop table

-- DROP TABLE exidm.connector_space_ad_organizationalunit_sync_details;

CREATE TABLE exidm.connector_space_ad_organizationalunit_sync_details (
    displayname varchar(2048) NULL,
    ou varchar(256) NOT NULL,
    dn varchar(256) NULL,
    objectguid varchar(256) NOT NULL,
    syncattrs_hash varchar(256) NULL,
    dn_hash varchar(256) NULL,
    objectclass varchar(256) NULL DEFAULT 'organizationalUnit'::character varying,
    objectcategory varchar(256) NULL DEFAULT 'CN=Organizational-Unit,CN=Schema,CN=Configuration,DC=fia,DC=gov,DC=tw'::character varying,
    placeholder varchar(256) NOT NULL,
    whencreated timestamptz NULL,
    whenchanged timestamptz NULL,
    CONSTRAINT connector_space_ad_organizationalunit_sync_details_pk PRIMARY KEY (ou, placeholder)
);

-- Drop table

-- DROP TABLE exidm.connector_space_ad_group_sync_details;

CREATE TABLE exidm.connector_space_ad_group_sync_details (
    placeholder varchar(256) NOT NULL,
    displayname varchar(2048) NULL,
    dn varchar(256) NOT NULL,
    objectguid varchar(256) NOT NULL,
    cn varchar(256) NOT NULL,
    samaccountname varchar(256) NULL,
    objectclass varchar(256) NULL DEFAULT 'group'::character varying,
    objectcategory varchar(256) NULL DEFAULT 'CN=Group,CN=Schema,CN=Configuration,DC=fia,DC=gov,DC=tw'::character varying,
    "name" varchar(256) NULL,
    grouptype int8 NULL,
    memberof varchar(2048) NULL,
    extensionattribute10 varchar(256) NULL,
    extensionattribute11 varchar(256) NULL,
    extensionattribute12 varchar(256) NULL,
    extensionattribute13 varchar(256) NULL,
    extensionattribute14 varchar(256) NULL,
    extensionattribute15 varchar(256) NULL,
    whencreated timestamptz NULL,
    whenchanged timestamptz NULL,
    mail_enabled varchar(256) NULL,
    syncattrs_hash varchar(256) NULL,
    dn_hash varchar(256) NULL,
    CONSTRAINT connector_space_ad_group_sync_details_pk PRIMARY KEY (cn, placeholder)
);

-- Drop table

-- DROP TABLE exidm.connector_space_ad_person_sync_details;

CREATE TABLE exidm.connector_space_ad_person_sync_details (
    employeeid varchar(256) NULL,
    useraccountcontrol int4 NULL,
    userparameters varchar(256) NULL,
    sn varchar(256) NULL,
    ou varchar(256) NULL,
    extensionattribute1 varchar(256) NULL,
    extensionattribute2 varchar(256) NULL,
    accountexpires timestamptz NULL,
    userprincipalname varchar(256) NULL,
    samaccountname varchar(256) NULL,
    title varchar(256) NULL,
    department varchar(256) NULL,
    memberof varchar(2048) NULL,
    extensionattribute10 varchar(256) NULL,
    extensionattribute11 varchar(256) NULL,
    extensionattribute12 varchar(256) NULL,
    extensionattribute13 varchar(256) NULL,
    extensionattribute14 varchar(256) NULL,
    extensionattribute15 varchar(256) NULL,
    displayname varchar(256) NULL,
    cn varchar(256) NOT NULL,
    unicodepwd varchar(256) NULL,
    dn varchar(256) NULL,
    mail varchar(256) NULL,
    objectguid varchar(256) NOT NULL,
    unicodepwd_hash varchar(256) NULL,
    syncattrs_hash varchar(256) NULL,
    dn_hash varchar(256) NULL,
    pager varchar(256) NULL,
    pwdlastset timestamptz NULL,
    objectclass varchar(256) NULL DEFAULT 'user'::character varying,
    objectcategory varchar(256) NULL DEFAULT 'CN=Person,CN=Schema,CN=Configuration,DC=fia,DC=gov,DC=tw'::character varying,
    placeholder varchar(256) NOT NULL,
    whenchanged timestamptz NULL,
    whencreated timestamptz NULL,
    CONSTRAINT connector_space_ad_person_sync_details_pk PRIMARY KEY (cn, placeholder)
);

-- Drop table

-- DROP TABLE exidm.connector_space_ad_organizationalunit_external_details;

CREATE TABLE exidm.connector_space_ad_organizationalunit_external_details (
    displayname varchar(2048) NULL,
    ou varchar(256) NOT NULL,
    dn varchar(256) NULL,
    objectguid varchar(256) NOT NULL,
    syncattrs_hash varchar(256) NULL,
    dn_hash varchar(256) NULL,
    objectclass varchar(256) NULL DEFAULT 'organizationalUnit'::character varying,
    objectcategory varchar(256) NULL DEFAULT 'CN=Organizational-Unit,CN=Schema,CN=Configuration,DC=fia,DC=gov,DC=tw'::character varying,
    placeholder varchar(256) NOT NULL,
    whencreated timestamptz NULL,
    whenchanged timestamptz NULL,
    CONSTRAINT connector_space_ad_organizationalunit_external_details_pk PRIMARY KEY (ou, placeholder)
);

-- Drop table

-- DROP TABLE exidm.connector_space_ad_group_external_details;

CREATE TABLE exidm.connector_space_ad_group_external_details (
    placeholder varchar(256) NOT NULL,
    displayname varchar(2048) NULL,
    dn varchar(256) NOT NULL,
    objectguid varchar(256) NOT NULL,
    cn varchar(256) NOT NULL,
    samaccountname varchar(256) NULL,
    objectclass varchar(256) NULL DEFAULT 'group'::character varying,
    objectcategory varchar(256) NULL DEFAULT 'CN=Group,CN=Schema,CN=Configuration,DC=fia,DC=gov,DC=tw'::character varying,
    "name" varchar(256) NULL,
    grouptype int8 NULL,
    memberof varchar(2048) NULL,
    extensionattribute10 varchar(256) NULL,
    extensionattribute11 varchar(256) NULL,
    extensionattribute12 varchar(256) NULL,
    extensionattribute13 varchar(256) NULL,
    extensionattribute14 varchar(256) NULL,
    extensionattribute15 varchar(256) NULL,
    whencreated timestamptz NULL,
    whenchanged timestamptz NULL,
    mail_enabled varchar(256) NULL,
    syncattrs_hash varchar(256) NULL,
    dn_hash varchar(256) NULL,
    CONSTRAINT connector_space_ad_group_external_details_pk PRIMARY KEY (cn, placeholder)
);

-- Drop table

-- DROP TABLE exidm.connector_space_ad_person_external_details;

CREATE TABLE exidm.connector_space_ad_person_external_details (
    employeeid varchar(256) NULL,
    useraccountcontrol int4 NULL,
    userparameters varchar(256) NULL,
    sn varchar(256) NULL,
    ou varchar(256) NULL,
    extensionattribute1 varchar(256) NULL,
    extensionattribute2 varchar(256) NULL,
    accountexpires timestamptz NULL,
    userprincipalname varchar(256) NULL,
    samaccountname varchar(256) NULL,
    title varchar(256) NULL,
    department varchar(256) NULL,
    memberof varchar(2048) NULL,
    extensionattribute10 varchar(256) NULL,
    extensionattribute11 varchar(256) NULL,
    extensionattribute12 varchar(256) NULL,
    extensionattribute13 varchar(256) NULL,
    extensionattribute14 varchar(256) NULL,
    extensionattribute15 varchar(256) NULL,
    displayname varchar(256) NULL,
    cn varchar(256) NOT NULL,
    unicodepwd varchar(256) NULL,
    dn varchar(256) NULL,
    mail varchar(256) NULL,
    objectguid varchar(256) NOT NULL,
    unicodepwd_hash varchar(256) NULL,
    syncattrs_hash varchar(256) NULL,
    dn_hash varchar(256) NULL,
    pager varchar(256) NULL,
    pwdlastset timestamptz NULL,
    objectclass varchar(256) NULL DEFAULT 'user'::character varying,
    objectcategory varchar(256) NULL DEFAULT 'CN=Person,CN=Schema,CN=Configuration,DC=fia,DC=gov,DC=tw'::character varying,
    placeholder varchar(256) NOT NULL,
    whenchanged timestamptz NULL,
    whencreated timestamptz NULL,
    CONSTRAINT connector_space_ad_person_external_details_pk PRIMARY KEY (cn, placeholder)
);

-- Drop table

-- DROP TABLE exidm.databasechangelog;

CREATE TABLE exidm.databasechangelog (
	id varchar(255) NOT NULL,
	author varchar(255) NOT NULL,
	filename varchar(255) NOT NULL,
	dateexecuted timestamp NOT NULL,
	orderexecuted int4 NOT NULL,
	exectype varchar(10) NOT NULL,
	md5sum varchar(35) NULL,
	description varchar(255) NULL,
	"comments" varchar(255) NULL,
	tag varchar(255) NULL,
	liquibase varchar(20) NULL,
	contexts varchar(255) NULL,
	labels varchar(255) NULL,
	deployment_id varchar(10) NULL
);

-- Drop table

-- DROP TABLE exidm.databasechangeloglock;

CREATE TABLE exidm.databasechangeloglock (
	id int4 NOT NULL,
	"locked" bool NOT NULL,
	lockgranted timestamp NULL,
	lockedby varchar(255) NULL,
	CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id)
);

-- Drop table

-- DROP TABLE exidm.audit_logs;

CREATE TABLE exidm.audit_logs (
    region varchar(256) NULL,
    activitydatetime timestamp NULL,
    activitydisplayname varchar(256) NULL,
    additionaldetails text NULL,
    category varchar(256) NULL,
    correlationid varchar(256) NULL,
    id varchar(256) NOT NULL,
    initiatedby varchar(256) NULL,
    loggedbyservice varchar(256) NULL,
    "result" varchar(256) NULL,
    resultcode int4 NULL,
    resultreason text NULL,
    targetresources varchar(256) NULL,
    durationinmilliseconds int4 NULL,
    CONSTRAINT audit_logs_pk PRIMARY KEY (id)
);

-- Drop table

-- DROP TABLE exidm.status_logs;

CREATE TABLE exidm.status_logs (
    region varchar(256) NOT NULL,
    jobname varchar(256) NOT NULL,
    dn varchar(256) NOT NULL,
    displayname varchar(256) NOT NULL,
    running bool NOT NULL,
    health varchar(256) NULL,
    startdatetime timestamp NULL,
    durationseconds int4 NULL,
    CONSTRAINT status_logs_pk PRIMARY KEY (region, jobname, dn)
);

-- Drop table

-- DROP TABLE exidm.event_logs;

CREATE TABLE exidm.event_logs (
    region varchar(256) NOT NULL,
    jobname varchar(256) NOT NULL,
    dn varchar(256) NOT NULL,
    activitydatetime timestamp NOT NULL,
    exceptioncontent text NULL,
    exceptionhash text NULL,
    CONSTRAINT event_logs_pk PRIMARY KEY (region, jobname, dn, exceptionhash)
);

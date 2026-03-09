create database idmdb;

create user inidm with encrypted password 'inidm';
create schema authorization inidm;
GRANT ALL ON TABLE inidm.audit_logs TO inidm;
GRANT ALL ON TABLE inidm.connector_space_ad_group_details TO inidm;
GRANT ALL ON TABLE inidm.connector_space_ad_group_sync_details TO inidm;
GRANT ALL ON TABLE inidm.connector_space_ad_organizationalunit_details TO inidm;
GRANT ALL ON TABLE inidm.connector_space_ad_organizationalunit_sync_details TO inidm;
GRANT ALL ON TABLE inidm.connector_space_ad_person_details TO inidm;
GRANT ALL ON TABLE inidm.connector_space_ad_person_sync_details TO inidm;
GRANT ALL ON TABLE inidm.databasechangelog TO inidm;
GRANT ALL ON TABLE inidm.databasechangeloglock TO inidm;

create user ceidm with encrypted password 'ceidm';
create schema authorization ceidm;
GRANT ALL ON TABLE ceidm.audit_logs TO ceidm;
GRANT ALL ON TABLE ceidm.connector_space_ad_group_details TO ceidm;
GRANT ALL ON TABLE ceidm.connector_space_ad_group_sync_details TO ceidm;
GRANT ALL ON TABLE ceidm.connector_space_ad_organizationalunit_details TO ceidm;
GRANT ALL ON TABLE ceidm.connector_space_ad_organizationalunit_sync_details TO ceidm;
GRANT ALL ON TABLE ceidm.connector_space_ad_person_details TO ceidm;
GRANT ALL ON TABLE ceidm.connector_space_ad_person_sync_details TO ceidm;
GRANT ALL ON TABLE ceidm.databasechangelog TO ceidm;
GRANT ALL ON TABLE ceidm.databasechangeloglock TO ceidm;

create user exidm with encrypted password 'exidm';
create schema authorization exidm;
GRANT ALL ON TABLE exidm.audit_logs TO exidm;
GRANT ALL ON TABLE exidm.connector_space_ad_group_details TO exidm;
GRANT ALL ON TABLE exidm.connector_space_ad_group_sync_details TO exidm;
GRANT ALL ON TABLE exidm.connector_space_ad_organizationalunit_details TO exidm;
GRANT ALL ON TABLE exidm.connector_space_ad_organizationalunit_sync_details TO exidm;
GRANT ALL ON TABLE exidm.connector_space_ad_person_details TO exidm;
GRANT ALL ON TABLE exidm.connector_space_ad_person_sync_details TO exidm;
GRANT ALL ON TABLE exidm.databasechangelog TO exidm;
GRANT ALL ON TABLE exidm.databasechangeloglock TO exidm;

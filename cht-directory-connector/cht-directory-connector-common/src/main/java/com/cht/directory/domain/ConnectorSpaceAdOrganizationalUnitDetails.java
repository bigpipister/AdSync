package com.cht.directory.domain;

import java.io.Serializable;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
@IdClass(ConnectorSpaceAdOrganizationalUnitDetailsId.class)
@Table(name = "connector_space_ad_organizationalunit_details")
public class ConnectorSpaceAdOrganizationalUnitDetails implements Serializable {

    @JsonIgnore
    @Column(name = "objectguid", length = 256, nullable = false)
    private String objectguid;

    @JsonIgnore
    @Id
    @Column(name = "placeholder", length = 256, nullable = false)
    private String placeholder;

    @Size(max = 2048)
    @Column(name = "displayname", length = 2048, nullable = true)
    private String displayname;

    @Id
    @Size(max = 256)
    @Column(name = "ou", length = 256, nullable = false)
    private String ou;

    @Size(max = 256)
    @Column(name = "dn", length = 256, nullable = false)
    private String dn;

    @Size(max = 256)
    @Column(name = "dn_hash", length = 256, nullable = false)
    private String dnHash;

    @JsonIgnore
    @Size(max = 256)
    @Column(name = "syncattrs_hash", length = 256, nullable = false)
    private String syncattrsHash;

    @JsonIgnore
    @Column(name = "whencreated")
    private java.sql.Timestamp whencreated;

    @JsonIgnore
    @Column(name = "whenchanged")
    private java.sql.Timestamp whenchanged;

    @JsonIgnore
    @Column(name = "objectclass", length = 256, nullable = false)
    private String objectclass;

    @JsonIgnore
    @Column(name = "objectcategory", length = 256, nullable = false)
    private String objectcategory;

}

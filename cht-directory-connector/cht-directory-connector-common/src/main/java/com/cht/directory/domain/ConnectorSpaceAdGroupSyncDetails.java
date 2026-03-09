package com.cht.directory.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@IdClass(ConnectorSpaceAdGroupSyncDetailsId.class)
@Table(name = "connector_space_ad_group_sync_details")
public class ConnectorSpaceAdGroupSyncDetails implements Serializable {

    public ConnectorSpaceAdGroupSyncDetails() {

        dn = "";
        objectguid = "";
        placeholder = "";
        displayname = "";
        cn = "";
        samaccountname = "";
        name = "";
        groupType = 0;
        memberOf = "";
        extensionattribute10 = "";
        extensionattribute11 = "";
        extensionattribute12 = "";
        extensionattribute13 = "";
        extensionattribute14 = "";
        extensionattribute15 = "";
        mailEnabled = "0";
    }

    @Size(max = 256)
    @Column(name = "dn", length = 256, nullable = false)
    private String dn;

    @JsonIgnore
    @Column(name = "objectguid", length = 256)
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
    @Column(name = "cn", length = 256, nullable = false)
    private String cn;

    @JsonIgnore
    @Size(max = 256)
    @Column(name = "samaccountname", length = 256, nullable = false)
    private String samaccountname;

    @Size(max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @Column(name = "grouptype")
    private long groupType;

    @JsonIgnore
    @Size(max = 2048)
    @Column(name = "memberof", length = 256, nullable = true)
    private String memberOf;

    @Size(max = 256)
    @Column(name = "extensionattribute10", length = 256, nullable = true)
    private String extensionattribute10;

    @Size(max = 256)
    @Column(name = "extensionattribute11", length = 256, nullable = true)
    private String extensionattribute11;

    @Size(max = 256)
    @Column(name = "extensionattribute12", length = 256, nullable = true)
    private String extensionattribute12;

    @Size(max = 256)
    @Column(name = "extensionattribute13", length = 256, nullable = true)
    private String extensionattribute13;

    @Size(max = 256)
    @Column(name = "extensionattribute14", length = 256, nullable = true)
    private String extensionattribute14;

    @Size(max = 256)
    @Column(name = "extensionattribute15", length = 256, nullable = true)
    private String extensionattribute15;

    @JsonIgnore
    @Size(max = 256)
    @Column(name = "syncattrs_hash", length = 256, nullable = false)
    private String syncattrsHash;

    @JsonIgnore
    @Size(max = 256)
    @Column(name = "mail_enabled", length = 256, nullable = false)
    private String mailEnabled;

    @Size(max = 256)
    @Column(name = "dn_hash", length = 256, nullable = false)
    private String dnHash;

    @JsonIgnore
    @Column(name = "objectclass", length = 256, nullable = false)
    private String objectclass;

    @JsonIgnore
    @Column(name = "objectcategory", length = 256, nullable = false)
    private String objectcategory;

    @JsonIgnore
    @Column(name = "whencreated", nullable = false)
    private java.sql.Timestamp whencreated;

    @JsonIgnore
    @Column(name = "whenchanged", nullable = false)
    private java.sql.Timestamp whenchanged;

}

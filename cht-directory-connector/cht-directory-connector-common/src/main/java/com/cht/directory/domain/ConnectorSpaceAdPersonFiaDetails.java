package com.cht.directory.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@IdClass(ConnectorSpaceAdPersonFiaDetailsId.class)
@Table(name = "connector_space_ad_person_fia_details")
public class ConnectorSpaceAdPersonFiaDetails implements Serializable {

    @JsonIgnore
    @Column(name = "objectguid", length = 256, nullable = false)
    private String objectguid;

    @JsonIgnore
    @Id
    @Column(name = "placeholder", length = 256, nullable = false)
    private String placeholder;

    @Size(max = 256)
    @Column(name = "employeeid", length = 256, nullable = true)
    private String employeeid;

    @Size(max = 256)
    @Column(name = "userparameters", length = 256, nullable = true)
    private String userparameters;

    @Column(name = "useraccountcontrol", nullable = false)
    private int useraccountcontrol;

    @Size(max = 256)
    @Column(name = "sn", length = 256, nullable = true)
    private String sn;

    @Size(max = 256)
    @Column(name = "ou", length = 256, nullable = true)
    private String ou;

    @Size(max = 256)
    @Column(name = "extensionattribute1", length = 256, nullable = true)
    private String extensionattribute1;

    @Size(max = 256)
    @Column(name = "extensionattribute2", length = 256, nullable = true)
    private String extensionattribute2;

    @Column(name = "accountexpires", nullable = false)
    private java.sql.Timestamp accountexpires;

    @Size(max = 256)
    @Column(name = "userprincipalname", length = 256, nullable = false)
    private String userprincipalname;

    @Size(max = 256)
    @Column(name = "samaccountname", length = 256, nullable = false)
    private String samaccountname;

    @Size(max = 256)
    @Column(name = "title", length = 256, nullable = true)
    private String title;

    @Size(max = 256)
    @Column(name = "department", length = 256, nullable = true)
    private String department;

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

    @Size(max = 256)
    @Column(name = "displayname", length = 256, nullable = true)
    private String displayname;

    @Id
    @Size(max = 256)
    @Column(name = "cn", length = 256, nullable = false)
    private String cn;

    @JsonIgnore
    @Size(max = 256)
    @Column(name = "unicodepwd", length = 256, nullable = true)
    private String unicodepwd;

    @Size(max = 256)
    @Column(name = "dn", length = 256, nullable = false)
    private String dn;

    @Size(max = 256)
    @Column(name = "pager", length = 256, nullable = true)
    private String pager;

    @JsonIgnore
    @Size(max = 256)
    @Column(name = "unicodepwd_hash", length = 256, nullable = true)
    private String unicodepwdHash;

    @JsonIgnore
    @Size(max = 256)
    @Column(name = "syncattrs_hash", length = 256, nullable = false)
    private String syncattrsHash;

    @Size(max = 256)
    @Column(name = "dn_hash", length = 256, nullable = false)
    private String dnHash;

    @JsonIgnore
    @Column(name = "whencreated", nullable = false)
    private java.sql.Timestamp whencreated;

    @JsonIgnore
    @Column(name = "whenchanged", nullable = false)
    private java.sql.Timestamp whenchanged;

    @JsonIgnore
    @Column(name = "pwdlastset", nullable = true)
    private java.sql.Timestamp pwdlastset;

    @JsonIgnore
    @Column(name = "objectclass", length = 256, nullable = false)
    private String objectclass;

    @JsonIgnore
    @Column(name = "objectcategory", length = 256, nullable = false)
    private String objectcategory;

    @Size(max = 2048)
    @Column(name = "memberof", length = 256, nullable = true)
    private String memberOf;

    @JsonIgnore
    @Column(name = "mail", length = 256, nullable = true)
    private String mail;

}

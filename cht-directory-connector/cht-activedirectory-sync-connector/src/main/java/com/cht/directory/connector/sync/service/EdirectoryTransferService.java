package com.cht.directory.connector.sync.service;

import com.cht.directory.connector.security.KmsClientService;
import com.cht.directory.connector.security.utils.DataUtils;
import com.cht.directory.connector.service.ActiveDirectoryService;
import com.cht.directory.connector.service.EdirectoryService;
import com.cht.directory.connector.service.utils.DateTimeUtils;
import com.cht.directory.connector.sync.config.ServiceParameters;
import com.cht.directory.connector.type.ADGroupType;
import com.cht.directory.connector.type.ADUserParameters;
import com.cht.directory.domain.ConnectorSpaceAdOrganizationalUnitDetails;
import com.cht.directory.domain.ConnectorSpaceAdPersonDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novell.security.nmas.mgmt.NMASPwdMgr;
import com.novell.security.nmas.mgmt.PwdJLdapTransport;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.naming.Name;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
@Slf4j
public class EdirectoryTransferService {

    // person
    private static final String[] EDIR_ORGANIZATIONPERSON_ATTRIBUTES = new String[] { "employeeID",
            "userAccountControl", "userParameters", "sn", "ou", "extensionAttribute1",
            "extensionAttribute2", "accountExpires", "userPrincipalName", "sAMAccountName", "title",
            "department", "extensionAttribute10", "extensionAttribute11", "extensionAttribute12",
            "extensionAttribute13", "extensionAttribute14", "extensionAttribute15", "displayName",
            "cn", "objectGUID", "memberOf", "homeMDB", "objectClass", "objectCategory",
            "whenCreated", "whenChanged", "pwdLastSet", "mail", "pager" };

    // ou
    private static final String[] EDIR_ORGANIZATIONUNIT_ATTRIBUTES = new String[] { "objectguid", "ou",
            "dn", "displayName", "objectClass", "objectCategory", "whenCreated", "whenChanged" };

    private static final String[] ORGANIZATIONPERSON_ATTRIBUTES = new String[] { "employeeNumber",
            "loginDisabled", "employeeStatus", "sn", "ou", "taxOutAccountStatus",
            "taxMailEnableStatus", "loginExpirationTime", "title", "taxDepartment", "fullName",
            "cn", "GUID", "modifyTimestamp", "pwdChangedTime", "createTimestamp", "mail" };

    private static final String[] ORGANIZATIONUNIT_ATTRIBUTES = new String[] { "GUID", "fullName",
            "ou", "dn", "createTimestamp", "modifyTimestamp" };

    private static ObjectMapper mapper = new ObjectMapper();

    @Value("${activedirectory.service.domaindn}")
    private String domaindn;

    @Value("${activedirectory.service.upndomain}")
    private String upndomain;

    @Value("${activedirectory.service.mofdn}")
    private String mofdn;

    @Value("${edirectory.service.excludepersonfile:}")
    private String excludePersonFile;

    @Autowired
    private ServiceParameters serviceParameters;

    @Autowired
    private KmsClientService kmsClientService;

    // 這個是專門拿來向edir取密碼用的
    @Autowired(required = false)
    private com.novell.ldap.LDAPConnection ldapConnection;

    // 這個service用的是ldap2 connection pool
    @Autowired(required = false)
    private EdirectoryService edirectoryService;

    @Autowired
    private ActiveDirectoryService activeDirectoryService;

    public void doScanDirectory() throws Exception {

        try {

            log.info("ServiceParameters : {}", serviceParameters);

            if (StringUtils.equalsIgnoreCase(serviceParameters.getPlaceholder(), "inner")) {

                if (ldapConnection != null) {

                    log.info("{} - {} - {}", ldapConnection.getHost(), ldapConnection.getPort(),
                            serviceParameters);

                    if (serviceParameters.isDoscan()) {

                        scanOrganizationUnit();
                        scanOrganizationPerson();
                    }
                }
            }
        } catch (Exception ex) {

            String stackTrace = ExceptionUtils.getStackTrace(ex);

            log.error("{}", stackTrace);
        }
    }

    /**
     * Polling eDirectory 組織資料
     * 
     * @throws Exception
     */
    public void scanOrganizationUnit() throws Exception {

        // 可根據修改時間與指定ou來篩選ou(先查ad上的dn list)
        SearchResult searchResult = activeDirectoryService.searchOrganizationUnit(
                serviceParameters.getBasedn(), serviceParameters.getModifytimestamp(),
                ORGANIZATIONUNIT_ATTRIBUTES);

        log.info("{}", searchResult);

        List<String> dnList = new ArrayList<>();

        // 要加這個，否則一筆ad cn都沒有時db會抓不出資料
        dnList.add("");
        for (SearchResultEntry entry : searchResult.getSearchEntries()) {

            Attribute attr = entry.getAttribute("dn");

            if (null != attr)
                dnList.add(attr.getValue());
        }

        // 再查edirectory上的ou資料
        searchResult = edirectoryService.searchOrganizationUnit(
                serviceParameters.getEdirbasedn(), serviceParameters.getModifytimestamp(),
                EDIR_ORGANIZATIONUNIT_ATTRIBUTES);

        String universalGroupMailEnabled = "1";

        if (StringUtils.containsIgnoreCase(serviceParameters.getBasedn(), "ELROOT"))
            universalGroupMailEnabled = "0";

        for (SearchResultEntry entry : searchResult.getSearchEntries()) {

            try {

                log.info("Get Entry : {}", entry.getDN());

                // =============================================================================
                // 新增組織資料

                ConnectorSpaceAdOrganizationalUnitDetails connectorSpaceAdOrganizationalUnitDetails =
                        new ConnectorSpaceAdOrganizationalUnitDetails();

                String dn = entry.getDN().toUpperCase().replace("O=", "OU=") + "," + domaindn;

                // AD 上已有這個ou
                if (dnList.contains(dn)) {
                    continue;
                }

                connectorSpaceAdOrganizationalUnitDetails.setDn(dn);
                connectorSpaceAdOrganizationalUnitDetails.setDnHash(DataUtils.hash(dn));
                connectorSpaceAdOrganizationalUnitDetails
                        .setPlaceholder(serviceParameters.getPlaceholder());

                for (Attribute attr : entry.getAttributes()) {

                    String attrName = attr.getName();
                    String[] attrValues = attr.needsBase64Encoding()
                            ? base64Encode(attr.getValueByteArrays())
                            : attr.getValues();
                    String attrValue = (null != attrValues && attrValues.length == 1)
                            ? attrValues[0]
                            : "";

                    if (StringUtils.equalsIgnoreCase(attrName, "GUID")) {

                        connectorSpaceAdOrganizationalUnitDetails
                                .setObjectguid(DataUtils.byte2Hex(attr.getValueByteArray()));
                    } else if (StringUtils.equalsIgnoreCase(attrName, "ou")) {

                        connectorSpaceAdOrganizationalUnitDetails.setOu(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "fullName")) {

                        connectorSpaceAdOrganizationalUnitDetails.setDisplayname(attr.getValue());
                    } else if (StringUtils.equalsIgnoreCase(attrName, "createTimestamp")) {

                        Timestamp createTimestamp = new Timestamp(attr.getValueAsDate().getTime());
                        connectorSpaceAdOrganizationalUnitDetails.setWhencreated(createTimestamp);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "modifyTimestamp")) {

                        Timestamp modifyTimestamp = new Timestamp(attr.getValueAsDate().getTime());
                        connectorSpaceAdOrganizationalUnitDetails.setWhenchanged(modifyTimestamp);
                    }

                    log.debug("Attribute => {} : {}", attrName, attrValue);
                }

                connectorSpaceAdOrganizationalUnitDetails
                        .setDisplayname(connectorSpaceAdOrganizationalUnitDetails.getOu() + (StringUtils
                                .isNotBlank(connectorSpaceAdOrganizationalUnitDetails.getDisplayname())
                                        ? connectorSpaceAdOrganizationalUnitDetails.getDisplayname()
                                        : ""));

                // connectorSpaceAdOrganizationalUnitDetails.setPlaceholder("inner");

                String detailsJson = mapper.writeValueAsString(connectorSpaceAdOrganizationalUnitDetails);
                log.info("{}", detailsJson);

                connectorSpaceAdOrganizationalUnitDetails.setSyncattrsHash(DataUtils.hash(detailsJson));

                // 在 add 之前會先確認 ad 上無相同 dn 物件
                LDAPResult result = activeDirectoryService.addOrganizationUnit(
                        connectorSpaceAdOrganizationalUnitDetails.getDn(),
                        connectorSpaceAdOrganizationalUnitDetails.getOu(),
                        connectorSpaceAdOrganizationalUnitDetails.getDisplayname());

            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);

                log.error("{}", stackTrace);
            }
        }
    }

    /**
     * Polling eDirectory 帳號資料
     * 
     * @throws Exception
     */
    public void scanOrganizationPerson() throws Exception {

        // 先查ad上的cn list
        SearchResult searchResult = activeDirectoryService.searchOrganizationPerson(
                serviceParameters.getBasedn(), null, ORGANIZATIONPERSON_ATTRIBUTES);

        log.info("{}", searchResult);

        List<String> cnList = new ArrayList<>();

        // 要加這個，否則一筆ad cn都沒有時db會抓不出資料
        cnList.add("");
        for (SearchResultEntry entry : searchResult.getSearchEntries()) {

            Attribute attr = entry.getAttribute("cn");

            if (null != attr)
                cnList.add(attr.getValue());
        }

        // 再查edirectory上的person資料
        searchResult = edirectoryService.searchOrganizationPerson(
                serviceParameters.getEdirbasedn(), serviceParameters.getModifytimestamp(),
                EDIR_ORGANIZATIONPERSON_ATTRIBUTES);

        for (SearchResultEntry entry : searchResult.getSearchEntries()) {

            try {

                log.info("Get Entry : {}", entry.getDN());

                if (StringUtils.contains(entry.getDN(), "_YBDTEMP")) {
                    continue;
                }

                ConnectorSpaceAdPersonDetails connectorSpaceAdPersonDetails = new ConnectorSpaceAdPersonDetails();

                String dn = entry.getDN().toUpperCase().replace("O=", "OU=") + "," + domaindn;

                connectorSpaceAdPersonDetails.setDn(dn);
                connectorSpaceAdPersonDetails.setDnHash(DataUtils.hash(dn));
                connectorSpaceAdPersonDetails.setUseraccountcontrol(512);
                connectorSpaceAdPersonDetails.setUserparameters(ADUserParameters.ACTIVE.name());
                connectorSpaceAdPersonDetails
                        .setAccountexpires(DateTimeUtils.msADtoTimestamp("9223372036854775807"));
                connectorSpaceAdPersonDetails.setPlaceholder(serviceParameters.getPlaceholder());

                for (String attrName : EDIR_ORGANIZATIONPERSON_ATTRIBUTES) {

                    Attribute attr = entry.getAttribute(attrName);
                    String[] attrValues = null;
                    String attrValue = null;

                    if (null != attr) {
                        attrValues = attr.getValues();
                        attrValue = (null != attrValues && attrValues.length == 1) ? attrValues[0]
                                : (null != attrValues && attrValues.length > 1)
                                        ? String.join(";", attr.getValues())
                                        : "";
                    }

                    if (StringUtils.equalsIgnoreCase(attrName, "GUID")) {

                        if (null != attr)
                            connectorSpaceAdPersonDetails
                                    .setObjectguid(DataUtils.byte2Hex(attr.getValueByteArray()));
                    } else if (StringUtils.equalsIgnoreCase(attrName, "employeeNumber")) {

                        connectorSpaceAdPersonDetails.setEmployeeid(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "loginDisabled")) {

                        if (null != attr) {
                            if (Boolean.valueOf(attrValue))
                                connectorSpaceAdPersonDetails
                                        .setUseraccountcontrol(ADUserParameters.SUSPEND.getCode());
                            else
                                connectorSpaceAdPersonDetails
                                        .setUseraccountcontrol(ADUserParameters.ACTIVE.getCode());

                            connectorSpaceAdPersonDetails.setUserparameters(ADUserParameters
                                    .getByCode(connectorSpaceAdPersonDetails.getUseraccountcontrol())
                                    .name());
                        }
                    } else if (StringUtils.equalsIgnoreCase(attrName, "employeeStatus")) {

                        if (null != attr) {
                            connectorSpaceAdPersonDetails.setUserparameters(attrValue);
                            if (StringUtils.equalsIgnoreCase(attrValue, "SUSPEND"))
                                connectorSpaceAdPersonDetails
                                        .setUseraccountcontrol(ADUserParameters.SUSPEND.getCode());
                            else if (StringUtils.equalsIgnoreCase(attrValue, "DELETE"))
                                connectorSpaceAdPersonDetails
                                        .setUseraccountcontrol(ADUserParameters.DELETE.getCode());
                        }
                    } else if (StringUtils.equalsIgnoreCase(attrName, "sn")) {

                        connectorSpaceAdPersonDetails.setSn(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "ou")) {

                        connectorSpaceAdPersonDetails.setOu(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "taxOutAccountStatus")) {

                        connectorSpaceAdPersonDetails.setExtensionattribute1(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "taxMailEnableStatus")) {

                        connectorSpaceAdPersonDetails.setExtensionattribute2(attrValue);
                        // - 2020/07/14 MAIL2000 Support
                        // 將LDAP.taxMailEnableStatus讀到的值寫入metaverse_person_details.pager
                        connectorSpaceAdPersonDetails.setPager(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "loginExpirationTime")) {

                        if (StringUtils.isNotEmpty(attrValue)
                                && !StringUtils.equals(attrValue, "0")) {
                            Timestamp loginExpirationTime = new Timestamp(
                                    attr.getValueAsDate().getTime());
                            connectorSpaceAdPersonDetails.setAccountexpires(loginExpirationTime);
                        }
                    } else if (StringUtils.equalsIgnoreCase(attrName, "title")) {

                        connectorSpaceAdPersonDetails.setTitle(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "taxDepartment")) {

                        String department = attrValue;

                        connectorSpaceAdPersonDetails.setDepartment(department);
                        connectorSpaceAdPersonDetails.setExtensionattribute10(department);
                        connectorSpaceAdPersonDetails.setExtensionattribute11(department);
                        connectorSpaceAdPersonDetails.setExtensionattribute12(department);
                        connectorSpaceAdPersonDetails.setExtensionattribute13(department);
                        connectorSpaceAdPersonDetails.setExtensionattribute14(department);
                        connectorSpaceAdPersonDetails.setExtensionattribute15(department);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "fullName")) {

                        connectorSpaceAdPersonDetails.setDisplayname(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "cn")) {

                        connectorSpaceAdPersonDetails.setCn(attrValue);
                        connectorSpaceAdPersonDetails.setSamaccountname(attrValue);

                        connectorSpaceAdPersonDetails
                                .setUserprincipalname(connectorSpaceAdPersonDetails.getCn() + upndomain);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "createTimestamp")) {

                        if (null != attr) {
                            Timestamp createTimestamp = new Timestamp(
                                    attr.getValueAsDate().getTime());
                            connectorSpaceAdPersonDetails.setWhencreated(createTimestamp);
                        } else
                            connectorSpaceAdPersonDetails.setWhencreated(null);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "modifyTimestamp")) {

                        if (null != attr) {
                            Timestamp modifyTimestamp = new Timestamp(
                                    attr.getValueAsDate().getTime());
                            connectorSpaceAdPersonDetails.setWhenchanged(modifyTimestamp);
                        } else
                            connectorSpaceAdPersonDetails.setWhenchanged(null);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "pwdChangedTime")) {

                        if (null != attr) {
                            Timestamp pwdChangedTime = new Timestamp(
                                    attr.getValueAsDate().getTime());
                            connectorSpaceAdPersonDetails.setPwdlastset(pwdChangedTime);
                        } else
                            connectorSpaceAdPersonDetails.setPwdlastset(null);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "mail")) {

                        connectorSpaceAdPersonDetails.setMail(attrValue);
                    }

                    log.debug("Attribute => {} : {}", attrName, attrValue);
                }

                // 1213 : 釐正 taxDepartment 缺少 OU 的狀況
                if (StringUtils.indexOf(connectorSpaceAdPersonDetails.getDepartment(),
                        connectorSpaceAdPersonDetails.getOu()) == -1) {

                    String department = connectorSpaceAdPersonDetails.getOu()
                            + connectorSpaceAdPersonDetails.getDepartment();

                    connectorSpaceAdPersonDetails.setDepartment(department);
                    connectorSpaceAdPersonDetails.setExtensionattribute10(department);
                    connectorSpaceAdPersonDetails.setExtensionattribute11(department);
                    connectorSpaceAdPersonDetails.setExtensionattribute12(department);
                    connectorSpaceAdPersonDetails.setExtensionattribute13(department);
                    connectorSpaceAdPersonDetails.setExtensionattribute14(department);
                    connectorSpaceAdPersonDetails.setExtensionattribute15(department);
                }

                try {

                    byte[] unicodePwd = getUserUnicodePassword(entry.getDN());

                    String unicodePwdHash = DataUtils.hash(unicodePwd);

                    String encryptUnicodePwd = kmsClientService
                            .encrypt(connectorSpaceAdPersonDetails.getCn(), unicodePwd);

                    connectorSpaceAdPersonDetails.setUnicodepwd(encryptUnicodePwd);
                    connectorSpaceAdPersonDetails.setUnicodepwdHash(unicodePwdHash);
                } catch (Exception ex) {

                    log.error(ExceptionUtils.getStackTrace(ex));
                }

                connectorSpaceAdPersonDetails.setDisplayname(connectorSpaceAdPersonDetails.getSn() + "("
                        + connectorSpaceAdPersonDetails.getCn() + ")");

//                ArrayList memberOf = new ArrayList();
//                memberOf.add(getGroupDn(connectorSpaceAdPersonDetails.getDn(),
//                        ADGroupType.UNIVERSAL_SECURITY_GROUP));
//
//                if (StringUtils.contains(connectorSpaceAdPersonDetails.getDn(),
//                        "OU=" + MOFType.NTBT.getType() + "ROOT")) {
//                    memberOf.add(getGroupDn(connectorSpaceAdPersonDetails.getDn(),
//                            ADGroupType.GLOBAL_SECURITY_GROUP));
//                }
//
//                Collections.sort(memberOf);
//
//                connectorSpaceAdPersonDetails.setMemberOf(StringUtils.join(memberOf, ";"));
//                connectorSpaceAdPersonDetails.setPlaceholder("inner");

                String detailsJson = mapper.writeValueAsString(connectorSpaceAdPersonDetails);
                log.info("{}", detailsJson);

                connectorSpaceAdPersonDetails.setSyncattrsHash(DataUtils.hash(detailsJson));

                // 如果 cn 已經存在於 ad 上，則不處理
                if (!cnList.contains(connectorSpaceAdPersonDetails.getCn())) {

                    byte[] unicodePwd = kmsClientService.decrypt(connectorSpaceAdPersonDetails.getSamaccountname(),
                            connectorSpaceAdPersonDetails.getUnicodepwd());

                    LDAPResult result = activeDirectoryService.addOrganizationPerson(
                            connectorSpaceAdPersonDetails.getDn(),
                            connectorSpaceAdPersonDetails.getCn(),
                            connectorSpaceAdPersonDetails.getEmployeeid(),
                            connectorSpaceAdPersonDetails.getSn(),
                            connectorSpaceAdPersonDetails.getOu(),
                            connectorSpaceAdPersonDetails.getExtensionattribute1(),
                            connectorSpaceAdPersonDetails.getExtensionattribute2(),
                            connectorSpaceAdPersonDetails.getAccountexpires(),
                            connectorSpaceAdPersonDetails.getUserprincipalname(),
                            connectorSpaceAdPersonDetails.getSamaccountname(),
                            connectorSpaceAdPersonDetails.getTitle(),
                            connectorSpaceAdPersonDetails.getDepartment(),
                            connectorSpaceAdPersonDetails.getExtensionattribute10(),
                            connectorSpaceAdPersonDetails.getExtensionattribute11(),
                            connectorSpaceAdPersonDetails.getExtensionattribute12(),
                            connectorSpaceAdPersonDetails.getExtensionattribute13(),
                            connectorSpaceAdPersonDetails.getExtensionattribute14(),
                            connectorSpaceAdPersonDetails.getExtensionattribute15(),
                            connectorSpaceAdPersonDetails.getDisplayname(), unicodePwd,
                            connectorSpaceAdPersonDetails.getUseraccountcontrol(),
                            connectorSpaceAdPersonDetails.getUserparameters(),
                            connectorSpaceAdPersonDetails.getPager());
                }
            } catch (Exception ex) {

                log.error(ExceptionUtils.getStackTrace(ex));
            }
        }
    }

    public byte[] getUserUnicodePassword(String dn) throws Exception {

        NMASPwdMgr pwdMgr = new NMASPwdMgr(new PwdJLdapTransport(ldapConnection));

        String entryPassword = pwdMgr.getPwd("", dn);

        log.debug("user password {} : {}", dn, entryPassword);

        return DataUtils.toQuoteUnicode(entryPassword);
    }

    private String getGroupDn(String dn, ADGroupType adGroupType) throws Exception {

        LdapName ldapName = new LdapName(dn);

        for (int index = 0; index < ldapName.getRdns().size(); index++) {

            Rdn rdn = ldapName.getRdn(index);
            if (StringUtils.equalsIgnoreCase("CN", rdn.getType())) {

                ldapName.remove(index);
            }
        }

        Name ouSuffix = ldapName.getSuffix(ldapName.size() - 1);

        Rdn rdn = new Rdn(ouSuffix.toString());

        ldapName.add("CN=" + ((String) rdn.getValue() + adGroupType.getSuffix()));

        return ldapName.toString();
    }

    private String getParentGroupDn(String dn, ADGroupType adGroupType) throws Exception {

        LdapName ldapName = new LdapName(dn);
        ldapName.remove(ldapName.size() - 1);

        if (StringUtils.equalsIgnoreCase(ldapName.toString(), mofdn))
            return "";

        return getGroupDn(ldapName.toString(), adGroupType);
    }

    private static String[] base64Encode(byte[][] data) {
        String[] encoded = new String[data.length];
        for (int i = 0, size = encoded.length; i < size; i++) {
            encoded[i] = Base64.getEncoder().encodeToString(data[i]);
        }

        return encoded;
    }

    private static byte[][] base64Decode(String[] data) {
        byte[][] decoded = new byte[data.length][];
        for (int i = 0, size = decoded.length; i < size; i++) {
            decoded[i] = Base64.getDecoder().decode(data[i]);
        }

        return decoded;
    }
}

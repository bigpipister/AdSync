package com.cht.directory.connector.sync.service;

import com.cht.directory.connector.service.EventLogsService;
import com.cht.directory.connector.service.StatusLogsService;
import com.cht.directory.connector.sync.config.ServiceParameters;
import com.cht.directory.connector.security.utils.DataUtils;
import com.cht.directory.connector.service.ActiveDirectoryService;
import com.cht.directory.connector.service.utils.DateTimeUtils;
import com.cht.directory.connector.type.PlaceHolder;
import com.cht.directory.domain.*;
import com.cht.directory.repository.*;
import com.cht.org.apache.commons.io.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import com.cht.directory.connector.type.ADGroupType;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.*;

@Component
@Slf4j
public class ActivedirectoryScanService {

    // person
    private static final String[] ORGANIZATIONPERSON_ATTRIBUTES = new String[] { "employeeID",
            "userAccountControl", "userParameters", "sn", "ou", "extensionAttribute1",
            "extensionAttribute2", "accountExpires", "userPrincipalName", "sAMAccountName", "title",
            "department", "extensionAttribute10", "extensionAttribute11", "extensionAttribute12",
            "extensionAttribute13", "extensionAttribute14", "extensionAttribute15", "displayName",
            "cn", "objectGUID", "memberOf", "homeMDB", "objectClass", "objectCategory",
            "whenCreated", "whenChanged", "pwdLastSet", "mail", "pager" };

    // ou
    private static final String[] ORGANIZATIONUNIT_ATTRIBUTES = new String[] { "objectguid", "ou",
            "dn", "displayName", "objectClass", "objectCategory", "whenCreated", "whenChanged" };

    // group
    private static final String[] ORGANIZATIONGROUP_ATTRIBUTES = new String[] { "sAMAccountName",
            "name", "displayName", "grouptype", "extensionAttribute10", "extensionAttribute11",
            "extensionAttribute12", "extensionAttribute13", "extensionAttribute14",
            "extensionAttribute15", "cn", "dn", "objectGUID", "memberOf", "mail", "objectClass",
            "objectCategory", "whenCreated", "whenChanged" };

    private static ObjectMapper mapper = new ObjectMapper();

    @Value("${activedirectory.service.domaindn}")
    private String domaindn;

    @Value("${activedirectory.service.upndomain}")
    private String upndomain;

    @Value("${activedirectory.service.indomaindn:}") // 用來外網時將內網 domain dn 換成外網 domain dn
    private String inDomaindn;

    @Value("${activedirectory.service.inupndomain:}") // 用來外網時將內網 upndomain 換成外網 upndomain
    private String inUpndomain;

    @Value("${activedirectory.service.excludepersonfile:}")
    private String excludePersonFile;

    @Value("${activedirectory.service.excludegroupfile:}")
    private String excludeGroupFile;

    @Autowired
    private LDAPConnectionPool connectionPool;

    @Autowired
    private ActiveDirectoryService activeDirectoryService;

    @Autowired
    private ConnectorSpaceAdGroupDetailsRepository connectorSpaceAdGroupDetailsRepository;

    @Autowired
    private ConnectorSpaceAdOrganizationalUnitDetailsRepository connectorSpaceAdOrganizationalUnitDetailsRepository;

    @Autowired
    private ConnectorSpaceAdPersonDetailsRepository connectorSpaceAdPersonDetailsRepository;

    @Autowired
    private ConnectorSpaceAdGroupSyncDetailsRepository connectorSpaceAdGroupSyncDetailsRepository;

    @Autowired
    private ConnectorSpaceAdOrganizationalUnitSyncDetailsRepository connectorSpaceAdOrganizationalUnitSyncDetailsRepository;

    @Autowired
    private ConnectorSpaceAdPersonSyncDetailsRepository connectorSpaceAdPersonSyncDetailsRepository;

    @Autowired
    private ConnectorSpaceAdGroupExternalDetailsRepository connectorSpaceAdGroupExternalDetailsRepository;

    @Autowired
    private ConnectorSpaceAdOrganizationalUnitExternalDetailsRepository connectorSpaceAdOrganizationalUnitExternalDetailsRepository;

    @Autowired
    private ConnectorSpaceAdPersonExternalDetailsRepository connectorSpaceAdPersonExternalDetailsRepository;

    @Autowired
    private ServiceParameters serviceParameters;

    @Autowired
    private StatusLogsService statusLogsService;

    @Autowired
    private EventLogsService eventLogsService;

    private String health = "Y";

    public void doScanDirectory() throws Exception {

        log.info("ServiceParameters : {}", serviceParameters);

        Boolean running = true;
        health = "Y";
        Date date = new Date();
        Timestamp startDateTime = new Timestamp(date.getTime());
        long startTime = System.currentTimeMillis(); // 取得開始時間 (毫秒)
        statusLogsService.record("scan", serviceParameters.getBasedn(), "擷取AD資料",
                startDateTime, running, health, 0);

        try {
            eventLogsService.del("scan", serviceParameters.getBasedn());

            log.info("{} - {} - {}", connectionPool.getConnection().getConnectedAddress(),
                    connectionPool.getConnection().getConnectedPort(), serviceParameters);

            if (serviceParameters.isDoscan()) {

                scanOrganizationUnit();
                scanOrganizationGroup();
                scanOrganizationPerson();
            }

            // 將交換到外網的內網資料，其內網 domain 置換為外網 domain
            if (StringUtils.equalsIgnoreCase(serviceParameters.getPlaceholder(), "external") &&
                    serviceParameters.isDotrans()) {

                log.info("do external sync domain modification");
                scanExternalOrganizationUnit();
                scanExternalOrganizationGroup();
                scanExternalOrganizationPerson();
            }
        } catch (Exception ex) {

            String stackTrace = ExceptionUtils.getStackTrace(ex);
            // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
            stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
            log.error("{}", stackTrace);
            eventLogsService.record("scan", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
            health = "N";
        }

        running = false;
        long endTime = System.currentTimeMillis(); // 取得結束時間 (毫秒)
        long duration = (endTime - startTime) / 1000; // 轉換為秒數
        statusLogsService.record("scan", serviceParameters.getBasedn(), "擷取AD資料",
                startDateTime, running, health, duration);
    }

    /**
     * Polling ActiveDirectory 組織資料
     * 
     * @throws Exception
     */
    public void scanOrganizationUnit() throws Exception {

        // 可根據修改時間與指定ou來篩選ou
        SearchResult searchResult = activeDirectoryService.searchOrganizationUnit(
                serviceParameters.getBasedn(), serviceParameters.getModifytimestamp(),
                ORGANIZATIONUNIT_ATTRIBUTES);

        for (SearchResultEntry entry : searchResult.getSearchEntries()) {

            try {

                log.info("Entry's dn : {}", entry.getDN());

                ConnectorSpaceAdOrganizationalUnitDetails connectorSpaceAdOrganizationalUnitDetails =
                        new ConnectorSpaceAdOrganizationalUnitDetails();

                // 先從 db 拿出既有 object 的資料(如果有的話)
                Optional<ConnectorSpaceAdOrganizationalUnitDetails> connectorSpaceAdOrganizationalUnitDetailsOptional =
                        connectorSpaceAdOrganizationalUnitDetailsRepository.findByDnAndPlaceholder(entry.getDN(), serviceParameters.getPlaceholder());

                if (connectorSpaceAdOrganizationalUnitDetailsOptional.isPresent())
                    connectorSpaceAdOrganizationalUnitDetails = connectorSpaceAdOrganizationalUnitDetailsOptional.get();

                connectorSpaceAdOrganizationalUnitDetails.setDn(entry.getDN());
                connectorSpaceAdOrganizationalUnitDetails.setDnHash(DataUtils.hash(entry.getDN()));

                // 處理屬性
                for (String attrName : ORGANIZATIONUNIT_ATTRIBUTES) {

                    Attribute attr = entry.getAttribute(attrName);
                    String[] attrValues = null;
                    String attrValue = null;

                    // 大部份的屬性值都可以先做這種處理
                    if (null != attr) {
                        attrValues = attr.getValues();
                        // 空字串、單值、;串接多值
                        attrValue = (null != attrValues && attrValues.length == 1) ? attrValues[0]
                                : (null != attrValues && attrValues.length > 1)
                                        ? String.join(";", attr.getValues())
                                        : "";
                    }

                    if (StringUtils.equalsIgnoreCase(attrName, "objectguid")) {

                        if (null != attr)
                            connectorSpaceAdOrganizationalUnitDetails
                                    .setObjectguid(DataUtils.byte2Hex(attr.getValueByteArray()));
                        else
                            connectorSpaceAdOrganizationalUnitDetails.setObjectguid(null);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "ou")) {

                        connectorSpaceAdOrganizationalUnitDetails.setOu(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "displayName")) {

                        connectorSpaceAdOrganizationalUnitDetails.setDisplayname(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "objectClass")) {

                        connectorSpaceAdOrganizationalUnitDetails.setObjectclass(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "objectCategory")) {

                        connectorSpaceAdOrganizationalUnitDetails.setObjectcategory(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "whenCreated")) {

                        if (null != attr && null != attr.getValueAsDate()) {
                            Timestamp whenCreated = new Timestamp(attr.getValueAsDate().getTime());
                            connectorSpaceAdOrganizationalUnitDetails.setWhencreated(whenCreated);
                        } else
                            connectorSpaceAdOrganizationalUnitDetails.setWhencreated(null);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "whenChanged")) {

                        if (null != attr && null != attr.getValueAsDate()) {
                            Timestamp whenChanged = new Timestamp(attr.getValueAsDate().getTime());
                            connectorSpaceAdOrganizationalUnitDetails.setWhenchanged(whenChanged);
                        } else
                            connectorSpaceAdOrganizationalUnitDetails.setWhenchanged(null);
                    }

                    log.debug("Attribute => {} : {}", attrName, attrValue);
                }

                connectorSpaceAdOrganizationalUnitDetails
                        .setPlaceholder(serviceParameters.getPlaceholder());

                // 這一步是為了要hash attributes用
                String detailsJson = mapper
                        .writeValueAsString(connectorSpaceAdOrganizationalUnitDetails);
                log.info("Entry's detailsJson : {}", detailsJson);

                // hash attributes 用於後續判斷該 dn 是否有屬性異動
                connectorSpaceAdOrganizationalUnitDetails
                        .setSyncattrsHash(DataUtils.hash(detailsJson));

                // key是ou跟placeholder，所以組織搬移時寫入db只會更新其dn欄位
                connectorSpaceAdOrganizationalUnitDetailsRepository
                        .save(connectorSpaceAdOrganizationalUnitDetails);
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                stackTrace = "dn: " + entry.getDN() + "\n" + stackTrace;
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("scan", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }
        // HE: 測試 exception log
        //throw new Exception("scanOrganizationUnit exception test");
    }

    /**
     * Polling ActiveDirectory 群組資料
     * 
     * @throws Exception
     */
    public void scanOrganizationGroup() throws Exception {

        // 可根據修改時間與指定ou來篩選group
        SearchResult searchResult = activeDirectoryService.searchOrganizationGroup(
                serviceParameters.getBasedn(), serviceParameters.getModifytimestamp(),
                ORGANIZATIONGROUP_ATTRIBUTES);

        for (SearchResultEntry entry : searchResult.getSearchEntries()) {

            try {

                log.info("Entry's dn : {}", entry.getDN());

                ConnectorSpaceAdGroupDetails connectorSpaceAdGroupDetails = new ConnectorSpaceAdGroupDetails();

                // 先從 db 拿出既有 object 的資料(如果有的話)
                Optional<ConnectorSpaceAdGroupDetails> connectorSpaceAdGroupDetailsOptional = connectorSpaceAdGroupDetailsRepository
                        .findByDnAndPlaceholder(entry.getDN(), serviceParameters.getPlaceholder());

                if (connectorSpaceAdGroupDetailsOptional.isPresent())
                    connectorSpaceAdGroupDetails = connectorSpaceAdGroupDetailsOptional.get();

                connectorSpaceAdGroupDetails.setDn(entry.getDN());
                connectorSpaceAdGroupDetails.setDnHash(DataUtils.hash(entry.getDN()));
                // 預設不啟用，後面依據如果 mail 不為空則啟用
                connectorSpaceAdGroupDetails.setMailEnabled("0");

                // 處理屬性
                for (String attrName : ORGANIZATIONGROUP_ATTRIBUTES) {

                    Attribute attr = entry.getAttribute(attrName);
                    String[] attrValues = null;
                    String attrValue = null;

                    // 大部份的屬性值都可以先做這種處理
                    if (null != attr) {
                        attrValues = attr.getValues();
                        attrValue = (null != attrValues && attrValues.length == 1) ? attrValues[0]
                                : (null != attrValues && attrValues.length > 1)
                                        ? String.join(";", attr.getValues())
                                        : "";
                    }

                    if (StringUtils.equalsIgnoreCase(attrName, "sAMAccountName")) {

                        connectorSpaceAdGroupDetails.setSamaccountname(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "name")) {

                        connectorSpaceAdGroupDetails.setName(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "displayName")) {

                        connectorSpaceAdGroupDetails.setDisplayname(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "grouptype")) {

                        if (null != attr)
                            // ad group 的 groupType 是 long 型態
                            connectorSpaceAdGroupDetails
                                    .setGroupType(attr.getValueAsLong().longValue());
                    } else if (StringUtils.equalsIgnoreCase(attrName, "extensionAttribute10")) {

                        connectorSpaceAdGroupDetails.setExtensionattribute10(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "extensionAttribute11")) {

                        connectorSpaceAdGroupDetails.setExtensionattribute11(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "extensionAttribute12")) {

                        connectorSpaceAdGroupDetails.setExtensionattribute12(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "extensionAttribute13")) {

                        connectorSpaceAdGroupDetails.setExtensionattribute13(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "extensionAttribute14")) {

                        connectorSpaceAdGroupDetails.setExtensionattribute14(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "extensionAttribute15")) {

                        connectorSpaceAdGroupDetails.setExtensionattribute15(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "cn")) {

                        connectorSpaceAdGroupDetails.setCn(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "objectGUID")) {

                        if (null != attr)
                            connectorSpaceAdGroupDetails
                                    .setObjectguid(DataUtils.byte2Hex(attr.getValueByteArray()));
                        else
                            connectorSpaceAdGroupDetails.setObjectguid(null);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "memberOf")) {

                        if (!ObjectUtils.isEmpty(attrValues)) {
                            List<String> list = Arrays.asList(attrValues);
                            List<String> filterList = new ArrayList<>();

                            for (String memberOf : list) {
                                // 對 memberof 成員做 basedn 的檢核
                                if (StringUtils.endsWithIgnoreCase(memberOf, serviceParameters.getBasedn())) {
                                    filterList.add(memberOf);
                                }
                            }

                            // log.info("{}-{}-{}-{}", serviceParameters.getBaseDn(), list, filterList, entry.getDN());

                            Collections.sort(filterList);
                            connectorSpaceAdGroupDetails
                                    .setMemberOf(StringUtils.join(filterList, ";"));
                        } else {
                            connectorSpaceAdGroupDetails.setMemberOf(null);
                        }
                    } else if (StringUtils.equalsIgnoreCase(attrName, "mail")) {

                        // group scan 不存 mail，額外用一個 MailEnabled, 如果 mail 不為空就標示為 1
                        connectorSpaceAdGroupDetails.setMailEnabled(
                                BooleanUtils.toInteger(StringUtils.isNotBlank(attrValue)) + "");
                    } else if (StringUtils.equalsIgnoreCase(attrName, "objectClass")) {

                        connectorSpaceAdGroupDetails.setObjectclass(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "objectCategory")) {

                        connectorSpaceAdGroupDetails.setObjectcategory(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "whenCreated")) {

                        if (null != attr && null != attr.getValueAsDate()) {
                            Timestamp whenCreated = new Timestamp(attr.getValueAsDate().getTime());
                            connectorSpaceAdGroupDetails.setWhencreated(whenCreated);
                        } else
                            connectorSpaceAdGroupDetails.setWhencreated(null);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "whenChanged")) {

                        if (null != attr && null != attr.getValueAsDate()) {
                            Timestamp whenChanged = new Timestamp(attr.getValueAsDate().getTime());
                            connectorSpaceAdGroupDetails.setWhenchanged(whenChanged);
                        } else
                            connectorSpaceAdGroupDetails.setWhenchanged(null);
                    }

                    log.debug("Attribute => {} : {}", attrName, attrValue);
                }

                // 不支援的 group type 就不處理
                if (ADGroupType.findByAdType(connectorSpaceAdGroupDetails.getGroupType()) == ADGroupType.UNKNOWN) {
                    continue;
                }

                connectorSpaceAdGroupDetails.setPlaceholder(serviceParameters.getPlaceholder());

                // 這一步是為了要hash attributes用
                String detailsJson = mapper.writeValueAsString(connectorSpaceAdGroupDetails);
                log.info("Entry's detailsJson : {}", detailsJson);

                // hash attributes 用於後續判斷該 dn 是否有屬性異動
                connectorSpaceAdGroupDetails.setSyncattrsHash(DataUtils.hash(detailsJson));

                // key是cn跟placeholder，所以組織搬移時寫入db只會更新其dn欄位
                connectorSpaceAdGroupDetailsRepository.save(connectorSpaceAdGroupDetails);

            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                stackTrace = "dn: " + entry.getDN() + "\n" + stackTrace;
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("scan", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }
        // HE: 測試 exception log
        //throw new Exception("scanOrganizationGroup exception test");
    }

    /**
     * Polling ActiveDirectory 帳號資訊
     * 
     * @throws Exception
     */
    public void scanOrganizationPerson() throws Exception {

        // 特定 person 不 scan
        List<String> personExcludeList = new ArrayList<>();

        if (StringUtils.isNotBlank(excludePersonFile)) {

            File file = new File(excludePersonFile);
            if (file.exists()) {

                try (FileReader fr = new FileReader(file)) {
                    personExcludeList = IOUtils.readLines(fr);
                }
            }
        }

        log.info("Exclude person list : {}", personExcludeList);

        // 特定 group 不塞 member
        List<String> groupExcludeList = new ArrayList<>();

        if (StringUtils.isNotBlank(excludeGroupFile)) {

            File file = new File(excludeGroupFile);
            if (file.exists()) {

                try (FileReader fr = new FileReader(file)) {
                    groupExcludeList = IOUtils.readLines(fr);
                }
            }
        }

        log.info("Exclude group list : {}", groupExcludeList);

        // 可根據修改時間與指定ou來篩選person
        SearchResult searchResult = activeDirectoryService.searchOrganizationPerson(
                serviceParameters.getBasedn(), serviceParameters.getModifytimestamp(),
                ORGANIZATIONPERSON_ATTRIBUTES);

        for (SearchResultEntry entry : searchResult.getSearchEntries()) {

            try {

                log.info("Get Entry : {}", entry.getDN());

                if (StringUtils.contains(entry.getDN(), "_YBDTEMP"))
                    continue;

                String cn = entry.getAttribute("cn").getValue();

                // 在 personExludeList 清單內的不 scan 若已存在db內的資料，則標記其 objectguid 為 -2
                if (personExcludeList.contains(cn)) {

                    log.info("Bypass Person ({}) in exclude list", cn);

                    // exclude 不處理的 person 只把 objectguid 標記為 -2，方便外網辨識
//                    if (StringUtils.equalsIgnoreCase("inner", serviceParameters.getPlaceholder())) {
                        Optional<ConnectorSpaceAdPersonDetails> connectorSpaceAdPersonDetailsOptional =
                                connectorSpaceAdPersonDetailsRepository.findByCnAndPlaceholderAndObjectguidNot(cn, serviceParameters.getPlaceholder(), "-1");

                        ConnectorSpaceAdPersonDetails connectorSpaceAdPersonDetails = null;

                        if (connectorSpaceAdPersonDetailsOptional.isPresent()) {
                            connectorSpaceAdPersonDetails = connectorSpaceAdPersonDetailsOptional.get();
                            connectorSpaceAdPersonDetails.setObjectguid("-2");
                            connectorSpaceAdPersonDetailsRepository.save(connectorSpaceAdPersonDetails);
                        }
//                    } else {
//                        // 外網直接刪除
//                        ConnectorSpaceAdPersonDetailsId connectorSpaceAdPersonDetailsId =
//                                new ConnectorSpaceAdPersonDetailsId();
//                        connectorSpaceAdPersonDetailsId.setCn(cn);
//                        connectorSpaceAdPersonDetailsId.setPlaceholder(serviceParameters.getPlaceholder());
//
//                        if (connectorSpaceAdPersonDetailsRepository.existsById(connectorSpaceAdPersonDetailsId)) {
//                            connectorSpaceAdPersonDetailsRepository.deleteById(connectorSpaceAdPersonDetailsId);
//                        }
//                    }

                    continue;
                }

                ConnectorSpaceAdPersonDetails connectorSpaceAdPersonDetails = new ConnectorSpaceAdPersonDetails();

                // 先從 db 拿出既有 object 的資料(如果有的話)
                Optional<ConnectorSpaceAdPersonDetails> connectorSpaceAdPersonDetailsOptional =
                        connectorSpaceAdPersonDetailsRepository.findByDnAndPlaceholder(entry.getDN(), serviceParameters.getPlaceholder());

                // person 異動單位雖然用dn找不到，但因為key是cn跟placeholder，因此寫入時會蓋掉db內的原dn
                if (connectorSpaceAdPersonDetailsOptional.isPresent())
                    connectorSpaceAdPersonDetails = connectorSpaceAdPersonDetailsOptional.get();

                // - 2020/07/14
                // Keep Ad Extensionattribute2 Value(exchange停用後 這個值就ad上是什麼就照實寫入db)
                String adExtensionattribute2 = connectorSpaceAdPersonDetails.getExtensionattribute2();

                connectorSpaceAdPersonDetails.setDn(entry.getDN());
                connectorSpaceAdPersonDetails.setDnHash(DataUtils.hash(entry.getDN()));
                // 先預設帳號啟用，信箱不啟用
                connectorSpaceAdPersonDetails.setUseraccountcontrol(512);
                connectorSpaceAdPersonDetails.setExtensionattribute2("0");

                // 處理屬性
                for (String attrName : ORGANIZATIONPERSON_ATTRIBUTES) {

                    Attribute attr = entry.getAttribute(attrName);
                    String[] attrValues = null;
                    String attrValue = null;

                    // 大部份的屬性值都可以先做這種處理
                    if (null != attr) {
                        attrValues = attr.getValues();
                        attrValue = (null != attrValues && attrValues.length == 1) ? attrValues[0]
                                : (null != attrValues && attrValues.length > 1)
                                        ? String.join(";", attr.getValues())
                                        : "";
                    }

                    if (StringUtils.equalsIgnoreCase(attrName, "employeeID")) {

                        connectorSpaceAdPersonDetails.setEmployeeid(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "userAccountControl")) {

                        if (null != attr)
                            connectorSpaceAdPersonDetails
                                    .setUseraccountcontrol(attr.getValueAsInteger());
                    } else if (StringUtils.equalsIgnoreCase(attrName, "userParameters")) {

                        connectorSpaceAdPersonDetails.setUserparameters(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "sn")) {

                        connectorSpaceAdPersonDetails.setSn(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "ou")) {

                        connectorSpaceAdPersonDetails.setOu(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "extensionAttribute1")) {

                        connectorSpaceAdPersonDetails.setExtensionattribute1(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "extensionAttribute2")) {

                        // 外網 scan person 信箱啟用保留原 ad 值
                        adExtensionattribute2 = attrValue;
                        if (StringUtils.equalsIgnoreCase("external", serviceParameters.getPlaceholder()))
                            connectorSpaceAdPersonDetails
                                    .setExtensionattribute2(adExtensionattribute2);
                    } else if (StringUtils.equalsIgnoreCase("inner", serviceParameters.getPlaceholder())
                            && StringUtils.equalsIgnoreCase(attrName, "homeMDB")) {

                        // 內網 scan person 信箱啟用是依據 homeMDB 屬性是否有值(這個邏輯只適用於使用 exchange 時)
                        if (StringUtils.isNotBlank(attrValue))
                            connectorSpaceAdPersonDetails.setExtensionattribute2("1");
                    } else if (StringUtils.equalsIgnoreCase(attrName, "accountExpires")) {

                        if (StringUtils.isNotEmpty(attrValue)
                                && !StringUtils.equals(attrValue, "0"))
                            connectorSpaceAdPersonDetails
                                    .setAccountexpires(DateTimeUtils.msADtoTimestamp(attrValue));
                        else
                            connectorSpaceAdPersonDetails.setAccountexpires(null);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "userPrincipalName")) {

                        connectorSpaceAdPersonDetails.setUserprincipalname(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "sAMAccountName")) {

                        connectorSpaceAdPersonDetails.setSamaccountname(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "title")) {

                        connectorSpaceAdPersonDetails.setTitle(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "department")) {

                        connectorSpaceAdPersonDetails.setDepartment(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "extensionAttribute10")) {

                        connectorSpaceAdPersonDetails.setExtensionattribute10(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "extensionAttribute11")) {

                        connectorSpaceAdPersonDetails.setExtensionattribute11(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "extensionAttribute12")) {

                        connectorSpaceAdPersonDetails.setExtensionattribute12(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "extensionAttribute13")) {

                        connectorSpaceAdPersonDetails.setExtensionattribute13(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "extensionAttribute14")) {

                        connectorSpaceAdPersonDetails.setExtensionattribute14(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "extensionAttribute15")) {

                        connectorSpaceAdPersonDetails.setExtensionattribute15(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "displayName")) {

                        connectorSpaceAdPersonDetails.setDisplayname(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "cn")) {

                        connectorSpaceAdPersonDetails.setCn(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "objectGUID")) {

                        if (null != attr)
                            connectorSpaceAdPersonDetails
                                    .setObjectguid(DataUtils.byte2Hex(attr.getValueByteArray()));
                    } else if (StringUtils.equalsIgnoreCase(attrName, "memberOf")) {

                        if (!ObjectUtils.isEmpty(attrValues)) {
                            List<String> list = Arrays.asList(attrValues);
                            List<String> filterList = new ArrayList<>();

                            for (String memberOf : list) {

                                String groupCn = "";
                                LdapName ln = new LdapName(memberOf);

                                // 截出 memberOf 的 group cn
                                for(Rdn rdn : ln.getRdns()) {
                                    if(rdn.getType().equalsIgnoreCase("CN")) {
                                        groupCn=(String)rdn.getValue();
                                        break;
                                    }
                                }

                                // 排除掉在 groupExcludeList 與不屬 baseDN 的 group
                                if (StringUtils.endsWithIgnoreCase(memberOf, serviceParameters.getBasedn()))
                                    if (!groupExcludeList.contains(groupCn))
                                        filterList.add(memberOf);
                                    else
                                        log.info("Bypass group cn ({}) in exclude list", groupCn);
                            }

                            // log.info("{}-{}-{}-{}", serviceParameters.getBaseDn(), list, filterList, entry.getDN());

                            Collections.sort(filterList);
                            connectorSpaceAdPersonDetails.setMemberOf(StringUtils.join(filterList, ";"));
                        } else {
                            connectorSpaceAdPersonDetails.setMemberOf(null);
                        }
                    } else if (StringUtils.equalsIgnoreCase(attrName, "objectClass")) {

                        connectorSpaceAdPersonDetails.setObjectclass(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "objectCategory")) {

                        connectorSpaceAdPersonDetails.setObjectcategory(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "whenCreated")) {

                        if (null != attr && null != attr.getValueAsDate()) {
                            Timestamp whenCreated = new Timestamp(attr.getValueAsDate().getTime());
                            connectorSpaceAdPersonDetails.setWhencreated(whenCreated);
                        } else
                            connectorSpaceAdPersonDetails.setWhencreated(null);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "whenChanged")) {

                        if (null != attr && null != attr.getValueAsDate()) {
                            Timestamp whenChanged = new Timestamp(attr.getValueAsDate().getTime());
                            connectorSpaceAdPersonDetails.setWhenchanged(whenChanged);
                        } else
                            connectorSpaceAdPersonDetails.setWhenchanged(null);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "pwdLastSet")) {

                        // 2019/06/02 - 為避免 AD 同步時間差，導致登入時間讀到舊的，此欄位改由 password filter 控制
                        //              (比方改密碼落在第二台ad，filter 攔截到寫入了 db，但第一台 ad 還未同步就被 scan 到舊的 pwdLastSet)
                        if (StringUtils.isNotEmpty(attrValue) && !StringUtils.equals(attrValue, "0")) {
                            // 只對 pwdLastSet db 沒有值的會先塞
                            if (ObjectUtils.isEmpty(connectorSpaceAdPersonDetails.getPwdlastset()))

                                connectorSpaceAdPersonDetails.setPwdlastset(DateTimeUtils.msADtoTimestamp(attrValue));
                        } else
                            // 新建的 person 的 pwdlastset 會是 null ，變更過密碼後才會有值
                            connectorSpaceAdPersonDetails.setPwdlastset(null);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "mail")) {

                        connectorSpaceAdPersonDetails.setMail(attrValue);
                    } else if (StringUtils.equalsIgnoreCase(attrName, "pager")) {

                        connectorSpaceAdPersonDetails.setPager(attrValue);
                    }

                    log.debug("Attribute => {} : {}", attrName, attrValue);

                }

                // - 2020/7/14
                // add MAIL2000 support
                // 內網當啟用 exchange 服務時，homeMDB 有值則 填入 extensionattribute2 = 1
                // 內網當停用 exchange 服務時，直接填入 AD 上的 extensionattribute2 值
                if (StringUtils.equalsIgnoreCase("inner", serviceParameters.getPlaceholder())
                        && StringUtils.equalsIgnoreCase("false", serviceParameters.getExchangeServiceEnabled())) {
                    // 不管上面怎麼塞，這裡一慮保留ad上的值
                    connectorSpaceAdPersonDetails.setExtensionattribute2(adExtensionattribute2);
                }

                connectorSpaceAdPersonDetails.setPlaceholder(serviceParameters.getPlaceholder());

                // 這一步是為了要hash attributes用
                String detailsJson = mapper.writeValueAsString(connectorSpaceAdPersonDetails);
                log.info("Entry's detailsJson : {}", detailsJson);

                // hash attributes 用於後續判斷該 dn 是否有屬性異動
                connectorSpaceAdPersonDetails.setSyncattrsHash(DataUtils.hash(detailsJson));

                // key是cn跟placeholder，所以組織或人員搬移時寫入db只會更新其dn欄位
                connectorSpaceAdPersonDetailsRepository.save(connectorSpaceAdPersonDetails);
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                stackTrace = "dn: " + entry.getDN() + "\n" + stackTrace;
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("scan", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }
        // HE: 測試 exception log
        //throw new Exception("scanOrganizationPerson exception test");
    }

    /**
     * 轉換 sync ad 為外網組織資料
     *
     * @throws Exception
     */
    public void scanExternalOrganizationUnit() throws Exception {

        List<ConnectorSpaceAdOrganizationalUnitSyncDetails> connectorSpaceAdOrganizationalUnitSyncDetailsList =
                connectorSpaceAdOrganizationalUnitSyncDetailsRepository
                .findAll();

        for (ConnectorSpaceAdOrganizationalUnitSyncDetails connectorSpaceAdOrganizationalUnitSyncDetails :
                connectorSpaceAdOrganizationalUnitSyncDetailsList) {

            try {

                log.info("Get OrganizationalUnitDetails : {}", connectorSpaceAdOrganizationalUnitSyncDetails);

                ConnectorSpaceAdOrganizationalUnitExternalDetails connectorSpaceAdOrganizationalUnitExternalDetails =
                        new ConnectorSpaceAdOrganizationalUnitExternalDetails();

                BeanUtils.copyProperties(connectorSpaceAdOrganizationalUnitSyncDetails,
                        connectorSpaceAdOrganizationalUnitExternalDetails);

                connectorSpaceAdOrganizationalUnitExternalDetails.setDn(StringUtils.replace(
                        connectorSpaceAdOrganizationalUnitExternalDetails.getDn(), inDomaindn, domaindn));
                connectorSpaceAdOrganizationalUnitExternalDetails.setDnHash(
                        DataUtils.hash(connectorSpaceAdOrganizationalUnitExternalDetails.getDn()));

                connectorSpaceAdOrganizationalUnitExternalDetails
                        .setPlaceholder(PlaceHolder.EXTERNAL.name().toLowerCase());

                String detailsJson = mapper
                        .writeValueAsString(connectorSpaceAdOrganizationalUnitExternalDetails);
                log.info("{}", detailsJson);

                connectorSpaceAdOrganizationalUnitExternalDetails
                        .setSyncattrsHash(DataUtils.hash(detailsJson));

                connectorSpaceAdOrganizationalUnitExternalDetailsRepository
                        .save(connectorSpaceAdOrganizationalUnitExternalDetails);
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                stackTrace = "dn: " + connectorSpaceAdOrganizationalUnitSyncDetails.getDn() + "\n" + stackTrace;
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("scan", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }
        // HE: 測試 exception log
        //throw new Exception("scanExternalOrganizationUnit exception test");
    }

    /**
     * 轉換 sync ad 為外網群組資料
     *
     * @throws Exception
     */
    public void scanExternalOrganizationGroup() throws Exception {

        List<ConnectorSpaceAdGroupSyncDetails> connectorSpaceAdGroupSyncDetailsList = connectorSpaceAdGroupSyncDetailsRepository
                .findAll();

        for (ConnectorSpaceAdGroupSyncDetails connectorSpaceAdGroupSyncDetails : connectorSpaceAdGroupSyncDetailsList) {

            try {

                log.info("Get OrganizationalGroupDetails : {}", connectorSpaceAdGroupSyncDetails);

                ConnectorSpaceAdGroupExternalDetails connectorSpaceAdGroupExternalDetails = new ConnectorSpaceAdGroupExternalDetails();

                BeanUtils.copyProperties(connectorSpaceAdGroupSyncDetails, connectorSpaceAdGroupExternalDetails);

                connectorSpaceAdGroupExternalDetails.setDn(StringUtils
                        .replace(connectorSpaceAdGroupExternalDetails.getDn(), inDomaindn, domaindn));
                connectorSpaceAdGroupExternalDetails
                        .setDnHash(DataUtils.hash(connectorSpaceAdGroupExternalDetails.getDn()));
                connectorSpaceAdGroupExternalDetails.setMemberOf(StringUtils.replace(
                        connectorSpaceAdGroupExternalDetails.getMemberOf(), inDomaindn, domaindn));
                connectorSpaceAdGroupExternalDetails
                        .setPlaceholder(PlaceHolder.EXTERNAL.name().toLowerCase());

                String detailsJson = mapper.writeValueAsString(connectorSpaceAdGroupExternalDetails);
                log.info("{}", detailsJson);

                connectorSpaceAdGroupExternalDetails.setSyncattrsHash(DataUtils.hash(detailsJson));

                connectorSpaceAdGroupExternalDetailsRepository.save(connectorSpaceAdGroupExternalDetails);
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                stackTrace = "dn: " + connectorSpaceAdGroupSyncDetails.getDn() + "\n" + stackTrace;
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("scan", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }
        // HE: 測試 exception log
        //throw new Exception("scanExternalOrganizationGroup exception test");
    }

    /**
     * 轉換 sync ad 為外網帳號資料
     *
     * @throws Exception
     */
    public void scanExternalOrganizationPerson() throws Exception {

        List<ConnectorSpaceAdPersonSyncDetails> connectorSpaceAdPersonSyncDetailsList =
                connectorSpaceAdPersonSyncDetailsRepository.findAll();

        List<ConnectorSpaceAdPersonExternalDetails> connectorSpaceAdPersonExternalDetailsList =
                new ArrayList<>();

        for (ConnectorSpaceAdPersonSyncDetails connectorSpaceAdPersonSyncDetails : connectorSpaceAdPersonSyncDetailsList) {

            try {

                log.info("Get PersonDetails : {}", connectorSpaceAdPersonSyncDetails);

                ConnectorSpaceAdPersonExternalDetails connectorSpaceAdPersonExternalDetails =
                        new ConnectorSpaceAdPersonExternalDetails();

                BeanUtils.copyProperties(connectorSpaceAdPersonSyncDetails, connectorSpaceAdPersonExternalDetails);

                // 2020/01/14
                // 不同步到外網的啟用帳號依然會轉換，但設為停用
                // 512: active 514: suspend、delete
                if (connectorSpaceAdPersonExternalDetails.getUseraccountcontrol() == 512
                        && StringUtils.equalsIgnoreCase(connectorSpaceAdPersonExternalDetails.getExtensionattribute1(), "0")) {

                    connectorSpaceAdPersonExternalDetails.setUseraccountcontrol(514);
                    connectorSpaceAdPersonExternalDetails.setUserparameters("SUSPEND");
                }

                // - 2020/07/07 MAIL2000 Support
                // 外網connector_space_ad_person_external_details. pager部分和既有邏輯一樣，都寫"0" (不啟用信箱) 屬性外網預設為 0
                connectorSpaceAdPersonExternalDetails.setPager("0");

                connectorSpaceAdPersonExternalDetails.setDn(StringUtils
                        .replace(connectorSpaceAdPersonExternalDetails.getDn(), inDomaindn, domaindn));
                connectorSpaceAdPersonExternalDetails
                        .setDnHash(DataUtils.hash(connectorSpaceAdPersonExternalDetails.getDn()));
                connectorSpaceAdPersonExternalDetails.setUserprincipalname(
                        StringUtils.replace(connectorSpaceAdPersonExternalDetails.getUserprincipalname(),
                                inUpndomain, upndomain));
                connectorSpaceAdPersonExternalDetails.setMemberOf(StringUtils
                        .replace(connectorSpaceAdPersonSyncDetails.getMemberOf(), inDomaindn, domaindn));
                connectorSpaceAdPersonExternalDetails
                        .setPlaceholder(PlaceHolder.EXTERNAL.name().toLowerCase());

                String detailsJson = mapper.writeValueAsString(connectorSpaceAdPersonExternalDetails);
                log.info("{}", detailsJson);

                connectorSpaceAdPersonExternalDetails.setSyncattrsHash(DataUtils.hash(detailsJson));

                connectorSpaceAdPersonExternalDetailsList.add(connectorSpaceAdPersonExternalDetails);
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                stackTrace = "dn: " + connectorSpaceAdPersonSyncDetails.getDn() + "\n" + stackTrace;
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("scan", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }

        }
        connectorSpaceAdPersonExternalDetailsRepository.saveAll(connectorSpaceAdPersonExternalDetailsList);
        // HE: 測試 exception log
        //throw new Exception("scanExternalOrganizationPerson exception test");
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

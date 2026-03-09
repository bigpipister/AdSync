package com.cht.directory.connector.sync.service;

import com.cht.directory.connector.service.EventLogsService;
import com.cht.directory.connector.service.StatusLogsService;
import com.cht.directory.connector.service.error.LDAPResultCode;
import com.cht.directory.connector.sync.config.ServiceParameters;
import com.cht.directory.connector.security.utils.DataUtils;
import com.cht.directory.connector.service.ActiveDirectoryService;
import com.cht.directory.connector.service.AuditLogsService;
import com.cht.directory.connector.type.AuditLogsCategoryType;
import com.cht.directory.connector.type.AuditLogsResult;
import com.cht.directory.domain.ConnectorSpaceAdGroupDetails;
import com.cht.directory.domain.ConnectorSpaceAdPersonDetails;
import com.cht.directory.domain.ConnectorSpaceAdPersonExternalDetails;
import com.cht.directory.repository.ConnectorSpaceAdGroupDetailsRepository;
import com.cht.directory.repository.ConnectorSpaceAdPersonDetailsRepository;
import com.cht.directory.repository.ConnectorSpaceAdPersonExternalDetailsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class ActivedirectoryCleanService {

    private static final String[] ORGANIZATIONPERSON_ATTRIBUTES = new String[] { "cn" };

    private static final String[] ORGANIZATIONPERSON_ATTRIBUTES2 = new String[] { "cn",
            "userAccountControl", "userParameters" };

    private static final String[] ORGANIZATIONGROUP_ATTRIBUTES = new String[] { "cn" };

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ServiceParameters serviceParameters;

    @Autowired
    private ConnectorSpaceAdPersonExternalDetailsRepository connectorSpaceAdPersonExternalDetailsRepository;

    @Autowired
    private ConnectorSpaceAdPersonDetailsRepository connectorSpaceAdPersonDetailsRepository;

    @Autowired
    private ConnectorSpaceAdGroupDetailsRepository connectorSpaceAdGroupDetailsRepository;

    @Autowired
    private ActiveDirectoryService activeDirectoryService;

    @Autowired
    protected AuditLogsService auditLogsService;

    @Autowired
    private StatusLogsService statusLogsService;

    @Autowired
    private EventLogsService eventLogsService;

    private String health = "Y";

    public void executeClean() throws Exception {

        log.info("ServiceParameters : {}", serviceParameters);

        Boolean running = true;
        health = "Y";
        Date date = new Date();
        Timestamp startDateTime = new Timestamp(date.getTime());
        long startTime = System.currentTimeMillis(); // 取得開始時間 (毫秒)
        statusLogsService.record("clean", serviceParameters.getBasedn(), "刪除幽靈帳號",
                startDateTime, running, health, 0);

        // 先處理db(標記、刪除)
        try {
            eventLogsService.del("clean", serviceParameters.getBasedn());
            // 搜尋 AD 不存在的 user，內網資料庫標記資料為DELETE，外網資料庫則直接刪除該筆
            cleanActiveDirectoryOrganizationPerson();
            // 搜尋 AD 不存在的 group，內網資料庫標記資料為DELETE，外網資料庫則直接刪除該筆
            cleanActiveDirectoryOrganizationGroup();
        } catch (Exception ex) {

            String stackTrace = ExceptionUtils.getStackTrace(ex);
            // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
            stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
            log.error("{}", stackTrace);
            eventLogsService.record("clean", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
            health = "N";
        }

        // 再從ad刪除
        if (serviceParameters.isRemoveAduserEnabled()) {

            try {

                deleteActiveDirectoryOrganizationPerson();
                // HE: exception log 測試
                //throw new Exception("ActivedirectoryCleanService excpeiton test");
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("clean", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }

        running = false;
        long endTime = System.currentTimeMillis(); // 取得結束時間 (毫秒)
        long duration = (endTime - startTime) / 1000; // 轉換為秒數
        statusLogsService.record("clean", serviceParameters.getBasedn(), "刪除幽靈帳號",
                startDateTime, running, health, duration);
    }

    /**
     * 搜尋 AD 不存在的 user，並標記資料庫資料為DELETE(外網直接刪除)
     *
     * @throws Exception
     */
    private void cleanActiveDirectoryOrganizationPerson() throws Exception {

        log.info("Begin mark or delete db data diff from ActiveDirectory OrganizationPerson ...");

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

        List<ConnectorSpaceAdPersonDetails> connectorSpaceAdPersonDetailsList = connectorSpaceAdPersonDetailsRepository
                .findAllByCnNotInAndPlaceholderAndObjectguidNot(cnList, serviceParameters.getPlaceholder(), "-1");

        log.info("Mark or delete ActiveDirectory OrganizationPerson list size: {}", connectorSpaceAdPersonDetailsList.size());

        for (ConnectorSpaceAdPersonDetails connectorSpaceAdPersonDetails : connectorSpaceAdPersonDetailsList) {

            try {

                log.info(
                        "(Dryrun : {}) found ActiveDirectory OrganizationPerson is not existed : {}",
                        serviceParameters.isDryrun(), connectorSpaceAdPersonDetails);

                // 再確定一次 ad 裡已經沒這個 person
                SearchResult confirmSearchResult = activeDirectoryService
                        .searchOrganizationPersonByCn(serviceParameters.getBasedn(),
                                connectorSpaceAdPersonDetails.getCn(), ORGANIZATIONPERSON_ATTRIBUTES);

                log.info("{}", confirmSearchResult);

                if (confirmSearchResult.getSearchEntries().isEmpty()) {

                    log.info(" confirm ActiveDirectory OrganizationPerson is not existed : {}",
                            connectorSpaceAdPersonDetails);

                    if (!serviceParameters.isDryrun()) {

                        // 內網 db 將 person 標記為 delete 就好
                        if (StringUtils.equalsIgnoreCase(serviceParameters.getPlaceholder(), "inner")) {
                            log.info("To be mark from db : {}", connectorSpaceAdPersonDetails);

                            connectorSpaceAdPersonDetails.setObjectguid("-1");
                            connectorSpaceAdPersonDetails.setUseraccountcontrol(514);
                            connectorSpaceAdPersonDetails.setUserparameters("DELETE");
                            // 標記為刪除時間(不然屬性異動比對時不會同步到外網)
                            connectorSpaceAdPersonDetails.setWhenchanged(new Timestamp(new Date().getTime()));
                            String detailsJson = mapper.writeValueAsString(connectorSpaceAdPersonDetails);
                            log.info("{}", detailsJson);
                            connectorSpaceAdPersonDetails.setSyncattrsHash(DataUtils.hash(detailsJson));

                            connectorSpaceAdPersonDetailsRepository.save(connectorSpaceAdPersonDetails);

                            auditLogsService.audit(
                                    AuditLogsCategoryType.UserManagement_MarkUser.getActivityDisplayName(),
                                    connectorSpaceAdPersonDetails.toString(),
                                    AuditLogsCategoryType.UserManagement_MarkUser.getCategory(),
                                    "", this.getClass().getName(), AuditLogsResult.SUCCESS, 0, "",
                                    connectorSpaceAdPersonDetails.getDn(), 0L);
                        }

                        // 外網 db 直接將此 person 刪除(外網ad進行person刪除時，也是將db內的資料刪除)
                        else if (StringUtils.equalsIgnoreCase(serviceParameters.getPlaceholder(), "external")) {
                            log.info("To be delete from db : {}", connectorSpaceAdPersonDetails);

                            connectorSpaceAdPersonDetailsRepository
                                    .delete(connectorSpaceAdPersonDetails);

                            auditLogsService.audit(
                                    AuditLogsCategoryType.UserManagement_DeletePhantomUser.getActivityDisplayName(),
                                    connectorSpaceAdPersonDetails.toString(),
                                    AuditLogsCategoryType.UserManagement_DeletePhantomUser.getCategory(),
                                    "", this.getClass().getName(), AuditLogsResult.SUCCESS, 0, "",
                                    connectorSpaceAdPersonDetails.getDn(), 0L);
                        }
                    }
                }
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("clean", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }
    }

    /**
     * 搜尋 AD 不存在的 user，並移除
     *
     * @throws Exception
     */
    private void deleteActiveDirectoryOrganizationPerson() throws Exception {

        if (StringUtils.equalsIgnoreCase(serviceParameters.getPlaceholder(), "external"))
            deleteExternalActiveDirectoryOrganizationPerson();
    }

    private void deleteExternalActiveDirectoryOrganizationPerson() throws Exception {

        log.info("Begin remove mark deleted ActiveDirectory OrganizationPerson (external) ...");

        List<Object[]> connectorSpaceAdPersonDetailsList = connectorSpaceAdPersonExternalDetailsRepository
                .findExternalAllConnectorSpaceAdPersonSyncDetailsNotInConnectorSpaceAdPersonDetailsWhenIsDeleted(
                        serviceParameters.getPlaceholder(), serviceParameters.getBasedn());

        log.info("Remove ActiveDirectory OrganizationPerson list size: {}", connectorSpaceAdPersonDetailsList.size());

        if (null != connectorSpaceAdPersonDetailsList && connectorSpaceAdPersonDetailsList
                .size() >= serviceParameters.getRemoveAduserMaxlimit()) {

            log.warn(
                    " ActiveDirectory OrganizationPerson mark deleted counts are over max limit : {}",
                    serviceParameters.getRemoveAduserMaxlimit());

            auditLogsService.audit(
                    AuditLogsCategoryType.UserManagement_DeleteUserOverMaxLimit.getActivityDisplayName(),
                    "ActiveDirectory OrganizationPerson mark deleted counts are over max limit : "
                            + connectorSpaceAdPersonDetailsList.size(),
                    AuditLogsCategoryType.UserManagement_DeleteUser.getCategory(), "",
                    this.getClass().getName(), AuditLogsResult.FAILURE, 0, "", "", 0L);

            return;
        }

        for (Object[] object : connectorSpaceAdPersonDetailsList) {

            ConnectorSpaceAdPersonExternalDetails connectorSpaceAdPersonExternalDetails = (ConnectorSpaceAdPersonExternalDetails) object[0];
            ConnectorSpaceAdPersonDetails connectorSpaceAdPersonDetails = (ConnectorSpaceAdPersonDetails) object[1];

            try {
                log.info(
                        "(Dryrun : {}) found ActiveDirectory OrganizationPerson is mark deleted : {}-{}",
                        serviceParameters.isDryrun(), connectorSpaceAdPersonExternalDetails,
                        connectorSpaceAdPersonDetails);

                if (!StringUtils.equalsIgnoreCase(connectorSpaceAdPersonExternalDetails.getObjectguid(), "-1")) {

                    log.warn(
                            " found ActiveDirectory OrganizationPerson mark deleted is not removed from source ad. bypass processing ...");
                    continue;
                }

                String dn = connectorSpaceAdPersonDetails.getDn();

                SearchResultEntry entry = activeDirectoryService.getEntry(dn, ORGANIZATIONPERSON_ATTRIBUTES2);

                // 再次外網ad上此dn user還存在再進行刪除
                if (null != entry) {

                    log.info(
                            "confirm ActiveDirectory OrganizationPerson mark deleted is existed, will be removed : {}",
                            connectorSpaceAdPersonDetails);

                    if (!serviceParameters.isDryrun()) {

                        String userAccountControl = entry.getAttributeValue("userAccountControl");
                        String userParameters = entry.getAttributeValue("userParameters");

                        // 再次確認外網 ad 上的 userAccountControl 和 userParameters 已經是標記刪除
                        if (StringUtils.equalsIgnoreCase(userAccountControl, "514")
                                && StringUtils.equalsIgnoreCase(userParameters, "DELETE")) {

                            LDAPResult result = activeDirectoryService.removeOrganizationPerson(dn);

                            log.info("   result = {}", result);

                            // remove db after removing ad user successfully
                            if (result.getResultCode().intValue() == LDAPResultCode.LDAP_SUCCESS
                                    .getCode())
                                connectorSpaceAdPersonDetailsRepository
                                        .delete(connectorSpaceAdPersonDetails);
                        } else
                            log.info(
                                    " confirm ActiveDirectory OrganizationPerson is not mark deleted : {} {} {}",
                                    connectorSpaceAdPersonDetails.getCn(),
                                    connectorSpaceAdPersonDetails.getUseraccountcontrol(),
                                    connectorSpaceAdPersonDetails.getUserparameters());
                    }
                } else {

                    log.info(
                            " confirm ActiveDirectory OrganizationPerson mark deleted is not existed  : {}",
                            connectorSpaceAdPersonDetails);
                }
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                stackTrace = "dn: " + connectorSpaceAdPersonExternalDetails.getDn() + "\n" + stackTrace;
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("clean", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }
    }

    /**
     * 搜尋 AD 不存在的 group，並外網資料庫直接刪除
     *
     * @throws Exception
     */
    private void cleanActiveDirectoryOrganizationGroup() throws Exception {

        log.info("Begin mark or delete db data diff from ActiveDirectory OrganizationGroup ...");

        SearchResult searchResult = activeDirectoryService.searchOrganizationGroup(
                serviceParameters.getBasedn(), null, ORGANIZATIONGROUP_ATTRIBUTES);

        log.info("{}", searchResult);

        List<String> cnList = new ArrayList<>();

        // 要加這個，否則一筆ad cn都沒有時db會抓不出資料
        cnList.add("");
        for (SearchResultEntry entry : searchResult.getSearchEntries()) {

            Attribute attr = entry.getAttribute("cn");

            if (null != attr)
                cnList.add(attr.getValue());
        }

        List<ConnectorSpaceAdGroupDetails> connectorSpaceAdGroupDetailsList = connectorSpaceAdGroupDetailsRepository
                .findAllByCnNotInAndPlaceholderAndObjectguidNot(cnList, serviceParameters.getPlaceholder(), "-1");

        log.info("Mark or delete  ActiveDirectory OrganizationGroup list size: {}", connectorSpaceAdGroupDetailsList.size());

        for (ConnectorSpaceAdGroupDetails connectorSpaceAdGroupDetails : connectorSpaceAdGroupDetailsList) {

            try {

                log.info(
                        "(Dryrun : {}) found ActiveDirectory OrganizationGroup is not existed : {}",
                        serviceParameters.isDryrun(), connectorSpaceAdGroupDetails);

                // 再確定一次 ad 裡已經沒這個 group
                SearchResult confirmSearchResult = activeDirectoryService
                        .searchOrganizationGroupByCn(serviceParameters.getBasedn(),
                                connectorSpaceAdGroupDetails.getCn(), ORGANIZATIONGROUP_ATTRIBUTES);

                log.info("{}", confirmSearchResult);

                if (confirmSearchResult.getSearchEntries().isEmpty()) {

                    log.info(" confirm ActiveDirectory OrganizationGroup is not existed : {}",
                            connectorSpaceAdGroupDetails);

                    if (!serviceParameters.isDryrun()) {

                        // 內網 db 將 group 標記為 delete 就好
                        if (StringUtils.equalsIgnoreCase(serviceParameters.getPlaceholder(), "inner")) {
                            log.info("To be mark from db : {}", connectorSpaceAdGroupDetails);

                            connectorSpaceAdGroupDetails.setObjectguid("-1");
                            String detailsJson = mapper.writeValueAsString(connectorSpaceAdGroupDetails);
                            log.info("{}", detailsJson);
                            connectorSpaceAdGroupDetails.setSyncattrsHash(DataUtils.hash(detailsJson));

                            connectorSpaceAdGroupDetailsRepository.save(connectorSpaceAdGroupDetails);

                            auditLogsService.audit(
                                    AuditLogsCategoryType.GroupManagement_MarkGroup.getActivityDisplayName(),
                                    connectorSpaceAdGroupDetails.toString(),
                                    AuditLogsCategoryType.GroupManagement_MarkGroup.getCategory(),
                                    "", this.getClass().getName(), AuditLogsResult.SUCCESS, 0, "",
                                    connectorSpaceAdGroupDetails.getDn(), 0L);
                        }

                        // 外網 db 直接將此 group 刪除
                        else if (StringUtils.equalsIgnoreCase(serviceParameters.getPlaceholder(), "external")) {
                            log.info("To be delete from db : {}", connectorSpaceAdGroupDetails);

                            connectorSpaceAdGroupDetailsRepository.delete(connectorSpaceAdGroupDetails);

                            auditLogsService.audit(
                                    AuditLogsCategoryType.GroupManagement_DeletePhantomGroup.getActivityDisplayName(),
                                    connectorSpaceAdGroupDetails.toString(),
                                    AuditLogsCategoryType.GroupManagement_DeletePhantomGroup.getCategory(),
                                    "", this.getClass().getName(), AuditLogsResult.SUCCESS, 0, "",
                                    connectorSpaceAdGroupDetails.getDn(), 0L);
                        }
                    }
                }
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("clean", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }
    }
}

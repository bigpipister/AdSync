package com.cht.directory.connector.sync.service;

import java.util.List;

import com.cht.directory.connector.security.utils.DataUtils;
import com.cht.directory.connector.service.EventLogsService;
import com.cht.directory.domain.ConnectorSpaceAdGroupExternalDetails;
import com.cht.directory.domain.ConnectorSpaceAdPersonDetails;
import com.cht.directory.domain.ConnectorSpaceAdPersonExternalDetails;
import com.cht.directory.repository.ConnectorSpaceAdGroupExternalDetailsRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cht.directory.connector.service.ActiveDirectoryService;
//import com.cht.directory.connector.service.ExchangeService;
import com.cht.directory.connector.service.error.LDAPResultCode;
import com.cht.directory.connector.sync.config.ServiceParameters;
import com.cht.directory.connector.type.ADGroupType;
import com.cht.directory.connector.type.PlaceHolder;
import com.cht.directory.domain.ConnectorSpaceAdGroupDetails;
import com.unboundid.ldap.sdk.LDAPResult;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GroupSyncService {

    @Autowired
    private ServiceParameters serviceParameters;

    @Value("${activedirectory.service.domaindn}")
    private String domaindn;

    @Value("${activedirectory.service.placeholder}")
    private PlaceHolder placeHolder;

    @Autowired
    private ConnectorSpaceAdGroupExternalDetailsRepository connectorSpaceAdGroupExternalDetailsRepository;

    @Autowired
    private ActiveDirectoryService adDirectoryService;

//    @Autowired
//    private ExchangeService exchangeService;

    @Autowired
    private EventLogsService eventLogsService;

    private String health = "Y";

    public String executeSync() throws Exception {

        addOrganizationGroup();
        // changeOrganizationGroup(); // cn同，dn不同，針對更換單位
        // 同步屬性: dn, displayname, cn, samaccountname, name, groupType,
        //         entensionattribute10~15, dnHash, memberof
        modifyOrganizationGroup(); // dn同，attributes不同

        return health;
    }

    public void addOrganizationGroup() throws Exception {

        // 外網
        if (placeHolder == PlaceHolder.EXTERNAL) {

            List<ConnectorSpaceAdGroupExternalDetails> connectorSpaceAdGroupExternalDetailsList =
                    connectorSpaceAdGroupExternalDetailsRepository
                    .findAllConnectorSpaceAdGroupExternalDetailsNotInConnectorSpaceAdGroupDetails(
                            serviceParameters.getBasedn());
            log.info("Add OrganizationGroup list size: {}", connectorSpaceAdGroupExternalDetailsList.size());
            addExternalOrganizationGroup(connectorSpaceAdGroupExternalDetailsList);
        }
    }

    private void addExternalOrganizationGroup(
            List<ConnectorSpaceAdGroupExternalDetails> connectorSpaceAdGroupExternalDetailsList)
            throws Exception {

        for (ConnectorSpaceAdGroupExternalDetails connectorSpaceAdGroupExternalDetails :
                connectorSpaceAdGroupExternalDetailsList) {

            // 因為只針對cn、placeholder挑出新group，所以要再檢核一下dn
            if (StringUtils.endsWithIgnoreCase(connectorSpaceAdGroupExternalDetails.getDn(), domaindn)) {
                try {

                    log.info("(Dryrun : {}) Add Group : {}", serviceParameters.isDryrun(),
                            connectorSpaceAdGroupExternalDetails);

                    if (!serviceParameters.isDryrun()) {

                        String groupCn = connectorSpaceAdGroupExternalDetails.getCn();
                        String groupDn = connectorSpaceAdGroupExternalDetails.getDn();
                        ADGroupType adGroupType = ADGroupType
                                .findByAdType(connectorSpaceAdGroupExternalDetails.getGroupType());

                        // 這裡只塞基本屬性
                        LDAPResult result = adDirectoryService.addGroup(
                                groupDn, groupCn,
                                adGroupType.getType(),
                                connectorSpaceAdGroupExternalDetails.getDisplayname(),
                                connectorSpaceAdGroupExternalDetails.getExtensionattribute10(),
                                connectorSpaceAdGroupExternalDetails.getExtensionattribute11(),
                                connectorSpaceAdGroupExternalDetails.getExtensionattribute12(),
                                connectorSpaceAdGroupExternalDetails.getExtensionattribute13(),
                                connectorSpaceAdGroupExternalDetails.getExtensionattribute14(),
                                connectorSpaceAdGroupExternalDetails.getExtensionattribute15());

                        // 下面會嘗試將 group 加入預設邏輯的 group member
                        if (result.getResultCode().intValue() == LDAPResultCode.LDAP_SUCCESS
                                .getCode()) {

                            String orgDn = adDirectoryService.removeCn(groupDn);

                            // 萬用群組
                            if (adGroupType == ADGroupType.UNIVERSAL_SECURITY_GROUP) {

                                String parentGroupDn = adDirectoryService.getParentGroupDn(orgDn,
                                        ADGroupType.UNIVERSAL_SECURITY_GROUP);

                                // 萬用群組新增至上層 OU 萬用群組成員
                                if (StringUtils.isNotBlank(parentGroupDn)) {

                                    log.info("Add OrganizationGroup : {} to a member of Group : {} ",
                                            groupDn, parentGroupDn);
                                    // 將萬用群組加入上層萬用群組member
                                    adDirectoryService.addOrganizationUnitGroupMember(parentGroupDn,
                                            groupDn);
                                }
                            // 網域本機群組
                            } else if (adGroupType == ADGroupType.DOMAINLOCAL_SECURITY_GROUP) {

                                // 取得全域群組 dn
                                String globalGroupDn = "CN="
                                        + groupCn.substring(0, groupCn.length() - 3) + "-G," + orgDn;

                                log.info("Add OrganizationGroup : {} to a member of Group : {} ",
                                        groupDn, globalGroupDn);

                                // 將全域群組加入網域本機群組 member
                                LDAPResult dlmAddresult = adDirectoryService
                                        .addOrganizationUnitGroupMember(groupDn, globalGroupDn);
                            // 全域群組
                            } else if (adGroupType == ADGroupType.GLOBAL_SECURITY_GROUP) {

                                String parentGlobalGroupDn = adDirectoryService.getParentGroupDn(orgDn,
                                        ADGroupType.GLOBAL_SECURITY_GROUP);

                                if (StringUtils.isNotBlank(parentGlobalGroupDn)) {

                                    log.info("Add OrganizationGroup : {} to a member of Group : {} ",
                                            groupDn, parentGlobalGroupDn);
                                    // 將全域群組加入上層全域群組member
                                    LDAPResult gmAddresult = adDirectoryService
                                            .addOrganizationUnitGroupMember(parentGlobalGroupDn,
                                                    groupDn);
                                }
                            }
                        }
                    }
                } catch (Exception ex) {

                    String stackTrace = ExceptionUtils.getStackTrace(ex);
                    stackTrace = "dn: " + connectorSpaceAdGroupExternalDetails.getDn() + "\n" + stackTrace;
                    // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                    stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                    log.error("{}", stackTrace);
                    eventLogsService.record("sync", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                    health = "N";
                }
            }
        }
    }

    public void changeOrganizationGroup() throws Exception {

        // 外網
        if (placeHolder == PlaceHolder.EXTERNAL) {

            // cn相同，dn不同，針對更換單位
            List<Object[]> connectorSpaceAdGroupExternalDetailsList = connectorSpaceAdGroupExternalDetailsRepository
                    .findAllConnectorSpaceAdGroupExternalDetailsJoinConnectorSpaceAdGroupDetailsWhenDnChanged(
                            serviceParameters.getBasedn());
            log.info("Change OrganizationGroup list size: {}", connectorSpaceAdGroupExternalDetailsList.size());
            changeExternalOrganizationGroup(connectorSpaceAdGroupExternalDetailsList);
        }
    }

    private void changeExternalOrganizationGroup(List<Object[]> connectorSpaceAdGroupExternalDetailsList)
            throws Exception {

        for (Object[] entrys : connectorSpaceAdGroupExternalDetailsList) {

            ConnectorSpaceAdGroupExternalDetails connectorSpaceAdGroupExternalDetails = (ConnectorSpaceAdGroupExternalDetails) entrys[0];
            ConnectorSpaceAdGroupDetails connectorSpaceAdGroupDetails = (ConnectorSpaceAdGroupDetails) entrys[1];

            try {

                String groupCn = "CN=" + connectorSpaceAdGroupExternalDetails.getCn();
                String oldGroupDn = connectorSpaceAdGroupDetails.getDn();
                String newOrgDn = StringUtils.remove(connectorSpaceAdGroupExternalDetails.getDn(), groupCn + ",");

                log.info("(Dryrun : {}) Change Group : {} to Organization : {} ",
                        serviceParameters.isDryrun(), oldGroupDn, newOrgDn);

                if (!serviceParameters.isDryrun()) {

                    // 將group change到新的ou
                    LDAPResult changeResult = adDirectoryService
                            .changeGroup(oldGroupDn, groupCn, newOrgDn);

                }
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("sync", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }
    }

    public void modifyOrganizationGroup() throws Exception {

        // 外網
        if (placeHolder == PlaceHolder.EXTERNAL) {

            List<Object[]> connectorSpaceAdGroupExternalDetailsList = connectorSpaceAdGroupExternalDetailsRepository
                    .findAllConnectorSpaceAdGroupExternalDetailsInConnectorSpaceAdGroupDetailsWhenAttrsChanged(
                            serviceParameters.getBasedn());
            log.info("Modify OrganizationGroup list size: {}", connectorSpaceAdGroupExternalDetailsList.size());
            modifyExternalOrganizationGroup(connectorSpaceAdGroupExternalDetailsList);
        }
    }

    private void modifyExternalOrganizationGroup(List<Object[]> connectorSpaceAdGroupExternalDetailsList)
            throws Exception {

        for (Object[] entrys : connectorSpaceAdGroupExternalDetailsList) {

            ConnectorSpaceAdGroupExternalDetails connectorSpaceAdGroupExternalDetails = (ConnectorSpaceAdGroupExternalDetails) entrys[0];
            ConnectorSpaceAdGroupDetails connectorSpaceAdGroupDetails = (ConnectorSpaceAdGroupDetails) entrys[1];

            try {
                if (StringUtils.endsWithIgnoreCase(connectorSpaceAdGroupExternalDetails.getDn(), domaindn)) {

                    log.info("(Dryrun : {}) Modify Group : {}->{}", serviceParameters.isDryrun(),
                            connectorSpaceAdGroupDetails, connectorSpaceAdGroupExternalDetails);

                    if (!serviceParameters.isDryrun()) {

//                        if (!StringUtils.equals(connectorSpaceAdGroupExternalDetails.getExtensionattribute10(),
//                                connectorSpaceAdGroupDetails.getExtensionattribute10())) {
//
//                            adDirectoryService.modifyGroup(connectorSpaceAdGroupDetails.getDn(),
//                                    connectorSpaceAdGroupExternalDetails.getDisplayname(),
//                                    connectorSpaceAdGroupExternalDetails.getExtensionattribute10(),
//                                    connectorSpaceAdGroupExternalDetails.getExtensionattribute11(),
//                                    connectorSpaceAdGroupExternalDetails.getExtensionattribute12(),
//                                    connectorSpaceAdGroupExternalDetails.getExtensionattribute13(),
//                                    connectorSpaceAdGroupExternalDetails.getExtensionattribute14(),
//                                    connectorSpaceAdGroupExternalDetails.getExtensionattribute15());
//                        }

                        LDAPResult result = adDirectoryService.modifyGroup(connectorSpaceAdGroupDetails.getDn(),
                                connectorSpaceAdGroupExternalDetails.getDisplayname(),
                                connectorSpaceAdGroupExternalDetails.getExtensionattribute10(),
                                connectorSpaceAdGroupExternalDetails.getExtensionattribute11(),
                                connectorSpaceAdGroupExternalDetails.getExtensionattribute12(),
                                connectorSpaceAdGroupExternalDetails.getExtensionattribute13(),
                                connectorSpaceAdGroupExternalDetails.getExtensionattribute14(),
                                connectorSpaceAdGroupExternalDetails.getExtensionattribute15());

                        // 已停用exchange
//                        ADGroupType adGroupType = ADGroupType
//                                .findByAdType(connectorSpaceAdGroupExternalDetails.getGroupType());
//
//                        if (adGroupType == ADGroupType.UNIVERSAL_SECURITY_GROUP) {
//
//                            if (!StringUtils.equals(connectorSpaceAdGroupExternalDetails.getMailEnabled(),
//                                    connectorSpaceAdGroupDetails.getMailEnabled())) {
//
//                                 if (StringUtils.equals(metaverseGroupDetails.getMailEnabled(),
//                                 "1")) {
//
//                                 log.info(" Call Exchange Enable-DistributionGroup API : {}",
//                                 connectorSpaceAdGroupDetails.getDn());
//
//                                 try {
//                                 // 萬用群組新增成功後，呼叫 Enable-DistributionGroup API
//                                 // 將該萬用群組加入Exchange通訊錄清單裡
//
//                                 exchangeService.enableDistributionGroup(
//                                 connectorSpaceAdGroupDetails.getDn());
//                                 } catch (Exception ex) {
//
//                                 log.error("{}", ExceptionUtils.getStackTrace(ex));
//                                 }
//                                 } else if (StringUtils
//                                 .equals(metaverseGroupDetails.getMailEnabled(), "0")) {
//
//                                 log.info(" Call Disable Disable-DistributionGroup API : {}",
//                                 connectorSpaceAdGroupDetails.getDn());
//                                 }
//                            }
//                        }

                        // he: 2025-07-28 不處理 group memberof 的內外網同步(內外網group memberof可能管理方式不同)
//                        if (result.getResultCode().intValue() == LDAPResultCode.LDAP_SUCCESS.getCode()) {
//
//                            // memberOf 屬性如果不同，重新呼叫加入群組
//                            if (!StringUtils.equals(connectorSpaceAdGroupExternalDetails.getMemberOf(),
//                                    connectorSpaceAdGroupDetails.getMemberOf())) {
//
//                                if (StringUtils
//                                        .isNotBlank(connectorSpaceAdGroupDetails.getMemberOf())) {
//                                    String adGroupMemberOf = connectorSpaceAdGroupDetails.getMemberOf();
//
//                                    for (String groupDn : StringUtils.split(adGroupMemberOf, ";")) {
//                                        // 將 ad 上關聯此 group memberof 的 group，將此 group 從其 member 中移除
//                                        adDirectoryService.removeOrganizationUnitGroupMember(
//                                                groupDn, connectorSpaceAdGroupDetails.getDn());
//                                    }
//                                }
//
//                                if (StringUtils.isNotBlank(connectorSpaceAdGroupExternalDetails.getMemberOf())) {
//                                    String adGroupMemberOf = connectorSpaceAdGroupExternalDetails.getMemberOf();
//
//                                    for (String groupDn : StringUtils.split(adGroupMemberOf, ";")) {
//                                        // 根據同步過來 group 的 memberof 重新加入其 group 的 member 中
//                                        adDirectoryService.addOrganizationUnitGroupMember(groupDn,
//                                                connectorSpaceAdGroupExternalDetails.getDn());
//                                    }
//                                }
//                            }
//                        }
                    }
                }
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                stackTrace = "dn: " + connectorSpaceAdGroupExternalDetails.getDn() + "\n" + stackTrace;
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("sync", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }
    }
}

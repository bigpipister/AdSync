package com.cht.directory.connector.sync.service;

import java.util.List;

import javax.naming.Name;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import com.cht.directory.connector.security.utils.DataUtils;
import com.cht.directory.connector.service.EventLogsService;
import com.cht.directory.connector.service.StatusLogsService;
import com.cht.directory.domain.ConnectorSpaceAdOrganizationalUnitExternalDetails;
import com.cht.directory.repository.ConnectorSpaceAdOrganizationalUnitExternalDetailsRepository;
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
import com.cht.directory.connector.type.MOFType;
import com.cht.directory.connector.type.PlaceHolder;
import com.cht.directory.domain.ConnectorSpaceAdOrganizationalUnitDetails;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.SearchResultEntry;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrganizationUnitSyncService {

    @Autowired
    private ServiceParameters serviceParameters;

    @Value("${activedirectory.service.domaindn}")
    private String domaindn;

    @Value("${activedirectory.service.placeholder}")
    private PlaceHolder placeHolder;

    @Autowired
    private ConnectorSpaceAdOrganizationalUnitExternalDetailsRepository connectorSpaceAdOrganizationalUnitExternalDetailsRepository;

    @Autowired
    private ActiveDirectoryService adDirectoryService;

//    @Autowired
//    private ExchangeService exchangeService;

    @Autowired
    private EventLogsService eventLogsService;

    private String health = "Y";

    public String executeSync() throws Exception {

        addOrganizationUnit();
        // change ou時，裡面的group與person其ou會跟著一起變更
        changeOrganizationUnit(); // ou同，dn不同
        // 同步屬性: displayname, ou, dn, dnHash
        modifyOrganizationUnit(); // dn同，attributes不同

        return health;
    }

    public void addOrganizationUnit() throws Exception {

        // 外網
        if (placeHolder == PlaceHolder.EXTERNAL) {

            List<ConnectorSpaceAdOrganizationalUnitExternalDetails> connectorSpaceAdOrganizationalUnitExternalDetailsList =
                    connectorSpaceAdOrganizationalUnitExternalDetailsRepository
                    .findAllConnectorSpaceAdOrganizationalUnitExternalDetailsNotInConnectorSpaceAdOrganizationalUnitDetails(
                            serviceParameters.getBasedn());
            log.info("Add OrganizationUnit list size: {}", connectorSpaceAdOrganizationalUnitExternalDetailsList.size());
            addExternalOrganizationUnit(connectorSpaceAdOrganizationalUnitExternalDetailsList);
        }
    }

    public void addExternalOrganizationUnit(
            List<ConnectorSpaceAdOrganizationalUnitExternalDetails> connectorSpaceAdOrganizationalUnitExternalDetailsList)
            throws Exception {

        for (ConnectorSpaceAdOrganizationalUnitExternalDetails connectorSpaceAdOrganizationalUnitExternalDetails :
                connectorSpaceAdOrganizationalUnitExternalDetailsList) {

            // 因為只針對ou、placeholder挑出新組織，所以要再檢核一下dn
            if (StringUtils.endsWithIgnoreCase(connectorSpaceAdOrganizationalUnitExternalDetails.getDn(), domaindn)) {

                try {

                    log.info("(Dryrun : {}) Add OrganizationUnit : {}",
                            serviceParameters.isDryrun(), connectorSpaceAdOrganizationalUnitExternalDetails);

                    if (!serviceParameters.isDryrun()) {

                        // 在 add 之前會先確認 ad 上無相同 dn 物件
                        LDAPResult result = adDirectoryService.addOrganizationUnit(
                                connectorSpaceAdOrganizationalUnitExternalDetails.getDn(),
                                connectorSpaceAdOrganizationalUnitExternalDetails.getOu(),
                                connectorSpaceAdOrganizationalUnitExternalDetails.getDisplayname());

                        // 新增 ou 後同時也會新增預設 group
                        if (result.getResultCode().intValue() == LDAPResultCode.LDAP_SUCCESS
                                .getCode()) {
                            addOrganizationUnitGroup(connectorSpaceAdOrganizationalUnitExternalDetails.getDn(),
                                    connectorSpaceAdOrganizationalUnitExternalDetails.getDisplayname());
                        }
                    }
                } catch (Exception ex) {

                    String stackTrace = ExceptionUtils.getStackTrace(ex);
                    stackTrace = "dn: " + connectorSpaceAdOrganizationalUnitExternalDetails.getDn() + "\n" + stackTrace;
                    // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                    stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                    log.error("{}", stackTrace);
                    eventLogsService.record("sync", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                    health = "N";
                }
            }
        }
    }

    public void addOrganizationUnitGroup(String orgDn, String displayName) throws Exception {

        LdapName ldapName = new LdapName(orgDn);

        Name ouSuffix = ldapName.getSuffix(ldapName.size() - 1);

        // 取得最尾的RDN
        Rdn rdn = new Rdn(ouSuffix.toString());

        // 拿最ou最尾的rdn value當其group的cn
        String groupCn = (String) rdn.getValue();

        String groupDn = adDirectoryService.getGroupDn(orgDn);

        log.info("Add OrganizationUnit Group : {}", groupDn);

        // 2025-08-06 修正：因為資料來源已不是ldap，所以這裡不再使用displayName
//        String displayName2 = displayName;
//
//        if (StringUtils.isBlank(displayName)) {
//
//            // 拿ou的displayName當其group的displayName、extensionAttribute10~15
//            SearchResultEntry entry = adDirectoryService.getEntry(orgDn, "displayName");
//
//            if (null != entry)
//                displayName2 = entry.getAttributeValue("displayName");
//        }

        // 新增ou時同時新增這個ou的萬用group
        // 2025-08-06 修正：這裡先不塞基本屬性，讓group modify時再塞入
        LDAPResult result = adDirectoryService.addGroup(groupDn, groupCn,
                ADGroupType.UNIVERSAL_SECURITY_GROUP.getType(),
                null, null, null, null,
                null, null, null);

        if (result.getResultCode().intValue() == LDAPResultCode.LDAP_SUCCESS.getCode()) {

            String parentGroupDn = adDirectoryService.getParentGroupDn(orgDn,
                    ADGroupType.UNIVERSAL_SECURITY_GROUP); // 根據ad group type，cn會有不同的suffix

            // 萬用群組新增至上層 OU 萬用群組成員
            if (StringUtils.isNotBlank(parentGroupDn)) {

                log.info("Add OrganizationGroup : {} to a member of Group : {} ", groupDn,
                        parentGroupDn);
                adDirectoryService.addOrganizationUnitGroupMember(parentGroupDn, groupDn);
            }

            // 1213 - 當為內網時，呼叫啟用通訊錄
//            if (placeHolder == PlaceHolder.INNER) {
//
//                try {
//                    // 萬用群組新增成功後，呼叫 Enable-DistributionGroup API 將該萬用群組加入Exchange通訊錄清單裡
//
//                    exchangeService.enableDistributionGroup(groupCn);
//                } catch (Exception ex) {
//
//                    log.error("{}", ExceptionUtils.getStackTrace(ex));
//                }
//            }

            // 北國新增全域與網域本機群組
            if (StringUtils.contains(groupDn, MOFType.NTBT.getRdn())) {

                String globalGroupDn = adDirectoryService.getGroupDn(groupDn,
                        ADGroupType.GLOBAL_SECURITY_GROUP);
                String dlGroupDn = adDirectoryService.getGroupDn(groupDn,
                        ADGroupType.DOMAINLOCAL_SECURITY_GROUP);

                String globalGroupCn = groupCn + "-G";

                // 新增全域群組
                // 2025-08-06 修正：這裡先不塞基本屬性，讓group modify時再塞入
                LDAPResult gAddresult = adDirectoryService.addGroup(globalGroupDn, globalGroupCn,
                        ADGroupType.GLOBAL_SECURITY_GROUP.getType(),
                        null, null, null, null,
                        null, null, null);

                // 新增網域本機群組
                // 2025-08-06 修正：這裡先不塞基本屬性，讓group modify時再塞入
                LDAPResult dlAddresult = adDirectoryService.addGroup(dlGroupDn, groupCn + "-DL",
                        ADGroupType.DOMAINLOCAL_SECURITY_GROUP.getType(),
                        null, null, null, null,
                        null, null, null);

                // 將全域群組加入網域本機群組member
                LDAPResult dlmAddresult = adDirectoryService
                        .addOrganizationUnitGroupMember(dlGroupDn, globalGroupDn);

                String parentGlobalGroupDn = adDirectoryService.getParentGroupDn(orgDn,
                        ADGroupType.GLOBAL_SECURITY_GROUP);

                if (StringUtils.isNotBlank(parentGlobalGroupDn)) {

                    // 將全域群組加入上層全域群組member
                    LDAPResult gmAddresult = adDirectoryService
                            .addOrganizationUnitGroupMember(parentGlobalGroupDn, globalGroupDn);
                }
            }
        }
    }

    public void changeOrganizationUnit() throws Exception {

        // 外網
        if (placeHolder == PlaceHolder.EXTERNAL) {

            List<Object[]> connectorSpaceAdOrganizationalUnitExternalDetailsList =
                    connectorSpaceAdOrganizationalUnitExternalDetailsRepository
                    .findAllConnectorSpaceAdOrganizationalUnitExternalDetailsJoinConnectorSpaceAdOrganizationalUnitDetailsWhenDnChanged(
                            serviceParameters.getBasedn());
            log.info("Change OrganizationUnit list size: {}", connectorSpaceAdOrganizationalUnitExternalDetailsList.size());
            changeExternalOrganizationUnit(connectorSpaceAdOrganizationalUnitExternalDetailsList);
        }
    }

    private void changeExternalOrganizationUnit(
            List<Object[]> connectorSpaceAdOrganizationalUnitExternalDetailsList) throws Exception {

        for (Object[] entrys : connectorSpaceAdOrganizationalUnitExternalDetailsList) {

            ConnectorSpaceAdOrganizationalUnitExternalDetails connectorSpaceAdOrganizationalUnitExternalDetails = (ConnectorSpaceAdOrganizationalUnitExternalDetails) entrys[0];
            ConnectorSpaceAdOrganizationalUnitDetails connectorSpaceAdOrganizationalUnitDetails = (ConnectorSpaceAdOrganizationalUnitDetails) entrys[1];

            try {

                String ou = "OU=" + connectorSpaceAdOrganizationalUnitExternalDetails.getOu();
                String oldOuDn = connectorSpaceAdOrganizationalUnitDetails.getDn();
                String newOrgDn = StringUtils.remove(connectorSpaceAdOrganizationalUnitExternalDetails.getDn(), ou + ",");

                log.info("(Dryrun : {}) Change Organization : {} to Organization : {} ",
                        serviceParameters.isDryrun(), oldOuDn, newOrgDn);

                if (!serviceParameters.isDryrun()) {

                    LDAPResult changeResult = adDirectoryService.changeOrganizationUnit(oldOuDn, ou, newOrgDn);
                }
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                stackTrace = "dn: " + connectorSpaceAdOrganizationalUnitExternalDetails.getDn() + "\n" + stackTrace;
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("sync", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }
    }

    /**
     *
     * @throws Exception
     */
    public void modifyOrganizationUnit() throws Exception {

        // 外網
        if (placeHolder == PlaceHolder.EXTERNAL) {

            List<Object[]> connectorSpaceAdOrganizationalUnitExternalDetailsList =
                    connectorSpaceAdOrganizationalUnitExternalDetailsRepository
                    .findAllConnectorSpaceAdOrganizationalUnitExternalDetailsInConnectorSpaceAdOrganizationalUnitDetailsWhenAttrsChanged(
                            serviceParameters.getBasedn());
            log.info("Modify OrganizationUnit list size: {}", connectorSpaceAdOrganizationalUnitExternalDetailsList.size());
            modifyExternalOrganizationUnit(connectorSpaceAdOrganizationalUnitExternalDetailsList);
        }
    }

    private void modifyExternalOrganizationUnit(
            List<Object[]> connectorSpaceAdOrganizationalUnitExternalDetailsList) throws Exception {

        for (Object[] entrys : connectorSpaceAdOrganizationalUnitExternalDetailsList) {

            ConnectorSpaceAdOrganizationalUnitExternalDetails connectorSpaceAdOrganizationalUnitExternalDetails = (ConnectorSpaceAdOrganizationalUnitExternalDetails) entrys[0];
            ConnectorSpaceAdOrganizationalUnitDetails connectorSpaceAdOrganizationalUnitDetails = (ConnectorSpaceAdOrganizationalUnitDetails) entrys[1];

            if (StringUtils.endsWithIgnoreCase(connectorSpaceAdOrganizationalUnitExternalDetails.getDn(), domaindn)) {

                try {

                    log.info("(Dryrun : {}) Modify OrganizationUnit : {}->{}",
                            serviceParameters.isDryrun(), connectorSpaceAdOrganizationalUnitDetails,
                            connectorSpaceAdOrganizationalUnitExternalDetails);

                    if (!serviceParameters.isDryrun()) {
                        // ou 的 modify 只會改 displayName
                        LDAPResult result = adDirectoryService.modifyOrganizationUnit(
                                connectorSpaceAdOrganizationalUnitExternalDetails.getDn(),
                                connectorSpaceAdOrganizationalUnitExternalDetails.getDisplayname());

                        if (result.getResultCode().intValue() == LDAPResultCode.LDAP_SUCCESS.getCode()) {

                            // 順便 modify 其 group 的 extensionAttribute10~15
                            // 2025-08-06 修正：這裡先不改屬性，讓group modify時再塞入
//                            LDAPResult modifyResult = modifyOrganizationUnitGroup(
//                                    connectorSpaceAdOrganizationalUnitExternalDetails.getDn(),
//                                    null, null, null ,
//                                    null, null, null, null);
                        }
                    }
                } catch (Exception ex) {

                    String stackTrace = ExceptionUtils.getStackTrace(ex);
                    stackTrace = "dn: " + connectorSpaceAdOrganizationalUnitExternalDetails.getDn() + "\n" + stackTrace;
                    // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                    stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                    log.error("{}", stackTrace);
                    eventLogsService.record("sync", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                    health = "N";
                }
            }
        }
    }

    public LDAPResult modifyOrganizationUnitGroup(String orgDn, String displayName,
                                                  String extensionAttribute10, String extensionAttribute11,
                                                  String extensionAttribute12, String extensionAttribute13,
                                                  String extensionAttribute14, String extensionAttribute15)
            throws Exception {

        LdapName ldapName = new LdapName(orgDn);

        Name ouSuffix = ldapName.getSuffix(ldapName.size() - 1);

        // 取得最尾的RDN
        Rdn rdn = new Rdn(ouSuffix.toString());

        // 拿最ou最尾的rdn value當其group的cn
        String groupCn = (String) rdn.getValue();

        String groupDn = adDirectoryService.getGroupDn(orgDn);

        log.info("Modify Group : {} attributes => {}", groupDn, displayName);

        LDAPResult modifyResult = adDirectoryService.modifyGroup(groupDn, displayName,
                extensionAttribute10, extensionAttribute11, extensionAttribute12,
                extensionAttribute13, extensionAttribute14, extensionAttribute15);

        // 北國也一拼修改全域與網域本機群組
        if (StringUtils.contains(groupDn, MOFType.NTBT.getRdn())) {
            String globalGroupDn = adDirectoryService.getGroupDn(groupDn,
                    ADGroupType.GLOBAL_SECURITY_GROUP);
            String dlGroupDn = adDirectoryService.getGroupDn(groupDn,
                    ADGroupType.DOMAINLOCAL_SECURITY_GROUP);

            // 修改全域群組
            modifyResult = adDirectoryService.modifyGroup(globalGroupDn, displayName,
                    extensionAttribute10, extensionAttribute11, extensionAttribute12,
                    extensionAttribute13, extensionAttribute14, extensionAttribute15);

            // 修改網域本機群組
            modifyResult = adDirectoryService.modifyGroup(dlGroupDn, displayName,
                    extensionAttribute10, extensionAttribute11, extensionAttribute12,
                    extensionAttribute13, extensionAttribute14, extensionAttribute15);
        }

        return modifyResult;
    }
}

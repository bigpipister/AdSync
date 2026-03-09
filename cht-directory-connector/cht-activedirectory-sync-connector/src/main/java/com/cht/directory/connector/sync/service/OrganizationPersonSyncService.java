package com.cht.directory.connector.sync.service;

import java.util.List;

import com.cht.directory.connector.security.utils.DataUtils;
import com.cht.directory.connector.service.EventLogsService;
import com.cht.directory.domain.ConnectorSpaceAdPersonExternalDetails;
import com.cht.directory.repository.ConnectorSpaceAdPersonDetailsRepository;
import com.cht.directory.repository.ConnectorSpaceAdPersonExternalDetailsRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cht.directory.connector.security.KmsClientService;
import com.cht.directory.connector.service.ActiveDirectoryService;
//import com.cht.directory.connector.service.ExchangeService;
import com.cht.directory.connector.service.error.LDAPResultCode;
import com.cht.directory.connector.sync.config.ServiceParameters;
import com.cht.directory.connector.type.ADGroupType;
import com.cht.directory.connector.type.MOFType;
import com.cht.directory.connector.type.PlaceHolder;
import com.cht.directory.domain.ConnectorSpaceAdPersonDetails;
import com.unboundid.ldap.sdk.LDAPResult;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrganizationPersonSyncService {

    @Autowired
    private ServiceParameters serviceParameters;

    @Value("${activedirectory.service.disableMailbox}")
    private String disableMailbox;

    @Value("${activedirectory.service.domaindn}")
    private String domaindn;

    @Value("${activedirectory.service.placeholder}")
    private PlaceHolder placeHolder;

    @Autowired
    private ConnectorSpaceAdPersonExternalDetailsRepository connectorSpaceAdPersonExternalDetailsRepository;

    @Autowired
    private ConnectorSpaceAdPersonDetailsRepository connectorSpaceAdPersonDetailsRepository;

    @Autowired
    private KmsClientService kmsClientService;

    @Autowired
    private ActiveDirectoryService adDirectoryService;

//    @Autowired
//    private ExchangeService exchangeService;

    @Autowired
    private EventLogsService eventLogsService;

    private String health = "Y";

    /**
     * @throws Exception
     */
    public String executeSync() throws Exception {

        addOrganizationPerson(); // 內網啟用且objectguid不為-1的帳號，外網才會新增
        changePersonOrganizationUnit(); // cn同，dn不同，針對更換單位
        // 同步屬性: employyeeid, userparameters, useraccountcontrol, sn, ou,
        //         extensionattribute1~2, accountexpires, userprincipalname,
        //         samaccountname, title, department, extensionattribute10~15,
        //         displayname, cn, dn, pager, dnHash, memberOf
        modifyOrganizationPerson(); // dn同，attributes不同，且內網objectguid不為-1的帳號，外網才會修改
        // removeOrganizationPerson();

        return health;
    }

    /**
     * @throws Exception
     */
    public void addOrganizationPerson() throws Exception {

        // 外網
        if (placeHolder == PlaceHolder.EXTERNAL) {

            // 以cn為key去搜尋，因此換單位同cn不同dn的那種，不會視為新增
            // 資料庫內密碼欄位為空的(pwd filter還沒寫入)也不會挑出(密碼為空無法寫入外網ad)
            List<ConnectorSpaceAdPersonExternalDetails> connectorSpaceAdPersonExternalDetailsList =
                    connectorSpaceAdPersonExternalDetailsRepository
                    .findAllConnectorSpaceAdPersonExternalDetailsNotInConnectorSpaceAdPersonDetails(
                            serviceParameters.getBasedn());
            log.info("Add OrganizationPerson list size: {}", connectorSpaceAdPersonExternalDetailsList.size());
            addExternalOrganizationPerson(connectorSpaceAdPersonExternalDetailsList);
        }
    }

    private void addExternalOrganizationPerson(
            List<ConnectorSpaceAdPersonExternalDetails> connectorSpaceAdPersonExternalDetailsList) throws Exception {

        for (ConnectorSpaceAdPersonExternalDetails connectorSpaceAdPersonExternalDetails :
                connectorSpaceAdPersonExternalDetailsList) {

            // 因為只針對cn、placeholder挑出新person，所以要再檢核一下dn
            if (StringUtils.endsWithIgnoreCase(connectorSpaceAdPersonExternalDetails.getDn(), domaindn)) {

                try {

                    log.info("(Dryrun : {}) Add OrganizationPerson : {}", serviceParameters.isDryrun(),
                            connectorSpaceAdPersonExternalDetails);

                    byte[] unicodePwd = "".getBytes();

                    if (StringUtils.isNotBlank(connectorSpaceAdPersonExternalDetails.getUnicodepwd())) {
//                    unicodePwd = kmsClientService.decrypt(connectorSpaceAdPersonExternalDetails.getCn(),
//                            connectorSpaceAdPersonExternalDetails.getUnicodepwd());
                        // 2024-11-12 pwd filter 攔截到的其實是sAMAccountName
                        unicodePwd = kmsClientService.decrypt(connectorSpaceAdPersonExternalDetails.getSamaccountname(),
                                connectorSpaceAdPersonExternalDetails.getUnicodepwd());
                    }
                    // 一開始新增時 pwd filter 會找不到 person，需等 scan 完，此時如果已同步到外網用預設密碼建立
                    // 使用者外網會看到person已建立卻會登入失敗
//                    else {
//                        // 不管怎樣先設組預設的給它
//                        String password = "!QAZ2wsx3edc";
//                        unicodePwd = DataUtils.toQuoteUnicode(password);
//                    }

                    if (!serviceParameters.isDryrun()) {

                        LDAPResult result = adDirectoryService.addOrganizationPerson(
                                connectorSpaceAdPersonExternalDetails.getDn(),
                                connectorSpaceAdPersonExternalDetails.getCn(),
                                connectorSpaceAdPersonExternalDetails.getEmployeeid(),
                                connectorSpaceAdPersonExternalDetails.getSn(),
                                connectorSpaceAdPersonExternalDetails.getOu(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute1(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute2(),
                                connectorSpaceAdPersonExternalDetails.getAccountexpires(),
                                connectorSpaceAdPersonExternalDetails.getUserprincipalname(),
                                connectorSpaceAdPersonExternalDetails.getSamaccountname(),
                                connectorSpaceAdPersonExternalDetails.getTitle(),
                                connectorSpaceAdPersonExternalDetails.getDepartment(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute10(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute11(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute12(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute13(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute14(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute15(),
                                connectorSpaceAdPersonExternalDetails.getDisplayname(), unicodePwd,
                                connectorSpaceAdPersonExternalDetails.getUseraccountcontrol(),
                                connectorSpaceAdPersonExternalDetails.getUserparameters(),
                                connectorSpaceAdPersonExternalDetails.getPager());

                        if (result.getResultCode().intValue() == LDAPResultCode.LDAP_SUCCESS.getCode()) {

                            // person 預設都會加同ou的萬用群組，北國會額外多加同ou的全域群組
                            addOrganizationPersonGroupMember(connectorSpaceAdPersonExternalDetails.getCn(),
                                    connectorSpaceAdPersonExternalDetails.getDn());

                            // 1213 - 外網不啟用信箱
                            // try {
                            // // 當 extensionAttribute2=1啟用信箱, 0停用信箱
                            // if (StringUtils.equalsIgnoreCase(
                            // metaversePersonDetails.getExtensionattribute2(), "1")) {
                            // exchangeService
                            // .enableMailBox(metaversePersonDetails.getSamaccountname());
                            // }
                            // } catch (Exception ex) {
                            //
                            // log.error("{}", ExceptionUtils.getStackTrace(ex));
                            // }
                        }
                    }
                } catch (Exception ex) {

                    String stackTrace = ExceptionUtils.getStackTrace(ex);
                    stackTrace = "dn: " + connectorSpaceAdPersonExternalDetails.getDn() + "\n" + stackTrace;
                    // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                    stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                    log.error("{}", stackTrace);
                    eventLogsService.record("sync", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                    health = "N";
                }
            }
        }
    }

    /**
     * @param userCn
     * @param userDn
     * @throws Exception
     */
    public void addOrganizationPersonGroupMember(String userCn, String userDn) throws Exception {

        String orgDn = StringUtils.remove(userDn, "CN=" + userCn + ",");
        String groupDn = adDirectoryService.getGroupDn(orgDn);

        log.info("Add OrganizationPerson : {} to to a member of Group : {}", userDn, groupDn);

        LDAPResult result = adDirectoryService.addOrganizationUnitGroupMember(groupDn, userDn);

        if (StringUtils.contains(groupDn, MOFType.NTBT.getRdn())) {

            String globalGroupDn = adDirectoryService.getGroupDn(orgDn,
                    ADGroupType.GLOBAL_SECURITY_GROUP);

            log.info("Add OrganizationPerson : {} to to a member of Group : {}", userDn,
                    globalGroupDn);

            LDAPResult addResult = adDirectoryService.addOrganizationUnitGroupMember(globalGroupDn,
                    userDn);
        }
    }

    public void changePersonOrganizationUnit() throws Exception {

        // 外網
        if (placeHolder == PlaceHolder.EXTERNAL) {

            // cn相同，dn不同，針對更換單位
            List<Object[]> connectorSpaceAdPersonExternalDetailsList = connectorSpaceAdPersonExternalDetailsRepository
                    .findAllConnectorSpaceAdPersonExternalDetailsJoinConnectorSpaceAdPersonDetailsWhenDnChanged(
                            serviceParameters.getBasedn());
            log.info("Change OrganizationPerson list size: {}", connectorSpaceAdPersonExternalDetailsList.size());
            changeExternalPersonOrganizationUnit(connectorSpaceAdPersonExternalDetailsList);
        }
    }

    private void changeExternalPersonOrganizationUnit(List<Object[]> connectorSpaceAdPersonExternalDetailsList)
            throws Exception {

        for (Object[] entrys : connectorSpaceAdPersonExternalDetailsList) {

            ConnectorSpaceAdPersonExternalDetails connectorSpaceAdPersonExternalDetails = (ConnectorSpaceAdPersonExternalDetails) entrys[0];
            ConnectorSpaceAdPersonDetails connectorSpaceAdPersonDetails = (ConnectorSpaceAdPersonDetails) entrys[1];

            try {

                String userCn = "CN=" + connectorSpaceAdPersonExternalDetails.getCn();
                String oldUserDn = connectorSpaceAdPersonDetails.getDn();
                String newOrgDn = StringUtils.remove(connectorSpaceAdPersonExternalDetails.getDn(), userCn + ",");

                log.info("(Dryrun : {}) Change Person : {} to Organization : {} ",
                        serviceParameters.isDryrun(), oldUserDn, newOrgDn);

                if (!serviceParameters.isDryrun()) {

                    // 取得舊的group dn(萬用群組)
                    String oldGroupDn = adDirectoryService.getGroupDn(oldUserDn);

                    // 從舊萬用群組中移除person
                    LDAPResult removeResult = adDirectoryService
                            .removeOrganizationUnitGroupMember(oldGroupDn, oldUserDn);

                    // 北國也一拼修改全域
                    if (StringUtils.contains(oldGroupDn, MOFType.NTBT.getRdn())) {
                        String globalGroupDn = adDirectoryService.getGroupDn(oldGroupDn,
                                ADGroupType.GLOBAL_SECURITY_GROUP);

                        // 從舊全域群組中移除person
                        removeResult = adDirectoryService
                                .removeOrganizationUnitGroupMember(globalGroupDn, oldUserDn);
                    }

                    if (removeResult.getResultCode().intValue() == LDAPResultCode.LDAP_SUCCESS.getCode()
                            || removeResult.getResultCode().intValue() == LDAPResultCode.LDAP_NO_SUCH_OBJECT.getCode()) {

                        // 將person change到新的ou
                        LDAPResult changeResult = adDirectoryService
                                .changeOrganizationPerson(oldUserDn, userCn, newOrgDn);

                        if (changeResult.getResultCode().intValue() == LDAPResultCode.LDAP_SUCCESS.getCode()) {

                            // 將person新增新ou的gourp member裡
                            addOrganizationPersonGroupMember(connectorSpaceAdPersonExternalDetails.getCn(),
                                    connectorSpaceAdPersonExternalDetails.getDn());
                        }
                    }

                }
            } catch (Exception ex) {

                String stackTrace = ExceptionUtils.getStackTrace(ex);
                stackTrace = "dn: " + connectorSpaceAdPersonExternalDetails.getDn() + "\n" + stackTrace;
                // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                log.error("{}", stackTrace);
                eventLogsService.record("sync", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                health = "N";
            }
        }
    }

    /**
     * 偵測是否需要修改 AD 人屬性
     *
     * @throws Exception
     */
    public void modifyOrganizationPerson() throws Exception {

        // 外網
        if (placeHolder == PlaceHolder.EXTERNAL) {

            // cn相同，syncattrsHash不同，針對屬性異動
            List<Object[]> connectorSpaceAdPersonExternalDetailsList = connectorSpaceAdPersonExternalDetailsRepository
                    .findAllConnectorSpaceAdPersonExternalDetailsInConnectorSpaceAdOrganizationalUnitDetailsWhenAttrsChanged(
                            serviceParameters.getBasedn());
            log.info("Modify OrganizationPerson list size: {}", connectorSpaceAdPersonExternalDetailsList.size());
            modifyExternalOrganizationPerson(connectorSpaceAdPersonExternalDetailsList);
        }
    }

    private void modifyExternalOrganizationPerson(
            List<Object[]> connectorSpaceAdPersonExternalDetailsList) throws Exception {

        for (Object[] entrys : connectorSpaceAdPersonExternalDetailsList) {

            ConnectorSpaceAdPersonExternalDetails connectorSpaceAdPersonExternalDetails = (ConnectorSpaceAdPersonExternalDetails) entrys[0];
            ConnectorSpaceAdPersonDetails connectorSpaceAdPersonDetails = (ConnectorSpaceAdPersonDetails) entrys[1];

            if (StringUtils.endsWithIgnoreCase(connectorSpaceAdPersonExternalDetails.getDn(), domaindn)) {

                try {

                    log.info("(Dryrun : {}) Modify OrganizationPerson : {}->{}",
                            serviceParameters.isDryrun(), connectorSpaceAdPersonDetails,
                            connectorSpaceAdPersonExternalDetails);

                    if (!serviceParameters.isDryrun()) {

                        LDAPResult result = adDirectoryService.modifyOrganizationPerson(
                                connectorSpaceAdPersonExternalDetails.getDn(),
                                connectorSpaceAdPersonExternalDetails.getCn(),
                                connectorSpaceAdPersonExternalDetails.getEmployeeid(),
                                connectorSpaceAdPersonExternalDetails.getSn(),
                                connectorSpaceAdPersonExternalDetails.getOu(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute1(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute2(),
                                connectorSpaceAdPersonExternalDetails.getAccountexpires(),
                                connectorSpaceAdPersonExternalDetails.getUserprincipalname(),
                                connectorSpaceAdPersonExternalDetails.getSamaccountname(),
                                connectorSpaceAdPersonExternalDetails.getTitle(),
                                connectorSpaceAdPersonExternalDetails.getDepartment(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute10(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute11(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute12(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute13(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute14(),
                                connectorSpaceAdPersonExternalDetails.getExtensionattribute15(),
                                connectorSpaceAdPersonExternalDetails.getDisplayname(),
                                connectorSpaceAdPersonExternalDetails.getUseraccountcontrol(),
                                connectorSpaceAdPersonExternalDetails.getUserparameters(),
                                connectorSpaceAdPersonExternalDetails.getPager());

                        if (result.getResultCode().intValue() == LDAPResultCode.LDAP_SUCCESS.getCode()) {

                            // memberOf 屬性如果不同，重新呼叫加入群組
                            // 注意: 如果是ou整個change，因為ou change時底下的group與person其ou會跟著一起更正
                            //      此時舊的group已不存在了，但因為db裡person的memberof還是舊的所以會進行memberof的異動->會失敗
                            //      這個錯誤等下次外網重新scan校正db內的memberof資料就會釐正
                            if (!StringUtils.equalsIgnoreCase(connectorSpaceAdPersonExternalDetails.getMemberOf(),
                                    connectorSpaceAdPersonDetails.getMemberOf())) {

                                if (StringUtils.isNotBlank(connectorSpaceAdPersonDetails.getMemberOf())) {
                                    String adMemberOf = connectorSpaceAdPersonDetails.getMemberOf();

                                    for (String groupDn : StringUtils.split(adMemberOf, ";")) {
                                        // 將 ad 上關聯此 person memberof 的 group，將 person 從其 member 中移除
                                        adDirectoryService.removeOrganizationUnitGroupMember(
                                                groupDn, connectorSpaceAdPersonExternalDetails.getDn());
                                    }
                                }

                                if (StringUtils.isNotBlank(connectorSpaceAdPersonExternalDetails.getMemberOf())) {
                                    String adPersonMemberOf = connectorSpaceAdPersonExternalDetails.getMemberOf();

                                    for (String groupDn : StringUtils.split(adPersonMemberOf, ";")) {
                                        // 根據同步過來 person 的 memberof 重新加入其 group 的 member 中
                                        adDirectoryService.addOrganizationUnitGroupMember(groupDn,
                                                connectorSpaceAdPersonExternalDetails.getDn());
                                    }
                                }
                            }

                            // 在外網轉換內網資料時就將extensionattribute1為0的person轉換為514 suspend了
//                            if (StringUtils.equalsIgnoreCase(
//                                    connectorSpaceAdPersonExternalDetails.getExtensionattribute1(), "0")
//                                    && StringUtils.equalsIgnoreCase(
//                                            connectorSpaceAdPersonDetails.getExtensionattribute1(), "1")) {
//
//                                LDAPResult suspendResult = adDirectoryService.suspendOrganizationPerson(
//                                        connectorSpaceAdPersonExternalDetails.getDn());
//
//                                // 外網信箱由管理員手動管理
//                                // if (suspendResult.getResultCode()
//                                // .intValue() == LDAPResultCode.LDAP_SUCCESS.getCode()) {
//                                //
//                                // exchangeService.disableMailBox(
//                                // connectorSpaceAdPersonDetails.getSamaccountname());
//                                // }
//                            } else if (StringUtils.equalsIgnoreCase(
//                                    connectorSpaceAdPersonExternalDetails.getExtensionattribute1(), "1") && StringUtils
//                                    .equalsIgnoreCase(connectorSpaceAdPersonDetails.getExtensionattribute1(), "0")) {
//
//                                LDAPResult activeResult = adDirectoryService.activeOrganizationPerson(
//                                        connectorSpaceAdPersonExternalDetails.getDn());
//                            }
                        }
                    }
                } catch (Exception ex) {

                    String stackTrace = ExceptionUtils.getStackTrace(ex);
                    stackTrace = "dn: " + connectorSpaceAdPersonExternalDetails.getDn() + "\n" + stackTrace;
                    // 排除非 UTF-8 合法字元（尤其是 0x00 null byte）
                    stackTrace = stackTrace.replace("\u0000", "").replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
                    log.error("{}", stackTrace);
                    eventLogsService.record("sync", serviceParameters.getBasedn(), stackTrace, DataUtils.hash(stackTrace));
                    health = "N";
                }
            }
        }
    }

    /**
     * @throws Exception
     */
//    public void removeOrganizationPerson() throws Exception {
//
//        long count = connectorSpaceAdPersonSyncDetailsRepository.count();
//
//        if (count == 0) {
//
//            log.info("(Dryrun : {}) MetaversePersonDetails is empty, it's unusual case, stop !",
//                    serviceParameters.isDryrun());
//            return;
//        }
//
//        List<ConnectorSpaceAdPersonDetails> connectorSpaceAdPersonDetailsList = connectorSpaceAdPersonDetailsRepository
//                .findAllConnectorSpaceAdPersonDetailsNotInMetaversePersonDetails(domaindn,
//                        placeHolder.name().toLowerCase());
//
//        for (ConnectorSpaceAdPersonDetails connectorSpaceAdPersonDetails : connectorSpaceAdPersonDetailsList) {
//
//            log.info("(Dryrun : {}) Remove OrganizationPerson : {}", serviceParameters.isDryrun(),
//                    connectorSpaceAdPersonDetails);
//
//            if (!serviceParameters.isDryrun()) {
//
//                String groupDn = adDirectoryService
//                        .getGroupDn(connectorSpaceAdPersonDetails.getDn());
//                String userDn = connectorSpaceAdPersonDetails.getDn();
//
//                if (StringUtils.contains(groupDn, "OU=" + MOFType.FIA.getType() + "ROOT")) {
//
//                    LDAPResult result = adDirectoryService
//                            .removeOrganizationUnitGroupMember(groupDn, userDn);
//
//                    if (result.getResultCode().intValue() == LDAPResultCode.LDAP_SUCCESS
//                            .getCode()) {
//
//                        LDAPResult removeResult = adDirectoryService
//                                .suspendOrganizationPerson(userDn);
//                        if (removeResult.getResultCode().intValue() == LDAPResultCode.LDAP_SUCCESS
//                                .getCode()) {
//
//                            exchangeService.disableMailBox(
//                                    connectorSpaceAdPersonDetails.getSamaccountname());
//                        }
//                    } else {
//
//                        log.error("   remove user {} from group member {} fails", userDn, groupDn);
//                    }
//                } else {
//
//                    LDAPResult result = adDirectoryService.removeOrganizationPerson(userDn);
//
//                    if (result.getResultCode().intValue() == LDAPResultCode.LDAP_SUCCESS
//                            .getCode()) {
//
//                        // 11/19 暫時先註刪，不實體刪除
//                        // connectorSpaceAdPersonDetailsRepository.delete(connectorSpaceAdPersonDetails);
//                        LDAPResult removeResult = adDirectoryService
//                                .suspendOrganizationPerson(userDn);
//                        if (removeResult.getResultCode().intValue() == LDAPResultCode.LDAP_SUCCESS
//                                .getCode()) {
//
//                            exchangeService.disableMailBox(
//                                    connectorSpaceAdPersonDetails.getSamaccountname());
//                        }
//                    } else
//                        log.error("   delete user {} fails", userDn);
//                }
//            }
//        }
//    }

}

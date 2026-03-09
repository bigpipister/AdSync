package com.cht.directory.connector.sync.service;

import com.cht.directory.connector.service.ActiveDirectoryService;
import com.cht.directory.connector.sync.config.ServiceParameters;
import com.cht.directory.connector.type.ADGroupType;
import com.cht.directory.domain.ConnectorSpaceAdGroupDetails;
import com.cht.directory.repository.ConnectorSpaceAdGroupDetailsRepository;
import com.unboundid.ldap.sdk.LDAPResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GroupMemberOfCorrectionService {

    @Autowired
    private ServiceParameters serviceParameters;

    @Autowired
    private ConnectorSpaceAdGroupDetailsRepository connectorSpaceAdGroupDetailsRepository;

    @Autowired
    private ActiveDirectoryService adDirectoryService;

    private String health = "Y";

    public String executeCorrection() throws Exception {

        correction();

        return health;
    }

    public void correction() throws Exception {

        List<ConnectorSpaceAdGroupDetails> connectorSpaceAdGroupDetailsList =
                connectorSpaceAdGroupDetailsRepository
                        .findAllConnectorSpaceAdGroupDetailsAndPlaceholderWithBaseDn(
                                serviceParameters.getPlaceholder(), serviceParameters.getBasedn());

        for (ConnectorSpaceAdGroupDetails connectorSpaceAdGroupDetails : connectorSpaceAdGroupDetailsList) {
            String groupCn = connectorSpaceAdGroupDetails.getCn();
            String groupDn = connectorSpaceAdGroupDetails.getDn();
            ADGroupType adGroupType = ADGroupType
                    .findByAdType(connectorSpaceAdGroupDetails.getGroupType());
            String orgDn = adDirectoryService.removeCn(groupDn);

            // 萬用群組
            if (adGroupType == ADGroupType.UNIVERSAL_SECURITY_GROUP) {

                String parentGroupDn = adDirectoryService.getParentGroupDn(orgDn,
                        ADGroupType.UNIVERSAL_SECURITY_GROUP);

                // 萬用群組新增至上層 OU 萬用群組成員
                if (StringUtils.isNotBlank(parentGroupDn) &&
                        !connectorSpaceAdGroupDetails.getMemberOf().contains(parentGroupDn)) {

                    log.info("Add OrganizationGroup : {} to a member of Group : {} ", groupDn, parentGroupDn);
                    // 將萬用群組加入上層萬用群組member
                    adDirectoryService.addOrganizationUnitGroupMember(parentGroupDn, groupDn);
                }
            // 全域群組
            } else if (adGroupType == ADGroupType.GLOBAL_SECURITY_GROUP) {

                String parentGlobalGroupDn = adDirectoryService.getParentGroupDn(orgDn,
                                    ADGroupType.GLOBAL_SECURITY_GROUP);

                if (StringUtils.isNotBlank(parentGlobalGroupDn) &&
                        !connectorSpaceAdGroupDetails.getMemberOf().contains(parentGlobalGroupDn)) {

                    log.info("Add OrganizationGroup : {} to a member of Group : {} ", groupDn, parentGlobalGroupDn);
                    // 將全域群組加入上層全域群組member
                    LDAPResult gmAddresult = adDirectoryService
                            .addOrganizationUnitGroupMember(parentGlobalGroupDn, groupDn);
                }

                // 取得網域本機群組 dn
                String domainLocalGroupDn = "CN="
                        + groupCn.substring(0, groupCn.length() - 3) + "-DL," + orgDn;

                if (StringUtils.isNotBlank(domainLocalGroupDn) &&
                        !connectorSpaceAdGroupDetails.getMemberOf().contains(domainLocalGroupDn)) {
                    log.info("Add OrganizationGroup : {} to a member of Group : {} ", groupDn, domainLocalGroupDn);
                    // 將全域群組加入上層全域群組member
                    LDAPResult gmAddresult = adDirectoryService
                            .addOrganizationUnitGroupMember(domainLocalGroupDn, groupDn);
                }
            }
        }
    }

}

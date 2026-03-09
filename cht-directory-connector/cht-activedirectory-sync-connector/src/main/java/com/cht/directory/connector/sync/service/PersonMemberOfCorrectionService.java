package com.cht.directory.connector.sync.service;

import com.cht.directory.connector.service.ActiveDirectoryService;
import com.cht.directory.connector.sync.config.ServiceParameters;
import com.cht.directory.connector.type.ADGroupType;
import com.cht.directory.connector.type.MOFType;
import com.cht.directory.domain.ConnectorSpaceAdPersonDetails;
import com.cht.directory.repository.ConnectorSpaceAdPersonDetailsRepository;
import com.unboundid.ldap.sdk.LDAPResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class PersonMemberOfCorrectionService {

    @Autowired
    private ServiceParameters serviceParameters;

    @Autowired
    private ConnectorSpaceAdPersonDetailsRepository connectorSpaceAdPersonDetailsRepository;

    @Autowired
    private ActiveDirectoryService adDirectoryService;

    private String health = "Y";

    /**
     * @throws Exception
     */
    public String executeCorrection() throws Exception {

        correction();

        return health;
    }

    /**
     * @throws Exception
     */
    public void correction() throws Exception {

        List<ConnectorSpaceAdPersonDetails> connectorSpaceAdPersonDetailsList =
                connectorSpaceAdPersonDetailsRepository
                        .findAllConnectorSpaceAdPersonDetailsAndPlaceholderWithBaseDn(
                                serviceParameters.getPlaceholder(), serviceParameters.getBasedn());

        for (ConnectorSpaceAdPersonDetails connectorSpaceAdPersonDetails : connectorSpaceAdPersonDetailsList) {
            String groupDn = adDirectoryService.getGroupDn(connectorSpaceAdPersonDetails.getDn());
            if (!connectorSpaceAdPersonDetails.getMemberOf().contains(groupDn)) {
                // person 預設都會加同ou的萬用群組，北國會額外多加同ou的全域群組
                addOrganizationPersonGroupMember(connectorSpaceAdPersonDetails.getCn(),
                        connectorSpaceAdPersonDetails.getDn());
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

}

package com.cht.directory.connector.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cht.directory.connector.service.objects.ModifyObject;
import com.cht.directory.connector.type.AuditLogsCategoryType;
import com.unboundid.ldap.sdk.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EdirectoryService extends BaseDirectoryService {

    @Autowired
    public EdirectoryService(LDAPConnectionPool connectionPool) {

        super(connectionPool);
    }

    public SearchResult searchOrganizationUnit(String baseDn, String modifytimestamp,
            String[] returnAttrs) throws Exception {

        List<Filter> filters = new ArrayList<>();

        filters.add(Filter.createPresenceFilter("ou"));
        filters.add(Filter.createEqualityFilter("objectClass", "organizationalUnit"));

        // 排除 YBD 暫存群組
        filters.add(Filter.createNOTFilter(
                Filter.createSubstringFilter("ou", null, new String[] { "_YBDTEMP" }, null)));

        SearchResult searchResult = search(baseDn, filters, modifytimestamp, returnAttrs);

        return searchResult;
    }

    public SearchResult searchOrganizationPerson(String baseDn, String modifytimestamp,
            String[] returnAttrs) throws Exception {

        List<Filter> filters = new ArrayList<>();

        filters.add(Filter.createPresenceFilter("cn"));
        filters.add(Filter.createEqualityFilter("objectClass", "organizationalPerson"));
        filters.add(Filter.createNOTFilter(
                Filter.createSubstringFilter("cn", null, new String[] { "_" }, null)));
        // TODO 排除 YBD 暫存群組

        SearchResult searchResult = search(baseDn, filters, modifytimestamp, returnAttrs);
        return searchResult;
    }

    public SearchResult searchOrganizationPersonByCn(String baseDn, String cn, String[] returnAttrs)
            throws Exception {

        List<Filter> filters = new ArrayList<>();

        filters.add(Filter.createEqualityFilter("cn", cn));
        filters.add(Filter.createEqualityFilter("objectClass", "organizationalPerson"));

        SearchResult searchResult = search(baseDn, filters, null, returnAttrs);
        return searchResult;
    }

    public LDAPResult modifyOrganizationPersonEmail(String userDn, String email) throws Exception {

        SearchResultEntry entry = getEntry(userDn);

        if (null == entry) {

            log.error("  OrganizationPerson : {} is not existed.", userDn);
            return entryNotExistedResult;
        }

        ModifyObject.Modification modification = new ModifyObject.Modification();

        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("mail");
        modification.setValues(new String[] { email });

        List<ModifyObject.Modification> modifications = Arrays.asList(modification);

        ModifyObject modifyObject = new ModifyObject();

        modifyObject
                .setAuditLogsCategoryType(AuditLogsCategoryType.UserManagement_UpdateUserAttribute);
        modifyObject.setLogname(this.getClass().getName());
        modifyObject.setModifications(modifications);
        modifyObject.setDn(userDn);

        return modify(modifyObject);
    }

    public LDAPResult changeUserPassword(String userDn, String password, String unicodePwdHash,
            Timestamp ldapPwdlastsetTime, Timestamp adPwdLastsetTime) throws Exception {

        SearchResultEntry entry = getEntry(userDn);

        if (null == entry) {

            log.error("  OrganizationPerson : {} is not existed.", userDn);
            return entryNotExistedResult;
        }

        ModifyObject.Modification modification = new ModifyObject.Modification();

        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("userPassword");
        modification.setValues(new String[] { password });

        List<ModifyObject.Modification> modifications = Arrays.asList(modification);

        String ldapPwdlastset = (null != ldapPwdlastsetTime) ? ldapPwdlastsetTime.toString()
                : "N/A";
        String adPwdlastset = (null != adPwdLastsetTime) ? adPwdLastsetTime.toString() : "N/A";

        ModifyObject modifyObject = new ModifyObject();

        modifyObject.setAuditLogsCategoryType(
                AuditLogsCategoryType.PasswordManagement_UserPasswordChanges);
        modifyObject.setModifications(modifications);
        modifyObject.setDn(userDn);
        modifyObject.setAdditionalDetails(
                "dn: " + userDn + "\n" + "changetype: modify\n" + "replace: userPassword\n"
                        + "userPassword : " + unicodePwdHash + "\n-- LDAP pwdlastset: "
                        + ldapPwdlastset + "\n-- AD pwdlastset: " + adPwdlastset);

        return modify(modifyObject);
    }
}

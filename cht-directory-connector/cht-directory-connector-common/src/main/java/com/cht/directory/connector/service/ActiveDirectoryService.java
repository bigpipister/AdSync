package com.cht.directory.connector.service;

import java.sql.Timestamp;
import java.util.*;

import javax.naming.Name;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import com.cht.directory.connector.service.error.LDAPResultCode;
import com.cht.directory.connector.service.objects.AddObject;
import com.cht.directory.connector.service.objects.DeleteObject;
import com.cht.directory.connector.service.objects.ModifyObject;
import com.cht.directory.connector.service.utils.DateTimeUtils;
import com.cht.directory.connector.type.ADGroupType;
import com.cht.directory.connector.type.AuditLogsCategoryType;
import com.cht.directory.connector.type.AuditLogsResult;
import com.unboundid.ldap.sdk.*;
import com.unboundid.ldif.LDIFChangeRecord;
import com.unboundid.ldif.LDIFModifyChangeRecord;
import com.unboundid.ldif.LDIFModifyDNChangeRecord;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActiveDirectoryService extends BaseDirectoryService {

    @Value("${activedirectory.service.domaindn}")
    private String domaindn;

    @Value("${activedirectory.service.mofdn:}")
    private String mofdn;

    @Value("${activedirectory.service.organization.objectclass:}")
    private String ouObjectClass;

    @Value("${activedirectory.service.organization.objectcategory:}")
    private String ouObjectCategory;

    @Value("${activedirectory.service.group.objectclass:}")
    private String groupObjectClass;

    @Value("${activedirectory.service.group.objectcategory:}")
    private String groupObjectCategory;

    @Value("${activedirectory.service.person.objectclass:}")
    private String personObjectClass;

    @Value("${activedirectory.service.person.objectcategory:}")
    private String personObjectCategory;

    @Autowired
    public ActiveDirectoryService(LDAPConnectionPool connectionPool) {

        super(connectionPool);
    }

    public SearchResult searchOrganizationUnit(String baseDn, String modifytimestamp,
            String[] returnAttrs) throws Exception {

        List<Filter> filters = new ArrayList<>();

        filters.add(Filter.createPresenceFilter("ou"));
        filters.add(Filter.createEqualityFilter("objectClass", "organizationalUnit"));

        SearchResult searchResult = search(baseDn, filters, modifytimestamp, returnAttrs);

        return searchResult;
    }

    public SearchResult searchOrganizationPerson(String baseDn, String modifytimestamp,
            String[] returnAttrs) throws Exception {

        List<Filter> filters = new ArrayList<>();

        filters.add(Filter.createPresenceFilter("cn"));
        filters.add(Filter.createEqualityFilter("objectClass", "organizationalPerson"));
        // 1210 排除 AD 聯絡人
        filters.add(Filter.createNOTFilter(Filter.createEqualityFilter("objectClass", "contact")));

        SearchResult searchResult = search(baseDn, filters, modifytimestamp, returnAttrs);

        return searchResult;
    }

    public SearchResult searchOrganizationPersonByCn(String baseDn, String cn, String[] returnAttrs)
            throws Exception {

        List<Filter> filters = new ArrayList<>();

        filters.add(Filter.createEqualityFilter("cn", cn));
        filters.add(Filter.createEqualityFilter("objectClass", "organizationalPerson"));
        // 1210 排除 AD 聯絡人
        filters.add(Filter.createNOTFilter(Filter.createEqualityFilter("objectClass", "contact")));

        SearchResult searchResult = search(baseDn, filters, null, returnAttrs);

        return searchResult;
    }

    public SearchResult searchOrganizationGroup(String baseDn, String modifytimestamp,
            String[] returnAttrs) throws Exception {

        List<Filter> filters = new ArrayList<>();

        filters.add(Filter.createPresenceFilter("cn"));
        filters.add(Filter.createEqualityFilter("objectClass", "group"));

        SearchResult searchResult = search(baseDn, filters, modifytimestamp, returnAttrs);

        return searchResult;
    }

    public SearchResult searchOrganizationGroupByCn(String baseDn, String cn, String[] returnAttrs) throws Exception {

        List<Filter> filters = new ArrayList<>();

        filters.add(Filter.createEqualityFilter("cn", cn));
        filters.add(Filter.createEqualityFilter("objectClass", "group"));

        SearchResult searchResult = search(baseDn, filters, null, returnAttrs);

        return searchResult;
    }

    public LDAPResult addGroup(String groupDn, String groupCn, String groupType, String displayName,
                               String extensionAttribute10, String extensionAttribute11, String extensionAttribute12,
                               String extensionAttribute13, String extensionAttribute14, String extensionAttribute15)
            throws Exception {

        SearchResultEntry entry = getEntry(groupDn);

        if (null != entry) {

            log.warn("  Group : {} is existed.", groupDn);
            return entryExistedResult;
        }

        Map<String, String> attributes = new HashMap<>();

        attributes.put("cn", groupCn);
        attributes.put("name", groupCn);
        attributes.put("sAMAccountName", groupCn);
        attributes.put("groupType", groupType);
        if (StringUtils.isNotEmpty(displayName)) {
            attributes.put("displayName", displayName);
        }
        if (StringUtils.isNotEmpty(extensionAttribute10)) {
            attributes.put("extensionAttribute10", extensionAttribute10);
        }
        if (StringUtils.isNotEmpty(extensionAttribute11)) {
            attributes.put("extensionAttribute11", extensionAttribute11);
        }
        if (StringUtils.isNotEmpty(extensionAttribute12)) {
            attributes.put("extensionAttribute12", extensionAttribute12);
        }
        if (StringUtils.isNotEmpty(extensionAttribute13)) {
            attributes.put("extensionAttribute13", extensionAttribute13);
        }
        if (StringUtils.isNotEmpty(extensionAttribute14)) {
            attributes.put("extensionAttribute14", extensionAttribute14);
        }
        if (StringUtils.isNotEmpty(extensionAttribute15)) {
            attributes.put("extensionAttribute15", extensionAttribute15);
        }
        attributes.put("objectClass", groupObjectClass);
        attributes.put("objectCategory", groupObjectCategory + "," + domaindn);

        AddObject addObject = new AddObject();

        addObject.setAuditLogsCategoryType(AuditLogsCategoryType.GroupManagement_CreateGroup);
        addObject.setLogname(this.getClass().getName());
        addObject.setAttributes(attributes);
        addObject.setDn(groupDn);

        LDAPResult ldapResult = add(addObject);

        return ldapResult;
    }

    public LDAPResult changeGroup(String oldGroupDn, String groupCn, String newOrgDn)
            throws Exception {

        // TODO
        LDIFChangeRecord ldifChangeRecord = new LDIFModifyDNChangeRecord(oldGroupDn, groupCn, true,
                newOrgDn);
        LDAPResult changeResult = ldifChangeRecord.processChange(connectionPool);

        log.info("  result = {}", changeResult);

        return changeResult;
    }

    public LDAPResult modifyGroup(String groupDn, String displayName,
                                  String extensionAttribute10, String extensionAttribute11, String extensionAttribute12,
                                  String extensionAttribute13, String extensionAttribute14, String extensionAttribute15) throws Exception {

        SearchResultEntry entry = getEntry(groupDn);

        if (null == entry) {

            log.error("  Group : {} is not existed.", groupDn);
            return entryNotExistedResult;
        }

        List<ModifyObject.Modification> modifications = new ArrayList<>();

        ModifyObject.Modification modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("displayName");
        if (StringUtils.isNotEmpty(displayName)) {
            modification.setValues(new String[] { displayName });
        } else {
            modification.setValues(new String[] {});
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("extensionAttribute10");
        if (StringUtils.isNotEmpty(extensionAttribute10)) {
            modification.setValues(new String[] { extensionAttribute10 });
        } else {
            modification.setValues(new String[] {});
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("extensionAttribute11");
        if (StringUtils.isNotEmpty(extensionAttribute11)) {
            modification.setValues(new String[] { extensionAttribute11 });
        } else {
            modification.setValues(new String[] {});
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("extensionAttribute12");
        if (StringUtils.isNotEmpty(extensionAttribute12)) {
            modification.setValues(new String[] { extensionAttribute12 });
        } else {
            modification.setValues(new String[] {});
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("extensionAttribute13");
        if (StringUtils.isNotEmpty(extensionAttribute13)) {
            modification.setValues(new String[] { extensionAttribute13 });
        } else {
            modification.setValues(new String[] {});
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("extensionAttribute14");
        if (StringUtils.isNotEmpty(extensionAttribute14)) {
            modification.setValues(new String[] { extensionAttribute14 });
        } else {
            modification.setValues(new String[] {});
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("extensionAttribute15");
        if (StringUtils.isNotEmpty(extensionAttribute15)) {
            modification.setValues(new String[] { extensionAttribute15 });
        } else {
            modification.setValues(new String[] {});
        }
        modifications.add(modification);

//        for (int index = 10; index < 16; index++) {
//
//            ModifyObject.Modification modification = new ModifyObject.Modification();
//
//            modification.setType(ModificationType.REPLACE_INT_VALUE);
//            modification.setAttribute("extensionAttribute" + index);
//            modification.setValues(new String[] { displayName });
//
//            modifications.add(modification);
//        }

        ModifyObject modifyObject = new ModifyObject();

        modifyObject.setAuditLogsCategoryType(
                AuditLogsCategoryType.GroupManagement_UpdateGroupAttribute);
        modifyObject.setLogname(this.getClass().getName());
        modifyObject.setModifications(modifications);
        modifyObject.setDn(groupDn);

        return modify(modifyObject);
    }

    public LDAPResult addOrganizationUnit(String dn, String ou, String displayName)
            throws Exception {

        SearchResultEntry entry = getEntry(dn);

        if (null != entry) {

            log.warn("  OrganizationUnit : {} is already existed.", dn);
            return entryExistedResult;
        }

        Map<String, String> attributes = new HashMap<>();

        attributes.put("ou", ou);
        if (StringUtils.isNotEmpty(displayName)) {
            attributes.put("displayName", displayName);
        }
        attributes.put("objectClass", ouObjectClass);
        attributes.put("objectCategory", ouObjectCategory + "," + domaindn);

        AddObject addObject = new AddObject();

        addObject.setAuditLogsCategoryType(
                AuditLogsCategoryType.OrganizationalUnitManagement_CreateOrganizationalUnit);
        addObject.setLogname(this.getClass().getName());
        addObject.setAttributes(attributes);
        addObject.setDn(dn);

        LDAPResult ldapResult = add(addObject);

        return ldapResult;
    }

    public LDAPResult changeOrganizationUnit(String oldOuDn, String ou, String newOuDn)
            throws Exception {

        // TODO
        LDIFChangeRecord ldifChangeRecord = new LDIFModifyDNChangeRecord(oldOuDn, ou, true,
                newOuDn);
        LDAPResult changeResult = ldifChangeRecord.processChange(connectionPool);

        log.info("result = {}", changeResult);

        return changeResult;
    }

    public boolean checkOrganizationUnitGroupMember(String groupDn, String memberDn)
            throws Exception {

        Filter filter = Filter.createEqualityFilter("member", memberDn);

        SearchResult searchResult = search(groupDn, SearchScope.BASE, filter, "cn", "member");

        return searchResult.getEntryCount() > 0;
    }

    public LDAPResult addOrganizationUnitGroupMember(String groupDn, String memberDn)
            throws Exception {

        if (checkOrganizationUnitGroupMember(groupDn, memberDn)) {

            log.warn("  Entry : {} is already a member of Group : {}", memberDn, groupDn);
            return entryExistedResult;
        }

        Long startTime = new Date().getTime();
        LDIFChangeRecord ldifChangeRecord = new LDIFModifyChangeRecord(groupDn,
                new Modification(ModificationType.ADD, "member", memberDn));

        LDAPResult result = ldifChangeRecord.processChange(connectionPool);
        Long endTime = new Date().getTime();

        log.info("  result = {}", result);

        AuditLogsResult auditLogsResult = (result.getResultCode()
                .intValue() == LDAPResultCode.LDAP_SUCCESS.getCode()) ? AuditLogsResult.SUCCESS
                        : AuditLogsResult.FAILURE;

        auditLogsService.audit(
                AuditLogsCategoryType.UserManagement_AddMemberToGroup.getActivityDisplayName(),
                ldifChangeRecord.toLDIFString(),
                AuditLogsCategoryType.UserManagement_AddMemberToGroup.getCategory(), "",
                this.getClass().getName(), auditLogsResult, result.getResultCode().intValue(),
                result.getResultString(), memberDn, endTime - startTime);

        return result;
    }

    public LDAPResult removeOrganizationUnitGroupMember(String groupDn, String memberDn)
            throws Exception {

        Filter filter = Filter.createEqualityFilter("member", memberDn);

        SearchResultEntry entry = getEntry(groupDn);

        if (null == entry) {

            log.warn("  Group to remove member : {} is not existed", groupDn);
            return entryNotExistedResult;
        }

        SearchResult searchResult = search(groupDn, SearchScope.BASE, filter, "cn", "member");

        if (searchResult.getEntryCount() <= 0) {

            log.warn("  Entry : {} is not a member of Group : {}, can't remove member", memberDn,
                    groupDn);
            return entryNotExistedResult;
        }

        Long startTime = new Date().getTime();
        LDIFChangeRecord ldifChangeRecord = new LDIFModifyChangeRecord(groupDn,
                new Modification(ModificationType.DELETE, "member", memberDn));

        LDAPResult result = ldifChangeRecord.processChange(connectionPool);
        Long endTime = new Date().getTime();

        log.info("  result = {}", result);

        AuditLogsResult auditLogsResult = (result.getResultCode()
                .intValue() == LDAPResultCode.LDAP_SUCCESS.getCode()) ? AuditLogsResult.SUCCESS
                        : AuditLogsResult.FAILURE;

        auditLogsService.audit(
                AuditLogsCategoryType.UserManagement_RemoveMemberFromGroup.getActivityDisplayName(),
                ldifChangeRecord.toLDIFString(),
                AuditLogsCategoryType.UserManagement_AddMemberToGroup.getCategory(), "",
                this.getClass().getName(), auditLogsResult, result.getResultCode().intValue(),
                result.getResultString(), memberDn, endTime - startTime);

        return result;
    }

    public LDAPResult modifyOrganizationUnit(String orgDn, String displayName) throws Exception {

        SearchResultEntry entry = getEntry(orgDn);

        if (null == entry) {

            log.error("  OrganizationUnit : {} is not existed.", orgDn);
            return entryNotExistedResult;
        }

        ModifyObject.Modification modification = new ModifyObject.Modification();

        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("displayName");
        if (StringUtils.isNotEmpty(displayName)) {
            modification.setValues(new String[] { displayName });
        } else {
            modification.setValues(new String[] {  });
        }

        List<ModifyObject.Modification> modifications = Arrays.asList(modification);

        ModifyObject modifyObject = new ModifyObject();

        modifyObject.setAuditLogsCategoryType(
                AuditLogsCategoryType.OrganizationalUnitManagement_UpdateOrganizationalUnitAttribute);
        modifyObject.setLogname(this.getClass().getName());
        modifyObject.setModifications(modifications);
        modifyObject.setDn(orgDn);

        return modify(modifyObject);
    }

    public LDAPResult addOrganizationPerson(String dn, String cn, String employeeId, String sn,
            String ou, String extensionAttribute1, String extensionAttribute2,
            Timestamp accountExpires, String userPrincipalname, String sAMAccountName, String title,
            String department, String extensionAttribute10, String extensionAttribute11,
            String extensionAttribute12, String extensionAttribute13, String extensionAttribute14,
            String extensionAttribute15, String displayName, byte[] unicodePwd,
            int userAccountControl, String userParameters, String pager) throws Exception {

        Filter filter = Filter.createEqualityFilter("userPrincipalName", userPrincipalname);

        SearchResult searchResult = search(mofdn, SearchScope.SUB, filter, "cn");

        if (searchResult.getEntryCount() > 0) {

            log.warn("  OrganizationPerson : {} is already existed.",
                    searchResult.getSearchEntries().get(0).getDN());
            return entryExistedResult;
        }

        Map<String, String> attributes = new HashMap<>();

        attributes.put("cn", cn);

        if (StringUtils.isNotBlank(employeeId))
            attributes.put("employeeID", employeeId);
        if (StringUtils.isNotBlank(sn))
            attributes.put("sn", sn);
        if (StringUtils.isNotBlank(ou))
            attributes.put("ou", ou);
        if (StringUtils.isNotBlank(extensionAttribute1))
            attributes.put("extensionAttribute1", extensionAttribute1);
        if (StringUtils.isNotBlank(extensionAttribute2))
            attributes.put("extensionAttribute2", extensionAttribute2);
        if (!ObjectUtils.isEmpty(accountExpires))
            attributes.put("accountExpires", DateTimeUtils.timestamptoMsAD(accountExpires));

        attributes.put("userPrincipalName", userPrincipalname);
        attributes.put("sAMAccountName", sAMAccountName);

        // 當為空時，不新增該屬性
        if (StringUtils.isNotBlank(title))
            attributes.put("title", title);
        if (StringUtils.isNotBlank(department))
            attributes.put("department", department);
        if (StringUtils.isNotBlank(extensionAttribute10))
            attributes.put("extensionAttribute10", extensionAttribute10);
        if (StringUtils.isNotBlank(extensionAttribute11))
            attributes.put("extensionAttribute11", extensionAttribute11);
        if (StringUtils.isNotBlank(extensionAttribute12))
            attributes.put("extensionAttribute12", extensionAttribute12);
        if (StringUtils.isNotBlank(extensionAttribute13))
            attributes.put("extensionAttribute13", extensionAttribute13);
        if (StringUtils.isNotBlank(extensionAttribute14))
            attributes.put("extensionAttribute14", extensionAttribute14);
        if (StringUtils.isNotBlank(extensionAttribute15))
            attributes.put("extensionAttribute15", extensionAttribute15);
        if (StringUtils.isNotBlank(displayName))
            attributes.put("displayName", displayName);

        attributes.put("userAccountControl", "" + userAccountControl);

        if (StringUtils.isNotBlank(userParameters))
            attributes.put("userParameters", userParameters);
        if (StringUtils.isNotBlank(pager))
            attributes.put("pager", pager);

        attributes.put("objectClass", personObjectClass);
        attributes.put("objectCategory", personObjectCategory + "," + domaindn);

        Map<String, String> binAttributes = new HashMap<>();

        if (!ObjectUtils.isEmpty(unicodePwd)) {
            binAttributes.put("unicodePwd", Base64.getEncoder().encodeToString(unicodePwd));
        }

        AddObject addObject = new AddObject();

        addObject.setAuditLogsCategoryType(AuditLogsCategoryType.UserManagement_CreateUser);
        addObject.setLogname(this.getClass().getName());
        addObject.setAttributes(attributes);
        addObject.setBinaryAttributes(binAttributes);
        addObject.setDn(dn);

        return add(addObject);
    }

    public LDAPResult changeOrganizationPerson(String oldUserDn, String userCn, String newOrgDn)
            throws Exception {

        // TODO
        LDIFChangeRecord ldifChangeRecord = new LDIFModifyDNChangeRecord(oldUserDn, userCn, true,
                newOrgDn);
        LDAPResult changeResult = ldifChangeRecord.processChange(connectionPool);

        log.info("  result = {}", changeResult);

        return changeResult;
    }

    public LDAPResult unlockOrganizationPerson(String dn) throws Exception {

        SearchResultEntry entry = getEntry(dn);

        if (null == entry) {

            log.error("  OrganizationPerson : {} is not existed.", dn);
            return entryNotExistedResult;
        }

        List<ModifyObject.Modification> modifications = new ArrayList<>();

        ModifyObject.Modification modification = new ModifyObject.Modification();

        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("lockoutTime");
        modification.setValues(new String[] { "0" });

        modifications.add(modification);

        ModifyObject modifyObject = new ModifyObject();

        modifyObject.setAuditLogsCategoryType(AuditLogsCategoryType.UserManagement_UnlockUser);
        modifyObject.setModifications(modifications);
        modifyObject.setDn(dn);

        return modify(modifyObject);
    }

    public LDAPResult modifyOrganizationPerson(String dn, String cn, String employeeId, String sn,
            String ou, String extensionAttribute1, String extensionAttribute2,
            Timestamp accountExpires, String userPrincipalname, String sAMAccountName, String title,
            String department, String extensionAttribute10, String extensionAttribute11,
            String extensionAttribute12, String extensionAttribute13, String extensionAttribute14,
            String extensionAttribute15, String displayName, int userAccountControl,
            String userParameters, String pager) throws Exception {

        SearchResultEntry entry = getEntry(dn);

        if (null == entry) {

            log.error("  OrganizationPerson : {} is not existed.", dn);
            return entryNotExistedResult;
        }

        List<ModifyObject.Modification> modifications = new ArrayList<>();

        ModifyObject.Modification modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("employeeID");
        if (!StringUtils.isEmpty(employeeId)) {
            modification.setValues(new String[] { employeeId });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        // 系統保護屬性，不能 DELETE 或 REPLACE 空值
        if (!StringUtils.isEmpty(sn)) {
            modification = new ModifyObject.Modification();
            modification.setType(ModificationType.REPLACE_INT_VALUE);
            modification.setAttribute("sn");
            modification.setValues(new String[] { sn });
            modifications.add(modification);
        }

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("ou");
        if (!StringUtils.isEmpty(ou)) {
            modification.setValues(new String[] { ou });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("extensionAttribute1");
        if (!StringUtils.isEmpty(extensionAttribute1)) {
            modification.setValues(new String[] { extensionAttribute1 });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("extensionAttribute2");
        if (!StringUtils.isEmpty(extensionAttribute2)) {
            modification.setValues(new String[] { extensionAttribute2 });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        if (!ObjectUtils.isEmpty(accountExpires)) {
            modification = new ModifyObject.Modification();
            modification.setType(ModificationType.REPLACE_INT_VALUE);
            modification.setAttribute("accountExpires");
            modification.setValues(new String[] { DateTimeUtils.timestamptoMsAD(accountExpires) });
            modifications.add(modification);
        }

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("userPrincipalname");
        if (!StringUtils.isEmpty(userPrincipalname)) {
            modification.setValues(new String[] { userPrincipalname });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        // 系統保護屬性，不能 DELETE 或 REPLACE 空值
        if (!StringUtils.isEmpty(sn)) {
            modification = new ModifyObject.Modification();
            modification.setType(ModificationType.REPLACE_INT_VALUE);
            modification.setAttribute("sAMAccountName");
            modification.setValues(new String[] { sAMAccountName });
            modifications.add(modification);
        }

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("title");
        if (!StringUtils.isEmpty(title)) {
            modification.setValues(new String[] { title });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("department");
        if (!StringUtils.isEmpty(department)) {
            modification.setValues(new String[] { department });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("extensionAttribute10");
        if (!StringUtils.isEmpty(extensionAttribute10)) {
            modification.setValues(new String[] { extensionAttribute10 });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("extensionAttribute11");
        if (!StringUtils.isEmpty(extensionAttribute11)) {
            modification.setValues(new String[] { extensionAttribute11 });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("extensionAttribute12");
        if (!StringUtils.isEmpty(extensionAttribute12)) {
            modification.setValues(new String[] { extensionAttribute12 });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("extensionAttribute13");
        if (!StringUtils.isEmpty(extensionAttribute13)) {
            modification.setValues(new String[] { extensionAttribute13 });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("extensionAttribute14");
        if (!StringUtils.isEmpty(extensionAttribute14)) {
            modification.setValues(new String[] { extensionAttribute14 });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("extensionAttribute15");
        if (!StringUtils.isEmpty(extensionAttribute15)) {
            modification.setValues(new String[] { extensionAttribute15 });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("displayName");
        if (!StringUtils.isEmpty(displayName)) {
            modification.setValues(new String[] { displayName });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("userAccountControl");
        modification.setValues(new String[] { userAccountControl + "" });
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("userParameters");
        if (!StringUtils.isEmpty(userParameters)) {
            modification.setValues(new String[] { userParameters });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        modification = new ModifyObject.Modification();
        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("pager");
        if (!StringUtils.isEmpty(pager)) {
            modification.setValues(new String[] { pager });
        } else {
            modification.setValues(new String[0]);
        }
        modifications.add(modification);

        ModifyObject modifyObject = new ModifyObject();
        modifyObject.setAuditLogsCategoryType(AuditLogsCategoryType.UserManagement_UpdateUserAttribute);
        modifyObject.setModifications(modifications);
        modifyObject.setDn(dn);

        return modify(modifyObject);
    }

    public LDAPResult changeUserPassword(String userDn, byte[] unicodePwd, String unicodePwdaHash,
            Timestamp ldapPwdlastsetTime, Timestamp adPwdLastsetTime) throws Exception {

        SearchResultEntry entry = getEntry(userDn);

        if (null == entry) {

            log.error("  OrganizationPerson : {} is not existed.", userDn);
            return entryNotExistedResult;
        }

        ModifyObject.Modification modification = new ModifyObject.Modification();

        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("unicodePwd");
        modification.setBinary(true);
        modification.setValues(new String[] { Base64.getEncoder().encodeToString(unicodePwd) });

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
                        + "userPassword : " + unicodePwdaHash + "\n-- LDAP pwdlastset: "
                        + ldapPwdlastset + "\n-- AD pwdlastset: " + adPwdlastset);

        return modify(modifyObject);
    }

    /**
     * 啟用 AD User
     * 
     * @param userDn
     * @return
     * @throws Exception
     */
    public LDAPResult activeOrganizationPerson(String userDn) throws Exception {

        return controlOrganizationPerson(userDn, "512", "ACTIVE");
    }

    /**
     * 停用 AD User
     * 
     * @param userDn
     * @return
     * @throws Exception
     */
    public LDAPResult suspendOrganizationPerson(String userDn) throws Exception {

        return controlOrganizationPerson(userDn, "514", "SUSPEND");
    }

    /**
     * 註記刪除 AD User
     * 
     * @param userDn
     * @return
     * @throws Exception
     */
    public LDAPResult markDeleteOrganizationPerson(String userDn) throws Exception {

        return controlOrganizationPerson(userDn, "514", "DELETE");
    }

    private LDAPResult controlOrganizationPerson(String userDn, String userAccountControl,
            String userParameters) throws Exception {

        SearchResultEntry entry = getEntry(userDn);

        if (null == entry) {

            log.error("  OrganizationPerson : {} is not existed.", userDn);
            return entryNotExistedResult;
        }

        ModifyObject.Modification modification = new ModifyObject.Modification();

        modification.setType(ModificationType.REPLACE_INT_VALUE);
        modification.setAttribute("userAccountControl");
        modification.setValues(new String[] { userAccountControl });

        ModifyObject.Modification modification2 = new ModifyObject.Modification();

        modification2.setType(ModificationType.REPLACE_INT_VALUE);
        modification2.setAttribute("userParameters");
        modification2.setValues(new String[] { userParameters });

        List<ModifyObject.Modification> modifications = Arrays.asList(modification, modification2);

        ModifyObject modifyObject = new ModifyObject();

        if (StringUtils.equalsIgnoreCase(userParameters, "ACTIVE"))
            modifyObject
                    .setAuditLogsCategoryType(AuditLogsCategoryType.UserManagement_ActivateUser);
        else if (StringUtils.equalsIgnoreCase(userParameters, "SUSPEND"))
            modifyObject.setAuditLogsCategoryType(AuditLogsCategoryType.UserManagement_SuspendUser);
        else if (StringUtils.equalsIgnoreCase(userParameters, "DELETE"))
            modifyObject.setAuditLogsCategoryType(AuditLogsCategoryType.UserManagement_DeleteUser);
        modifyObject.setModifications(modifications);
        modifyObject.setDn(userDn);

        return modify(modifyObject);
    }

    /**
     *
     */
    public LDAPResult removeOrganizationPerson(String userDn) throws Exception {

        SearchResultEntry entry = getEntry(userDn);

        if (null == entry) {

            log.error("  OrganizationPerson : {} is not existed.", userDn);
            return entryNotExistedResult;
        }

        DeleteObject deleteObject = new DeleteObject();

        deleteObject.setAuditLogsCategoryType(AuditLogsCategoryType.UserManagement_DeleteUser);
        deleteObject.setDn(userDn);

        return delete(deleteObject);
    }

    /**
     *
     * @param dn
     * @return
     * @throws Exception
     */
    public String getGroupDn(String dn) throws Exception {

        return getGroupDn(dn, ADGroupType.UNIVERSAL_SECURITY_GROUP);
    }

    /**
     * 
     * @param dn
     * @return
     * @throws Exception
     */
    public String getGroupDn(String dn, ADGroupType adGroupType) throws Exception {

        LdapName ldapName = new LdapName(dn);

        // 先取得此dn最底層ou的dn
        for (int index = 0; index < ldapName.getRdns().size(); index++) {

            Rdn rdn = ldapName.getRdn(index);
            if (StringUtils.equalsIgnoreCase("CN", rdn.getType())) {

                ldapName.remove(index);
            }
        }

        // 取得最底層ou的rdn
        Name ouSuffix = ldapName.getSuffix(ldapName.size() - 1);

        Rdn rdn = new Rdn(ouSuffix.toString());
        // 拿最底層ou其rdn的value當其group的cn
        ldapName.add("CN=" + ((String) rdn.getValue() + adGroupType.getSuffix()));

        return ldapName.toString();
    }

    public String getParentGroupDn(String dn, ADGroupType adGroupType) throws Exception {

        LdapName ldapName = new LdapName(dn);
        ldapName.remove(ldapName.size() - 1);

        if (StringUtils.equalsIgnoreCase(ldapName.toString(), mofdn))
            return "";

        return getGroupDn(ldapName.toString(), adGroupType);
    }

    public String removeCn(String dn) throws Exception {

        LdapName ldapName = new LdapName(dn);

        for (int index = 0; index < ldapName.getRdns().size(); index++) {

            Rdn rdn = ldapName.getRdn(index);
            if (StringUtils.equalsIgnoreCase("CN", rdn.getType())) {

                ldapName.remove(index);
                break;
            }
        }

        return ldapName.toString();
    }
}

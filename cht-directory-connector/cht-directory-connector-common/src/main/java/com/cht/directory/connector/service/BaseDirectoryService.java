package com.cht.directory.connector.service;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cht.directory.connector.service.error.LDAPResultCode;
import com.cht.directory.connector.service.objects.AddObject;
import com.cht.directory.connector.service.objects.DeleteObject;
import com.cht.directory.connector.service.objects.ModifyObject;
import com.cht.directory.connector.type.AuditLogsCategoryType;
import com.cht.directory.connector.type.AuditLogsResult;
import com.unboundid.asn1.ASN1OctetString;
import com.unboundid.ldap.sdk.*;
import com.unboundid.ldap.sdk.controls.SimplePagedResultsControl;
import com.unboundid.util.LDAPTestUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseDirectoryService {

    public final LDAPResult entryExistedResult = new LDAPResult(0, ResultCode.ENTRY_ALREADY_EXISTS);
    public final LDAPResult entryNotExistedResult = new LDAPResult(0, ResultCode.NO_SUCH_OBJECT);

    public final int MAX_SIZE = 1000;

    protected LDAPConnectionPool connectionPool;

    @Autowired(required = false)
    protected AuditLogsService auditLogsService;

    @Autowired
    public BaseDirectoryService(LDAPConnectionPool connectionPool) {

        this.connectionPool = connectionPool;
    }

    public LDAPResult add(AddObject addObject) throws Exception {

        String ldifRequest = "";

        Long startTime = new Date().getTime();
        try {

            log.info("   add = {}", addObject);

            final Collection<Attribute> attrs = new ArrayList();

            if (addObject.getAttributes() != null) {
                Map<String, String> reqattrs = addObject.getAttributes();
                List<Attribute> list = new ArrayList<>();
                for (String attrName : reqattrs.keySet()) {
                    Attribute attribute = new Attribute(attrName, reqattrs.get(attrName));
                    list.add(attribute);
                }
                attrs.addAll(list);
            }

            if (addObject.getBinaryAttributes() != null) {
                Map<String, String> reqattrs = addObject.getBinaryAttributes();
                attrs.addAll(reqattrs.keySet().stream()
                        .map(attrName -> new Attribute(attrName,
                                Base64.getDecoder().decode(reqattrs.get(attrName))))
                        .collect(Collectors.toList()));
            }

            AddRequest addRequest = new AddRequest(addObject.getDn(), attrs);

            ldifRequest = addRequest.toLDIFString();

            LDAPResult result = connectionPool.add(addRequest);
            Long endTime = new Date().getTime();

            log.info("   result = {}", result);

            AuditLogsResult auditLogsResult = (result.getResultCode()
                    .intValue() == LDAPResultCode.LDAP_SUCCESS.getCode()) ? AuditLogsResult.SUCCESS
                            : AuditLogsResult.FAILURE;

            auditLogsService.audit(addObject.getAuditLogsCategoryType().getActivityDisplayName(), ldifRequest,
                    addObject.getAuditLogsCategoryType().getCategory(),
                    addObject.getCorrelationId(), addObject.getLogname(), auditLogsResult,
                    result.getResultCode().intValue(), result.getResultString(), addObject.getDn(),
                    endTime - startTime);

            return result;
        } catch (LDAPException ex) {

            Long endTime = new Date().getTime();
            String resultString = ex.getResultString().replace("\0", "");

            auditLogsService.audit(addObject.getAuditLogsCategoryType().getActivityDisplayName(), ldifRequest,
                    addObject.getAuditLogsCategoryType().getCategory(),
                    addObject.getCorrelationId(), addObject.getLogname(), AuditLogsResult.FAILURE,
                    ex.getResultCode().intValue(), resultString, addObject.getDn(), endTime - startTime);

            throw ex;
        }
    }

    public LDAPResult modify(ModifyObject modifyObject) throws Exception {

        String ldifRequest = "";

        Long startTime = new Date().getTime();
        try {

            List<Modification> mods = new ArrayList<>();
            for (ModifyObject.Modification mod : modifyObject.getModifications()) {
                if (mod.isBinary()) {
                    mods.add(new Modification(ModificationType.valueOf(mod.getType()),
                            mod.getAttribute(), base64Decode(mod.getValues())));
                } else {
                    mods.add(new Modification(ModificationType.valueOf(mod.getType()),
                            mod.getAttribute(), mod.getValues()));
                }
            }

            ModifyRequest modifyRequest = new ModifyRequest(modifyObject.getDn(), mods);

            if (modifyObject
                    .getAuditLogsCategoryType() == AuditLogsCategoryType.PasswordManagement_UserPasswordChanges)
                ldifRequest = modifyObject.getAdditionalDetails();
            else
                ldifRequest = modifyRequest.toLDIFString();

            LDAPResult result = connectionPool.modify(modifyRequest);
            Long endTime = new Date().getTime();

            log.info("   result = {}", result);

            AuditLogsResult auditLogsResult = (result.getResultCode()
                    .intValue() == LDAPResultCode.LDAP_SUCCESS.getCode()) ? AuditLogsResult.SUCCESS
                            : AuditLogsResult.FAILURE;

            auditLogsService.audit(modifyObject.getAuditLogsCategoryType().getActivityDisplayName(),
                    ldifRequest, modifyObject.getAuditLogsCategoryType().getCategory(),
                    modifyObject.getCorrelationId(), this.getClass().getName(), auditLogsResult,
                    result.getResultCode().intValue(), result.getResultString(),
                    modifyObject.getDn(), endTime - startTime);

            return result;
        } catch (LDAPException ex) {

            Long endTime = new Date().getTime();
            String resultString = ex.getResultString().replace("\0", "");

            auditLogsService.audit(modifyObject.getAuditLogsCategoryType().getActivityDisplayName(),
                    ldifRequest, modifyObject.getAuditLogsCategoryType().getCategory(),
                    modifyObject.getCorrelationId(), this.getClass().getName(),
                    AuditLogsResult.FAILURE, ex.getResultCode().intValue(), resultString,
                    modifyObject.getDn(), endTime - startTime);

            throw ex;
        }
    }

    public LDAPResult delete(DeleteObject deleteObject) throws Exception {

        String ldifRequest = "";

        Long startTime = new Date().getTime();
        try {

            DeleteRequest deleteRequest = new DeleteRequest(deleteObject.getDn());
            ldifRequest = deleteRequest.toLDIFString();

            LDAPResult result = connectionPool.delete(deleteRequest);
            Long endTime = new Date().getTime();

            log.info("   result = {}", result);

            AuditLogsResult auditLogsResult = (result.getResultCode()
                    .intValue() == LDAPResultCode.LDAP_SUCCESS.getCode()) ? AuditLogsResult.SUCCESS
                            : AuditLogsResult.FAILURE;

            auditLogsService.audit(deleteObject.getAuditLogsCategoryType().getActivityDisplayName(),
                    ldifRequest, deleteObject.getAuditLogsCategoryType().getCategory(),
                    deleteObject.getCorrelationId(), this.getClass().getName(), auditLogsResult,
                    result.getResultCode().intValue(), result.getResultString(),
                    deleteObject.getDn(), endTime - startTime);

            return result;

        } catch (LDAPException ex) {

            Long endTime = new Date().getTime();
            String resultString = ex.getResultString().replace("\0", "");

            auditLogsService.audit(deleteObject.getAuditLogsCategoryType().getActivityDisplayName(),
                    ldifRequest, deleteObject.getAuditLogsCategoryType().getCategory(),
                    deleteObject.getCorrelationId(), this.getClass().getName(),
                    AuditLogsResult.FAILURE, ex.getResultCode().intValue(), resultString,
                    deleteObject.getDn(), endTime - startTime);

            throw ex;
        }
    }

    public SearchResultEntry getEntry(String dn, String... args) throws Exception {

        SearchResultEntry entry = connectionPool.getEntry(dn, args);

        return entry;
    }

    public SearchResult search(String basedn, List<Filter> filters, String modifytimestamp,
            String[] returnAttrs) throws Exception {

        if (StringUtils.isNotBlank(modifytimestamp)) {

            Filter modifyTimestampFilter = Filter.createGreaterOrEqualFilter("modifyTimestamp",
                    modifytimestamp);

            filters.add(modifyTimestampFilter);
        }

        Filter searchFilter = Filter.createANDFilter(filters);

        log.info("search filter : {}", searchFilter.toString());

        SearchResult searchResult = search(basedn, SearchScope.SUB, searchFilter, returnAttrs);

        log.info("result = {}", searchResult);

        return searchResult;
    }

    public SearchResult search(String baseDn, SearchScope searchScope, Filter filter,
            String... attributes) throws Exception {

        int numSearches = 0;
        int totalEntriesReturned = 0;
        int totalNumReferences = 0;
        int messageID;
        ResultCode resultCode;
        String diagnosticMessage;
        String matchedDN;
        String[] referralURLs;
        List<SearchResultReference> searchReferences;
        Control[] responseControls;

        List<SearchResultEntry> searchEntries = new ArrayList<>();

        SearchRequest searchRequest = new SearchRequest(baseDn, searchScope, filter, attributes);

        ASN1OctetString resumeCookie = null;
        SearchResult searchResult = null;

        LDAPConnection connection = connectionPool.getConnection();

        while (true) {
            searchRequest.setControls(new SimplePagedResultsControl(MAX_SIZE, resumeCookie));
            searchRequest.setSizeLimit(Integer.MAX_VALUE);

            SearchResult pageSearchResult = connection.search(searchRequest);

            numSearches++;
            messageID = pageSearchResult.getMessageID();
            resultCode = pageSearchResult.getResultCode();
            diagnosticMessage = pageSearchResult.getDiagnosticMessage();
            matchedDN = pageSearchResult.getMatchedDN();
            referralURLs = pageSearchResult.getReferralURLs();
            searchReferences = pageSearchResult.getSearchReferences();
            int entriesReturned = pageSearchResult.getEntryCount();
            totalEntriesReturned += entriesReturned;
            totalNumReferences += pageSearchResult.getReferenceCount();
            responseControls = searchRequest.getControls();

            searchEntries.addAll(pageSearchResult.getSearchEntries());

            LDAPTestUtils.assertHasControl(pageSearchResult,
                    SimplePagedResultsControl.PAGED_RESULTS_OID);
            SimplePagedResultsControl responseControl = SimplePagedResultsControl
                    .get(pageSearchResult);

            log.info("  numSearches: {}", numSearches);
            log.info("  entriesReturned: {}", entriesReturned);

            if (responseControl.moreResultsToReturn()) {
                // The resume cookie can be included in the simple paged results
                // control included in the next search to get the next page of results.
                resumeCookie = responseControl.getCookie();
            } else
                break;
        }

        log.info("  totalEntriesReturned: {}", totalEntriesReturned);

        searchResult = new SearchResult(messageID, resultCode, diagnosticMessage, matchedDN,
                referralURLs, searchEntries, searchReferences, totalEntriesReturned,
                totalNumReferences, responseControls);

        return searchResult;
    }

    private static String[] base64Encode(byte[][] data) {

        String[] encoded = new String[data.length];
        for (int i = 0, size = encoded.length; i < size; i++) {
            encoded[i] = Base64.getEncoder().encodeToString((data[i]));
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

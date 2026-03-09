package com.cht.directory.repository;

import com.cht.directory.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface ConnectorSpaceAdOrganizationalUnitExternalDetailsRepository extends
        JpaRepository<ConnectorSpaceAdOrganizationalUnitExternalDetails, ConnectorSpaceAdOrganizationalUnitExternalDetailsId>,
        JpaSpecificationExecutor<ConnectorSpaceAdOrganizationalUnitDetails> {

    Optional<ConnectorSpaceAdOrganizationalUnitExternalDetails> findByDnAndPlaceholder(String dn,
            String placeholder);

    // 新增查詢 JPQL
    @Query("select a from ConnectorSpaceAdOrganizationalUnitExternalDetails a where not exists (select b from ConnectorSpaceAdOrganizationalUnitDetails b where a.ou = b.ou and b.placeholder = 'external') and lower(a.dn) like lower(concat('%', ?1,'%')) order by a.whencreated asc, length(a.dn) asc")
    List<ConnectorSpaceAdOrganizationalUnitExternalDetails> findAllConnectorSpaceAdOrganizationalUnitExternalDetailsNotInConnectorSpaceAdOrganizationalUnitDetails(
            String basedn);

    // 異動DN查詢 JPQL
    @Query("select a, b from ConnectorSpaceAdOrganizationalUnitExternalDetails a, ConnectorSpaceAdOrganizationalUnitDetails b where a.ou = b.ou and a.dnHash <> b.dnHash and b.placeholder = 'external' and lower(a.dn) like lower(concat('%', ?1,'%')) order by a.whencreated asc")
    List<Object[]> findAllConnectorSpaceAdOrganizationalUnitExternalDetailsJoinConnectorSpaceAdOrganizationalUnitDetailsWhenDnChanged(
            String basedn);

    // 異動屬性查詢 JPQL
    @Query("select a, b from ConnectorSpaceAdOrganizationalUnitExternalDetails a, ConnectorSpaceAdOrganizationalUnitDetails b where a.dn = b.dn and a.syncattrsHash <> b.syncattrsHash and b.placeholder = 'external' and lower(a.dn) like lower(concat('%', ?1,'%')) order by a.whencreated asc, length(a.dn) asc")
    List<Object[]> findAllConnectorSpaceAdOrganizationalUnitExternalDetailsInConnectorSpaceAdOrganizationalUnitDetailsWhenAttrsChanged(
            String basedn);

}

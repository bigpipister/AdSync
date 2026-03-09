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
public interface ConnectorSpaceAdGroupExternalDetailsRepository
        extends JpaRepository<ConnectorSpaceAdGroupExternalDetails, ConnectorSpaceAdGroupExternalDetailsId>,
        JpaSpecificationExecutor<ConnectorSpaceAdGroupDetails> {

    Optional<ConnectorSpaceAdGroupExternalDetails> findByDnAndPlaceholder(String dn, String placeholder);

    // 新增查詢 JPQL
    @Query("select a from ConnectorSpaceAdGroupExternalDetails a where not exists (select b from ConnectorSpaceAdGroupDetails b where a.cn = b.cn and b.placeholder = 'external') and lower(a.dn) like lower(concat('%', ?1,'%')) and a.objectguid <> '-1' order by a.whencreated asc, length(a.dn) asc")
    List<ConnectorSpaceAdGroupExternalDetails> findAllConnectorSpaceAdGroupExternalDetailsNotInConnectorSpaceAdGroupDetails(
            String basedn);

    // 異動DN查詢 JPQL
    @Query("select a, b from ConnectorSpaceAdGroupExternalDetails a, ConnectorSpaceAdGroupDetails b where a.cn = b.cn and a.dnHash <> b.dnHash and b.placeholder = 'external' and lower(a.dn) like lower(concat('%', ?1,'%')) order by a.whencreated asc")
    List<Object[]> findAllConnectorSpaceAdGroupExternalDetailsJoinConnectorSpaceAdGroupDetailsWhenDnChanged(
            String basedn);

    // 異動屬性查詢 JPQL
    @Query("select a, b from ConnectorSpaceAdGroupExternalDetails a, ConnectorSpaceAdGroupDetails b where a.dn = b.dn and a.syncattrsHash <> b.syncattrsHash and b.placeholder = 'external' and lower(a.dn) like lower(concat('%', ?1,'%')) order by a.whencreated asc, length(a.dn) asc")
    List<Object[]> findAllConnectorSpaceAdGroupExternalDetailsInConnectorSpaceAdGroupDetailsWhenAttrsChanged(
            String basedn);

}

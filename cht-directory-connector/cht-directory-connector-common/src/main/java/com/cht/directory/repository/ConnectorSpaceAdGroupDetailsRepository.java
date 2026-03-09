package com.cht.directory.repository;

import java.util.List;
import java.util.Optional;

import com.cht.directory.domain.ConnectorSpaceAdPersonDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cht.directory.domain.ConnectorSpaceAdGroupDetails;
import com.cht.directory.domain.ConnectorSpaceAdGroupDetailsId;

@SuppressWarnings("unused")
@Repository
public interface ConnectorSpaceAdGroupDetailsRepository
        extends JpaRepository<ConnectorSpaceAdGroupDetails, ConnectorSpaceAdGroupDetailsId>,
        JpaSpecificationExecutor<ConnectorSpaceAdGroupDetails> {

    Optional<ConnectorSpaceAdGroupDetails> findByDnAndPlaceholder(String dn, String placeholder);

    // 在內、外網搜尋 group 資料庫不在現有 ad group cn 清單的查詢 JPQL
    List<ConnectorSpaceAdGroupDetails> findAllByCnNotInAndPlaceholderAndObjectguidNot(
            List<String> cnList, String placeHolder, String objectguid);

    // 取出指定basedn 的 group
    @Query("select a from ConnectorSpaceAdGroupDetails a where a.placeholder = ?1 and lower(a.dn) like lower(concat('%', ?2,'%')) order by a.whencreated asc")
    List<ConnectorSpaceAdGroupDetails> findAllConnectorSpaceAdGroupDetailsAndPlaceholderWithBaseDn(
            String placeHolder, String basedn);
}

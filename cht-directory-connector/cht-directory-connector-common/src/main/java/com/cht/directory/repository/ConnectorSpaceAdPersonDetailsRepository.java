package com.cht.directory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cht.directory.domain.ConnectorSpaceAdPersonDetails;
import com.cht.directory.domain.ConnectorSpaceAdPersonDetailsId;

@SuppressWarnings("unused")
@Repository
public interface ConnectorSpaceAdPersonDetailsRepository
        extends JpaRepository<ConnectorSpaceAdPersonDetails, ConnectorSpaceAdPersonDetailsId>,
        JpaSpecificationExecutor<ConnectorSpaceAdPersonDetails> {

    Optional<ConnectorSpaceAdPersonDetails> findByCnAndPlaceholderAndObjectguidNot(
            String cn, String placeholder, String objectguid);

    Optional<ConnectorSpaceAdPersonDetails> findByDnAndPlaceholder(
            String dn, String placeholder);

    Optional<ConnectorSpaceAdPersonDetails> findByCnAndPlaceholder(
            String cn, String placeholder);

    Optional<ConnectorSpaceAdPersonDetails> findBySamaccountnameAndPlaceholder(
            String name, String placeholder);

    // 在內外網搜尋 person 資料庫不在現有 ad pseron cn 清單的查詢 JPQL
    List<ConnectorSpaceAdPersonDetails> findAllByCnNotInAndPlaceholderAndObjectguidNot(
            List<String> cnList, String placeHolder, String objectguid);

    // 取出指定basedn 的 person
    @Query("select a from ConnectorSpaceAdPersonDetails a where a.placeholder = ?1 and lower(a.dn) like lower(concat('%', ?2,'%')) order by a.whencreated asc")
    List<ConnectorSpaceAdPersonDetails> findAllConnectorSpaceAdPersonDetailsAndPlaceholderWithBaseDn(
            String placeHolder, String basedn);

    // 尋找幽靈帳號
//    @Query("select a from ConnectorSpaceAdPersonDetails a where not exists (select b from ConnectorSpaceAdPersonSyncDetails b where a.cn = b.cn) and a.dn like %:domaindn and a.useraccountcontrol <> 514 and a.placeholder = :placeholder order by a.whencreated asc")
//    List<ConnectorSpaceAdPersonDetails> findAllConnectorSpaceAdPersonDetailsNotInMetaversePersonDetails(
//            String domaindn, String placeholder);

}

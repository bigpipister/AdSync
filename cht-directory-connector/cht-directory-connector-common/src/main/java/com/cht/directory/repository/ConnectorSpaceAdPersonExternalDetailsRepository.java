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
public interface ConnectorSpaceAdPersonExternalDetailsRepository
        extends JpaRepository<ConnectorSpaceAdPersonExternalDetails, ConnectorSpaceAdPersonExternalDetailsId>,
        JpaSpecificationExecutor<ConnectorSpaceAdPersonDetails> {

    Optional<ConnectorSpaceAdPersonExternalDetails> findByDnAndPlaceholder(
            String dn, String placeholder);

    // 新增查詢 JPQL
    //@Query("select a from ConnectorSpaceAdPersonExternalDetails a where not exists (select b from ConnectorSpaceAdPersonDetails b where a.cn = b.cn and b.placeholder = 'external') and lower(a.dn) like lower(concat('%', ?1,'%')) and a.extensionattribute1 = '1' and a.userparameters <> 'DELETE' order by a.whencreated asc")
    @Query("select a from ConnectorSpaceAdPersonExternalDetails a where not exists (select b from ConnectorSpaceAdPersonDetails b where a.cn = b.cn and b.placeholder = 'external') and lower(a.dn) like lower(concat('%', ?1,'%')) and a.objectguid <> '-1' and a.objectguid <> '-2' and a.extensionattribute1 = '1' and a.unicodepwd is not null order by a.whencreated asc")
    List<ConnectorSpaceAdPersonExternalDetails> findAllConnectorSpaceAdPersonExternalDetailsNotInConnectorSpaceAdPersonDetails(
            String basedn);

    // 異動DN查詢 JPQL
    @Query("select a, b from ConnectorSpaceAdPersonExternalDetails a, ConnectorSpaceAdPersonDetails b where a.cn = b.cn and a.dnHash <> b.dnHash and b.objectguid <> '-2' and b.placeholder = 'external' and lower(a.dn) like lower(concat('%', ?1,'%')) order by a.whencreated asc")
    List<Object[]> findAllConnectorSpaceAdPersonExternalDetailsJoinConnectorSpaceAdPersonDetailsWhenDnChanged(
            String basedn);

    // 異動屬性查詢 JPQL
    @Query("select a, b from ConnectorSpaceAdPersonExternalDetails a, ConnectorSpaceAdPersonDetails b where a.dn = b.dn and a.syncattrsHash <> b.syncattrsHash and b.objectguid <> '-2' and b.placeholder = 'external' and lower(a.dn) like lower(concat('%', ?1,'%')) order by a.whencreated asc")
    List<Object[]> findAllConnectorSpaceAdPersonExternalDetailsInConnectorSpaceAdOrganizationalUnitDetailsWhenAttrsChanged(
            String basedn);

    // 密碼異動 JPQL
    @Query("select a, b from ConnectorSpaceAdPersonExternalDetails a, ConnectorSpaceAdPersonDetails b where a.dn = b.dn and ((a.unicodepwdHash <> b.unicodepwdHash) or (a.unicodepwdHash is NULL) or (b.unicodepwdHash is NULL)) and b.objectguid <> '-2' and b.placeholder = 'external' and lower(a.dn) like lower(concat('%', ?1,'%')) order by a.whencreated asc")
    List<Object[]> findAllPersonDetailsWhenPwdChanged(
            String basedn);

    // 在外網尋找外網 external ad 資料庫裡已標記為刪除但外網 ad 還在的清單 JPQL
    //@Query("select a, b from ConnectorSpaceAdPersonExternalDetails a, ConnectorSpaceAdPersonDetails b where a.dn = b.dn and a.objectguid = '-1' and b.useraccountcontrol = 514 and b.userparameters = 'DELETE' and b.placeholder = ?1 and lower(a.dn) like lower(concat('%', ?2,'%')) order by a.whencreated asc")
    @Query("select a, b from ConnectorSpaceAdPersonExternalDetails a, ConnectorSpaceAdPersonDetails b where a.dn = b.dn and a.objectguid = '-1' and b.useraccountcontrol = 514 and b.userparameters = 'DELETE' and b.placeholder = ?1 and lower(a.dn) like lower(concat('%', ?2,'%')) order by a.whencreated asc")
    List<Object[]> findExternalAllConnectorSpaceAdPersonSyncDetailsNotInConnectorSpaceAdPersonDetailsWhenIsDeleted(
            String placeHolder, String basedn);

}

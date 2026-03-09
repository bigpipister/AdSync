package com.cht.directory.connector.filter.web.repository;

import com.cht.directory.connector.filter.web.entity.WebConnectorSpaceAdPersonDetails;
import com.cht.directory.connector.filter.web.entity.WebConnectorSpaceAdPersonDetailsId;
import com.cht.directory.connector.filter.web.entity.WebConnectorSpaceAdPersonSyncDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface WebConnectorSpaceAdPersonDetailsRepository
        extends JpaRepository<WebConnectorSpaceAdPersonDetails, WebConnectorSpaceAdPersonDetailsId>,
        JpaSpecificationExecutor<WebConnectorSpaceAdPersonSyncDetails> {

    // filter 找出帳號改密碼
    Optional<WebConnectorSpaceAdPersonDetails> findBySamaccountnameAndPlaceholder(
            String name, String placeholder);

    // 找出外網密碥修改時間比內網晚(總筆數)
    @Query("select a, b from WebConnectorSpaceAdPersonDetails a, WebConnectorSpaceAdPersonSyncDetails b where a.samaccountname = b.samaccountname and a.objectguid <> '-1' and b.objectguid <> '-1' and a.unicodepwdHash <> b.unicodepwdHash and a.pwdlastset > b.pwdlastset order by a.pwdlastset asc")
    List<Object[]> findAllInvalidPwdDiff();

    // 找出外網密碥修改時間比內網晚
    @Query("select a, b from WebConnectorSpaceAdPersonDetails a, WebConnectorSpaceAdPersonSyncDetails b where a.samaccountname = b.samaccountname and a.objectguid <> '-1' and b.objectguid <> '-1' and a.unicodepwdHash <> b.unicodepwdHash and a.pwdlastset > b.pwdlastset order by a.pwdlastset asc")
    List<Object[]> findInvalidPwdDiff(Pageable pageable);

    // 成對列出內外網person
    @Query("select a, b from WebConnectorSpaceAdPersonDetails a, WebConnectorSpaceAdPersonSyncDetails b where a.samaccountname = b.samaccountname and a.objectguid <> '-1' and b.objectguid <> '-1'  order by a.whenchanged asc")
    List<Object[]> findInvalidPropertiesDiff();
}

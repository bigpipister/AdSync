package com.cht.directory.repository;

import com.cht.directory.domain.ConnectorSpaceAdPersonFiaDetails;
import com.cht.directory.domain.ConnectorSpaceAdPersonFiaDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface ConnectorSpaceAdPersonFiaDetailsRepository
        extends JpaRepository<ConnectorSpaceAdPersonFiaDetails, ConnectorSpaceAdPersonFiaDetailsId>,
        JpaSpecificationExecutor<ConnectorSpaceAdPersonFiaDetails> {

    // 在中心匯集 ou 中沒有，但五區內網 person 資料庫有的 cn 清單查詢 JPQL
    List<ConnectorSpaceAdPersonFiaDetails> findAllByCnNotInAndPlaceholderAndObjectguidNotAndUnicodepwdIsNotNull(
            List<String> cnList, String placeHolder, String objectguid);

    // 在中心匯集 ou 中有，但五區內網 person 資料庫已標記被刪的 cn 清單查詢 JPQL
    List<ConnectorSpaceAdPersonFiaDetails> findAllByCnInAndPlaceholderAndObjectguid(
            List<String> cnList, String placeHolder, String objectguid);

    // 找出 PwdLastSet 晚於指定時間的清單
    List<ConnectorSpaceAdPersonFiaDetails> findAllByObjectguidNotAndPwdlastsetAfter(String objectguid, Timestamp timestamp);

}

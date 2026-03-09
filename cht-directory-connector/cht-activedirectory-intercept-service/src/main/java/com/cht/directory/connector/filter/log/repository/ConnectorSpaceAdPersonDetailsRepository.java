package com.cht.directory.connector.filter.log.repository;

import com.cht.directory.connector.filter.log.entity.ConnectorSpaceAdPersonDetails;
import com.cht.directory.connector.filter.log.entity.ConnectorSpaceAdPersonDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface ConnectorSpaceAdPersonDetailsRepository
        extends JpaRepository<ConnectorSpaceAdPersonDetails, ConnectorSpaceAdPersonDetailsId>,
        JpaSpecificationExecutor<ConnectorSpaceAdPersonDetails> {

    // filter 找出帳號改密碼
    Optional<ConnectorSpaceAdPersonDetails> findBySamaccountnameAndPlaceholder(
            String name, String placeholder);

}

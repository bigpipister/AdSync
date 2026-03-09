package com.cht.directory.repository;

import com.cht.directory.domain.ConnectorSpaceAdPersonDetails;
import com.cht.directory.domain.ConnectorSpaceAdPersonSyncDetails;
import com.cht.directory.domain.ConnectorSpaceAdPersonSyncDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface ConnectorSpaceAdPersonSyncDetailsRepository
        extends JpaRepository<ConnectorSpaceAdPersonSyncDetails, ConnectorSpaceAdPersonSyncDetailsId>,
        JpaSpecificationExecutor<ConnectorSpaceAdPersonSyncDetails> {

    Optional<ConnectorSpaceAdPersonDetails> findByDnAndPlaceholder(String dn, String placeholder);

}

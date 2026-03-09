package com.cht.directory.repository;

import com.cht.directory.domain.ConnectorSpaceAdGroupDetails;
import com.cht.directory.domain.ConnectorSpaceAdGroupSyncDetails;
import com.cht.directory.domain.ConnectorSpaceAdGroupSyncDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface ConnectorSpaceAdGroupSyncDetailsRepository
        extends JpaRepository<ConnectorSpaceAdGroupSyncDetails, ConnectorSpaceAdGroupSyncDetailsId>,
        JpaSpecificationExecutor<ConnectorSpaceAdGroupSyncDetails> {

    Optional<ConnectorSpaceAdGroupSyncDetails> findByDnAndPlaceholder(String dn, String placeholder);

}

package com.cht.directory.repository;

import com.cht.directory.domain.ConnectorSpaceAdOrganizationalUnitDetails;
import com.cht.directory.domain.ConnectorSpaceAdOrganizationalUnitSyncDetails;
import com.cht.directory.domain.ConnectorSpaceAdOrganizationalUnitSyncDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface ConnectorSpaceAdOrganizationalUnitSyncDetailsRepository extends
        JpaRepository<ConnectorSpaceAdOrganizationalUnitSyncDetails, ConnectorSpaceAdOrganizationalUnitSyncDetailsId>,
        JpaSpecificationExecutor<ConnectorSpaceAdOrganizationalUnitSyncDetails> {

    Optional<ConnectorSpaceAdOrganizationalUnitSyncDetails> findByDnAndPlaceholder(String dn,
            String placeholder);

}

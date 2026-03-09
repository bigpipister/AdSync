package com.cht.directory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cht.directory.domain.ConnectorSpaceAdOrganizationalUnitDetails;
import com.cht.directory.domain.ConnectorSpaceAdOrganizationalUnitDetailsId;

@SuppressWarnings("unused")
@Repository
public interface ConnectorSpaceAdOrganizationalUnitDetailsRepository extends
        JpaRepository<ConnectorSpaceAdOrganizationalUnitDetails, ConnectorSpaceAdOrganizationalUnitDetailsId>,
        JpaSpecificationExecutor<ConnectorSpaceAdOrganizationalUnitDetails> {

    Optional<ConnectorSpaceAdOrganizationalUnitDetails> findByDnAndPlaceholder(String dn,
            String placeholder);

}

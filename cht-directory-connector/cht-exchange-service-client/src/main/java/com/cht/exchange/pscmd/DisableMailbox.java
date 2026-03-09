package com.cht.exchange.pscmd;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class DisableMailbox {

    @Parameter(names = "-Identity")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String identity;

    @Parameter(names = "-Arbitration")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean arbitration;

    @Parameter(names = "-DisableLastArbitrationMailboxAllowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean disableLastArbitrationMailboxAllowed;

    @Parameter(names = "-DisableArbitrationMailboxWithOABsAllowed")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean disableArbitrationMailboxWithOABsAllowed;

    @Parameter(names = "-Confirm")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean confirm;

    @Parameter(names = "-DomainController")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String domainController;

    @Parameter(names = "-IgnoreDefaultScope")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean ignoreDefaultScope;

    @Parameter(names = "-IgnoreLegalHold")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean ignoreLegalHold;

    @Parameter(names = "-WhatIf")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean whatIf;

    @Parameter(names = "-Archive")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean archive;

    @Parameter(names = "-PermanentlyDisable")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean permanentlyDisable;

    @Parameter(names = "-PublicFolder")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean publicFolder;

    @Parameter(names = "-RemoteArchive")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean remoteArchive;
}

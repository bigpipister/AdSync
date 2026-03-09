package com.cht.exchange.pscmd;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class EnableDistributionGroup {

    @Parameter(names = "-Identity")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String identity;

    @Parameter(names = "-Alias")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String alias;

    @Parameter(names = "-Confirm")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean confirm;

    @Parameter(names = "-DisplayName")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String displayName;

    @Parameter(names = "-DomainController")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String domainController;

    @Parameter(names = "-PrimarySmtpAddress")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String primarySmtpAddress;

    @Parameter(names = "-WhatIf")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean whatIf;
}

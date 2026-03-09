package com.cht.exchange.pscmd;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class GetDistributionGroupMember {

    @Parameter(names = "-Identity")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String identity;

    @Parameter(names = "-Credential")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String credential;

    @Parameter(names = "-DomainController")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String domainController;

    @Parameter(names = "-IgnoreDefaultScope")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean ignoreDefaultScope;

    @Parameter(names = "-ReadFromDomainController")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean readFromDomainController;

    @Parameter(names = "-ResultSize")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String resultSize;
}

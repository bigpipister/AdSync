package com.cht.exchange.pscmd;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class GetDistributionGroup {

    @Parameter(names = "-Identity")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String identity;

    @Parameter(names = "-Anr")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String anr;

    @Parameter(names = "-Credential")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String credential;

    @Parameter(names = "-DomainController")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String domainController;

    @Parameter(names = "-Filter")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String filter;

    @Parameter(names = "-IgnoreDefaultScope")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean ignoreDefaultScope;

    @Parameter(names = "-ManagedBy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String managedBy;

    @Parameter(names = "-OrganizationalUnit")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String organizationalUnit;

    @Parameter(names = "-readFromDomainController")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean readFromDomainController;

    @Parameter(names = "-RecipientTypeDetails")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String recipientTypeDetails;

    @Parameter(names = "-ResultSize")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String resultSize;

    @Parameter(names = "-SortBy")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String sortBy;
}

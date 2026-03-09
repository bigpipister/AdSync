package com.cht.exchange.pscmd;

import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class GetMailboxDatabase {
    @Parameter(names = "-Server")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String server;

    @Parameter(names = "-Identity")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String identity;

    @Parameter(names = "-DomainController")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    String domainController;

    @Parameter(names = "-DumpsterStatistics")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean dumpsterStatistics;

    @Parameter(names = "-IncludeCorrupted")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean includeCorrupted;

    @Parameter(names = "-IncludePreExchange2010")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean includePreExchange2010;

    @Parameter(names = "-IncludePreExchange2013")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean includePreExchange2013;

    @Parameter(names = "-Status")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Boolean status;
}

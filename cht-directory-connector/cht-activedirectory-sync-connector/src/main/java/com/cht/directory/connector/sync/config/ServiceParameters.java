package com.cht.directory.connector.sync.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
public class ServiceParameters {

    private static final DateFormat sdf_cmd = new SimpleDateFormat("yyyyMMdd");
    private static final DateFormat sdf_iso8601 = new SimpleDateFormat("yyyyMMddHHmmss'Z'");

    @Parameter(names = "-name", description = "batch job name")
    private String name;

    // common
    @Parameter(names = { "-basedn" }, description = "base dn of sync、scan、pwd、clean base of ad", required = false)
    // @Value("${activedirectory.service.mofdn}")
    private String basedn;

    @Parameter(names = {
            "-edirbasedn" }, description = "base dn for search root of edirectory", required = false)
    private String edirbasedn;

    @Parameter(names = { "-dryrun" }, description = "perform dry run", required = false, arity = 1)
    private boolean dryrun = false;

    @Parameter(names = "-help", help = true)
    private boolean help;

    // scan job
    @Parameter(names = {
            "-modifytimestamp" }, description = "scan edirectory entries after modify timestamp (yyyyMMdd)", required = false)
    private String modifytimestamp;

    @Parameter(names = {
            "-doscan" }, description = "perform scan or not", required = false, arity = 1)
    private boolean doscan = true;

    @Parameter(names = {
            "-dotrans" }, description = "transfer inner domain to external domain", required = false, arity = 1)
    private boolean dotrans = true;

    @Value("${activedirectory.service.placeholder}")
    private String placeholder;

    @Value("${exchangeService.enabled}")
    private String exchangeServiceEnabled;

    // sync job
    @Parameter(names = {
            "-scope" }, description = "run scope of sync to ad (1: OrganizationUnit, 2: OrganizationPerson, 3: Group)", required = false)
    private List<String> scope;

    // clean job
    @Parameter(names = { "-removeaduser" }, description = "remove ad user", required = false, arity = 1)
    @Value("${activedirectory.service.removeuser.enabled:false}")
    private boolean removeAduserEnabled;

    @Value("${activedirectory.service.removeuser.maxlimit:10}")
    private int removeAduserMaxlimit;

    // transfer job
    @Parameter(names = { "-targetdn" }, description = "target dn of transfer to ad", required = false)
    @Value("${activedirectory.service.targetdn}")
    private String targetdn;

    public void parse(String[] args) throws Exception {

        JCommander jCommander = JCommander.newBuilder().addObject(this).build();
        jCommander.parse(args);

        if (this.help) {
            jCommander.usage();
        }

        if (StringUtils.isNotBlank(modifytimestamp)) {

            Date modifytimestampDate = sdf_cmd.parse(modifytimestamp);
            sdf_iso8601.setTimeZone(TimeZone.getTimeZone("UTC"));
            modifytimestamp = sdf_iso8601.format(modifytimestampDate);
        }
    }
}

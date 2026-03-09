package com.cht.exchange.commander;

import com.cht.exchange.pscmd.*;
import com.cht.exchange.security.OauthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Map;

@Service
@Slf4j
public class RemoteCommander {
    @Value("${exServer_ip:127.0.0.1:3888}")
    private String exServer_ip;

    @Autowired
    private OauthService oauthService;

    /**
     *呼叫remote  exchange command service的getMailboxDatabase rest api
     * @param  params 指令參數物件
     * @return 回傳遠端PowerShell的執行結果
     */
    public Map<String,String> getMailboxDatabase(GetMailboxDatabase params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/api").path(currentMethodName);
        log.info("call remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        return response.readEntity(Map.class);
    }

    /**
     *呼叫remote  exchange command service的disableMailbox rest api
     * @param  params 指令參數物件
     * @return 回傳遠端PowerShell的執行結果
     */
    public Map<String,String> disableMailbox(DisableMailbox params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/api").path(currentMethodName);
        log.info("call remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        return response.readEntity(Map.class);
    }

    /**
     *呼叫remote  exchange command service的enableMailbox rest api
     * @param  params 指令參數物件
     * @return 回傳遠端PowerShell的執行結果
     */
    public Map<String,String> enableMailbox(EnableMailbox params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/api").path(currentMethodName);
        log.info("call remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        return response.readEntity(Map.class);
    }

    /**
     *呼叫remote  exchange command service的getMailbox rest api
     * @param  params 指令參數物件
     * @return 回傳遠端PowerShell的執行結果
     */
    public Map<String,String> getMailbox(GetMailbox params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/api").path(currentMethodName);
        log.info("call remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        return response.readEntity(Map.class);
    }

    /**
     *呼叫remote  exchange command service的newMailbox rest api
     * @param  params 指令參數物件
     * @return 回傳遠端PowerShell的執行結果
     */
    public Map<String,String> newMailbox(NewMailbox params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/api").path(currentMethodName);
        log.info("call remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        return response.readEntity(Map.class);
    }

    /**
     *呼叫remote  exchange command service的removeMailbox rest api
     * @param  params 指令參數物件
     * @return 回傳遠端PowerShell的執行結果
     */
    public Map<String,String> removeMailbox(RemoveMailbox params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/api").path(currentMethodName);
        log.info("call remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        return response.readEntity(Map.class);
    }

    /**
     *呼叫remote  exchange command service的setMailbox rest api
     * @param  params 指令參數物件
     * @return 回傳遠端PowerShell的執行結果
     */
    public Map<String,String> setMailbox(SetMailbox params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/api").path(currentMethodName);
        log.info("call remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        return response.readEntity(Map.class);
    }

    /**
     *呼叫remote  exchange command service的getDistributionGroup rest api
     * @param  params 指令參數物件
     * @return 回傳遠端PowerShell的執行結果
     */
    public Map<String,String> getDistributionGroup(GetDistributionGroup params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/api").path(currentMethodName);
        log.info("call remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        return response.readEntity(Map.class);
    }

    /**
     *呼叫remote  exchange command service的getDistributionGroupMember rest api
     * @param  params 指令參數物件
     * @return 回傳遠端PowerShell的執行結果
     */
    public Map<String,String> getDistributionGroupMember(GetDistributionGroupMember params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/api").path(currentMethodName);
        log.info("call remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        return response.readEntity(Map.class);
    }

    /**
     *呼叫remote  exchange command service的enableDistributionGroup rest api
     * @param  params 指令參數物件
     * @return 回傳遠端PowerShell的執行結果
     */
    public Map<String,String> enableDistributionGroup(EnableDistributionGroup params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/api").path(currentMethodName);
        log.info("call remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        return response.readEntity(Map.class);
    }

    /**
     *呼叫remote  exchange command service的disableDistributionGroup rest api
     * @param  params 指令參數物件
     * @return 回傳遠端PowerShell的執行結果
     */
    public Map<String,String> disableDistributionGroup(DisableDistributionGroup params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/api").path(currentMethodName);
        log.info("call remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        return response.readEntity(Map.class);
    }
}

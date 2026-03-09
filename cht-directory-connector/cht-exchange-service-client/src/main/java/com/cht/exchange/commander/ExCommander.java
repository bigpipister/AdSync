package com.cht.exchange.commander;

import com.cht.exchange.common.CmdResult;
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
public class ExCommander {

    @Value("${exServer_ip:127.0.0.1:3888}")
    private String exServer_ip;

    @Autowired
    private OauthService oauthService;

    /**
     *呼叫remote  exchange command service的getMailboxDatabase rest api
     * @param  params 指令參數物件
     * @return 回傳是否有成功放入queue中
     */
    public CmdResult getMailboxDatabase(GetMailboxDatabase params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/queue").path(currentMethodName);
        log.info("publish remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        Map<String,Object> resultMap = response.readEntity(Map.class);
        CmdResult cmdResult = new CmdResult();
        cmdResult.setCommand(String.valueOf(resultMap.get("command")));
        cmdResult.setStatus(Integer.parseInt(String.valueOf(resultMap.get("status"))));
        return cmdResult;
    }

    /**
     *呼叫remote  exchange command service的disableMailbox rest api
     * @param  params 指令參數物件
     * @return 回傳是否有成功放入queue中
     */
    public CmdResult disableMailbox(DisableMailbox params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/queue").path(currentMethodName);
        log.info("publish remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        Map<String,Object> resultMap = response.readEntity(Map.class);
        CmdResult cmdResult = new CmdResult();
        cmdResult.setCommand(String.valueOf(resultMap.get("command")));
        cmdResult.setStatus(Integer.parseInt(String.valueOf(resultMap.get("status"))));
        return cmdResult;
    }

    /**
     *呼叫remote  exchange command service的enableMailbox rest api
     * @param  params 指令參數物件
     * @return 回傳是否有成功放入queue中
     */
    public CmdResult enableMailbox(EnableMailbox params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/queue").path(currentMethodName);
        log.info("publish remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        Map<String,Object> resultMap = response.readEntity(Map.class);
        CmdResult cmdResult = new CmdResult();
        cmdResult.setCommand(String.valueOf(resultMap.get("command")));
        cmdResult.setStatus(Integer.parseInt(String.valueOf(resultMap.get("status"))));
        return cmdResult;
    }

    /**
     *呼叫remote  exchange command service的getMailbox rest api
     * @param  params 指令參數物件
     * @return 回傳是否有成功放入queue中
     */
    public CmdResult getMailbox(GetMailbox params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/queue").path(currentMethodName);
        log.info("publish remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        Map<String,Object> resultMap = response.readEntity(Map.class);
        CmdResult cmdResult = new CmdResult();
        cmdResult.setCommand(String.valueOf(resultMap.get("command")));
        cmdResult.setStatus(Integer.parseInt(String.valueOf(resultMap.get("status"))));
        return cmdResult;
    }

    /**
     *呼叫remote  exchange command service的newMailbox rest api
     * @param  params 指令參數物件
     * @return 回傳是否有成功放入queue中
     */
    public CmdResult newMailbox(NewMailbox params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/queue").path(currentMethodName);
        log.info("publish remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        Map<String,Object> resultMap = response.readEntity(Map.class);
        CmdResult cmdResult = new CmdResult();
        cmdResult.setCommand(String.valueOf(resultMap.get("command")));
        cmdResult.setStatus(Integer.parseInt(String.valueOf(resultMap.get("status"))));
        return cmdResult;
    }

    /**
     *呼叫remote  exchange command service的removeMailbox rest api
     * @param  params 指令參數物件
     * @return 回傳是否有成功放入queue中
     */
    public CmdResult removeMailbox(RemoveMailbox params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/queue").path(currentMethodName);
        log.info("publish remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        Map<String,Object> resultMap = response.readEntity(Map.class);
        CmdResult cmdResult = new CmdResult();
        cmdResult.setCommand(String.valueOf(resultMap.get("command")));
        cmdResult.setStatus(Integer.parseInt(String.valueOf(resultMap.get("status"))));
        return cmdResult;
    }

    /**
     *呼叫remote  exchange command service的setMailbox rest api
     * @param  params 指令參數物件
     * @return 回傳是否有成功放入queue中
     */
    public CmdResult setMailbox(SetMailbox params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/queue").path(currentMethodName);
        log.info("publish remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        Map<String,Object> resultMap = response.readEntity(Map.class);
        CmdResult cmdResult = new CmdResult();
        cmdResult.setCommand(String.valueOf(resultMap.get("command")));
        cmdResult.setStatus(Integer.parseInt(String.valueOf(resultMap.get("status"))));
        return cmdResult;
    }

    /**
     *呼叫remote  exchange command service的getDistributionGroup rest api
     * @param  params 指令參數物件
     * @return 回傳是否有成功放入queue中
     */
    public CmdResult getDistributionGroup(GetDistributionGroup params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/queue").path(currentMethodName);
        log.info("publish remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        Map<String,Object> resultMap = response.readEntity(Map.class);
        CmdResult cmdResult = new CmdResult();
        cmdResult.setCommand(String.valueOf(resultMap.get("command")));
        cmdResult.setStatus(Integer.parseInt(String.valueOf(resultMap.get("status"))));
        return cmdResult;
    }

    /**
     *呼叫remote  exchange command service的getDistributionGroupMember rest api
     * @param  params 指令參數物件
     * @return 回傳是否有成功放入queue中
     */
    public CmdResult getDistributionGroupMember(GetDistributionGroupMember params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/queue").path(currentMethodName);
        log.info("publish remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        Map<String,Object> resultMap = response.readEntity(Map.class);
        CmdResult cmdResult = new CmdResult();
        cmdResult.setCommand(String.valueOf(resultMap.get("command")));
        cmdResult.setStatus(Integer.parseInt(String.valueOf(resultMap.get("status"))));
        return cmdResult;
    }

    /**
     *呼叫remote  exchange command service的enableDistributionGroup rest api
     * @param  params 指令參數物件
     * @return 回傳是否有成功放入queue中
     */
    public CmdResult enableDistributionGroup(EnableDistributionGroup params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://"+exServer_ip+"/adws/exchange/queue").path(currentMethodName);
        log.info("publish remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        Map<String,Object> resultMap = response.readEntity(Map.class);
        CmdResult cmdResult = new CmdResult();
        cmdResult.setCommand(String.valueOf(resultMap.get("command")));
        cmdResult.setStatus(Integer.parseInt(String.valueOf(resultMap.get("status"))));
        return cmdResult;
    }

    /**
     *呼叫remote  exchange command service的disableDistributionGroup rest api
     * @param  params 指令參數物件
     * @return 回傳是否有成功放入queue中
     */
    public CmdResult disableDistributionGroup(DisableDistributionGroup params) {
        // getStackTrace() method return
        // current method name at 0th index
        String currentMethodName = new Throwable().getStackTrace()[0].getMethodName();

        Client client = ClientBuilder.newClient();
        WebTarget webTarget
                = client.target("http://" + exServer_ip + "/adws/exchange/queue").path(currentMethodName);
        log.info("publish remote command: " + webTarget.getUri());

        Response response = webTarget
                .queryParam("access_token", oauthService.getToken())
                .request(javax.ws.rs.core.MediaType.APPLICATION_JSON)
                .post(Entity.entity(params, javax.ws.rs.core.MediaType.APPLICATION_JSON));
        Map<String,Object> resultMap = response.readEntity(Map.class);
        CmdResult cmdResult = new CmdResult();
        cmdResult.setCommand(String.valueOf(resultMap.get("command")));
        cmdResult.setStatus(Integer.parseInt(String.valueOf(resultMap.get("status"))));
        return cmdResult;
    }
}

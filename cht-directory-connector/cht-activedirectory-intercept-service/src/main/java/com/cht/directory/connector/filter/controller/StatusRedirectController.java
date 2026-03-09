package com.cht.directory.connector.filter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatusRedirectController {

    @GetMapping("/status")
    public String redirectToStatusIndex() {
        return "forward:/status/index.html";
    }

    @GetMapping("/status/login")
    public String redirectToStatusLogin() {
        return "forward:/status/index.html";
    }

    @GetMapping("/status/dashboard")
    public String redirectToStatusDashboard() {
        return "forward:/status/index.html";
    }

    @GetMapping("/status/event/event")
    public String redirectToStatusEvent() {
        return "forward:/status/index.html";
    }

    @GetMapping("/status/audit/audit")
    public String redirectToStatusAudit() {
        return "forward:/status/index.html";
    }

    @GetMapping("/status/pwd/pwd")
    public String redirectToStatusPwd() {
        return "forward:/status/index.html";
    }
}
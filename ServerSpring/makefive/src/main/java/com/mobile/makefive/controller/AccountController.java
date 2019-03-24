package com.mobile.makefive.controller;

import com.mobile.makefive.model.Response;
import com.mobile.makefive.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController extends AbstractController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/api/register")
    public String register(String username, String password) {
        Response response = new Response(Response.STATUS_FAIL, Response.MESSAGE_FAIL);
        try {
            response = accountService.register(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponse(Response.STATUS_SERVER_ERROR, Response.MESSAGE_SERVER_ERROR);
        }
        return gson.toJson(response);
    }

    @PostMapping("/api/point")
    public String getPoint() {
        Response response = new Response(Response.STATUS_FAIL, Response.MESSAGE_FAIL);
        try {
            response = accountService.getPoint();
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponse(Response.STATUS_SERVER_ERROR, Response.MESSAGE_SERVER_ERROR);
        }
        return gson.toJson(response);
    }

}

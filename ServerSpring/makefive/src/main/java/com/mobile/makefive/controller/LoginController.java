package com.mobile.makefive.controller;

import com.mobile.makefive.model.Response;
import com.mobile.makefive.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController extends AbstractController {

    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @GetMapping("/logout_success")
    public String logoutSuccess() {
        System.out.println("logout");
        return gson.toJson(new Response(Response.STATUS_SUCCESS, "logout"));
    }




}

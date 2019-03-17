package com.mobile.makefive.controller;

import com.mobile.makefive.common.Fix;
import com.mobile.makefive.model.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController extends AbstractController{

    @PostMapping( "/logout_success")
    public String logoutSuccess() {
        Response response = new Response(Response.STATUS_FAIL, Response.MESSAGE_FAIL);
        try {

        } catch (Exception e) {
            e.printStackTrace();
            response.setResponse(Response.STATUS_SERVER_ERROR, Response.MESSAGE_SERVER_ERROR);
        }
        return gson.toJson(response);
    }
}

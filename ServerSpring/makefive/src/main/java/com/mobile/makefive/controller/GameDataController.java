package com.mobile.makefive.controller;

import com.mobile.makefive.common.Fix;
import com.mobile.makefive.model.Response;
import com.mobile.makefive.service.GameDataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class GameDataController extends AbstractController {

    private GameDataService matchService;

    public GameDataController(GameDataService matchService) {
        this.matchService = matchService;
    }

    @PostMapping(Fix.MAP_API + "/match/start")
    public String startMatch() {
        System.out.println("startMatch");
        Response response = new Response(Response.STATUS_FAIL, Response.MESSAGE_FAIL);
        try {
            response = matchService.startMatch();
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponse(Response.STATUS_SERVER_ERROR, Response.MESSAGE_SERVER_ERROR);
        }
        return gson.toJson(response);
    }

    @PostMapping(Fix.MAP_API +  "/match/wait")
    public String firstWait(String id) {
        Response response = new Response(Response.STATUS_FAIL, Response.MESSAGE_FAIL);
        try {
            System.out.println("id " + id);
            response = matchService.firstWait(id);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponse(Response.STATUS_SERVER_ERROR, Response.MESSAGE_SERVER_ERROR);
        }
        return gson.toJson(response);
    }

    @PostMapping(Fix.MAP_API + "/match/move")
    public String setMove(String id, Integer col, Integer row) {
        Response response = new Response(Response.STATUS_FAIL, Response.MESSAGE_FAIL);
        try {
//            System.out.println("id " + id + " col " + col + " row " + row);
            response = matchService.setMove(id, col, row);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponse(Response.STATUS_SERVER_ERROR, Response.MESSAGE_SERVER_ERROR);
        }
        return gson.toJson(response);
    }

    @PostMapping(Fix.MAP_API +  "/match/quit")
    public String quitMatch() {
        System.out.println("q1");
        Response response = new Response(Response.STATUS_FAIL, Response.MESSAGE_FAIL);
        try {
            response = matchService.quitMatch();
        } catch (Exception e) {
            e.printStackTrace();
            response.setResponse(Response.STATUS_SERVER_ERROR, Response.MESSAGE_SERVER_ERROR);
        }
        return gson.toJson(response);
    }


}

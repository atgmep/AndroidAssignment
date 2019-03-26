package com.mobile.makefive.service;

import com.google.gson.Gson;
import com.mobile.makefive.common.Fix;
import com.mobile.makefive.common.Methods;
import com.mobile.makefive.common.Validator;
import com.mobile.makefive.entity.TblAccount;
import com.mobile.makefive.model.Response;
import com.mobile.makefive.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {


    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Response register(String username, String password) {
        Methods methods = new Methods();
        Validator valid = new Validator();
        Response response = new Response(Response.STATUS_FAIL, Response.MESSAGE_FAIL);
        username = valid.checkUsername(username);
        if (username == null) {
            response.setResponse(Response.STATUS_FAIL, "Invalid username");
            return response;
        }
        if (accountRepository.findByUsername(username).isPresent()) {
            response.setResponse(Response.STATUS_FAIL, "Username exist");
            return response;
        }

        TblAccount tblAccount = new TblAccount();
        tblAccount.setUsername(username);
        tblAccount.setPassword(methods.hashPass(password));
        tblAccount.setRole(Fix.ROL_MEM);
        tblAccount.setPoint(1000l);
        tblAccount.setWin(0l);
        tblAccount.setLose(0l);
        tblAccount.setStatus(Fix.ACC_NEW.index);
        accountRepository.save(tblAccount);

        response.setResponse(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS);
        return response;
    }

    public Response getPoint() {
        Response response = new Response(Response.STATUS_FAIL, Response.MESSAGE_FAIL);
        Methods methods = new Methods();
        TblAccount user = methods.getUser();
        response.setResponse(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, user.getPoint() + "");
        return response;
    }

    public Response getLeaderBoard(Gson gson) {
        Response response = new Response(Response.STATUS_FAIL, Response.MESSAGE_FAIL);
        List<TblAccount> accountList = accountRepository.findTop10ByOrderByPointDesc();
        String[] stringList = new String[accountList.size()];
        for (int i = 0; i < accountList.size(); i++) {
            stringList[i] = gson.toJson(accountList.get(i));
        }
        response.setResponse(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, stringList);
        return response;
    }
}

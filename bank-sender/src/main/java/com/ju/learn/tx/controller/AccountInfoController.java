package com.ju.learn.tx.controller;

import com.ju.learn.tx.model.AccountChangeEvent;
import com.ju.learn.tx.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping
public class AccountInfoController {
    @Autowired
    private AccountInfoService accountInfoService;


    @RequestMapping("transfer")
    public Object transfer(@RequestParam("accountNo")String accountNo, @RequestParam("amount") Double amount){
        String s = UUID.randomUUID().toString();
        accountInfoService.sendUpdateAccountBalance(new AccountChangeEvent(accountNo,amount,s));
        return "转账成功";
    }

}

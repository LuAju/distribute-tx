package com.ju.learn.tx.service;

import com.ju.learn.tx.dao.AccountInfoDao;
import com.ju.learn.tx.model.AccountChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountInfoService {
    @Autowired
    private AccountInfoDao accountInfoDao;


    //更新账户，
    @Transactional
   public void addAccountInfoBalance(AccountChangeEvent accountChangeEvent){
       if(accountInfoDao.isExistTx(accountChangeEvent.getTxNo())>0){

           return ;
       }
       //增加金额
       accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(),accountChangeEvent.getAmount());
       //添加事务记录，用于幂等
       accountInfoDao.addTx(accountChangeEvent.getTxNo());
       if(accountChangeEvent.getAmount() == 4){
           throw new RuntimeException("人为制造异常");
       }
    }

}

package com.ju.learn.tx.service;

import com.alibaba.fastjson.JSONObject;
import com.ju.learn.tx.dao.AccountInfoDao;
import com.ju.learn.tx.model.AccountChangeEvent;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountInfoService {
    @Autowired
    private AccountInfoDao accountInfoDao;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value("${custom.mq.tx.producer.group}")
    private String txProducerGroup;

    @Value("${custom.mq.destination}")
    private String mqDestination;


    @Value("${custom.mq.key}")
    private String key;

    //向mq发送转账消息
    public void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        // 封装消息对象
        //将accountChangeEvent转成json
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, accountChangeEvent);
        String jsonString = jsonObject.toJSONString();
        //生成message类型
        Message<String> message = MessageBuilder.withPayload(jsonString).build();
        // 发送给消息队列，等待消息队列的恢复
        // 组的名字不能与配置文件中的一致
        rocketMQTemplate.sendMessageInTransaction("bank_sender", mqDestination, message, null);
    }

    //更新账户，扣减金额
    @Transactional
    public void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        if (accountInfoDao.isExistTx(accountChangeEvent.getTxNo()) > 0) {
            return;
        }

        accountInfoDao.updateAccountBalance("1", accountChangeEvent.getAmount() * -1);
//        accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(), accountChangeEvent.getAmount() * -1);
        accountInfoDao.addTx(accountChangeEvent.getTxNo());
        if(accountChangeEvent.getAmount() == 3){
            throw new RuntimeException("人为制造异常");
        }
    }
}

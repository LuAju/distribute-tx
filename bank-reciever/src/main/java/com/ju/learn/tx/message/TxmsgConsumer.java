package com.ju.learn.tx.message;


import com.alibaba.fastjson.JSONObject;
import com.ju.learn.tx.model.AccountChangeEvent;
import com.ju.learn.tx.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @version 1.0
 **/
@Component
@Slf4j
// topic指定监听的生产组的topic
@RocketMQMessageListener(consumerGroup = "sdfs",topic = "topic_send")
public class TxmsgConsumer implements RocketMQListener<String> {

    @Autowired
    AccountInfoService accountInfoService;

    //接收消息
    @Override
    public void onMessage(String message) {
        log.info("开始消费消息:{}",message);
        //解析消息
        JSONObject jsonObject = JSONObject.parseObject(message);
        String accountChangeString = jsonObject.getString("accountChange");
        //转成AccountChangeEvent
        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(accountChangeString, AccountChangeEvent.class);
        //设置账号为李四的
        accountChangeEvent.setAccountNo("2");
        //更新本地账户，增加金额
        accountInfoService.addAccountInfoBalance(accountChangeEvent);

    }
}

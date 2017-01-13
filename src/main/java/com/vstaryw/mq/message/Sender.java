package com.vstaryw.mq.message;

import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.vstaryw.mq.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Sumail on 2017/1/12.
 */
@Component
public class Sender {

    Logger logger = LoggerFactory.getLogger(Sender.class);

    @Resource
    private Producer producer;

    public void sendMessage(String topic,String tags,String key,String body){
        Message msg = new Message(topic,tags,key,body.getBytes());
        SendResult sendResult = null;
        try{
            //无序消费
            sendResult = producer.getDefaultMQProducer().send(msg);
            //顺序模式；假设相同订单号的支付，退款需要放到同一个队列，那么就可以在send的时候，自己实现MessageQueueSelector，根据里面的arg字段来选择queue
//            sendResult = producer.getDefaultMQProducer().send(msg, new MessageQueueSelector() {
//                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
//                    Integer id = Integer.parseInt(o.toString());
//                    logger.info("select queue {}",id);
//                    int index = id % list.size();
//                    return list.get(index);
//                }
//            },"10001"); //orderID “10001”是传递给回调方法的 自定义数据
            // 当消息发送失败时如何处理
            if (sendResult == null || sendResult.getSendStatus() != SendStatus.SEND_OK) {
                logger.warn("send message failed",body);
            }
        }catch (Exception e) {
            logger.error("send message is error",e);
        }
    }

}

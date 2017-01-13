package com.vstaryw.mq.consumer;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.*;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.vstaryw.mq.message.TaskHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by Sumail on 2017/1/11.
 */
public class Consumer {
    Logger logger = LoggerFactory.getLogger(Consumer.class);

    private DefaultMQPushConsumer defaultMQPushConsumer;

    private String namesrvAddr;

    private String consumerGroup;

    private String consumerInstanceName;

    private String subsciption;

    private TaskHandler taskHandler;

    public void init() throws MQClientException {
        // 参数信息
        logger.info("DefaultMQPushConsumer initialize!");
        logger.info(consumerGroup);
        logger.info(namesrvAddr);

        // 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例
        // 注意：ConsumerGroupName需要由应用来保证唯一
        defaultMQPushConsumer = new DefaultMQPushConsumer(consumerGroup);
        defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
        defaultMQPushConsumer.setInstanceName(consumerInstanceName);

        // 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
        // 如果非第一次启动，那么按照上次消费的位置继续消费
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 设置为集群消费(区别于广播消费)
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        if(!Strings.isNullOrEmpty(subsciption)) {
            Map subsciptionMap = JSON.parseObject(subsciption,Map.class);
            defaultMQPushConsumer.setSubscription(subsciptionMap);
        }
        //批量消费消息，一次消费多少条消息，默认为1
        defaultMQPushConsumer.setConsumeMessageBatchMaxSize(500);
        // Consumer对象在使用之前必须要调用start初始化，初始化一次即可
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() { //无序
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for(MessageExt msg : list){
                    try {
                        logger.info("fetch message id is  {}, the key {}",msg.getMsgId(),msg.getKeys());
                        taskHandler.handle(msg.getTopic(),msg.getTags(),new String(msg.getBody(),"UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                       logger.error(e.getMessage(),e);
                    }
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
//        defaultMQPushConsumer.registerMessageListener(new MessageListenerOrderly() { //有序的
//            @Override
//            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
//                for(MessageExt msg : list){
//                    try {
//                        logger.info("fetch message id is  {}, the key {}",msg.getMsgId(),msg.getKeys());
//                        taskHandler.handle(msg.getTopic(),msg.getTags(),new String(msg.getBody(),"UTF-8"));
//                    } catch (UnsupportedEncodingException e) {
//                       logger.error(e.getMessage(),e);
//                    }
//                }
//                return ConsumeOrderlyStatus.SUCCESS;
//            }
//        });
        defaultMQPushConsumer.start();

        logger.info("DefaultMQPushConsumer start success!");
    }



    public void destroy() {
        defaultMQPushConsumer.shutdown();
    }

    public DefaultMQPushConsumer getDefaultMQPushConsumer() {
        return defaultMQPushConsumer;
    }

    public void setDefaultMQPushConsumer(DefaultMQPushConsumer defaultMQPushConsumer) {
        this.defaultMQPushConsumer = defaultMQPushConsumer;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getConsumerInstanceName() {
        return consumerInstanceName;
    }

    public void setConsumerInstanceName(String consumerInstanceName) {
        this.consumerInstanceName = consumerInstanceName;
    }

    public String getSubsciption() {
        return subsciption;
    }

    public void setSubsciption(String subsciption) {
        this.subsciption = subsciption;
    }

    public TaskHandler getTaskHandler() {
        return taskHandler;
    }

    public void setTaskHandler(TaskHandler taskHandler) {
        this.taskHandler = taskHandler;
    }
}

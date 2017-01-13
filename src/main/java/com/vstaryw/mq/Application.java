package com.vstaryw.mq;

import com.vstaryw.mq.message.Sender;
import com.vstaryw.mq.util.ConfigInfo;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Sumail on 2017/1/12.
 */
public class Application {

    private static ClassPathXmlApplicationContext context = null;

    private static AtomicInteger flag = new AtomicInteger(1);


    public static void main(String[] args) {
        context = new ClassPathXmlApplicationContext("classpath:conf/applicationContext.xml");
        context.registerShutdownHook();
        try {
            String topic = ConfigInfo.getStrValue("common_task.topic");
            String tags_1 = ConfigInfo.getStrValue("common_task.tags");
            String tags_2 = ConfigInfo.getStrValue("common_task.tags_2");
            for(int i = 0; i < 100;i++) {
                sendMsg(topic, tags_1, getMsgKey(), topic+":"+tags_1+":hello msg "+ i);
                sendMsg(topic, tags_2, getMsgKey(), topic+":"+tags_2+":hello msg "+ i);
            }
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void sendMsg(String topic,String tags,String key,String body){
        Sender sender = (Sender) context.getBean("sender");
        sender.sendMessage(topic,tags,key,body);
    }

    public static  String getMsgKey(){
        StringBuffer sb = new StringBuffer("msg_");
        sb.append(flag.getAndIncrement());
        return sb.toString();
    }
}

package com.vstaryw.mq.message.task;

import com.vstaryw.mq.message.base.AbstractMessageTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Sumail on 2017/1/12.
 */
public class Topic2MessageTask extends AbstractMessageTask{

    Logger logger = LoggerFactory.getLogger(Topic2MessageTask.class);


    public void execute(String message) {
        logger.info(this.getClass().getSimpleName() + " receive message :{}",message);
    }
}

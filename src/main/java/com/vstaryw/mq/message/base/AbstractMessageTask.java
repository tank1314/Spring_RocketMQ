package com.vstaryw.mq.message.base;

/**
 * Created by Sumail on 2017/1/11.
 */
public abstract class AbstractMessageTask  implements MessageTask {

    private String topic;
    private String tags;


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

}

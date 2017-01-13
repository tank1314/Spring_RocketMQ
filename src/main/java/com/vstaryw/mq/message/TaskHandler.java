package com.vstaryw.mq.message;

import com.vstaryw.mq.message.base.AbstractMessageTask;

import java.util.List;

/**
 * Created by Sumail on 2017/1/12.
 */
public class TaskHandler {

    private List<AbstractMessageTask> taskList;

    public void handle(String topic,String tags,String body){
        if(null != taskList){
           for(AbstractMessageTask task : taskList){
                if(task.getTopic().equals(topic) && task.getTags().equals(tags)){
                    task.execute(body);
                    break;
                }
           }
        }
    }

    public List<AbstractMessageTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<AbstractMessageTask> taskList) {
        this.taskList = taskList;
    }
}

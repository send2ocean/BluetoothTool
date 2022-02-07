package com.fyts.bluetoothtool.bean;

/**
 * @ClassName 接受到消息item
 * @Author lsy
 * @Date 2021/4/15 15:23
 */
public class MessageBean {

    private String name;
    private String time;

    public MessageBean(String name, String time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

package com.fyts.bluetoothtool.bean;

/**
 * @ClassName
 * @Author lsy
 * @Date 2021/4/20 14:56
 */
public class DataBean {

    private String name;
    private double value;

    public DataBean() {
    }

    public DataBean(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

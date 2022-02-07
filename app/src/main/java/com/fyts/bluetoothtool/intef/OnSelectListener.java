package com.fyts.bluetoothtool.intef;

/**
 * Message:  回调接口基类
 * Created by  lsy.
 * Created by Time on 2018/6/4.
 */

public interface OnSelectListener<T> {

    void onChose(String text, String options1, String options2, String options3);

    void onIndex(int pos);

    void onClose();

    void onConfig();

    void onChoseData(String name, String id);

    void onTimeSelect(String time);

    void onConfig(T t);

    void onConfig(T t, T t1);
}

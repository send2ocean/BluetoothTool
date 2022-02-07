package com.fyts.bluetoothtool.intef;

/**
 * Message:  回调的实现类
 * 需要哪个回调 就重新哪个回调
 * Created by  lsy.
 * Created by Time on 2018/6/4.
 */

public class OnSelectListenerImpl<T> implements OnSelectListener<T> {
    /**
     * 地址选择回调
     *
     * @param options1 省
     * @param options2 市
     * @param options3 区
     */
    @Override
    public void onChose(String text, String options1, String options2, String options3) {

    }

    /**
     * 选择的索引
     *
     * @param pos
     */
    @Override
    public void onIndex(int pos) {

    }

    /**
     * 取消
     */
    @Override
    public void onClose() {

    }

    /**
     * 确定
     */
    @Override
    public void onConfig() {

    }

    /**
     * 选中数据的  标题与id
     *
     * @param title
     * @param id
     */
    @Override
    public void onChoseData(String title, String id) {

    }

    /**
     * 时间选择回调
     *
     * @param time
     */
    @Override
    public void onTimeSelect(String time) {

    }

    /**
     * 给定什么类型的数据   就返回什么类型的数据
     *
     * @param t
     */
    @Override
    public void onConfig(T t) {

    }

    /**
     * 给定什么类型的数据   就返回什么类型的数据
     *
     * @param t
     */
    @Override
    public void onConfig(T t, T t1) {

    }
}

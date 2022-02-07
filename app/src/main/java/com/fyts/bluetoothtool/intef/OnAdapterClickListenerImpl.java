package com.fyts.bluetoothtool.intef;

import android.view.View;

/**
 * Message:adpter 的点击事件回调 实现类
 * Created by  lsy.
 * Created by Time on 2018/2/2.
 */

public class OnAdapterClickListenerImpl implements OnAdapterClickListener {
    //单条点击事件
    @Override
    public void onItemClickListener(View v, int pos) {

    }
    //长按事件
    @Override
    public void onItemLongClickListener(View v, int pos) {

    }
    //添加事件
    @Override
    public void onAddItemListener(int pos) {

    }
    //删除事件
    @Override
    public void onDelItemListener(int pos) {

    }
    //选择事件
    @Override
    public void onChoseListener(int pos) {

    }

    @Override
    public void onConfigListener(int pos) {

    }

    //多层嵌套点击事件
    @Override
    public void onChildListener(int parentPos, int pos) {

    }

    @Override
    public void onTypeListener(int type, int pos) {

    }

    @Override
    public void onTypeListener(int type) {

    }

}

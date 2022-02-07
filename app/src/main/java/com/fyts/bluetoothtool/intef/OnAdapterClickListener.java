package com.fyts.bluetoothtool.intef;

import android.view.View;

/**
 * Message: adpter 的点击事件回调
 * Created by  lsy.
 * Created by Time on 2018/2/2.
 */

public interface OnAdapterClickListener {

    void onItemClickListener(View v, int pos);

    void onItemLongClickListener(View v, int pos);

    void onAddItemListener(int pos);

    void onDelItemListener(int pos);

    void onChoseListener(int pos);

    void onConfigListener(int pos);

    void onChildListener(int parentPos, int pos);

    void onTypeListener(int type, int pos);

    void onTypeListener(int type);
}

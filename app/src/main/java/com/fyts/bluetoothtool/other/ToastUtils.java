package com.fyts.bluetoothtool.other;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;


/**
 * Message:  toast工具类
 * Created by  ChenLong.
 * Created by Time on 2018/2/2.
 */

public class ToastUtils {
    //默认样式的toast
    public static void showToast(Context context, String data, int toastType) {
        if (context == null || TextUtils.isEmpty(data)) return;
        Toast toast = Toast.makeText(context, data == null ? "" : data, toastType);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showShort(Context context, String data) {
        showToast(context, data, Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, String data) {
        showToast(context, data, Toast.LENGTH_LONG);
    }

    //列表无数据提示
    public static void showNoData(Context context) {
//        Toast.makeText(context, "到底了呦~", Toast.LENGTH_SHORT).show();
    }
}

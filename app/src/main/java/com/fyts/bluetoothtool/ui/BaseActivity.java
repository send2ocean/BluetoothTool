package com.fyts.bluetoothtool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fyts.bluetoothtool.R;
import com.fyts.bluetoothtool.other.StatusBarUtil;
import com.fyts.bluetoothtool.view.LoadingDialog;

import static com.fyts.bluetoothtool.other.StatusBarUtil.setStatusBarColor;


/**
 * Message:  activity 基类
 */

public abstract class BaseActivity extends AppCompatActivity  {
    //上下文
    protected Activity activity;
    //当前类名
    protected String TAG = getClass().getSimpleName();
    //分页信息
    protected int PAGE = 1;
    protected int SIZE = 10;
    //列表的总页数
    protected int pages;

    protected Bundle savedInstanceState;
    //根布局
    protected View inflate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "当前页面:----" + getLocalClassName());
        this.savedInstanceState = savedInstanceState;
        activity = this;
        if (TAG.toUpperCase().equals("SplashActivity".toUpperCase())) {
            //再次打开app 重新进入启动页问题
            if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                finish();
                return;
            }
            //点击推送合并消息问题
            if (!isTaskRoot()) {
                finish();
                return;
            }
        }
        inflate = LayoutInflater.from(this).inflate(getLayoutId(), null);
        setContentView(inflate);
        initView();
        getData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setStatubarColor();
    }

    //改变状态栏
    protected void setStatubarColor() {
        StatusBarUtil.setStatusBarColor(this, true, R.color.appColor);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 布局layout
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始控件
     */
    protected abstract void initView();

    /**
     * 获取数据
     */
    protected abstract void getData();

    private LoadingDialog loadingDialog; //连网对话框

    /**
     * 网络加载框
     *
     * @param show
     */
    public void showProgress(boolean show) {
        if (show) {
            loadingDialog = new LoadingDialog().setOnTouchOutside(false);
            loadingDialog.show(getFragmentManager(), "iosLoadingDialog");
        } else {
            if (loadingDialog != null) {
                loadingDialog.dismissAllowingStateLoss();
            }
        }
    }
}

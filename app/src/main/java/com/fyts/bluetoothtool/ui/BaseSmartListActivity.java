package com.fyts.bluetoothtool.ui;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyts.bluetoothtool.R;
import com.fyts.bluetoothtool.adapter.BaseRecyclerAdapter;
import com.fyts.bluetoothtool.other.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


/**
 * Message:  带刷新的activity
 */

public abstract class BaseSmartListActivity extends BaseActivity {
    protected SmartRefreshLayout refresh;
    protected RecyclerView recycle;
    private BaseRecyclerAdapter adapter;

    //初始化控件
    protected void findRefresh() {
        refresh = findViewById(R.id.refreshLayout);
        recycle = findViewById(R.id.recycle);
        recycle.setLayoutManager(getLayoutManager());
        adapter = (BaseRecyclerAdapter) getAdapter();
        recycle.setAdapter(adapter);
        refresh.setRefreshHeader(new ClassicsHeader(activity).setSpinnerStyle(SpinnerStyle.Scale));
        refresh.setRefreshFooter(new ClassicsFooter(activity).setSpinnerStyle(SpinnerStyle.Scale));
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                onrefresh();
            }
        });

        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                onloadMore();
            }
        });
//        refresh.setEnablePureScrollMode(true);//是否启用纯滚动模式
    }

    /**
     * 1不刷新只加载 2只刷新不加载  其他不刷新不加载
     * 默认刷新且加载
     *
     * @param refreshType 刷新状态
     */
    protected void setNoRefresh(int refreshType) {
        if (refresh != null) {
            if (refreshType == 1) {
                refresh.setEnableRefresh(false);
            } else if (refreshType == 2) {
                refresh.setEnableLoadMore(false);
            } else {
                refresh.setEnableRefresh(false);
                refresh.setEnableLoadMore(false);
            }
        }
    }

    /**
     * 开始刷新
     */
    protected void startRefresh() {
        if (refresh != null) {
//            refresh.startRefresh();
        }
    }


    /**
     * 获取adapter
     *
     * @return
     */
    protected abstract RecyclerView.Adapter getAdapter();

    /**
     * 获取LayoutManager
     *
     * @return
     */
    protected abstract RecyclerView.LayoutManager getLayoutManager();

    /**
     * 加载跟多
     */
    protected void onloadMore() {
        PAGE++;
        if (PAGE > pages) {
            closeRefresh();
            ToastUtils.showNoData(activity);
        } else {
            getList();
        }
    }

    /**
     * 加载数据
     */
    protected void getList() {
        closeRefresh();
    }

    /**
     * 刷新
     */
    protected void onrefresh() {
        PAGE = 1;
        getList();
    }


    /**
     * 关闭刷新
     */
    protected void closeRefresh() {
        if (refresh != null) {
            if (PAGE == 1) {
                refresh.finishRefresh();
            } else {
                refresh.finishLoadMore();
            }
        }
    }
}

package com.fyts.bluetoothtool.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fyts.bluetoothtool.intef.OnAdapterClickListenerImpl;
import com.fyts.bluetoothtool.other.ToolUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Message:   Adapter基类
 * Created by  ChenLong.
 * Created by Time on 2017/11/23.
 */

public abstract class BaseRecyclerAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    protected Context context;
    protected OnAdapterClickListenerImpl iClickListener;
    protected LayoutInflater inflater;
    protected List<T> mList = new ArrayList<>();

    //加载数据
    public void setData(List<T> list) {
        mList.clear();
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    //跟多数据
    public void setMoreData(List<T> list) {
        if (list != null && list.size() > 0) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    //获取全部数据
    public List<T> getData() {
        return mList;
    }

    //获取选中的数据
    public T getChoseData(int pos) {
        T t = null;
        if (ToolUtils.isList(mList) && pos < mList.size()) {
            t = mList.get(pos);
        }
        return t;
    }


    protected BaseRecyclerAdapter(Context context, OnAdapterClickListenerImpl iClickListener) {
        this.context = context;
        this.iClickListener = iClickListener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getHolder(parent, viewType);
    }


    @Override
    public void onBindViewHolder(@NonNull V holder, @SuppressLint("RecyclerView") final int position) {
        if (getItemCount() > 0 && mList != null) {
            bindHolder(holder, position);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iClickListener != null) {
                    iClickListener.onItemClickListener(v, position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected abstract V getHolder(ViewGroup parent, int viewType);

    protected abstract void bindHolder(V holder, int position);

}

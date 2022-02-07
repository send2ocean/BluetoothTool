package com.fyts.bluetoothtool.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fyts.bluetoothtool.R;
import com.fyts.bluetoothtool.intef.OnAdapterClickListenerImpl;
import com.inuker.bluetooth.library.search.SearchResult;


/**
 * Message: 蓝牙列表
 * Author: lsy
 * Date: 2019/12/20 18:49
 */
public class BluetoothListAdapter extends BaseRecyclerAdapter<SearchResult, BluetoothListAdapter.ViewHolder> {

    public BluetoothListAdapter(Context context, OnAdapterClickListenerImpl iClickListener) {
        super(context, iClickListener);
    }

    @Override
    protected ViewHolder getHolder(ViewGroup parent, int viewType) {
        View inflate = inflater.inflate(R.layout.layout_bluetooth, parent, false);
        return new ViewHolder(inflate);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void bindHolder(ViewHolder holder, final int position) {
        SearchResult bean = mList.get(position);
        holder.tv_title.setText(bean.getName());
        holder.tv_address.setText(bean.getAddress());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_address;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_address = itemView.findViewById(R.id.tv_address);
        }
    }

}

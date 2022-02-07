package com.fyts.bluetoothtool.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fyts.bluetoothtool.R;
import com.fyts.bluetoothtool.bean.DetailItem;
import com.fyts.bluetoothtool.intef.OnAdapterClickListenerImpl;
import com.fyts.bluetoothtool.other.ToolUtils;


/**
 * Message: 服务列表
 * Author: lsy
 * Date: 2019/12/20 18:49
 */
public class ServiceListAdapter extends BaseRecyclerAdapter<DetailItem, ServiceListAdapter.ViewHolder> {

    public ServiceListAdapter(Context context, OnAdapterClickListenerImpl iClickListener) {
        super(context, iClickListener);
    }

    @Override
    protected ViewHolder getHolder(ViewGroup parent, int viewType) {
        View inflate = inflater.inflate(R.layout.layout_service, parent, false);
        return new ViewHolder(inflate);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void bindHolder(ViewHolder holder, final int position) {
        DetailItem bean = mList.get(position);
        if (bean.type == DetailItem.TYPE_SERVICE) {
            holder.view.setVisibility(View.VISIBLE);
            holder.tv_service.setTextColor(ToolUtils.showColor(context, R.color.color_1E90FF));
            holder.tv_service.setText("service：" + bean.uuid.toString());
        } else {
            holder.view.setVisibility(View.GONE);
            holder.tv_service.setTextColor(ToolUtils.showColor(context, R.color.color_333333));
            holder.tv_service.setText("    " + bean.uuid.toString());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_service;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            tv_service = itemView.findViewById(R.id.tv_service);
        }
    }

}

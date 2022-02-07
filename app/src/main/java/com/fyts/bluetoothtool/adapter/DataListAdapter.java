package com.fyts.bluetoothtool.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fyts.bluetoothtool.R;
import com.fyts.bluetoothtool.bean.DataBean;
import com.fyts.bluetoothtool.bean.DetailItem;
import com.fyts.bluetoothtool.intef.OnAdapterClickListenerImpl;
import com.fyts.bluetoothtool.other.ToolUtils;


/**
 * Message: 数据
 * Author: lsy
 * Date: 2019/12/20 18:49
 */
public class DataListAdapter extends BaseRecyclerAdapter<DataBean, DataListAdapter.ViewHolder> {

    public DataListAdapter(Context context, OnAdapterClickListenerImpl iClickListener) {
        super(context, iClickListener);
    }

    @Override
    protected ViewHolder getHolder(ViewGroup parent, int viewType) {
        View inflate = inflater.inflate(R.layout.item_data, parent, false);
        return new ViewHolder(inflate);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void bindHolder(ViewHolder holder, final int position) {
        DataBean dataBean = mList.get(position);
        holder.tv_name.setText(dataBean.getName());
        holder.ed_content.setText(String.valueOf(dataBean.getValue()));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView ed_content;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            ed_content = itemView.findViewById(R.id.ed_content);
        }
    }

}

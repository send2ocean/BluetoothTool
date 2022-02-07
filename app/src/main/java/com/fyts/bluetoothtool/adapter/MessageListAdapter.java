package com.fyts.bluetoothtool.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fyts.bluetoothtool.R;
import com.fyts.bluetoothtool.bean.MessageBean;
import com.fyts.bluetoothtool.intef.OnAdapterClickListenerImpl;
import com.inuker.bluetooth.library.search.SearchResult;


/**
 * Message: 消息列表
 * Author: lsy
 * Date: 2019/12/20 18:49
 */
public class MessageListAdapter extends BaseRecyclerAdapter<MessageBean, MessageListAdapter.ViewHolder> {

    public MessageListAdapter(Context context, OnAdapterClickListenerImpl iClickListener) {
        super(context, iClickListener);
    }

    @Override
    protected ViewHolder getHolder(ViewGroup parent, int viewType) {
        View inflate = inflater.inflate(R.layout.layout_message, parent, false);
        return new ViewHolder(inflate);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void bindHolder(ViewHolder holder, final int position) {
        MessageBean bean = mList.get(position);
        holder.tv_title.setText(bean.getName());
        holder.tv_time.setText(bean.getTime());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView tv_time;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }

}

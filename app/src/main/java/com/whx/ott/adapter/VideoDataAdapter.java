package com.whx.ott.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whx.ott.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HelloWorld on 2016/9/1.
 */
public class VideoDataAdapter extends RecyclerView.Adapter<VideoDataAdapter.MyViewHoder> {

    private List<String> names;
    private List<Integer> ivs;
    private Context context;
    private OnItemClickListener listener;

    public VideoDataAdapter(List<String> names, List<Integer> ivs, Context context) {
        this.names = names;
        this.ivs = ivs;
        this.context = context;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int position);

        public void onItemLongClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_videodata, parent, false);
        MyViewHoder myViewHoder = new MyViewHoder(view);
        return myViewHoder;
    }

    @Override
    public void onBindViewHolder(MyViewHoder holder, int position) {
        holder.iv.setImageResource(ivs.get(position % 1));
        holder.tv.setText(names.get(position));
        setUpEvent(holder);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    protected void setUpEvent(final MyViewHoder holder) {
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int location = holder.getLayoutPosition();
                    listener.onItemClick(holder.itemView, location);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //获取点击的是holder的第几条
                    int location = holder.getLayoutPosition();
                    listener.onItemLongClick(holder.itemView, location);
                    return false;
                }
            });
        }
    }

    class MyViewHoder extends RecyclerView.ViewHolder {
        private TextView tv;
        private ImageView iv;

        public MyViewHoder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.item_rv_iv);
            tv = (TextView) itemView.findViewById(R.id.item_rv_tv);
        }
    }
}

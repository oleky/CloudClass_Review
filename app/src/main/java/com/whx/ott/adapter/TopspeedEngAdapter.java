package com.whx.ott.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whx.ott.R;
import com.whx.ott.beanfeature.SoulcoursesBean;

import java.util.List;

/**
 * Created by HelloWorld on 2017/4/12.
 */

public class TopspeedEngAdapter extends RecyclerView.Adapter<TopspeedEngAdapter.MyViewHolder> {
    private Context context;
    private List<SoulcoursesBean> tvs;
    private List<Integer> ivs;


    public TopspeedEngAdapter(Context context, List<SoulcoursesBean> tvs, List<Integer> ivs) {
        this.context = context;
        this.tvs = tvs;
        this.ivs = ivs;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_videodata, parent,
                false);
        TopspeedEngAdapter.MyViewHolder holder = new TopspeedEngAdapter.MyViewHolder(item);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(tvs.get(position).getSoulcourse_name());
        holder.iv.setImageResource(ivs.get(2));
        holder.itemView.setTag(position);
//        setUpEvent(holder);
    }

    @Override
    public int getItemCount() {
        return tvs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tv;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.item_rv_iv);
            tv = (TextView) itemView.findViewById(R.id.item_rv_tv);
        }
    }
}

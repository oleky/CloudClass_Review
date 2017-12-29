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
    private List<Integer> textcolors;
    private String years;

    public TopspeedEngAdapter(Context context, List<SoulcoursesBean> tvs, String years, List<Integer> ivs, List<Integer> textcolors) {
        this.context = context;
        this.tvs = tvs;
        this.years = years;
        this.ivs = ivs;
        this.textcolors = textcolors;
    }

    public TopspeedEngAdapter(Context context, List<SoulcoursesBean> tvs) {
        this.context = context;
        this.tvs = tvs;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_videodata, parent,
                false);
        MyViewHolder holder = new MyViewHolder(item);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(tvs.get(position).getSoulcourse_name());
        String soul_id = tvs.get(position).getSoulplate_id();
        holder.tv_year.setText(years + "系列");
        holder.itemView.setTag(position);
        if (soul_id.equals(4 + "")) {
            holder.tv.setTextColor(context.getResources().getColor(R.color.gsjdc));
            holder.tv_year.setTextColor(context.getResources().getColor(R.color.gsjdc));
        } else if (soul_id.equals(5 + "")) {
            holder.tv_year.setTextColor(context.getResources().getColor(R.color.dcjyf));
            holder.tv.setTextColor(context.getResources().getColor(R.color.dcjyf));
        }
//        setUpEvent(holder);
    }

    @Override
    public int getItemCount() {
        return tvs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tv;
        ImageView iv;
        TextView tv_year;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.item_rv_iv);
            tv = itemView.findViewById(R.id.item_rv_tv);
            tv_year = itemView.findViewById(R.id.item_year_tv);
        }
    }
}

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
 * Created by HelloWorld on 2016/9/1.
 */
public class VideoDataAdapter extends RecyclerView.Adapter<VideoDataAdapter.MyViewHoder> {

    private List<String> names;
    private List<Integer> ivs;
    private Context context;
    private OnItemClickListener listener;
    private List<Integer> textcolors;
    private String years_name;
    private List<SoulcoursesBean> soulcoursesBeans;

    public VideoDataAdapter(List<String> names, String years_name, List<Integer> ivs, List<Integer> textcolors, Context context) {
        this.names = names;
        this.years_name = years_name;
        this.ivs = ivs;
        this.context = context;
        this.textcolors = textcolors;
        notifyDataSetChanged();
    }

    public VideoDataAdapter(List<SoulcoursesBean> soulcoursesBeans, Context context) {
        this.context = context;
        this.soulcoursesBeans = soulcoursesBeans;
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);

        void onItemLongClick(View itemView, int position);
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
//        holder.iv.setImageResource(ivs.get(position % 1));
//        holder.tv.setText(names.get(position));
        holder.tv.setText(soulcoursesBeans.get(position).getSoulcourse_name());
//        holder.tv_year.setTextColor(textcolors.get(0));
        String soul_id = soulcoursesBeans.get(position).getSoulplate_id();
        if (soul_id.equals(1 + "")) {
            holder.tv.setTextColor(context.getResources().getColor(R.color.jmyc));
            holder.tv_year.setTextColor(context.getResources().getColor(R.color.jmyc));
            holder.iv.setImageResource(R.drawable.jmycbg);
        } else if (soul_id.equals(2 + "")) {
            holder.tv.setTextColor(context.getResources().getColor(R.color.dnb));
            holder.tv_year.setTextColor(context.getResources().getColor(R.color.dnb));
            holder.iv.setImageResource(R.drawable.dnbbg);
        } else if (soul_id.equals(3 + "")) {
            holder.tv.setTextColor(context.getResources().getColor(R.color.xxyd));
            holder.tv_year.setTextColor(context.getResources().getColor(R.color.xxyd));
            holder.iv.setImageResource(R.drawable.sytskbg);
        } else if (soul_id.equals(6 + "")) {
            holder.tv.setTextColor(context.getResources().getColor(R.color.gktxy));
            holder.tv_year.setTextColor(context.getResources().getColor(R.color.gktxy));
            holder.iv.setImageResource(R.drawable.gktxybg);
        } else if (soul_id.equals(7 + "")) {
            holder.tv.setTextColor(context.getResources().getColor(R.color.xxyd));
            holder.tv_year.setTextColor(context.getResources().getColor(R.color.xxyd));
            holder.iv.setImageResource(R.drawable.xxydbg);
        } else {
            holder.tv.setTextColor(context.getResources().getColor(R.color.dnb));
            holder.tv_year.setTextColor(context.getResources().getColor(R.color.dnb));
            holder.iv.setImageResource(R.drawable.dnbbg);
        }
        setUpEvent(holder);
    }

    @Override
    public int getItemCount() {
//        return names.size();
        return soulcoursesBeans.size();
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
        private TextView tv_year;
        private TextView tv;
        private ImageView iv;

        public MyViewHoder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.item_rv_iv);
            tv = itemView.findViewById(R.id.item_rv_tv);
            tv_year = itemView.findViewById(R.id.item_year_tv);
        }
    }
}

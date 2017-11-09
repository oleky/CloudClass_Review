package com.whx.ott.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whx.ott.R;

import java.util.List;

/**
 * Created by HelloWorld on 2016/9/16.
 */
public class MineRcAdapter extends RecyclerView.Adapter<MineRcAdapter.MyViewHolder> {
    private Context context;
    private List<String> tvs;
    private List<Integer> ivs;
    private List<String> create_time_tvs;
    private List<String> year_name_tvs;
    private List<String> term_name1_tvs;
    private OnItemClickListener listener;

    public MineRcAdapter(Context context, List<String> tvs, List<Integer> ivs, List<String> create_time_tvs, List<String> year_name_tvs, List<String> term_name1_tvs) {
        this.context = context;
        this.tvs = tvs;
        this.ivs = ivs;
        this.create_time_tvs = create_time_tvs;
        this.year_name_tvs = year_name_tvs;
        this.term_name1_tvs = term_name1_tvs;
    }

    public MineRcAdapter(Context context, List<String> tvs, List<Integer> ivs, List<String> create_time_tvs, List<String> year_name_tvs) {
        this.context = context;
        this.tvs = tvs;
        this.ivs = ivs;
        this.create_time_tvs = create_time_tvs;
        this.year_name_tvs = year_name_tvs;
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int position);

        public void onItemLongClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_mine_values, parent,
                false);
        MyViewHolder holder = new MyViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(tvs.get(position));
        holder.iv.setImageResource(ivs.get(position % 6));
        holder.create_time_tv.setText(create_time_tvs.get(position));
        holder.year_name_tv.setText(year_name_tvs.get(position));
        if (term_name1_tvs != null) {
            holder.term_name1_tv.setText(term_name1_tvs.get(position));
        }
        holder.itemView.setTag(position);
        setUpEvent(holder);
    }

    @Override
    public int getItemCount() {
        return tvs.size();
    }

    protected void setUpEvent(final MyViewHolder holder) {
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

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView term_name1_tv;
        TextView grade_name_tv;
        TextView term_name_tv;
        TextView year_name_tv;
        TextView tv;
        ImageView iv;
        TextView create_time_tv;

        public MyViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.item_mine_iv);
            tv = (TextView) view.findViewById(R.id.id_item_mine_class_name);
            create_time_tv = (TextView) view.findViewById(R.id.id_item_mine_create_time);
            year_name_tv = (TextView) view.findViewById(R.id.id_item_mine_year_name);
            term_name_tv = (TextView) view.findViewById(R.id.id_item_mine_term_name);
            term_name1_tv = (TextView) view.findViewById(R.id.id_item_mine_term_name1);
            grade_name_tv = (TextView) view.findViewById(R.id.id_item_mine_grade_name);
        }

    }
}

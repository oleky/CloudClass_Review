package com.whx.ott.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whx.ott.R;
import com.whx.ott.beanfeature.NewMineFeatureBean;
import com.whx.ott.conn.NumToString;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HelloWorld on 2017/2/22.
 */

public class NewAllClassNameAdapter extends RecyclerView.Adapter<NewAllClassNameAdapter.MyViewHolder> {
    private Context context;
    private List<String> all_tvs;
    private List<String> names;
    private HashMap<String, String> values;
    private OnItemClickListener listener;


    public NewAllClassNameAdapter(Context context, HashMap<String, String> values) {
        this.context = context;
        this.values = values;
    }

    public NewAllClassNameAdapter(Context context, List<String> names, List<String> all_tvs) {
        this.context = context;
        this.names = names;
        this.all_tvs = all_tvs;
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
        View item = LayoutInflater.from(context).inflate(R.layout.item_newallcallfeature_class, parent,
                false);
        MyViewHolder holder = new MyViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        int id = Integer.parseInt(names.get(position).trim());
        if (all_tvs.get(position).equals("all")) {
            holder.all_tv.setText("无限次");
        } else if (all_tvs.get(position).equals("0")) {
            holder.all_tv.setText("0次");
        } else {
            holder.all_tv.setText(all_tvs.get(position)+"次");
        }
        holder.class_name_tv.setText(NumToString.searchSubject(id)+"每节可播:");
        setUpEvent(holder);
    }

    @Override
    public int getItemCount() {
        return names.size();
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

        TextView all_tv;
        TextView used_tv;
        TextView class_name_tv;

        public MyViewHolder(View view) {
            super(view);
            class_name_tv = (TextView) view.findViewById(R.id.item_allclass_feature_name);
//            used_tv = (TextView) view.findViewById(R.id.item_allclass_feature_used);
            all_tv = (TextView) view.findViewById(R.id.item_allclass_feature_all);
        }

    }
}
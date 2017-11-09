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
 * Created by HelloWorld on 2016/9/18.
 */
public class NewAllClassAdapter extends RecyclerView.Adapter<NewAllClassAdapter.MyViewHolder> {
    private Context context;
    private List<String> used_tvs;
    private List<String> all_tvs;
    private List<String> names;
    private OnItemClickListener listener;

    public NewAllClassAdapter(Context context, List<String> used_tvs, List<String> all_tvs, List<String> names) {
        this.context = context;
        this.used_tvs = used_tvs;
        this.all_tvs = all_tvs;
        this.names = names;
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
        View item = LayoutInflater.from(context).inflate(R.layout.item_newallcallfeature, parent,
                false);
        MyViewHolder holder = new MyViewHolder(item);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.all_tv.setText(all_tvs.get(position));
        holder.used_tv.setText(used_tvs.get(position));
        holder.class_name_tv.setText(names.get(position));
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
            used_tv = (TextView) view.findViewById(R.id.item_allclass_feature_used);
//            all_tv = (TextView) view.findViewById(R.id.item_allclass_feature_all);
        }

    }
}
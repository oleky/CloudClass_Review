package com.whx.ott.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.whx.ott.R;

import java.util.List;

/**
 * Created by HelloWorld on 2016/9/9.
 */
public class TopMenuAdapter extends RecyclerView.Adapter<TopMenuAdapter.MyViewHolder> {
    private Context context;
    private List<String> names;
    private OnItemClickListener listener;

    public TopMenuAdapter(Context context, List<String> names) {
        this.context = context;
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
        View item = LayoutInflater.from(context).inflate(R.layout.item_topmenu, parent,
                false);
        MyViewHolder holder = new MyViewHolder(item);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.btn.setText(names.get(position));
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


        private Button btn;

        public MyViewHolder(View view) {
            super(view);
            btn = (Button) view.findViewById(R.id.top_menu_btn);
        }

    }

}

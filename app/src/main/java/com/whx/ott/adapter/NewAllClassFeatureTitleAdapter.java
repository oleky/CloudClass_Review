package com.whx.ott.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whx.ott.R;

import java.util.List;

/**
 * Created by HelloWorld on 2017/11/17.
 */

public class NewAllClassFeatureTitleAdapter extends RecyclerView.Adapter<NewAllClassFeatureTitleAdapter.MyViewHolder> {
    private List<String> names;
    private Context context;
    private int color;

    public NewAllClassFeatureTitleAdapter(Context context, List<String> names, int color) {
        this.names = names;
        this.context = context;
        this.color = color;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_allclass_two, parent, false);
        MyViewHolder viewHolde = new MyViewHolder(view);
        return viewHolde;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_allclass_title_top.setText(names.get(position));
        holder.tv_allclass_title_top.setTextColor(context.getResources().getColor(color));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_allclass_title_top;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_allclass_title_top = itemView.findViewById(R.id.tv_allclass_title_top);
        }
    }
}

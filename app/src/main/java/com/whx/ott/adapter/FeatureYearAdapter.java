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
 * Created by HelloWorld on 2017/11/7.
 */

public class FeatureYearAdapter extends RecyclerView.Adapter<FeatureYearAdapter.ViewHolder> {
    private Context context;
    private List<String> names;

    public FeatureYearAdapter(Context context, List<String> names) {
        this.context = context;
        this.names = names;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_featurn_tv, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_feature.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_feature;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_feature = itemView.findViewById(R.id.tv_feature_item);
        }
    }
}

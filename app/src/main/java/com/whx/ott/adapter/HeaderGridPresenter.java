package com.whx.ott.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.whx.ott.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 带标题头的 Grid RecyclerView测试.
 */
public class HeaderGridPresenter extends OpenPresenter {

    private static final int ITEM_VIEW_TYPE_HEADER = 0; // 头
    private static final int ITEM_VIEW_TYPE_ITEM = 1; // item.

    private List<String> tvs;
    private List<Integer> ivs;
    private List<String>names;
    private int headerPosition;

    public HeaderGridPresenter(List<String> tvs, List<Integer> ivs, List<String> names, int headerPosition) {
        this.tvs = tvs;
        this.ivs = ivs;
        this.names = names;
        this.headerPosition = headerPosition;
    }

    public HeaderGridPresenter(List<String> tvs, List<Integer> ivs, List<String> names) {
        this.tvs = tvs;
        this.ivs = ivs;
        this.names = names;
    }


//    public HeaderGridPresenter(int count) {
//        this.tvs = new ArrayList<String>(count);
//        for (int i = 0; i < count; i++) {
//            tvs.add(String.valueOf(i));
//        }
//    }

    /**
     * 判断是否为Header.
     */
    public boolean isHeader(int position) {
        return (position % 7) == 0;
    }

    @Override
    public int getItemCount() {
        return tvs.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            View headview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_view, parent, false);
            return new GridViewHolder(headview);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_view, parent, false);
        return new GridViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (getItemViewType(position)==ITEM_VIEW_TYPE_HEADER) {
            GridViewHolder.head_tv.setText(names.get(headerPosition));
            return;
        }
        GridViewHolder1.ivs.setImageResource(ivs.get(position));
        GridViewHolder1.tvs.setText(tvs.get(position));
    }


}

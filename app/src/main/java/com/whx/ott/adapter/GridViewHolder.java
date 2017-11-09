package com.whx.ott.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whx.ott.R;


public class GridViewHolder extends OpenPresenter.ViewHolder {

    public static TextView head_tv;

    public GridViewHolder(View itemView) {
        super(itemView);
        head_tv = (TextView) itemView.findViewById(R.id.head_tv);

    }

}

class GridViewHolder1 extends OpenPresenter.ViewHolder {

    public static ImageView ivs;
    public static TextView tvs;

    public GridViewHolder1(View view) {
        super(view);
        ivs = (ImageView) view.findViewById(R.id.item_rv_iv);
        tvs = (TextView) view.findViewById(R.id.item_rv_tv);
    }
}

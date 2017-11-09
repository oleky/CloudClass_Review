package com.whx.ott.adapter;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Button;

import com.whx.ott.R;
import java.util.List;

/**
 * RecyclerView 的菜单.
 */
public class LeftMenuPresenter extends OpenPresenter {
    public static int headerPosition;
    private List<String> strList;

    public LeftMenuPresenter(List<String> strList) {
        this.strList = strList;
    }


    @Override
    public int getItemCount() {
        return strList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Button btn = new Button(parent.getContext());
        return new ViewHolder(btn);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Button btn = (Button) viewHolder.view;
        btn.setFocusableInTouchMode(true);
        btn.setFocusable(true);
        btn.setBackground(null);
        btn.setTextSize(25);
        btn.setTextColor(Color.WHITE);
        String str = strList.get(position);
        btn.setBackgroundResource(R.drawable.button);
        btn.setText(str);
        headerPosition = position;
    }

}

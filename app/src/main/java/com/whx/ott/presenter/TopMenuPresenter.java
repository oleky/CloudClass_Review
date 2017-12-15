package com.whx.ott.presenter;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Button;

import com.whx.ott.R;
import com.whx.ott.adapter.OpenPresenter;

import java.util.List;

/**
 * Created by HelloWorld on 2016/9/18.
 * 我的课程界面内特色课已播信息标题
 */

public class TopMenuPresenter extends OpenPresenter {
    private List<String> strList;

    public TopMenuPresenter(List<String> strList) {
        this.strList = strList;
    }


    @Override
    public int getItemCount() {
        return strList.size();
    }

    @Override
    public OpenPresenter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Button btn = new Button(parent.getContext());
        return new OpenPresenter.ViewHolder(btn);
    }

    @Override
    public void onBindViewHolder(OpenPresenter.ViewHolder viewHolder, int position) {
        Button btn = (Button) viewHolder.view;
        btn.setFocusableInTouchMode(true);
        btn.setFocusable(true);
        btn.setBackground(null);
        btn.setTextSize(18);
        btn.setTextColor(Color.WHITE);
        String str = strList.get(position);
        btn.setBackgroundResource(R.drawable.button);
        btn.setText(str);
    }

}

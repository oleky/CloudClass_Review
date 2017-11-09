package com.whx.ott.adapter;

/**
 * Created by HelloWorld on 2016/8/26.
 */

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.whx.ott.R;
import com.whx.ott.widget.TextViewWithTTF;


public class OpenTabTitleAdapter extends BaseTabTitleAdapter {
    private List<String> titleList = new ArrayList<String>();

    public OpenTabTitleAdapter() {
        titleList.add("精越系列");
        titleList.add("绝密预测");
        titleList.add("大年班");
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    /**
     * 为何要设置ID标识。<br>
     * 因为PAGE页面中的ITEM如果向上移到标题栏， <br>
     * 它会查找最近的，你只需要在布局中设置 <br>
     * android:nextFocusUp="@+id/title_bar1" <br>
     * 就可以解决焦点问题哦.
     */
    private List<Integer> ids = new ArrayList<Integer>() {
//        {
//            add(R.id.title_bar1);
//            add(R.id.title_bar2);
//            add(R.id.title_bar3);
//        }
    };

    @Override
    public Integer getTitleWidgetID(int pos) {
        return ids.get(pos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        parent.getContext();
        String title = titleList.get(position);
        if (convertView == null) {
            convertView = newTabIndicator(parent.getContext(), title, false);
            convertView.setId(ids.get(position)); // 设置ID.
        } else {
        }
        return convertView;
    }

    private View newTabIndicator(Context context, String tabName, boolean focused) {
        final String name = tabName;
        View viewC = View.inflate(context, R.layout.tab_view_indicator_item, null);
        TextViewWithTTF view = (TextViewWithTTF) viewC.findViewById(R.id.tv_tab_indicator);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 0, 20, 0);
        view.setLayoutParams(lp);


        view.setText(name);

        if (focused == true) {
            Resources res = context.getResources();
            view.setTextColor(res.getColor(android.R.color.white));
            view.setTypeface(null, Typeface.BOLD);
            view.requestFocus();
        }
        return viewC;
    }
}
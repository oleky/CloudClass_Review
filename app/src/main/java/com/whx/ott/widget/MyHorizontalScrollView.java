package com.whx.ott.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by HelloWorld on 2017/11/16.
 */

public class MyHorizontalScrollView extends HorizontalScrollView {

    private ScrollViewListener scrollViewListener = null;
    private View scrollView;

    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollView != null) {
            scrollView.scrollTo(x, y);
        }
//        if (scrollViewListener != null) {
//            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
//        }
    }

    public interface ScrollViewListener {
        void onScrollChanged(MyHorizontalScrollView scrollView, int x, int y, int oldx, int oldy);
    }

    public void setScollViewTo(View view) {
        scrollView = view;
    }
}

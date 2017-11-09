package com.whx.ott.util;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.whx.ott.R;
import com.whx.ott.application.YunApplication;


/**
 * Created by oleky on 2016/8/16.
 * 屏幕工具类：图片获取，dp,sp,px转换
 */
public class Utils {
    public Utils() {
    }

    /**
     * 获取SDK版本
     */
    public static int getSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }
        return version;
    }
    /**
     * @return 屏幕显示尺寸
     */
    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    /**
     * 图片获取类
     * */
    public static void showImage(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .into(imageView);

    }

    /**
     * Shows a (long) toast
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Shows a (long) toast.
     */
    public static void showToast(Context context, int resourceId) {
        Toast.makeText(context, context.getString(resourceId), Toast.LENGTH_LONG).show();
    }
    /**
     * 获取状态栏高度
     */
    public static int getSystemBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 毫秒转hh:mm:ss
     */
    public static String formatMillis(int millis) {
        String result = "";
        int hr = millis / 3600000;
        millis %= 3600000;
        int min = millis / 60000;
        millis %= 60000;
        int sec = millis / 1000;
        if (hr > 0) {
            result += hr + ":";
        }
        if (min >= 0) {
            if (min > 9) {
                result += min + ":";
            } else {
                result += "0" + min + ":";
            }
        }
        if (sec > 9) {
            result += sec;
        } else {
            result += "0" + sec;
        }
        return result;
    }

    /**
     *
     * @param context
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}

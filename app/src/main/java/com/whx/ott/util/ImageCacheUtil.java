package com.whx.ott.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import java.io.File;

/**
 * Created by oleky on 2016/11/26.
 */

public class ImageCacheUtil {
    private static LruCache<String,Bitmap> cache;
    private Context context;
    public ImageCacheUtil(Context context) {
        this.context = context;
        //获取可占内存的1/8作为缓存
        int maxSize = (int) (Runtime.getRuntime().maxMemory()/8);


        cache = new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }


    /**
     * 从LrcCache,SDCard中获取Bitmap
     * */
    public Bitmap getBitmap(String s) {
        if (cache.get(s) != null) {
            //从LrcCache中获取
//            Log.i("tag", "从LrcCache获取");
            return cache.get(s);
        } else {
            String imgName =s.substring(s.lastIndexOf("/")+1);
            String  filePath = context.getExternalCacheDir().getAbsolutePath()+ File.separator+imgName;
            Bitmap bitmap=null;
            if (new File(filePath).exists()) {
                byte[] data = SDCardHelper.loadFileFromSDCard(filePath);
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                cache.put(s, bitmap);
//                Log.i("tag", "从SDCard中获取");
            }
            return bitmap;
        }

    }

    /**
     * 将Bitmap存入LrcCache,SDCard中
     * */
    public void putBitmap(String s, Bitmap bitmap) {
        //存入LrcCache
        cache.put(s, bitmap);
        //判断SDCard中是否已经存好，如果没有，存入SDCard
        String imgName =s.substring(s.lastIndexOf("/")+1);
        String  filePath = context.getExternalCacheDir().getAbsolutePath()+ File.separator+imgName;
        if (!new File(filePath).exists()) {
            SDCardHelper.saveBitmapToSDCardCacheDir(bitmap, imgName, context);
        }
    }
}

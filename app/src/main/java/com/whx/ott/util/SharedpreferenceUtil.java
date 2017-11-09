package com.whx.ott.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by oleky on 2016/9/17.
 */
public class SharedpreferenceUtil {
    //存储的sharedpreferences文件名
    private static final String FILE_NAME = "userinfo";

    /**
     * 保存数据到文件
     * @param context
     * @param key
     * @param data
     */
    public static void saveData(Context context, String key, Object data){

        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if ("Integer".equals(type)){
            editor.putInt(key, (Integer)data);
        }else if ("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)data);
        }else if ("String".equals(type)){
            editor.putString(key, (String)data);
        }else if ("Float".equals(type)){
            editor.putFloat(key, (Float)data);
        }else if ("Long".equals(type)){
            editor.putLong(key, (Long)data);
        }

        editor.commit();
    }

    /**
     * 从文件中读取数据
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static Object getData(Context context, String key, Object defValue){

        String type = defValue.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (FILE_NAME, Context.MODE_PRIVATE);

        //defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)){
            return sharedPreferences.getInt(key, (Integer)defValue);
        }else if ("Boolean".equals(type)){
            return sharedPreferences.getBoolean(key, (Boolean)defValue);
        }else if ("String".equals(type)){
            return sharedPreferences.getString(key, (String)defValue);
        }else if ("Float".equals(type)){
            return sharedPreferences.getFloat(key, (Float)defValue);
        }else if ("Long".equals(type)){
            return sharedPreferences.getLong(key, (Long)defValue);
        }

        return null;
    }

    /**
     * 存储对象到sp中
     * 注意：对象要经过序列化！
     * @param key 键名
     * @param obj 对象
     * */
    public static void saveObj2Sp(Context context,String key,Object obj) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            String value = new String(Base64.encode(bos.toByteArray()));
            saveData(context, key, value);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从sp中读取对象
     * @param key 键名
     * @return 对象或null
     */
    public static Object queryObj2Sp(Context context, String key) {
        String value = (String) getData(context, key, "");
        if (value != null && !value.equals("")) {
            byte[] valueBytes = Base64.decode(value);
            ByteArrayInputStream bis = new ByteArrayInputStream(valueBytes);
            try {
                ObjectInputStream ois = new ObjectInputStream(bis);
                return ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}


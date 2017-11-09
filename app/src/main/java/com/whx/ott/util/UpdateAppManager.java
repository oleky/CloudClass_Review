package com.whx.ott.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.whx.ott.bean.ParseVersion;
import com.whx.ott.conn.Conn;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;

/**
 * Created by oleky on 2016/9/17.
 */
public class UpdateAppManager {

    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() +"/" + "yunketang" +"/";
    // 下载应用存放全路径
    private static final String FILE_NAME = FILE_PATH + "yunketang.apk";
    // 准备安装新版本应用标记
    private static final int INSTALL_TOKEN = 1;
    //Log日志打印标签
    private static final String TAG = "Update_log";

    private Context context;
    //获取版本数据的地址
    private String version_path = Conn.BASEURL+Conn.CHECKVERSON;
    //获取新版APK的默认地址
    private String apk_path = "";
    // 下载应用的进度条
    private ProgressDialog progressDialog;
    ParseVersion   getversion;

    //新版本号和描述语言
    private int update_versionCode;
    private String update_describe = "页面更新，优化内存,改善用户体验";

    public UpdateAppManager(Context mContext) {
        this.context = mContext;
    }


    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }
    /**
     * 从服务器获得更新信息
     */
    public void getUpdateMsg() {

        OkHttpUtils.get()
                .url(version_path)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        getversion = ParseVersion.getVersion(response);
                        if (getversion.getCode() == 0) {
                            int ver = getversion.getData().get(0).getApp_version();
                            int myver =  getVersionCode(context);
                            if (myver < ver) {
                                //更新数据
                                apk_path = getversion.getData().get(0).getApp_url();
                                update_describe = getversion.getData().get(0).getApp_message();
                                showNoticeDialog();

                            }
                        }
                    }
                });
    }


    /**
     * 显示提示更新对话框
     */
    private void showNoticeDialog() {
        new AlertDialog.Builder(context)
                .setTitle("检测到新版本！")
                .setMessage(update_describe)
                .setPositiveButton("立刻下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDownloadDialog();
                    }
                }).setCancelable(false).create().show();
    }

    /**
     * 显示下载进度对话框
     */
    public void showDownloadDialog() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        new downloadAsyncTask().execute();
    }

    /**
     * 下载新版本应用
     */
    private class downloadAsyncTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            Log.e(TAG, "执行至--onPreExecute");
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {

                    Log.e(TAG, "执行至--doInBackground");

                    URL url;
                    HttpURLConnection connection = null;
                    InputStream in = null;
                    FileOutputStream out = null;
                    try {
                        url = new URL(apk_path);
                        connection = (HttpURLConnection) url.openConnection();

                        in = connection.getInputStream();
                        long fileLength = connection.getContentLength();
                        File file_path = new File(FILE_PATH);
                        if (!file_path.exists()) {
                            file_path.mkdir();
                }

                out = new FileOutputStream(new File(FILE_NAME));//为指定的文件路径创建文件输出流
                byte[] buffer = new byte[1024 * 1024];
                int len = 0;
                long readLength = 0;


                while ((len = in.read(buffer)) != -1) {

                    out.write(buffer, 0, len);//从buffer的第0位开始读取len长度的字节到输出流
                    readLength += len;
                    int curProgress = (int) (((float) readLength / fileLength) * 100);

                    publishProgress(curProgress);

                    if (readLength >= fileLength) {

                        break;
                    }
                }

                out.flush();
                return INSTALL_TOKEN;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {

            progressDialog.dismiss();//关闭进度条
            //安装应用
            installApp();
        }
    }

    /**
     * 安装新版本应用
     */
    private void installApp() {
        File appFile = new File(FILE_NAME);
        if (!appFile.exists()) {
            return;
        }
        // 跳转到新版本应用安装页面
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

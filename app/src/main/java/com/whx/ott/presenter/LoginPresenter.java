package com.whx.ott.presenter;

import android.content.Context;
import android.util.Log;

import com.whx.ott.bean.ParseLogin;
import com.whx.ott.conn.ApiService;
import com.whx.ott.conn.RetrofitClient;
import com.whx.ott.presenter.viewinface.LoginView;
import com.whx.ott.util.RxUtil;
import com.whx.ott.util.SharedpreferenceUtil;
import com.whx.ott.util.TimeUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import io.reactivex.functions.Consumer;

/**
 * Created by oleky on 2017/9/10.
 */

public class LoginPresenter extends Presenter{

    private LoginView mLoginView;
    private ApiService mService;
    private Context mContext;
    private String macAdress = "00:00:00:00:00:00";


    public LoginPresenter(Context context,LoginView loginView) {
        mLoginView = loginView;
        mContext = context;
        mService = new RetrofitClient().createApiClient();
        macAdress = getMacAddress()+"";  //量产版小盒子
//        macAdress = "e8:bb:3d:67:5b:3a";  //测试版mac账号(276号盒子) 605666
//        macAdress = "d8:96:e0:03:19:bd";  //测试版mac账号(858号盒子)
//        macAdress = "00:04:a3:01:83:db";
        macAdress = "d8:96:e0:03:1a:aa";
//        macAdress = "7c:c7:09:93:46:6a"; //275账号微炮盒子 test222

        if (macAdress.equals("")) {
            macAdress = getLocalMacAddressFromIp(mContext); //其他版双网卡盒子
        }
        if (macAdress != null) {
            SharedpreferenceUtil.saveData(mContext, "dev_id", macAdress);
        } else {
            SharedpreferenceUtil.saveData(mContext, "dev_id", "00:00:00:00:00:00");
        }
    }


    public void login(String username, String password) {
        mService.login(username,password,macAdress)
                .compose(RxUtil.<ParseLogin>rxScheduleHelper())
                .subscribe(data -> {
                    String code = data.getCode();
                    if (null != mLoginView) {
                        if ("0".equals(code)) {
                            long valideTime = TimeUtils.string2Millis(data.getData().get(0).getValide_time());
                            long currentTime = TimeUtils.getNowTimeMills();
                            SharedpreferenceUtil.saveData(mContext, "user_validetime", data.getData().get(0).getValide_time());

                            if (valideTime - currentTime < 0) {
                                mLoginView.loginFailed("本机代理期限已过期");
                                SharedpreferenceUtil.saveData(mContext, "hasLogin", false);
                            } else {
                                SharedpreferenceUtil.saveData(mContext, "hasLogin", true);
                                SharedpreferenceUtil.saveData(mContext, "user_id", data.getData().get(0).getUser_id());
                                SharedpreferenceUtil.saveData(mContext, "user_name", data.getData().get(0).getUser_name());
                                SharedpreferenceUtil.saveData(mContext, "user_money", data.getData().get(0).getUser_money());
                                mLoginView.loginSuccess();
                            }
                        } else {
                            if (!data.getMeg().contains("密码")) {
                                mLoginView.loginFailed("登录失败\n读取到的mac地址为：" + macAdress);
                            } else {
                                mLoginView.loginFailed(data.getMeg());
                            }
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (null != mLoginView) {
                            mLoginView.loginFailed("请检查网络");
                        }
                    }
                });

    }


    public String getMacAddress() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        if (result == null) {
            return "网络出错，请检查网络";
        }
        if (result.length() > 0 && result.contains("HWaddr")) {
            Mac = result.substring(result.indexOf("HWaddr") + 6, result.length() - 1);
            if (Mac.length() > 1) {
                result = Mac.toLowerCase();
            }
        }
        return result.trim();
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            //执行命令cmd，只取结果中含有filter的这一行
            while ((line = br.readLine()) != null && line.contains(filter) == false) {
                //result += line;
                Log.i("test", "line: " + line);
            }

            result = line;
            Log.i("test", "result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //根据IP获取本地Mac
    public static String getLocalMacAddressFromIp(Context context) {
        String mac_s = "";
        try {
            byte[] mac;
            NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
            mac = ne.getHardwareAddress();
            String str = byte2hex(mac);
            int size = ((str.length()) % 2 == 0) ? ((str.length()) / 2) : ((str.length()) / 2 + 1);
            for (int i = 0; i < size; i++) {
                int endIndex = (i + 1) * 2;
                if ((i + 1) == size) {
                    endIndex = str.length();
                }
                if (i == 0) {
                    mac_s += str.substring(i, endIndex);
                } else {
                    mac_s += ":" + str.substring(i * 2, endIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mac_s;
    }

    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs.append("0").append(stmp);
            else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }

    //获取本地IP
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return null;
    }

    @Override
    public void onDestory() {
        if (null != mLoginView) {
            mLoginView = null;
        }
    }
}

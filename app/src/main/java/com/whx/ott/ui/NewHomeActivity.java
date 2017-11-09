package com.whx.ott.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.whx.ott.R;
import com.whx.ott.bean.BaseInfo;
import com.whx.ott.bean.CoursesBean;
import com.whx.ott.bean.GradesBean;
import com.whx.ott.bean.HomeInfo;
import com.whx.ott.bean.ParseSearch;
import com.whx.ott.bean.Soulplates;
import com.whx.ott.bean.SubjectsBean;
import com.whx.ott.bean.TeachersBean;
import com.whx.ott.bean.TermsBean;
import com.whx.ott.bean.YearsBean;
import com.whx.ott.conn.Conn;
import com.whx.ott.conn.JsonGetUserInfo;
import com.whx.ott.conn.JsonPostPosition;
import com.whx.ott.db.DBManager;
import com.whx.ott.db.TownDBManager;
import com.whx.ott.presenter.BaseInfoPresenter;
import com.whx.ott.presenter.SearchPresenter;
import com.whx.ott.presenter.viewinface.BaseInfoView;
import com.whx.ott.presenter.viewinface.SearchView;
import com.whx.ott.util.FindIPUtils;
import com.whx.ott.util.KeyBoardUtils;
import com.whx.ott.util.LocationUtils;
import com.whx.ott.util.SharedpreferenceUtil;
import com.whx.ott.util.TimeUtils;
import com.whx.ott.util.UpdateAppManager;
import com.whx.ott.widget.FancyCoverFlow;
import com.whx.ott.widget.FilmInfoTest;
import com.whx.ott.widget.ImageAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by oleky on 2016/10/18.
 */

public class NewHomeActivity extends Activity implements BaseInfoView,SearchView{

    private FancyCoverFlow fancyCoverFlow;
    private List<HomeInfo> filmList;
    private ImageAdapter adapter;
    private EditText editText;
    private DBManager manager;
    private TownDBManager mTownDBManager;

    private BaseInfoPresenter mInfoPresenter;
    private SearchPresenter mSearchPresenter;

    private UpdateAppManager updateAppManager;
    private String macAdress;
    private int cur_index = 0;

    //单次定位
    private AMapLocationClient locationClientSingle = null;

    private String user_pos;
    private String province; // 省
    private String city; //市
    private String district; // 区/县
    private String user_id;
    private String user_name;

    private String myIP = ""; //获取的外网IP

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                user_pos = (String) msg.obj;
                startSingleLocation();
            }
        }
    };
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_demo);
        //Context.MODE_APPEND可以对已存在的值进行修改
        mInfoPresenter = new BaseInfoPresenter(this, this);
        mSearchPresenter = new SearchPresenter(this);
        user_id = (String) SharedpreferenceUtil.getData(this, "user_id", "");
        user_name = (String) SharedpreferenceUtil.getData(this, "user_name", "");
        macAdress = (String) SharedpreferenceUtil.getData(this, "dev_id", "");
        SharedpreferenceUtil.saveData(this, "lasttime", TimeUtils.getNowTimeMills()+"");
        manager = new DBManager(this);
        mTownDBManager = new TownDBManager(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initEditText();
        initFancyCoverFlow();
        getIP();
        mInfoPresenter.getAddress(user_id);
        mInfoPresenter.getBaseInfo(user_id);
        mInfoPresenter.getTownBaseInfo(user_id);

        updateAppManager = new UpdateAppManager(this);
        updateAppManager.getUpdateMsg();

    }

    /**
     * 用于检测用户录入位置与实际位置是否相符
     * */
    private void checkPos() {
        String mydistrict = district.substring(0, district.length() - 1);
        if (!user_pos.contains(mydistrict)) {
            Log.e("checkpos", "获取位置与存储位置不相同");
            String currentPos = (String) SharedpreferenceUtil.getData(this, "localposition", "定位失败1");
            /**
             * ------------------------------------------------------------
             * 上线前解除注释，提交服务器position
             * ------------------------------------------------------------
             * */
            if (!currentPos.equals(province + city + district)) {
                //当与存储的位置不同时候，提交位置信息
                mInfoPresenter.uploadAddress(user_id, user_name, province + city + district + "参考IP" + myIP, macAdress);
            }
        }
    }

    /**
     * 查看用户代理权限是否到期
     */
    private void checkTime() {
        long curTime = TimeUtils.getNowTimeMills();
        String vali = (String) SharedpreferenceUtil.getData(this, "user_validetime", "");
        if (!TextUtils.isEmpty(vali)) {
            long valideTime = TimeUtils.string2Millis(vali);
            Log.e("limit", (valideTime - curTime) + "");
            if (valideTime - curTime < 0) {
                Toast.makeText(this, "账户代理期限已过期", Toast.LENGTH_SHORT).show();
                SharedpreferenceUtil.saveData(this, "hasLogin", false);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    /**
     * 启动单次客户端定位
     */
    void startSingleLocation(){
        if(null == locationClientSingle){
            locationClientSingle = new AMapLocationClient(this.getApplicationContext());
        }

        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        //使用单次定位
        locationClientOption.setOnceLocation(true);
        // 地址信息
        locationClientOption.setNeedAddress(true);
        locationClientOption.setLocationCacheEnable(false);
        locationClientSingle.setLocationOption(locationClientOption);
        locationClientSingle.setLocationListener(locationSingleListener);
        locationClientSingle.startLocation();
    }

    /**
     * 停止单次客户端
     */
    void stopSingleLocation(){
        if(null != locationClientSingle){
            locationClientSingle.stopLocation();
        }
    }

    /**
     * 单次客户端的定位监听
     */
    AMapLocationListener locationSingleListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {

            if(null == location){
                Log.e("Position", "定位失败");
            } else {
                //解析定位结果
//                String result = LocationUtils.getLocStr(location);
//                Log.e("Position", result);
                province = LocationUtils.getLocProvince(location);
                city = LocationUtils.getLocCity(location);
                district = LocationUtils.getLocDistrict(location);
                Log.e("POSID", "定位id：" + LocationUtils.getLocationStr(location));
                checkPos();
            }
        }
    };

    /**
     * 处理自定义Gallery相关
     */
    private void initFancyCoverFlow() {
        filmList = FilmInfoTest.getfilmInfo();
        adapter = new ImageAdapter(this, filmList);
        fancyCoverFlow = (FancyCoverFlow) findViewById(R.id.fancyCoverFlow);
        // item之间的间隙可以近似认为是imageview的宽度与缩放比例的乘积的一半
        fancyCoverFlow.setSpacing(-150);
        fancyCoverFlow.setAdapter(adapter);
        fancyCoverFlow.setSelection(1002);
//		fancyCoverFlow.setActionDistance(10);
        fancyCoverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                cur_index = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 点击事件
        fancyCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                switch (filmList.get(position % filmList.size()).getRs()) {
                    case R.mipmap.jichu_icon:
                        Intent intent = new Intent(NewHomeActivity.this, BasicClassActivity.class);
                        startActivity(intent);
                        break;
                    case R.mipmap.tese_icon:
                        List<Soulplates> soulplateList = (List<Soulplates>) SharedpreferenceUtil.queryObj2Sp(NewHomeActivity.this, "soulplatelist");
                        Intent intent2 = new Intent(NewHomeActivity.this, FeatureRcActivity.class);
                        intent2.putExtra("soulplate_list", (Serializable) soulplateList);
                        startActivity(intent2);
                        break;
                    case R.mipmap.gebie_icon:
                        Intent intent3 = new Intent(NewHomeActivity.this, ExampleActivity.class);
                        intent3.putExtra("url", "http://vip.ls928.com/v.jsp");
                        startActivity(intent3);
                        break;
                    case R.mipmap.me_icon:
                        Intent intent4 = new Intent(NewHomeActivity.this, MineNewRcActivity.class);
                        startActivity(intent4);
                        break;
                    case R.mipmap.town_jichu:
                        Intent intent5 = new Intent(NewHomeActivity.this, TownClassActivity.class);
                        startActivity(intent5);
                        break;
                }
            }
        });
    }


    /**
     * 处理顶部Editext相关
     * */
    private void initEditText() {

        editText = (EditText) findViewById(R.id.search_gallery);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setOnEditorActionListener(mOnEditorActionListener);
        Drawable drawable = getResources().getDrawable(R.drawable.search);
        assert drawable != null;
        drawable.setBounds(0,0,32,32);
        editText.setCompoundDrawables(null, null, drawable, null);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.openKeyBoard(editText, NewHomeActivity.this);
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    KeyBoardUtils.closeKeyBoard(editText, NewHomeActivity.this);
                }
            }
        });

    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            Log.d("ACTIONID", "--->" + actionId);
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String str_search = editText.getText().toString();
                if (!TextUtils.isEmpty(str_search)) {
                    mSearchPresenter.searchCourse(editText.getText().toString(), macAdress, 0, 60);
                }
                return true;
            }
            return false;
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(null != locationClientSingle){
            locationClientSingle.onDestroy();
            locationClientSingle = null;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            NewHomeActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onStop() {
        super.onStop();
    }



    //版本号
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
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

    private void getIP() {
        FindIPUtils utils = new FindIPUtils();
        utils.catchIP(new FindIPUtils.IPListener() {
            @Override
            public void findIP(String ip) {
                myIP = ip;
            }
        });
    }


    @Override
    public void getFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void userAddress(String pos) {
        Log.e("location", "pos->" + pos);
        Message message = Message.obtain();
        message.obj = pos;
        message.what = 1;
        mHandler.sendMessage(message);
        checkTime();
    }

    @Override
    public void searchSucc(List<CoursesBean> clist) {
        Intent intent = new Intent(NewHomeActivity.this, SearchActivity.class);
        intent.putExtra("courseList", (Serializable) clist);
        NewHomeActivity.this.startActivity(intent);
    }

    @Override
    public void searchFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getUrl(String url) {

    }
}

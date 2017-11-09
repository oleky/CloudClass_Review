package com.whx.ott.ui;

/**
 * Created by HelloWorld on 2016/8/27.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.whx.ott.R;
import com.whx.ott.adapter.GeneralAdapter;
import com.whx.ott.adapter.HomeAdapter;
import com.whx.ott.adapter.LeftMenuPresenter;
import com.whx.ott.adapter.TopspeedEngAdapter;
import com.whx.ott.bean.ParseTopspeedEng;
import com.whx.ott.bean.ResultBean;
import com.whx.ott.bean.Soulplates;
import com.whx.ott.bean.SubjectsBean;
import com.whx.ott.bean.TestUrl;
import com.whx.ott.beanfeature.ParseFeature;
import com.whx.ott.beanfeature.SoulcoursesBean;
import com.whx.ott.bridge.RecyclerViewBridge;
import com.whx.ott.conn.Conn;
import com.whx.ott.db.DBManager;
import com.whx.ott.widget.GridLayoutManagerTV;
import com.whx.ott.widget.MainUpView;
import com.whx.ott.widget.RecyclerViewTV;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.crypto.spec.PSource;
import javax.security.auth.login.LoginException;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * setSelectedItemAtCentered 设置一直在中间. (如果设置 false，那么请使用setSelectedItemOffset来设置相差的边距)
 */
public class FeatureRcActivity extends Activity implements RecyclerViewTV.OnItemListener {

    private Context mContext;
    private RecyclerViewTV top_menu_rv;
    private RecyclerViewTV mRecyclerView;
    private MainUpView mainUpView1;
    private RecyclerViewBridge mRecyclerViewBridge;
    private View oldView;
    private List<String> tvs = new ArrayList<String>(); //科目集合
    private List<Integer> ivs = new ArrayList<Integer>();//科目背景图片集合
    private static List<String> names = new ArrayList<String>();//标题集合
    private String name;
    private TextView title_name_tv;
    private View all;
    private ParseFeature parseFeature;
    private List<SoulcoursesBean> soulcoursesBeanList;
    private List<Soulplates> soulplates;
    private int Key;
    public static int soulNum;
    private String URL = Conn.BASEURL + Conn.GET_FEATURE;
    private String Soulplate_id = "soulplate_id";
    private String Subject_id = "subject_id";
    private HashMap<String, Integer> sub_pos;   //存放点击位置
    private List<List<SoulcoursesBean>> mLists = new ArrayList<>(); //存放接口返回值数据
    private int subpostion;
    private DBManager manager = new DBManager(this);

    private static String TAG = "FeatureRcActivity";
    private ParseTopspeedEng parseTopspeedEng;
    private List<ResultBean> resultBeanLists;
    private ResultBean resultBean;
    private String ENG_BASE = "http://114.215.66.250/whx/course/clickNumber?";
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_rc);
        initView();
        initTopMenu();
        initData();
    }

    private void initView() {
        mContext = FeatureRcActivity.this;
        top_menu_rv = (RecyclerViewTV) findViewById(R.id.top_menu_rv);
        mRecyclerView = (RecyclerViewTV) findViewById(R.id.content_feature_rv);
        mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
        mainUpView1.setEffectBridge(new RecyclerViewBridge());
        title_name_tv = (TextView) findViewById(R.id.feature_rv_tv);
        // 注意这里，需要使用 RecyclerViewBridge 的移动边框 Bridge.
        mRecyclerViewBridge = (RecyclerViewBridge) mainUpView1.getEffectBridge();
//        all.setNextFocusDownId();
        //获得上一级所传ID
        soulplates = (List<Soulplates>) getIntent().getExtras().getSerializable("soulplate_list");

    }

    //添加顶部标题
    private void initTopMenu() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        top_menu_rv.setLayoutManager(layoutManager);
        top_menu_rv.setFocusable(false);
        GeneralAdapter generalAdapter = new GeneralAdapter(new LeftMenuPresenter(names));
        top_menu_rv.setAdapter(generalAdapter);
        generalAdapter.notifyDataSetChanged();
        top_menu_rv.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                // 传入 itemView也可以, 自己保存的 oldView也可以.
                mRecyclerViewBridge.setUnFocusView(itemView);
                name = names.get(position);
                title_name_tv.setText(names.get(position));
                //获得所点击位置上控件的ID
                soulNum = soulplates.get(position).getId();

            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.1f);
                oldView = itemView;

                name = names.get(position);
                title_name_tv.setText(names.get(position));
                //获得所点击位置上控件的ID
                soulNum = soulplates.get(position).getId();
                if (soulNum == 3) {
                    type = 1 + "";
                }
                if (soulNum == 4) {
                    type = 2 + "";
                }
                onViewItemClick(itemView, position);
            }


            /**
             * 这里是调整开头和结尾的移动边框.
             */
            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.1f);
                oldView = itemView;
            }
        });
        top_menu_rv.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.1f);
                oldView = itemView;

                name = names.get(position);
                title_name_tv.setText(names.get(position));
                //获得所点击位置上控件的ID
                soulNum = soulplates.get(position).getId();
                Log.e("TAG", "onItemClick: " + soulNum);
                if (soulNum == 3) {
                    type = 1 + "";
                }
                if (soulNum == 4) {
                    type = 2 + "";
                }
                onViewItemClick(itemView, position);
            }
        });
        //设置默认选中
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                top_menu_rv.requestFocusFromTouch();
                if (top_menu_rv.getChildCount() > 0) {
                    top_menu_rv.getChildAt(0).callOnClick();
                }
            }
        };
        handler.sendMessageDelayed(handler.obtainMessage(), 188);
    }


    //标题选择
    private void onViewItemClick(View v, int pos) {

        if (pos == 3) {
            type = 1 + "";
            MyRcGlDataEng(GridLayoutManager.VERTICAL);
        } else if (pos == 4) {
            type = 2 + "";
            MyRcGlDataEng(GridLayoutManager.VERTICAL);
        } else {
            MyRcGlData(GridLayoutManager.VERTICAL);
        }
    }

    private void MyRcGlDataEng(int orientation) {
        GridLayoutManagerTV gridLayoutManagerEng = new GridLayoutManagerTV(this, 3);
        gridLayoutManagerEng.setOrientation(orientation);
        mRecyclerView.setLayoutManager(gridLayoutManagerEng);
        mRecyclerView.setFocusable(false);
        updateUIEng(0, 100);

        mRecyclerView.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setUnFocusView(itemView);

            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.2f);
                oldView = itemView;
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.2f);
                oldView = itemView;
            }
        });
        mRecyclerView.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.2f);
                oldView = itemView;
                onViewItemClickEng(itemView, position);
                subpostion = position;
            }
        });

    }

    private void onViewItemClickEng(View v, int pos) {
//        getUrl(type, (pos + 1) + "");
        updateUIEng(0, 100, type, pos + 1);

    }

    //添加gridlayout
    private void MyRcGlData(int orientation) {
        GridLayoutManagerTV gridlayoutManager = new GridLayoutManagerTV(this, 3); // 解决快速长按焦点丢失问题.
        gridlayoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(gridlayoutManager);
        mRecyclerView.setFocusable(false);
        HomeAdapter homeAdapter = new HomeAdapter(this, tvs, ivs);
        mRecyclerView.setAdapter(homeAdapter);
        mRecyclerView.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                // 传入 itemView也可以, 自己保存的 oldView也可以.
                mRecyclerViewBridge.setUnFocusView(itemView);

            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.2f);
                oldView = itemView;
            }

            /**
             * 这里是调整开头和结尾的移动边框.
             */
            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.2f);
                oldView = itemView;
            }
        });
        //Gridview内容选择进行跳转
        mRecyclerView.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.2f);
                oldView = itemView;
                onViewItemClick1(itemView, position);
                subpostion = position;
            }
        });
    }

    private void onViewItemClick1(View v, int pos) {
        sub_pos = new HashMap<String, Integer>();
        sub_pos.put("subject_id", pos + 1);
        updateUI2(0, 100, sub_pos);
    }

    @Override
    public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
    }

    @Override
    public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
        mRecyclerViewBridge.setFocusView(itemView, oldView, 1.2f);
        oldView = itemView;
    }

    @Override
    public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
    }

    private void initData() {

        List<SubjectsBean> subjectsBeanList = manager.getSubjectList();
        for (int i = 0; i < subjectsBeanList.size(); i++) {
            tvs.add(subjectsBeanList.get(i).getSubject_name());
        }
        ivs.add(R.drawable.rechina);
        ivs.add(R.drawable.remath);
        ivs.add(R.drawable.reeng);
        ivs.add(R.drawable.rephy);
        ivs.add(R.drawable.rechemis);
        ivs.add(R.drawable.reblo);
        ivs.add(R.drawable.wenkeshuxue);
        ivs.add(R.drawable.lishi);
        ivs.add(R.drawable.dili);
        ivs.add(R.drawable.zhengzhi);

        names.clear();
        for (int i = 0; i < soulplates.size(); i++) {
            String name = soulplates.get(i).getSoulplate_name();
            names.add(name);
        }
    }

    /**
     * 点击item加载最里面数据
     *
     * @param start
     * @param end
     */
    private void updateUI2(final int start, int end, final Map<String, Integer> pos) {

        OkHttpUtils.get()
                .url(URL)
                .addParams("start", start + "")
                .addParams("end", end + "")
                .addParams(Soulplate_id, soulNum + "")
                .addParams(Subject_id, sub_pos.get("subject_id").toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(FeatureRcActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        parseFeature = ParseFeature.getFeatureData(response);
                        if (null != parseFeature) {
                            soulcoursesBeanList = parseFeature.getSoulcourses();
                        }
                        if (mLists != null) {
                            mLists.clear();
                        }
                        mLists.add(soulcoursesBeanList);
                        Key = parseFeature.getCode();
                        if (mLists.get(0).size() != 0) {
                            if (Key == 0) {
                                //根据所选标题
                                Intent intent = new Intent(FeatureRcActivity.this, VideoDataActivity.class);
                                intent.putExtra("mLists", (Serializable) mLists);
                                intent.putExtra("name", tvs.get(subpostion));
//                                intent.putExtra("image", ivs.get(subpostion));
                                startActivity(intent);
                            } else {
                                Toast.makeText(FeatureRcActivity.this, "用户没有权限访问", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(FeatureRcActivity.this, "目前没有开放此课程", Toast.LENGTH_SHORT).show();
                        }

//                        Log.e("TAG", "初始化后需要集合数据长度: " + mLists.size());
//                        Log.e("TAG", "数据内容: " + response);
                    }
                });

    }

    private void updateUIEng(final int start, int end) {

        OkHttpUtils.get()
                .url(URL)
                .addParams("start", start + "")
                .addParams("end", end + "")
                .addParams(Soulplate_id, soulNum + "")
                .addParams(Subject_id, 3 + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(FeatureRcActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        parseFeature = ParseFeature.getFeatureData(response);
                        if (null != parseFeature) {
                            soulcoursesBeanList = parseFeature.getSoulcourses();
                        }
                        TopspeedEngAdapter topspeedEngAdapter = new TopspeedEngAdapter(FeatureRcActivity.this, soulcoursesBeanList, ivs);
                        mRecyclerView.setAdapter(topspeedEngAdapter);
                    }
                });

    }


    private void updateUIEng(final int start, int end, final String type, final int lecture) {

        OkHttpUtils.get()
                .url(URL)
                .addParams("start", start + "")
                .addParams("end", end + "")
                .addParams(Soulplate_id, soulNum + "")
                .addParams(Subject_id, 3 + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(FeatureRcActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        parseFeature = ParseFeature.getFeatureData(response);
                        Log.e(TAG, "onResponse: " + parseFeature.getSoulcourses());
                        if (null != parseFeature) {
                            soulcoursesBeanList = parseFeature.getSoulcourses();
                        }
                        if (mLists != null) {
                            mLists.clear();
                        }
                        mLists.add(soulcoursesBeanList);
                        SoulcoursesBean bean = soulcoursesBeanList.get(lecture - 1);
                        Log.e(TAG, "mlists: " + mLists.size());

                        Key = parseFeature.getCode();
                        if (mLists.get(0).size() != 0) {
                            if (Key == 0) {

                                getUrl(type, lecture + "", bean);
                            } else {
                                Toast.makeText(FeatureRcActivity.this, "用户没有权限访问", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(FeatureRcActivity.this, "目前没有开放此课程", Toast.LENGTH_SHORT).show();
                        }

//                        Log.e("TAG", "初始化后需要集合数据长度: " + mLists.size());
//                        Log.e("TAG", "数据内容: " + response);
                    }
                });

    }


    private void getUrl(String type, String lecture, final SoulcoursesBean bean) {
        OkHttpUtils.get()
                .url(Conn.ENG_BASE)
                .addParams("type", type)
                .addParams("lecture", lecture)
                .addParams("token", "whxqisutoken888888")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(FeatureRcActivity.this, "视频地址已过期", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        resultBean = ParseTopspeedEng.parser(response);
                        Intent intent = new Intent(FeatureRcActivity.this, CCPlayerActivity.class);
                        Bundle bundle = new Bundle();
                        String videoPath = resultBean.getForwardUrl();
                        String status = resultBean.getStatus();
                        bundle.putSerializable("soulcoursesBean", bean);
                        intent.putExtra("videoPath", videoPath);
                        intent.putExtra("model_id", 2);
                        intent.putExtra("type", "cloud");
                        intent.putExtra("status", status);
                        Log.e("path", "---------" + videoPath);
                        intent.putExtras(bundle);
                        FeatureRcActivity.this.startActivity(intent);
                    }
                });
    }


}

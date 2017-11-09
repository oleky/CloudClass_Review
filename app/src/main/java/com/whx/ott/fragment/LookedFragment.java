package com.whx.ott.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.whx.ott.R;
import com.whx.ott.adapter.MineRcAdapter;
import com.whx.ott.beanfeature.BasisedBean;
import com.whx.ott.beanfeature.MineFeatureBean;
import com.whx.ott.beanfeature.ParseMineBasised;
import com.whx.ott.beanfeature.ParseMineFeature;
import com.whx.ott.bridge.RecyclerViewBridge;
import com.whx.ott.conn.Conn;
import com.whx.ott.util.SharedpreferenceUtil;
import com.whx.ott.widget.MainUpView;
import com.whx.ott.widget.RecyclerViewTV;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by HelloWorld on 2016/8/17.
 */
public class LookedFragment extends Fragment {
    private RecyclerViewTV basic_rc;
    private RecyclerViewTV feature_rc;
    private RecyclerViewTV feature_frag_rc_country;
    private MainUpView mainUpView1;
    private RecyclerViewBridge mRecyclerViewBridge;
    private List<String> names_basecountry;
    private List<String> time_basecountry;
    private List<String> subject_name_base_country;
    private List<String> grade_name_base_country;
    private List<String> names_basise;
    private List<String> time_basise;
    private List<String> subject_name_basise;
    private List<String> Grade_name_basise;
    private List<String> names_feature;
    private List<String> time_feature;
    private List<String> subject_name_feature;
    private List<Integer> ivs = new ArrayList<Integer>();//科目背景图片集合
    public static final String START = "start";
    public static final String END = "end";
    private View oldView;
    private ParseMineBasised parseMineBasised;
    private ParseMineFeature parseMineFeature;
    private List<BasisedBean> basisedBeanList;
    private List<MineFeatureBean> mineFeatureBeanList;
    private List<BasisedBean> countrybasisedBeanList;
    private int FeatureKey;
    private int BasicKey;
    private String BASIAURL = Conn.BASEURL + Conn.LOOKED_BASIC;
    private String FEATUREURL = Conn.BASEURL + Conn.LOOKED_FEATURE;
    private String user_id;
    private String feature_user_id;
    private String macAdress;
    private String TAG = LookedFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_looked, null);
        initView(view);
        updateUI();
        updateUI1();
        upCountrydateUI();
        initData();
        initBasicRc();
        initFeatureRc();
        initCountryBasicRc();
        return view;
    }

    private void initView(View view) {
        basic_rc = (RecyclerViewTV) view.findViewById(R.id.basic_frag_rc);
        feature_rc = (RecyclerViewTV) view.findViewById(R.id.feature_frag_rc);
        feature_frag_rc_country = (RecyclerViewTV) view.findViewById(R.id.feature_frag_rc_country);
        mainUpView1 = (MainUpView) view.findViewById(R.id.mainUpView1);
        mainUpView1.setEffectBridge(new RecyclerViewBridge());

        mRecyclerViewBridge = (RecyclerViewBridge) mainUpView1.getEffectBridge();
        macAdress = (String) SharedpreferenceUtil.getData(getActivity(), "dev_id", "");
    }

    private void initData() {
        names_basise = new ArrayList<String>();
        names_feature = new ArrayList<String>();
        time_basise = new ArrayList<String>();
        time_feature = new ArrayList<String>();
        subject_name_basise = new ArrayList<String>();
        Grade_name_basise = new ArrayList<String>();
        subject_name_feature = new ArrayList<String>();
        names_basecountry = new ArrayList<>();
        time_basecountry = new ArrayList<>();
        grade_name_base_country = new ArrayList<>();
        subject_name_base_country = new ArrayList<>();

        ivs.add(R.drawable.rectmath);
        ivs.add(R.drawable.rectchinese);
        ivs.add(R.drawable.recteng);
        ivs.add(R.drawable.rectchemistry);
        ivs.add(R.drawable.rectphysics);
        ivs.add(R.drawable.biology);
    }

    private void initBasicRc() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        basic_rc.setLayoutManager(layoutManager);
        basic_rc.setFocusable(false);
        MineRcAdapter adapter = new MineRcAdapter(getActivity(), names_basise, ivs, time_basise, subject_name_basise, Grade_name_basise);
        basic_rc.setAdapter(adapter);
        basic_rc.setOnItemListener(new RecyclerViewTV.OnItemListener() {
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
    }

    //乡镇云教室基础课
    private void initCountryBasicRc() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        feature_frag_rc_country.setLayoutManager(layoutManager);
        feature_frag_rc_country.setFocusable(false);
        MineRcAdapter adapter = new MineRcAdapter(getActivity(), names_basecountry, ivs, time_basecountry, subject_name_base_country, grade_name_base_country);
        feature_frag_rc_country.setAdapter(adapter);
        feature_frag_rc_country.setOnItemListener(new RecyclerViewTV.OnItemListener() {
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
    }

    private void initFeatureRc() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        feature_rc.setLayoutManager(layoutManager);
        feature_rc.setFocusable(false);
        MineRcAdapter adapter = new MineRcAdapter(getActivity(), names_feature, ivs, time_feature, subject_name_feature);
        feature_rc.setAdapter(adapter);
        feature_rc.setOnItemListener(new RecyclerViewTV.OnItemListener() {
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
    }

    private void updateUI() {
        user_id = (String) SharedpreferenceUtil.getData(getActivity(), "user_id", "");
        OkHttpUtils.get()
                .url(BASIAURL)
                .addParams(START, 0 + "")
                .addParams(END, 99 + "")
                .addParams("user_id", user_id)
                .addParams("devid", macAdress)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        parseMineBasised = ParseMineBasised.getBasisData(response);

                        if (null != parseMineBasised) {
                            basisedBeanList = parseMineBasised.getData();
                        }
                        BasicKey = parseMineBasised.getCode();
                        if (BasicKey == 0 && basisedBeanList != null) {
                            for (int i = 0; i < basisedBeanList.size(); i++) {
                                names_basise.add(basisedBeanList.get(i).getCourse_name());
                                time_basise.add(basisedBeanList.get(i).getCreate_time());
                                subject_name_basise.add(basisedBeanList.get(i).getSubject_name());
                                Grade_name_basise.add(basisedBeanList.get(i).getGrade_name());
                            }
                        } else {
                            Toast.makeText(getActivity(), parseMineBasised.getMeg(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void upCountrydateUI() {
        user_id = (String) SharedpreferenceUtil.getData(getActivity(), "user_id", "");
        OkHttpUtils.get()
                .url(Conn.BASEURL + Conn.COUNTRY_SHIP)
                .addParams(START, 0 + "")
                .addParams(END, 99 + "")
                .addParams("user_id", user_id)
                .addParams("devid", macAdress)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        parseMineBasised = ParseMineBasised.getBasisData(response);

                        if (null != parseMineBasised) {
                            countrybasisedBeanList = parseMineBasised.getData();
                        }
                        int countrycode = parseMineBasised.getCode();

                        if (countrycode == 0 && countrybasisedBeanList != null) {
                            for (int i = 0; i < countrybasisedBeanList.size(); i++) {
                                names_basecountry.add(countrybasisedBeanList.get(i).getCourse_name());
                                time_basecountry.add(countrybasisedBeanList.get(i).getCreate_time());
                                subject_name_base_country.add(countrybasisedBeanList.get(i).getSubject_name());
                                grade_name_base_country.add(countrybasisedBeanList.get(i).getGrade_name());
                            }
                            Log.e(TAG, "time_basise: " + time_basecountry);
                        } else {
                            Toast.makeText(getActivity(), parseMineBasised.getMeg(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void updateUI1() {
        feature_user_id = (String) SharedpreferenceUtil.getData(getActivity(), "user_id", "");
        OkHttpUtils.get()
                .url(FEATUREURL)
                .addParams(START, 0 + "")
                .addParams(END, 99 + "")
                .addParams("user_id", feature_user_id)
                .addParams("devid", macAdress)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "请检查特色课网络", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        parseMineFeature = ParseMineFeature.getParseData(response);
                        Log.e(TAG, "onResponseParse: " + parseMineFeature);

                        if (null != parseMineFeature) {
                            mineFeatureBeanList = parseMineFeature.getData();
                            Log.e(TAG, "onResponsemine: " + mineFeatureBeanList);
                        }
                        FeatureKey = parseMineFeature.getCode();
                        Log.e(TAG, "onResponse222222: " + FeatureKey);
                        if (FeatureKey == 0 & mineFeatureBeanList != null) {
                            for (int i = 0; i < mineFeatureBeanList.size(); i++) {
                                names_feature.add(mineFeatureBeanList.get(i).getCourse_name());
                                time_feature.add(mineFeatureBeanList.get(i).getCreate_time());
                                subject_name_feature.add(mineFeatureBeanList.get(i).getSubject_name());
                            }
                        } else {
                            Toast.makeText(getActivity(), parseMineFeature.getMeg(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}



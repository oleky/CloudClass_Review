package com.whx.ott.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whx.ott.R;
import com.whx.ott.adapter.LookedTitleAdapter;
import com.whx.ott.adapter.NewLookedRecordAdapter;
import com.whx.ott.beanfeature.BaseClassBean;
import com.whx.ott.beanfeature.BasisedBean;
import com.whx.ott.beanfeature.FeatureClassBean;
import com.whx.ott.beanfeature.MineFeatureBean;
import com.whx.ott.beanfeature.ParseMineBaseClassed;
import com.whx.ott.beanfeature.ParseMineBasised;
import com.whx.ott.beanfeature.ParseMineFeature;
import com.whx.ott.beanfeature.ParseMineFeaturnClassed;
import com.whx.ott.bridge.RecyclerViewBridge;
import com.whx.ott.conn.Conn;
import com.whx.ott.util.SharedpreferenceUtil;
import com.whx.ott.widget.MainUpView;
import com.whx.ott.widget.RecyclerViewTV;
import com.whx.ott.widget.SpacesItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by HelloWorld on 2017/11/10.
 */

public class NewLookedFragment extends Fragment implements RecyclerViewTV.OnItemListener {
    private RecyclerViewTV rv_title;
    private MainUpView mainUpView;
    private RecyclerViewBridge mRecyclerViewBridge;
    private List<String> classKinds; // 课程类型
    private View oldView;
    private String TAG = NewLookedFragment.class.getSimpleName();
    private RecyclerViewTV rv_record;
    private List<String> names_basecountry;
    private List<String> time_basecountry;
    private List<String> subject_name_base_country;
    private List<String> grade_name_base_country;
    private List<String> names_basise;
    private List<String> time_basise;
    private List<String> subject_name_basise;
    private List<String> Grade_name_basise;
    private List<String> names_feature, name_feature_country;
    private List<String> time_feature, time_feature_country;
    private List<String> subject_name_feature, subject_name_feature_country;
    private List<String> model_feature, grade_feature;
    private List<BaseClassBean> baseClassBeanList;
    private List<FeatureClassBean> featureClassBeanList;
    private String user_id, stu_id;
    private List<FeatureClassBean> featureClassBeans;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newlooked, null);
        initView(view);
        initData();
        updateUI();
        updateUIFeature();
        titleView();
        baseRecordView();
        return view;
    }

    private void initView(View view) {
        rv_title = view.findViewById(R.id.rv_title);
        rv_record = view.findViewById(R.id.rv_record_newlooked);
        mainUpView = view.findViewById(R.id.mainUpView1);
        mainUpView.setEffectBridge(new RecyclerViewBridge());
        mRecyclerViewBridge = (RecyclerViewBridge) mainUpView.getEffectBridge();
//        tv_null = (TextView) view.findViewById(R.id.tv_null);
    }

    private void initData() {

        classKinds = new ArrayList<String>();
        classKinds.add("基础课");
        classKinds.add("特色课");
        classKinds.add("小初课");
        classKinds.add("特色课(小初)");
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
        featureClassBeans = new ArrayList<>();
        model_feature = new ArrayList<>();
        time_feature_country = new ArrayList<>();
        name_feature_country = new ArrayList<>();
        grade_feature = new ArrayList<>();
//        macAdress = (String) SharedpreferenceUtil.getData(getActivity(), "dev_id", "");
    }

    private void onViewItem(View itemView, RecyclerViewTV parent) {
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.white));
            }
        }
        if (itemView instanceof TextView) {
            ((TextView) itemView).setTextColor(getResources().getColor(R.color.new_feature));
        }
    }


    private void onViewItemClick(int position) {
        switch (position) {
            case 0:
                baseRecordView();
                break;
            case 1:
                featureRecordView();
                break;
            case 2:
                countryRecordView();
                break;
            case 3:
                countryfeatureRecordView();
                break;
            default:
                break;
        }
    }

    private void titleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_title.setLayoutManager(layoutManager);
        rv_title.setFocusable(false);
        Log.e(TAG, "titleView: " + classKinds);
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(getActivity(), SpacesItemDecoration.HORIZONTAL_LIST);
        spacesItemDecoration.setDivider(R.drawable.linebetween);
        LookedTitleAdapter titleAdapter = new LookedTitleAdapter(getActivity(), classKinds);
        rv_title.setAdapter(titleAdapter);
        titleAdapter.notifyDataSetChanged();

        rv_title.addItemDecoration(spacesItemDecoration);
        rv_title.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setUnFocusView(itemView);
//                onViewItemClick(position);

            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setFocusView(itemView, 1.1f);
                oldView = itemView;
                onViewItemClick(position);
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
//                onViewItem(itemView, parent);
                mRecyclerViewBridge.setFocusView(itemView, 1.1f);
                oldView = itemView;

//                onViewItemClick(position);
            }
        });
        rv_title.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.1f);
                oldView = itemView;
                onViewItemClick(position);
            }
        });
//        Handler handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                rv_title.requestFocusFromTouch();
//                if (rv_title.getChildCount() > 0) {
//                    rv_title.getChildAt(0).callOnClick();
//                }
//            }
//        };
//        handler.sendMessageDelayed(handler.obtainMessage(), 188);
    }

    private void baseRecordView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_record.setLayoutManager(layoutManager);
        rv_record.setFocusable(false);
        NewLookedRecordAdapter recordAdapter = new NewLookedRecordAdapter(time_basise, names_basise, Grade_name_basise, getActivity());
        rv_record.setAdapter(recordAdapter);
        recordAdapter.notifyDataSetChanged();
        rv_record.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {

                mRecyclerViewBridge.setUnFocusView(itemView);
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;

            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;

            }
        });

    }

    private void featureRecordView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_record.setLayoutManager(layoutManager);
        rv_record.setFocusable(false);
        NewLookedRecordAdapter recordAdapter = new NewLookedRecordAdapter(time_feature, names_feature, model_feature, getActivity());
//        NewLookedRecordAdapter recordAdapter = new NewLookedRecordAdapter(featureClassBeans, getActivity());
        rv_record.setAdapter(recordAdapter);
        recordAdapter.notifyDataSetChanged();
        rv_record.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setUnFocusView(itemView);
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
            }
        });

    }

    private void countryfeatureRecordView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_record.setLayoutManager(layoutManager);
        rv_record.setFocusable(false);
        NewLookedRecordAdapter recordAdapter = new NewLookedRecordAdapter(time_feature_country, name_feature_country, grade_feature, getActivity());
//        NewLookedRecordAdapter recordAdapter = new NewLookedRecordAdapter(featureClassBeans, getActivity());
        rv_record.setAdapter(recordAdapter);
        recordAdapter.notifyDataSetChanged();
        rv_record.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setUnFocusView(itemView);
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
            }
        });

    }

    private void countryRecordView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_record.setLayoutManager(layoutManager);
        rv_record.setFocusable(false);
        final NewLookedRecordAdapter recordAdapter = new NewLookedRecordAdapter(time_basecountry, names_basecountry, grade_name_base_country, getActivity());
        rv_record.setAdapter(recordAdapter);
        recordAdapter.notifyDataSetChanged();
        rv_record.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setUnFocusView(itemView);
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
            }
        });

    }

    private void updateUI() {
        user_id = (String) SharedpreferenceUtil.getData(getActivity(), "agent_id", "");
        stu_id = (String) SharedpreferenceUtil.getData(getActivity(), "user_id", "");
        OkHttpUtils.post()
                .url(Conn.BASEURL + Conn.BASE_CLASSED_RECORD)
                .addParams("user_id", user_id)
                .addParams("stu_id", stu_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ParseMineBaseClassed parseMineBaseClassed = ParseMineBaseClassed.parse(response);
                        int code = parseMineBaseClassed.getCode();
                        baseClassBeanList = parseMineBaseClassed.getData();
                        if (code == 0 && baseClassBeanList != null) {
                            for (int i = 0; i < baseClassBeanList.size(); i++) {
                                String type = baseClassBeanList.get(i).getType_name();
                                if (type.equals("云教室")) {
                                    names_basise.add(baseClassBeanList.get(i).getCourse_name());
                                    time_basise.add(baseClassBeanList.get(i).getCreate_time());
                                    subject_name_basise.add(baseClassBeanList.get(i).getSubject_name());
                                    Grade_name_basise.add(baseClassBeanList.get(i).getGrade_name());
                                } else if (type.equals("雄博士")) {
                                    names_basecountry.add(baseClassBeanList.get(i).getCourse_name());
                                    time_basecountry.add(baseClassBeanList.get(i).getCreate_time());
                                    subject_name_base_country.add(baseClassBeanList.get(i).getSubject_name());
                                    grade_name_base_country.add(baseClassBeanList.get(i).getGrade_name());
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), parseMineBaseClassed.getMeg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void updateUIFeature() {
        OkHttpUtils.post()
                .url(Conn.BASEURL + Conn.FEATURE_CLASSED_RECORD)
                .addParams("user_id", user_id)
                .addParams("stu_id", stu_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "请检查特色课网络", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ParseMineFeaturnClassed parseMineFeaturnClassed = ParseMineFeaturnClassed.parse(response);
                        int code = parseMineFeaturnClassed.getCode();
                        featureClassBeanList = parseMineFeaturnClassed.getData();
                        if (code == 0 && featureClassBeanList != null) {
                            for (int i = 0; i < featureClassBeanList.size(); i++) {
                                String type = featureClassBeanList.get(i).getType_name();
                                if (type.equals("云教室")) {
                                    names_feature.add(featureClassBeanList.get(i).getCourse_name());
                                    time_feature.add(featureClassBeanList.get(i).getCreate_time());
                                    subject_name_feature.add(featureClassBeanList.get(i).getSubject_name());
                                    model_feature.add(featureClassBeanList.get(i).getModel_name());
                                } else if (type.equals("雄博士")) {
                                    name_feature_country.add(featureClassBeanList.get(i).getCourse_name());
                                    time_feature_country.add(featureClassBeanList.get(i).getCreate_time());
                                    grade_feature.add(featureClassBeanList.get(i).getGrade_name());
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), parseMineFeaturnClassed.getMeg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {

    }

    @Override
    public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {

    }

    @Override
    public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {

    }
}

package com.whx.ott.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hjoleky.stucloud.bean.DictionaryBean;
import com.whx.ott.R;
import com.whx.ott.adapter.NewAllClassFeatureTitleAdapter;
import com.whx.ott.bean.AllClassBean;
import com.whx.ott.bean.ParseDictionary;
import com.whx.ott.bean.ParseStuJurisdiction;
import com.whx.ott.bean.StuJurisdictionBean;
import com.whx.ott.conn.Conn;
import com.whx.ott.util.SharedpreferenceUtil;
import com.whx.ott.widget.FullyGridLayoutManager;
import com.whx.ott.widget.FullyLinearLayoutManager;
import com.whx.ott.widget.RecyclerViewTV;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by HelloWorld on 2017/12/5.
 */

public class NewAllClassFragmentThree extends Fragment {
    private RecyclerViewTV rv_content, rv_title;
    private List<String> titleNames;
    private List<DictionaryBean> dictionaryBeanList;
    private List<AllClassBean> allClassBeanList;
    private String TAG = NewAllClassFragmentThree.class.getSimpleName();
    private List<String> contentLists;
    private String user_id, stu_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allclass_three, null);
        initView(view);
        initData();
        titleView();
        contentView();
        getDictionaryData();
        return view;
    }

    private void initView(View view) {
        rv_title = view.findViewById(R.id.rv_title);
        rv_content = view.findViewById(R.id.rv_content);
    }

    private void initData() {
        titleNames = new ArrayList<>();

        dictionaryBeanList = new ArrayList<>();
        allClassBeanList = new ArrayList<>();
        contentLists = new ArrayList<>();
        titleNames.add("课程阶段");
        titleNames.add("课程类型");
        titleNames.add("年份");
        titleNames.add("已购");
        titleNames.add("剩余");
        stu_id = (String) SharedpreferenceUtil.getData(getActivity(), "user_id", "");
        user_id = (String) SharedpreferenceUtil.getData(getActivity(), "agent_id", "");
    }

    private void titleView() {
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_title.setLayoutManager(layoutManager);
        NewAllClassFeatureTitleAdapter adapter = new NewAllClassFeatureTitleAdapter(getActivity(), titleNames, R.color.yellow);
        rv_title.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void contentView() {
        FullyGridLayoutManager layoutManager = new FullyGridLayoutManager(getActivity(), 5);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_content.setLayoutManager(layoutManager);
//        NewAllClassFeatureTitleAdapter adapter = new NewAllClassFeatureTitleAdapter(getActivity(), titleNames);
//        rv_content.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }


    private void getStuJurisdictionData() {
        OkHttpUtils.get()
                .url(Conn.BASEURL + Conn.STU_JURISDICTION)
                .addParams("user_id", user_id)
                .addParams("stu_id", stu_id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ParseStuJurisdiction parseStuJurisdiction = ParseStuJurisdiction.parseStuJurisdiction(response);
                        int code = parseStuJurisdiction.getCode();
                        if (code == 0) {
                            List<StuJurisdictionBean> stuJurisdictionBeanList = parseStuJurisdiction.getData();
                            for (int i = 0; i < stuJurisdictionBeanList.size(); i++) {
                                String jurisdiction = stuJurisdictionBeanList.get(i).getRule_code();
                                AllClassBean allClassBean = new AllClassBean();
                                Log.e(TAG, "jurisdiction: " + jurisdiction);
                                if (!TextUtils.isEmpty(jurisdiction)) {
                                    String[] strarray = jurisdiction.split("_");
                                    if (strarray[1].length() > 2) {
                                        if (strarray[1].equals("jichuke")) {
                                            for (int j = 0; j < strarray.length; j++) {
                                                for (int k = 0; k < dictionaryBeanList.size(); k++) {
                                                    if (dictionaryBeanList.get(k).getCode_num().equals(strarray[j])) {
                                                        if (dictionaryBeanList.get(k).getType().equals("bankuai")) {
                                                            allClassBean.setBankuai(dictionaryBeanList.get(k).getCode_name());
                                                        } else if (dictionaryBeanList.get(k).getType().equals("banben")) {
//                                                            Log.e(TAG, "dictionary: " + dictionaryBeanList.get(k).getCode_num());
                                                            allClassBean.setBanben(dictionaryBeanList.get(k).getCode_name());
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            for (int j = 0; j < strarray.length; j++) {
                                                for (int k = 0; k < dictionaryBeanList.size(); k++) {
                                                    if (dictionaryBeanList.get(k).getCode_num().equals(strarray[j])) {
                                                        if (dictionaryBeanList.get(k).getType().equals("banben")) {
//                                                            Log.e(TAG, "dictionary: " + dictionaryBeanList.get(k).getCode_num());
                                                            allClassBean.setBanben(dictionaryBeanList.get(k).getCode_name());
                                                        } else if (dictionaryBeanList.get(k).getType().equals("xueke")) {
                                                            allClassBean.setXueke(dictionaryBeanList.get(k).getCode_name());
                                                        } else if (dictionaryBeanList.get(k).getType().equals("nianfen")) {
                                                            allClassBean.setNianfen(dictionaryBeanList.get(k).getCode_name());
                                                        } else if (dictionaryBeanList.get(k).getType().equals("zibankuai")) {
                                                            allClassBean.setBankuai(dictionaryBeanList.get(k).getCode_name());
                                                        } else if (dictionaryBeanList.get(k).getType().equals("xczibankuai")) {
                                                            allClassBean.setBankuai(dictionaryBeanList.get(k).getCode_name());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        allClassBean.setHave_num(stuJurisdictionBeanList.get(i).getHave_num());
                                        allClassBean.setOver_num(stuJurisdictionBeanList.get(i).getOver_num());
                                        allClassBeanList.add(allClassBean);
                                        contentLists.add(allClassBean.getBanben());
//                                        Log.e(TAG, "getbanben: " + allClassBean.getBanben());
                                        contentLists.add(allClassBean.getBankuai());

                                        if (TextUtils.isEmpty(allClassBean.getNianfen())) {
//                                            Log.e(TAG, "getnianfen: " + allClassBean.getNianfen());
                                            contentLists.add("----");
                                        } else {
//                                            Log.e(TAG, "nianfen: " + allClassBean.getNianfen());
                                            contentLists.add(allClassBean.getNianfen());
                                        }
//                                    contentLists.add(allClassBean.getXueke());
                                        contentLists.add(allClassBean.getHave_num() + "");
                                        contentLists.add((allClassBean.getHave_num() - allClassBean.getOver_num()) + "");
                                    }
                                }
                            }
//

                            NewAllClassFeatureTitleAdapter adapter = new NewAllClassFeatureTitleAdapter(getActivity(), contentLists, R.color.white);
                            rv_content.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
//                            Log.e(TAG, "allclasslist: " + allClassBeanList);
                        }
                    }
                });
    }

    private void getDictionaryData() {
        OkHttpUtils.get()
                .url(Conn.BASEURL + Conn.DICTIONARY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ParseDictionary parseDictionary = ParseDictionary.parseDictionary(response);
                        int code = parseDictionary.getCode();
                        if (code == 0) {
                            dictionaryBeanList = parseDictionary.getDicts();
                            getStuJurisdictionData();

                        }
                    }
                });
    }
}

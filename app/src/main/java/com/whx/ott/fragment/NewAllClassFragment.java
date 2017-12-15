package com.whx.ott.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whx.ott.R;
import com.whx.ott.adapter.GeneralAdapter;
import com.whx.ott.adapter.NewAllClassAdapter;
import com.whx.ott.adapter.NewAllClassNameAdapter;
import com.whx.ott.bean.BaseInfo;
import com.whx.ott.bean.Soulplates;
import com.whx.ott.bean.SubjectsBean;
import com.whx.ott.beanfeature.NewMineFeatureBean;
import com.whx.ott.beanfeature.ParseNewMineFeature;
import com.whx.ott.beanfeature.ParseSearchSoul;
import com.whx.ott.beanfeature.SoulLastBean;
import com.whx.ott.bridge.RecyclerViewBridge;
import com.whx.ott.conn.Conn;
import com.whx.ott.conn.NumToString;
import com.whx.ott.db.DBManager;
import com.whx.ott.presenter.TopMenuPresenter;
import com.whx.ott.util.SharedpreferenceUtil;
import com.whx.ott.widget.LinearLayoutManagerTV;
import com.whx.ott.widget.MainUpView;
import com.whx.ott.widget.RecyclerViewTV;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by HelloWorld on 2016/9/18.
 */
public class NewAllClassFragment extends Fragment {
    private RecyclerViewTV top_menu_rv;
    private RecyclerViewTV mRecyclerView;
    private MainUpView mainUpView1;
    private RecyclerViewBridge mRecyclerViewBridge;
    private View oldView;
    private TextView classhour_tv;
    private String basic_time;
    private TextView title_name_tv;
    private List<Soulplates> soulplatesLists = new ArrayList<Soulplates>();//标题集合
    private List<String> allClassNames = new ArrayList<>();//所有科目名字集合
    private List<String> all_tvs = new ArrayList<>();//所有课程次数集合
    private List<String> used_tvs = new ArrayList<>();//已经播放次数集合
    private List<String> names = new ArrayList<>();
    private String name;
    private static int soulNum;
    private String URL = Conn.BASEURL + Conn.NEWALL_CLASS;
    private String user_id;
    private List<SoulLastBean> soulLastBeanList;
    private List<NewMineFeatureBean> newMineFeatureList;
    private ParseSearchSoul data;
    private ParseNewMineFeature newdata;
    private List<SoulLastBean> lastBeenlist;
    private List<NewMineFeatureBean> MineFeatureList;
    private TextView subject_tv;
    private String macAdress;
    private static int posInt;
    private TextView subjectnum_tv;
    private RecyclerViewTV feature_classname;
    private List<String> keys;
    private List<String> newKeys;
    private List<String> valuse;
    private DBManager manager;
    private List<SubjectsBean> subjectLists;
    private TextView tv_town_money;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_allclass, null);
        initView(view);
        macAdress = (String) SharedpreferenceUtil.getData(getActivity(), "dev_id", "");
        user_id = (String) SharedpreferenceUtil.getData(getActivity(), "user_id", "");
        initTopMenu();
        initData();
        return view;
    }

    private void initView(View view) {
//        classhour_tv = (TextView) view.findViewById(R.id.classhour_mine_tv);
        top_menu_rv = view.findViewById(R.id.allclass_top_menu_rv);
        mRecyclerView = view.findViewById(R.id.new_allclass_feature_bow);
        mainUpView1 = view.findViewById(R.id.mainUpView1);
        mainUpView1.setEffectBridge(new RecyclerViewBridge());
        title_name_tv = view.findViewById(R.id.new_allclass_top_tv);
        classhour_tv = view.findViewById(R.id.classhour_mine_newtv);
        subject_tv = view.findViewById(R.id.new_allclass_subject_tv);
        tv_town_money = view.findViewById(R.id.classhour_mine_newtvcountry);
//        subjectnum_tv = (TextView) view.findViewById(R.id.new_allclass_subjectnum_tv);
        feature_classname = view.findViewById(R.id.new_allclass_feature_name);
        // 注意这里，需要使用 RecyclerViewBridge 的移动边框 Bridge.
        mRecyclerViewBridge = (RecyclerViewBridge) mainUpView1.getEffectBridge();
        manager = new DBManager(getActivity());
    }

    private void initData() {
        soulLastBeanList = new ArrayList<>();
        MineFeatureList = new ArrayList<>();
        //存储标题集合
        soulplatesLists = ((BaseInfo) SharedpreferenceUtil.queryObj2Sp(getActivity(), "base_info")).getSoulplates();
        for (int i = 0; i < soulplatesLists.size(); i++) {
            names.add(soulplatesLists.get(i).getSoulplate_name());
        }

    }

    private void initTopMenu() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        top_menu_rv.setLayoutManager(layoutManager);
        top_menu_rv.setFocusable(false);
        GeneralAdapter generalAdapter = new GeneralAdapter(new TopMenuPresenter(names));
        top_menu_rv.setAdapter(generalAdapter);
        generalAdapter.notifyDataSetChanged();
        top_menu_rv.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                // 传入 itemView也可以, 自己保存的 oldView也可以.
                mRecyclerViewBridge.setUnFocusView(itemView);

            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.1f);
                oldView = itemView;
                clickChangeData(position);
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
                onViewItemClick(itemView, position);
                clickChangeData(position);
            }
        });
        //设置默认选中
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                top_menu_rv.requestFocusFromTouch();
                top_menu_rv.getChildAt(0).callOnClick();
            }
        };
        handler.sendMessageDelayed(handler.obtainMessage(), 188);
    }

    //切换焦点数据发生变化
    private void clickChangeData(int position) {
        name = soulplatesLists.get(position).getSoulplate_name();
        Log.e("TAG", "name========= " + name);
        title_name_tv.setText(soulplatesLists.get(position).getSoulplate_name());
        //获得锁点击位置上控件的ID
        soulNum = soulplatesLists.get(position).getId();
        Log.e("TAG", "soulnum下面 " + soulNum);
        posInt = position;
        upView();

    }

    //标题选择
    private void onViewItemClick(View v, int pos) {
        MyRcGlData();
        MyRcGlClassName();
    }

    private void upAdapter() {
        Log.e("all_tvs", all_tvs.size() + "");
        NewAllClassAdapter classAdapter = new NewAllClassAdapter(getActivity(), used_tvs, all_tvs, allClassNames);
        mRecyclerView.setAdapter(classAdapter);
    }

    private void upNameAdapter(List<String> keys, List<String> values) {
        NewAllClassNameAdapter classAdapter = new NewAllClassNameAdapter(getActivity(), keys, values);
        feature_classname.setAdapter(classAdapter);
    }


    //特色课已播课程与次数展示
    private void MyRcGlData() {
        LinearLayoutManagerTV layoutManager = new LinearLayoutManagerTV(getActivity());
        layoutManager.setOrientation(LinearLayoutManagerTV.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                // 传入 itemView也可以, 自己保存的 oldView也可以.
                mRecyclerViewBridge.setUnFocusView(itemView);
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
                if (MineFeatureList.size() != 0) {
                    subject_tv.setText(NumToString.searchSubject(MineFeatureList.get(position).getSubject_id()));
//                subjectnum_tv.setText("总共" + all_tvs.get(position) + "次");
                }
            }

            /**
             * 这里是调整开头和结尾的移动边框.
             */
            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
            }
        });
        //Gridview内容选择进行跳转
        mRecyclerView.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.05f);
                oldView = itemView;
//                onViewItemClick(itemView, position);
            }
        });
    }

    //特色课课程总数展示
    private void MyRcGlClassName() {
        LinearLayoutManagerTV layoutManager = new LinearLayoutManagerTV(getActivity());
        layoutManager.setOrientation(LinearLayoutManagerTV.VERTICAL);
        feature_classname.setLayoutManager(layoutManager);
        feature_classname.setFocusable(false);
        feature_classname.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                // 传入 itemView也可以, 自己保存的 oldView也可以.
                mRecyclerViewBridge.setUnFocusView(itemView);
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
                Log.e("TAG", "onItemSelected--classname-------------> " + position);
                subject_tv.setText(NumToString.searchSubject(Integer.parseInt(keys.get(position))));
            }

            /**
             * 这里是调整开头和结尾的移动边框.
             */
            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
            }
        });
        //Gridview内容选择进行跳转
        feature_classname.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.05f);
                oldView = itemView;
//                onViewItemClick(itemView, position);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);


        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void upView() {
        user_id = (String) SharedpreferenceUtil.getData(getActivity(), "user_id", "");

        Log.e("TAG", "upView: " + user_id);
        Log.e("TAG", "获取的标题ID: " + NewAllClassFragment.soulNum);
        OkHttpUtils.get()
                .url(URL)
                .addParams("user_id", user_id)
                .addParams("model_id", "2")
                .addParams("soulplate_id", soulNum + "")
                .build()
                .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e, int id) {
                                 Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT).show();
                             }

                             @Override
                             public void onResponse(String response, int id) {
                                 data = ParseSearchSoul.getSoul(response);
                                 newdata = ParseNewMineFeature.parseNewMineFeature(response);

                                 if (newdata != null && newdata.getCode().equals("0")) {
                                     if (!newdata.getPlaytimes().equals("max")) {
                                         if (feature_classname != null || mRecyclerView != null) {
                                             feature_classname.setVisibility(View.VISIBLE);
                                             mRecyclerView.setVisibility(View.VISIBLE);
                                         }
                                         newMineFeatureList = newdata.getData();
                                         MineFeatureList.clear();
                                         if (newMineFeatureList != null) {
                                             for (int i = 0; i < newMineFeatureList.size(); i++) {
                                                 MineFeatureList.add(newMineFeatureList.get(i));
                                             }

                                             Collections.sort(MineFeatureList, new Comparator<NewMineFeatureBean>() {

                                                 /*
                                                  * int compare(Student o1, Student o2) 返回一个基本类型的整型，
                                                  * 返回负数表示：o1 小于o2，
                                                  * 返回0 表示：o1和o2相等，
                                                  * 返回正数表示：o1大于o2。
                                                  */
                                                 public int compare(NewMineFeatureBean o1, NewMineFeatureBean o2) {

                                                     //按照学科升序排列
                                                     if (o1.getSubject_id() > o2.getSubject_id()) {
                                                         return 1;
                                                     }
                                                     if (o1.getSubject_id() == o2.getSubject_id()) {
                                                         return 0;
                                                     }
                                                     return -1;
                                                 }
                                             });

                                             allClassNames.clear();
                                             used_tvs.clear();
                                             all_tvs.clear();
                                             if (soulNum == (NewAllClassFragment.posInt + 1)) {
                                                 Log.e("TAG", "ID位置是否绑定测试: " + (NewAllClassFragment.posInt + 1));
                                                 if (MineFeatureList.size() != 0 && MineFeatureList != null) {
                                                     for (int i = 0; i < MineFeatureList.size(); i++) {
                                                         allClassNames.add(MineFeatureList.get(i).getSoulcourse_name());
                                                         used_tvs.add(MineFeatureList.get(i).getOver_viewnum() + "");
                                                         all_tvs.add(MineFeatureList.get(i).getPlay_count() + "");
                                                     }
                                                 }
                                                 Log.e("TAG", "position--------->" + allClassNames.size());
                                                 upAdapter();
                                                 String rule = newdata.getRule();
                                                 String[] rulearray = rule.split("[;]");
                                                 keys = new ArrayList<String>();
                                                 List<String> values = new ArrayList<String>();
                                                 subjectLists = manager.getSubjectList();

                                                 final HashMap<String, String> hashMap = new HashMap<String, String>();
                                                 for (int j = 0; j < subjectLists.size(); j++) {
                                                     hashMap.put((j + 1) + "", "0");
                                                 }
                                                 for (int j = 0; j < subjectLists.size(); j++) {
                                                     for (int i = 0; i < rulearray.length; i++) {
                                                         String[] valueArray = rulearray[i].split("[#]");
                                                         if (valueArray[0].equals(subjectLists.get(j).getId() + "")) {
                                                             Log.e("TAG", "onResponse value里面的内容---------> " + valueArray[0] + "---------" + valueArray[1]);
                                                             hashMap.put(valueArray[0], valueArray[1]);
                                                             Log.e("TAG", "onResponse hashmap---------> " + hashMap);
                                                         }
                                                     }
                                                 }


                                                 Log.e("TAG", "onResponse hashMap1---------> " + hashMap);
                                                 // 排序y

                                                 List<Map.Entry<String, String>> mapList = new ArrayList<Map.Entry<String, String>>(hashMap.entrySet());
                                                 Collections.sort(mapList, new Comparator<Map.Entry<String, String>>() {

                                                     @Override
                                                     public int compare(Map.Entry<String, String> lhs, Map.Entry<String, String> rhs) {
                                                         return lhs.getKey().compareTo(rhs.getKey());
                                                     }
                                                 });
                                                 for (int i = 0; i < mapList.size(); i++) {
                                                     String key = mapList.get(i).getKey().toString();
                                                     String val = mapList.get(i).getValue().toString();
                                                     keys.add(key);
                                                     values.add(val + "");
                                                 }
                                                 Log.e("tag", "onResponse:keys--> " + keys.toString());
                                                 Log.e("tag", "onResponse:values-------------> " + values.toString());
                                                 upNameAdapter(keys, values);
                                             }
                                         } else {
                                             Toast.makeText(getActivity(), "ID出现错误", Toast.LENGTH_SHORT).show();
                                         }
                                     }
                                 }
                                 if (newdata.getCode().equals("0")) {
                                     if (newdata.getPlaytimes().equals("max")) {
                                         final HashMap<String, String> hashMap = new HashMap<String, String>();
                                         subjectLists = manager.getSubjectList();
                                         feature_classname.setVisibility(View.VISIBLE);
                                         mRecyclerView.setVisibility(View.VISIBLE);
                                         keys = new ArrayList<String>();
                                         List<String> values = new ArrayList<String>();
                                         for (int i = 0; i < subjectLists.size(); i++) {
                                             hashMap.put((i + 1) + "", "all");
                                         }
                                         List<Map.Entry<String, String>> mapList = new ArrayList<Map.Entry<String, String>>(hashMap.entrySet());
                                         Collections.sort(mapList, new Comparator<Map.Entry<String, String>>() {

                                             @Override
                                             public int compare(Map.Entry<String, String> lhs, Map.Entry<String, String> rhs) {
                                                 return lhs.getKey().compareTo(rhs.getKey());
                                             }
                                         });
                                         for (int i = 0; i < mapList.size(); i++) {
                                             String key = mapList.get(i).getKey().toString();
                                             String val = mapList.get(i).getValue().toString();
                                             keys.add(key);
                                             values.add(val + "");
                                         }
                                         Log.e("tag", "onResponse:keys--max> " + keys.toString());
                                         Log.e("tag", "onResponse:values-------------max> " + values.toString());
                                         upNameAdapter(keys, values);
                                     }
                                 } else {
                                     subject_tv.setText("科目");
                                     feature_classname.setVisibility(View.INVISIBLE);
                                     mRecyclerView.setVisibility(View.INVISIBLE);
                                 }
                             }

                         }
                );

    }


}

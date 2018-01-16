package com.whx.ott.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.whx.ott.R;
import com.whx.ott.adapter.FeatureYearAdapter;
import com.whx.ott.adapter.TopspeedEngAdapter;
import com.whx.ott.adapter.VideoDataAdapter;
import com.whx.ott.bean.ParseTopspeedEng;
import com.whx.ott.bean.ResultBean;
import com.whx.ott.bean.Soulplates;
import com.whx.ott.bean.SubjectsBean;
import com.whx.ott.bean.TeachersBean;
import com.whx.ott.bean.VideoPathBean;
import com.whx.ott.bean.YearsBean;
import com.whx.ott.beanfeature.ParseFeature;
import com.whx.ott.beanfeature.SoulcoursesBean;
import com.whx.ott.bridge.RecyclerViewBridge;
import com.whx.ott.conn.Conn;
import com.whx.ott.db.DBManager;
import com.whx.ott.util.SharedpreferenceUtil;
import com.whx.ott.widget.GridLayoutManagerTV;
import com.whx.ott.widget.MainUpView;
import com.whx.ott.widget.RecyclerViewTV;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by HelloWorld on 2017/11/7.
 */

public class NewFeatureActivity extends Activity {

    private RecyclerViewTV rv_years;
    private RecyclerViewTV rv_names;
    private RecyclerViewTV rv_subjects;
    private RecyclerViewTV rv_classnames;
    private MainUpView mainUpView;
    private RecyclerViewBridge mRecyclerViewBridge;
    private View oldView;
    private List<String> subjectLists = new ArrayList<String>(); //科目集合
    private List<String> tvs = new ArrayList<>();
    private List<Integer> ivs = new ArrayList<Integer>();//科目背景图片集合
    private List<String> names = new ArrayList<String>();//标题集合
    private List<Integer> textcolors = new ArrayList<>();//字体颜色集合
    private ParseFeature parseFeature;
    private List<SoulcoursesBean> soulcoursesBeanList;
    private List<Soulplates> soulplates;
    private int Key;
    public int soulNum;
    private String URL = Conn.BASEURL + Conn.GET_FEATURE;
    private List<List<SoulcoursesBean>> mLists = new ArrayList<>(); //存放接口返回值数据
    private DBManager manager = new DBManager(this);
    private VideoDataAdapter videoDataAdapter;
    private String TAG = NewFeatureActivity.class.getSimpleName();
    private ResultBean resultBean;
    private String type;
    private VideoPathBean test;
    private String macAdress;
    private TopspeedEngAdapter topspeedEngAdapter;
    private List<SubjectsBean> subjectsBeanList;
    private List<String> years = new ArrayList<String>();
    private List<YearsBean> yearsBeanList;
    private String years_name;
    private RecyclerViewTV rv_teachers;
    private List<TeachersBean> teacherBeanList;
    private List<String> teachers = new ArrayList<>();
    private int year_id;
    private int teacher_id;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (soulNum == 5 || soulNum == 4) {
                        MyRcGlDataEng();
//                        teacherView(3);
                    } else if (soulNum == 0) {
                        soulNum = 1;
                        MyRcGlData();
//                        updateUI2(1);
                    } else {
                        MyRcGlData();
//                        teacherView(1);
                    }
                    break;
                default:
                    MyRcGlData();
//                    teacherView(1);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_new);
        initView();
        titleData();
        yearsView();
        soulplateView();
        teacherView(1);
    }

    private void initView() {
        rv_years = findViewById(R.id.rv_years_feature);
        rv_names = findViewById(R.id.rv_name_feature);
        rv_subjects = findViewById(R.id.rv_subjects_feature);
        rv_classnames = findViewById(R.id.rv_classname_feature);
        rv_teachers = findViewById(R.id.rv_teachers_feature);
        mainUpView = findViewById(R.id.mainUpView1);
        mainUpView.setEffectBridge(new RecyclerViewBridge());
        mRecyclerViewBridge = (RecyclerViewBridge) mainUpView.getEffectBridge();
        soulplates = (List<Soulplates>) getIntent().getExtras().getSerializable("soulplate_list");
        macAdress = (String) SharedpreferenceUtil.getData(this, "dev_id", "");
    }

    private void yearsView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_years.setLayoutManager(layoutManager);
        rv_years.setFocusable(false);
        FeatureYearAdapter featureYearAdapter = new FeatureYearAdapter(this, years);
        rv_years.setAdapter(featureYearAdapter);
        featureYearAdapter.notifyDataSetChanged();
        rv_years.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setUnFocusView(itemView);
                oldView = itemView;
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
            }
        });
        rv_years.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.05f);
                oldView = itemView;
                onTitleItemClick(position);
            }
        });

        //设置默认选中
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                rv_years.requestFocusFromTouch();
                if (rv_years.getChildCount() > 0) {
                    rv_years.getChildAt(0).callOnClick();
                }
            }
        };
        handler.sendMessageDelayed(handler.obtainMessage(), 188);
    }

    private void onTitleItemClick(int position) {
        years_name = yearsBeanList.get(position).getYear_name();
        year_id = yearsBeanList.get(position).getId();
        handler.sendEmptyMessageDelayed(1, 300);
    }

    //添加顶部标题
    private void soulplateView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_names.setLayoutManager(layoutManager);
        rv_names.setFocusable(false);
        FeatureYearAdapter featureYearAdapter = new FeatureYearAdapter(this, names);
        rv_names.setAdapter(featureYearAdapter);
        featureYearAdapter.notifyDataSetChanged();
        rv_names.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                // 传入 itemView也可以, 自己保存的 oldView也可以.
                mRecyclerViewBridge.setUnFocusView(itemView);
                //获得所点击位置上控件的ID
                soulNum = soulplates.get(position).getId();

            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
                //获得所点击位置上控件的ID
                soulNum = soulplates.get(position).getId();
                onViewItemClick();
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
        rv_names.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.05f);
                oldView = itemView;

                //获得所点击位置上控件的ID
                soulNum = soulplates.get(position).getId();
                Log.e("TAG", "onItemClick: " + soulNum);
                onViewItemClick();
            }
        });
    }


    //标题选择
    private void onViewItemClick() {
        if (soulNum == 4) {
            subjectLists.clear();
            subjectLists.add("英语");
            type = 1 + "";
            teacherView(3);
            MyRcGlDataEng();
        } else if (soulNum == 5) {
            subjectLists.clear();
            subjectLists.add("英语");
            type = 2 + "";
            teacherView(3);
            MyRcGlDataEng();
        } else {
            subjectLists.clear();
            subjectsBeanList = manager.getSubjectList();
            for (int i = 0; i < subjectsBeanList.size(); i++) {
                subjectLists.add(subjectsBeanList.get(i).getSubject_name());
            }
            MyRcGlData();
        }
    }

    private void teacherView(int subject_id) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_teachers.setLayoutManager(layoutManager);
        rv_teachers.setFocusable(false);
        teacherBeanList = manager.getTeacherList(subject_id);
//        Log.e(TAG, "teacherView: " + teacherBeanList);
        teachers.clear();
        for (int i = 0; i < teacherBeanList.size(); i++) {
            teachers.add(teacherBeanList.get(i).getTeacher_name());
        }
        FeatureYearAdapter featureYearAdapter = new FeatureYearAdapter(this, teachers);
        rv_teachers.setAdapter(featureYearAdapter);
        rv_teachers.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {

            }
        });
        rv_teachers.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
                teacher_id = teacherBeanList.get(position).getId();
                updateUI2(subject_id);
            }
        });
    }

    private void MyRcGlDataEng() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_subjects.setLayoutManager(layoutManager);
        rv_subjects.setFocusable(false);
        FeatureYearAdapter featureYearAdapter = new FeatureYearAdapter(this, subjectLists);
        rv_subjects.setAdapter(featureYearAdapter);
        featureYearAdapter.notifyDataSetChanged();
        updateUIEng(type);
        rv_subjects.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setUnFocusView(itemView);
//                onViewItemClickEng(itemView, position);
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
//                onViewItemClickEng(itemView, position);
            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
            }
        });

        rv_subjects.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.05f);
                oldView = itemView;
                onViewItemClickEng();
            }
        });
    }

    private void onViewItemClickEng() {
        teacher_id=0;
        updateUIEng(type);
    }

    //添加gridlayout
    private void MyRcGlData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_subjects.setLayoutManager(layoutManager);
        rv_subjects.setFocusable(false);
        FeatureYearAdapter featureYearAdapter = new FeatureYearAdapter(this, subjectLists);
        rv_subjects.setAdapter(featureYearAdapter);
        updateUI2(0);
        rv_subjects.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setUnFocusView(itemView);
                int subject_id = subjectsBeanList.get(position).getId();
//                onViewItemClick1(itemView, subject_id);
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
                int subject_id = subjectsBeanList.get(position).getId();
//                onViewItemClick1(itemView, subject_id);

            }

            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;
            }
        });

        //Gridview内容选择进行跳转
        rv_subjects.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.05f);
                oldView = itemView;

                int subject_id = subjectsBeanList.get(position).getId();
                onViewItemClick1(subject_id);
            }
        });
    }

    private void onViewItemClick1(int subject_id) {
        teacher_id=0;
        teacherView(subject_id);
        updateUI2(subject_id);
    }


    private void titleData() {
        yearsBeanList = manager.getYearList();
        for (int i = 0; i < yearsBeanList.size(); i++) {
            years.add(yearsBeanList.get(i).getYear_name());
        }
        subjectsBeanList = manager.getSubjectList();
        for (int i = 0; i < subjectsBeanList.size(); i++) {
            subjectLists.add(subjectsBeanList.get(i).getSubject_name());
        }
        names.clear();
        for (int i = 0; i < soulplates.size(); i++) {
            String name = soulplates.get(i).getSoulplate_name();
            names.add(name);
        }
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

    /**
     * 点击item加载最里面数据
     */
    private void updateUI2(int subject_id) {
        OkHttpUtils.get()
                .url(URL)
                .addParams("start", 0 + "")
                .addParams("end", 100 + "")
                .addParams("soulplate_id", soulNum + "")
                .addParams("subject_id", subject_id + "")
                .addParams("year_id", year_id + "")
                .addParams("teacher_id", teacher_id + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(NewFeatureActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
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
                                initVideoRc();
                            } else {
                                Toast.makeText(NewFeatureActivity.this, "用户没有权限访问", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            initVideoRc();
                        }

                    }
                });

    }


    private void updateUIEng(final String type) {

        OkHttpUtils.get()
                .url(URL)
                .addParams("start", 0 + "")
                .addParams("end", 100 + "")
                .addParams("soulplate_id", soulNum + "")
                .addParams("subject_id", 3 + "")
                .addParams("year_id", year_id + "")
                .addParams("teacher_id", teacher_id + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(NewFeatureActivity.this, "请检查网络", Toast.LENGTH_SHORT).show();
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
                        Log.e(TAG, "mlists: " + mLists.size());
                        Key = parseFeature.getCode();
                        if (mLists.get(0).size() != 0) {
                            if (Key == 0) {
                                MyFeatureEng(type, soulcoursesBeanList);
                            } else {
                                Toast.makeText(NewFeatureActivity.this, "用户没有权限访问", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            soulcoursesBeanList.clear();
                            MyFeatureEng(type, soulcoursesBeanList);
                        }
                    }
                });

    }

    private void MyFeatureEng(String type, List<SoulcoursesBean> soulcoursesBeanList) {
        GridLayoutManagerTV gridLayoutManagerEng = new GridLayoutManagerTV(this, 2);
        gridLayoutManagerEng.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_classnames.setLayoutManager(gridLayoutManagerEng);
        rv_classnames.setFocusable(false);

        topspeedEngAdapter = new TopspeedEngAdapter(NewFeatureActivity.this, soulcoursesBeanList, years_name);
        rv_classnames.setAdapter(topspeedEngAdapter);
        topspeedEngAdapter.notifyDataSetChanged();
        rv_classnames.setOnItemListener(new RecyclerViewTV.OnItemListener() {
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

        rv_classnames.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.05f);
                oldView = itemView;
                int lecture = position + 1;
                SoulcoursesBean bean = soulcoursesBeanList.get(position);
                getUrl(type, lecture + "", bean);
            }
        });
    }


    private void initVideoRc() {
        GridLayoutManagerTV gridlayoutManager = new GridLayoutManagerTV(this, 2); // 解决快速长按焦点丢失问题.
        gridlayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        rv_classnames.setLayoutManager(gridlayoutManager);
        rv_classnames.setFocusable(false);

        videoDataAdapter = new VideoDataAdapter(soulcoursesBeanList, years_name, this);
        rv_classnames.setAdapter(videoDataAdapter);
        videoDataAdapter.notifyDataSetChanged();
        videoDataAdapter.setOnItemClickListener(new VideoDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.05f);
                oldView = itemView;
                SoulcoursesBean soulcoursesBean = soulcoursesBeanList.get(position);
                Log.e("soulcoursesBean", "===>" + soulcoursesBean.getSoulcourse_name() + "  " + soulcoursesBean.getFile_name());
                String file_name = soulcoursesBean.getFile_name();
                if (TextUtils.isEmpty(file_name)) {
                    //判断file_name 是否为空，如果为空无此视频
                    Toast.makeText(NewFeatureActivity.this, "资源不存在", Toast.LENGTH_SHORT).show();
                } else {
                    //跳转播放视频
                    getFeatureUrl(Conn.BASEURL + Conn.VIDEOURL, file_name, soulcoursesBean);
                }

            }

            @Override
            public void onItemLongClick(View itemView, int position) {
            }
        });
        rv_classnames.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                // 传入 itemView也可以, 自己保存的 oldView也可以.
                mRecyclerViewBridge.setUnFocusView(itemView);

            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, 1.05f);
                oldView = itemView;

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
        rv_classnames.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.05f);
                oldView = itemView;
            }
        });

    }


    /**
     * 获取播放地址
     */

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
                        Toast.makeText(NewFeatureActivity.this, "视频地址已过期", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        resultBean = ParseTopspeedEng.parser(response);
                        Intent intent = new Intent(NewFeatureActivity.this, CCPlayerActivity.class);
                        Bundle bundle = new Bundle();
                        String videoPath = resultBean.getForwardUrl();
                        String status = resultBean.getStatus();
                        bundle.putSerializable("soulcoursesBean", bean);
                        intent.putExtra("videoPath", videoPath);
                        intent.putExtra("model_id", 2);
                        intent.putExtra("type_id", 2);
                        intent.putExtra("type_name", "云教室");
//                        intent.putExtra("status", status);
                        Log.e("path", "---------" + videoPath);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
    }

    private void getFeatureUrl(String path, String filename, final SoulcoursesBean bean) {
        String[] strarray = bean.getCode_num().split("_");
        OkHttpUtils.get()
                .url(path)
                .addParams("file_name", filename)
                .addParams("devid", macAdress)
                .addParams("ban_code", strarray[0])
                .addParams("mode_code", strarray[1])
                .addParams("year_code", "year_" + year_id + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(NewFeatureActivity.this, "视频地址已过期", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        test = VideoPathBean.getdata(response);
                        Intent intent = new Intent(NewFeatureActivity.this, CCPlayerActivity.class);
                        Bundle bundle = new Bundle();
                        String videoPath = test.getUrl();
                        bundle.putSerializable("soulcoursesBean", bean);
                        intent.putExtra("videoPath", videoPath);
                        intent.putExtra("model_id", 2);
                        intent.putExtra("type_id", 2);
                        intent.putExtra("type_name", "云教室");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
    }

}

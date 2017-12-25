package com.whx.ott.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whx.ott.R;
import com.whx.ott.adapter.GradesAdapter;
import com.whx.ott.adapter.SubjectsAdapter;
import com.whx.ott.adapter.TeachersAdapter;
import com.whx.ott.adapter.TermsAdapter;
import com.whx.ott.adapter.TownSearchResultAdapter;
import com.whx.ott.adapter.YearsAdapter;
import com.whx.ott.bean.CoursesBean;
import com.whx.ott.bean.GradesBean;
import com.whx.ott.bean.JichuResult;
import com.whx.ott.bean.SubjectsBean;
import com.whx.ott.bean.TeachersBean;
import com.whx.ott.bean.TermsBean;
import com.whx.ott.bean.VideoPathBean;
import com.whx.ott.bean.YearsBean;
import com.whx.ott.bridge.RecyclerViewBridge;
import com.whx.ott.db.TownDBManager;
import com.whx.ott.presenter.HighClassPresenter;
import com.whx.ott.presenter.viewinface.BasicClassView;
import com.whx.ott.util.KeyBoardUtils;
import com.whx.ott.util.SharedpreferenceUtil;
import com.whx.ott.widget.CustomProgressDialog;
import com.whx.ott.widget.GridLayoutManagerTV;
import com.whx.ott.widget.LinearLayoutManagerTV;
import com.whx.ott.widget.MainUpView;
import com.whx.ott.widget.RecyclerViewTV;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oleky on 2016/9/1.
 */
public class TownClassActivity extends Activity implements View.OnFocusChangeListener
        ,RecyclerViewTV.OnItemListener,BasicClassView{

    public static final String YEARID       = "year_id";
    public static final String TERMID       = "term_id";
    public static final String GRADEID      = "grade_id";
    public static final String SUBJECTID    = "subject_id";
    public static final String TEACHERID    = "teacher_id";
    public static final String START        = "start";
    public static final String END          = "end";
    VideoPathBean test;
    private int start_item  = 0;            //分页开始
    private int end_item    =100;//分页结束 共100条
    private String macAdress = "";
    private RecyclerViewTV years_gp, terms_gp, grades_gp, subjects_gp, teachers_gp,results_gp;
    private View                oldView;
    private EditText etKeyWords;
    private String keyWord = "";

    private YearsAdapter        yearsAdapter;
    private TermsAdapter        termsAdapter;
    private GradesAdapter       gradesAdapter;
    private SubjectsAdapter     subjectsAdapter;
    private TeachersAdapter     teachersAdapter;
    private TownSearchResultAdapter resultAdapter;
    JichuResult jichu;
    MainUpView                  mainUpView1;
    RecyclerViewBridge          mRecyclerViewBridge;

    private List<YearsBean>     yearList    = new ArrayList<>();
    private List<TermsBean>     termList    = new ArrayList<>();
    private List<GradesBean>    gradeList   = new ArrayList<>();
    private List<SubjectsBean>  subjectList = new ArrayList<>();
    private List<TeachersBean>  teacherList = new ArrayList<>();
    private List<CoursesBean>   courseList  = new ArrayList<>();
    private List<Map<String, String>> singleList = new ArrayList<>();
    Map<String, String> map1 = new HashMap<>();
    Map<String, String> map2 = new HashMap<>();
    Map<String, String> map3 = new HashMap<>();
    Map<String, String> map4 = new HashMap<>();
    Map<String, String> map5 = new HashMap<>();
    TownDBManager manager = new TownDBManager(this);
    private HighClassPresenter mClassPresenter;
    private CoursesBean mCoursesBean;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int key = msg.what;
            start_item = 0;
            switch (key) {
                case 1:
                    singleList.set(0,(HashMap<String,String>)msg.obj);
                    updateUI(singleList, start_item, end_item);
                    keyWord = "";
                    break;
                case 2:
                    singleList.set(1,(HashMap<String,String>)msg.obj);
                    updateUI(singleList, start_item, end_item);
                    etKeyWords.setText("");
                    keyWord = "";
                    break;
                case 3:
                    singleList.set(2,(HashMap<String,String>)msg.obj);
                    updateUI(singleList, start_item, end_item);
                    etKeyWords.setText("");
                    keyWord = "";
                    break;
                case 4:
                    singleList.set(3,(HashMap<String,String>)msg.obj);
                    updateUI(singleList, start_item, end_item);
                    etKeyWords.setText("");
                    keyWord = "";
                    break;
                case 5:
                    singleList.set(4,(HashMap<String,String>)msg.obj);
                    updateUI(singleList, start_item, end_item);
                    etKeyWords.setText("");
                    keyWord = "";
                    break;
                case 6:
                    updateUI(singleList, start_item, end_item);
                    break;
            }
            updateGridView();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_basic_town);
        mClassPresenter = new HighClassPresenter(this);
        initData();
        initRecycler();

    }

    /**
     * 联网数据初始化
     * */
    private void initData() {
        //调试版
        map1.put(YEARID, "");
        map2.put(TERMID, "");
        map3.put(GRADEID, "");
        map4.put(SUBJECTID,"");
        map5.put(TEACHERID, "");
//初始化显示
        String[] str_array = new String[]{"1", "2", "3"};
        yearList = manager.getYearList();
        termList = manager.getTermList();
        gradeList = manager.getGradeList(str_array);
        subjectList = manager.getSubjectList();
        teacherList = manager.getTeacherList(1);
        singleList.add(0, map1);
        singleList.add(1, map2);
        singleList.add(2, map3);
        singleList.add(3, map4);
        singleList.add(4, map5);

        macAdress = (String) SharedpreferenceUtil.getData(this, "dev_id", "");
    }


    private void initRecycler() {
        years_gp    = findViewById(R.id.id_years_new);
        terms_gp    = findViewById(R.id.id_terms_new);
        grades_gp   = findViewById(R.id.id_grades_new);
        subjects_gp = findViewById(R.id.id_subjects_new);
        teachers_gp = findViewById(R.id.id_teachers_new);
        results_gp  = findViewById(R.id.id_gridview_new);
        mainUpView1 = findViewById(R.id.id_mainUpView_new);


        mainUpView1.setEffectBridge(new RecyclerViewBridge());
        mRecyclerViewBridge = (RecyclerViewBridge) mainUpView1.getEffectBridge();
        mRecyclerViewBridge.setUpRectResource(R.drawable.item_rectangle_2);
        mRecyclerViewBridge.setTranDurAnimTime(200);

        LinearLayoutManagerTV linearLayoutManager1 = new LinearLayoutManagerTV(TownClassActivity.this);
        LinearLayoutManagerTV linearLayoutManager2 = new LinearLayoutManagerTV(TownClassActivity.this);
        LinearLayoutManagerTV linearLayoutManager3 = new LinearLayoutManagerTV(TownClassActivity.this);
        LinearLayoutManagerTV linearLayoutManager4 = new LinearLayoutManagerTV(TownClassActivity.this);
        LinearLayoutManagerTV linearLayoutManager5 = new LinearLayoutManagerTV(TownClassActivity.this);
        GridLayoutManagerTV gridLayoutManager = new GridLayoutManagerTV(TownClassActivity.this, 2);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager5.setOrientation(LinearLayoutManager.HORIZONTAL);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        years_gp.setLayoutManager(linearLayoutManager1);
        terms_gp.setLayoutManager(linearLayoutManager2);
        grades_gp.setLayoutManager(linearLayoutManager3);
        subjects_gp.setLayoutManager(linearLayoutManager4);
        teachers_gp.setLayoutManager(linearLayoutManager5);
        results_gp.setLayoutManager(gridLayoutManager);

        initEdit();
        initYear();
        initTerm();
        initGrade();
        initSubject();
        initTeacher();
    }

    private void initEdit() {
        etKeyWords = findViewById(R.id.et_keywords);
        etKeyWords.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        etKeyWords.setOnEditorActionListener(mOnEditorActionListener);
        etKeyWords.setOnClickListener(v -> KeyBoardUtils.openKeyBoard(etKeyWords, TownClassActivity.this));
        etKeyWords.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                KeyBoardUtils.closeKeyBoard(etKeyWords, TownClassActivity.this);
            }
        });
    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String word = etKeyWords.getText().toString().trim();
                if (TextUtils.isEmpty(word)) {
                    keyWord = "";
                } else {
                    keyWord = word;
                }
                handler.sendEmptyMessageDelayed(6, 200);
                return true;
            }
            return false;
        }
    };

    /**
     * 分类栏操作
     * */
    private void initYear() {
        yearsAdapter = new YearsAdapter(this);
        yearsAdapter.setData(yearList);
        years_gp.setAdapter(yearsAdapter);
        years_gp.setFocusable(false);
        yearsAdapter.notifyDataSetChanged();

        years_gp.setOnItemListener(this);

        years_gp.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                int count = parent.getChildCount();
                for (int i = 0; i < count; i++) {
//                    parent.getChildAt(i).setBackgroundResource(R.drawable.colorfulbg);
                    ((TextView) parent.getChildAt(i)).setTextSize(14);
                    ((TextView) parent.getChildAt(i)).setTextColor(Color.WHITE);
                }
//                itemView.setBackgroundResource(R.drawable.item_rectangle);
                ((TextView) itemView).setTextColor(Color.YELLOW);
                ((TextView) itemView).setTextSize(16);
                int id = yearList.get(position).getId();
                //请求服务器，返回参数更新下面UI
                Message msg = Message.obtain();
                map1.put(YEARID, id + "");
               msg.obj = map1;
                msg.what = 1;
                handler.sendMessageDelayed(msg, 200);
                updateUI(singleList, start_item, end_item);

            }
        });

//
           Handler handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                years_gp.requestFocusFromTouch();
                years_gp.getChildAt(0).callOnClick();
                updateGridView();
            }
        };
        handler1.sendMessageDelayed(handler1.obtainMessage(), 188);
        updateGridView();
    }

    /**
     * 阶段栏操作
     * */
    private void initTerm() {
        termsAdapter = new TermsAdapter(this);
        termsAdapter.setData(termList);
        terms_gp.setAdapter(termsAdapter);
        terms_gp.setFocusable(false);
        termsAdapter.notifyDataSetChanged();
        terms_gp.setOnItemListener(this);

        terms_gp.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
              int count =  parent.getChildCount();
                for (int i = 0; i < count; i++) {
//                    parent.getChildAt(i).setBackgroundResource(R.drawable.colorfulbg);
                    ((TextView) parent.getChildAt(i)).setTextSize(14);
                    ((TextView) parent.getChildAt(i)).setTextColor(Color.WHITE);
                }
//                itemView.setBackgroundResource(R.drawable.item_rectangle);
                ((TextView) itemView).setTextColor(Color.YELLOW);
                ((TextView) itemView).setTextSize(16);

                int id = termList.get(position).getId();
                String gradeids = termList.get(position).getGrade_ids();
                /**
                 * term栏与grade栏联动
                 * */
                if (!TextUtils.isEmpty(gradeids)&&gradeids!=null) {
                    String[] array = gradeids.split(",");
                    gradeList = manager.getGradeList(array);
                    initGrade();
                    map3.put(GRADEID, "");
                }

                //请求服务器，返回参数更新下面UI
                Message msg = Message.obtain();
                map2.put(TERMID, id + "");
                msg.obj = map2;
                msg.what = 2;
                handler.sendMessageDelayed(msg, 200);
            }
        });
    }

    /**
     * 年级栏操作
     * */
    private void initGrade() {
        gradesAdapter = new GradesAdapter(this);
        gradesAdapter.setData(gradeList);
        grades_gp.setAdapter(gradesAdapter);
        grades_gp.setFocusable(false);
        gradesAdapter.notifyDataSetChanged();

        grades_gp.setOnItemListener(this);

        grades_gp.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                int count = parent.getChildCount();
                for (int i = 0; i < count; i++) {
//                    parent.getChildAt(i).setBackgroundResource(R.drawable.colorfulbg);
                    ((TextView) parent.getChildAt(i)).setTextSize(14);
                    ((TextView) parent.getChildAt(i)).setTextColor(Color.WHITE);
                }
//                itemView.setBackgroundResource(R.drawable.item_rectangle);
                ((TextView) itemView).setTextColor(Color.YELLOW);
                ((TextView) itemView).setTextSize(16);

                int id = gradeList.get(position).getId();
                //请求服务器，返回参数更新下面UI
                Message msg = Message.obtain();
                map3.put(GRADEID, id + "");
                msg.what = 3;
                msg.obj = map3;
                handler.sendMessage(msg);

            }
        });
    }

    /**
     *学科栏操作
     * */
    private void initSubject() {
        subjectsAdapter = new SubjectsAdapter(this);
        subjectsAdapter.setData(subjectList);
        subjects_gp.setAdapter(subjectsAdapter);
        subjects_gp.setFocusable(false);
        subjectsAdapter.notifyDataSetChanged();

        subjects_gp.setOnItemListener(this);

        subjects_gp.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                int count = parent.getChildCount();
                for (int i = 0; i < count; i++) {
//                    parent.getChildAt(i).setBackgroundResource(R.drawable.colorfulbg);
                    ((TextView) parent.getChildAt(i)).setTextSize(14);
                    ((TextView) parent.getChildAt(i)).setTextColor(Color.WHITE);
                }
//                itemView.setBackgroundResource(R.drawable.item_rectangle);
                ((TextView) itemView).setTextColor(Color.YELLOW);
                ((TextView) itemView).setTextSize(16);

                teacherList = manager.getTeacherList(position + 1);
                initTeacher();
                map5.put(TEACHERID, "");

                int id = subjectList.get(position).getId();
                //请求服务器，返回参数更新下面UI
                Message msg = Message.obtain();
                map4.put(SUBJECTID, id + "");
                msg.what = 4;
                msg.obj = map4;
                handler.sendMessageDelayed(msg, 200);
            }
        });


    }


    //教师栏，特殊，从学科动态获取，在数据库中查询
    private void initTeacher() {
        teachersAdapter = new TeachersAdapter(this);
        teachersAdapter.setData(teacherList);
        teachers_gp.setAdapter(teachersAdapter);
        teachers_gp.setFocusable(false);
        teachersAdapter.notifyDataSetChanged();

        teachers_gp.setOnItemListener(this);

        teachers_gp.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                int count = parent.getChildCount();
                for (int i = 0; i < count; i++) {
//                    parent.getChildAt(i).setBackgroundResource(R.drawable.colorfulbg);
                    ((TextView) parent.getChildAt(i)).setTextSize(14);
                    ((TextView) parent.getChildAt(i)).setTextColor(Color.WHITE);
                }
//                itemView.setBackgroundResource(R.drawable.item_rectangle);
                ((TextView) itemView).setTextColor(Color.YELLOW);
                ((TextView) itemView).setTextSize(16);

                int id = teacherList.get(position).getId();
                //请求服务器，返回参数更新下面UI
                Message msg = Message.obtain();
                map5.put(TEACHERID, id + "");
                msg.what = 5;
                msg.obj = map5;
                handler.sendMessageDelayed(msg, 200);
            }
        });
    }


    /**
     * 获取已经点击的控件id，将已经点击的控件id传入hashmap中,联网
     */
    private void updateUI(List<Map<String,String>> mList,int start,int end) {
        Map<String, String> map = new HashMap<>();
        map.put(START, start + "");
        map.put(END, end + "");
        map.put("devid", macAdress);
        map.put(YEARID, mList.get(0).get(YEARID));
        map.put(TERMID, mList.get(1).get(TERMID));
        map.put(GRADEID, mList.get(2).get(GRADEID));
        map.put(SUBJECTID, mList.get(3).get(SUBJECTID));
        map.put(TEACHERID, mList.get(4).get(TEACHERID));
        mClassPresenter.getTownClassList(map);
        }

    private void loadMore(List<Map<String,String>> mList,int start,int end) {
        Map<String, String> map = new HashMap<>();
        map.put(START, start + "");
        map.put(END, end + "");
        map.put("devid", macAdress);
        map.put(YEARID, mList.get(0).get(YEARID));
        map.put(TERMID, mList.get(1).get(TERMID));
        map.put(GRADEID, mList.get(2).get(GRADEID));
        map.put(SUBJECTID, mList.get(3).get(SUBJECTID));
        map.put(TEACHERID, mList.get(4).get(TEACHERID));
        mClassPresenter.loadmoreTownList(map);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
//        mRecyclerViewBridge.setFocusView(v, oldView, 1.05f);
        v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(300);
        if (v instanceof TextView) {
            ((TextView) v).setTextColor(Color.YELLOW);
            ((TextView) v).setTextSize(16);
            v.animate().scaleX(1.4f).scaleY(1.4f).setDuration(300);
        }
        if (oldView != null) {
            oldView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300);
            if (oldView instanceof TextView) {
                ((TextView) oldView).setTextColor(Color.WHITE);
                ((TextView) oldView).setTextSize(14);
            }
        }
        oldView = v;

    }

    @Override
    public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
        itemView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300);
    }

    @Override
    public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
        itemView.animate().scaleX(1.1f).scaleY(1.1f).setDuration(300);
        if (itemView instanceof TextView) {
            itemView.animate().scaleX(1.4f).scaleY(1.4f).setDuration(300);
        }
        if (oldView != null) {
            oldView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300);
        }
        oldView = itemView;
    }

    @Override
    public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
        itemView.animate().scaleX(1.1f).scaleY(1.1f).setDuration(300);
        if (itemView instanceof TextView) {
            itemView.animate().scaleX(1.4f).scaleY(1.4f).setDuration(300);

        }
        if (oldView != null) {
            oldView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300);

        }
        oldView = itemView;
    }
    private void updateGridView() {

        resultAdapter = new TownSearchResultAdapter(TownClassActivity.this);
        resultAdapter.setData(courseList);
        results_gp.setAdapter(resultAdapter);
        results_gp.setFocusable(false);
        resultAdapter.notifyDataSetChanged();
        results_gp.setOnItemListener(this);
        results_gp.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                //
                CoursesBean bean = courseList.get(position);
                mCoursesBean = bean;
                String file_name = bean.getFile_name();
                if (TextUtils.isEmpty(file_name)) {
                    //判断file_name 是否为空，如果为空无此视频
                    Toast.makeText(TownClassActivity.this, "资源不存在", Toast.LENGTH_SHORT).show();
                } else {
                    //跳转播放视频
                    if (file_name.contains(".")) {
                        mClassPresenter.geturl(file_name, macAdress);
                    } else {
                        //wkweb 地址
                        String videoPath = "http://studycenter.vko.cn/thdGoods/play?videoId="+file_name+"&thirdId=1008";

                        Intent intent = new Intent(TownClassActivity.this, WkWebActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("courseBean", bean);
                        intent.putExtra("videoPath", videoPath);
                        intent.putExtra("model_id",1);
                        intent.putExtra("type", "town");
                        intent.putExtras(bundle);
                        TownClassActivity.this.startActivity(intent);
                    }
                }

            }
        });

        results_gp.setPagingableListener(new RecyclerViewTV.PagingableListener() {
            @Override
            public void onLoadMoreItems() {
                if (courseList != null && courseList.size() > 0) {
                    if (!isLoading) {
                        CustomProgressDialog.show(TownClassActivity.this, "加载中···", false, null);
                        loadHandler.sendEmptyMessageDelayed(10, 2000);
                        isLoading = true;
                    }
                }
            }
        });

        results_gp.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                    int visibleItemCount = recyclerView.getChildCount();
                    int firstVisibleItem = ((RecyclerViewTV) recyclerView).findFirstVisibleItemPosition();
                    if (!((RecyclerViewTV) recyclerView).isLoading && totalItemCount - visibleItemCount <= firstVisibleItem) {
                        if (!isLoading) {
                            CustomProgressDialog.show(TownClassActivity.this, "加载中···", false, null);
                            loadHandler.sendEmptyMessageDelayed(10, 2000);
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    private Handler loadHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            start_item += 100;
            loadMore(singleList, start_item, end_item);
        }
    };

    private boolean isLoading = false;

    @Override
    public void getDate(List<CoursesBean> cList) {
        if (cList != null ) {
            courseList.clear();
            courseList.addAll(cList);
            updateGridView();
        }
    }

    @Override
    public void geturl(String url) {
        Intent intent = new Intent(TownClassActivity.this, CCPlayerActivity.class);
        Bundle bundle = new Bundle();
        String videoPath = !TextUtils.isEmpty(url) ? url : "";
        bundle.putSerializable("courseBean", mCoursesBean);
        intent.putExtra("videoPath", videoPath);
        intent.putExtra("model_id",1);
        intent.putExtra("type", "town");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void moreDate(List<CoursesBean> mList) {
        CustomProgressDialog.dimiss();
        if (mList != null && mList.size() > 0 && courseList != null) {
            courseList.addAll(mList);
            isLoading = false;
            results_gp.setOnLoadMoreComplete();
        }
        resultAdapter.setData(courseList);
        resultAdapter.notifyDataSetChanged();
        isLoading = false;
    }
}

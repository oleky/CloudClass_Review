package com.whx.ott.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.whx.ott.R;
import com.whx.ott.adapter.VideoDataAdapter;
import com.whx.ott.bean.SubjectsBean;
import com.whx.ott.bean.TestUrl;
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

import static android.R.attr.orientation;
import static android.R.attr.switchMinWidth;

/**
 * Created by HelloWorld on 2016/9/1.
 */
public class VideoDataActivity extends Activity {
    private RecyclerViewTV video_rv;
    private MainUpView mainUpView1;
    private RecyclerViewBridge mRecyclerViewBridge;
    private TextView videoname_tv;
    private View oldView;
    private VideoDataAdapter videoDataAdapter;
    private List<String> tvs = new ArrayList<String>(); //科目集合
    private List<Integer> ivs = new ArrayList<Integer>();//科目背景图片集合
    private TextView names_tv;
    private String name;
    private String macAdress;
    private List<SoulcoursesBean> soulcoursesBeanList;
    private int tag;
    private List<List<SoulcoursesBean>> lists = new ArrayList<>();
    TestUrl test;
    private TextView top_tv_name;
    private TextView video_item_rv_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videodata);
        lists = (List<List<SoulcoursesBean>>) getIntent().getExtras().getSerializable("mLists");
        if (lists != null) {
            Log.e("TAG", lists.toString());
        }
        soulcoursesBeanList = lists.get(0);

        name = getIntent().getStringExtra("name");
        Log.e("name", name);
        initView();
        initData();
        initVideoRc();
    }

    private void initView() {
        video_rv = (RecyclerViewTV) findViewById(R.id.video_rv);
        videoname_tv = (TextView) findViewById(R.id.videoname_tv);
        names_tv = (TextView) findViewById(R.id.videoname_tv);
//        top_tv_name = (TextView) findViewById(R.id.video_tv_top);
//        video_item_rv_tv = (TextView) findViewById(R.id.video_item_rv_tv);
        mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
        mainUpView1.setEffectBridge(new RecyclerViewBridge());
        mRecyclerViewBridge = (RecyclerViewBridge) mainUpView1.getEffectBridge();

    }

    private void initData() {
        tvs = new ArrayList<String>();
        if (soulcoursesBeanList != null) {
            for (int i = 0; i < soulcoursesBeanList.size(); i++) {
                tvs.add(soulcoursesBeanList.get(i).getSoulcourse_name());
            }
        } else {
            Toast.makeText(this, "目前没有视频可以播放", Toast.LENGTH_SHORT).show();
        }

        names_tv.setText(name);


        if (name.equals("语文")) {
            ivs.add(R.drawable.rechina);
        } else if (name.equals("数学")) {
            ivs.add(R.drawable.remath);
        } else if (name.equals("英语")) {
            ivs.add(R.drawable.reeng);
        } else if (name.equals("物理")) {
            ivs.add(R.drawable.rephy);
        } else if (name.equals("化学")) {
            ivs.add(R.drawable.rechemis);
        } else if (name.equals("生物")) {
            ivs.add(R.drawable.reblo);
        } else if (name.equals("历史")) {
            ivs.add(R.drawable.lishi);
        } else if (name.equals("地理")) {
            ivs.add(R.drawable.dili);
        } else if (name.equals("数学（文科）")) {
            ivs.add(R.drawable.wenkeshuxue);
        } else if (name.equals("政治")) {
            ivs.add(R.drawable.zhengzhi);
        } else {
            ivs.add(R.drawable.reeng);
        }
        macAdress = (String) SharedpreferenceUtil.getData(this, "dev_id", "");
    }

    private void initVideoRc() {
        GridLayoutManagerTV gridlayoutManager = new GridLayoutManagerTV(this, 3); // 解决快速长按焦点丢失问题.
        gridlayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        video_rv.setLayoutManager(gridlayoutManager);
        video_rv.setFocusable(false);
        videoDataAdapter = new VideoDataAdapter(tvs, ivs, this);
        video_rv.setAdapter(videoDataAdapter);
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
                    Toast.makeText(VideoDataActivity.this, "资源不存在", Toast.LENGTH_SHORT).show();
                } else {
                    //跳转播放视频
                    getUrl(Conn.BASEURL + Conn.GET_VIDEOURL, file_name, soulcoursesBean);
//                    Toast.makeText(VideoDataActivity.this, "视频id"+soulcoursesBean.getSoulcourse_name(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onItemLongClick(View itemView, int position) {

            }
        });
        video_rv.setOnItemListener(new RecyclerViewTV.OnItemListener() {
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
        video_rv.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
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
    private void getUrl(String path, String filename, final SoulcoursesBean bean) {
        OkHttpUtils.get()
                .url(path)
                .addParams("file_name", filename)
                .addParams("devid", macAdress)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(VideoDataActivity.this, "视频地址已过期", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        test = TestUrl.getdata(response);
                        Intent intent = new Intent(VideoDataActivity.this, CCPlayerActivity.class);
                        Bundle bundle = new Bundle();
                        String videoPath = test.getUrl();
                        bundle.putSerializable("soulcoursesBean", bean);
                        intent.putExtra("videoPath", videoPath);
                        intent.putExtra("model_id", 2);
                        intent.putExtra("type", "cloud");
                        intent.putExtras(bundle);
                        VideoDataActivity.this.startActivity(intent);
                    }
                });
    }
}

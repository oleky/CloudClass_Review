///*
// * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.whx.ott.ui;
//
//import android.app.AlertDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Color;
//import android.media.MediaPlayer;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TableLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//import com.whx.ott.R;
//import com.whx.ott.bean.CoursesBean;
//import com.whx.ott.bean.ParseAddSoulInfo;
//import com.whx.ott.bean.ParseLogin;
//import com.whx.ott.beanfeature.SoulcoursesBean;
//import com.whx.ott.conn.Conn;
//import com.whx.ott.conn.JsonAddPlayInfo;
//import com.whx.ott.conn.JsonAddSoulplatePlayInfo;
//import com.whx.ott.conn.JsonAddTownInfo;
//import com.whx.ott.conn.JsonPaySoulplate;
//import com.whx.ott.conn.NumToString;
//import com.whx.ott.conn.TownNumToString;
//import com.whx.ott.fragment.TracksFragment;
//import com.whx.ott.model.RecentMediaStorage;
//import com.whx.ott.util.SharedpreferenceUtil;
//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.StringCallback;
//
//
//import okhttp3.Call;
//import tv.danmaku.ijk.media.player.IMediaPlayer;
//import tv.danmaku.ijk.media.player.IjkMediaPlayer;
//import tv.danmaku.ijk.media.player.misc.ITrackInfo;
//
///**
// * 全屏播放器
// */
//public class FullScreenVideoActivity extends AppCompatActivity implements TracksFragment.ITrackHolder {
//
//    private int model_id;
//    private String TAG = FullScreenVideoActivity.class.getSimpleName();
//    private CoursesBean coursesBean;  //基础课类
//    private SoulcoursesBean soulcoursesBean; //特色课类
//
//    private String mVideoPath;
//    private Uri mVideoUri;
//    private String mVideoTitle;
//    private String macAdress;
//    //    private AndroidMediaController mMediaController;
//    private CustomMediaController customMediaController;
//    private IjkVideoView mVideoView;
//    private TextView mToastTextView;
//    private TableLayout mHudView;
//    private DrawerLayout mDrawerLayout;
//    private ViewGroup mRightDrawer;
//    private TextView videoTitle;
//    private View showPlayerLoading;//缓冲图标,此处View开始时是显示的
//
//    private int count = 0;
//    private final int CONNECTION_TIMES = 800; //重连次数
//    private int mPosition = 0; //记录播放位置
//
//    private Settings mSettings;
//    private boolean mBackPressed;
//    ParseLogin data;
//    ParseAddSoulInfo addsoulbean;
//    private int savedSeekPos = 0;
//    private volatile boolean hasPayed = false;
//    private volatile boolean hasPayfead = false;
//    private ImageView pauseImg;
//    PlayBroadcastReceiver receiver;
//    private String user_id;
//    private String user_name;
//    private boolean isconnected;
//
//    private String Status;
//    private int mpos = 0;
//    private String type;
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    pauseImg.setVisibility(View.VISIBLE);
//                    break;
//                case 2:
//                    pauseImg.setVisibility(View.GONE);
//                    break;
//                case 4:
//                    hasPayed = true;
//                    break;
//                case 5:
//                    hasPayfead = true;
//                    break;
//                case 6:
//                    //长时间显示loading页面->>固定时间后取消，暂停
//                    tryconnect();
//                    break;
//                default:
//                    pauseImg.setVisibility(View.GONE);
//                    break;
//            }
//        }
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_player);
//        mSettings = new Settings(this);
//        hasPayed = false;
//
//        user_id = (String) SharedpreferenceUtil.getData(FullScreenVideoActivity.this, "user_id", "");
//        user_name = (String) SharedpreferenceUtil.getData(FullScreenVideoActivity.this, "user_name", "");
//        macAdress = (String) SharedpreferenceUtil.getData(FullScreenVideoActivity.this, "dev_id", "");
//        // handle arguments
//        coursesBean = (CoursesBean) getIntent().getSerializableExtra("courseBean");
//        soulcoursesBean = (SoulcoursesBean) getIntent().getSerializableExtra("soulcoursesBean");
//        model_id = getIntent().getIntExtra("model_id", 1);
//        type = getIntent().getStringExtra("type");
//        if (model_id == 1) {
//            if (coursesBean != null) {
//                mVideoPath = getIntent().getStringExtra("videoPath");
//                model_id = coursesBean.getModel_id();
//                mVideoTitle = coursesBean.getCourse_name();
//            } else {
//                mVideoPath = getIntent().getStringExtra("videoPath");
//                model_id = Integer.parseInt(soulcoursesBean.getModel_id());
//                mVideoTitle = soulcoursesBean.getSoulcourse_name();
//            }
//        } else {
//            mVideoPath = getIntent().getStringExtra("videoPath");
//            model_id = Integer.parseInt(soulcoursesBean.getModel_id());
//            mVideoTitle = soulcoursesBean.getSoulcourse_name();
//
//        }
//
//        if (!TextUtils.isEmpty(mVideoPath)) {
//            new RecentMediaStorage(this).saveUrlAsync(mVideoPath);
//        }
//
//        // init UI
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        ActionBar actionBar = getSupportActionBar();
////        mMediaController = new AndroidMediaController(this, false);
////        mMediaController.setSupportActionBar(actionBar);
//        customMediaController = new CustomMediaController(this, false);
//        customMediaController.setSupportActionBar(actionBar);
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//
//        mToastTextView = (TextView) findViewById(R.id.toast_text_view);
//        mHudView = (TableLayout) findViewById(R.id.hud_view);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mRightDrawer = (ViewGroup) findViewById(R.id.right_drawer);
//        pauseImg = (ImageView) findViewById(R.id.pauseimg);
//        showPlayerLoading = (ProgressBar) findViewById(R.id.progressBar);
//        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
//
//        // init player
//        IjkMediaPlayer.loadLibrariesOnce(null);
//        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
//
//        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
//        videoTitle = (TextView) findViewById(R.id.liveName);
//        assert mVideoView != null;
//        mVideoView.setMediaController(customMediaController);
//        mVideoView.setHudView(mHudView);
//
//
//        if (!TextUtils.isEmpty(mVideoTitle)) {
//            actionBar.setTitle(mVideoTitle);
//        } else {
//            actionBar.setTitle("云教室");
//        }
////
//
//        // prefer mVideoPath
//        if (mVideoPath != null)
//            mVideoView.setVideoPath(mVideoPath);
//        else if (mVideoUri != null)
//            mVideoView.setVideoURI(mVideoUri);
//        else {
//            Log.e(TAG, "Null Data Source\n");
//            FullScreenVideoActivity.this.finish();
//            return;
//        }
//
//        mVideoView.start();
//        if (type.equals("town")) {
//            if (model_id == 1) {
//                new JsonAddTownInfo(FullScreenVideoActivity.this, coursesBean).addTownPlay();
//            }
//        } else {
//            if (model_id == 1) {
//                //增加基础课点播信息
//                new JsonAddPlayInfo(FullScreenVideoActivity.this, coursesBean).addPlayInfo(Conn.BASEURL + Conn.ADD_BASPLAYINFO);
//            } else if (model_id == 2) {
//                if (soulcoursesBean != null) {
//                    new JsonAddSoulplatePlayInfo(FullScreenVideoActivity.this, soulcoursesBean).addSoulplatePlayInfo(Conn.BASEURL + Conn.ADD_SOULPLAYINFO);
//                }
//            }
//        }
//
//
//        mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener()
//
//        {
//            @Override
//            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
//                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
//                    showPlayerLoading.setVisibility(View.VISIBLE);
//
//
//                    handler.sendEmptyMessageDelayed(6, 10000);
//
//
//                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
//                    //此接口每次回调完START就回调END,若不加上判断就会出现缓冲图标一闪一闪的卡顿现象
//
//                    handler.removeMessages(6);
//                    if (mp.isPlaying()) {
//                        showPlayerLoading.setVisibility(View.GONE);
//                    }
//                }
//                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
//                    mVideoView.seekTo(mpos);
//                    Log.e("Flag", "myflag is-->" + String.valueOf(hasPayed));
//                }
//
//                return true;
//            }
//        });
//
//        float mSpeed = mVideoView.getRate();
//        Log.e("VideoSpeed", "myspeed now is :" + mSpeed);
//
//        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener()
//
//        {
//            @Override
//            public void onCompletion(IMediaPlayer mp) {
//                Toast.makeText(FullScreenVideoActivity.this, "播放完成", Toast.LENGTH_SHORT).show();
//                FullScreenVideoActivity.this.finish();
//            }
//        });
//
//        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener()
//
//        {
//            @Override
//            public void onPrepared(IMediaPlayer mp) {
//                showPlayerLoading.setVisibility(View.GONE);//缓冲完成就隐藏
//
//            }
//        });
//
//        mVideoView.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener()
//
//        {
//            @Override
//            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
//
//                if (percent >= 40) {
//                    if (type.equals("town")) {
//                        if (model_id == 1) {
//                            if (!hasPayed) {
//                                hasPayed = true;
//                                addCountryPay();
//                            }
//                        }
//                    } else {
//                        if (model_id == 1) {
//                            if (!hasPayed) {
//                                hasPayed = true;
//                                addPayInfo(Conn.BASEURL + Conn.ADD_BASPAYINFO);
//                            }
//                        } else if (model_id == 2) {
//                            if (!hasPayfead) {
//                                hasPayfead = true;
////                            addSoulPayInfo(Conn.BASEURL + Conn.ADD_SOULPAYINFO);
//                                /**
//                                 * 2月21日新增对打包用户和普通用户新接口，原接口作废
//                                 * */
//                                Log.e("mac", macAdress);
////                            if (soulcoursesBean != null) {
//                                JsonPaySoulplate.addSoulpay(user_id, user_name, macAdress, soulcoursesBean.getSoulplate_id(), soulcoursesBean.getSubject_id(),
//                                        soulcoursesBean.getId(), soulcoursesBean.getSoulcourse_name(), new JsonPaySoulplate.PayListener() {
//                                            @Override
//                                            public void code(String code) {
//                                                if ("0".equals(code)) {
//                                                    //打包用户或者有剩余次数
//                                                } else if ("-1".equals(code)) {
//                                                    Toast.makeText(FullScreenVideoActivity.this, "试看时间已过，请及时充值", Toast.LENGTH_SHORT).show();
//                                                    FullScreenVideoActivity.this.finish();
//                                                }
//                                            }
//                                        });
//
////                            }
//                            }
//                        }
//                    }
//                }
//
//                mPosition = (int) mp.getCurrentPosition();
//
//            }
//        });
//
//        mVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener()
//
//        {
//            @Override
//            public boolean onError(IMediaPlayer mp, int what, int extra) {
//                Log.e("error", "onError occured");
//                mpos = mPosition;
//                if (count > CONNECTION_TIMES) {
//                    tryconnect();
//                } else {
//                    showPlayerLoading.setVisibility(View.VISIBLE);
//                    mVideoView.stopPlayback();
//                    mVideoView.release(true);
//                    mVideoView.setVideoURI(Uri.parse(mVideoPath));
//                    mVideoView.start();
//                }
//                count++;
//                Log.e("RETRY", "replay------" + count);
//                return true;
//            }
//        });
//
//
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_MENU) {
//            new AlertDialog.Builder(this)
//                    .setTitle("设置播放速度")
//                    .setSingleChoiceItems(new String[]{"原始速度", "1.5倍速", "2.0倍速"}, 0, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            switch (which) {
//                                case 0:
//                                    mVideoView.setRate(1.0f);
//                                    dialog.dismiss();
//                                    break;
//                                case 1:
//                                    mVideoView.setRate(1.5f);
//                                    dialog.dismiss();
//                                    break;
//                                case 2:
//                                    mVideoView.setRate(2.0f);
//                                    dialog.dismiss();
//                                    break;
//                            }
//                        }
//                    }).show();
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
//
//
//    private void tryconnect() {
//        showPlayerLoading.setVisibility(View.GONE);
//        new AlertDialog.Builder(FullScreenVideoActivity.this).setMessage("网络开小差啦")
//                .setIcon(R.mipmap.ic_launcher)
//                .setNegativeButton("结束播放", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        FullScreenVideoActivity.this.finish();
//                    }
//                })
//                .setPositiveButton("重连一下", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mVideoView.stopPlayback();
//                        mVideoView.release(true);
//                        mVideoView.setVideoURI(Uri.parse(mVideoPath));
//                        mVideoView.start();
//                        count = 0;
//                    }
//                })
//                .setCancelable(false)
//                .show();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        receiver = new PlayBroadcastReceiver();
//        IntentFilter filter = new IntentFilter("android.intent.action.PlayBroadcastReceiver");
//        registerReceiver(receiver, filter);
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        mBackPressed = true;
//        finish();
//        super.onBackPressed();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        unregisterReceiver(receiver);
////        unregisterReceiver(connectionReceiver);
//        if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
//            mVideoView.stopPlayback();
//            mVideoView.release(true);
//            mVideoView.stopBackgroundPlay();
//        } else {
//            mVideoView.enterBackground();
//        }
//        IjkMediaPlayer.native_profileEnd();
//        handler.removeMessages(1);
//        handler.removeMessages(2);
//        handler.removeMessages(4);
//        handler.removeMessages(5);
//        handler.removeMessages(6);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        customMediaController = null;
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        savedSeekPos = mPosition;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mVideoView.seekTo(savedSeekPos);
//
//    }
//
//
//    @Override
//    public ITrackInfo[] getTrackInfo() {
//        if (mVideoView == null)
//            return null;
//
//        return mVideoView.getTrackInfo();
//    }
//
//    @Override
//    public void selectTrack(int stream) {
//        mVideoView.selectTrack(stream);
//    }
//
//    @Override
//    public void deselectTrack(int stream) {
//        mVideoView.deselectTrack(stream);
//    }
//
//    @Override
//    public int getSelectedTrack(int trackType) {
//        if (mVideoView == null)
//            return -1;
//
//        return mVideoView.getSelectedTrack(trackType);
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.activity_up_in, R.anim.activity_up_out);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_player, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_toggle_ratio) {
//            int aspectRatio = mVideoView.toggleAspectRatio();
//            String aspectRatioText = MeasureHelper.getAspectRatioText(this, aspectRatio);
//            mToastTextView.setText(aspectRatioText);
//            customMediaController.showOnce(mToastTextView);
//            return true;
//        }
//
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    /**
//     * 增加基础课扣费
//     */
//    public void addPayInfo(String url) {
//        Log.e(TAG, "addPayInfo: " + coursesBean.getCourse_length() + "" + "devid" + macAdress);
//        OkHttpUtils.get()
//                .url(url)
//                .addParams("user_id", user_id)
//                .addParams("user_name", user_name)
//                .addParams("model_id", 1 + "")
//                .addParams("model_name", "基础课程")
//                .addParams("course_id", coursesBean.getId() + "")
//                .addParams("course_name", coursesBean.getCourse_name())
//                .addParams("teacher_id", coursesBean.getTeacher_id() + "")
//                .addParams("teacher_name", NumToString.searchTeacher(coursesBean.getTeacher_id()))
//                .addParams("deduct_time", coursesBean.getCourse_length() + "") //扣除课时
//                .addParams("devid", macAdress)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        Toast.makeText(FullScreenVideoActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        data = ParseLogin.getUserInfo(response);
//                        if (data.getCode() != null && data.getCode().equals("0")) {
//                            double time = Double.parseDouble(data.getData().get(0).getUser_money());
//                            if (time >= 1) {
//                                Log.e("Time", "---剩余时长" + time);
//                                SharedpreferenceUtil.saveData(FullScreenVideoActivity.this, "user_money", data.getData().get(0).getUser_money());
//                                Toast.makeText(FullScreenVideoActivity.this, "扣费成功", Toast.LENGTH_SHORT).show();
//                                handler.sendEmptyMessage(4);
//                            } else {
//                                Toast.makeText(FullScreenVideoActivity.this, "试看时间已过，请及时充值", Toast.LENGTH_SHORT).show();
//                                FullScreenVideoActivity.this.finish();
//                            }
//                        } else {
//                            Toast.makeText(FullScreenVideoActivity.this, "试看时间已过，请及时充值", Toast.LENGTH_SHORT).show();
//                            FullScreenVideoActivity.this.finish();
//                        }
//                    }
//                });
//    }
//
//    /**
//     * 乡镇基础课扣费
//     */
//    public void addCountryPay() {
//        OkHttpUtils.get()
//                .url(Conn.BASEURL + Conn.COUNTRY_PAY)
//                .addParams("devid", macAdress)
//                .addParams("user_id", user_id)
//                .addParams("user_name", user_name)
//                .addParams("model_id", coursesBean.getModel_id() + "")
//                .addParams("model_name", "乡镇云教室基础课程")
//                .addParams("course_id", coursesBean.getId() + "")
//                .addParams("course_name", coursesBean.getCourse_name())
//                .addParams("teacher_id", coursesBean.getTeacher_id() + "")
//                .addParams("teacher_name", TownNumToString.searchTeacher(coursesBean.getTeacher_id()))
//                .addParams("deduct_time", coursesBean.getCourse_length() + "")
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        data = ParseLogin.getUserInfo(response);
//                        if (data.getCode() != null && data.getCode().equals("0")) {
//                            double time = Double.parseDouble(data.getData().get(0).getTown_money());
//                            if (time >= 1) {
//                                Log.e("Time", "---剩余时长" + time);
//                                SharedpreferenceUtil.saveData(FullScreenVideoActivity.this, "country_user_money", data.getData().get(0).getTown_money());
//                                Toast.makeText(FullScreenVideoActivity.this, "扣费成功", Toast.LENGTH_SHORT).show();
//                                handler.sendEmptyMessage(4);
//                            } else {
//                                Toast.makeText(FullScreenVideoActivity.this, "试看时间已过，请及时充值", Toast.LENGTH_SHORT).show();
//                                FullScreenVideoActivity.this.finish();
//                            }
//                        } else {
//                            Toast.makeText(FullScreenVideoActivity.this, "试看时间已过，请及时充值", Toast.LENGTH_SHORT).show();
//                            FullScreenVideoActivity.this.finish();
//                        }
//                    }
//                });
//    }
//
//
//    public class PlayBroadcastReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String msg = intent.getExtras().getString("msg");
//            if (!TextUtils.isEmpty(msg) && msg.equals("1")) {
//                handler.sendEmptyMessage(1);
//            } else {
//                handler.sendEmptyMessage(2);
//            }
//        }
//    }
//
//
//
//}

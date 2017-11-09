package com.whx.ott.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whx.ott.R;
import com.whx.ott.adapter.SearchResultAdapter;
import com.whx.ott.bean.CoursesBean;
import com.whx.ott.bean.ParseSearch;
import com.whx.ott.bean.TestUrl;
import com.whx.ott.bridge.RecyclerViewBridge;

import com.whx.ott.presenter.SearchPresenter;
import com.whx.ott.presenter.viewinface.SearchView;
import com.whx.ott.util.KeyBoardUtils;
import com.whx.ott.util.SharedpreferenceUtil;
import com.whx.ott.widget.GridLayoutManagerTV;
import com.whx.ott.widget.MainUpView;
import com.whx.ott.widget.RecyclerViewTV;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by HelloWorld on 2016/8/16.
 */
public class SearchActivity extends Activity implements View.OnClickListener,
        RecyclerViewTV.OnItemListener,SearchView{

    private RecyclerViewTV  gridView;
    private EditText        editText;
    private View            oldView;
    MainUpView              mainUpView1;
    RecyclerViewBridge      mRecyclerViewBridge;
    String input = "";
    String macAdress = "";
    ParseSearch jichu;
    private SearchResultAdapter resultAdapter;
    private List<CoursesBean> cList = new ArrayList<>();
    View oldFocus;
    private SearchPresenter mSearchPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initview();
        mSearchPresenter = new SearchPresenter(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initview() {
        editText = (EditText) findViewById(R.id.id_topsearch_home);
        gridView = (RecyclerViewTV) findViewById(R.id.id_gridview_search);
        mainUpView1 = (MainUpView) findViewById(R.id.mainUpViewSearch);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setOnEditorActionListener(mOnEditorActionListener);
        Drawable drawable = getResources().getDrawable(R.drawable.search);
        assert drawable != null;
        drawable.setBounds(0,0,32,32);
        editText.setCompoundDrawables(null, null, drawable, null);
        macAdress = (String) SharedpreferenceUtil.getData(this, "dev_id", "");
        mainUpView1.setEffectBridge(new RecyclerViewBridge());
        mRecyclerViewBridge = (RecyclerViewBridge) mainUpView1.getEffectBridge();
        mRecyclerViewBridge.setUpRectResource(R.drawable.item_rectangle_2);
        mRecyclerViewBridge.setTranDurAnimTime(200);
        GridLayoutManagerTV gridLayoutManager = new GridLayoutManagerTV(SearchActivity.this, 3);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        gridView.setLayoutManager(gridLayoutManager);

        cList = (List<CoursesBean>) getIntent().getExtras().getSerializable("courseList");

        updateGridView();
        editText.setOnClickListener(this);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    KeyBoardUtils.closeKeyBoard(editText,SearchActivity.this);
                }
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (oldFocus != editText) {
                    KeyBoardUtils.closeKeyBoard(editText,SearchActivity.this);
                }
                mainUpView1.setFocusView(editText, oldView, 1.1f);
            }
        });
    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            Log.d("ACTIONID", "--->" + actionId);
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mSearchPresenter.searchCourse(editText.getText().toString(), macAdress, 0, 80);
                return true;
            }
            return false;
        }
    };


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_topsearch_home) {
            KeyBoardUtils.openKeyBoard(editText, this);
        }
    }

    CoursesBean mCoursesBean;

    private void updateGridView() {

        resultAdapter = new SearchResultAdapter(SearchActivity.this);
        resultAdapter.setData(cList);
        gridView.setAdapter(resultAdapter);
        gridView.setFocusable(false);
        resultAdapter.notifyDataSetChanged();
        gridView.setOnItemListener(SearchActivity.this);
        gridView.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                //
                CoursesBean bean = cList.get(position);
                mCoursesBean = bean;

                String file_name = bean.getFile_name();
                if (TextUtils.isEmpty(file_name)) {
                    //判断file_name 是否为空，如果为空无此视频
                    Toast.makeText(SearchActivity.this, "资源不存在", Toast.LENGTH_SHORT).show();
                } else {
                    //跳转播放视频
                    mSearchPresenter.geturl(file_name, macAdress);
                }

            }
        });
    }



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

    @Override
    public void searchSucc(List<CoursesBean> clist) {
        if (cList != null) {
            cList.clear();
            cList.addAll(clist);
            updateGridView();
        }
    }

    @Override
    public void searchFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(this, "未能获取到播放地址，请检查网络重试", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(SearchActivity.this, CCPlayerActivity.class);
            Bundle bundle = new Bundle();
            String videoPath = url;
            bundle.putSerializable("courseBean", mCoursesBean);
            intent.putExtra("videoPath", videoPath);
            intent.putExtra("model_id",1);
            intent.putExtra("type","cloud");
            intent.putExtras(bundle);
            SearchActivity.this.startActivity(intent);
        }
    }

}

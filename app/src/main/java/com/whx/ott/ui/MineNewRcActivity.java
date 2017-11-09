package com.whx.ott.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;


import com.whx.ott.R;
import com.whx.ott.adapter.GeneralAdapter;
import com.whx.ott.adapter.LeftMenuPresenter;
import com.whx.ott.bridge.RecyclerViewBridge;
import com.whx.ott.fragment.LookedFragment;
import com.whx.ott.fragment.ModificationFragment;
import com.whx.ott.fragment.NewAllClassFragment;
import com.whx.ott.widget.MainUpView;
import com.whx.ott.widget.RecyclerViewTV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HelloWorld on 2016/9/4.
 */
public class MineNewRcActivity extends Activity implements RecyclerViewTV.OnItemListener {

    private Context mContext;
    private RecyclerViewTV top_menu_rv;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private NewAllClassFragment allClassFrag;
    private LookedFragment lookedFrag;
    private ModificationFragment modificationFrag;
    private MainUpView mainUpView1;
    private RecyclerViewBridge mRecyclerViewBridge;
    private View oldView;
    private List<String> names = new ArrayList<String>();//标题集合
    LeftMenuPresenter leftMenuPresenter = new LeftMenuPresenter(names);
    private boolean hasFinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_newrc);

        // 初始化上面菜单.
        initView();
        initTopMenu();
        initData();

    }

    private void initView() {
        mContext = MineNewRcActivity.this;
        top_menu_rv = (RecyclerViewTV) findViewById(R.id.top_mine_rv);
        mainUpView1 = (MainUpView) findViewById(R.id.mainUpView1);
        mainUpView1.setEffectBridge(new RecyclerViewBridge());
        // 注意这里，需要使用 RecyclerViewBridge 的移动边框 Bridge.
        mRecyclerViewBridge = (RecyclerViewBridge) mainUpView1.getEffectBridge();
        allClassFrag = new NewAllClassFragment();
        lookedFrag = new LookedFragment();
        modificationFrag = new ModificationFragment();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.top_mine_ll, allClassFrag);
        transaction.add(R.id.top_mine_ll, lookedFrag);
        transaction.add(R.id.top_mine_ll, modificationFrag);
        transaction.hide(lookedFrag);
        transaction.hide(modificationFrag);
        transaction.commit();
    }

    private void initData() {

        names.add("全部课程");
        names.add("学习记录");
        names.add("修改密码");


    }

    private void initTopMenu() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        top_menu_rv.setLayoutManager(layoutManager);
        top_menu_rv.setFocusable(false);
        final GeneralAdapter generalAdapter = new GeneralAdapter(leftMenuPresenter);
        top_menu_rv.setAdapter(generalAdapter);

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
                if (hasFinish==true) {

                } else {
                    onViewItemClick(itemView, position);
                }
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
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        hasFinish = true;
    }



    private void onViewItemClick(View v, int pos) {
        transaction = manager.beginTransaction();
        switch (pos) {
            case 0:
                transaction.show(allClassFrag);
                transaction.hide(lookedFrag);
                transaction.hide(modificationFrag);
                break;
            case 1:
                transaction.show(lookedFrag);
                transaction.hide(allClassFrag);
                transaction.hide(modificationFrag);
                break;
            case 2:
                transaction.show(modificationFrag);
                transaction.hide(lookedFrag);
                transaction.hide(allClassFrag);
            default:
                break;
        }
        transaction.commit();
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


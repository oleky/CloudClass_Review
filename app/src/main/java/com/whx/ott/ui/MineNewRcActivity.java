package com.whx.ott.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.whx.ott.R;
import com.whx.ott.adapter.NewMineTitleAdapter;
import com.whx.ott.bridge.RecyclerViewBridge;
import com.whx.ott.fragment.ModificationFragment;
import com.whx.ott.fragment.NewAllClassFragmentThree;
import com.whx.ott.fragment.NewLookedFragment;
import com.whx.ott.widget.MainUpView;
import com.whx.ott.widget.RecyclerViewTV;

import java.util.ArrayList;
import java.util.List;

//import com.whx.ott.fragment.NewAllClassFragmentTwo;

/**
 * Created by HelloWorld on 2016/9/4.
 */
public class MineNewRcActivity extends Activity implements RecyclerViewTV.OnItemListener {

    private Context mContext;
    private RecyclerViewTV top_menu_rv;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private ModificationFragment modificationFrag;
    private NewAllClassFragmentThree allClassFragmentThree;
    private MainUpView mainUpView1;
    private RecyclerViewBridge mRecyclerViewBridge;
    private View oldView;
    private List<String> names = new ArrayList<String>();//标题集合
    private boolean hasFinish;
    private NewLookedFragment newLookedFragment;

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
        top_menu_rv = findViewById(R.id.top_mine_rv);
        mainUpView1 = findViewById(R.id.mainUpView1);
        mainUpView1.setEffectBridge(new RecyclerViewBridge());
        // 注意这里，需要使用 RecyclerViewBridge 的移动边框 Bridge.
        mRecyclerViewBridge = (RecyclerViewBridge) mainUpView1.getEffectBridge();
        modificationFrag = new ModificationFragment();

//        allClassFragmentTwo = new NewAllClassFragmentTwo();
        allClassFragmentThree = new NewAllClassFragmentThree();
        newLookedFragment = new NewLookedFragment();
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
//        transaction.add(R.id.top_mine_ll, allClassFrag);
//        transaction.add(R.id.top_mine_ll, lookedFrag);
        transaction.add(R.id.top_mine_ll, allClassFragmentThree);
        transaction.add(R.id.top_mine_ll, modificationFrag);
        transaction.add(R.id.top_mine_ll, newLookedFragment);
//        transaction.hide(lookedFrag);
        transaction.hide(newLookedFragment);
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
//        final GeneralAdapter generalAdapter = new GeneralAdapter(leftMenuPresenter);
//        top_menu_rv.setAdapter(generalAdapter);
        NewMineTitleAdapter titleAdapter = new NewMineTitleAdapter(mContext, names);
        top_menu_rv.setAdapter(titleAdapter);
        top_menu_rv.setOnItemListener(new RecyclerViewTV.OnItemListener() {
            @Override
            public void onItemPreSelected(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
                // 传入 itemView也可以, 自己保存的 oldView也可以.
//                mRecyclerViewBridge.setUnFocusView(itemView);
            }

            @Override
            public void onItemSelected(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
//                mRecyclerViewBridge.setFocusView(itemView, 1.1f);
//                oldView = itemView;
//                if (hasFinish == true) {
//                } else {
                onViewItemClick(itemView, position);
//                }
            }

            /**
             * 这里是调整开头和结尾的移动边框.
             */
            @Override
            public void onReviseFocusFollow(RecyclerViewTV parent, View itemView, int position) {
//                mRecyclerViewBridge.setFocusView(itemView, 1.1f);
//                oldView = itemView;
            }
        });
        top_menu_rv.setOnItemClickListener(new RecyclerViewTV.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerViewTV parent, View itemView, int position) {
                onViewItem(itemView, parent);
//                mRecyclerViewBridge.setFocusView(itemView, oldView, 1.1f);
//                oldView = itemView;
                onViewItemClick(itemView, position);
            }
        });
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                top_menu_rv.requestFocusFromTouch();
                top_menu_rv.getChildAt(0).callOnClick();
            }
        };
        handler.sendMessageDelayed(handler.obtainMessage(), 188);
    }

    private void onViewItem(View itemView, RecyclerViewTV parent) {
        int count = parent.getChildCount();
        Drawable drawable = getResources().getDrawable(R.drawable.looked_divider);
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.white));
                ((TextView) view).setCompoundDrawables(null, null, null, null);
            }
        }
        if (itemView instanceof TextView) {
            ((TextView) itemView).setTextColor(getResources().getColor(R.color.new_feature));
            ((TextView) itemView).setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
//            ((TextView) itemView).setCompoundDrawables(null, null, null, drawable);
        }

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
//                transaction.show(allClassFrag);
//                transaction.hide(lookedFrag);
//                transaction.show(allClassFragmentTwo);
                transaction.show(allClassFragmentThree);
                transaction.hide(newLookedFragment);
                transaction.hide(modificationFrag);
                break;
            case 1:
//                transaction.show(lookedFrag);
                transaction.show(newLookedFragment);
//                transaction.hide(allClassFrag);
//                transaction.hide(allClassFragmentTwo);
                transaction.hide(allClassFragmentThree);
                transaction.hide(modificationFrag);
                break;
            case 2:
                transaction.show(modificationFrag);
//                transaction.hide(lookedFrag);
                transaction.hide(newLookedFragment);
//                transaction.hide(allClassFrag);
//                transaction.hide(allClassFragmentTwo);
                transaction.hide(allClassFragmentThree);
            default:
                break;
        }
        if (v!=null&&v.getVisibility() == View.VISIBLE){
            transaction.commit();

        }
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


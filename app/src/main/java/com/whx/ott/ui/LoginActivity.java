package com.whx.ott.ui;

import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.whx.ott.R;
import com.whx.ott.beanfeature.UpdateInfo;
import com.whx.ott.bridge.EffectNoDrawBridge;
import com.whx.ott.presenter.LoginPresenter;
import com.whx.ott.presenter.viewinface.LoginView;
import com.whx.ott.util.UpdateAppManager;
import com.whx.ott.widget.MainUpView;


public class LoginActivity extends Activity implements View.OnClickListener,LoginView{

    private Button loginBtn;
    private EditText username;
    private EditText userpassword;
    private MainUpView mainUpView1;
    private View all;
    private ViewTreeObserver vto;
    private UpdateInfo info;
    private ProgressDialog pBar;
    private UpdateAppManager updateAppManager;
    private LoginPresenter mLoginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        mLoginPresenter = new LoginPresenter(this, this);
        mainUpView1.setEffectBridge(new EffectNoDrawBridge());
        EffectNoDrawBridge bridge = (EffectNoDrawBridge) mainUpView1.getEffectBridge();
        bridge.setTranDurAnimTime(200);
        //设置一定边框的图片
    }
    private void initView(){
        all = this.findViewById(R.id.rl_login_top);
        mainUpView1 = (MainUpView) findViewById(R.id.id_mainUpView_login);
        vto = (ViewTreeObserver) all.getViewTreeObserver();
        loginBtn = (Button) findViewById(R.id.login_btn);
        username = (EditText) findViewById(R.id.username_et);
        userpassword = (EditText) findViewById(R.id.userpassword_et);
        loginBtn.setOnClickListener(this);
        vto.addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if (newFocus != null)
                    newFocus.bringToFront(); //防止被压下面
                float scale = 1.05f;
                mainUpView1.setFocusView(newFocus,all,scale);
                all = newFocus;
            }
        });
        updateAppManager = new UpdateAppManager(this);
        updateAppManager.getUpdateMsg();
    }

    @Override
    public void onClick(View v) {
        String str_user     = username.getText().toString();
        String str_password = userpassword.getText().toString();
        if (TextUtils.isEmpty(str_user)) {
            username.setError("用户名不能为空");
        } else if (TextUtils.isEmpty(str_password)) {
            userpassword.setError("密码不能为空");
        } else {
            //账户名密码输入完整，login
            mLoginPresenter.login(str_user, str_password);
        }

    }

    @Override
    public void loginSuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, NewHomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void loginFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

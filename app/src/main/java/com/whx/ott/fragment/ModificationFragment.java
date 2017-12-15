package com.whx.ott.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whx.ott.R;
import com.whx.ott.beanfeature.ParseMineModification;
import com.whx.ott.conn.Conn;
import com.whx.ott.ui.AgentLoginActivity;
import com.whx.ott.util.SharedpreferenceUtil;
import com.whx.ott.widget.MainUpView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by HelloWorld on 2016/9/22.
 */
public class ModificationFragment extends Fragment implements View.OnClickListener {
    private EditText et_old_pass;
    private EditText et_new_pass;
    private EditText et_new_pass2;
    private Button password_btn;
    private String old_password;
    private String password;
    private String new_password;
    private String user_id;
    private TextView password_tv;
    private String code;
    private View all;
    private ViewTreeObserver vto;
    private TextView old_tv;
    private String username;
    private MainUpView mainUpView1;
    private Animation myAnimation;
    private String URL = Conn.BASEURL + Conn.MODIFY;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modification, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        et_old_pass = view.findViewById(R.id.modification_frg_et_one);
        et_new_pass = view.findViewById(R.id.modification_frg_et_two);
        et_new_pass2 = view.findViewById(R.id.modification_frg_et_three);
        password_btn = view.findViewById(R.id.modification_frg_password);
        password_tv = view.findViewById(R.id.textView4);
        old_tv = view.findViewById(R.id.modification_frg_tv);


        password_btn.setOnClickListener(this);
        viewOnFocusChange(password_btn);

    }
    private  void viewOnFocusChange(final View view){
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    scaleImpl(view);
                }else {
                    scaleImpl1(view);
                }
            }
        });
    }

    public void scaleImpl(View v) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.btn_user_set);
        v.startAnimation(animation);
    }
    public void scaleImpl1(View v) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.btn_usered_set);
        v.startAnimation(animation);

    }

    private void getData(final String username, String password, String new_password, String user_id) {


        OkHttpUtils.post()
                .url(URL)
                .addParams("username", username)
                .addParams("password", password)
                .addParams("user_id", user_id)
                .addParams("new_password", new_password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "修改密码失败", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ParseMineModification.getModifyData(response);
                        code = ParseMineModification.getModifyData(response).getCode();
                        Log.e("TAG", "modify_code: " + code);

                        if (code.equals("-1")) {
                            old_tv.setVisibility(View.VISIBLE);
                            old_tv.setText("*您输入的旧密码有误");
                        } else {
                            Toast.makeText(getActivity(), "修改密码成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), AgentLoginActivity.class);
                            getActivity().startActivity(intent);
                        }
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        old_tv.setVisibility(View.GONE);
        password_tv.setVisibility(View.GONE);
        user_id = (String) SharedpreferenceUtil.getData(getActivity(), "user_id", "");
        username = (String) SharedpreferenceUtil.getData(getActivity(), "user_name", "");
        Log.e("TAG", "modify_user_id: " + user_id);
        old_password = et_old_pass.getText().toString();
        Log.e("TAG", "modify_old_pass: " + old_password);
        password = et_new_pass.getText().toString();
        Log.e("TAG", "modify_pass: " + password);
        new_password = et_new_pass2.getText().toString();
        Log.e("TAG", "modify_new_pass: " + new_password);
        if (TextUtils.isEmpty(old_password)) {
            old_tv.setVisibility(View.VISIBLE);
            old_tv.setText("*旧密码不能为空");
        } else if (TextUtils.isEmpty(new_password) || !password.equals(new_password)) {
            password_tv.setVisibility(View.VISIBLE);
            password_tv.setText("*您的新密码不能为空或您输入的2次新密码不一致");
        } else {
            getData(username, old_password, new_password, user_id);
        }
    }
}

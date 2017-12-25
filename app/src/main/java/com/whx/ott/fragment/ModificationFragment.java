package com.whx.ott.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whx.ott.R;
import com.whx.ott.bean.ParseStuJurisdiction;
import com.whx.ott.conn.Conn;
import com.whx.ott.ui.AgentLoginActivity;
import com.whx.ott.util.SharedpreferenceUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by HelloWorld on 2016/9/22.
 */
public class ModificationFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {
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

    private LinearLayout ll_modification_import;
    private LinearLayout ll_modification_newpass;
    private String stu_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modification, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        ll_modification_import = view.findViewById(R.id.ll_modification_import);
        ll_modification_newpass = view.findViewById(R.id.ll_modification_newpass);
        et_old_pass = view.findViewById(R.id.modification_frg_et_one);
        et_new_pass = view.findViewById(R.id.modification_frg_et_two);
        et_new_pass2 = view.findViewById(R.id.modification_frg_et_three);
        password_btn = view.findViewById(R.id.modification_frg_password);
        password_tv = view.findViewById(R.id.textView4);
        old_tv = view.findViewById(R.id.modification_frg_tv);

        et_old_pass.setOnFocusChangeListener(this);
        et_new_pass.setOnFocusChangeListener(this);
        et_new_pass2.setOnFocusChangeListener(this);
        password_btn.setOnClickListener(this);
        viewOnFocusChange(password_btn);
//        viewOnFocusChange(et_old_pass);
//        viewOnFocusChange(et_new_pass);
//        viewOnFocusChange(et_new_pass2);
        stu_id = (String) SharedpreferenceUtil.getData(getActivity(), "user_id", "");
    }

    private void viewOnFocusChange(final View view) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    scaleImpl(view);
                } else {
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

    private void modificationOldPassWord(String old_password, final String new_password, final String stu_id) {
        OkHttpUtils.post()
                .url(Conn.BASEURL + Conn.STU_VALIDEOLD)
                .addParams("id", stu_id)
                .addParams("old_password", old_password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), "修改密码失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ParseStuJurisdiction parseStuModification = ParseStuJurisdiction.parseStuJurisdiction(response);
                        int code = parseStuModification.getCode();
                        if (code == -1) {
                            ll_modification_import.setVisibility(View.VISIBLE);
                            old_tv.setText(parseStuModification.getMeg());
                        } else {
                            modificationNewPassWord(new_password, stu_id);
                        }
                    }
                });
    }

    private void modificationNewPassWord(String new_password, String stu_id) {
        OkHttpUtils.post()
                .url(Conn.BASEURL + Conn.STU_NEWPASSWORD)
                .addParams("id", stu_id)
                .addParams("new_password", new_password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ParseStuJurisdiction parseStuModification = ParseStuJurisdiction.parseStuJurisdiction(response);
                        int code = parseStuModification.getCode();
                        if (code == 0) {
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
        ll_modification_import.setVisibility(View.GONE);
        ll_modification_newpass.setVisibility(View.GONE);
        stu_id = (String) SharedpreferenceUtil.getData(getActivity(), "user_id", "");
        username = (String) SharedpreferenceUtil.getData(getActivity(), "user_name", "");
//        Log.e("TAG", "modify_user_id: " + user_id);
        old_password = et_old_pass.getText().toString();
//        Log.e("TAG", "modify_old_pass: " + old_password);
        password = et_new_pass.getText().toString();
//        Log.e("TAG", "modify_pass: " + password);
        new_password = et_new_pass2.getText().toString();
//        Log.e("TAG", "modify_new_pass: " + new_password);
        if (TextUtils.isEmpty(old_password)) {
            ll_modification_import.setVisibility(View.VISIBLE);
            old_tv.setText("旧密码不能为空");
        } else if (TextUtils.isEmpty(new_password) || !password.equals(new_password)) {
            ll_modification_newpass.setVisibility(View.VISIBLE);
            password_tv.setText("您的新密码不能为空或您输入的2次新密码不一致");
        } else {
//            getData(username, old_password, new_password, user_id);
            modificationOldPassWord(old_password, new_password, stu_id);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        ll_modification_import.setVisibility(View.GONE);
        ll_modification_newpass.setVisibility(View.GONE);
    }
}

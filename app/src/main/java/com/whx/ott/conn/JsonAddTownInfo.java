package com.whx.ott.conn;

import android.content.Context;
import android.util.Log;

import com.whx.ott.bean.CodeBean;
import com.whx.ott.bean.CoursesBean;
import com.whx.ott.util.SharedpreferenceUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by HelloWorld on 2017/9/13.
 */

public class JsonAddTownInfo {
    Context context;
    String user_id;
    String user_name;
    String devid;
    CoursesBean coursesBean;
    private String TAG = JsonAddTownInfo.class.getSimpleName();

    public JsonAddTownInfo(Context context, CoursesBean coursesBean) {
        this.context = context;
        this.coursesBean = coursesBean;
    }

    public void addTownPlay() {
        user_id = (String) SharedpreferenceUtil.getData(context, "user_id", "");
        user_name = (String) SharedpreferenceUtil.getData(context, "user_name", "");
        devid = (String) SharedpreferenceUtil.getData(context, "dev_id", "");

        OkHttpUtils.get()
                .url(Conn.BASEURL + Conn.COUNTRY_LOOKED)
                .addParams("user_id", user_id)
                .addParams("user_name", user_name)
                .addParams("devid", devid)
                .addParams("model_id", coursesBean.getModel_id() + "")
                .addParams("model_name", "乡镇云教室基础课程")
                .addParams("subject_id", coursesBean.getSubject_id() + "")
                .addParams("subject_name", TownNumToString.searchSubject(coursesBean.getSubject_id()))
                .addParams("grade_id", coursesBean.getGrade_id() + "")
                .addParams("grade_name", TownNumToString.searchGrades(coursesBean.getGrade_id()))
                .addParams("course_id", coursesBean.getId() + "")
                .addParams("course_name", coursesBean.getCourse_name())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "--出现错误--");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        CodeBean codeBean = CodeBean.getCode(response);
                        if (codeBean.getCode().equals("0")) {
                            Log.e(TAG, "---添加成功---");
                        } else {
                            Log.e(TAG, "---添加失败---");
                        }
                    }
                });
    }
}

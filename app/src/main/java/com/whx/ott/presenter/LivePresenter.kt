package com.whx.ott.presenter


import android.content.Context

import com.whx.ott.bean.CoursesBean
import com.whx.ott.beanfeature.SoulcoursesBean
import com.whx.ott.conn.ApiService
import com.whx.ott.conn.NumToString
import com.whx.ott.conn.RetrofitClient
import com.whx.ott.conn.TownNumToString
import com.whx.ott.extentions.DelegatesExt
import com.whx.ott.presenter.viewinface.LiveView
import com.whx.ott.util.RxUtil
import com.whx.ott.util.SharedpreferenceUtil
import io.reactivex.functions.Consumer

import java.util.HashMap


/**
 * Created by oleky on 2017/9/11.
 */

class LivePresenter(private val mContext: Context, private var mLiveView: LiveView?) : Presenter() {
    private val mService: ApiService = RetrofitClient().createApiClient()

    val user_id: String by DelegatesExt.preference(mContext, "user_id", "")

    val user_name: String by DelegatesExt.preference(mContext, "user_name", "")

    val devid: String by DelegatesExt.preference(mContext, "dev_id", "")

    //增加乡镇基础课播放记录
    fun addTownPlayInfo(bean: CoursesBean) {
        val map = HashMap<String, String>()
        map.apply {
            put("user_id", user_id)
            put("user_name", user_name)
            put("devid", devid)
            put("model_id", "1")
            put("model_name", "乡镇云教室基础课程")
            put("subject_id","${bean.subject_id}")
            put("subject_name", TownNumToString.searchSubject(bean.subject_id))
            put("grade_id", "${bean.grade_id}")
            put("grade_name", TownNumToString.searchGrades(bean.grade_id))
            put("course_id", "${bean.id}")
            put("course_name", bean.course_name)
        }.let {
            mService.townPlay(it)
                    .compose(RxUtil.rxScheduleHelper())
                    .subscribe({ },
                            { error ->error.printStackTrace() })
        }


    }

    //增加高中基础课播放记录
    fun addHighBasePlayInfo(bean: CoursesBean) {
        val map = HashMap<String, String>()
        map.apply {
            put("user_id", user_id)
            put("user_name", user_name)
            put("devid", devid)
            put("model_id", "1")
            put("model_name", "基础课程")
            put("subject_id","${bean.subject_id}")
            put("subject_name", NumToString.searchSubject(bean.subject_id))
            put("grade_id", "${bean.grade_id}")
            put("grade_name", NumToString.searchGrades(bean.grade_id))
            put("course_id", "${bean.id}")
            put("course_name", bean.course_name)
        }.let {
            mService.highbasePlay(it)
                    .compose(RxUtil.rxScheduleHelper())
                    .subscribe({},
                            { error ->error.printStackTrace() })
        }
    }

    //增加高中特色课播放记录
    fun addHighSoulPlayInfo(bean: SoulcoursesBean) {
        val map = HashMap<String, String>()
        map.apply {
            put("user_id", user_id)
            put("user_name", user_name)
            put("devid", devid)
            put("model_id", "2")
            put("model_name", "特色课")
            put("subject_id","${bean.subject_id}")
            put("subject_name",NumToString.searchSubject(bean.subject_id.toInt()))
            put("soulplate_id",bean.soulplate_id)
            put("soulplate_name",NumToString.searSoulplate(bean.soulplate_id.toInt()))
            put("course_id", "${bean.id}")
            put("course_name", bean.soulcourse_name)
        }.let {
            mService.highSouPlay(it)
                    .compose(RxUtil.rxScheduleHelper())
                    .subscribe({},
                            { error ->error.printStackTrace() })
        }
    }

    //乡镇基础课扣费
    fun townPay(bean: CoursesBean) {
        val map = HashMap<String, String>()
        map.apply {
            put("user_id", user_id)
            put("user_name", user_name)
            put("devid", devid)
            put("model_id", "1")
            put("model_name", "乡镇云教室基础课程")
            put("course_id", "${bean.id}")
            put("course_name", bean.course_name)
            put("teacher_id", "${bean.teacher_id}")
            put("teacher_name", TownNumToString.searchTeacher(bean.teacher_id))
            put("deduct_time", "${bean.course_length}")

        }.let {
            mService.townPay(it)
                    .compose(RxUtil.rxScheduleHelper())
                    .subscribe({ result ->
                        if (result.code == "0") {
                            val time:Double = result.data[0].town_money.toDouble()
                            if (time >= 1) {
                                DelegatesExt.preference(mContext, "country_user_money", result.data[0].town_money)
                                mLiveView?.paySucc()
                            } else {
                                mLiveView?.payFailed("余额不足，请及时充值")
                            }
                        }
                    }, { mLiveView?.payFailed("网络状况差") })
        }
    }

    fun highBasePay(bean: CoursesBean) {
        val map = HashMap<String, String>()
        map.apply {
            put("user_id", user_id)
            put("user_name", user_name)
            put("devid", devid)
            put("model_id", "1")
            put("model_name", "基础课程")
            put("course_id", "${bean.id}")
            put("course_name", bean.course_name)
            put("teacher_id", "${bean.teacher_id}")
            put("teacher_name", TownNumToString.searchTeacher(bean.teacher_id))
            put("deduct_time", "${bean.course_length}")
        }.let { mService.highBasePay(it)
                .compose(RxUtil.rxScheduleHelper())
                .subscribe({ result ->
                    if (result.code == "0") {
                        val time:Double = result.data[0].user_money.toDouble()
                        if (time >= 1) {
                            DelegatesExt.preference(mContext, "country_user_money", result.data[0].town_money)
                            mLiveView?.paySucc()
                        } else {
                            mLiveView?.payFailed("余额不足，请及时充值")
                        }
                    }
                }, {error -> error.printStackTrace()
                    mLiveView?.payFailed("网络状况差")

                })
        }
    }

    fun highSoulPay(bean: SoulcoursesBean) {
        val map = HashMap<String, String>()
        map.apply {
            put("user_id", user_id)
            put("user_name", user_name)
            put("devid", devid)
            put("model_id", "2")
            put("model_name", "特色课")
            put("subject_id","${bean.subject_id}")
            put("subject_name",NumToString.searchSubject(bean.subject_id.toInt()))
            put("soulplate_id",bean.soulplate_id)
            put("soulplate_name",NumToString.searSoulplate(bean.soulplate_id.toInt()))
            put("soulcourse_id", "${bean.id}")
            put("soulcourse_name", bean.soulcourse_name)
        }.let {
            mService.highSoulPay(it)
                    .compose(RxUtil.rxScheduleHelper())
                    .subscribe(
                            {result ->
                                if (result.code == "0") {
                                    mLiveView?.paySucc()
                                } else {
                                    mLiveView?.payFailed("余额不足，请及时充值")
                                }
                            },{ mLiveView?.payFailed("网络状况差")
                    })
        }
    }

    override fun onDestory() {
        if (null != mLiveView) {
            mLiveView = null
        }
    }
}

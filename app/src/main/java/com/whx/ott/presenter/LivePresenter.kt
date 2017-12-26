package com.whx.ott.presenter


import android.content.Context
import android.util.Log

import com.whx.ott.bean.CoursesBean
import com.whx.ott.beanfeature.SoulcoursesBean
import com.whx.ott.conn.ApiService
import com.whx.ott.conn.NumToString
import com.whx.ott.conn.RetrofitClient
import com.whx.ott.conn.TownNumToString
import com.whx.ott.extentions.DelegatesExt
import com.whx.ott.presenter.viewinface.LiveView
import com.whx.ott.util.Const
import com.whx.ott.util.RxUtil
import com.whx.ott.util.SharedpreferenceUtil
import io.reactivex.functions.Consumer

import java.util.HashMap


/**
 * Created by oleky on 2017/9/11.
 */

class LivePresenter( mContext: Context, private var mLiveView: LiveView?) : Presenter() {
    private val mService: ApiService = RetrofitClient().createApiClient()

    val student_id: String by DelegatesExt.preference(mContext, Const.USER_ID, "")

    val student_name: String by DelegatesExt.preference(mContext, Const.USER_NAME, "")

    val devid: String by DelegatesExt.preference(mContext, Const.MACADDRESS, "")

    val agent_id: String by DelegatesExt.preference(mContext, Const.AGENT_ID, "")

    val real_name: String by DelegatesExt.preference(mContext, Const.STU_REAL_NAME, "")


    /**
     * 新版本增加基础课播放记录（高中与小初通用，用type区分
     * type:1 雄博士，type：2云教室
     * */
    fun addJichuInfo(bean: CoursesBean,type_id:String,type_name:String) {
        val map = HashMap<String, String>()
        map.apply {
            put("user_id", agent_id)
            put("stu_name", student_name)
            put("stu_realname",real_name)
            put("code_num",bean.code_num?:"")
            put("student_id",student_id)
            put("type_id",type_id)
            put("type_name",type_name)
            put("year_id","${bean.year_id}")
            put("stage_id","${bean.term_id}")
            put("year_id","${bean.year_id}")
            put("subject_id","${bean.subject_id}")
            put("grade_id", "${bean.grade_id}")
            put("teacher_id","${bean.teacher_id}")
            put("course_id", "${bean.id}")
            put("course_name", bean.course_name?:"")

            if (type_id == "2") { //高中基础课
                put("subject_name", NumToString.searchSubject(bean.subject_id))
                put("grade_name", NumToString.searchGrades(bean.grade_id))
                put("year_name",NumToString.searchYears(bean.year_id))
                put("stage_name", NumToString.searchTerms(bean.term_id))
                put("teacher_name",NumToString.searchTeacher(bean.teacher_id))
            } else { //小初基础课
                put("subject_name", TownNumToString.searchSubject(bean.subject_id))
                put("grade_name", TownNumToString.searchGrades(bean.grade_id))
                put("year_name",TownNumToString.searchYears(bean.year_id))
                put("stage_name", TownNumToString.searchTerms(bean.term_id))
                put("teacher_name",TownNumToString.searchTeacher(bean.teacher_id))
            }
        }.let {
            mService.addJichuPlay(it)
                    .compose(RxUtil.rxScheduleHelper())
                    .subscribe({
                        result ->
                        Log.e("REsult",result.meg)
                    },
                            { error ->error.printStackTrace() })
        }


    }


    /**
     * 新版增加特色课播放记录（通用笑出，高中
     * */
    fun addTesePlayInfo(bean: SoulcoursesBean,type_id: String,type_name: String) {
        val map = HashMap<String, String>()
        map.apply {
            put("user_id", agent_id)
            put("stu_name", student_name)
            put("stu_realname",real_name)
            put("code_num", bean.code_num?:"")
            put("student_id",student_id)
            put("type_id",type_id)
            put("type_name", type_name)
            put("year_id","${bean.year_id}")
            put("model_id", bean.soulplate_id?:"") //模块id
            put("model_name", NumToString.searSoulplate(bean.soulplate_id!!.toInt())) //模块名称，绝密预测
            put("teacher_id","${bean.teacher_id}")
            put("subject_id",bean.subject_id?:"")
            put("course_id", bean.id?:"")
            put("course_name", bean.soulcourse_name?:"")
            if (type_id == "2") {
                put("teacher_name", NumToString.searchTeacher(bean.teacher_id))
                put("subject_name", NumToString.searchSubject(bean.subject_id!!.toInt()))
                put("year_name",NumToString.searchYears(bean.year_id))
            } else {
                put("year_name",TownNumToString.searchYears(bean.year_id))
                put("subject_name",TownNumToString.searchSubject(bean.subject_id!!.toInt()))
                put("teacher_name", TownNumToString.searchTeacher(bean.teacher_id))
            }
        }.let {
            mService.addTesePlay(it)
                    .compose(RxUtil.rxScheduleHelper())
                    .subscribe({
                        result->
                        Log.e("AddTesePlaylog",result.meg)
                    },
                            { error ->error.printStackTrace() })
        }
    }

    /**
     * 新版基础课扣费（通用
     * */
    fun jichuPay(bean: CoursesBean,type_id: String,type_name: String) {
        val map = HashMap<String, String>()
        map.apply {
            put("user_id", agent_id)
            put("stu_name", student_name)
            put("stu_realname",real_name)
            put("code_num",bean.code_num?:"")
            put("student_id",student_id)
            put("type_id",type_id)
            put("type_name",type_name)
            put("year_id","${bean.year_id}")
            put("stage_id","${bean.term_id}")
            put("year_id","${bean.year_id}")
            put("subject_id","${bean.subject_id}")
            put("grade_id", "${bean.grade_id}")
            put("teacher_id","${bean.teacher_id}")
            put("course_id", "${bean.id}")
            put("course_name", bean.course_name?:"")

            if (type_id == "2") { //高中基础课
                put("subject_name", NumToString.searchSubject(bean.subject_id))
                put("grade_name", NumToString.searchGrades(bean.grade_id))
                put("year_name",NumToString.searchYears(bean.year_id))
                put("stage_name", NumToString.searchTerms(bean.term_id))
                put("teacher_name",NumToString.searchTeacher(bean.teacher_id))
            } else { //小初基础课
                put("subject_name", TownNumToString.searchSubject(bean.subject_id))
                put("grade_name", TownNumToString.searchGrades(bean.grade_id))
                put("year_name",TownNumToString.searchYears(bean.year_id))
                put("stage_name", TownNumToString.searchTerms(bean.term_id))
                put("teacher_name",TownNumToString.searchTeacher(bean.teacher_id))
            }
        }.let {
            mService.jichuPay(it)
                    .compose(RxUtil.rxScheduleHelper())
                    .subscribe({ result ->
                        Log.e("Pay", result.meg)
                        if (result.code == "0") {
                            mLiveView?.paySucc()
                        } else {
                            mLiveView?.payFailed("余额不足，请及时充值")
                        }
                    }, {
                        e ->e.printStackTrace()
                        mLiveView?.payFailed("网络状况差") })
        }
    }


    /**
     * 新版特色课扣课
     * */
    fun tesePay(bean: SoulcoursesBean,type_id: String,type_name: String) {
        val map = HashMap<String, String>()
        map.apply {
            put("user_id", agent_id)
            put("stu_name", student_name)
            put("stu_realname",real_name)
            put("code_num", bean.code_num?:"")
            put("student_id",student_id)
            put("type_id",type_id)
            put("type_name", type_name)
            put("year_id","${bean.year_id}")
            put("model_id", bean.soulplate_id?:"") //模块id
            put("model_name", NumToString.searSoulplate(bean.soulplate_id!!.toInt())) //模块名称，绝密预测
            put("teacher_id","${bean.teacher_id}")
            put("subject_id",bean.subject_id?:"")
            put("course_id", bean.id?:"")
            put("course_name", bean.soulcourse_name?:"")
            if (type_id == "2") {
                put("teacher_name", NumToString.searchTeacher(bean.teacher_id))
                put("subject_name", NumToString.searchSubject(bean.subject_id!!.toInt()))
                put("year_name",NumToString.searchYears(bean.year_id))
            } else {
                put("year_name",TownNumToString.searchYears(bean.year_id))
                put("subject_name",TownNumToString.searchSubject(bean.subject_id!!.toInt()))
                put("teacher_name", TownNumToString.searchTeacher(bean.teacher_id))
            }
        }.let {
            mService.tesePay(it)
                    .compose(RxUtil.rxScheduleHelper())
                    .subscribe(
                            {result ->
                                Log.e("Pay", result.meg)
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

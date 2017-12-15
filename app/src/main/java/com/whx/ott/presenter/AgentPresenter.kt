package com.whx.ott.presenter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.whx.ott.conn.RetrofitClient
import com.whx.ott.extentions.DelegatesExt
import com.whx.ott.presenter.viewinface.AgentView
import com.whx.ott.util.Const
import com.whx.ott.util.MacUtil
import com.whx.ott.util.RxUtil
import com.whx.ott.util.TimeUtils
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import okhttp3.Call
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

/**
 * Created by oleky on 2017/12/6.
 */
class AgentPresenter(mContext: Context, private var mLoginView: AgentView?) : Presenter() {

    private val mService by lazy {
        RetrofitClient().createApiClient()
    }
    private var macAddress: String? by DelegatesExt.preference(mContext, Const.MACADDRESS, "")
    private var agentID: String? by DelegatesExt.preference(mContext, Const.AGENT_ID, "")
    private var userID: String? by DelegatesExt.preference(mContext, Const.USER_ID, "")
    private var userName: String? by DelegatesExt.preference(mContext, Const.USER_NAME, "")
    private var realName:String? by DelegatesExt.preference(mContext,Const.STU_REAL_NAME,"")


    init {
        macAddress = MacUtil.getMacAddress() //1-1000
        macAddress = "d8:96:e0:03:1a:aa" //测试用
        if (TextUtils.isEmpty(macAddress)) {
            macAddress = MacUtil.getLocalMacAddressFromIp(mContext)?:"00:00:00:00:00:00"
        }
    }

    fun varifyAgent() {
            OkHttpUtils.get()
                    .url("http://api.121drhero.com/public/index.php/interfaces/two/users/getsystemtimeinfo")
                    .build()
                    .execute(object : StringCallback() {
                        override fun onResponse(response: String, id: Int) {
                            try {
                                val obj = JSONObject(response)
                                val code = obj.optInt("code")
                                if (200 == code) {
                                    val timestamp = obj.optString("data")
                                    Log.e("tamstamp", timestamp)

                                    initAgent(TimeUtils.string2Millis(timestamp))
                                } else {
                                    mLoginView?.agentLoginFailed("网络状况差")
                                }
                            }catch (e:JSONException){
                                e.printStackTrace()
                                mLoginView?.agentLoginFailed("网络状况差")
                            }
                        }

                        override fun onError(call: Call, e: Exception, id: Int) {
                            mLoginView?.agentLoginFailed("网络状况差")
                        }
                    })



    }

    private fun initAgent(timestamp: Long) {
        mService.agentVarify(macAddress)
                .compose(RxUtil.rxScheduleHelper())
                .subscribe({
                    result ->
                    val code = result.code
                    if (0 == code) {
                        val hightime = TimeUtils.string2Millis(result.userinfo?.valide_time)
                        val towntime = TimeUtils.string2Millis(result.userinfo?.town_time)
                        if (hightime <= timestamp && towntime <= timestamp) {
                            mLoginView?.agentLoginFailed("您的代理期限已经到期，请与客服联系")
                        } else {
                            agentID = result.userinfo?.user_id
                            val lastIP = result.userinfo?.last_ip ?: ""
                            val lastAddress=result.userinfo?.address_name?:""
                            val agentName = result.userinfo?.user_name ?: ""

                            mLoginView?.agentLoginSucc(agentID,agentName,macAddress, lastIP,lastAddress)
                        }
                    } else {
                        mLoginView?.agentLoginFailed(result.meg)
                    }
                },{
                    e ->e.printStackTrace()
                    mLoginView?.agentLoginFailed("网络状况差")
                })
    }

    //学生登录
    fun stuLogin(agentid: String, username: String, password: String) {
        mService.studentlogin(agentid,username,password)
                .compose(RxUtil.rxScheduleHelper())
                .subscribe({
                    result ->
                    val code = result.code
                    if (0 == code) {
                        mLoginView?.stuLoginSucc()
                        userID = result.stuinfo?.id
                        userName = result.stuinfo?.stu_name
                        realName = result.stuinfo?.stu_real_name
                    } else {
                        mLoginView?.stuLoginFailed(result.meg)
                    }
                },{
                    e ->e.printStackTrace()
                    mLoginView?.stuLoginFailed("请检查网络")
                })

    }

    override fun onDestory() {
        if (mLoginView != null) {
            mLoginView = null
        }
    }

}
package com.whx.ott.extentions

import android.content.Context
import com.hjoleky.stucloud.bean.DictionaryBean
import com.whx.ott.conn.RetrofitClient
import com.whx.ott.util.Const
import com.whx.ott.util.RxUtil
import com.whx.ott.util.SharedpreferenceUtil

/**
 * Created by oleky on 2017/12/5.
 */
fun Context.getDictionary() {
    val types = ArrayList<DictionaryBean>()
    val years = ArrayList<DictionaryBean>()
    val subjects = ArrayList<DictionaryBean>()

    RetrofitClient().createApiClient().dictionary("")
            .compose(RxUtil.rxScheduleHelper())
            .subscribe({
                result ->
                val code = result.code
                if (0 == code) {
                    val dicList = result.dicts
                    dicList?.forEach {
                        bean ->
                        when (bean.type) {
                            "bankuai" -> types.add(bean)
                            "zibankuai" -> types.add(bean)
                            "nianfen" -> years.add(bean)
                            "xueke" -> subjects.add(bean)
                            else ->{}
                        }
                        SharedpreferenceUtil.saveObj2Sp(this, Const.DIC_CLASSTYPE, types)
                        SharedpreferenceUtil.saveObj2Sp(this, Const.DIC_YEAR, years)
                        SharedpreferenceUtil.saveObj2Sp(this, Const.DIC_SUBJECT, subjects)
                    }
                }
            },{
                e ->e.printStackTrace()
                showToast("网络状况差")
            })
}
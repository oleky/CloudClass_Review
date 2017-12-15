package com.hjoleky.stucloud.bean

import java.io.Serializable

/**
 * Created by oleky on 2017/12/5.
 * 数据字典类
 */

class DictionaryBean :Serializable{
    var id: Int? = 0
    var code_num: String? = null
    var code_name: String? = null
    var parent_id: String? = null
    var type: String? = null
    var view_flag: String? = null
}

package com.whx.ott.beanfeature

import java.io.Serializable

/**
 * Created by HelloWorld on 2016/9/6.
 */
class SoulcoursesBean : Serializable {
    var id: String? = null// "1",
    var soulcourse_name: String? = null// "大年班语文",
    var soulcourse_url: String? = null// null,
    var ban_id: String? = null
    var year_id: Int = 0
    var teacher_id: Int = 0
    var model_id: String? = null// "2",
    var soulplate_id: String? = null// "3",
    var subject_id: String? = null// "1",
    var status: String? = null// "0",
    var soulcourse_detail: String? = null// "测试",
    var create_time: String? = null// "2016-08-25 17:59:42",
    var create_user: String? = null// "0",
    var soulcourse_length: String? = null// "90"
    var file_name: String? = null
    var flow_length: String? = null
    var code_num: String? = null
    var grade_id: String? = null
    var grade_name: String? = null

}

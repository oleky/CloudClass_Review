package com.whx.ott.bean

import java.io.Serializable

/**
 * Created by oleky on 2016/9/5.
 * 视频信息列表
 */
class CoursesBean : Serializable {
    var id: Int = 0  //视频id
    var course_name: String? = null //视频名
    var course_url: String? = null //视频url
    var ban_id: Int = 0
    var year_id: Int = 0 //学年id
    var term_id: Int = 0 // 学期id
    var grade_id: Int = 0 //年级id
    var subject_id: Int = 0 //学科id
    var teacher_id: Int = 0 //教师id
    var course_length: Int = 0 //课程时长
    var course_detail: String? = null //课程简介
    var model_id: Int = 0 //所属模块id 基础课or特色课
    var file_name: String? = null //视频文件名
    var flow_length: String? = null //流
    var code_num: String? = null //codenum
}

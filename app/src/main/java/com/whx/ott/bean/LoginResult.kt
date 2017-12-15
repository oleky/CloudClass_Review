package com.whx.ott.bean

import java.io.Serializable

/**
 * Created by oleky on 2017/11/17.
 */
class LoginResult : Serializable {
    var code: Int? = -1
    var meg: String? = null
    var stuinfo: StudentBean? = null
}
package com.general.data.bean

import com.general.utils.MyConstants.UserIdNotLogin
import com.google.gson.annotations.Expose
import java.io.Serializable

data class User  (
    var user_id: String ? = UserIdNotLogin ,
    var email: String  ,
    var name: String ,
    var token: String  ,
    var longitude: Double  ,
    var latitude: Double ,
    var locationUpdatedDate: Long ,
    var createdDate: Long  ,
    @Expose
    var remoteChildEvent: String? = null
) : Serializable
{
    constructor() : this("0","","","",0.0,
        0.0,0,
    0)


}
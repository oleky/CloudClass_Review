package com.whx.ott.conn;


import com.hjoleky.stucloud.bean.DictionaryResult;
import com.whx.ott.bean.AgentResult;
import com.whx.ott.bean.BaseInfo;
import com.whx.ott.bean.LoginResult;
import com.whx.ott.bean.JichuResult;
import com.whx.ott.bean.ParseSearch;
import com.whx.ott.bean.VideoPathBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


/**
 * Created by oleky on 2017/7/8.
 */

public interface ApiService {

    /**
     * 代理商mac验证登录
     */
    @FormUrlEncoded
    @POST(Conn.AGENT_MAC_LOGIN)
    Observable<AgentResult> agentVarify(@Field("dev_id") String macAddress);

    /**
     * 学生登录
     */
    @FormUrlEncoded
    @POST(Conn.STUDENT_LOGIN)
    Observable<LoginResult> studentlogin(@Field("user_id") String agentid,
                                         @Field("stu_name") String stuname,
                                         @Field("stu_pass") String stupass);



    /**
     * 数据字典表查询
     */
    @GET(Conn.DICTIONARY)
    Observable<DictionaryResult> dictionary(@Query("type") String type);

    /**
     * 高中基础数据
     */
    @GET(Conn.GET_BASEINFO)
    Observable<BaseInfo> highBaseInfo();

    /**
     * 乡镇基础数据
     */
    @GET(Conn.TOWN_BASEINFO)
    Observable<BaseInfo> townBaseInfo();



    /**
     * 基础课搜索
     */
    @GET(Conn.SEARCHCOURSE)
    Observable<ParseSearch> seach(@Query("course_name") String cousename,
                                  @Query("devid") String mac,
                                  @Query("start") String start,
                                  @Query("end") String end);

    /**
     * 获取Cloud视频url地址
     */
    @GET(Conn.GET_VIDEOURL)
    Observable<VideoPathBean> liveUrl(@Query("file_name") String filename,
                                      @Query("devid") String mac);

    /**
     * 获取基础课课程列表
     */
    @GET(Conn.GET_BASICCLASS)
    Observable<JichuResult> basicCourses(@QueryMap Map<String, String> map);

    /**
     * 获取乡镇基础课课程列表
     */
    @GET(Conn.TOWN_SELECT)
    Observable<JichuResult> townCourses(@QueryMap Map<String, String> map);


    /**
     * 增加基础课（小初&高中）播放记录
     */
    @FormUrlEncoded
    @POST(Conn.ADD_BASEPLAY_LOG)
    Observable<ApiResult> addJichuPlay(@FieldMap Map<String, String> map);

    /**
     * 增加特色课（小初&高中）播放记录
     */
    @FormUrlEncoded
    @POST(Conn.ADD_TESEPLAY_LOG)
    Observable<ApiResult> addTesePlay(@FieldMap Map<String, String> map);

    /**
     * 基础课扣费（小初&高中
     */
    @FormUrlEncoded
    @POST(Conn.ADD_BASEPAY_LOG)
    Observable<ApiResult> jichuPay(@FieldMap Map<String, String> map);

    /**
     * 特色课扣费（小初&高中
     */
    @FormUrlEncoded
    @POST(Conn.ADD_TESEPAY_LOG)
    Observable<ApiResult> tesePay(@FieldMap Map<String, String> map);

    /**
     * 二维码轮训
     */
    @FormUrlEncoded
    @POST(Conn.SCANFLAG)
    Observable<ApiResult> scanFlag(@Field("user_id") String agentid,
                                   @Field("dev_mac") String mac);


    //---------------------------原接口作废------------------
//    /**
//     * 登陆
//     */
//    @FormUrlEncoded
//    @POST(Conn.USER_LOGIN)
//    Observable<ParseLogin> login(@Field("username") String userName,
//                                 @Field("password") String userPass,
//                                 @Field("devid") String imei);
//    /**
//     * 获取用户存储位置
//     */
//    @FormUrlEncoded
//    @POST(Conn.USER_POS)
//    Observable<ParseLogin> userPos(@Field("user_id") String userid);
//
//    /**
//     * 上报位置报警
//     */
//    @FormUrlEncoded
//    @POST(Conn.POST_POSITION)
//    Observable<ParseLogin> uploadPosition(@Field("user_id") String userid,
//                                          @Field("user_name") String username,
//                                          @Field("location_name") String location,
//                                          @Field("dev_mac") String mac);
//
//    /**
//     * 乡镇云教室基础课播放记录
//     * */
//    @GET(Conn.COUNTRY_LOOKED)
//    Observable<ApiResult>townPlay(@QueryMap Map<String,String>map);
//
//    /**
//     * 乡镇云教室基础课扣课
//     */
//    @GET(Conn.COUNTRY_PAY)
//    Observable<ParseLogin> townPay(@QueryMap Map<String, String> map);
//
//    /**
//     * 高中云教室基础课扣课
//     */
//    @GET(Conn.ADD_BASPAYINFO)
//    Observable<ParseLogin> highBasePay(@QueryMap Map<String, String> map);
//
//    /**
//     * 高中云教室特色课扣课
//     */
//    @GET(Conn.NEW_SOULPAY)
//    Observable<ParseLogin> highSoulPay(@QueryMap Map<String, String>map);
//
//    /**
//     * 增加高中云教室基础课播放记录
//     */
//    @GET(Conn.ADD_BASPLAYINFO)
//    Observable<ApiResult> highbasePlay(@QueryMap Map<String, String> map);
//
//    /**
//     *增加高中云教室特色课播放记录
//     */
//    @GET(Conn.ADD_SOULPLAYINFO)
//    Observable<ApiResult> highSouPlay(@QueryMap Map<String, String> map);
//
}

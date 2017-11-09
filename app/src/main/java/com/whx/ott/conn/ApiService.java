package com.whx.ott.conn;


import com.whx.ott.bean.BaseInfo;
import com.whx.ott.bean.CodeBean;
import com.whx.ott.bean.ParseJichu;
import com.whx.ott.bean.ParseLogin;
import com.whx.ott.bean.ParseSearch;
import com.whx.ott.bean.TestUrl;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
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
     * 登陆
     */
    @FormUrlEncoded
    @POST(Conn.USER_LOGIN)
    Observable<ParseLogin> login(@Field("username") String userName,
                                 @Field("password") String userPass,
                                 @Field("devid") String imei);

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
     * 获取用户存储位置
     */
    @FormUrlEncoded
    @POST(Conn.USER_POS)
    Observable<ParseLogin> userPos(@Field("user_id") String userid);

    /**
     * 上报位置报警
     */
    @FormUrlEncoded
    @POST(Conn.POST_POSITION)
    Observable<ParseLogin> uploadPosition(@Field("user_id") String userid,
                                          @Field("user_name") String username,
                                          @Field("location_name") String location,
                                          @Field("dev_mac") String mac);

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
    Observable<TestUrl> liveUrl(@Query("file_name") String filename,
                                @Query("devid") String mac);

    /**
     * 获取基础课课程列表
     */
    @GET(Conn.GET_BASICCLASS)
    Observable<ParseJichu> basicCourses(@QueryMap Map<String, String> map);

    /**
     * 获取乡镇基础课课程列表
     */
    @GET(Conn.TOWN_SELECT)
    Observable<ParseJichu> townCourses(@QueryMap Map<String, String> map);

    /**
     * 乡镇云教室基础课播放记录
     * */
    @GET(Conn.COUNTRY_LOOKED)
    Observable<CodeBean>townPlay(@QueryMap Map<String,String>map);

    /**
     * 乡镇云教室基础课扣课
     */
    @GET(Conn.COUNTRY_PAY)
    Observable<ParseLogin> townPay(@QueryMap Map<String, String> map);

    /**
     * 高中云教室基础课扣课
     */
    @GET(Conn.ADD_BASPAYINFO)
    Observable<ParseLogin> highBasePay(@QueryMap Map<String, String> map);

    /**
     * 高中云教室特色课扣课
     */
    @GET(Conn.NEW_SOULPAY)
    Observable<CodeBean> highSoulPay(@QueryMap Map<String, String>map);

    /**
     * 增加高中云教室基础课播放记录
     */
    @GET(Conn.ADD_BASPLAYINFO)
    Observable<CodeBean> highbasePlay(@QueryMap Map<String, String> map);

    /**
     *增加高中云教室特色课播放记录
     */
    Observable<CodeBean> highSouPlay(@QueryMap Map<String, String> map);

}

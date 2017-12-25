package com.whx.ott.conn;

/**
 * Created by oleky on 2016/9/7.
 * 网络获取接口
 */
public interface Conn {
//    String BASEURL = "http://60.205.148.237/cloudapi/";
//    String BASEURL = "http://192.168.1.132:8080/cloudapi/";
    String BASEURL = "http://39.107.61.98/cloudapi/";
    //1.登录 post
//    String USER_LOGIN = "index.php?m=Home&c=User&a=login";


    //2.高中基础信息Get
    String GET_BASEINFO = "index.php?m=Home&c=Index&a=baseinfo";

    //3.获取基础课信息Get
    String GET_BASICCLASS = "index.php?m=Home&c=Course&a=courseinfo";

//    //4.增加基础课扣费信息Get
//    String ADD_BASPAYINFO = "index.php?m=Home&c=Payinfo&a=addpayinfo";



//    //6.增加基础课点播记录
//    String ADD_BASPLAYINFO = "index.php?m=Home&c=Playinfo&a=addbaseplayinfo";
//
//    //7.增加特色课点播记录
//    String ADD_SOULPLAYINFO = "index.php?m=Home&c=Playinfo&a=addsoulplayinfo";

    //8.获取视频url地址
    String GET_VIDEOURL = "index.php?m=Home&c=Video&a=baseinfo";

    //9.特色课信息
    String GET_FEATURE = "index.php?m=Home&c=Course&a=soulcourseinfo";

//    //10.基础课点播记录
//    String LOOKED_BASIC = "index.php?m=Home&c=Playinfo&a=getbaseplayinfo";
//
//    //11.特色课点播记录
//    String LOOKED_FEATURE = "index.php?m=Home&c=Playinfo&a=getsoulplayinfo";
    //12.全局搜索
    String SEARCHCOURSE = "index.php?m=Home&c=Scourse&a=searinfo";
    //13.检查更新
    String CHECKVERSON = "index.php?m=Home&c=App&a=getValue";
    //14.特色课点播信息
//    String ALL_CLASS = "index.php?m=Home&c=User&a=getsoulcourse";
    //15.修改密码接口信息
//    String MODIFY = "index.php?m=Home&c=User&a=modifypass";

//    //16.测试个别指导
//    String GET_GEURL = "index.php?m=Home&c=Video&a=guideinfo";
//
//    //17.提交位置信息
//    String POST_POSITION = "index.php?m=Home&c=User&a=warningaddress";
//    //18.用户录入原始位置信息及剩余课时
//    String USER_POS = "index.php?m=Home&c=User&a=getaddress";
//
//    //19.新版特色课扣课（打包，非打包,有无权限
//    String NEW_SOULPAY = "index.php?m=Home&c=Payinfo&a=addnewsoulpayinfo";

    //20.新版特色课记录
//    String NEWALL_CLASS = "index.php?m=Home&c=User&a=getnewsoulcourse";

    //21.新特色课英语接口
    String ENG_BASE = "http://114.215.66.250/whx/course/clickNumber?";

    //22.乡镇云教室基础属性
    String TOWN_BASEINFO = "index.php?m=Home&c=Index&a=townshipbaseinfo";

    //23。乡镇云教室筛选
    String TOWN_SELECT = "index.php?m=Home&c=Course&a=townshipcourseinfo";

//    //24.增加乡镇云教室的播放记录
//    String COUNTRY_LOOKED = "index.php?m=Home&c=Playinfo&a=townshipaddbaseplayinfo";
//
//    //25.乡镇云教室扣费
//    String COUNTRY_PAY = "index.php?m=Home&c=Payinfo&a=townshipaddpayinfo";

    //26.获取所有乡镇云教室的播放历史记录
//    String COUNTRY_SHIP = "index.php?m=Home&c=Playinfo&a=gettownshipbaseplayinfo";

    /**
     * ----------12月新版本云教室---------------
     */

    //1.通过mac验证盒子(post
    String AGENT_MAC_LOGIN = "index.php?m=Home&c=Student&a=authuserbymac";

    //2.学生账号登录(post
    String STUDENT_LOGIN = "index.php?m=Home&c=Student&a=studentlogin";

    //3.数据字典表
    String DICTIONARY = "index.php?m=Home&c=Student&a=getdictdata";

    //4。增加学生基础课（小初/高中）播放记录
    String ADD_BASEPLAY_LOG = "index.php?m=Home&c=Recordlog&a=addstubaseplaylog";

    //5.增加学生基础课(小初/高中扣费记录
    String ADD_BASEPAY_LOG = "index.php?m=Home&c=Recordlog&a=addstubasepaylog";

    //6.增加学生特色课(小初/高中)播放记录
    String ADD_TESEPLAY_LOG = "index.php?m=Home&c=Recordlog&a=addstukindplaylog";

    //7.增加学生特色课扣费记录
    String ADD_TESEPAY_LOG = "index.php?m=Home&c=Recordlog&a=addstukindpaylog";


    //学生的基础课扣课记录信息
    String BASE_CLASSED_RECORD = "index.php?m=Home&c=User&a=getbystuidbaselogs";

    //学生的特色课扣课记录信息
    String FEATURE_CLASSED_RECORD = "index.php?m=Home&c=User&a=getbystuidkindlogs";

    //校验学生输入的旧密码是否正确
    String STU_VALIDEOLD = "index.php?m=Home&c=Student&a=valideoldpassword";

    //修改学生的密码
    String STU_NEWPASSWORD = "index.php?m=Home&c=Student&a=modifypassword";

    //获取学生的看课权限信息
    String STU_JURISDICTION = "index.php?m=Home&c=Student&a=getstuviewauth";

    //二维码轮训接口
    String SCANFLAG = "index.php?m=Home&c=User&a=scanpolicemessage";


}

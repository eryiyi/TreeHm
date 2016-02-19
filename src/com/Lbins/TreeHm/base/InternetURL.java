package com.Lbins.TreeHm.base;

/**
 * Created by liuzwei on 2015/1/12.
 */
public class InternetURL {
    public static final String INTERNAL = "http://192.168.1.100:8080/";
    public static final String QINIU_URL = "http://7xqzj9.com1.z0.glb.clouddn.com/";

    public static final String UPLOAD_TOKEN = INTERNAL + "token.json";
    //1登陆
    public static final String LOGIN__URL = INTERNAL + "memberLogin.do";
    //2根据用户id获得用户信息
    public static final String GET_MEMBER_URL = INTERNAL + "getMemberInfoById.do";
    //3获得所有省份
    public static final String GET_PROVINCE_URL = INTERNAL + "getProvince.do";
    //4获得城市
    public static final String GET_CITY_URL = INTERNAL + "getCity.do";
    //5获得地区
    public static final String GET_COUNTRY_URL = INTERNAL + "getCountry.do";
    //6注册
    public static final String REG_URL = INTERNAL + "memberRegister.do";
    //7发布的求购供应信息列表
    public static final String GET_RECORD_LIST_URL = INTERNAL + "recordList.do";
    //8保存求购供应信息
    public static final String SEND_RECORD_URL = INTERNAL + "sendRecord.do";
    //9获得求购供应详细信息
    public static final String GET_RECORD_BY_ID_URL = INTERNAL + "getRecordById.do";
    //10.分享求购供应信息
    public static final String VIEW_RECORD_BYID_URL = INTERNAL + "viewRecord.do";

}
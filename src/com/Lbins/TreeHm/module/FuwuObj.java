package com.Lbins.TreeHm.module;

/**
 * Created by Administrator on 2016/2/21.
 */
public class FuwuObj {
    private String mm_fuwu_id;
    private String mm_fuwu_nickname;
    private String mm_fuwu_tel;
    private String mm_fuwu_content;
    private String mm_fuwu_type;//0苗木商店
//    1装车工人
//    2物流中心
//    3嫁接团队
//    4调车服务
    private String dateline;
    private String lat;
    private String lng;

    private String distance;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getMm_fuwu_id() {
        return mm_fuwu_id;
    }

    public void setMm_fuwu_id(String mm_fuwu_id) {
        this.mm_fuwu_id = mm_fuwu_id;
    }

    public String getMm_fuwu_nickname() {
        return mm_fuwu_nickname;
    }

    public void setMm_fuwu_nickname(String mm_fuwu_nickname) {
        this.mm_fuwu_nickname = mm_fuwu_nickname;
    }

    public String getMm_fuwu_tel() {
        return mm_fuwu_tel;
    }

    public void setMm_fuwu_tel(String mm_fuwu_tel) {
        this.mm_fuwu_tel = mm_fuwu_tel;
    }

    public String getMm_fuwu_content() {
        return mm_fuwu_content;
    }

    public void setMm_fuwu_content(String mm_fuwu_content) {
        this.mm_fuwu_content = mm_fuwu_content;
    }

    public String getMm_fuwu_type() {
        return mm_fuwu_type;
    }

    public void setMm_fuwu_type(String mm_fuwu_type) {
        this.mm_fuwu_type = mm_fuwu_type;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }
}

package com.Lbins.TreeHm.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.Lbins.TreeHm.MainActivity;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.UniversityApplication;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.dao.RecordMsg;
import com.Lbins.TreeHm.data.EmpData;
import com.Lbins.TreeHm.data.KefuTelData;
import com.Lbins.TreeHm.module.Emp;
import com.Lbins.TreeHm.util.HttpUtils;
import com.Lbins.TreeHm.util.StringUtil;
import com.Lbins.TreeHm.util.Utils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/18.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener ,AMapLocationListener {
    private EditText mobile;
    private EditText password;
    //定位
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private TextView btn_kf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mobile = (EditText) this.findViewById(R.id.mobile);
        password = (EditText) this.findViewById(R.id.password);
        btn_kf = (TextView) this.findViewById(R.id.btn_kf);
        btn_kf.setOnClickListener(this);
        this.findViewById(R.id.reg).setOnClickListener(this);
        this.findViewById(R.id.forgetpwr).setOnClickListener(this);

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mm_emp_mobile", ""), String.class))){
            mobile.setText(getGson().fromJson(getSp().getString("mm_emp_mobile", ""), String.class));
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("password", ""), String.class))){
            password.setText(getGson().fromJson(getSp().getString("password", ""), String.class));
        }

        this.findViewById(R.id.btn_kf).setOnClickListener(this);

        //定位
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位监听
        locationClient.setLocationListener(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }


    boolean isMobileNet, isWifiNet;

    public void sureLogin(View view){
        try {
            isMobileNet = HttpUtils.isMobileDataEnable(getApplicationContext());
            isWifiNet = HttpUtils.isWifiDataEnable(getApplicationContext());
            if (!isMobileNet && !isWifiNet) {
                Toast.makeText(this, R.string.net_work_error, Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //登陆
        if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
            showMsg(LoginActivity.this, getResources().getString(R.string.pwr_error_seven));
            return;
        }
        if(StringUtil.isNullOrEmpty(password.getText().toString())){
            showMsg(LoginActivity.this,  getResources().getString(R.string.pwr_error_three));
            return;
        }
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        loginData();
//        Intent loginV = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(loginV);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forgetpwr:
                Intent forgetV = new Intent(LoginActivity.this, UpdatePwrActivity.class);
                startActivity(forgetV);
                break;
            case R.id.reg:
                Intent regV = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(regV);
                break;
            case R.id.btn_kf:
                //联系客服
                Intent kefuV = new Intent(LoginActivity.this, SelectTelActivity.class);
                startActivity(kefuV);
                break;
        }
    }

    void loginData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.LOGIN__URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    EmpData data = getGson().fromJson(s, EmpData.class);
                                    initOption();
                                    // 设置定位参数
                                    locationClient.setLocationOption(locationOption);
                                    // 启动定位
                                    locationClient.startLocation();
                                    mHandler.sendEmptyMessage(Utils.MSG_LOCATION_START);
                                    saveAccount(data.getData());

                                }else if(Integer.parseInt(code) == 1){
                                    showMsg(LoginActivity.this, getResources().getString(R.string.login_error_one));
                                }
                                else if(Integer.parseInt(code) == 2){
                                    showMsg(LoginActivity.this, getResources().getString(R.string.login_error_two));
                                }
                                else if(Integer.parseInt(code) == 3){
                                    showMsg(LoginActivity.this, getResources().getString(R.string.login_error_three));
                                }
                                else if(Integer.parseInt(code) == 4){
                                    showMsg(LoginActivity.this, getResources().getString(R.string.login_error_four));
                                }
                                else if(Integer.parseInt(code) == 7){
                                    showMsg(LoginActivity.this, getResources().getString(R.string.login_error_seven));
                                }
                                else{
                                    showMsg(LoginActivity.this, getResources().getString(R.string.login_error));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this,  R.string.login_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", mobile.getText().toString());
                params.put("password", password.getText().toString());
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("userId", ""), String.class))){
                    //说明存在userId
                    params.put("userId", getGson().fromJson(getSp().getString("userId", ""), String.class));
                }else {
                    params.put("userId", "");
                }

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    public void saveAccount(Emp emp) {
        //登录成功，绑定百度云推送
        if(StringUtil.isNullOrEmpty(emp.getUserId())){
            //进行绑定
            PushManager.startWork(getApplicationContext(),
                    PushConstants.LOGIN_TYPE_API_KEY,
                    com.Lbins.TreeHm.baidu.Utils.getMetaValue(LoginActivity.this, "api_key"));
        }else {
            //如果已经绑定，就保存
            save("userId", emp.getUserId());
        }

        // 登陆成功，保存用户名密码
        save("mm_emp_id", emp.getMm_emp_id());
        save("mm_emp_mobile", emp.getMm_emp_mobile());
        save("password", password.getText().toString());
        save("mm_emp_nickname", emp.getMm_emp_nickname());
        save("mm_emp_password", emp.getMm_emp_password());
        save("mm_emp_cover", emp.getMm_emp_cover());
        save("mm_emp_type", emp.getMm_emp_type());
        save("mm_emp_company", emp.getMm_emp_company());
        save("mm_emp_company_pic", emp.getMm_emp_company_pic());
        save("mm_emp_company_type", emp.getMm_emp_company_type());
        save("mm_emp_company_address", emp.getMm_emp_company_address());
        save("mm_emp_company_detail", emp.getMm_emp_company_detail());
        save("mm_emp_provinceId", emp.getMm_emp_provinceId());
        save("mm_emp_cityId", emp.getMm_emp_cityId());
        save("mm_emp_countryId", emp.getMm_emp_countryId());
        save("mm_emp_regtime", emp.getMm_emp_regtime());
        save("mm_emp_endtime", emp.getMm_emp_endtime());
        save("mm_level_id", emp.getMm_level_id());
        save("mm_emp_beizhu", emp.getMm_emp_beizhu());
        save("mm_emp_msg_num", emp.getMm_emp_msg_num());
        save("is_login", emp.getIs_login());
        save("is_fabugongying", emp.getIs_fabugongying());
        save("is_fabuqiugou", emp.getIs_fabuqiugou());
        save("is_fabuqiugou_see", emp.getIs_fabuqiugou_see());
        save("is_fabugongying_see", emp.getIs_fabugongying_see());
        save("is_see_all", emp.getIs_see_all());
        save("is_use", emp.getIs_use());
        save("is_pic", emp.getIs_pic());
        save("is_chengxin", emp.getIs_chengxin());
        save("is_miaomu", emp.getIs_miaomu());
        save("lat", emp.getLat());
        save("lng", emp.getLng());
        save("ischeck", emp.getIscheck());
        save("access_token", emp.getAccess_token());
        save("provinceName", emp.getProvinceName());
        save("levelName", emp.getLevelName());
        save("cityName", emp.getCityName());
        save("areaName", emp.getAreaName());
        save("access_token", emp.getAccess_token());
        save("mm_level_num", emp.getMm_level_num());
        save("mm_msg_length", emp.getMm_msg_length());

        save("isLogin", "1");//1已经登录了  0未登录
        save("is_upate_profile", emp.getIs_upate_profile());//1是否补充资料 0否 1是

        Intent intent  =  new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    // 根据控件的选择，重新设置定位参数
    private void initOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
//        String strInterval = etInterval.getText().toString();
//        if (!TextUtils.isEmpty(strInterval)) {
//            // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
            locationOption.setInterval(Long.valueOf("1000"));
//        }

    }

    Handler mHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                case Utils.MSG_LOCATION_FINISH:
                    AMapLocation loc = (AMapLocation) msg.obj;
                    String result = Utils.getLocationStr(loc);
                    if("true".equals(result)){
                        //定位成功
                        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mm_emp_id", ""), String.class))){
                            sendLocation();
                        }
                    }else if("false".equals(result)){

                    }
                    break;
                default:
                    break;
            }
        };
    };

    // 定位监听
    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            msg.what = Utils.MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
        }
    }

    void sendLocation(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SEND_LOCATION_BYID_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){

                                }
                                else{
                                    showMsg(LoginActivity.this, getResources().getString(R.string.location_error_one));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        showMsg(LoginActivity.this, getResources().getString(R.string.location_error_one));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lat", (UniversityApplication.lat==null?"":UniversityApplication.lat));
                params.put("lng", (UniversityApplication.lng==null?"":UniversityApplication.lng));
                params.put("mm_emp_id", getGson().fromJson(getSp().getString("mm_emp_id", ""), String.class) );
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }




}

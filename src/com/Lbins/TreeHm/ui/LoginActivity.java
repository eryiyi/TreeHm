package com.Lbins.TreeHm.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.Lbins.TreeHm.data.EmpData;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mobile = (EditText) this.findViewById(R.id.mobile);
        password = (EditText) this.findViewById(R.id.password);

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
                Toast.makeText(this, "当前网络连接不可用", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //登陆
        if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
            showMsg(LoginActivity.this, "请输入手机号");
            return;
        }
        if(StringUtil.isNullOrEmpty(password.getText().toString())){
            showMsg(LoginActivity.this, "请输入密码");
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
//                Intent forgetV = new Intent(LoginActivity.this, ForgetPwrActivity.class);
//                startActivity(forgetV);
                break;
            case R.id.reg:
                Intent regV = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(regV);
                break;
            case R.id.btn_kf:
                //联系客服
                showTel();
                break;
        }
    }

    // 客服电话窗口
    private void showTel() {
        final Dialog picAddDialog = new Dialog(LoginActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.tel_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        final TextView jubao_cont = (TextView) picAddInflate.findViewById(R.id.jubao_cont);
        jubao_cont.setText(getResources().getString(R.string.kefu_tel));
        //提交
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String contreport = jubao_cont.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + jubao_cont.getText().toString()));
                startActivity(intent);
                picAddDialog.dismiss();
            }
        });

        //取消
        TextView btn_cancel = (TextView) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
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
                                    showMsg(LoginActivity.this, "请检查手机号是否存在");
                                }
                                else if(Integer.parseInt(code) == 2){
                                    showMsg(LoginActivity.this, "请检查密码是否正确");
                                }
                                else if(Integer.parseInt(code) == 3){
                                    showMsg(LoginActivity.this, "该用户已被禁用");
                                }
                                else if(Integer.parseInt(code) == 4){
                                    showMsg(LoginActivity.this, "该用户尚未审核，请联系客服");
                                }
                                else{
                                    showMsg(LoginActivity.this, "登录失败");
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
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", mobile.getText().toString());
                params.put("password", password.getText().toString());
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
        // 登陆成功，保存用户名密码
        save("mm_emp_id", emp.getMm_emp_id());
        save("mm_emp_mobile", emp.getMm_emp_mobile());
        save("password", password.getText().toString());
        save("mm_emp_nickname", emp.getMm_emp_nickname());
        save("mm_emp_password", emp.getMm_emp_password());
        save("mm_emp_cover", emp.getMm_emp_cover());
        save("mm_emp_type", emp.getMm_emp_type());
        save("mm_emp_company", emp.getMm_emp_company());
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

        save("isLogin", "1");//1已经登录了  0未登录

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
                        sendLocation();
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
                                    showMsg(LoginActivity.this, "定位失败！");
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
                        showMsg(LoginActivity.this, "定位失败！");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lat", UniversityApplication.lat);
                params.put("lng", UniversityApplication.lng);
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

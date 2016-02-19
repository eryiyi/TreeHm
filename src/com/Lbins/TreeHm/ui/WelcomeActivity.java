package com.Lbins.TreeHm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.Lbins.TreeHm.MainActivity;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.data.EmpData;
import com.Lbins.TreeHm.module.Emp;
import com.Lbins.TreeHm.util.HttpUtils;
import com.Lbins.TreeHm.util.StringUtil;
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
public class WelcomeActivity extends BaseActivity implements View.OnClickListener,Runnable {
    boolean isMobileNet, isWifiNet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
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
        // 启动一个线程
        new Thread(WelcomeActivity.this).start();
    }
    @Override
    public void onClick(View view) {

    }
    @Override
    public void run() {
        try {
            // 3秒后跳转到登录界面
            Thread.sleep(3000);
            if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mm_emp_mobile", ""), String.class)) && !StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("password", ""), String.class))){
                loginData();
            }else{
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
                                    saveAccount(data.getData());
                                }else if(Integer.parseInt(code) == 1){
                                    showMsg(WelcomeActivity.this, "请检查手机号是否存在");
                                }
                                else if(Integer.parseInt(code) == 2){
                                    showMsg(WelcomeActivity.this, "请检查密码是否正确");
                                }
                                else if(Integer.parseInt(code) == 3){
                                    showMsg(WelcomeActivity.this, "该用户已被禁用");
                                }
                                else if(Integer.parseInt(code) == 4){
                                    showMsg(WelcomeActivity.this, "该用户尚未审核，请联系客服");
                                }
                                else{
                                    showMsg(WelcomeActivity.this, "登录失败");
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
                        Toast.makeText(WelcomeActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", getGson().fromJson(getSp().getString("mm_emp_mobile", ""), String.class));
                params.put("password", getGson().fromJson(getSp().getString("password", ""), String.class));
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

        Intent intent  =  new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
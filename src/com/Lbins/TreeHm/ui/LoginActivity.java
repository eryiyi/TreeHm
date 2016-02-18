package com.Lbins.TreeHm.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText mobile;
    private EditText password;

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
}

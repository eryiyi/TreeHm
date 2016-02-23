package com.Lbins.TreeHm.ui;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.base.ActivityTack;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.base.InternetURL;
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
 * Created by Administrator on 2016/2/23.
 */
public class UpdatePwrActivity extends BaseActivity implements View.OnClickListener {
    Resources res;
    private EditText mm_emp_mobile;
    private EditText code;
    private EditText password;
    private EditText surepass;
    private Button btn_code;
    private Button btn;


    //mob短信
    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "f8238165a882";//"69d6705af33d";0d786a4efe92bfab3d5717b9bc30a10d
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "7b3833871687dfa31baa880701907b4e";
    public String phString;//手机号码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pwr_activity);
        res = getResources();
        //mob短信无GUI
        SMSSDK.initSDK(this, APPKEY, APPSECRET, true);
        EventHandler eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);
        initView();
    }

    void initView(){
        mm_emp_mobile = (EditText) this.findViewById(R.id.mm_emp_mobile);
        code = (EditText) this.findViewById(R.id.code);
        password = (EditText) this.findViewById(R.id.password);
        surepass = (EditText) this.findViewById(R.id.surepass);
        btn_code = (Button) this.findViewById(R.id.btn_code);
        btn = (Button) this.findViewById(R.id.btn);

        btn_code.setOnClickListener(this);
        btn.setOnClickListener(this);
        this.findViewById(R.id.back).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_code:
                //验证码
                if(!TextUtils.isEmpty(mm_emp_mobile.getText().toString())){
                    SMSSDK.getVerificationCode("86", mm_emp_mobile.getText().toString());//发送请求验证码，手机10s之内会获得短信验证码
                    phString=mm_emp_mobile.getText().toString();
                    btn_code.setClickable(false);//不可点击
                    MyTimer myTimer = new MyTimer(60000,1000);
                    myTimer.start();
                }else {
                    showMsg(UpdatePwrActivity.this, "请输入手机号码");
                    return;
                }
                break;
            case R.id.btn:
                //确定
                if(StringUtil.isNullOrEmpty(mm_emp_mobile.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请输入手机号码");
                    return;
                }
                if(StringUtil.isNullOrEmpty(code.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请输入验证码");
                    return;
                }

                if(StringUtil.isNullOrEmpty(password.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请输入密码");
                    return;
                }
                if(password.getText().toString().length()>18 || password.getText().toString().length()<6){
                    showMsg(UpdatePwrActivity.this, "密码长度在6到18位之间");
                    return;
                }
                if(StringUtil.isNullOrEmpty(surepass.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "请输入确认密码");
                    return;
                }
                if(!password.getText().toString().equals(surepass.getText().toString())){
                    showMsg(UpdatePwrActivity.this, "两次输入密码不一致");
                    return;
                }
                progressDialog = new ProgressDialog(UpdatePwrActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                SMSSDK.submitVerificationCode("86", phString, code.getText().toString());
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn_code.setText(res.getString(R.string.daojishi_three));
            btn_code.setClickable(true);//可点击
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_code.setText(res.getString(R.string.daojishi_one) + millisUntilFinished / 1000 + res.getString(R.string.daojishi_two));
        }
    }

    void reg(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.UPDATE_PWR_URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    showMsg(UpdatePwrActivity.this, "修改密码成功");
                                    finish();
                                }
                                else {
                                    Toast.makeText(UpdatePwrActivity.this, "修改密码失败，请稍后重试", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(UpdatePwrActivity.this, "修改密码失败，请稍后重试", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UpdatePwrActivity.this, "修改密码失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mm_emp_mobile" , mm_emp_mobile.getText().toString());
                params.put("newpass" , password.getText().toString());
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


    Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                System.out.println("--------result"+event);
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
//                    Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                    reg();

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //已经验证
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }

            } else {
//				((Throwable) data).printStackTrace();
                Toast.makeText(UpdatePwrActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
//					Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();
                int status = 0;
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(UpdatePwrActivity.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }


        };
    };


    public void onDestroy() {
        super.onPause();
        SMSSDK.unregisterAllEventHandler();
    };

}

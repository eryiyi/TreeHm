package com.Lbins.TreeHm.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.*;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.data.CityData;
import com.Lbins.TreeHm.data.CountrysData;
import com.Lbins.TreeHm.data.ProvinceData;
import com.Lbins.TreeHm.module.CityObj;
import com.Lbins.TreeHm.module.CountryObj;
import com.Lbins.TreeHm.module.ProvinceObj;
import com.Lbins.TreeHm.util.StringUtil;
import com.Lbins.TreeHm.widget.CustomerSpinner;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/18.
 */
public class RegistActivity extends BaseActivity implements View.OnClickListener {
    Resources res;
    private EditText mm_emp_mobile;
    private EditText mm_emp_company;
    private EditText code;
    private EditText password;
    private EditText surepass;
    private EditText mm_emp_nickname;
    private EditText mm_emp_company_address;
    private Button btn_code;
    private Button btn;

    private CustomerSpinner empTypeSpinner;
    ArrayAdapter<String> adapterEmpType;
    private ArrayList<String> empTypeList = new ArrayList<String>();
    private String mm_emp_type;//注册类型   用户

     private CustomerSpinner companyTypeSpinner;
    ArrayAdapter<String> adapterCompanyType;
    private ArrayList<String> companyTypeList = new ArrayList<String>();
    private String mm_emp_company_type;//注册类型  公司


    //省市县
    private CustomerSpinner province;
    private CustomerSpinner city;
    private CustomerSpinner country;
    private List<ProvinceObj> provinces = new ArrayList<ProvinceObj>();//省
    private ArrayList<String> provinceNames = new ArrayList<String>();//省份名称
    private List<CityObj> citys = new ArrayList<CityObj>();//市
    private ArrayList<String> cityNames = new ArrayList<String>();//市名称
    private List<CountryObj> countrys = new ArrayList<CountryObj>();//区
    private ArrayList<String> countrysNames = new ArrayList<String>();//区名称
    ArrayAdapter<String> ProvinceAdapter;
    ArrayAdapter<String> cityAdapter;
    ArrayAdapter<String> countryAdapter;
    private String provinceName = "";
    private String cityName = "";
    private String countryName = "";
    private String provinceCode = "";
    private String cityCode = "";
    private String countryCode = "";


    //mob短信
    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "f8238165a882";//"69d6705af33d";0d786a4efe92bfab3d5717b9bc30a10d
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "7b3833871687dfa31baa880701907b4e";
    public String phString;//手机号码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_activity);
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

        getProvince();
    }

    void initView(){
        mm_emp_mobile = (EditText) this.findViewById(R.id.mm_emp_mobile);
        code = (EditText) this.findViewById(R.id.code);
        password = (EditText) this.findViewById(R.id.password);
        surepass = (EditText) this.findViewById(R.id.surepass);
        mm_emp_company = (EditText) this.findViewById(R.id.mm_emp_company);
        mm_emp_nickname = (EditText) this.findViewById(R.id.mm_emp_nickname);
        mm_emp_company_address = (EditText) this.findViewById(R.id.mm_emp_company_address);
        btn_code = (Button) this.findViewById(R.id.btn_code);
        btn = (Button) this.findViewById(R.id.btn);

        btn_code.setOnClickListener(this);
        btn.setOnClickListener(this);
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.reg_msg).setOnClickListener(this);

        //个人注册类型
        empTypeSpinner = (CustomerSpinner) this.findViewById(R.id.mm_emp_type);
        empTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mm_emp_type = empTypeList.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        empTypeList.add("请选择注册类型");
        empTypeList.add("苗木经营");
        empTypeList.add("苗木会员");
        adapterEmpType = new ArrayAdapter<String>(RegistActivity.this, android.R.layout.simple_spinner_item, empTypeList);
        empTypeSpinner.setList(empTypeList);
        empTypeSpinner.setAdapter(adapterEmpType);

        //公司注册类型
        companyTypeSpinner = (CustomerSpinner) this.findViewById(R.id.mm_emp_company_type);
        companyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mm_emp_company_type = companyTypeList.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        companyTypeList.add("请选择公司类型");
        companyTypeList.add("苗木");
        companyTypeList.add("园林");
        adapterCompanyType = new ArrayAdapter<String>(RegistActivity.this, android.R.layout.simple_spinner_item, companyTypeList);
        companyTypeSpinner.setList(companyTypeList);
        companyTypeSpinner.setAdapter(adapterCompanyType);

        ProvinceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, provinceNames);
        ProvinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province = (CustomerSpinner) findViewById(R.id.mm_emp_provinceId);
        province.setAdapter(ProvinceAdapter);
        province.setList(provinceNames);
        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citys.clear();
                cityNames.clear();
                cityNames.add(getResources().getString(R.string.select_city));
                cityAdapter.notifyDataSetChanged();
                ProvinceObj province = null;
                if(provinces != null && provinces.size() > 1 && position > 1){
                    province = provinces.get(position-1);
                    provinceName = province.getProvince();
                    provinceCode = province.getProvinceID();
                }else if(provinces != null ) {
                    province = provinces.get(position);
                    provinceName = province.getProvince();
                    provinceCode = province.getProvinceID();
                }
                try {
                    getCitys();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityNames);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city = (CustomerSpinner) findViewById(R.id.mm_emp_cityId);
        city.setAdapter(cityAdapter);
        city.setList(cityNames);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    countrys.clear();
                    countrysNames.clear();
                    countrysNames.add(getResources().getString(R.string.select_area));
                    CityObj city = citys.get(position-1);
                    cityName = city.getCity();
                    cityCode = city.getCityID();
                    try {
                        getArea();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    country.setEnabled(true);
                    countrysNames.clear();
                    countrysNames.add(res.getString(R.string.select_area));
                    countrys.clear();
                    countryAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countrysNames);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country = (CustomerSpinner) findViewById(R.id.mm_emp_countryId);
        country.setAdapter(countryAdapter);
        country.setList(countrysNames);
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    CountryObj country = countrys.get(position - 1);
                    countryCode = country.getAreaID();
                    countryName = country.getArea();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reg_msg:
                //注册协议
                Intent regMsg = new Intent(RegistActivity.this, RegistMsgActivity.class);
                startActivity(regMsg);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.btn_code:
                //验证码
                if(!TextUtils.isEmpty(mm_emp_mobile.getText().toString())){
                    SMSSDK.getVerificationCode("86",mm_emp_mobile.getText().toString());//发送请求验证码，手机10s之内会获得短信验证码
                    phString=mm_emp_mobile.getText().toString();
                    btn_code.setClickable(false);//不可点击
                    MyTimer myTimer = new MyTimer(60000,1000);
                    myTimer.start();
                }else {
                    showMsg(RegistActivity.this, "请输入手机号码");
                    return;
                }
                break;
            case R.id.btn:
                //确定
                if(StringUtil.isNullOrEmpty(mm_emp_mobile.getText().toString())){
                    showMsg(RegistActivity.this, "请输入手机号码");
                    return;
                }
                if(StringUtil.isNullOrEmpty(code.getText().toString())){
                    showMsg(RegistActivity.this, "请输入验证码");
                    return;
                }

                if(StringUtil.isNullOrEmpty(password.getText().toString())){
                    showMsg(RegistActivity.this, "请输入密码");
                    return;
                }
                if(password.getText().toString().length()>18 || password.getText().toString().length()<6){
                    showMsg(RegistActivity.this, "密码长度在6到18位之间");
                    return;
                }
                if(StringUtil.isNullOrEmpty(surepass.getText().toString())){
                    showMsg(RegistActivity.this, "请输入确认密码");
                    return;
                }
                if(!password.getText().toString().equals(surepass.getText().toString())){
                    showMsg(RegistActivity.this, "两次输入密码不一致");
                    return;
                }
                if(StringUtil.isNullOrEmpty(mm_emp_nickname.getText().toString())){
                    showMsg(RegistActivity.this, "请输入姓名");
                    return;
                }
                if(StringUtil.isNullOrEmpty(mm_emp_type) || "请选择注册类型".equals(mm_emp_type)){
                    showMsg(RegistActivity.this, "请选择注册类型");
                    return;
                }
                if(StringUtil.isNullOrEmpty(mm_emp_company.getText().toString())){
                    showMsg(RegistActivity.this, "请输公司名称");
                    return;
                }
                if(StringUtil.isNullOrEmpty(mm_emp_company_type) || "请选择公司类型".equals(mm_emp_company_type)){
                    showMsg(RegistActivity.this, "请选择公司类型");
                    return;
                }
                if(StringUtil.isNullOrEmpty(mm_emp_company_address.getText().toString())){
                    showMsg(RegistActivity.this, "请输公司地址");
                    return;
                }
                if(StringUtil.isNullOrEmpty(provinceCode)){
                    showMsg(RegistActivity.this, "请选择省份");
                    return;
                }
                if(StringUtil.isNullOrEmpty(cityCode)){
                    showMsg(RegistActivity.this, "请选择城市");
                    return;
                }
                if(StringUtil.isNullOrEmpty(countryCode)){
                    showMsg(RegistActivity.this, "请选择县区");
                    return;
                }
                reg();
//                SMSSDK.submitVerificationCode("86", phString, code.getText().toString());
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
                InternetURL.REG_URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    showMsg(RegistActivity.this, "注册成功，请登录");
                                    finish();
                                }else if(Integer.parseInt(code) == 1){
                                    showMsg(RegistActivity.this, "注册失败，请稍后重试");
                                }else if(Integer.parseInt(code) == 2){
                                    showMsg(RegistActivity.this, "该手机号已经注册，请直接登录");
                                }
                                else {
                                    Toast.makeText(RegistActivity.this, R.string.reg_error_one , Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(RegistActivity.this, R.string.reg_error_one, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(RegistActivity.this, R.string.reg_error_one, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mm_emp_mobile" , mm_emp_mobile.getText().toString());
                params.put("mm_emp_nickname" , mm_emp_nickname.getText().toString());
                params.put("mm_emp_password" , password.getText().toString());
                params.put("mm_emp_company" , mm_emp_company.getText().toString());
                params.put("mm_emp_company_address" , mm_emp_company_address.getText().toString());
                if("苗木经营".equals(mm_emp_type)){
                    params.put("mm_emp_type" , "0");
                }
                if("苗木会员".equals(mm_emp_type)){
                    params.put("mm_emp_type" , "1");
                }
                if("苗木".equals(mm_emp_company_type)){
                    params.put("mm_emp_company_type" , "0");
                }
                if("园林".equals(mm_emp_company_type)){
                    params.put("mm_emp_company_type" , "1");
                }
                params.put("mm_emp_company_detail" , "");
                params.put("mm_emp_provinceId" , provinceCode);
                params.put("mm_emp_cityId" , cityCode);
                params.put("mm_emp_countryId" , countryCode);
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
				Toast.makeText(RegistActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
//					Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();
                int status = 0;
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(RegistActivity.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }


        };
    };

    public void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
    };

    //获得省份
    public void getProvince(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_PROVINCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    provinceNames.add("请选择省份");
                                    ProvinceData data = getGson().fromJson(s, ProvinceData.class);
                                    provinces = data.getData();
                                    if(provinces != null){
                                        for(int i=0;i<provinces.size();i++){
                                            provinceNames.add(provinces.get(i).getProvince());
                                        }
                                    }
                                    ProvinceAdapter.notifyDataSetChanged();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(RegistActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegistActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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

    //获得城市
    public void getCitys(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_CITY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    CityData data = getGson().fromJson(s, CityData.class);
                                    citys = data.getData();
                                    if(citys != null){
                                        for(int i=0;i<citys.size();i++){
                                            cityNames.add(citys.get(i).getCity());
                                        }
                                    }
                                    cityAdapter.notifyDataSetChanged();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(RegistActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegistActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("access_token", getGson().fromJson(getSp().getString("access_token", ""), String.class));
                params.put("father", provinceCode);
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

    //获得地区
    public void getArea(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_COUNTRY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    CountrysData data = getGson().fromJson(s, CountrysData.class);
                                    countrys = data.getData();
                                    if(countrys != null){
                                        for(int i=0;i<countrys.size();i++){
                                            countrysNames.add(countrys.get(i).getArea());
                                        }
                                    }
                                    countryAdapter.notifyDataSetChanged();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(RegistActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegistActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("father", cityCode);
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

package com.Lbins.TreeHm.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.Lbins.TreeHm.MainActivity;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.base.ActivityTack;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.widget.CustomerSpinner;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private CustomerSpinner textSize;
    ArrayAdapter<String> adapterEmpType;
    private ArrayList<String> empTypeList = new ArrayList<String>();
    private String mm_emp_type;//

    private CustomerSpinner textColor;
    ArrayAdapter<String> adapterCompanyType;
    private ArrayList<String> companyTypeList = new ArrayList<String>();
    private String mm_emp_company_type;//

    private ImageView switch_shengyin;
    private ImageView switch_zhendong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        initView();
    }

    private void initView() {
        switch_shengyin = (ImageView) this.findViewById(R.id.switch_shengyin);
        switch_zhendong = (ImageView) this.findViewById(R.id.switch_zhendong);
        switch_shengyin.setOnClickListener(this);
        switch_zhendong.setOnClickListener(this);
        this.findViewById(R.id.back).setOnClickListener(this);
        textSize = (CustomerSpinner) this.findViewById(R.id.textSize);
        textColor = (CustomerSpinner) this.findViewById(R.id.textColor);

        //个人注册类型
        textSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mm_emp_type = empTypeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        empTypeList.add("正常字体");
        empTypeList.add("小号字体");
        empTypeList.add("中号字体");
        empTypeList.add("大号字体");
        empTypeList.add("超大号字体");
        adapterEmpType = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_spinner_item, empTypeList);
        textSize.setList(empTypeList);
        textSize.setAdapter(adapterEmpType);


        textColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mm_emp_company_type = companyTypeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        companyTypeList.add("黑色");
        companyTypeList.add("灰色");
        companyTypeList.add("蓝色");
        companyTypeList.add("橙色");
        companyTypeList.add("红色");
        adapterCompanyType = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_spinner_item, companyTypeList);
        textColor.setList(companyTypeList);
        textColor.setAdapter(adapterCompanyType);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.switch_zhendong:
                if("1".equals(getGson().fromJson(getSp().getString("switch_zhendong", ""), String.class))){
                    //打开的
                    switch_zhendong.setImageResource(R.drawable.switch_close);
                    save("switch_shengyin", "0");
                }else {
                    switch_zhendong.setImageResource(R.drawable.switch_open);
                    save("switch_zhendong", "1");
                }

                break;
            case R.id.switch_shengyin:
                if("1".equals(getGson().fromJson(getSp().getString("switch_shengyin", ""), String.class))){
                    //打开的
                    switch_shengyin.setImageResource(R.drawable.switch_close);
                    save("switch_shengyin", "0");
                }else {
                    switch_shengyin.setImageResource(R.drawable.switch_open);
                    save("switch_shengyin", "1");
                }

                break;
        }
    }

    public void surequite(View view){
        AlertDialog dialog = new AlertDialog.Builder(SettingActivity.this)
                .setIcon(R.drawable.logo)
                .setTitle("确定退出花木通吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        save("password" ,"");
                        save("isLogin" ,"0");
                        //调用广播，刷新主页
                        Intent intent1 = new Intent("sure_quite");
                        sendBroadcast(intent1);
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        dialog.show();
    }
}

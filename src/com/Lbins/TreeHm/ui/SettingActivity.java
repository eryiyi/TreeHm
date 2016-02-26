package com.Lbins.TreeHm.ui;

import android.app.AlertDialog;
import android.content.*;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.UniversityApplication;
import com.Lbins.TreeHm.base.BaseActivity;

import com.Lbins.TreeHm.module.SetFontColor;
import com.Lbins.TreeHm.module.SetFontSize;
import com.Lbins.TreeHm.util.StringUtil;
import com.Lbins.TreeHm.widget.CustomerSpinner;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/2/19.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private CustomerSpinner textSize;
    ArrayAdapter<String> adapterEmpType;
    private ArrayList<SetFontSize> empTypeList = new ArrayList<SetFontSize>();
    private ArrayList<String> empTypeListStr = new ArrayList<String>();
    private SetFontSize mm_emp_type;//

    private CustomerSpinner textColor;
    ArrayAdapter<String> adapterCompanyType;
    private ArrayList<SetFontColor> companyTypeList = new ArrayList<SetFontColor>();
    private ArrayList<String> companyTypeListStr = new ArrayList<String>();
    private SetFontColor mm_emp_company_type;//

    private ImageView switch_shengyin;
    private ImageView switch_zhendong;

    private TextView fontsize_text;
    private TextView fontcolor_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        registerBoradcastReceiver();
        initView();
        changeColorOrSize();
    }

    private void initView() {
        fontsize_text = (TextView) this.findViewById(R.id.fontsize_text);
        fontcolor_text = (TextView) this.findViewById(R.id.fontcolor_text);

        switch_shengyin = (ImageView) this.findViewById(R.id.switch_shengyin);
        switch_zhendong = (ImageView) this.findViewById(R.id.switch_zhendong);
        switch_shengyin.setOnClickListener(this);
        switch_zhendong.setOnClickListener(this);
        this.findViewById(R.id.back).setOnClickListener(this);
        textSize = (CustomerSpinner) this.findViewById(R.id.textSize);
        textColor = (CustomerSpinner) this.findViewById(R.id.textColor);

        empTypeList.add(new SetFontSize("正常字体", "16"));
        empTypeList.add(new SetFontSize("小号字体", "14"));
        empTypeList.add(new SetFontSize("中号字体", "18"));
        empTypeList.add(new SetFontSize("大号字体", "22"));
        empTypeList.add(new SetFontSize("超大号字体", "26"));
        empTypeListStr.add("正常字体");
        empTypeListStr.add("小号字体");
        empTypeListStr.add("中号字体");
        empTypeListStr.add("大号字体");
        empTypeListStr.add("超大号字体");
        adapterEmpType = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_spinner_item, empTypeListStr);
        textSize.setList(empTypeListStr);
        textSize.setAdapter(adapterEmpType);

        companyTypeList.add(new SetFontColor("黑色", "black"));
        companyTypeList.add(new SetFontColor("灰色", "gray"));
        companyTypeList.add(new SetFontColor("蓝色", "blue"));
        companyTypeList.add(new SetFontColor("橙色", "orange"));
        companyTypeList.add(new SetFontColor("红色","red"));
        companyTypeListStr.add("黑色");
        companyTypeListStr.add("灰色");
        companyTypeListStr.add("蓝色");
        companyTypeListStr.add("橙色");
        companyTypeListStr.add("红色");
        adapterCompanyType = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_spinner_item, companyTypeListStr);
        textColor.setList(companyTypeListStr);
        textColor.setAdapter(adapterCompanyType);

        textColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mm_emp_company_type = companyTypeList.get(position);
                save("font_color", mm_emp_company_type.getFontColor());
                //调用广播，刷新主页
                Intent intent1 = new Intent("change_color_size");
                sendBroadcast(intent1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        textSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mm_emp_type = empTypeList.get(position);
                save("font_size", mm_emp_type.getSizeStr());
                //调用广播，刷新主页
                Intent intent1 = new Intent("change_color_size");
                sendBroadcast(intent1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

    void changeColorOrSize(){
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("font_size", ""), String.class))){
            fontsize_text.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            fontcolor_text.setTextSize(Float.valueOf(getGson().fromJson(getSp().getString("font_size", ""), String.class)));
            if("16".equals(getGson().fromJson(getSp().getString("font_size", ""), String.class))){
                textSize.setSelection(0, true);
            }
            if("14".equals(getGson().fromJson(getSp().getString("font_size", ""), String.class))){
                textSize.setSelection(1, true);
            }
            if("18".equals(getGson().fromJson(getSp().getString("font_size", ""), String.class))){
                textSize.setSelection(2, true);
            }
            if("22".equals(getGson().fromJson(getSp().getString("font_size", ""), String.class))){
                textSize.setSelection(3, true);
            }
            if("26".equals(getGson().fromJson(getSp().getString("font_size", ""), String.class))){
                textSize.setSelection(4, true);
            }

        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("font_color", ""), String.class))){
            if("black".equals(getGson().fromJson(getSp().getString("font_color", ""), String.class))){
                fontsize_text.setTextColor(Color.BLACK);
                fontcolor_text.setTextColor(Color.BLACK);
                textColor.setSelection(0,true);
            }
            if("gray".equals(getGson().fromJson(getSp().getString("font_color", ""), String.class))){
                fontsize_text.setTextColor(Color.GRAY);
                fontcolor_text.setTextColor(Color.GRAY);
                textColor.setSelection(1,true);
            }
            if("blue".equals(getGson().fromJson(getSp().getString("font_color", ""), String.class))){
                fontsize_text.setTextColor(Color.BLUE);
                fontcolor_text.setTextColor(Color.BLUE);
                textColor.setSelection(2,true);
            }
            if("orange".equals(getGson().fromJson(getSp().getString("font_color", ""), String.class))){
                fontsize_text.setTextColor(Color.YELLOW);
                fontcolor_text.setTextColor(Color.YELLOW);
                textColor.setSelection(3,true);
            }
            if("red".equals(getGson().fromJson(getSp().getString("font_color", ""), String.class))){
                fontsize_text.setTextColor(Color.RED);
                fontcolor_text.setTextColor(Color.RED);
                textColor.setSelection(4,true);
            }

        }
    }


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("change_color_size")){
                changeColorOrSize();
                //控制字体 颜色和大小
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("font_size", ""), String.class))){
                    UniversityApplication.fontSize = getGson().fromJson(getSp().getString("font_size", ""), String.class);
                }
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("font_color", ""), String.class))){
                    UniversityApplication.fontColor = getGson().fromJson(getSp().getString("font_color", ""), String.class);
                }
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("change_color_size");//
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

}

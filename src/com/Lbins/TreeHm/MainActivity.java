package com.Lbins.TreeHm;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.data.GuanzhuAreaObjData;
import com.Lbins.TreeHm.fragment.*;
import com.Lbins.TreeHm.module.GuanzhuAreaObj;
import com.Lbins.TreeHm.ui.AddRecordActivity;
import com.Lbins.TreeHm.ui.Constants;
import com.Lbins.TreeHm.ui.LoginActivity;
import com.Lbins.TreeHm.ui.RegistActivity;
import com.Lbins.TreeHm.util.HttpUtils;
import com.Lbins.TreeHm.util.StringUtil;
import com.Lbins.TreeHm.widget.MainPopMenu;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.umeng.update.UmengUpdateAgent;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements View.OnClickListener,MainPopMenu.OnItemClickListener{
    /**
     * Called when the activity is first created.
     */
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fm;

    private FirstFragment oneFragment;
    private SecondFragment twoFragment;
    private TopFragment threeFragment;
    private FourFragment fourFragment;

    private ImageView foot_one;
    private ImageView foot_two;
    private ImageView foot_three;
    private ImageView foot_four;

    private long waitTime = 2000;
    private long touchTime = 0;

    //设置底部图标
    Resources res;

    private int index;
    public MainPopMenu mainPopMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        UmengUpdateAgent.update(this);
        //获得上一次登陆时间

        //保存这次登陆时间
        save("denglu_time", System.currentTimeMillis() + "");
        res = getResources();
        fm = getSupportFragmentManager();
        initView();

        switchFragment(R.id.foot_one);

        //控制字体 颜色和大小
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("font_size", ""), String.class))){
            UniversityApplication.fontSize = getGson().fromJson(getSp().getString("font_size", ""), String.class);
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("font_color", ""), String.class))){
            UniversityApplication.fontColor = getGson().fromJson(getSp().getString("font_color", ""), String.class);
        }

        mainPopMenu = new MainPopMenu(this);
        mainPopMenu.setOnItemClickListener(this);


    }

    boolean isMobileNet, isWifiNet;

    @Override
    public void onClick(View v) {
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
        if((StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) || "0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))) &&  (v.getId() == R.id.foot_four)){
            //未登录
            showLogin();
        }else {
            switchFragment(v.getId());
        }

    }

    // 登陆注册选择窗口
    private void showLogin() {
        final Dialog picAddDialog = new Dialog(MainActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(MainActivity.this, R.layout.login_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        final TextView jubao_cont = (TextView) picAddInflate.findViewById(R.id.jubao_cont);
        jubao_cont.setText(getResources().getString(R.string.please_reg_or_login));
        //登陆
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginV = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginV);
                picAddDialog.dismiss();
            }
        });
        //注册
        TextView btn_cancel = (TextView) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginV = new Intent(MainActivity.this, RegistActivity.class);
                startActivity(loginV);
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    private void initView() {
        foot_one = (ImageView) this.findViewById(R.id.foot_one);
        foot_two = (ImageView) this.findViewById(R.id.foot_two);
        foot_three = (ImageView) this.findViewById(R.id.foot_three);
        foot_four = (ImageView) this.findViewById(R.id.foot_four);
        foot_one.setOnClickListener(this);
        foot_two.setOnClickListener(this);
        foot_three.setOnClickListener(this);
        foot_four.setOnClickListener(this);

    }


    public void switchFragment(int id) {
        fragmentTransaction = fm.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (id) {
            case R.id.foot_one:
                if (oneFragment == null) {
                    oneFragment = new FirstFragment();
                    fragmentTransaction.add(R.id.content_frame, oneFragment);
                } else {
                    fragmentTransaction.show(oneFragment);
                }
                foot_one.setImageResource(R.drawable.tree_toolbar_wanted_p);
                foot_two.setImageResource(R.drawable.tree_toolbar_sell);
                foot_three.setImageResource(R.drawable.tree_toolbar_star);
                foot_four.setImageResource(R.drawable.tree_toolbar_user);

                break;
            case R.id.foot_two:
                if (twoFragment == null) {
                    twoFragment = new SecondFragment();
                    fragmentTransaction.add(R.id.content_frame, twoFragment);
                } else {
                    fragmentTransaction.show(twoFragment);
                }

                foot_one.setImageResource(R.drawable.tree_toolbar_wanted);
                foot_two.setImageResource(R.drawable.tree_toolbar_sell_p);
                foot_three.setImageResource(R.drawable.tree_toolbar_star);
                foot_four.setImageResource(R.drawable.tree_toolbar_user);
                break;
            case R.id.foot_three:
                if (threeFragment == null) {
                    threeFragment = new TopFragment();
                    fragmentTransaction.add(R.id.content_frame, threeFragment);
                } else {
                    fragmentTransaction.show(threeFragment);
                }
                foot_one.setImageResource(R.drawable.tree_toolbar_wanted);
                foot_two.setImageResource(R.drawable.tree_toolbar_sell);
                foot_three.setImageResource(R.drawable.tree_toolbar_star_p);
                foot_four.setImageResource(R.drawable.tree_toolbar_user);
                break;
            case R.id.foot_four:
                if (fourFragment == null) {
                    fourFragment = new FourFragment();
                    fragmentTransaction.add(R.id.content_frame, fourFragment);
                } else {
                    fragmentTransaction.show(fourFragment);
                }
                foot_one.setImageResource(R.drawable.tree_toolbar_wanted);
                foot_two.setImageResource(R.drawable.tree_toolbar_sell);
                foot_three.setImageResource(R.drawable.tree_toolbar_star);
                foot_four.setImageResource(R.drawable.tree_toolbar_user_p);
                break;

        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction ft) {
        if (oneFragment != null) {
            ft.hide(oneFragment);
        }
        if (twoFragment != null) {
            ft.hide(twoFragment);
        }
        if (threeFragment != null) {
            ft.hide(threeFragment);
        }
        if (fourFragment != null) {
            ft.hide(fourFragment);
        }
    }



    //弹出顶部主菜单
    public void onTopMenuPopupButtonClick(View view) {
        mainPopMenu.showAsDropDown(view);
    }

    @Override
    public void onItemClick(int index) {
        switch (index){
            case 0:
                //发布信息
                if((StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) || "0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class)))){
                    //未登录
                    showLogin();
                }else {
                    Intent addV = new Intent(MainActivity.this, AddRecordActivity.class);
                    startActivity(addV);
                }
                break;
            case 1:
                //一键关注区域
                if((StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) || "0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class)))){
                    //未登录
                    showLogin();
                }else {
                    getGuanzhuArea();
                }

                break;
        }
    }

    //查询关注区域
    public void getGuanzhuArea(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_GUANZHU_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    GuanzhuAreaObjData data = getGson().fromJson(s, GuanzhuAreaObjData.class);
                                    if(data.getData() != null && data.getData().size() > 0){
                                        //说明已经申请了
                                        List<GuanzhuAreaObj> listgz = data.getData();
                                        if(listgz != null && listgz.size()>0){
                                            GuanzhuAreaObj guanzhuAreaObj = listgz.get(0);
                                            if(guanzhuAreaObj != null){
                                                if("0".equals(guanzhuAreaObj.getIscheck())){
                                                    showMsg(MainActivity.this, "您已经设置关注区域，不能重复设置！请等待管理员审核");
                                                }
                                                if("1".equals(guanzhuAreaObj.getIscheck())){
//                                                    showMsg(MainActivity.this, "您已经设置关注区域，不能重复设置！");
                                                    //调用广播，刷新主页
                                                    Intent intent1 = new Intent("change_guanzhu_area");
                                                    save("gz_areaId", guanzhuAreaObj.getAreaid());
                                                    sendBroadcast(intent1);
                                                }
                                                if("2".equals(guanzhuAreaObj.getIscheck())){
                                                    showMsg(MainActivity.this, "您申请的关注区域未通过审核，请联系管理员！");
                                                }
                                            }
                                        }
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(MainActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mm_emp_id", getGson().fromJson(getSp().getString("mm_emp_id", ""), String.class));
                params.put("index", "1");
                params.put("size", "10");
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

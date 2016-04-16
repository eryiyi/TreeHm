package com.Lbins.TreeHm.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.*;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.adapter.ItemGuanzhuAdapter;
import com.Lbins.TreeHm.adapter.ItemProvinceAdapter;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.data.GuanzhuAreaObjData;
import com.Lbins.TreeHm.data.ProvinceData;
import com.Lbins.TreeHm.library.internal.PullToRefreshBase;
import com.Lbins.TreeHm.library.internal.PullToRefreshListView;
import com.Lbins.TreeHm.module.GuanzhuAreaObj;
import com.Lbins.TreeHm.module.ProvinceObj;
import com.Lbins.TreeHm.util.StringUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/19.
 */
public class SelectProvinceActivity extends BaseActivity implements View.OnClickListener {
    private GridView lstv;
    private ItemProvinceAdapter adapter;
    private List<ProvinceObj> lists = new ArrayList<ProvinceObj>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    private GridView lstvGz;
    private TextView no_data_text;
    private String[] areaNames = new String[10];
    private String[] areaIds = new String[10];
    private List<String> areaNamesList = new ArrayList<String>();

    private ItemGuanzhuAdapter adapterGz;
    GuanzhuAreaObj guanzhuAreaObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_province);
        initView();
        initData();
        getGuanzhuArea();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        lstvGz = (GridView) this.findViewById(R.id.lstvGz);
        no_data_text = (TextView) this.findViewById(R.id.no_data_text);
        adapterGz = new ItemGuanzhuAdapter(areaNamesList, SelectProvinceActivity.this);
        lstvGz.setAdapter(adapterGz);
        lstvGz.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lstvGz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(areaIds != null && areaIds.length >i){
                    String idPostion = areaIds[i];
                    String name = areaNames[i];
                    Intent intent = new Intent(SelectProvinceActivity.this, RecordGzActivity.class);
                    intent.putExtra("guanzhuAreaObj", guanzhuAreaObj);
                    intent.putExtra("idPostion", idPostion);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
            }
        });

        lstv = (GridView) this.findViewById(R.id.lstv);
        adapter = new ItemProvinceAdapter(lists, SelectProvinceActivity.this);
//        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setAdapter(adapter);
        lstv.setSelector(new ColorDrawable(Color.TRANSPARENT));
//        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                String label = DateUtils.formatDateTime(SelectProvinceActivity.this, System.currentTimeMillis(),
//                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//
//                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//                IS_REFRESH = true;
//                pageIndex = 1;
//                if( "1".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))){
//                    initData();
//                }else {
//                    lstv.onRefreshComplete();
//                    //未登录
//                    Intent loginV = new Intent(SelectProvinceActivity.this, LoginActivity.class);
//                    startActivity(loginV);
//                }
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//                String label = DateUtils.formatDateTime(SelectProvinceActivity.this, System.currentTimeMillis(),
//                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//
//                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//                IS_REFRESH = false;
//                pageIndex++;
//                if( "1".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))){
//                    initData();
//                }else {
//                    lstv.onRefreshComplete();
//                    //未登录
//                    Intent loginV = new Intent(SelectProvinceActivity.this, LoginActivity.class);
//                    startActivity(loginV);
//                }
//            }
//        });

        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent cityV = new Intent(SelectProvinceActivity.this, CityAreaActivity.class);
                ProvinceObj provinceObj = lists.get(position);
                cityV.putExtra("provinceObj", provinceObj);
                startActivity(cityV);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
    public void initData(){
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
                                    ProvinceData data = getGson().fromJson(s, ProvinceData.class);
                                    lists.clear();
                                    lists.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(SelectProvinceActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SelectProvinceActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("is_use", "1");
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
                                            guanzhuAreaObj = listgz.get(0);
                                            if(guanzhuAreaObj != null){
                                                if("0".equals(guanzhuAreaObj.getIscheck())){
//                                                    showMsg(SelectProvinceActivity.this, "您已经申请了关注区域！请等待管理员审核");
                                                    no_data_text.setText(getResources().getString(R.string.also_area_please_wait));
                                                    no_data_text.setClickable(false);
                                                }else
                                                if("1".equals(guanzhuAreaObj.getIscheck())){
//                                                    Intent intent = new Intent(SelectProvinceActivity.this, RecordGzActivity.class);
//                                                    intent.putExtra("guanzhuAreaObj", guanzhuAreaObj);
//                                                    startActivity(intent);
                                                    areaNames = guanzhuAreaObj.getArea_name().split(",");
                                                    areaIds = guanzhuAreaObj.getAreaid().split(",");
                                                    for(String str:areaNames){
                                                        areaNamesList.add(str);
                                                    }
                                                    adapterGz.notifyDataSetChanged();

                                                }else
                                                if("2".equals(guanzhuAreaObj.getIscheck())){
//                                                    showMsg(SelectProvinceActivity.this, "您申请的关注区域未通过审核，请联系客服！");
                                                    no_data_text.setText(getResources().getString(R.string.also_area_please_wait1));
                                                    no_data_text.setClickable(false);
                                                }else{
//                                                    showMsg(SelectProvinceActivity.this, "您尚未申请关注区域，请设置关注区域！");
                                                    no_data_text.setText(getResources().getString(R.string.also_area_please_wait2));
                                                    no_data_text.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Intent guanzhuV = new Intent(SelectProvinceActivity.this, SetGuanzhuActivity.class);
                                                            startActivity(guanzhuV);
                                                        }
                                                    });
                                                }
                                            }
                                        }else{
                                            no_data_text.setText(getResources().getString(R.string.also_area_please_wait2));
                                            no_data_text.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent guanzhuV = new Intent(SelectProvinceActivity.this, SetGuanzhuActivity.class);
                                                    startActivity(guanzhuV);
                                                }
                                            });
                                        }
                                    }else{
                                        no_data_text.setText(getResources().getString(R.string.also_area_please_wait2));
                                        no_data_text.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent guanzhuV = new Intent(SelectProvinceActivity.this, SetGuanzhuActivity.class);
                                                startActivity(guanzhuV);
                                            }
                                        });
                                    }
                                }else {
                                    no_data_text.setText(getResources().getString(R.string.also_area_please_wait2));
                                    no_data_text.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent guanzhuV = new Intent(SelectProvinceActivity.this, SetGuanzhuActivity.class);
                                            startActivity(guanzhuV);
                                        }
                                    });
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(SelectProvinceActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SelectProvinceActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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

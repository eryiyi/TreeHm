package com.Lbins.TreeHm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.adapter.ItemVipAdapter;
import com.Lbins.TreeHm.adapter.OnClickContentItemListener;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.data.FeiyongData;
import com.Lbins.TreeHm.module.FeiyongObj;
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
 * Created by Administrator on 2016/2/23.
 */
public class VipActivity extends BaseActivity implements View.OnClickListener ,OnClickContentItemListener{
    private ImageView no_data;
    private ListView lstv;
    private ItemVipAdapter adapter;
    List<FeiyongObj> lists ;

    private TextView msg_time;
    private TextView msg_jine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_activity);

        lists = new ArrayList<FeiyongObj>();
        this.findViewById(R.id.back).setOnClickListener(this);
        no_data = (ImageView) this.findViewById(R.id.no_data);
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemVipAdapter(lists, VipActivity.this);
        adapter.setOnClickContentItemListener(this);
        lstv.setAdapter(adapter);
        initData();
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detailV = new Intent(VipActivity.this, VipDetailActivity.class);
                FeiyongObj feiyongObj1 = lists.get(i);
                detailV.putExtra("level_id", feiyongObj1.getMm_level_id());
                startActivity(detailV);
            }
        });
        msg_time = (TextView) this.findViewById(R.id.msg_time);
        msg_jine = (TextView) this.findViewById(R.id.msg_jine);
    }

    public void initData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_VIP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    FeiyongData data = getGson().fromJson(s, FeiyongData.class);
                                    lists.clear();
                                    lists.addAll(data.getData());
                                    if(lists != null && lists.size()>0){
                                        lists.get(0).setIs_select("1");
                                    }
                                    toC();
                                    adapter.notifyDataSetChanged();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(VipActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if(lists.size()>0){
                            no_data.setVisibility(View.GONE);
                            lstv.setVisibility(View.VISIBLE);
                        }else {
                            no_data.setVisibility(View.VISIBLE);
                            lstv.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(VipActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
    FeiyongObj feiyongObj;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
                feiyongObj = lists.get(position);
                for(int i=0;i<lists.size();i++){
                    FeiyongObj cell = lists.get(i);
                    if(cell.getMm_feiyong_id().equals(feiyongObj.getMm_feiyong_id())){
                        //如果是当前的
                        lists.get(i).setIs_select("1");
                        msg_time.setText(feiyongObj.getMm_feiyong_name());
                        msg_jine.setText("￥"+feiyongObj.getMm_feiyong_jine());
                    }else{
                        lists.get(i).setIs_select("0");
                    }
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }

    public void goToPay(View view){
        //
        if(StringUtil.isNullOrEmpty(msg_jine.getText().toString())){
            showMsg(VipActivity.this, getResources().getString(R.string.please_select));
        }else {

        }
    }

    void toC(){
        FeiyongObj feiyongObj = lists.get(0);
        msg_time.setText(feiyongObj.getMm_feiyong_name());
        msg_jine.setText(getResources().getString(R.string.money)+ feiyongObj.getMm_feiyong_jine());
    }
}

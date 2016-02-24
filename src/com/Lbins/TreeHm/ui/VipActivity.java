package com.Lbins.TreeHm.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.UniversityApplication;
import com.Lbins.TreeHm.adapter.ItemVipAdapter;
import com.Lbins.TreeHm.adapter.OnClickContentItemListener;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.data.FeiyongData;
import com.Lbins.TreeHm.data.FuwuObjData;
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
                if("1".equals(feiyongObj.getIs_select())){
                    lists.get(position).setIs_select("0");
                }else {
                    lists.get(position).setIs_select("1");
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }
}

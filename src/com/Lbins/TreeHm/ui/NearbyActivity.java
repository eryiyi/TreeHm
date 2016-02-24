package com.Lbins.TreeHm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.UniversityApplication;
import com.Lbins.TreeHm.adapter.ItemNearbyAdapter;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.data.EmpData;
import com.Lbins.TreeHm.data.EmpsData;
import com.Lbins.TreeHm.module.Emp;
import com.Lbins.TreeHm.module.FuwuObj;
import com.Lbins.TreeHm.util.StringUtil;
import com.amap.api.maps.model.LatLng;
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
 * Created by Administrator on 2016/2/23.
 */
public class NearbyActivity extends BaseActivity implements View.OnClickListener {
    private ListView lstv;
    ItemNearbyAdapter adapter;
    List<Emp> lists;
    private ImageView no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_activity);

        initView();
        getData();
    }

    void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_NEARBY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    EmpsData data = getGson().fromJson(s, EmpsData.class);
                                    if(data != null && data.getData().size() > 0){
                                        //计算距离
                                        List<Emp> listsAll = new ArrayList<Emp>();
                                        listsAll.addAll(data.getData());
                                        for(int i=0;i<listsAll.size();i++){
                                            Emp fuwuObj = listsAll.get(i);
                                            if(fuwuObj != null && !StringUtil.isNullOrEmpty(fuwuObj.getLat()) && !StringUtil.isNullOrEmpty(fuwuObj.getLng())){
                                                LatLng latLng = new LatLng(Double.valueOf(UniversityApplication.lat), Double.valueOf(UniversityApplication.lng));
                                                LatLng latLng1 = new LatLng(Double.valueOf(fuwuObj.getLat()), Double.valueOf(fuwuObj.getLng()));
                                                String distance = StringUtil.getDistance(latLng ,latLng1 );
                                                listsAll.get(i).setDistance(distance + "km");
                                                fuwuObj.setDistance(distance+"km");
                                                if(Double.valueOf(distance) < 100){
                                                    //100KM以内的
                                                    lists.add(fuwuObj);
                                                }
                                            }
                                        }

                                        adapter.notifyDataSetChanged();
                                        if(lists.size() > 0){
                                            no_data.setVisibility(View.GONE);
                                            lstv.setVisibility(View.VISIBLE);
                                        }else{
                                            no_data.setVisibility(View.VISIBLE);
                                            lstv.setVisibility(View.GONE);
                                        }
                                    }
                                }
                                else{
                                    showMsg(NearbyActivity.this, getResources().getString(R.string.get_data_error));
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
                        showMsg(NearbyActivity.this, getResources().getString(R.string.get_data_error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lat", UniversityApplication.lat);
                params.put("lng", UniversityApplication.lng);
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
    void initView(){
        this.findViewById(R.id.back).setOnClickListener(this);
        lstv = (ListView) this.findViewById(R.id.lstv);
        lists = new ArrayList<Emp>();
        adapter = new ItemNearbyAdapter(lists, NearbyActivity.this);
        lstv.setAdapter(adapter);
        no_data = (ImageView) this.findViewById(R.id.no_data);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent profileV = new Intent(NearbyActivity.this, ProfileActivity.class);
                Emp emp = lists.get(i);
                profileV.putExtra("id", emp.getMm_emp_id());
                startActivity(profileV);
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
}

package com.Lbins.TreeHm.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.UniversityApplication;
import com.Lbins.TreeHm.adapter.ItemFavourAdapter;
import com.Lbins.TreeHm.adapter.OnClickContentItemListener;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.dao.RecordMsg;
import com.Lbins.TreeHm.data.EmpsData;
import com.Lbins.TreeHm.data.FavourData;
import com.Lbins.TreeHm.data.RecordData;
import com.Lbins.TreeHm.data.RecordDataSingle;
import com.Lbins.TreeHm.module.Emp;
import com.Lbins.TreeHm.module.Favour;
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
 * Created by zhanghailong on 2016/3/3.
 */
public class MineFavour extends BaseActivity implements View.OnClickListener,OnClickContentItemListener{
    private TextView back;
    private ImageView no_data;
    private ListView lstv;
    private List<Favour> lists = new ArrayList<Favour>();
    private ItemFavourAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_favour_activity);

        initView();
        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    void initView(){
        back = (TextView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        no_data = (ImageView) this.findViewById(R.id.no_data);
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemFavourAdapter(lists, MineFavour.this);
        lstv.setAdapter(adapter);
        adapter.setOnClickContentItemListener(this);
    }

    void getData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.LIST_FAVOUR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200) {
                                    FavourData data = getGson().fromJson(s, FavourData.class);
                                    lists.clear();
                                    lists.addAll(data.getData());
                                    adapter.notifyDataSetChanged();

                                }else if(Integer.parseInt(code) == 9){
                                    Toast.makeText(MineFavour.this, R.string.login_out, Toast.LENGTH_SHORT).show();
                                    save("password", "");
                                    Intent loginV = new Intent(MineFavour.this, LoginActivity.class);
                                    startActivity(loginV);
                                    finish();
                                }else {
                                    showMsg(MineFavour.this,getResources().getString(R.string.get_data_error));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        if (lists.size() > 0) {
                            no_data.setVisibility(View.GONE);
                            lstv.setVisibility(View.VISIBLE);
                        } else {
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
                        showMsg(MineFavour.this, getResources().getString(R.string.get_data_error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mm_emp_id" , getGson().fromJson(getSp().getString("mm_emp_id", ""), String.class));
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("access_token", ""), String.class))){
                    params.put("accessToken", getGson().fromJson(getSp().getString("access_token", ""), String.class));
                }else {
                    params.put("accessToken", "");
                }
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

    Favour recordVO;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
                //分享
                break;
            case 2:
            case 4:
            {
                //头像

                recordVO = lists.get(position);
                Intent mineV = new Intent(MineFavour.this, ProfileActivity.class);
                mineV.putExtra("id", recordVO.getMm_emp_id());
                startActivity(mineV);
            }
            break;
            case 3:
                //电话
                recordVO = lists.get(position);
                if(recordVO != null && !StringUtil.isNullOrEmpty(recordVO.getMm_emp_mobile())){
                    showTel(recordVO.getMm_emp_mobile());
                }else{
                    //
                    Toast.makeText(MineFavour.this, "商户暂无电话!", Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                //图片
                recordVO = lists.get(position);
                getRecord(recordVO.getMm_msg_id());
//
                break;

        }
    }

    // 拨打电话窗口
    private void showTel(String tel) {
        final Dialog picAddDialog = new Dialog(MineFavour.this, R.style.dialog);
        View picAddInflate = View.inflate(MineFavour.this, R.layout.tel_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        final TextView jubao_cont = (TextView) picAddInflate.findViewById(R.id.jubao_cont);
        jubao_cont.setText(tel);
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


    void getRecord(final String mm_msg_id){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_RECORD_BY_ID_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    RecordDataSingle data = getGson().fromJson(s, RecordDataSingle.class);
                                    Intent intent = new Intent(MineFavour.this, DetailRecordActivity.class);
                                    intent.putExtra("info", data.getData());
                                    startActivity(intent);

                                }else if(Integer.parseInt(code) == 9){
                                    Toast.makeText(MineFavour.this, R.string.login_out , Toast.LENGTH_SHORT).show();
                                    save("password", "");
                                    Intent loginV = new Intent(MineFavour.this, LoginActivity.class);
                                    startActivity(loginV);
                                }
                                else{
                                    Toast.makeText(MineFavour.this, R.string.get_data_error , Toast.LENGTH_SHORT).show();
                                }
                                if(lists.size() == 0){
                                    no_data.setVisibility(View.VISIBLE);
                                    lstv.setVisibility(View.GONE);
                                }else {
                                    no_data.setVisibility(View.GONE);
                                    lstv.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

//                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", mm_msg_id);
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("access_token", ""), String.class))){
                    params.put("accessToken", getGson().fromJson(getSp().getString("access_token", ""), String.class));
                }else {
                    params.put("accessToken", "");
                }
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

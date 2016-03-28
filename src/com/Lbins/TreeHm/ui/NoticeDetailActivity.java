package com.Lbins.TreeHm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.*;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.adapter.ItemNoticeAdapter;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.data.NoticeData;
import com.Lbins.TreeHm.data.NoticeSingleData;
import com.Lbins.TreeHm.library.internal.PullToRefreshBase;
import com.Lbins.TreeHm.library.internal.PullToRefreshListView;
import com.Lbins.TreeHm.module.Notice;
import com.Lbins.TreeHm.util.StringUtil;
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
 * Created by Administrator on 2016/3/26 0026.
 */
public class NoticeDetailActivity extends BaseActivity implements View.OnClickListener {
    private String id;
    private TextView title;
    private TextView content;
    private TextView dateline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_detail_activity);
        id = getIntent().getExtras().getString("id");
        this.findViewById(R.id.back).setOnClickListener(this);
        title  = (TextView) this.findViewById(R.id.title);
        content  = (TextView) this.findViewById(R.id.content);
        dateline  = (TextView) this.findViewById(R.id.dateline);
        initData();
    }


    void initData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_NOTICE_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    NoticeSingleData data = getGson().fromJson(s, NoticeSingleData.class);
                                    Notice notice = data.getData();
                                    title.setText(notice.getMm_notice_title());
                                    content.setText(notice.getMm_notice_content());
                                    dateline.setText(notice.getDateline());
                                }else if(Integer.parseInt(code) == 9){
                                    Toast.makeText(NoticeDetailActivity.this, R.string.login_out, Toast.LENGTH_SHORT).show();
                                    save("password", "");
                                    Intent loginV = new Intent(NoticeDetailActivity.this, LoginActivity.class);
                                    startActivity(loginV);
                                }
                                else{
                                    Toast.makeText(NoticeDetailActivity.this, R.string.get_data_error , Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(NoticeDetailActivity.this, R.string.get_data_error , Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
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
}

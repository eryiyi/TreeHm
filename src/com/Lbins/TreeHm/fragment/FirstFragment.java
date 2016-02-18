package com.Lbins.TreeHm.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.adapter.ItemRecordAdapter;
import com.Lbins.TreeHm.adapter.OnClickContentItemListener;
import com.Lbins.TreeHm.base.BaseFragment;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.data.EmpData;
import com.Lbins.TreeHm.data.RecordData;
import com.Lbins.TreeHm.library.internal.PullToRefreshBase;
import com.Lbins.TreeHm.library.internal.PullToRefreshListView;
import com.Lbins.TreeHm.module.Record;
import com.Lbins.TreeHm.module.RecordVO;
import com.Lbins.TreeHm.module.ReportObj;
import com.Lbins.TreeHm.ui.AddRecordActivity;
import com.Lbins.TreeHm.ui.Constants;
import com.Lbins.TreeHm.ui.DetailRecordActivity;
import com.Lbins.TreeHm.ui.LoginActivity;
import com.Lbins.TreeHm.util.HttpUtils;
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
 * Created by Administrator on 2016/1/22.
 */
public class FirstFragment extends BaseFragment implements OnClickContentItemListener,View.OnClickListener{
    private View view;
    private Resources res;
    private PullToRefreshListView lstv;
    private ItemRecordAdapter adapter;
    private List<RecordVO> lists ;
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.one_fragment, null);
        res = getActivity().getResources();
        initView();
        initData();
        return view;
    }

    void initView( ){
        lists = new ArrayList<RecordVO>();
        lstv = (PullToRefreshListView) view.findViewById(R.id.lstv);
        adapter = new ItemRecordAdapter(lists, getActivity());
        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                if( "1".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))){
                    initData();
                }else {
                    lstv.onRefreshComplete();
                    //未登录
                    Intent loginV = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginV);
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                if( "1".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))){
                    initData();
                }else {
                    lstv.onRefreshComplete();
                    //未登录
                    Intent loginV = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginV);
                }
            }
        });
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailRecordActivity.class);
                startActivity(intent);
            }
        });
        adapter.setOnClickContentItemListener(this);
        view.findViewById(R.id.mLocation).setOnClickListener(this);
        view.findViewById(R.id.add).setOnClickListener(this);
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
                //分享
                break;
            case 2:
                //电话
                break;
            case 3:
                //头像
            case 4:
                //昵称
                break;
        }
    }

    void initData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_RECORD_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    RecordData data = getGson().fromJson(s, RecordData.class);
                                    if (IS_REFRESH) {
                                        lists.clear();
                                    }
                                    lists.addAll(data.getData());
                                    lstv.onRefreshComplete();
                                    adapter.notifyDataSetChanged();
//                                    adapter.refresh(lists);
                                }
                                else{
                                    Toast.makeText(getActivity(), R.string.get_data_error , Toast.LENGTH_SHORT).show();
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
                params.put("index", String.valueOf(pageIndex));
                params.put("size", "10");
                params.put("mm_msg_type", "0");
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mm_emp_provinceId", ""), String.class))){
                    params.put("provinceid", getGson().fromJson(getSp().getString("mm_emp_provinceId", ""), String.class));
                }else {
                    params.put("provinceid", "");
                }
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mm_emp_cityId", ""), String.class))){
                    params.put("cityid", getGson().fromJson(getSp().getString("mm_emp_cityId", ""), String.class));
                }else {
                    params.put("cityid", "");
                }
                if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mm_emp_countryId", ""), String.class))){
                    params.put("countryid", getGson().fromJson(getSp().getString("mm_emp_countryId", ""), String.class));
                }else {
                    params.put("countryid", "");
                }
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


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:
                //添加信息
            {
                if((StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) || "0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class)))){
                    //未登录
                    Intent loginV = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginV);
                }else {
                    Intent addV = new Intent(getActivity(), AddRecordActivity.class);
                    startActivity(addV);
                }
            }
                break;
            case R.id.mLocation:
                //
                break;
        }
    }


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Constants.SEND_INDEX_SUCCESS_QIUGOU)){
                RecordVO record1 = (RecordVO) intent.getExtras().get("addRecord");
                lists.add(0, record1);
                adapter.notifyDataSetChanged();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();

        myIntentFilter.addAction(Constants.SEND_INDEX_SUCCESS_QIUGOU);//添加说说和添加视频成功，刷新首页
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }
}

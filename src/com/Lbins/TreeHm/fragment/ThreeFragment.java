package com.Lbins.TreeHm.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.adapter.ItemRecordAdapter;
import com.Lbins.TreeHm.adapter.OnClickContentItemListener;
import com.Lbins.TreeHm.base.BaseFragment;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.data.RecordData;
import com.Lbins.TreeHm.library.internal.PullToRefreshBase;
import com.Lbins.TreeHm.library.internal.PullToRefreshListView;
import com.Lbins.TreeHm.module.RecordVO;
import com.Lbins.TreeHm.ui.*;
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
public class ThreeFragment  extends BaseFragment implements OnClickContentItemListener,View.OnClickListener {
    private View view;
    private Resources res;
    private PullToRefreshListView lstv;
    private ItemRecordAdapter adapter;
    private List<RecordVO> lists = new ArrayList<RecordVO>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    private ImageView no_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.three_fragment, null);
        res = getActivity().getResources();
        initView();
        initData();
        return view;
    }

    void initView() {
        //
        no_data = (ImageView) view.findViewById(R.id.no_data);
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

            }
        });
        adapter.setOnClickContentItemListener(this);
    }

    RecordVO recordVO;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
                //分享
                lists.get(position).setIs_read("1");
                adapter.notifyDataSetChanged();
                break;
            case 2:
            case 4:
            {
                //头像
                lists.get(position).setIs_read("1");
                adapter.notifyDataSetChanged();

                recordVO = lists.get(position);
                lists.get(position).setIs_read("1");
                adapter.notifyDataSetChanged();
                Intent mineV = new Intent(getActivity(), ProfileActivity.class);
                mineV.putExtra("id", recordVO.getMm_emp_id());
                startActivity(mineV);
            }
            break;
            case 3:
                //电话
                lists.get(position).setIs_read("1");
                adapter.notifyDataSetChanged();

                recordVO = lists.get(position);
                if(recordVO != null && !StringUtil.isNullOrEmpty(recordVO.getMm_emp_mobile())){
                    showTel(recordVO.getMm_emp_mobile());
                }else{
                    //
                    Toast.makeText(getActivity(), "商户暂无电话!", Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
            case 6:
                //图片
                Intent intent = new Intent(getActivity(), DetailRecordActivity.class);
                recordVO = lists.get(position);
                intent.putExtra("info", recordVO);
                startActivity(intent);

                lists.get(position).setIs_read("1");
                adapter.notifyDataSetChanged();
                break;
        }
    }

    // 拨打电话窗口
    private void showTel(String tel) {
        final Dialog picAddDialog = new Dialog(getActivity(), R.style.dialog);
        View picAddInflate = View.inflate(getActivity(), R.layout.tel_dialog, null);
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

    void initData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_TOP_URL,
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
                                    lists .addAll(data.getData());
                                    lstv.onRefreshComplete();
                                    adapter.notifyDataSetChanged();
                                }
                                else{
                                    Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(lists.size() > 0){
                            no_data.setVisibility(View.GONE);
                            lstv.setVisibility(View.VISIBLE);
                        }else{
                            no_data.setVisibility(View.VISIBLE);
                            lstv.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("index", String.valueOf(pageIndex));
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}

package com.Lbins.TreeHm.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.Lbins.TreeHm.dao.DBHelper;
import com.Lbins.TreeHm.dao.RecordMsg;
import com.Lbins.TreeHm.data.RecordData;
import com.Lbins.TreeHm.library.internal.PullToRefreshBase;
import com.Lbins.TreeHm.library.internal.PullToRefreshListView;
import com.Lbins.TreeHm.ui.*;
import com.Lbins.TreeHm.util.StringUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
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
    private List<RecordMsg> lists = new ArrayList<RecordMsg>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    private ImageView no_data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
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
                lists.get(position-1).setIs_read("1");
                adapter.notifyDataSetChanged();

                recordVO = lists.get(position-1);
                DBHelper.getInstance(getActivity()).updateRecord(recordVO);
            }
        });
        adapter.setOnClickContentItemListener(this);
        no_data.setOnClickListener(this);
    }

    RecordMsg recordVO;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag){
            case 1:
                //分享
                recordVO = lists.get(position);
                lists.get(position).setIs_read("1");
                adapter.notifyDataSetChanged();

                recordVO.setIs_read("1");
                DBHelper.getInstance(getActivity()).updateRecord(recordVO);
                share(recordVO);
                break;
            case 2:
            case 4:
            {
                //头像

                recordVO = lists.get(position);
                lists.get(position).setIs_read("1");
                adapter.notifyDataSetChanged();
                Intent mineV = new Intent(getActivity(), ProfileActivity.class);
                mineV.putExtra("id", recordVO.getMm_emp_id());
                startActivity(mineV);

                recordVO.setIs_read("1");
                DBHelper.getInstance(getActivity()).updateRecord(recordVO);
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

                recordVO.setIs_read("1");
                DBHelper.getInstance(getActivity()).updateRecord(recordVO);
                break;
            case 5:
                //图片
                Intent intent = new Intent(getActivity(), DetailRecordActivity.class);
                recordVO = lists.get(position);
                intent.putExtra("info", recordVO);
                startActivity(intent);

                lists.get(position).setIs_read("1");
                adapter.notifyDataSetChanged();

                recordVO.setIs_read("1");
                DBHelper.getInstance(getActivity()).updateRecord(recordVO);
                break;
            case 6:
                //收藏图标
                lists.get(position).setIs_read("1");
                adapter.notifyDataSetChanged();
                if("1".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))){
                    recordVO = lists.get(position);
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();
                    saveFavour(recordVO.getMm_msg_id());

                    recordVO.setIs_read("1");
                    DBHelper.getInstance(getActivity()).updateRecord(recordVO);
                }else {
                    //未登录
                    Intent loginV = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginV);
                }
                break;
        }
    }

    private RecordMsg recordMsgTmp;

    void share(RecordMsg recordVO){
        //
        recordMsgTmp = recordVO;

        new ShareAction(getActivity()).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setShareboardclickCallback(shareBoardlistener)
                .open();
    }

    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform,SHARE_MEDIA share_media) {
            UMImage image = new UMImage(getActivity(), (recordVO.getMm_emp_cover()==null?"":recordVO.getMm_emp_cover()));
            String msg = recordMsgTmp.getMm_msg_title()==""?recordMsgTmp.getMm_msg_content():recordMsgTmp.getMm_msg_title();
            String msgC = recordMsgTmp.getMm_msg_content()==""?"花木通":recordMsgTmp.getMm_msg_content();
            new ShareAction(getActivity()).setPlatform(share_media).setCallback(umShareListener)
                    .withText(msgC)
                    .withTitle(msg)
                    .withTargetUrl(InternetURL.VIEW_RECORD_BYID_URL + "?id=" + recordMsgTmp.getMm_msg_id())
                    .withMedia(image)
                    .share();
        }
    };

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(),platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(),platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
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
                                    if(data != null && data.getData() != null){
                                        for(RecordMsg recordMsg:data.getData()){
                                            RecordMsg recordMsgLocal = DBHelper.getInstance(getActivity()).getRecord(recordMsg.getMm_msg_id());
                                            if(recordMsgLocal != null){
                                                //已经存在了 不需要插入了
                                            }else{
                                                DBHelper.getInstance(getActivity()).saveRecord(recordMsg);
                                            }
                                        }
                                    }
                                    lstv.onRefreshComplete();
                                    adapter.notifyDataSetChanged();
                                }else if(Integer.parseInt(code) == 9){
                                    Toast.makeText(getActivity(), R.string.login_out , Toast.LENGTH_SHORT).show();
                                    save("password", "");
                                    Intent loginV = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(loginV);
                                    getActivity().finish();
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
            case R.id.no_data:
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
                break;
        }
    }


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("change_color_size")){
                adapter.notifyDataSetChanged();
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("change_color_size");//
        //注册广播
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    void saveFavour(final String mm_msg_id){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.ADD_FAVOUR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    Toast.makeText(getActivity(), R.string.favour_success , Toast.LENGTH_SHORT).show();
                                }else if(Integer.parseInt(code) == 9){
                                    Toast.makeText(getActivity(), R.string.login_out, Toast.LENGTH_SHORT).show();
                                    save("password", "");
                                    Intent loginV = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(loginV);
                                    getActivity().finish();
                                }else if(Integer.parseInt(code) == 2){
                                    Toast.makeText(getActivity(), R.string.favour_error_one , Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getActivity(), R.string.no_favour , Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), R.string.no_favour, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mm_msg_id", mm_msg_id);
                params.put("mm_emp_id",  getGson().fromJson(getSp().getString("mm_emp_id", ""), String.class));
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

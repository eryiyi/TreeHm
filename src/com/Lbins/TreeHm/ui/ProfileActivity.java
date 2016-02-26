package com.Lbins.TreeHm.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.UniversityApplication;
import com.Lbins.TreeHm.adapter.AnimateFirstDisplayListener;
import com.Lbins.TreeHm.adapter.ItemRecordAdapter;
import com.Lbins.TreeHm.adapter.OnClickContentItemListener;
import com.Lbins.TreeHm.adapter.ViewPagerAdapter;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.data.EmpAdObjData;
import com.Lbins.TreeHm.data.EmpData;
import com.Lbins.TreeHm.data.RecordData;
import com.Lbins.TreeHm.module.Emp;
import com.Lbins.TreeHm.module.EmpAdObj;
import com.Lbins.TreeHm.module.RecordVO;
import com.Lbins.TreeHm.util.StringUtil;
import com.Lbins.TreeHm.widget.ContentListView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/19.
 */
public class ProfileActivity extends BaseActivity implements View.OnClickListener, ContentListView.OnRefreshListener,
        ContentListView.OnLoadListener ,OnClickContentItemListener{
    private ContentListView lstv;
    private int pageIndex = 1;
    private ItemRecordAdapter adapter;
    private List<RecordVO> lists = new ArrayList<RecordVO>();

    //header
    LinearLayout headLiner;
    private ImageView head;//头像
    private TextView content;//公司简介
    private TextView nickname;//姓名
    private TextView address;//公司地址

    private TextView back;
    private String id;
    private Emp emp;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    //导航
    private ViewPager viewpager;
    private ViewPagerAdapter adapterAd;
    private LinearLayout viewGroup;
    private ImageView dot, dots[];
    private Runnable runnable;
    private int autoChangeTime = 5000;
    private List<EmpAdObj> listsAd = new ArrayList<EmpAdObj>();


    /**
     * 加载数据监听实现
     */
    @Override
    public void onLoad() {
        pageIndex++;
        loadData(ContentListView.LOAD);
    }

    /**
     * 刷新数据监听实现
     */
    @Override
    public void onRefresh() {
        pageIndex = 1;
        loadData(ContentListView.REFRESH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        id = getIntent().getExtras().getString("id");

        lstv = (ContentListView) this.findViewById(R.id.lstv);
        headLiner = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.profile_header, null);
        head = (ImageView) headLiner.findViewById(R.id.head);
        content = (TextView) headLiner.findViewById(R.id.content);
        nickname = (TextView) headLiner.findViewById(R.id.nickname);
        address = (TextView) headLiner.findViewById(R.id.address);
        back = (TextView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        adapter = new ItemRecordAdapter(lists, ProfileActivity.this);
        adapter.setOnClickContentItemListener(this);
        lstv.setAdapter(adapter);
        lstv.addHeaderView(headLiner);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lists.get(i-2).setIs_read("1");
                adapter.notifyDataSetChanged();
            }
        });

        getProfile();
        loadData(ContentListView.REFRESH);

        getAd();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    void getProfile(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_MEMBER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    EmpData data = getGson().fromJson(s, EmpData.class);
                                    emp = data.getData();
                                    initData();
                                }
                                else{
                                    showMsg(ProfileActivity.this, getResources().getString(R.string.get_data_error));
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
                        showMsg(ProfileActivity.this, getResources().getString(R.string.get_data_error));
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

    private void loadData(final int currentid) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_RECORDS_BYID_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        lstv.onRefreshComplete();
                        lstv.onLoadComplete();
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    RecordData data = getGson().fromJson(s, RecordData.class);
                                    if (ContentListView.REFRESH == currentid) {
                                        lists.clear();
                                        lists.addAll(data.getData());
                                        lstv.setResultSize(data.getData().size());
                                        adapter.notifyDataSetChanged();
                                    }
                                    if (ContentListView.LOAD == currentid) {
                                        lists.addAll(data.getData());
                                        lstv.setResultSize(data.getData().size());
                                        adapter.notifyDataSetChanged();
                                    }
                                } else if(Integer.parseInt(code) == 9){
                                    Toast.makeText(ProfileActivity.this, R.string.login_out , Toast.LENGTH_SHORT).show();
                                    save("password", "");
                                    Intent loginV = new Intent(ProfileActivity.this, LoginActivity.class);
                                    startActivity(loginV);
                                    finish();
                                }else {
                                    Toast.makeText(ProfileActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(ProfileActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        lstv.onRefreshComplete();
                        lstv.onLoadComplete();
                        Toast.makeText(ProfileActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("index", String.valueOf(pageIndex));
                params.put("size", "10");
                params.put("mm_emp_id", id);
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




    void initData(){
        imageLoader.displayImage(emp.getMm_emp_cover(), head, UniversityApplication.txOptions, animateFirstListener);
        address.setText(emp.getMm_emp_company());
        content.setText(emp.getMm_emp_company_detail());
        nickname.setText(emp.getMm_emp_nickname());
        back.setText(emp.getMm_emp_nickname());
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
//                头像
                recordVO = lists.get(position);
                lists.get(position).setIs_read("1");
                adapter.notifyDataSetChanged();
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
                    Toast.makeText(ProfileActivity.this, "商户暂无电话!", Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
            case 6:
                //图片
                Intent intent = new Intent(ProfileActivity.this, DetailRecordActivity.class);
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
        final Dialog picAddDialog = new Dialog(ProfileActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(ProfileActivity.this, R.layout.tel_dialog, null);
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


    private void initViewPager() {
        adapterAd = new ViewPagerAdapter(ProfileActivity.this);
        adapterAd.change(listsAd);
        adapterAd.setOnClickContentItemListener(this);
        viewpager = (ViewPager) headLiner.findViewById(R.id.viewpager);
        viewpager.setAdapter(adapterAd);
        viewpager.setOnPageChangeListener(myOnPageChangeListener);
        initDot();
        runnable = new Runnable() {
            @Override
            public void run() {
                int next = viewpager.getCurrentItem() + 1;
                if (next >= adapterAd.getCount()) {
                    next = 0;
                }
                viewHandler.sendEmptyMessage(next);
            }
        };
        viewHandler.postDelayed(runnable, autoChangeTime);
    }


    // 初始化dot视图
    private void initDot() {
        viewGroup = (LinearLayout) headLiner.findViewById(R.id.viewGroup);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                20, 20);
        layoutParams.setMargins(4, 3, 4, 3);

        dots = new ImageView[adapterAd.getCount()];
        for (int i = 0; i < adapterAd.getCount(); i++) {
            dot = new ImageView(ProfileActivity.this);
            dot.setLayoutParams(layoutParams);
            dots[i] = dot;
            dots[i].setTag(i);
            dots[i].setOnClickListener(onClick);

            if (i == 0) {
                dots[i].setBackgroundResource(R.drawable.dotc);
            } else {
                dots[i].setBackgroundResource(R.drawable.dotn);
            }

            viewGroup.addView(dots[i]);
        }
    }

    ViewPager.OnPageChangeListener myOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            setCurDot(arg0);
            viewHandler.removeCallbacks(runnable);
            viewHandler.postDelayed(runnable, autoChangeTime);
        }

    };
    // 实现dot点击响应功能,通过点击事件更换页面
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            setCurView(position);
        }

    };

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position > adapterAd.getCount()) {
            return;
        }
        viewpager.setCurrentItem(position);
//        if (!StringUtil.isNullOrEmpty(lists.get(position).getNewsTitle())){
//            titleSlide = lists.get(position).getNewsTitle();
//            if(titleSlide.length() > 13){
//                titleSlide = titleSlide.substring(0,12);
//                article_title.setText(titleSlide);//当前新闻标题显示
//            }else{
//                article_title.setText(titleSlide);//当前新闻标题显示
//            }
//        }

    }

    /**
     * 选中当前引导小点
     */
    private void setCurDot(int position) {
        for (int i = 0; i < dots.length; i++) {
            if (position == i) {
                dots[i].setBackgroundResource(R.drawable.dotc);
            } else {
                dots[i].setBackgroundResource(R.drawable.dotn);
            }
        }
    }

    /**
     * 每隔固定时间切换广告栏图片
     */
    @SuppressLint("HandlerLeak")
    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setCurView(msg.what);
        }

    };



    private void getAd() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_AD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if (Integer.parseInt(code) == 200) {
                                    EmpAdObjData data = getGson().fromJson(s, EmpAdObjData.class);
                                    listsAd.clear();
                                    if(data != null && data.getData().size() > 0){
                                        listsAd.addAll(data.getData());
                                    }
                                    initViewPager();
                                } else {
                                    Toast.makeText(ProfileActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(ProfileActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(ProfileActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mm_emp_id", id);
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

    public void companyIndex(View view){
        //
        if(StringUtil.isNullOrEmpty(emp.getMm_emp_company_url())){
            showMsg(ProfileActivity.this, "暂无微网站");
        }else{
            Intent webV = new Intent(ProfileActivity.this, WebViewActivity.class);
            webV.putExtra("strurl", emp.getMm_emp_company_url());
            startActivity(webV);
        }
    }

}

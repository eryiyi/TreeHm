package com.Lbins.TreeHm.ui;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.UniversityApplication;
import com.Lbins.TreeHm.adapter.ItemFourFuwuAdapter;
import com.Lbins.TreeHm.adapter.OnClickContentItemListener;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.dao.DBHelper;
import com.Lbins.TreeHm.dao.RecordMsg;
import com.Lbins.TreeHm.data.FuwuObjData;
import com.Lbins.TreeHm.library.internal.PullToRefreshBase;
import com.Lbins.TreeHm.library.internal.PullToRefreshListView;
import com.Lbins.TreeHm.module.FuwuObj;
import com.Lbins.TreeHm.util.StringUtil;
import com.amap.api.maps.model.LatLng;
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
 * Created by Administrator on 2016/2/22.
 */
public class FourFuwuActivity extends BaseActivity implements View.OnClickListener, OnClickContentItemListener {


    private List<FuwuObj> lists = new ArrayList<FuwuObj>();
    private PullToRefreshListView gridView;
    private ItemFourFuwuAdapter adapter;
    private String mm_fuwu_type;

    private TextView back;
    private ImageView no_data1;

    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_fuwu_activity);
        mm_fuwu_type = getIntent().getExtras().getString("mm_fuwu_type");
        initView();
        switch (Integer.parseInt(mm_fuwu_type)) {
            case 0:
                back.setText(getResources().getString(R.string.miaomushop));
                break;
            case 1:
                back.setText(getResources().getString(R.string.zhuangchework));
                break;
            case 2:
                back.setText(getResources().getString(R.string.wuliucenter));
                break;
            case 3:
                back.setText(getResources().getString(R.string.jiajieteam));
                break;
            case 4:
                back.setText(getResources().getString(R.string.diaocheservie));
                break;
        }
        ;
        InitViewPager();
        initData();
    }

    public void initData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_FUWU_BY_LOCATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    FuwuObjData data = getGson().fromJson(s, FuwuObjData.class);
                                    if (IS_REFRESH) {
                                        lists.clear();
                                    }
                                    lists.addAll(data.getData());
                                    gridView.onRefreshComplete();
                                    adapter.notifyDataSetChanged();
                                } else if (Integer.parseInt(code1) == 9) {
                                    Toast.makeText(FourFuwuActivity.this, R.string.login_out, Toast.LENGTH_SHORT).show();
                                    save("password", "");
                                    Intent loginV = new Intent(FourFuwuActivity.this, LoginActivity.class);
                                    startActivity(loginV);
                                    finish();
                                } else {
                                    Toast.makeText(FourFuwuActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(FourFuwuActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(FourFuwuActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if (!StringUtil.isNullOrEmpty(mm_fuwu_type)) {
                    params.put("mm_fuwu_type", mm_fuwu_type);
                }
                if (!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("mm_emp_countryId", ""), String.class))) {
                    params.put("countryid", getGson().fromJson(getSp().getString("mm_emp_countryId", ""), String.class));
                } else {
                    params.put("countryid", "");
                }
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

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        back = (TextView) this.findViewById(R.id.back);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    private void InitViewPager() {
        gridView = (PullToRefreshListView) this.findViewById(R.id.lstv);
        no_data1 = (ImageView) this.findViewById(R.id.no_data);
        adapter = new ItemFourFuwuAdapter(lists, FourFuwuActivity.this, mm_fuwu_type);
        gridView.setMode(PullToRefreshBase.Mode.BOTH);
        gridView.setAdapter(adapter);
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                initData();
            }
        });
        adapter.setOnClickContentItemListener(this);

    }

    // 拨打电话窗口
    private void showTel(String tel) {
        final Dialog picAddDialog = new Dialog(FourFuwuActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(FourFuwuActivity.this, R.layout.tel_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        final TextView jubao_cont = (TextView) picAddInflate.findViewById(R.id.jubao_cont);
        jubao_cont.setText(tel);
        //提交
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String contreport = jubao_cont.getText().toString();
                if (!StringUtil.isNullOrEmpty(contreport)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + jubao_cont.getText().toString()));
                    startActivity(intent);
                }
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

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag) {
            case 1: {
                FuwuObj fuwuObj = lists.get(position);
                String mm_fuwu_url = fuwuObj.getMm_fuwu_url();
                if (!StringUtil.isNullOrEmpty(mm_fuwu_url)) {
                    Intent webV = new Intent(FourFuwuActivity.this, WebViewActivity.class);
                    webV.putExtra("strurl", mm_fuwu_url);
                    startActivity(webV);
                } else {
                    showMsg(FourFuwuActivity.this, getResources().getString(R.string.zanwu_www));
                }

            }
            break;
            case 2: {
                FuwuObj fuwuObj = lists.get(position);
                if (fuwuObj != null) {
                    showTel(fuwuObj.getMm_fuwu_tel());
                }
            }
            break;
            case 3: {
                FuwuObj fuwuObj = lists.get(position);
                if (fuwuObj != null && !StringUtil.isNullOrEmpty(UniversityApplication.lat) && !StringUtil.isNullOrEmpty(UniversityApplication.lng)) {
                    //开始导航
                    Intent naviV = new Intent(FourFuwuActivity.this, GPSNaviActivity.class);
                    naviV.putExtra("lat_end", fuwuObj.getLat());
                    naviV.putExtra("lng_end", fuwuObj.getLng());
                    startActivity(naviV);
                } else {
                    showMsg(FourFuwuActivity.this, getResources().getString(R.string.no_location_lat_lng));
                }
            }
            break;
        }
    }


//    private class MyOnClickListener implements View.OnClickListener {
//        private int index=0;
//        public MyOnClickListener(int i){
//            index=i;
//        }
//        public void onClick(View v) {
//            viewPager.setCurrentItem(index);
//        }
//
//    }
//
//    public class MyViewPagerAdapter extends PagerAdapter {
//        private List<View> mListViews;
//
//        public MyViewPagerAdapter(List<View> mListViews) {
//            this.mListViews = mListViews;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) 	{
//            container.removeView(mListViews.get(position));
//        }
//
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            container.addView(mListViews.get(position), 0);
//            return mListViews.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return  mListViews.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0==arg1;
//        }
//    }
//
//    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
//        public void onPageScrollStateChanged(int arg0) {
//        }
//
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//        }
//        public void onPageSelected(int arg0) {
//            if(arg0 == 0){
//                cursor1.setImageDrawable(getResources().getDrawable(R.drawable.line_bg));
//                cursor2.setImageDrawable(getResources().getDrawable(R.drawable.line_bg_white));
//            }
//            if(arg0 == 1){
//                cursor1.setImageDrawable(getResources().getDrawable(R.drawable.line_bg_white));
//                cursor2.setImageDrawable(getResources().getDrawable(R.drawable.line_bg));
//            }
//        }
//    }

//    void initDataLocation(){
//        if(listsAll != null && !StringUtil.isNullOrEmpty(UniversityApplication.lat) && !StringUtil.isNullOrEmpty(UniversityApplication.lng)){
////            lists.clear();
//            //计算距离
//            for(int i=0;i<listsAll.size();i++){
//                FuwuObj fuwuObj = listsAll.get(i);
//                if(fuwuObj != null && !StringUtil.isNullOrEmpty(fuwuObj.getLat()) && !StringUtil.isNullOrEmpty(fuwuObj.getLng())){
//                    LatLng latLng = new LatLng(Double.valueOf(UniversityApplication.lat), Double.valueOf(UniversityApplication.lng));
//                    LatLng latLng1 = new LatLng(Double.valueOf(fuwuObj.getLat()), Double.valueOf(fuwuObj.getLng()));
//                    String distance = StringUtil.getDistance(latLng ,latLng1 );
//                    listsAll.get(i).setDistance(distance+"km");
//                    fuwuObj.setDistance(distance+"km");
//                    //判断是否有设置附近的距离
//                    String mm_distance = getGson().fromJson(getSp().getString("mm_distance", ""), String.class);
//                    if(!StringUtil.isNullOrEmpty(mm_distance)){
//                        //说明设置了附近的距离
//                        if(Double.valueOf(distance) < Integer.parseInt(mm_distance)){
//                            //设置距离以内的
//                            lists.add(fuwuObj);
//                        }
//                    }else {
//                        if(Double.valueOf(distance) < 30){
//                            //30KM以内的
//                            lists.add(fuwuObj);
//                        }
//                    }
//
//                }
//            }
//            adapter.notifyDataSetChanged();
//        }
//        if(lists.size() > 0){
//            no_data1.setVisibility(View.GONE);
//            gridView.setVisibility(View.VISIBLE);
//        }else {
//            no_data1.setVisibility(View.VISIBLE);
//            gridView.setVisibility(View.GONE);
//        }
//    }
}

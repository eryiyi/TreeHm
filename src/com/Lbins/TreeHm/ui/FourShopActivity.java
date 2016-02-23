package com.Lbins.TreeHm.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.UniversityApplication;
import com.Lbins.TreeHm.adapter.ItemFourFuwuAdapter;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.base.InternetURL;
import com.Lbins.TreeHm.data.FuwuObjData;
import com.Lbins.TreeHm.data.ProvinceData;
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
public class FourShopActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private ImageView imageView;
    private TextView textView1,textView2;
    private List<View> views;
    private int offset = 0;
    private int currIndex = 0;
    private int bmpW;
    private View view1,view2;

    private List<FuwuObj> lists = new ArrayList<FuwuObj>();
    private List<FuwuObj> listsAll = new ArrayList<FuwuObj>();
    private ListView gridView ;
    private ListView gridView2 ;
    private ItemFourFuwuAdapter adapter ;
    private ItemFourFuwuAdapter adapterVideo ;
    private String mm_fuwu_type;

    private TextView back;
    private ImageView no_data1;
    private ImageView no_data2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_shop_activity);
        mm_fuwu_type = getIntent().getExtras().getString("mm_fuwu_type");
        initView();
        switch (Integer.parseInt(mm_fuwu_type)){
            case 0:
                back.setText("苗木商店");
                break;
            case 1:
                back.setText("装车工人");
                break;
            case 2:
                back.setText("物流中心");
                break;
            case 3:
                back.setText("嫁接团队");
                break;
            case 4:
                back.setText("吊车服务");
                break;

        };
        InitImageView();
        InitTextView();
        InitViewPager();
        initData();
    }

    public void initData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_FUWU_MSG_BY_LOCATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    FuwuObjData data = getGson().fromJson(s, FuwuObjData.class);
                                    listsAll.clear();
                                    listsAll.addAll(data.getData());
                                    adapterVideo.notifyDataSetChanged();
                                    if(listsAll.size() > 0){
                                        no_data2.setVisibility(View.GONE);
                                        gridView2.setVisibility(View.VISIBLE);
                                    }else {
                                        no_data2.setVisibility(View.VISIBLE);
                                        gridView2.setVisibility(View.GONE);
                                    }
                                    initDataLocation();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(FourShopActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(FourShopActivity.this, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if(!StringUtil.isNullOrEmpty(mm_fuwu_type)){
                    params.put("mm_fuwu_type" , mm_fuwu_type);
                }
                if(!StringUtil.isNullOrEmpty(UniversityApplication.lat)){
                    params.put("lat", UniversityApplication.lat);
                }
                if(!StringUtil.isNullOrEmpty(UniversityApplication.lng)){
                    params.put("lng", UniversityApplication.lng);
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

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        back = (TextView) this.findViewById(R.id.back);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    private void InitViewPager() {
        viewPager=(ViewPager) findViewById(R.id.vPager);
        views=new ArrayList<View>();
        LayoutInflater inflater=getLayoutInflater();
        view1=inflater.inflate(R.layout.four_shop_lay1, null);
        view2=inflater.inflate(R.layout.four_shop_lay2, null);
        views.add(view2);
        views.add(view1);

        viewPager.setAdapter(new MyViewPagerAdapter(views));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        gridView = (ListView) view1.findViewById(R.id.lstv);
        gridView2 = (ListView) view2.findViewById(R.id.lstv);
        no_data1 = (ImageView) view1.findViewById(R.id.no_data);
        no_data2 = (ImageView) view2.findViewById(R.id.no_data);

        adapter = new ItemFourFuwuAdapter(lists, FourShopActivity.this);
        adapterVideo = new ItemFourFuwuAdapter(listsAll, FourShopActivity.this);

        gridView.setAdapter(adapter);
        gridView2.setAdapter(adapterVideo);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FuwuObj fuwuObj = lists.get(i);
                if(fuwuObj != null){
                    showTel(fuwuObj.getMm_fuwu_tel());
                }
            }
        });
        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FuwuObj fuwuObj = listsAll.get(i);
                if(fuwuObj != null){
                    showTel(fuwuObj.getMm_fuwu_tel());
                }
            }
        });

    }
    // 拨打电话窗口
    private void showTel(String tel) {
        final Dialog picAddDialog = new Dialog(FourShopActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(FourShopActivity.this, R.layout.tel_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        final TextView jubao_cont = (TextView) picAddInflate.findViewById(R.id.jubao_cont);
        jubao_cont.setText(tel);
        //提交
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String contreport = jubao_cont.getText().toString();
                if(!StringUtil.isNullOrEmpty(contreport)){
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

    private void InitTextView() {
        textView1 = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);

        textView1.setOnClickListener(new MyOnClickListener(0));
        textView2.setOnClickListener(new MyOnClickListener(1));
    }



    private void InitImageView() {
        imageView= (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.line_bg).getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (screenW / 2 - bmpW) / 1;
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        imageView.setImageMatrix(matrix);
    }


    private class MyOnClickListener implements View.OnClickListener {
        private int index=0;
        public MyOnClickListener(int i){
            index=i;
        }
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }

    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) 	{
            container.removeView(mListViews.get(position));
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return  mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = offset * 1 + bmpW;
        int two = one * 1;
        public void onPageScrollStateChanged(int arg0) {


        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {


        }

        public void onPageSelected(int arg0) {
            Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            imageView.startAnimation(animation);
        }

    }

    void initDataLocation(){
        if(listsAll != null && !StringUtil.isNullOrEmpty(UniversityApplication.lat) && !StringUtil.isNullOrEmpty(UniversityApplication.lng)){
            lists.clear();
            //计算距离
            for(int i=0;i<listsAll.size();i++){
                FuwuObj fuwuObj = listsAll.get(i);
                if(fuwuObj != null && !StringUtil.isNullOrEmpty(fuwuObj.getLat()) && !StringUtil.isNullOrEmpty(fuwuObj.getLng())){
                    LatLng latLng = new LatLng(Double.valueOf(UniversityApplication.lat), Double.valueOf(UniversityApplication.lng));
                    LatLng latLng1 = new LatLng(Double.valueOf(fuwuObj.getLat()), Double.valueOf(fuwuObj.getLng()));
                    String distance = StringUtil.getDistance(latLng ,latLng1 );
                    listsAll.get(i).setDistance(distance+"km");
                    fuwuObj.setDistance(distance+"km");
                    if(Double.valueOf(distance) < 30){
                        //30KM以内的
                        lists.add(fuwuObj);
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
        if(lists.size() > 0){
            no_data1.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }else {
            no_data1.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        }
    }
}
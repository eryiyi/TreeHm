package com.Lbins.TreeHm.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.UniversityApplication;
import com.Lbins.TreeHm.adapter.AnimateFirstDisplayListener;
import com.Lbins.TreeHm.adapter.OnClickContentItemListener;
import com.Lbins.TreeHm.adapter.ViewPagerAdapter;
import com.Lbins.TreeHm.base.BaseFragment;
import com.Lbins.TreeHm.ui.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/22.
 */
public class FourFragment extends BaseFragment implements View.OnClickListener ,OnClickContentItemListener {
    private View view;

    private final static int SCANNIN_GREQUEST_CODE = 1;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    Resources res;
    //导航
    private ViewPager viewpager;
    private ViewPagerAdapter adapter;
    private LinearLayout viewGroup;
    private ImageView dot, dots[];
    private Runnable runnable;
    private int autoChangeTime = 5000;
    private List<String> lists = new ArrayList<String>();

    private ImageView head;
    private TextView nickname;
    private TextView type;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.four_fragment, null);
        res = getActivity().getResources();
        initView();

        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        initViewPager();

        initData();
        return view;
    }

    private void initData() {
        imageLoader.displayImage(getGson().fromJson(getSp().getString("mm_emp_cover", ""), String.class), head, UniversityApplication.txOptions, animateFirstListener);
        nickname.setText(getGson().fromJson(getSp().getString("mm_emp_nickname", ""), String.class));
        if("0".equals(getGson().fromJson(getSp().getString("mm_emp_type", ""), String.class))){
            type.setText("苗木经营");
        }
        if("1".equals(getGson().fromJson(getSp().getString("mm_emp_type", ""), String.class))){
            type.setText("苗木会员");
        }

    }

    void initView( ){
        //
        head = (ImageView) view.findViewById(R.id.head);
        nickname = (TextView) view.findViewById(R.id.nickname);
        type = (TextView) view.findViewById(R.id.type);

//        view.findViewById(R.id.img_one).setOnClickListener(this);
//        view.findViewById(R.id.img_two).setOnClickListener(this);
//        view.findViewById(R.id.img_three).setOnClickListener(this);
//        view.findViewById(R.id.img_five).setOnClickListener(this);
//        view.findViewById(R.id.img_six).setOnClickListener(this);
//        view.findViewById(R.id.img_seven).setOnClickListener(this);
//        view.findViewById(R.id.img_eight).setOnClickListener(this);
//        view.findViewById(R.id.img_nine).setOnClickListener(this);
//        view.findViewById(R.id.img_ten).setOnClickListener(this);
//        view.findViewById(R.id.img_evelen).setOnClickListener(this);
//        view.findViewById(R.id.img_twelven).setOnClickListener(this);
        view.findViewById(R.id.relate_set).setOnClickListener(this);
        view.findViewById(R.id.relate_shop).setOnClickListener(this);
        view.findViewById(R.id.relate_bank).setOnClickListener(this);
        view.findViewById(R.id.relate_work).setOnClickListener(this);
        view.findViewById(R.id.relate_wuliu).setOnClickListener(this);
        view.findViewById(R.id.relate_jiajie).setOnClickListener(this);
        view.findViewById(R.id.relate_msg).setOnClickListener(this);
        view.findViewById(R.id.realte_diaoche).setOnClickListener(this);
        view.findViewById(R.id.relate_about).setOnClickListener(this);
        view.findViewById(R.id.realte_ziliao).setOnClickListener(this);
        view.findViewById(R.id.relate_updatepwr).setOnClickListener(this);
        view.findViewById(R.id.relate_suggest).setOnClickListener(this);
    }

    private void initViewPager() {
        adapter = new ViewPagerAdapter(getActivity());
        adapter.change(lists);
        adapter.setOnClickContentItemListener(this);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(myOnPageChangeListener);
        initDot();
        runnable = new Runnable() {
            @Override
            public void run() {
                int next = viewpager.getCurrentItem() + 1;
                if (next >= adapter.getCount()) {
                    next = 0;
                }
                viewHandler.sendEmptyMessage(next);
            }
        };
        viewHandler.postDelayed(runnable, autoChangeTime);
    }


    // 初始化dot视图
    private void initDot() {
        viewGroup = (LinearLayout) view.findViewById(R.id.viewGroup);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                20, 20);
        layoutParams.setMargins(4, 3, 4, 3);

        dots = new ImageView[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++) {

            dot = new ImageView(getActivity());
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
        if (position < 0 || position > adapter.getCount()) {
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

    String slidePic;
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        slidePic = lists.get(position);
        switch (flag){
            case 0:
//                Intent webView = new Intent(getActivity(), WebViewActivity.class);
//                webView.putExtra("strurl", slidePic.getHref_url());
//                startActivity(webView);
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.relate_set:
                Intent setV = new Intent(getActivity(), SettingActivity.class);
                startActivity(setV);
                break;
            case R.id.relate_shop:
                //商店
            {
                Intent shopV = new Intent(getActivity(), FourShopActivity.class);
                shopV.putExtra("mm_fuwu_type", "0" );
                startActivity(shopV);
            }
                break;
            case R.id.relate_bank:
                //银行

                break;
            case R.id.relate_work:
                //装车工人
            {
                Intent shopV = new Intent(getActivity(), FourShopActivity.class);
                shopV.putExtra("mm_fuwu_type", "1" );
                startActivity(shopV);
            }
                break;
            case R.id.relate_wuliu:
                //物流中心
            {
                Intent shopV = new Intent(getActivity(), FourShopActivity.class);
                shopV.putExtra("mm_fuwu_type", "2" );
                startActivity(shopV);
            }
                break;
            case R.id.relate_jiajie:
                //嫁接
            {
                Intent shopV = new Intent(getActivity(), FourShopActivity.class);
                shopV.putExtra("mm_fuwu_type", "3" );
                startActivity(shopV);
            }
                break;
            case R.id.relate_msg:
                //短信
                break;
            case R.id.realte_diaoche:
                //调车
            {
                Intent shopV = new Intent(getActivity(), FourShopActivity.class);
                shopV.putExtra("mm_fuwu_type", "4" );
                startActivity(shopV);
            }
                break;
            case R.id.relate_about:
                //关于我们
            {
                Intent aboutV = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(aboutV);
            }
                break;
            case R.id.realte_ziliao:
                //用户资料
            {
                Intent profileV = new Intent(getActivity(), ProfileActivity.class);
                profileV.putExtra("id", getGson().fromJson(getSp().getString("mm_emp_id", ""), String.class));
                startActivity(profileV);
            }
                break;
            case R.id.relate_updatepwr:
                //修改密码
            {
                Intent updateP = new Intent(getActivity(), UpdatePwrActivity.class);
                startActivity(updateP);
            }
                break;
            case R.id.relate_suggest:
                //意见反馈
            {
                Intent suggestV = new Intent(getActivity(), AddSuggestActivity.class);
                startActivity(suggestV);
            }
                break;
        }
    }
}

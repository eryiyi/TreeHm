package com.Lbins.TreeHm;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.fragment.FirstFragment;
import com.Lbins.TreeHm.fragment.FourFragment;
import com.Lbins.TreeHm.fragment.SecondFragment;
import com.Lbins.TreeHm.fragment.ThreeFragment;
import com.Lbins.TreeHm.ui.LoginActivity;
import com.Lbins.TreeHm.util.HttpUtils;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    /**
     * Called when the activity is first created.
     */

    private FragmentTransaction fragmentTransaction;
    private FragmentManager fm;

    private FirstFragment oneFragment;
    private SecondFragment twoFragment;
    private ThreeFragment threeFragment;
    private FourFragment fourFragment;

    private ImageView foot_one;
    private ImageView foot_two;
    private ImageView foot_three;
    private ImageView foot_four;
    private ImageView foot_five;

    private long waitTime = 2000;
    private long touchTime = 0;

    //设置底部图标
    Resources res;

    private int index;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        UmengUpdateAgent.update(this);
        res = getResources();
        fm = getSupportFragmentManager();
        initView();

        switchFragment(R.id.foot_one);

    }

    boolean isMobileNet, isWifiNet;

    @Override
    public void onClick(View v) {
        try {
            isMobileNet = HttpUtils.isMobileDataEnable(getApplicationContext());
            isWifiNet = HttpUtils.isWifiDataEnable(getApplicationContext());
            if (!isMobileNet && !isWifiNet) {
                Toast.makeText(this, "当前网络连接不可用", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if("0".equals(getGson().fromJson(getSp().getString("mm_emp_mobile", ""), String.class)) &&  (v.getId() == R.id.foot_four)){
            //未登录
            Intent loginV = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginV);
        }else {
            switchFragment(v.getId());
        }

    }

    private void initView() {
        foot_one = (ImageView) this.findViewById(R.id.foot_one);
        foot_two = (ImageView) this.findViewById(R.id.foot_two);
        foot_three = (ImageView) this.findViewById(R.id.foot_three);
        foot_four = (ImageView) this.findViewById(R.id.foot_four);
        foot_one.setOnClickListener(this);
        foot_two.setOnClickListener(this);
        foot_three.setOnClickListener(this);
        foot_four.setOnClickListener(this);

    }


    public void switchFragment(int id) {
        fragmentTransaction = fm.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (id) {
            case R.id.foot_one:
                if (oneFragment == null) {
                    oneFragment = new FirstFragment();
                    fragmentTransaction.add(R.id.content_frame, oneFragment);
                } else {
                    fragmentTransaction.show(oneFragment);
                }
                foot_one.setImageResource(R.drawable.tree_toolbar_wanted_p);
                foot_two.setImageResource(R.drawable.tree_toolbar_sell);
                foot_three.setImageResource(R.drawable.tree_toolbar_star);
                foot_four.setImageResource(R.drawable.tree_toolbar_user);

                break;
            case R.id.foot_two:
                if (twoFragment == null) {
                    twoFragment = new SecondFragment();
                    fragmentTransaction.add(R.id.content_frame, twoFragment);
                } else {
                    fragmentTransaction.show(twoFragment);
                }

                foot_one.setImageResource(R.drawable.tree_toolbar_wanted);
                foot_two.setImageResource(R.drawable.tree_toolbar_sell_p);
                foot_three.setImageResource(R.drawable.tree_toolbar_star);
                foot_four.setImageResource(R.drawable.tree_toolbar_user);
                break;
            case R.id.foot_three:
                if (threeFragment == null) {
                    threeFragment = new ThreeFragment();
                    fragmentTransaction.add(R.id.content_frame, threeFragment);
                } else {
                    fragmentTransaction.show(threeFragment);
                }
                foot_one.setImageResource(R.drawable.tree_toolbar_wanted);
                foot_two.setImageResource(R.drawable.tree_toolbar_sell);
                foot_three.setImageResource(R.drawable.tree_toolbar_star_p);
                foot_four.setImageResource(R.drawable.tree_toolbar_user);
                break;
            case R.id.foot_four:
                if (fourFragment == null) {
                    fourFragment = new FourFragment();
                    fragmentTransaction.add(R.id.content_frame, fourFragment);
                } else {
                    fragmentTransaction.show(fourFragment);
                }
                foot_one.setImageResource(R.drawable.tree_toolbar_wanted);
                foot_two.setImageResource(R.drawable.tree_toolbar_sell);
                foot_three.setImageResource(R.drawable.tree_toolbar_star);
                foot_four.setImageResource(R.drawable.tree_toolbar_user_p);
                break;

        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction ft) {
        if (oneFragment != null) {
            ft.hide(oneFragment);
        }
        if (twoFragment != null) {
            ft.hide(twoFragment);
        }
        if (threeFragment != null) {
            ft.hide(threeFragment);
        }
        if (fourFragment != null) {
            ft.hide(fourFragment);
        }
    }


}

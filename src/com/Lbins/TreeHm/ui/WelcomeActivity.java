package com.Lbins.TreeHm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.base.BaseActivity;

/**
 * Created by Administrator on 2016/2/18.
 */
public class WelcomeActivity extends BaseActivity implements View.OnClickListener,Runnable {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        // 启动一个线程
        new Thread(WelcomeActivity.this).start();
    }
    @Override
    public void onClick(View view) {

    }
    @Override
    public void run() {
        try {
            // 3秒后跳转到登录界面
            Thread.sleep(3000);
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

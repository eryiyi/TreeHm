package com.Lbins.TreeHm.ui;

import android.os.Bundle;
import android.view.View;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.base.BaseActivity;

/**
 * Created by Administrator on 2016/4/3 0003.
 */
public class ErweimaActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.erweima_activity);

        this.findViewById(R.id.back).setOnClickListener(this);
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

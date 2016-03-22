package com.Lbins.TreeHm.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.adapter.ItemWeixinAdapter;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.module.WeixinObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghailong on 2016/3/20.
 */
public class WeixinKefuActivity extends BaseActivity implements View.OnClickListener {
    private ListView lstv;
    private ItemWeixinAdapter adapter;
    List<WeixinObj> lists = new ArrayList<WeixinObj>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weixin_kefu_activity);

        lists.add(new WeixinObj("微信号一","13290271666"));
        lists.add(new WeixinObj("微信号二","13405437638"));

        this.findViewById(R.id.back).setOnClickListener(this);
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemWeixinAdapter(lists, WeixinKefuActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //
            }
        });
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

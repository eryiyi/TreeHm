package com.Lbins.TreeHm.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.adapter.ItemDetailPhotoAdapter;
import com.Lbins.TreeHm.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/22.
 */
public class DetailRecordActivity extends BaseActivity implements View.OnClickListener {
    private ImageView head;
    private TextView nickname;
    private TextView dateline;
    private LinearLayout liner_type;
    private ImageView type_one;
    private ImageView type_two;
    private ImageView type_three;
    private TextView content;
    private GridView gridView;
    private ImageView sharebtn;
    private TextView telbtn;

    private List<String> lists = new ArrayList<String>();
    ItemDetailPhotoAdapter adapterPhot ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_record_activity);
        initView();
    }

    void initView(){
        this.findViewById(R.id.mback).setOnClickListener(this);
        head = (ImageView) this.findViewById(R.id.head);
        nickname = (TextView) this.findViewById(R.id.nickname);
        dateline = (TextView) this.findViewById(R.id.dateline);
        liner_type = (LinearLayout) this.findViewById(R.id.liner_type);
        type_one = (ImageView) this.findViewById(R.id.type_one);
        type_two = (ImageView) this.findViewById(R.id.type_two);
        type_three = (ImageView) this.findViewById(R.id.type_three);
        content = (TextView) this.findViewById(R.id.content);
        gridView = (GridView) this.findViewById(R.id.gridView);
        sharebtn = (ImageView) this.findViewById(R.id.sharebtn);
        telbtn = (TextView) this.findViewById(R.id.telbtn);

        sharebtn.setOnClickListener(this);
        telbtn.setOnClickListener(this);
        head.setOnClickListener(this);

        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        adapterPhot = new ItemDetailPhotoAdapter(lists, DetailRecordActivity.this);
        gridView.setAdapter(adapterPhot);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mback:
                finish();
                break;
            case R.id.sharebtn:
                //分享
                break;
            case R.id.telbtn:
                //电话
                break;
            case R.id.head:
                //头像
                break;
        }
    }
}

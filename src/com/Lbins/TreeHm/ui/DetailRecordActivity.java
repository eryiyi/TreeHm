package com.Lbins.TreeHm.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.UniversityApplication;
import com.Lbins.TreeHm.adapter.AnimateFirstDisplayListener;
import com.Lbins.TreeHm.adapter.ItemDetailPhotoAdapter;
import com.Lbins.TreeHm.base.BaseActivity;
import com.Lbins.TreeHm.module.RecordVO;
import com.Lbins.TreeHm.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private List<String> lists = new ArrayList<String>();
    ItemDetailPhotoAdapter adapterPhot ;

    private RecordVO recordVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_record_activity);
        recordVO = (RecordVO) getIntent().getExtras().get("info");
        initView();
        initData();
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

        adapterPhot = new ItemDetailPhotoAdapter(lists, DetailRecordActivity.this);
        gridView.setAdapter(adapterPhot);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

    }

    void initData(){
        //
        imageLoader.displayImage(recordVO.getMm_emp_cover(), head, UniversityApplication.txOptions, animateFirstListener);
        nickname.setText(recordVO.getMm_emp_nickname());
        dateline.setText(recordVO.getDateline());
        content.setText(recordVO.getMm_msg_content());
        if("1".equals(recordVO.getIs_chengxin())){
            type_one.setVisibility(View.VISIBLE);
        }else {
            type_one.setVisibility(View.GONE);
        }
        if("1".equals(recordVO.getIs_miaomu())){
            type_two.setVisibility(View.VISIBLE);
        }else {
            type_two.setVisibility(View.GONE);
        }
        switch (Integer.parseInt((recordVO.getMm_level_num()==null?"0":recordVO.getMm_level_num()))){
            case 0:
                type_three.setImageResource(R.drawable.tree_icons_star_1);
                break;
            case 1:
                type_three.setImageResource(R.drawable.tree_icons_star_2);
                break;
            case 2:
                type_three.setImageResource(R.drawable.tree_icons_star_3);
                break;
            case 3:
                type_three.setImageResource(R.drawable.tree_icons_star_4);
                break;
            case 4:
                type_three.setImageResource(R.drawable.tree_icons_star_5);
                break;
        }
        telbtn.setText(recordVO.getMm_emp_mobile());
        if(!StringUtil.isNullOrEmpty(recordVO.getMm_msg_picurl())){
            final String[] picUrls = recordVO.getMm_msg_picurl().split(",");//图片链接切割
            for(String str:picUrls){
                lists.add(str);
            }
            adapterPhot.notifyDataSetChanged();
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(DetailRecordActivity.this, GalleryUrlActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    intent.putExtra(Constants.IMAGE_URLS, picUrls);
                    intent.putExtra(Constants.IMAGE_POSITION, position);
                    startActivity(intent);
                }
            });
        }
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
                showTel(recordVO.getMm_emp_mobile());
                break;
            case R.id.head:
                //头像
                break;
        }
    }

    // 拨打电话窗口
    private void showTel(String tel) {
        final Dialog picAddDialog = new Dialog(DetailRecordActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(DetailRecordActivity.this, R.layout.tel_dialog, null);
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
}

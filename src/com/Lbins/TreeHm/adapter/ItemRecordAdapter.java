package com.Lbins.TreeHm.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.Lbins.TreeHm.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemRecordAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<String> lists;
    private Context mContect;
    Resources res;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }


    public ItemRecordAdapter(List<String> lists, Context mContect){
        this.lists = lists;
        this.mContect = mContect;
    }
    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        res = mContect.getResources();
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_record,null);
            holder.btn_share = (ImageView) convertView.findViewById(R.id.btn_share);
            holder.btn_tel = (ImageView) convertView.findViewById(R.id.btn_tel);
//            holder.sell_price = (TextView) convertView.findViewById(R.id.sell_price);
//            holder.item_title = (TextView) convertView.findViewById(R.id.item_title);
//            holder.price = (TextView) convertView.findViewById(R.id.price);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
//        final ProducteObj cell = lists.get(position);
//        if(cell != null){
//            String title = cell.getProduct_name()==null?"":cell.getProduct_name();
//            if(title.length() > 13){
//                title = title.substring(0,12) + "...";
//            }
//            holder.item_title.setText(title);
//            holder.sell_price.setText( res.getString(R.string.money) + cell.getPrice_tuangou());
//            holder.price.setText( res.getString(R.string.money) + cell.getPrice());
//            holder.price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
////            holder.item_pic.setImageResource(cell.getImg());
//            imageLoader.displayImage( cell.getProduct_pic(), holder.item_pic, UniversityApplication.options, animateFirstListener);
//        }


        //
        holder.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 1, null);
            }
        });
        holder.btn_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 2, null);
            }
        });



        return convertView;
    }
    class ViewHolder {
        ImageView btn_share;
        ImageView btn_tel;
        TextView item_title;
        TextView sell_price;
        TextView price;
    }
}

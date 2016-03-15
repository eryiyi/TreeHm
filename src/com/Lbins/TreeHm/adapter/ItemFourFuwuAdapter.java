package com.Lbins.TreeHm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.module.CityObj;
import com.Lbins.TreeHm.module.FuwuObj;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemFourFuwuAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<FuwuObj> lists;
    private Context mContect;
    private String mm_fuwu_type;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public ItemFourFuwuAdapter(List<FuwuObj> lists, Context mContect,String mm_fuwu_type){
        this.lists = lists;
        this.mContect = mContect;
        this.mm_fuwu_type = mm_fuwu_type;
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
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_four_fuwu,null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);
            holder.tel = (TextView) convertView.findViewById(R.id.tel);
            holder.weiwangzhan = (TextView) convertView.findViewById(R.id.weiwangzhan);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final FuwuObj cell = lists.get(position);
        if(cell != null){
            String strname = "";
            switch (Integer.parseInt(mm_fuwu_type)){
                case 0:
                    strname = "商店名称:";
                    holder.weiwangzhan.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    strname = "工人名称:";
                    holder.weiwangzhan.setVisibility(View.GONE);
                    break;
                case 2:
                    strname = "物流:";
                    holder.weiwangzhan.setVisibility(View.GONE);
                    break;
                case 3:
                    strname = "团队名称:";
                    holder.weiwangzhan.setVisibility(View.GONE);
                    break;
                case 4:
                    strname = "";
                    holder.weiwangzhan.setVisibility(View.GONE);
                    break;

                };
            holder.title.setText(strname + cell.getMm_fuwu_nickname());
            holder.address.setText("服务内容:"+cell.getMm_fuwu_content());
            holder.tel.setText(cell.getMm_fuwu_tel());
            if(cell.getDistance() != null){
                holder.distance.setText(cell.getDistance()==null?"未知距离":cell.getDistance());
            }
            holder.weiwangzhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickContentItemListener.onClickContentItem(position, 1, null);
                }
            });
            holder.tel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickContentItemListener.onClickContentItem(position, 2, null);
                }
            });
        }
        return convertView;
    }
    class ViewHolder {
        TextView title;
        TextView address;
        TextView distance;
        TextView tel;
        TextView weiwangzhan;
//        TextView content;
    }
}

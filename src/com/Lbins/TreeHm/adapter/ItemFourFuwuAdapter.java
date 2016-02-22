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

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public ItemFourFuwuAdapter(List<FuwuObj> lists, Context mContect){
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
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_four_fuwu,null);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);
            holder.tel = (TextView) convertView.findViewById(R.id.tel);
//            holder.content = (TextView) convertView.findViewById(R.id.content);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final FuwuObj cell = lists.get(position);
        if(cell != null){
            holder.title.setText(cell.getMm_fuwu_nickname());
            holder.address.setText(cell.getMm_fuwu_content());
            holder.tel.setText(cell.getMm_fuwu_tel());
            if(cell.getDistance() != null){
                holder.distance.setText(cell.getDistance()==null?"未知距离":cell.getDistance());
            }
        }
        return convertView;
    }
    class ViewHolder {
        TextView title;
        TextView address;
        TextView distance;
        TextView tel;
//        TextView content;
    }
}

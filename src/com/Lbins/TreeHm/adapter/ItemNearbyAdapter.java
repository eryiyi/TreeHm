package com.Lbins.TreeHm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.UniversityApplication;
import com.Lbins.TreeHm.module.Emp;

import com.Lbins.TreeHm.util.StringUtil;
import com.amap.api.maps.model.LatLng;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemNearbyAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<Emp> lists;
    private Context mContect;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public ItemNearbyAdapter(List<Emp> lists, Context mContect){
        this.lists = lists;
        this.mContect = mContect;
    }
    public void refresh(List<Emp> d) {
        lists = d;
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_nearby,null);
            holder.head = (ImageView) convertView.findViewById(R.id.head);
            holder.nickname = (TextView) convertView.findViewById(R.id.nickname);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);
            holder.img_xinyong = (ImageView) convertView.findViewById(R.id.img_xinyong);
            holder.img_xiehui = (ImageView) convertView.findViewById(R.id.img_xiehui);
            holder.star = (ImageView) convertView.findViewById(R.id.star);


            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Emp cell = lists.get(position);
        if(cell != null){
            String title = (cell.getMm_emp_company()==null?"":cell.getMm_emp_company()) +" "+ (cell.getMm_emp_nickname()==null?"":cell.getMm_emp_nickname());
            holder.nickname.setText(title);
//            holder.distance.setText(cell.getDateline());
            LatLng latLng = new LatLng(Double.valueOf(UniversityApplication.lat), Double.valueOf(UniversityApplication.lng));
            LatLng latLng1 = new LatLng(Double.valueOf(cell.getLat()), Double.valueOf(cell.getLng()));
            String distance = StringUtil.getDistance(latLng ,latLng1 );
            holder.distance.setText(distance+"km");
            if("1".equals(cell.getIs_chengxin())){
                holder.img_xinyong.setVisibility(View.VISIBLE);
            }else {
                holder.img_xinyong.setVisibility(View.GONE);
            }
            if("1".equals(cell.getIs_miaomu())){
                holder.img_xiehui.setVisibility(View.VISIBLE);
            }else {
                holder.img_xiehui.setVisibility(View.GONE);
            }
            switch (Integer.parseInt((cell.getMm_level_num()==null?"0":cell.getMm_level_num()))){
                case 0:
                    holder.star.setImageResource(R.drawable.tree_icons_star_1);
                    break;
                case 1:
                    holder.star.setImageResource(R.drawable.tree_icons_star_2);
                    break;
                case 2:
                    holder.star.setImageResource(R.drawable.tree_icons_star_3);
                    break;
                case 3:
                    holder.star.setImageResource(R.drawable.tree_icons_star_4);
                    break;
                case 4:
                    holder.star.setImageResource(R.drawable.tree_icons_star_5);
                    break;
            }
            imageLoader.displayImage(cell.getMm_emp_cover(), holder.head, UniversityApplication.txOptions, animateFirstListener);

        }

        return convertView;
    }
    class ViewHolder {
        ImageView head;
        TextView nickname;
        TextView distance;
        ImageView img_xinyong;
        ImageView img_xiehui;
        ImageView star;

    }
}

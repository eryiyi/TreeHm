package com.Lbins.TreeHm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.Lbins.TreeHm.R;
import com.Lbins.TreeHm.UniversityApplication;
import com.Lbins.TreeHm.dao.DBHelper;
import com.Lbins.TreeHm.dao.RecordMsg;
import com.Lbins.TreeHm.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2015/5/27.
 */
public class ItemRecordAdapter extends BaseAdapter {
    private ViewHolder holder;
    private List<RecordMsg> lists;
    private Context mContect;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private OnClickContentItemListener onClickContentItemListener;

    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public ItemRecordAdapter(List<RecordMsg> lists, Context mContect) {
        this.lists = lists;
        this.mContect = mContect;
    }

    public void refresh(List<RecordMsg> d) {
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContect).inflate(R.layout.item_record, null);
            holder.btn_share = (ImageView) convertView.findViewById(R.id.btn_share);
            holder.btn_tel = (ImageView) convertView.findViewById(R.id.btn_tel);
            holder.btn_pic = (ImageView) convertView.findViewById(R.id.btn_pic);
            holder.head = (ImageView) convertView.findViewById(R.id.head);
            holder.nickname = (TextView) convertView.findViewById(R.id.nickname);
            holder.dateline = (TextView) convertView.findViewById(R.id.dateline);
//            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.img_xinyong = (ImageView) convertView.findViewById(R.id.img_xinyong);
            holder.img_xiehui = (ImageView) convertView.findViewById(R.id.img_xiehui);
            holder.star = (ImageView) convertView.findViewById(R.id.star);
            holder.is_read = (ImageView) convertView.findViewById(R.id.is_read);
            holder.btn_favour = (ImageView) convertView.findViewById(R.id.btn_favour);
            holder.btn_nav = (ImageView) convertView.findViewById(R.id.btn_nav);
            holder.btn_video = (ImageView) convertView.findViewById(R.id.btn_video);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final RecordMsg cell = lists.get(position);
        if (cell != null) {
            String title = (cell.getMm_emp_company() == null ? "" : cell.getMm_emp_company()) + " " + (cell.getMm_emp_nickname() == null ? "" : cell.getMm_emp_nickname());
            holder.nickname.setText(title);
            holder.dateline.setText((cell.getDateline() == null ? "" : cell.getDateline()) + " " + (cell.getArea() == null ? "" : cell.getArea()));
            String msg = cell.getMm_msg_content() == null ? "" : cell.getMm_msg_content();
            holder.content.setText(msg);
            if ("1".equals(cell.getIs_chengxin())) {
                holder.img_xinyong.setVisibility(View.VISIBLE);
            } else {
                holder.img_xinyong.setVisibility(View.GONE);
            }
            if ("1".equals(cell.getIs_miaomu())) {
                holder.img_xiehui.setVisibility(View.VISIBLE);
            } else {
                holder.img_xiehui.setVisibility(View.GONE);
            }
            switch (Integer.parseInt((cell.getMm_level_num() == null ? "0" : cell.getMm_level_num()))) {
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
            if(!StringUtil.isNullOrEmpty(cell.getMm_msg_video())){
                //说明存在视频
                holder.btn_video.setVisibility(View.VISIBLE);
                holder.btn_pic.setVisibility(View.GONE);
            }else {
                holder.btn_video.setVisibility(View.GONE);
                //不存在视频
                if (StringUtil.isNullOrEmpty(cell.getMm_msg_picurl())) {
                    holder.btn_pic.setVisibility(View.GONE);
                } else {
                    holder.btn_pic.setVisibility(View.VISIBLE);
                }
            }

            RecordMsg recordMsg = DBHelper.getInstance(mContect).getRecord(cell.getMm_msg_id());
            if (recordMsg != null) {
                if ("1".equals(recordMsg.getIs_read())) {
                    //已读
                    holder.is_read.setImageResource(R.drawable.tree_icons_read_1);
                } else {
                    holder.is_read.setImageResource(R.drawable.tree_icons_read_0);
                }
            } else {
                if ("1".equals(cell.getIs_read())) {
                    //已读
                    holder.is_read.setImageResource(R.drawable.tree_icons_read_1);
                } else {
                    holder.is_read.setImageResource(R.drawable.tree_icons_read_0);
                }
            }

            if (!StringUtil.isNullOrEmpty(UniversityApplication.fontSize)) {
                holder.content.setTextSize(Float.valueOf(UniversityApplication.fontSize));
                holder.nickname.setTextSize(Float.valueOf(UniversityApplication.fontSize));
                holder.dateline.setTextSize(Float.valueOf(UniversityApplication.fontSize));
            }
            if (!StringUtil.isNullOrEmpty(UniversityApplication.fontColor)) {
                if ("black".equals(UniversityApplication.fontColor)) {
                    holder.content.setTextColor(Color.BLACK);
                    holder.nickname.setTextColor(Color.BLACK);
                    holder.dateline.setTextColor(Color.BLACK);
                }
                if ("gray".equals(UniversityApplication.fontColor)) {
                    holder.content.setTextColor(Color.GRAY);
                    holder.nickname.setTextColor(Color.GRAY);
                    holder.dateline.setTextColor(Color.GRAY);
                }
                if ("blue".equals(UniversityApplication.fontColor)) {
                    holder.content.setTextColor(Color.BLUE);
                    holder.nickname.setTextColor(Color.BLUE);
                    holder.dateline.setTextColor(Color.BLUE);
                }
                if ("orange".equals(UniversityApplication.fontColor)) {
                    holder.content.setTextColor(Color.YELLOW);
                    holder.nickname.setTextColor(Color.YELLOW);
                    holder.dateline.setTextColor(Color.YELLOW);
                }
                if ("red".equals(UniversityApplication.fontColor)) {
                    holder.content.setTextColor(Color.RED);
                    holder.nickname.setTextColor(Color.RED);
                    holder.dateline.setTextColor(Color.RED);
                }
            }

//            if(position % 2 == 0){
//                //偶数
//                convertView.setBackgroundColor(Color.argb(250, 255, 255, 255)); //颜色设置
//            }else {
//                convertView.setBackgroundColor(Color.argb(255, 224, 243, 250));//颜色设置
//            }
        }

        //
        holder.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 1, "111");
            }
        });
        holder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 2, "111");
            }
        });
        holder.btn_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 3, "111");
            }
        });
        holder.nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 4, "111");
            }
        });
        holder.btn_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 5, "111");
            }
        });
        holder.btn_favour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 6, "111");
            }
        });
        holder.btn_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 7, "111");//导航
            }
        });
        holder.btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickContentItemListener.onClickContentItem(position, 8, "111");
            }
        });


        return convertView;
    }

    public static class ViewHolder {
        ImageView btn_share;
        ImageView btn_pic;
        ImageView btn_tel;
        ImageView head;
        TextView nickname;
        TextView dateline;
        TextView content;
        ImageView img_xinyong;
        ImageView img_xiehui;
        ImageView star;
        ImageView is_read;
        ImageView btn_favour;
        ImageView btn_nav;
        ImageView btn_video;
    }
}

package com.qianfeng.slidingitem.listview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qianfeng.slidingitem.ItemInfo;
import com.qianfeng.slidingitem.R;
import com.qianfeng.slidingitem.SlidingItemView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class ListViewSlidingItemAdapter extends BaseAdapter {

    private Context context;

    private List<ItemInfo> list;

    public ListViewSlidingItemAdapter(Context context, List<ItemInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_sliding_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ItemInfo info = list.get(position);
        holder.tv.setText(info.getText());
        holder.slidingItemView.setOnHideViewClickListener(
                new SlidingItemView.OnHideViewClickListener()
                {
                    @Override
                    public void onClick(View view, int position)
                    {
                        Log.i("info", "position=" + position);
                        Log.i("info", "text=" + list.get(position).getText());
                    }
                });
        holder.slidingItemView.bindViewAndData(convertView,
                SlidingItemView.HideViewMode.MODE_HIDE_RIGHT, list, position);
        return convertView;
    }

    class ViewHolder {
        TextView tv;

        SlidingItemView slidingItemView;

        ViewHolder(View convertView) {
            tv = (TextView) convertView.findViewById(R.id.tv);
            slidingItemView = (SlidingItemView) convertView.findViewById(R.id.item_view);
        }
    }
}

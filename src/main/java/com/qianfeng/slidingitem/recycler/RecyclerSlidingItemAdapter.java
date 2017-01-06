package com.qianfeng.slidingitem.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qianfeng.slidingitem.ItemInfo;
import com.qianfeng.slidingitem.R;
import com.qianfeng.slidingitem.SlidingItemView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class RecyclerSlidingItemAdapter
        extends RecyclerView.Adapter<RecyclerSlidingItemAdapter.BaseViewHolder>
{
    private OnItemClickListener onItemClickListener;

    private Context context;

    private List<ItemInfo> list;

    public RecyclerSlidingItemAdapter(Context context, List<ItemInfo> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View convertView = LayoutInflater.from(context)
                .inflate(R.layout.adapter_sliding_item, parent, false);
        return new BaseViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder,
            final int position)
    {
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
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (onItemClickListener != null)
                {
                    onItemClickListener.onItemClick(v,
                            holder.getLayoutPosition());
                }
            }
        });
        holder.slidingItemView.bindViewAndData(holder.itemView,
                SlidingItemView.HideViewMode.MODE_HIDE_RIGHT, list,
                holder.getLayoutPosition());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class BaseViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv;

        SlidingItemView slidingItemView;

        BaseViewHolder(View convertView)
        {
            super(convertView);
            tv = (TextView) convertView.findViewById(R.id.tv);
            slidingItemView = (SlidingItemView) convertView
                    .findViewById(R.id.item_view);
        }
    }

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }
}

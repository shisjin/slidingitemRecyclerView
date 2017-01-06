package com.qianfeng.slidingitem.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ListView;

import com.qianfeng.slidingitem.SlidingItemView;

/**
 * Created by Administrator on 2016/9/30.
 */
public class ListViewWithSlidingItem extends ListView {

    public ListViewWithSlidingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean isFirst;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            isFirst = true;
        }
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            ViewGroup convertView = (ViewGroup) getChildAt(i);
            SlidingItemView itemview = (SlidingItemView) convertView.getTag(convertView.getId());
            if (itemview.isOpen()) {
                return false;
            }
        }
        boolean flag = super.onInterceptTouchEvent(ev);
        if (flag) {
            if (isFirst) {
                for (int i = 0; i < count; i++) {
                    ViewGroup convertView = (ViewGroup) getChildAt(i);
                    if(convertView==null){
                        return flag;
                    }
                    SlidingItemView itemview = (SlidingItemView) convertView.getTag(convertView.getId());
                    if (itemview.getScrollX() != 0) {
                        itemview.scrollToEnd(null, 0, 200);
                    }
                }
                isFirst = false;
            }
        }
        return flag;
    }

}

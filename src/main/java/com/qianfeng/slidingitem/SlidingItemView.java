package com.qianfeng.slidingitem;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class SlidingItemView extends RelativeLayout
{
    private Scroller mScroller;

    private float downX, dispatchDownX, dispatchDownY;

    private boolean isOpen;

    private VelocityTracker mVelocityTracker;

    private View convertView;

    private TextView hideView;

    private int position;

    private List<Object> list;

    private int hideViewMode;

    public OnHideViewClickListener onHideViewClickListener;

    /**
     * 手指是否up
     */
    private boolean isActionUp = false;

    private int hideViewWidth;

    public SlidingItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public void setHideView(int hideViewMode)
    {
        this.hideViewMode = hideViewMode;
        if (hideView == null)
        {
            createHideView();
            switch (hideViewMode)
            {
                case HideViewMode.MODE_HIDE_BOTTOM:
                    if (convertView instanceof RelativeLayout)
                    {
                        ((RelativeLayout) convertView).addView(hideView);
                        RelativeLayout.LayoutParams params = (LayoutParams) hideView
                                .getLayoutParams();
                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        hideView.setLayoutParams(params);
                        bringToFront();
                    }
                    break;
                case HideViewMode.MODE_HIDE_RIGHT:
                    View container = getChildAt(0);
                    addView(hideView);
                    RelativeLayout.LayoutParams params = (LayoutParams) hideView
                            .getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    hideView.setLayoutParams(params);

                    params = (LayoutParams) container.getLayoutParams();
                    params.rightMargin = hideViewWidth;
                    container.setLayoutParams(params);

                    ViewGroup.LayoutParams viewGroupLp = getLayoutParams();
                    if (viewGroupLp instanceof LinearLayout.LayoutParams)
                    {
                        ((LinearLayout.LayoutParams) viewGroupLp).rightMargin = -hideViewWidth;
                        setLayoutParams(viewGroupLp);
                    }
                    if (viewGroupLp instanceof RelativeLayout.LayoutParams)
                    {
                        ((RelativeLayout.LayoutParams) viewGroupLp).rightMargin = -hideViewWidth;
                        setLayoutParams(viewGroupLp);
                    }
                    if (viewGroupLp instanceof FrameLayout.LayoutParams)
                    {
                        ((FrameLayout.LayoutParams) viewGroupLp).rightMargin = -hideViewWidth;
                        setLayoutParams(viewGroupLp);
                    }
                    break;
            }
        }
    }

    private void createHideView()
    {
        hideViewWidth = (int) (convertView.getLayoutParams().height * 1.5f);
        hideView = new TextView(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                hideViewWidth, LayoutParams.MATCH_PARENT);
        hideView.setLayoutParams(params);
        hideView.setBackgroundColor(Color.RED);
        hideView.setText("删除");
        hideView.setTextColor(Color.WHITE);
        hideView.setGravity(Gravity.CENTER);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                dispatchDownX = event.getX();
                dispatchDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(dispatchDownX - event.getX()) < 10
                        && Math.abs(dispatchDownY - event.getY()) < 10)
                {
                    View parent = (View) convertView.getParent();
                    if (canRemove())
                    {
                        int width = hideViewWidth;
                        if (hideViewMode == HideViewMode.MODE_HIDE_RIGHT)
                        {
                            width = hideViewWidth * 2;
                        }
                        else if (hideViewMode == HideViewMode.MODE_HIDE_BOTTOM)
                        {
                            width = hideViewWidth;
                        }
                        if (dispatchDownX < getWidth() - width)
                        {
                            ViewGroup viewGroup = (ViewGroup) parent;
                            int count = viewGroup.getChildCount();
                            for (int i = 0; i < count; i++)
                            {
                                View convertView = viewGroup.getChildAt(i);
                                SlidingItemView slidingItemView = (SlidingItemView) convertView
                                        .getTag(convertView.getId());
                                if (slidingItemView.canRemove()
                                        && slidingItemView != this)
                                {
                                    slidingItemView.scrollToEnd(null, 0, 200);
                                }
                            }
                        }
                        return super.dispatchTouchEvent(event);
                    }

                    ViewGroup viewGroup = (ViewGroup) parent;
                    int count = viewGroup.getChildCount();
                    boolean isExistOpenItem = false;
                    for (int i = 0; i < count; i++)
                    {
                        View convertView = viewGroup.getChildAt(i);
                        SlidingItemView slidingItemView = (SlidingItemView) convertView
                                .getTag(convertView.getId());
                        if (slidingItemView.canRemove())
                        {
                            isExistOpenItem = true;
                            slidingItemView.scrollToEnd(null, 0, 200);
                        }
                    }
                    if (!isExistOpenItem)
                    {
                        if (viewGroup instanceof ListView)
                        {
                            ((ListView) viewGroup).performItemClick(convertView,
                                    position, 0);
                        }
                        else if (viewGroup instanceof RecyclerView)
                        {
                            convertView.performClick();
                        }
                    }
                    return false;
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        createVelocityTracker(event);
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX() + getScrollX();
                if (canRemove()
                        && event.getX() > getWidth() - hideView.getWidth())
                {
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int distance = (int) (event.getX() - downX);
                if (getScrollX() > hideView.getWidth() / 10)
                {
                    isOpen = true;
                }
                // 当左移,滑动操作被禁用
                if (distance > 0)
                {
                    scrollTo(0, 0);
                    return false;
                }
                else if (distance < -hideView.getWidth())
                {
                    scrollTo(hideView.getWidth(), 0);
                    return false;
                }
                scrollTo(-distance, 0);
                break;
            case MotionEvent.ACTION_UP:
                // 当右滑距离超过item控件宽度的一半就将其移除,否则还原
                if (Math.abs(getScrollX()) > hideView.getWidth() / 2)
                {
                    scrollToEnd(mVelocityTracker, hideView.getWidth(), 200);
                }
                else
                {
                    scrollToEnd(mVelocityTracker, 0, 200);
                }
                isOpen = false;
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    public void bindViewAndData(final View convertView, int hideViewMode,
            final List list, final int position)
    {
        scrollTo(0, 0);
        this.list = list;
        this.convertView = convertView;
        this.convertView.setTag(convertView.getId(), this);
        setHideView(hideViewMode);
        this.position = position;
        this.hideView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    List<View> views = new ArrayList<>();
                    ViewGroup parent = (ViewGroup) convertView.getParent();
                    int count = parent.getChildCount();

                    for (int i = count - 1; i >= 0; i--)
                    {
                        View childView = parent.getChildAt(i);
                        SlidingItemView slidingItemView = (SlidingItemView) childView
                                .getTag(childView.getId());
                        if (slidingItemView.canRemove())
                        {
                            views.add(childView);
                            if (parent instanceof ListView)
                            {
                                // TODO执行删除按钮的点击事件
                                if (slidingItemView.onHideViewClickListener != null)
                                {
                                    slidingItemView.onHideViewClickListener
                                            .onClick(slidingItemView,
                                                    slidingItemView.position);
                                }
                                slidingItemView.hideItemAnimation(parent);
                            }
                            else if (parent instanceof RecyclerView)
                            {
                                int pos = ((RecyclerView) parent)
                                        .getChildViewHolder(childView)
                                        .getLayoutPosition();
                                // TODO执行删除按钮的点击事件
                                if (slidingItemView.onHideViewClickListener != null)
                                {
                                    slidingItemView.onHideViewClickListener
                                            .onClick(slidingItemView, pos);
                                }
                                list.remove(pos);
                                (((RecyclerView) parent).getAdapter())
                                        .notifyItemRemoved(pos);
                            }

                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public void hideItemAnimation(final ViewGroup parent)
    {
        final int height = convertView.getHeight();
        ObjectAnimator objectAnimator = ObjectAnimator
                .ofFloat(this, "xxx", 0, 1).setDuration(200);
        final SlidingItemView slidingItemView = (SlidingItemView) convertView
                .getTag(convertView.getId());
        objectAnimator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
                {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation)
                    {
                        float progress = (float) animation.getAnimatedValue();
                        // Log.i("info", progress + "");
                        if (convertView != null)
                        {
                            ViewGroup.LayoutParams params = convertView
                                    .getLayoutParams();
                            params.height = (int) (height - progress * height);
                            convertView.setLayoutParams(params);
                            // slidingItem.scrollTo(
                            // (int) (hideView.getWidth() * (1 - progress)),
                            // 0);
                        }
                        if (progress > 0.95f)
                        {
                            animation.cancel();
                            if (parent instanceof ListView)
                            {
                                list.remove(slidingItemView.getPosition());
                                ((BaseAdapter) ((ListView) parent).getAdapter())
                                        .notifyDataSetChanged();
                            }
                            // else if (parent instanceof RecyclerView)
                            // {
                            // list.remove(slidingItem.getPosition());
                            // ((RecyclerView.Adapter) ((RecyclerView) parent)
                            // .getAdapter()).notifyDataSetChanged();
                            // }
                            ViewGroup.LayoutParams params = convertView
                                    .getLayoutParams();
                            params.height = height;
                            convertView.setLayoutParams(params);
                            slidingItemView.scrollTo(0, 0);
                        }
                    }
                });
        objectAnimator.start();
    }

    public void scrollToEnd(VelocityTracker velocityTracker, int end,
            int duration)
    {
        isActionUp = true;
        // 当前的偏移位置
        int scrollX = getScrollX();
        int velocityX = 0;
        if (velocityTracker != null)
        {
            velocityTracker.computeCurrentVelocity(1000);
            velocityX = (int) velocityTracker.getXVelocity();
        }
        int distance = 0;
        if (velocityX > 1000)
        {
            distance = -scrollX;
        }
        else if (velocityX < -1000)
        {
            distance = end - scrollX;
        }
        else
        {
            distance = end - scrollX;
        }
        mScroller.startScroll(scrollX, 0, distance, 0, duration);
        // 此时需要手动刷新View 否则没效果
        invalidate();
    }

    @Override
    public void computeScroll()
    {
        // 如果返回true，表示动画还没有结束
        // 因为前面startScroll，所以只有在startScroll完成时 才会为false
        if (mScroller.computeScrollOffset() && isActionUp)
        {
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            // 产生了动画效果 每次滚动一点
            scrollTo(x, y);
            // 刷新View 否则效果可能有误差
            postInvalidate();
        }
        else
        {
            if (isActionUp)
            {
                isActionUp = false;
            }
        }
    }

    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     */
    private void createVelocityTracker(MotionEvent event)
    {
        if (mVelocityTracker == null)
        {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker()
    {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    public boolean isOpen()
    {
        return isOpen;
    }

    public int getPosition()
    {
        return position;
    }

    public boolean canRemove()
    {
        if (getScrollX() > hideView.getWidth() - 15)
        {
            return true;
        }
        return false;
    }

    private int dip2px(float dipValue)
    {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        return (int) (dipValue * dm.density + 0.5F);
    }

    public interface HideViewMode
    {
        int MODE_HIDE_BOTTOM = 0;

        int MODE_HIDE_RIGHT = 1;
    }

    public interface OnHideViewClickListener
    {
        void onClick(View view, int position);
    }

    public void setOnHideViewClickListener(
            OnHideViewClickListener onHideViewClickListener)
    {
        this.onHideViewClickListener = onHideViewClickListener;
    }

    public class DoNotRemoveDataException extends RuntimeException
    {
        public DoNotRemoveDataException(String detailMessage)
        {
            super(detailMessage);
        }
    }
}

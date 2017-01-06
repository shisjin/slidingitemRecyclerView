package com.qianfeng.slidingitem.recycler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.qianfeng.slidingitem.ItemInfo;
import com.qianfeng.slidingitem.R;
import com.qianfeng.slidingitem.recycler.callback.OnItemMoveListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerSlidingActivity extends AppCompatActivity {

    private RecyclerViewWithSlidingItem recyclerView;

    private List<ItemInfo> list;

    private RecyclerSlidingItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_sliding);
        recyclerView = (RecyclerViewWithSlidingItem) findViewById(
                R.id.recycler);
        recyclerView.setLongPressDragEnabled(true);
        recyclerView.setOnItemMoveListener(onItemMoveListener);
        initListView();
    }

    /**
     * 当Item移动的时候。
     */
    private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            // 当Item被拖拽的时候。
            Log.i("info", "fromPosition=" + fromPosition + ";toPosition=" + toPosition);
            swipePosition(fromPosition, toPosition);
            adapter.notifyItemMoved(fromPosition, toPosition);
            return true;// 返回true表示处理了，返回false表示你没有处理。
        }

        @Override
        public void onItemDismiss(int position) {

        }
    };

    private void swipePosition(int fromPosition, int toPosition) {
        if (fromPosition > toPosition) {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        } else {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        }
    }

    private void initListView() {
        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ItemInfo info = new ItemInfo();
            info.setText("position=" + i);
            list.add(info);
        }
        adapter = new RecyclerSlidingItemAdapter(this, list);
        adapter.setOnItemClickListener(
                new RecyclerSlidingItemAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.i("info",
                                "OnItemClick=" + list.get(position).getText());
                    }
                });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);
    }
}

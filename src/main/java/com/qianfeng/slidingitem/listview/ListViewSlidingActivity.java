package com.qianfeng.slidingitem.listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.qianfeng.slidingitem.ItemInfo;
import com.qianfeng.slidingitem.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewSlidingActivity extends AppCompatActivity
{

    private ListViewWithSlidingItem listview;

    private List<ItemInfo> list;

    private ListViewSlidingItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_sliding);
        listview = (ListViewWithSlidingItem) findViewById(
                R.id.listview);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("info",
                        "OnItemClick=" + list.get(position).getText());
            }
        });
        initListView();
    }

    private void initListView()
    {
        list = new ArrayList<>();
        for (int i = 0; i < 100; i++)
        {
            ItemInfo info = new ItemInfo();
            info.setText("position=" + i);
            list.add(info);
        }
        adapter = new ListViewSlidingItemAdapter(this, list);
        listview.setAdapter(adapter);
    }
}

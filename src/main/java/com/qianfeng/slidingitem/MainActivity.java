package com.qianfeng.slidingitem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.qianfeng.slidingitem.listview.ListViewSlidingActivity;
import com.qianfeng.slidingitem.recycler.RecyclerSlidingActivity;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_list:
                startActivity(new Intent(this, ListViewSlidingActivity.class));
                break;
            case R.id.btn_recycler:
                startActivity(new Intent(this, RecyclerSlidingActivity.class));
                break;
        }
    }
}

package com.example.benjamin.statusbardemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.benjamin.statusbardemo.R;
import com.example.benjamin.statusbardemo.common.StatusBarUtils;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    ImageView toolBarBg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setTranslucent(this);
        setContentView(R.layout.activity_main);
        StatusBarUtils.fitStatusBarTop(this, findViewById(R.id.contentPanel));
        toolBarBg = (ImageView) findViewById(R.id.toolBarBg);
        Glide.with(this).load(R.drawable.ic_toolbar).into(toolBarBg);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photoDetailExit:
                startActivity(new Intent(this, PhotoDetailActivity.class));
                break;
        }
    }
}

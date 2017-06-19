package com.example.benjamin.statusbardemo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.benjamin.statusbardemo.R;
import com.example.benjamin.statusbardemo.common.StatusBarUtils;

public class CoordinatorLayoutActivity extends AppCompatActivity {

    private ImageView topImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setTranslucent(this);
        setContentView(R.layout.activity_coordinator_layout);
        topImage = (ImageView) findViewById(R.id.topImage);
        Glide.with(this).load("http://img.hb.aicdn.com/e5faed6ba249d259798b843fcea760424d6e4d606dfdb-3x6xeN_fw658")
                .into(topImage);
    }
}

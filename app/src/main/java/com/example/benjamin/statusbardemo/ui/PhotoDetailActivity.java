package com.example.benjamin.statusbardemo.ui;

import android.os.Bundle;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.bumptech.glide.Glide;
import com.example.benjamin.statusbardemo.R;
import com.example.benjamin.statusbardemo.common.TestImageUriApi;
import com.github.chrisbanes.photoview.OnSingleFlingListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.orhanobut.logger.Logger;

/**
 * Created by Benjamin on 2017/6/14.
 */

public class PhotoDetailActivity extends FragmentActivity {
    private PhotoView photoDetail;
    private VelocityTracker velocityTracker;
    private float downY; //X/Y方向速度相关的帮助类
    private float distance = 0;
    private float distanceThreshold = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        photoDetail = (PhotoView) findViewById(R.id.photoDetail);
        Glide.with(this).load(TestImageUriApi.getOneImageUri()).into(photoDetail);
        //弹性动画
        velocityTracker = VelocityTracker.obtain();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //获取当前点击的Y轴方向
                downY = event.getY();
                //想车速类添加此事件
                velocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_MOVE:
                float cDistance = Math.abs(event.getY() - downY);
                //当图片的缩放率为1的时候（也就是原图状态），图片跟着手指移动
                if (photoDetail.getScale() == 1 && Math.abs(cDistance - distance) > distanceThreshold) {
                    photoDetail.setTranslationY(event.getY() - downY);
                    velocityTracker.addMovement(event);
                    distance = cDistance;
                    return false;
                } else {
                    distance = cDistance;
                    //有缩放率的时候交给父类处理
                    return super.dispatchTouchEvent(event);
                }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (event.getPointerCount() == 1) {
                    //当手指抬起或者动作取消的时候应该处理的事情
                    velocityTracker.computeCurrentVelocity(500);
                    if (photoDetail.getTranslationY() != 0) {
                        SpringAnimation animY = new SpringAnimation(photoDetail, SpringAnimation.TRANSLATION_Y, 0);
                        animY.getSpring().setStiffness(3000.0f);
                        animY.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
                        animY.setStartVelocity(velocityTracker.getYVelocity());
                        animY.start();
                    }
                    velocityTracker.clear();
                }
        }
        return super.dispatchTouchEvent(event);
    }

}

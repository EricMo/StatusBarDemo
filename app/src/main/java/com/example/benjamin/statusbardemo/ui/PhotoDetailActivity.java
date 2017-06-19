package com.example.benjamin.statusbardemo.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;

import com.bumptech.glide.Glide;
import com.example.benjamin.statusbardemo.R;
import com.example.benjamin.statusbardemo.common.HardWareInfoUtils;
import com.example.benjamin.statusbardemo.common.TestImageUriApi;
import com.github.chrisbanes.photoview.OnSingleFlingListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.orhanobut.logger.Logger;

import java.util.Map;

/**
 * Created by Benjamin on 2017/6/14.
 */

public class PhotoDetailActivity extends FragmentActivity {
    private PhotoView photoDetail;
    private VelocityTracker velocityTracker;
    //初始的Y坐标
    private float downY;
    ////第一根手指按下的Y坐标、最后一根手指按下的坐标
    private MotionEvent firstPoint, multiPoint;
    //手指之间的距离
    private float distance = 0;
    //距离偏移量，由他来决定是上下或者放大
    private static final float distanceOffset = 50;
    //图像限定偏移量
    private static float exitOffset = 0;
    private View alphaContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        photoDetail = (PhotoView) findViewById(R.id.photoDetail);
        Glide.with(this).load(TestImageUriApi.getOneImageUri()).into(photoDetail);
        alphaContent = findViewById(R.id.contentContainer);
        //弹性动画
        velocityTracker = VelocityTracker.obtain();
        //当控件初始化完成的回调
        photoDetail.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获取到图像高度并且计算阈值
                exitOffset = photoDetail.getHeight() / 2;
                Logger.d("图像限定偏移量:" + exitOffset);
                //避免重复，移除监听器
                photoDetail.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //开始点击Y坐标
                downY = event.getY();
                velocityTracker.addMovement(event);
                firstPoint = MotionEvent.obtainNoHistory(event);
                Logger.d("第一根手指" + firstPoint.getY());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                velocityTracker.addMovement(event);
                //大于1根手指的触碰点
                multiPoint = MotionEvent.obtainNoHistory(event);
                Logger.d("最后一根手指" + multiPoint.getY(multiPoint.getPointerCount() - 1));
                //计算当手机触摸到屏幕时的距离（不管有多少手指，我们只取最后一根）
                distance = getDistance();
                Logger.d("两点之间距离：" + distance);
                break;
            case MotionEvent.ACTION_MOVE:
                //计算图片的偏移带来的alpha效果
                alphaContent.setBackgroundColor(getARGBColor());
                //当前手指距离以及当前偏移量
                float cDis = 0, offset = 0;
                if (distance != 0) {
                    //计算距离
                    cDis = spacing(event);
                    //计算当前的偏移量
                    offset = Math.abs(cDis - distance);
                    //重新赋值
                    distance = cDis;
                    Logger.d("滑动时两点之间的距离变化：" + cDis + "    阈值：" + offset);
                }
                //当图片的缩放率为1的时候（也就是原图状态）&&一根手指（一根手指上下滑动）||当前偏移量小于固定阈值，就使用上下滑动
                if (photoDetail.getScale() == 1 && (event.getPointerCount() == 1 ||
                        (offset < distanceOffset))) {
                    photoDetail.setTranslationY(event.getY() - downY);
                    velocityTracker.addMovement(event);
                    return false;
                } else {
                    //上述条件不满足的时候交给父类处理
                    return super.dispatchTouchEvent(event);
                }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:
                //当手指抬起或者动作取消的时候应该处理的事情
                velocityTracker.computeCurrentVelocity(500);
                //此处获取图片在Y轴的偏移量，大于
                Logger.d("图片偏移量：" + photoDetail.getTranslationY());
                if (Math.abs(photoDetail.getTranslationY()) <= exitOffset) {
                    //构建弹性动画
                    SpringAnimation animY = new SpringAnimation(photoDetail, SpringAnimation.TRANSLATION_Y, 0);
                    //回弹快慢
                    animY.getSpring().setStiffness(SpringForce.STIFFNESS_LOW);
                    //物理效果
                    animY.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
                    animY.setStartVelocity(velocityTracker.getYVelocity());
                    animY.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() {
                        @Override
                        public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                            //计算图片的偏移带来的alpha效果
                            alphaContent.setBackgroundColor(getARGBColor());
                        }
                    });
                    animY.start();
                } else {
                    ObjectAnimator animator = ObjectAnimator.ofFloat(photoDetail, "translationY", photoDetail
                            .getTranslationY(), photoDetail.getTranslationY() < 0 ? -exitOffset * 2 : exitOffset * 2);
                    animator.setDuration(300);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            finish();
                        }
                    });
                    animator.start();
                }
                velocityTracker.clear();
                //重置距离
                distance = 0;
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 获取初始距离
     *
     * @return
     */
    private float getDistance() {
        float x = firstPoint.getX() - multiPoint.getX(multiPoint.getPointerCount() - 1);
        float y = firstPoint.getY() - multiPoint.getY(multiPoint.getPointerCount() - 1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 计算动态距离
     *
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX() - event.getX(event.getPointerCount() - 1);
        float y = event.getY() - event.getY(event.getPointerCount() - 1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 获取ARGB颜色
     *
     * @return
     */
    private int getARGBColor() {
        //计算图片的偏远带来的alpha效果
        int alpha = (int) ((exitOffset * 2 - Math.abs(photoDetail.getTranslationY())) * 255 /
                (exitOffset * 2));
        Logger.d("图像透明度：" + alpha);
        int color = Color.argb(alpha, 0, 0, 0);
        return color;
    }

}

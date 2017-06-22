package com.example.benjamin.statusbardemo.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.benjamin.statusbardemo.R;
import com.example.benjamin.statusbardemo.adapter.PhotoAdapter;
import com.example.benjamin.statusbardemo.common.TestImageUriApi;
import com.orhanobut.logger.Logger;

/**
 * Created by Benjamin on 2017/6/22.
 */

public class PhotoDetailWithViewPagerActivity extends FragmentActivity {
    //Y距离偏移量，由他来决定是上下或者放大
    private static final float DISTANCEOFFSET = 50;
    //水平上下偏移
    private static final float HORIZONTALOFFSET = 100;

    private ViewPager photoContainer;
    private VelocityTracker velocityTracker;
    //初始的X,Y坐标
    private float downY, cDownY, cDownX;
    //手指之间的距离
    private float distance = 0;
    //图像限定偏移量
    private float exitOffset = 0;
    //是否为横向滑动
    private boolean isHorizontal = false;
    //
    private boolean isNeed = true;
    //alpha可变图层
    private View alphaContent;
    //图片适配器
    private PhotoAdapter photoAdapter = new PhotoAdapter(TestImageUriApi.imageUri, this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail_view_pager);
        photoContainer = (ViewPager) findViewById(R.id.photoContainer);
        alphaContent = findViewById(R.id.contentContainer);
        //弹性动画
        velocityTracker = VelocityTracker.obtain();
        //当控件初始化完成的回调
        photoContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获取到图像高度并且计算阈值
                exitOffset = photoContainer.getHeight() / 2;
                Logger.d("图像限定偏移量:" + exitOffset);
                //避免重复，移除监听器
                photoContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        photoContainer.setAdapter(photoAdapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //开始点击Y坐标
                cDownY = downY = event.getY();
                cDownX = event.getX();
                velocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                velocityTracker.addMovement(event);
                //计算当手机触摸到屏幕时的距离（不管有多少手指，我们只取最后一根）
                distance = spacing(event);
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
                //如果当前显示的图片的缩放率为1（也就是没进行缩放），那么则有滑动歧义
                if (photoAdapter.getCurrentView().getScale() == 1) {
                    //当次数满足以后，则不再进行判断||当当前的手指在Y轴的上下偏移量小于固定阈值，说明满足横向滑动
                    if (Math.abs(event.getY(0) - cDownY) < HORIZONTALOFFSET) {
                        cDownY = event.getY(0);
                        isHorizontal = true;
                    } else if ((event.getPointerCount() == 1 || offset < DISTANCEOFFSET)) {
                        //如果当前是一根手指在滑动||在Y轴的滑动偏移量小于Y轴偏移量阈值，就认为是上下滑动
                        photoContainer.setTranslationY(event.getY() - downY);
                        velocityTracker.addMovement(event);
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_UP:

                    //当手指抬起或者动作取消的时候应该处理的事情
                    velocityTracker.computeCurrentVelocity(500);
                    //此处获取图片在Y轴的偏移量，大于
                    Logger.d("图片偏移量：" + photoContainer.getTranslationY());
                    if (Math.abs(photoContainer.getTranslationY()) <= exitOffset) {
                        //构建弹性动画
                        SpringAnimation animY = new SpringAnimation(photoContainer, SpringAnimation.TRANSLATION_Y, 0);
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
                        ObjectAnimator animator = ObjectAnimator.ofFloat(photoContainer, "translationY", photoContainer
                                .getTranslationY(), photoContainer.getTranslationY() < 0 ? -exitOffset * 2 :
                                exitOffset *
                                        2);
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
                    //重置shake变量
                    isHorizontal = false;
                    isNeed = true;

        }
        return super.dispatchTouchEvent(event);
    }

//    /**
//     * 获取初始距离
//     *
//     * @return
//     */
//    private float getDistance() {
//        float x = firstPoint.getX() - multiPoint.getX(multiPoint.getPointerCount() - 1);
//        float y = firstPoint.getY() - multiPoint.getY(multiPoint.getPointerCount() - 1);
//        return (float) Math.sqrt(x * x + y * y);
//    }

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
        int alpha = (int) ((exitOffset * 2 - Math.abs(photoContainer.getTranslationY())) * 255 /
                (exitOffset * 2));
        Logger.d("图像透明度：" + alpha);
        int color = Color.argb(alpha, 0, 0, 0);
        return color;
    }

}

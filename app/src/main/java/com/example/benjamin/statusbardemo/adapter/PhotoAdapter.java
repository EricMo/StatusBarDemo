package com.example.benjamin.statusbardemo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Created by Benjamin on 2017/6/22.
 */

public class PhotoAdapter extends PagerAdapter {

    private PhotoView currentView;

    //图片来源数据
    private String[] datas;

    //上下文
    private Context mContext;

    public PhotoAdapter(String[] datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return datas.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 当视图被销毁的时候所执行的方法，比如：比如最初我们设置当前留存3个实例，当滑动到第三页的时候，
     * 第一页就会被回收，此时，Glide还在加载第一页，所以我们需要取消加载Glide.clear，并且将此view移除
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //获取当前的大图控件
        PhotoView photoView = (PhotoView) object;
        //为null就不做操作
        if (photoView == null)
            return;
        //取消加载这个图片
        Glide.clear(photoView);
        //移除
        container.removeView(photoView);
    }


    /**
     * 当viewpager页面创建一个新实力的时候，比如最初我们设置当前留存3个实例，当滑动到第三页的时候，就会加载第4页就会调用此方法进行初始化
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //创建大图控件
        PhotoView photoView = new PhotoView(mContext);
        //大图充满屏幕
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        photoView.setLayoutParams(layoutParams);
        //加入页面
        container.addView(photoView);
        //加载图片
        Glide.with(mContext)
                .load(datas[position])
                .into(photoView);
        return photoView;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (object instanceof PhotoView)
            this.currentView = (PhotoView) object;
        super.setPrimaryItem(container, position, object);
    }

    public PhotoView getCurrentView() {
        return currentView;
    }
}

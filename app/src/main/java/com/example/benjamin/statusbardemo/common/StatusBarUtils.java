package com.example.benjamin.statusbardemo.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Benjamin on 2017/1/16.
 */

public class StatusBarUtils {
    private static final String TAG = "status";

    static class BUILD_MIUI {
        static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
        static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
        static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    }

    public static void setTranslucent(Activity activity) {
        setTransparentForWindow(activity);
        setTranslucentStatus(activity, true);
        setStatusBarColor(activity, Color.TRANSPARENT);
    }

    /**
     * 设置透明
     */
    private static void setTransparentForWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams
                            .FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 使状态栏透明
     * 这种透明方式会透明虚拟按键
     * 在某些虚拟按键不能隐藏的手机上会遮挡住内容，因此不推荐使用
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 此Flag会透明虚拟按键
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 设置状态栏颜色，次方法只针对5.0+
     *
     * @param activity 需要设置的界面
     * @param color    设置颜色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(color);
        }
    }


    /**
     * miui系统设置黑色状态栏文字,miuiv6只支持4.4及以上版本
     *
     * @param darkMode 是否为黑色主题
     * @param activity
     * @return 是个改变成功
     */
    private static void setMIUIStatusBarDarkMode(boolean darkMode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkMode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     */
    public static void statusBarDarkMode(Activity activity, boolean darkMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isMIUI()) {
                setMIUIStatusBarDarkMode(darkMode, activity);
            } else if (isFlyme()) {
                StatusbarColorUtils.setStatusBarDarkIcon(activity, darkMode);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                                .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    /**
     * 判断是否为miui系统
     *
     * @return
     */
    private static boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(BUILD_MIUI.KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(BUILD_MIUI.KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(BUILD_MIUI.KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    /**
     * 判断是否为flyme
     *
     * @return
     */
    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    public static void fitStatusBarTop(Context context, View view) {
        view.setPadding(0, HardWareInfoUtils.getStatusBarHeight(context), 0, 0);
    }

}

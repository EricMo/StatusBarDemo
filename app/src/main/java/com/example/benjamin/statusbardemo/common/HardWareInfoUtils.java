package com.example.benjamin.statusbardemo.common;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Created by wangf on 2016/2/22.
 */
public class HardWareInfoUtils {

    /**
     * 状态栏高度
     */
    private static int STATUS_BAR_HEIGHT = 0;

    /**
     * 获取状态栏高度
     *
     * @param mContext 上下文
     * @return
     */
    public static int getStatusBarHeight(Context mContext) {
        if (STATUS_BAR_HEIGHT == 0) {
            STATUS_BAR_HEIGHT = mContext.getResources().getSystem().getDimensionPixelSize(
                    mContext.getResources().getSystem().getIdentifier("status_bar_height",
                            "dimen", "android"));
        }
        return STATUS_BAR_HEIGHT;
    }


    public static Map<String, Object> getScreenIno(Context mContext) {
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Map<String, Object> info = new HashMap<>();
        info.put("density", density);
        info.put("width", width);
        info.put("height", height);
        return info;
    }
}

package com.yunhetong.sdk.fast;

import android.content.Context;
import android.content.res.Resources;

public class YhtResourceFinder {

    /**
     * @param context
     * @param resName 资源名
     */
    public static int findContentView(Context context, String resName) {
        Resources resources = context.getResources();
        //资源名 资源文件夹名 包名
        int indentify = resources.getIdentifier(resName, "layout", context.getPackageName());
        if (indentify > 0) {
            return indentify;
        } else {
            throw new Resources.NotFoundException("云合同的布局文件资源未找到，请检查");
        }
    }

    public static int findStyle(Context context, String resName) {
        Resources resources = context.getResources();
        //资源名 资源文件夹名 包名
        int indentify = resources.getIdentifier(resName, "style", context.getPackageName());
        if (indentify > 0) {
            return indentify;
        } else {
            throw new Resources.NotFoundException("云合同的资源未找到，请检查");
        }
    }

    /**
     * @param context
     * @param resName 资源名
     */
    public static int findView(Context context, String resName) {
        Resources resources = context.getResources();
        //资源名 资源文件夹名 包名
        int indentify = resources.getIdentifier(resName, "id", context.getPackageName());
        if (indentify > 0) {
            return indentify;
        } else {
            throw new Resources.NotFoundException("云合同的布局文件资源未找到，请检查");
        }
    }

    public static int findDrawable(Context context, String resName) {
        Resources resources = context.getResources();
        int indentify = resources.getIdentifier(resName, "drawable", context.getPackageName());
        if (indentify > 0) {
            return indentify;
        } else {
            throw new Resources.NotFoundException("云合同的图片资源未找到，请检查");
        }

    }

    public static int findValues(Context context, String resName) {
        Resources resources = context.getResources();
        int indentify = resources.getIdentifier(resName, "values", context.getPackageName());
        if (indentify > 0) {
            return indentify;
        } else {
            throw new Resources.NotFoundException("云合同的资源未找到，请检查文件");
        }

    }


    public static int findColor(Context context, String color) {
        Resources resources = context.getResources();
        int indentify = resources.getIdentifier(color, "color", context.getPackageName());
        if (indentify > 0) {
            return indentify;
        } else {
            throw new Resources.NotFoundException("云合同的色彩资源未找到，请检查文件");
        }

    }
}

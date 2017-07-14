package com.dover.glide;

import android.content.Context;
import android.net.Uri;

/**
 * Created by d on 2017/7/14.
 */
public class Utils {


    //1. 一个小助手功能：简单的从资源 id 转换成 Uri
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    public static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }


    //2.

}

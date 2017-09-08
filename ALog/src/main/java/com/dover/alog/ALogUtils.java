package com.dover.alog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.dover.alog.utils.PackageUtils;

/**
 * Created by d on 2017/9/8.
 */
public class ALogUtils {

    // init this in u application
    public static void initALog(@NonNull Context context) {
        ALog.LogConfig logConfig = ALog.init(context)   // context 为 自定义的 application 对象
                .setLogSwitch(BuildConfig.DEBUG)    // 设置log总开关，包括输出到控制台和文件，默认开
                .setConsoleSwitch(BuildConfig.DEBUG)// 设置是否输出到控制台开关，默认开
                .setGlobalTag(null)     // 设置log全局标签，默认为空
                // 当全局标签不为空时，我们输出的log全部为该全局tag，
                // 为空时，如果传入的tag为空那就显示类名，否则显示tag
//                .setAppNameTag(PackageUtils.getAppName(context))    //GlobalTag 为空时 才有效
                .setAppNameTag("ALog")    //GlobalTag 为空时 才有效
                .setLogHeadSwitch(true)     // 设置log头信息开关，默认为开
                .setLog2FileSwitch(false)   // 打印log时是否存到文件的开关，默认关
                .setDir("")         // 当自定义路径为空时，写入应用的/cache/log/目录中
                .setFilePrefix("")  // 当文件前缀为空时，默认为"alog"，即写入文件为"alog-MM-dd.txt"
                .setBorderSwitch(true)      // 输出日志是否带边框开关，默认开
                .setConsoleFilter(ALog.V)   // log的控制台过滤器，和logcat过滤器同理，默认Verbose
                .setFileFilter(ALog.V)      // log文件过滤器，和logcat过滤器同理，默认Verbose
                .setStackDeep(1);           // log栈深度，默认为1
        ALog.d(logConfig.toString());
    }
}

package com.dover.rxproject.seeweather.util.dlog;

import android.content.Context;
import android.support.annotation.NonNull;


/**
 * Created by d on 2017/9/8.
 */
public class DLogUtils {

    // init this in u application
    public static void initDLog(@NonNull Context context) {
        DLog.LogConfig logConfig = DLog.init(context)   // context 为 自定义的 application 对象
//                .setLogOpen(BuildConfig.DEBUG)          // 设置log总开关，包括输出到控制台和文件，默认开
//                .setConsoleOpen(BuildConfig.DEBUG)      // 设置是否输出到控制台开关，默认开
                .setGlobalTag(null)         // 设置log全局标签，默认为 AppName
                // 当全局标签不为空时，我们输出的log全部为该全局tag，
                // 为空时，如果传入的tag为空那就显示 AppName+类名，否则显示tag
                .setLog2FileOpen(false)     // 设置是否将log存到文件的开关，默认关
                .setDir("")                 // 当自定义路径为空时，写入应用的/cache/log/目录中
                .setFilePrefix("")          // 当文件前缀为空时，默认为"alog"，即写入文件为"alog-MM-dd.txt"
                .setBorderOpen(true)        // 输出日志是否带边框开关，默认开
                .setConsoleFilter(DLog.V)   // log的控制台过滤器，和logcat过滤器同理，默认Verbose
                .setFileFilter(DLog.V)      // log文件过滤器，和logcat过滤器同理，默认Verbose
                .setStackDeep(1);           // log栈深度，默认为1
        DLog.d(logConfig.toString());
    }

}

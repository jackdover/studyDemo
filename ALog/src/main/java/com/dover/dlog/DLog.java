package com.dover.dlog;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.dover.alog.utils.PackageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by d on 2017/9/8.
 */
public class DLog {


    public static final int V = Log.VERBOSE;    //2
    public static final int D = Log.DEBUG;      //3
    public static final int I = Log.INFO;       //4
    public static final int W = Log.WARN;       //5
    public static final int E = Log.ERROR;      //6
    public static final int A = Log.ASSERT;     //7

    @IntDef({V, D, I, W, E, A})
    @Retention(RetentionPolicy.SOURCE)
    private @interface TYPE {
    }

    private static final char[] T = new char[]{'V', 'D', 'I', 'W', 'E', 'A'};

    private static final int FILE = 0x10;
    private static final int JSON = 0x20;
    private static final int XML = 0x30;


    private static String sGlobalTag = null;    // 全局log标签

    private static boolean sLogOpen = true;           // log总开关，默认开
    private static boolean sLog2ConsoleOpen = true;   // logcat是否打印，默认打印
    private static boolean sLog2FileOpen = false;     // log写入文件开关，默认关

    private static int sConsoleFilter = V;      // log控制台过滤器
    private static int sFileFilter = V;         // log文件过滤器

    private static int sStackDeep = 1;          // log栈深度

    //private static boolean sLogHeadOpen = true;   // log头部开关，默认开
    private static boolean sLogBorderOpen = true;  // log边框开关，默认开


    private static final String NULL_TIPS = "Log with null object.";
    private static final String NULL = "null";
    private static final String ARGS = "args: ";

    private static final String FILE_SEP = System.getProperty("file.separator");
    private static final String LINE_SEP = System.getProperty("line.separator");

    private static final String TOP_BORDER = "┌────────────────────────────────────────────────────────────────────────────────";
    private static final String SPLIT_BORDER = "├─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─";
    private static final String LEFT_BORDER = "│ ";
    private static final String BOTTOM_BORDER = "└────────────────────────────────────────────────────────────────────────────────";

    private static final int MAX_LEN = 4000;

    private static String sDefaultDir;      // log默认存储目录
    private static String sDir;             // log存储目录

    private static String sFilePrefix = "dlog"; // log文件前缀

    private static ExecutorService sExecutor;   //异步操作, 线程池, 文件写入


    private DLog() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static LogConfig sConfig;

    // 初始化配置 --- 比如: 开关, 级别, 是否存文件, 是否上传等等
    public static LogConfig init(@NonNull Context context) {
        if (sConfig == null) {
            sConfig = new LogConfig(context);
        }
        return sConfig;
    }


    public static void v(final Object contents) {
        log(V, null, contents);
    }

    public static void v(final String tag, final Object... contents) {
        log(V, tag, contents);
    }

    public static void d(final Object contents) {
        log(D, null, contents);
    }

    public static void d(final String tag, final Object... contents) {
        log(D, tag, contents);
    }

    public static void i(final Object contents) {
        log(I, null, contents);
    }

    public static void i(final String tag, final Object... contents) {
        log(I, tag, contents);
    }

    public static void w(final Object contents) {
        log(W, null, contents);
    }

    public static void w(final String tag, final Object... contents) {
        log(W, tag, contents);
    }

    public static void e(final Object contents) {
        log(E, null, contents);
    }

    public static void e(final String tag, final Object... contents) {
        log(E, tag, contents);
    }

    public static void a(final Object contents) {
        log(A, null, contents);
    }

    public static void a(final String tag, final Object... contents) {
        log(A, tag, contents);
    }

    public static void file(final Object contents) {
        log(FILE | D, null, contents);
    }

    public static void file(@TYPE final int type, final Object contents) {
        log(FILE | type, null, contents);
    }

    public static void file(final String tag, final Object contents) {
        log(FILE | D, tag, contents);
    }

    public static void file(@TYPE final int type, final String tag, final Object contents) {
        log(FILE | type, tag, contents);
    }

    public static void json(final String contents) {
        log(JSON | D, null, contents);
    }

    public static void json(@TYPE final int type, final String contents) {
        log(JSON | type, null, contents);
    }

    public static void json(final String tag, final String contents) {
        log(JSON | D, tag, contents);
    }

    public static void json(@TYPE final int type, final String tag, final String contents) {
        log(JSON | type, tag, contents);
    }

    public static void xml(final String contents) {
        log(XML | D, null, contents);
    }

    public static void xml(@TYPE final int type, final String contents) {
        log(XML | type, null, contents);
    }

    public static void xml(final String tag, final String contents) {
        log(XML | D, tag, contents);
    }

    public static void xml(@TYPE final int type, final String tag, final String contents) {
        log(XML | type, tag, contents);
    }


    ////////////////////////////////////////////////////////////////////////////////////////

    private static class TagHead {
        String tag;
        String[] consoleHead;
        String fileHead;

        TagHead(String tag, String[] consoleHead, String fileHead) {
            this.tag = sGlobalTag + tag; // 默认在所有tag 前加 appName
            this.consoleHead = consoleHead;
            this.fileHead = fileHead;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    private static void log(final int type, final String tag, final Object... contents) {
        // 开关   总关, 或者 (控制台关 && 文件关)      --- 不需要日志输出
        if (!sLogOpen || (!sLog2ConsoleOpen && !sLog2FileOpen)) return;
        // 级别   低于配置级别,--- 不需要日志输出
        int type_low = type & 0x0f, type_high = type & 0xf0;
        if (type_low < sConsoleFilter && type_low < sFileFilter) return;
        // 头信息
        final TagHead tagHead = processTagAndHead(tag);
        // 内容信息
        String body = processBody(type_high, contents);
        // 打印到控制台
        if (sLog2ConsoleOpen && type_low >= sConsoleFilter && type_high != FILE) {
            print2Console(type_low, tagHead.tag, tagHead.consoleHead, body);
        }
        // 写入到文件
        if ((sLog2FileOpen || type_high == FILE) && type_low >= sFileFilter) {
            print2File(type_low, tagHead.tag, tagHead.fileHead + body);
        }
    }

    // 头信息
    private static TagHead processTagAndHead(String tag) {
        final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        StackTraceElement targetElement = stackTrace[3];    //TODO why
        String fileName = targetElement.getFileName();
        String className = fileName.substring(0, fileName.indexOf('.'));
        tag = TextUtils.isEmpty(tag) ? className : tag;     //为空就是类名
//        if (!sLogHeadOpen) {     //头信息关闭
//            return new TagHead(tag, null, ": ");
//        }
        String threadName = Thread.currentThread().getName();
        final String head = new Formatter()
                .format("%s, %s(%s:%d)",
                        threadName,
                        targetElement.getMethodName(),
                        fileName,
                        targetElement.getLineNumber())
                .toString();
        final String fileHead = " [" + head + "]: ";
        if (sStackDeep <= 1) {
            return new TagHead(tag, new String[]{head}, fileHead);
        } else {
            final String[] consoleHead = new String[Math.min(sStackDeep, stackTrace.length - 3)];
            consoleHead[0] = head;
            int spaceLen = threadName.length() + 2;
            String space = new Formatter().format("%" + spaceLen + "s", "").toString();
            for (int i = 1, len = consoleHead.length; i < len; ++i) {
                targetElement = stackTrace[i + 3];
                consoleHead[i] = new Formatter()
                        .format("%s%s(%s:%d)",
                                space,
                                targetElement.getMethodName(),
                                targetElement.getFileName(),
                                targetElement.getLineNumber())
                        .toString();
            }
            return new TagHead(tag, consoleHead, fileHead);
        }
    }

    // 内容信息
    private static String processBody(final int type, final Object... contents) {
        String body = NULL_TIPS;
        if (contents != null) {
            if (contents.length == 1) {
                Object object = contents[0];
                body = object == null ? NULL : object.toString();
                if (type == JSON) {
                    body = formatJson(body);
                } else if (type == XML) {
                    body = formatXml(body);
                }
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0, len = contents.length; i < len; ++i) {
                    Object content = contents[i];
                    sb.append(ARGS)
                            .append("[")
                            .append(i)
                            .append("]")
                            .append(" = ")
                            .append(content == null ? NULL : content.toString())
                            .append(LINE_SEP);
                }
                body = sb.toString();
            }
        }
        return body;
    }

    // JSON 格式
    private static String formatJson(String json) {
        try {
            if (json.startsWith("{")) {
                json = new JSONObject(json).toString(4);
            } else if (json.startsWith("[")) {
                json = new JSONArray(json).toString(4);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    // XML 格式
    private static String formatXml(String xml) {
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(xmlInput, xmlOutput);
            xml = xmlOutput.getWriter().toString().replaceFirst(">", ">" + LINE_SEP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }


    // 打印到控制台
    private static void print2Console(final int type, final String tag, final String[] head, String msg) {
        printBorder(type, tag, true);   // 上边框
        printHead(type, tag, head);     // 头
        printMsg(type, tag, msg);       // 内容
        printBorder(type, tag, false);  // 下边框
    }

    private static void printBorder(final int type, final String tag, boolean isTop) {
        if (sLogBorderOpen) {
            Log.println(type, tag, isTop ? TOP_BORDER : BOTTOM_BORDER);
        }
    }

    private static void printHead(final int type, final String tag, final String[] head) {
        if (head != null) {
            for (String aHead : head) {
                Log.println(type, tag, sLogBorderOpen ? LEFT_BORDER + aHead : aHead); //是否加边框
            }
            Log.println(type, tag, SPLIT_BORDER);
        }
    }

    private static void printMsg(final int type, final String tag, final String msg) {
        int len = msg.length();
        int countOfSub = len / MAX_LEN;
        if (countOfSub > 0) {   //超过最大长度
            int index = 0;
            for (int i = 0; i < countOfSub; i++) {  //前几行
                printSubMsg(type, tag, msg.substring(index, index + MAX_LEN));
                index += MAX_LEN;
            }
            if (index != len) {                     //最后一行
                printSubMsg(type, tag, msg.substring(index, len));
            }
        } else {    //没超过, 直接打印
            printSubMsg(type, tag, msg);
        }
    }

    private static void printSubMsg(final int type, final String tag, final String msg) {
        if (!sLogBorderOpen) {    //不加边框, 直接打印
            Log.println(type, tag, msg);
        } else {
            StringBuilder sb = new StringBuilder();
            String[] lines = msg.split(LINE_SEP);
            for (String line : lines) {
                Log.println(type, tag, LEFT_BORDER + line); //加边框
            }
        }
    }


    // 写入文件
    private static void print2File(final int type, final String tag, final String msg) {
        Date now = new Date(System.currentTimeMillis());
        String format = new SimpleDateFormat("MM-dd HH:mm:ss.SSS ", Locale.getDefault()).format(now);
        String date = format.substring(0, 5);
        String time = format.substring(6);
        final String fullPath = (sDir == null ? sDefaultDir : sDir) + sFilePrefix + "-" + date + ".txt";
        if (!createOrExistsFile(fullPath)) {    //创建文件失败
            Log.e(tag, "log to file: " + fullPath + " failed!");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(time)
                .append(T[type - V])
                .append("/")
                .append(tag)
                .append(msg)
                .append(LINE_SEP);
        final String content = sb.toString();
        if (sExecutor == null) {
            sExecutor = Executors.newSingleThreadExecutor();
        }
        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new FileWriter(fullPath, true));
                    bw.write(content);
                    Log.d(tag, "log to file: " + fullPath + " success!");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(tag, "log to file: " + fullPath + " failed!");
                } finally {
                    try {
                        if (bw != null) {
                            bw.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static boolean createOrExistsFile(final String filePath) {
        File file = new File(filePath);
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////

    public static class LogConfig {
        private LogConfig(@NonNull Context context) {
            sGlobalTag = PackageUtils.getAppName(context) + ": ";  //默认用 AppName 作为全局tag
            if (sDefaultDir != null) return;
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    && context.getExternalCacheDir() != null)
                sDefaultDir = context.getExternalCacheDir() + FILE_SEP + "dlog" + FILE_SEP;
            else {
                sDefaultDir = context.getCacheDir() + FILE_SEP + "dlog" + FILE_SEP;
            }
        }

        // 设置log全局tag   //默认为 appName :
        public LogConfig setGlobalTag(final String globalTag) {
            if (!TextUtils.isEmpty(globalTag)) {
                sGlobalTag = globalTag + " : ";
            }
            return this;
        }

        // 设置log总开关
        public LogConfig setLogOpen(final boolean logOpen) {
            sLogOpen = logOpen;
            return this;
        }

        // 设置log控制台开关
        public LogConfig setConsoleOpen(final boolean consoleOpen) {
            sLog2ConsoleOpen = consoleOpen;
            return this;
        }

        // 设置log文件开关
        public LogConfig setLog2FileOpen(final boolean log2FileOpen) {
            sLog2FileOpen = log2FileOpen;
            return this;
        }

        // 设置log文件存储目录 - String
        public LogConfig setDir(final String dir) {
            if (TextUtils.isEmpty(dir)) {
                sDir = null;
            } else {
                sDir = dir.endsWith(FILE_SEP) ? dir : dir + FILE_SEP;
            }
            return this;
        }

        // 设置log文件存储目录 - File
        public LogConfig setDir(final File dir) {
            sDir = dir == null ? null : dir.getAbsolutePath() + FILE_SEP;
            return this;
        }

        // 设置log文件前缀    // 默认 dlog
        public LogConfig setFilePrefix(final String filePrefix) {
            if (!TextUtils.isEmpty(filePrefix)) {
                sFilePrefix = filePrefix;
            }
            return this;
        }

        // 设置log边框开关
        public LogConfig setBorderOpen(final boolean borderOpen) {
            sLogBorderOpen = borderOpen;
            return this;
        }

        // 设置log控制台过滤器
        public LogConfig setConsoleFilter(@TYPE final int consoleFilter) {
            sConsoleFilter = consoleFilter;
            return this;
        }

        // 设置log文件过滤器
        public LogConfig setFileFilter(@TYPE final int fileFilter) {
            sFileFilter = fileFilter;
            return this;
        }

        // 设置log栈深度
        public LogConfig setStackDeep(@IntRange(from = 1) final int stackDeep) {
            sStackDeep = stackDeep;
            return this;
        }


        @Override
        public String toString() {
            return "LogConfig: "
                    + LINE_SEP + "isLogOpen:        " + sLogOpen
                    + LINE_SEP + "isConsoleOpen:    " + sLog2ConsoleOpen
                    + LINE_SEP + "isFileOpen:       " + sLog2FileOpen
                    + LINE_SEP + "sGlobalTag:       " + (TextUtils.isEmpty(sGlobalTag) ? "null" : sGlobalTag)
                    + LINE_SEP + "isBorderOpen:     " + sLogBorderOpen
                    + LINE_SEP + "dir:              " + (sDir == null ? sDefaultDir : sDir)
                    + LINE_SEP + "filePrefix:       " + sFilePrefix
                    + LINE_SEP + "consoleFilter:    " + T[sConsoleFilter - V]
                    + LINE_SEP + "fileFilter:       " + T[sFileFilter - V]
                    + LINE_SEP + "stackDeep:        " + sStackDeep;
        }

    }


}

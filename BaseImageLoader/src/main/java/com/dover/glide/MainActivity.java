package com.dover.glide;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.dover.baseimageloader.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Activity context;
    private ImageView targetImageView;
    private ListView lv_listview;


    static String[] eatFoodyImages = {
            "http://i.imgur.com/rFLNqWI.jpg",
            "http://i.imgur.com/C9pBVt7.jpg",
            "http://i.imgur.com/rT5vXE1.jpg",
            "http://i.imgur.com/aIy5R2k.jpg",
            "http://i.imgur.com/MoJs9pT.jpg",
            "http://i.imgur.com/S963yEM.jpg",
            "http://i.imgur.com/rLR2cyc.jpg",
            "http://i.imgur.com/SEPdUIx.jpg",
            "http://i.imgur.com/aC9OjaM.jpg",
            "http://i.imgur.com/76Jfv9b.jpg",
            "http://i.imgur.com/fUX7EIB.jpg",
            "http://i.imgur.com/syELajx.jpg",
            "http://i.imgur.com/COzBnru.jpg",
            "http://i.imgur.com/Z3QjilA.jpg",
    };
    private GridView gv_gridview;
    private ImageView transimageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        targetImageView = (ImageView) findViewById(R.id.iv_imageview);
        transimageview = (ImageView) findViewById(R.id.iv_trans_imageview);
        lv_listview = (ListView) findViewById(R.id.lv_listview);
        gv_gridview = (GridView) findViewById(R.id.gv_gridview);

        //1. 从一个URL加载
//        demo1_url();
        //2. 从资源中加载
//        demo2_res();
        //3. 从文件中加载
//        demo3_file();
        //4. 从 Uri 中加载
//        demo4_uri();
        //5. listview中加载
//        demo5_listview();
        //6. GridView中加载
//        demo6_gridview();
        //7. 默认图片 占位符
//        demo7_placeHolder();
        //8. 错误图片 占位符
//        demo8_error();
        //9. 淡入淡出动画 crossFade
//        demo9_crossFade();
        //10. 直接显示 不需要淡入显示 dontAnimate
//        demo10_dontAnimate();
        //11. 重新改变图片大小
//        demo11_override();
        //12. 缩放图像
//        demo12();
        //13. 显示 gif
//        demo13_gif();
        //14. 强制 Glide变成一个 Gif --asGif()
//        demo14_asgif();
        //15. Gif 转为 Bitmap
//        demo15_asBitmap();
        //16. 显示本地视频 -- 必须是本地视频
//        demo16_localVideo();
        //17. 跳过缓存
//        demo17_skipCache();
        //18. 自定义磁盘缓存行为
//        demo18_diskCache();
        //19. 设置图片请求的优先级--一些重要图片 优先加载   .priority( Priority.HIGH )
        // https://mrfu.me/2016/02/27/Glide_Request_Priorities/
        //20. 缩略图 thumbnail
//        demo20_thumbnail();
        //21. target 拿到bitmap对象
//        demo21_target();
        //22. 自定义view使用glide  -- ViewTarget
//        demo22_ViewTarget();
        //23. 加载图片到 Notifications ---NotificationTarget
//        demo23_Notifications();
        //24. 加载图片到 App Widgets
//        demo24_Widgets();
        //25. 日志+异常
//        demo25_log_exception();
        //26. 图片转换 Transformations -如模糊,旋转等
///      https://github.com/wasabeef/glide-transformations
        demo26_Transformations();
        //27. 集成网络库 --目前只支持 OkHttp 和 Volley
//        https://mrfu.me/2016/02/28/Glide_Integrating_Networking_Stacks/
        //28. 清除缓存
//        demo28_clearCache();
        //29. 暂停 和 开始加载图片  --- .resumeRequests()和.pauseRequests()
//        demo29_request();

    }


    /**
     * 将图像转换为四个角有弧度的图像
     */
    public class GlideRoundTransform extends BitmapTransformation {
        private float radius = 0f;

        public GlideRoundTransform(Context context) {
            this(context, 100);
        }

        public GlideRoundTransform(Context context, int dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            Log.e("11aa", radius + "");
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }

    /**
     * 将图像做旋转操作
     */
    public class GlideRotateTransform extends BitmapTransformation {
        private float rotateAngle = 0f;

        public GlideRotateTransform(Context context) {
            this(context, 90);
        }

        public GlideRotateTransform(Context context, float rotateAngle) {
            super(context);
            this.rotateAngle = rotateAngle;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotateAngle);
            return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
        }

        @Override
        public String getId() {
            return getClass().getName() + rotateAngle;
        }
    }

    /**
     * 将图像做模糊处理
     */
    public class BlurTransformation extends BitmapTransformation {

        private RenderScript rs;

        public BlurTransformation(Context context) {
            super(context);

            rs = RenderScript.create(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Bitmap blurredBitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true);

            // Allocate memory for Renderscript to work with
            Allocation input = Allocation.createFromBitmap(
                    rs,
                    blurredBitmap,
                    Allocation.MipmapControl.MIPMAP_FULL,
                    Allocation.USAGE_SHARED
            );
            Allocation output = Allocation.createTyped(rs, input.getType());

            // Load up an instance of the specific script that we want to use.
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);

            // Set the blur radius
            script.setRadius(10);

            // Start the ScriptIntrinisicBlur
            script.forEach(output);

            // Copy the output to the blurred bitmap
            output.copyTo(blurredBitmap);

            toTransform.recycle();

            return blurredBitmap;
        }

        @Override
        public String getId() {
            return "blur";
        }
    }

    //29. 暂停 和 开始加载图片 -- .resumeRequests()和.pauseRequests()
    private void demo29_request() {
        //这两个方法是为了保证用户界面的滑动流畅而设计的。
        // 当在ListView中加载图片的时候，如果用户滑动ListView的时候继续加载图片，就很有可能造成滑动不流畅、卡顿的现象，
        // 这是由于Activity需要同时处理滑动事件以及Glide加载图片。
        // Glide为我们提供了这两个方法，让我们可以在ListView等滑动控件滑动的过程中控制Glide停止加载或继续加载，可以有效的保证界面操作的流畅。
        //我们需要在ListView的OnScrollListener中写这部分的代码，如下（Adapter中的代码就不贴出来了）：
        // ListView滑动时触发的事件
        lv_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        // 当ListView处于滑动状态时，停止加载图片，保证操作界面流畅
                        Glide.with(MainActivity.this).pauseRequests();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 当ListView处于静止状态时，继续加载图片
                        Glide.with(MainActivity.this).resumeRequests();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    //28. 清除缓存
    private void demo28_clearCache() {
        Glide.get(context).clearMemory();//清除内存缓存
        new Thread(new Runnable() {
            @Override
            public void run() { //耗时
                Glide.get(context).clearDiskCache();// 清除磁盘缓存
            }
        }).start();

    }

    //26. 图片转换 Transformations -如模糊,旋转等
    // 提示：当你用了转换后你就不能使用 .centerCrop() 或 .fitCenter() 了。
    private void demo26_Transformations() {
        Glide
                .with(context)
                .load(eatFoodyImages[0])
                .into(targetImageView);

        //单个转换的应用
       /* Glide
                .with(context)
                .load(eatFoodyImages[0])
                .transform(new BlurTransformation(context))
                //.bitmapTransform( new BlurTransformation( context ) ) // this would work too!
                .into(transimageview);*/
        //多个转换的应用
        Glide
                .with(context)
                .load(eatFoodyImages[0])
                .transform(new GlideRoundTransform(context), new GlideRotateTransform(context), new BlurTransformation(context))
                .into(transimageview);

    }


    //25. 日志+异常
    // Glide 不能直接去访问 GenericRequest 类去设置日志，
    // 需要创建一个监听并传 .listener() 方法到 Glide 的建造者中。
    private void demo25_log_exception() {
        Glide
                .with(context)
                .load(eatFoodyImages[0])
                .listener(requestListener)
                .error(R.mipmap.running)//.error() 并不是必须的。即使在监听的 onException 中返回 false
                .into(targetImageView);
    }

    private RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            // todo log exception
            // 此处打印错误日志
            Log.e("onException", e.toString() + "  model:" + model + " isFirstResource: " + isFirstResource);

            // important to return false so the error placeholder can be placed
            // 重要: 如果 Glide 要在后续处理的话，(如显示一个错误的占位符等情况)，
            // 在 onException 方法中,需要返回 false
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            Log.e("onResourceReady", "model:" + model + " isFromMemoryCache:" + isFromMemoryCache + " isFirstResource: " + isFirstResource);
            return false;
        }
    };

    //24. 加载图片到 App Widgets
    private void demo24_Widgets() {

    }

    public class FSAppWidgetProvider extends AppWidgetProvider {

        private AppWidgetTarget appWidgetTarget;

        @Override
        public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                             int[] appWidgetIds) {

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.remoteview_notification);

            appWidgetTarget = new AppWidgetTarget(context, rv, R.id.remoteview_notification_icon, appWidgetIds);

            Glide
                    .with(context.getApplicationContext()) // safer!
                    .load(MainActivity.eatFoodyImages[3])
                    .asBitmap()
                    .into(appWidgetTarget);

            pushWidgetUpdate(context, rv);
        }

        public void pushWidgetUpdate(Context context, RemoteViews rv) {
            ComponentName myWidget = new ComponentName(context, FSAppWidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            manager.updateAppWidget(myWidget, rv);
        }
    }

    //23. 加载图片到 Notifications  --NotificationTarget
    private void demo23_Notifications() {
        int NOTIFICATION_ID = 1;
        final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.remoteview_notification);

        rv.setImageViewResource(R.id.remoteview_notification_icon, R.mipmap.running);

        rv.setTextViewText(R.id.remoteview_notification_headline, "Headline");
        rv.setTextViewText(R.id.remoteview_notification_short_message, "Short Message");

        // build notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Content Title")
                        .setContentText("Content Text")
                        .setContent(rv)
                        .setPriority(NotificationCompat.PRIORITY_MIN);

        final Notification notification = mBuilder.build();

        // set big content view for newer androids
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification.bigContentView = rv;
        }

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, notification);


        NotificationTarget notificationTarget = new NotificationTarget(
                context,
                rv,
                R.id.remoteview_notification_icon,
                notification,
                NOTIFICATION_ID);

        Glide
                .with(context.getApplicationContext()) // safer!
                .load(eatFoodyImages[3])
                .asBitmap()
                .into(notificationTarget);
    }

    //22. 自定义view使用glide  -- ViewTarget
    private void demo22_ViewTarget() {
//        FutureStudioView customView = (FutureStudioView) findViewById( R.id.custom_view );
        FutureStudioView customView = new FutureStudioView(context, null, 0);

        ViewTarget viewTarget = new ViewTarget<FutureStudioView, GlideDrawable>(customView) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setImage(resource.getCurrent());
            }
        };

        Glide
                .with(context.getApplicationContext()) // safer!
                .load(eatFoodyImages[2])
                .into(viewTarget);
    }

    public class FutureStudioView extends FrameLayout {
        ImageView iv;
//        TextView tv;

        public void initialize(Context context) {
//            inflate( context, R.layout.custom_view_futurestudio, this );
//
//            iv = (ImageView) findViewById( R.id.custom_view_image );
//            tv = (TextView) findViewById( R.id.custom_view_text );
        }

        public FutureStudioView(Context context, AttributeSet attrs) {
            super(context, attrs);
            initialize(context);
        }

        public FutureStudioView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initialize(context);
        }

        public void setImage(Drawable drawable) {
//            iv = (ImageView) findViewById( R.id.custom_view_image );

            iv.setImageDrawable(drawable);
        }
    }

    //21. target 拿到bitmap对象
    private void demo21_target() {
        //图片的请求绑定与context的生命周期
        loadImageSimpleTarget();
        //图片的请求将是全局的
        loadImageSimpleTargetApplicationContext();
    }


    // 如果你的 target 是独立于应用的 activity 生命周期。(当请求的 activity 已经停止的时候, Glide 将会自动停止请求)
    // 这里的解决方案是用 application 的 context
    private void loadImageSimpleTargetApplicationContext() {
        Glide
                .with(context.getApplicationContext())  // safer!
                .load(eatFoodyImages[1])
                .asBitmap()
                .into(target2);
    }

    // target 也可指定图片的大小
    private SimpleTarget target2 = new SimpleTarget<Bitmap>(250, 250) {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            targetImageView.setImageBitmap(bitmap);
        }
    };

    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            // do something with the bitmap   拿到加载的 bitmap 对象进行后续操作
            // 例如: 设置到 ImageView中
            targetImageView.setImageBitmap(bitmap);
        }
    };

    private void loadImageSimpleTarget() {
        Glide
                .with(context) // could be an issue!
                .load(eatFoodyImages[0])
                .asBitmap()
                .into(target);
    }

    //20. 缩略图 thumbnail
    private void demo20_thumbnail() {
        Glide
                .with(context)
                .load("http://img2.3lian.com/2014/f6/173/d/51.jpg")
                .thumbnail(0.1f)
                .into(targetImageView);
    }

    //18. 自定义磁盘缓存行为
    private void demo18_diskCache() {
        // .diskCacheStrategy() 方法来说不同的枚举参数的意义：

        // DiskCacheStrategy.NONE 什么都不缓存，就像刚讨论的那样
        // DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像。只缓存原图
        // DiskCacheStrategy.RESULT 仅仅缓存最终的图像, 只缓存最终(转换过的)的图片
        // DiskCacheStrategy.ALL 缓存所有版本的图像（默认行为）

        //对于一个图片多个不同地方的使用, 最好只缓存原图即可
        Glide
                .with(context)
                .load("http://img2.3lian.com/2014/f6/173/d/51.jpg")
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(targetImageView);
    }

    //17. 跳过缓存  --可分别调用(另一种缓存方式依然有效), 也可一起调用(内存+磁盘缓存 均无)
    private void demo17_skipCache() {
        Glide
                .with(context)
                .load("http://img2.3lian.com/2014/f6/173/d/51.jpg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)  //跳过磁盘缓存
                .skipMemoryCache(true) //跳过内存缓存
                .into(targetImageView);
    }

    //16. 显示本地视频 -- 必须只能是存储在手机上的本地视频
    private void demo16_localVideo() {
        String filePath = "/storage/emulated/0/Pictures/example_video.mp4";
        Glide
                .with(context)
                .load(Uri.fromFile(new File(filePath)))
                .into(targetImageView);
    }

    //15. Gif 转为 Bitmap --把 gif 图的第一帧转换为图片显示
    private void demo15_asBitmap() {
        String gifUrl = "http://i.kinja-img.com/gawker-media/image/upload/s--B7tUiM5l--/gf2r69yorbdesguga10i.gif";
        Glide
                .with(context)
                .load(gifUrl)
                .asBitmap()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.running)
                .into(targetImageView);
    }

    //14. 强制 Glide变成一个 Gif --asGif()
    private void demo14_asgif() {
        String gifUrl = "http://i.kinja-img.com/gawker-media/image/upload/s--B7tUiM5l--/gf2r69yorbdesguga10i.gif";
        Glide
                .with(context)
                .load(gifUrl)
                .asGif()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.running)
                .into(targetImageView);
    }

    //13. 显示 gif
    private void demo13_gif() {
//        String gifUrl = "http://i.kinja-img.com/gawker-media/image/upload/s--B7tUiM5l--/gf2r69yorbdesguga10i.gif";
        String gifUrl = "http://img1.3lian.com/2015/w4/17/d/64.gif";
        Glide
                .with(context)
                .load(gifUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.running)
                .into(targetImageView);
    }

    //12. 缩放图像  centerCrop 和 fitCenter
    private void demo12() {
        String internetUrl = "http://img2.3lian.com/2014/f6/173/d/51.jpg";

        //1. centerCrop()
        // 裁剪技术，即缩放图像让它填充到 ImageView 界限内并且裁剪额外的部分。
        // ImageView 可能会完全填充，但图像可能不会完整显示。
        //2. fitCenter()
        // 裁剪技术，即缩放图像让图像都测量出来等于或小于 ImageView 的边界范围。
        // 该图像将会完全显示，但可能不会填满整个 ImageView。
        Glide
                .with(context)
                .load(internetUrl)
                .override(320, 280)
                .placeholder(R.mipmap.ic_launcher)
//                .centerCrop()
                .fitCenter()
                .into(targetImageView);
    }

    //11. 重新改变图片大小   override(int width, int height)
    private void demo11_override() {
        String internetUrl = "http://img2.3lian.com/2014/f6/173/d/51.jpg";

        Glide
                .with(context)
                .load(internetUrl)
                .override(320, 280)
                .placeholder(R.mipmap.ic_launcher)
                .crossFade(2000)
                .into(targetImageView);
    }

    //10. 直接显示 不需要淡入显示 dontAnimate
    private void demo10_dontAnimate() {
        String internetUrl = "http://img2.3lian.com/2014/f6/173/d2/51.jpg";

        Glide
                .with(context)
                .load(internetUrl)
                .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                .error(R.mipmap.running) // will be displayed if the image cannot be loaded
                .dontAnimate()
                .into(targetImageView);
    }

    //9. 淡入淡出动画 crossFade
    // crossFade(int duration)默认动画时间300毫秒, 也可指定时间
    // crossFade(int animationId, int duration)  也可重新指定淡入淡出动画
    // crossFade(Animation animation, int duration)  也可重新指定动画
    private void demo9_crossFade() {
        String internetUrl = "http://img2.3lian.com/2014/f6/173/d2/51.jpg";

        Glide
                .with(context)
                .load(internetUrl)
                .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                .error(R.mipmap.running) // will be displayed if the image cannot be loaded
                .crossFade()
                .into(targetImageView);
    }

    //8. 错误图片 占位符
    //error()接受的参数只能是已经初始化的 drawable 对象或者指明它的资源(R.drawable.xxx)
    private void demo8_error() {
        String internetUrl = "http://img2.3lian.com/2014/f6/173/d2/51.jpg";

        Glide
                .with(context)
                .load(internetUrl)
                .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                .error(R.mipmap.running) // will be displayed if the image cannot be loaded
                .into(targetImageView);
    }

    //7. 默认图片 占位符
    private void demo7_placeHolder() {
        String internetUrl = "http://img2.3lian.com/2014/f6/173/d/51.jpg";

        Glide
                .with(context)
                .load(internetUrl)
                .placeholder(R.mipmap.ic_launcher)
                .into(targetImageView);
    }

    //6. GridView中加载
    private void demo6_gridview() {
        gv_gridview.setVisibility(View.VISIBLE);
        gv_gridview.setAdapter(new ImageListAdapter(MainActivity.this, eatFoodyImages));
    }

    //5. listview中加载
    private void demo5_listview() {
        lv_listview.setVisibility(View.VISIBLE);
        lv_listview.setAdapter(new ImageListAdapter(MainActivity.this, eatFoodyImages));

    }

    public class ImageListAdapter extends ArrayAdapter {
        private Context context;
        private LayoutInflater inflater;

        private String[] imageUrls;

        public ImageListAdapter(Context context, String[] imageUrls) {
            super(context, R.layout.listview_item_image, imageUrls);

            this.context = context;
            this.imageUrls = imageUrls;

            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.listview_item_image, parent, false);
            }

            Glide
                    .with(context)
                    .load(imageUrls[position])
                    .into((ImageView) convertView);

            return convertView;
        }
    }

    //4. 从 Uri 中加载---Uri 不必从资源中去生成，它可以是任何 Uri
    private void demo4_uri() {
        //这可能是任何 Uri。为了演示的目的我们只是用一个 launcher icon 去创建了一个 Uri
        Uri uri = Utils.resourceIdToUri(context, R.mipmap.ic_launcher);

        Glide
                .with(context)
                .load(uri)
                .into(targetImageView);
    }


    //3. 从文件中加载
    private void demo3_file() {
        //这个文件可能不存在于你的设备中。然而你可以用任何文件路径，去指定一个图片路径。
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "running.jpg");

        Glide
                .with(context)
                .load(file)
                .into(targetImageView);
    }

    //2. 从资源中加载
    private void demo2_res() {
        int resourceId = R.mipmap.ic_launcher;

        Glide
                .with(context)
                .load(resourceId)
                .into(targetImageView);
    }

    //1. 从一个URL加载
    private void demo1_url() {
        String internetUrl = "http://img2.3lian.com/2014/f6/173/d/51.jpg";
        // 加载空白, 原因: 网络权限
        Glide
                .with(context)
                .load(internetUrl)
                .into(targetImageView);
    }

}

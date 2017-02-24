package com.herokutest;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by Kain on 2016/9/22.
 */
public class AppApplication extends Application {

    private static AppApplication instance;

    public static AppApplication getInstance() {
        return instance;
    }

    public static ImageLoader imageLoader;

    public static DisplayImageOptions options;
    public static DisplayImageOptions bigOptions;

    public static String PATH_CACHE;
    public static String PATH_CACHE_IMAGES;// 图片缓存路径

    public static LinkedList<Activity> activityLinkedList;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Context mContext = this;
        initCache(mContext);
        initImageLoader();
    }

    private void initImageLoader() {
        //缓存路径初始化
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), PATH_CACHE_IMAGES);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(1080, 1080) // max width, max height，即保存的每个缓存文件的最大长宽
//                .memoryCacheExtraOptions(240, 320) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .discCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
//                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.img_loding) //设置图片在下载期间显示的图片
//                .showImageForEmptyUri(R.drawable.img_loding)//设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.img_loding)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .considerExifParams(true)
                .delayBeforeLoading(0)//int delayInMillis为你设置的下载前的延迟时间
//设置图片加入缓存前，对bitmap进行设置
//.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//                .displayer(new RoundedBitmapDisplayer(5))//是否设置为圆角，弧度为多少
//                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
    }

    // 初始化缓存路径
    public static void initCache(Context context) {
        Resources res = context.getResources();
        try {
            PATH_CACHE = context.getExternalCacheDir().getPath();
        } catch (NullPointerException e) {
            e.printStackTrace();
            PATH_CACHE = context.getCacheDir().getPath();
        }
        PATH_CACHE_IMAGES = PATH_CACHE
                + "/images";
    }

}

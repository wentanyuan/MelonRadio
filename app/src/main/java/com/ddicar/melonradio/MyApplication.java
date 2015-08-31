package com.ddicar.melonradio;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		GotyeLoginInfoManager.init(this);
		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs()
				// Remove
				// for
				// release
				// app
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize(1 * 1024 * 1024)
				.discCacheSize(40 * 1024 * 1024)
				.discCacheFileCount(400)
				.threadPoolSize(2)
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
				.build();
		ImageLoader.getInstance().init(config);
	}
}

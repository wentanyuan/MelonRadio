package com.ddicar.melonradio.util;

import java.util.HashMap;

import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.ddicar.melonradio.MainActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by Forrest on 5/30/14.
 */
public class PhotoUtils {

	private static final String TAG = "PhotoUtils";

	private static DisplayImageOptions coverDisplayImageOptions;

	private static Options options;

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	private static ImageLoader imageLoader = ImageLoader.getInstance();

	private static DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
			// .showImageOnLoading(R.drawable.loading_middle_341)
			// .showImageForEmptyUri(R.drawable.loading_middle_341)
			// .showImageOnFail(R.drawable.loading_middle_341)
			.displayer(new RoundedBitmapDisplayer(500))
			.cacheOnDisk(true)
			.considerExifParams(true).build();

	private static DisplayImageOptions sliderDisplayImageOptions;

	private static HashMap<Integer, DisplayImageOptions> mImageOptionsMap = new HashMap<Integer, DisplayImageOptions>();

	public static void showPhoto(UriType uriType, String photoPath, ImageView iv) {
		try {
			
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
			
			System.out.println("w,h = " + params.width + "," + params.height);
			
			imageLoader.init(ImageLoaderConfiguration
					.createDefault(MainActivity.instance));
			imageLoader.displayImage(uriType.toString() + photoPath, iv,
					defaultDisplayImageOptions);
			System.out.println("shown");
		} catch (OutOfMemoryError oom) {
			// TODO: handle exception
			Log.e(TAG, oom.getMessage());
			System.out.println("oom exception");
		}catch(Exception e){
			Log.e(TAG, e.getMessage());
			System.out.println(" exception");
		}
	}

	// public static void showPhoto(UriType uriType, String photoPath,
	// ImageView iv, int cornerRadiusPixels,
	// RoundedBitmapDisplayer.Style style) {
	// try {
	// imageLoader.displayImage(uriType.toString() + photoPath, iv,
	// getDisplayImageOptions(cornerRadiusPixels, style));
	// } catch (OutOfMemoryError oom) {
	// System.out.println("oom exception");
	// }
	// }

	// public static void showPhoto(UriType uriType, String photoPath,
	// ImageView iv, int cornerRadiusPixels,
	// RoundedBitmapDisplayer.Style style,
	// ImageLoadingListener imageLoadingListener) {
	// try {
	// imageLoader.displayImage(uriType.toString() + photoPath, iv,
	// getDisplayImageOptions(cornerRadiusPixels, style),
	// imageLoadingListener);
	// } catch (OutOfMemoryError oom) {
	// // TODO: handle exception
	// System.out.println("oom exception");
	// }
	// }

	// public static void showPhoto(UriType uriType, String photoPath,
	// ImageView iv, int cornerRadiusPixels,
	// RoundedBitmapDisplayer.CornerDiy[] cornerDiys) {
	// try {
	// imageLoader
	// .displayImage(
	// uriType.toString() + photoPath,
	// iv,
	// getDisplayImageOptions(cornerRadiusPixels,
	// RoundedBitmapDisplayer.Style.NORMAL_DIY,
	// cornerDiys));
	// } catch (OutOfMemoryError oom) {
	// // TODO: handle exception
	// System.out.println("oom exception");
	// }
	// }

	public static void loadPhoto(UriType uriType, String photoPath,
			ImageLoadingListener imageLoadingListener) {
		try {
			imageLoader.loadImage(uriType.toString() + photoPath,
					defaultDisplayImageOptions, imageLoadingListener);
		} catch (OutOfMemoryError oom) {
			System.out.println("oom exception");
		}
	}

	public static void loadPhoto(UriType uriType, String photoPath,
			ImageLoadingListener imageLoadingListener, ImageSize imageSize) {
		imageLoader.loadImage(uriType.toString() + photoPath, imageSize,
				defaultDisplayImageOptions, imageLoadingListener);
	}

	public static void showSlider(UriType uriType, String photoPath,
			ImageView iv, int cornerRadiusPixels) {
		try {
			imageLoader.displayImage(uriType.toString() + photoPath, iv);
		} catch (OutOfMemoryError oom) {
			// TODO: handle exception
		}
	}

	// public static void showCard(UriType uriType, String photoPath,
	// ImageView iv, int cornerRadiusPixels) {
	// try {
	// imageLoader.displayImage(uriType.toString() + photoPath, iv,
	// getSliderDisplayImageOptions(cornerRadiusPixels));
	// } catch (OutOfMemoryError oom) {
	//
	// }
	// }

	// public static void showCover(UriType uriType, String photoPath, ImageView
	// iv) {
	// try {
	// imageLoader.displayImage(uriType.toString() + photoPath, iv,
	// getCoverDisplayImageOptions());
	// } catch (OutOfMemoryError oom) {
	// System.out.println("oom exception");
	// }
	// }

	// private static DisplayImageOptions getCoverDisplayImageOptions() {
	// if (coverDisplayImageOptions == null) {
	// DisplayImageOptions.Builder displayImageOptionsBuilder = new
	// DisplayImageOptions.Builder();
	// displayImageOptionsBuilder
	// .showImageOnLoading(R.drawable.loading_cover);
	// displayImageOptionsBuilder
	// .showImageForEmptyUri(R.drawable.loading_cover);
	// displayImageOptionsBuilder
	// .showImageOnFail(R.drawable.loading_cover);
	// displayImageOptionsBuilder.cacheOnDisk(true);
	// displayImageOptionsBuilder
	// .imageScaleType(ImageScaleType.IN_SAMPLE_INT);
	// displayImageOptionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
	// displayImageOptionsBuilder.considerExifParams(true);
	// coverDisplayImageOptions = displayImageOptionsBuilder.build();
	// }
	// return coverDisplayImageOptions;
	// }

	// private static DisplayImageOptions getDisplayImageOptions(
	// int cornerRadiusPixels, RoundedBitmapDisplayer.Style style) {
	// return getDisplayImageOptions(cornerRadiusPixels, style, null);
	// }

	// private static DisplayImageOptions getSliderDisplayImageOptions(
	// int cornerRadiusPixels) {
	// if (sliderDisplayImageOptions == null) {
	// DisplayImageOptions.Builder displayImageOptionsBuilder = new
	// DisplayImageOptions.Builder();
	// displayImageOptionsBuilder
	// .showImageOnLoading(R.drawable.loading_slider);
	// displayImageOptionsBuilder
	// .showImageForEmptyUri(R.drawable.loading_slider);
	// displayImageOptionsBuilder
	// .showImageOnFail(R.drawable.loading_slider);
	// displayImageOptionsBuilder
	// .imageScaleType(ImageScaleType.IN_SAMPLE_INT);
	// ;
	// displayImageOptionsBuilder.cacheOnDisk(true);
	// displayImageOptionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
	// displayImageOptionsBuilder.considerExifParams(true);
	// displayImageOptionsBuilder.displayer(new RoundedBitmapDisplayer(
	// cornerRadiusPixels));
	// sliderDisplayImageOptions = displayImageOptionsBuilder.build();
	// }
	// return sliderDisplayImageOptions;
	// }

	// private static DisplayImageOptions getDisplayImageOptions(
	// int cornerRadiusPixels, RoundedBitmapDisplayer.Style style,
	// RoundedBitmapDisplayer.CornerDiy[] cornerDiys) {
	// // DisplayImageOptions displayImageOptions =
	// // mImageOptionsMap.get(cornerRadiusPixels);
	// DisplayImageOptions displayImageOptions;
	// // if (displayImageOptions == null) {
	// DisplayImageOptions.Builder displayImageOptionsBuilder = new
	// DisplayImageOptions.Builder();
	// displayImageOptionsBuilder
	// .showImageOnLoading(R.drawable.loading_small_150);
	// displayImageOptionsBuilder
	// .showImageForEmptyUri(R.drawable.loading_small_150);
	// displayImageOptionsBuilder
	// .showImageOnFail(R.drawable.loading_small_150);
	// displayImageOptionsBuilder.cacheInMemory(false);
	// displayImageOptionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
	// displayImageOptionsBuilder.cacheOnDisk(true);
	// displayImageOptionsBuilder.considerExifParams(true);
	// if (style.toString().equals("NORMAL")) {
	// displayImageOptionsBuilder.displayer(new RoundedBitmapDisplayer(
	// cornerRadiusPixels));
	// } else if (style.toString().equals("VIGNETTE")) {
	// displayImageOptionsBuilder
	// .displayer(new RoundedVignetteBitmapDisplayer(
	// cornerRadiusPixels, 0));
	// } else if (style.toString().equals("NORMAL_DIY")) {
	// displayImageOptionsBuilder.displayer(new RoundedBitmapDisplayer(
	// cornerRadiusPixels, cornerDiys));
	// }
	// displayImageOptions = displayImageOptionsBuilder.build();
	// // mImageOptionsMap.put(cornerRadiusPixels, displayImageOptions);
	// // }
	// return displayImageOptions;
	// }

	public static enum UriType {
		HTTP(""), FILE("file://"), DRAWABLE("drawable://");

		private final String value;

		UriType(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}

}

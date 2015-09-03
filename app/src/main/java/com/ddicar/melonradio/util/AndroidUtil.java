package com.ddicar.melonradio.util;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.ddicar.melonradio.MainActivity;

public class AndroidUtil {

	private static final String TAG = "AndroidUtil";

	public static int pixel(int points) {

		if (MainActivity.instance != null) {
			int[] windowSize = MainActivity.instance.getWindowSize();
			return points * windowSize[2] / 160;
		}
		return 0;
	}

	public static float fontSize(int size) {
		if (MainActivity.instance != null) {
			int fontSize = size;
			
			return fontSize;
		}
		return 40;
	}

	public static float dependentFontSize(int size) {
		if (MainActivity.instance != null) {
			int[] windowSize = MainActivity.instance.getWindowSize();

			int fontSize = size;// *  160 / windowSize[2];
			
			return fontSize;
		}
		return 40;
	}
	public static int pixelInPercentWidth(float percent) {
		if (MainActivity.instance != null) {
			int[] windowSize = MainActivity.instance.getWindowSize();

			return (int) (windowSize[0] * percent);//*  windowSize[2]/ 160;

		}
		return 0;
	}

	public static int pixelInPercentHeight(float percent) {
		if (MainActivity.instance != null) {
			int[] windowSize = MainActivity.instance.getWindowSize();
			return (int) (windowSize[1] * percent);// * windowSize[2]/ 160;
		}
		return 0;
	}

	public static int inversePixel(int points) {
		if (MainActivity.instance != null) {
			int[] windowSize = MainActivity.instance.getWindowSize();
			return points * 160 / windowSize[2];
		}
		return 0;
	}

	public static boolean isAppInstalled(String uri) {
		PackageManager pm = MainActivity.instance.getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}

	public static int parseColor(int colorResId) {
		return MainActivity.instance.getResources().getColor(colorResId);
	}

    public static Drawable parseDrawable(int drawableResId) {
        return MainActivity.instance.getResources().getDrawable(drawableResId);
    }
}

package com.ddicar.melonradio.util;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;

import com.ddicar.melonradio.MainActivity;

import java.util.List;

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

    public static void turnLightOn(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }

        List<String> flashModes = parameters.getSupportedFlashModes();
        // Check if camera flash exists
        if (flashModes == null) {
            // Use the screen as a flashlight (next best thing)
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            } else {
            }
        }
    }

    public static void turnLightOff(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        // Check if camera flash exists
        if (flashModes == null) {
            return;
        }
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            // Turn off the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            } else {
                Log.d(TAG, "FLASH_MODE_OFF not supported");
            }
        }
    }


	public static int fullScreenWidth(float adjustmentInDp) {
		if (MainActivity.instance != null) {
			int[] windowSize = MainActivity.instance.getWindowSize();
			return (int) (windowSize[0] - windowSize[2] * adjustmentInDp / 160);
		}

		return 0;
	}


	public static int fullScreenHeight(float adjustmentInDp) {
		if (MainActivity.instance != null) {
			int[] windowSize = MainActivity.instance.getWindowSize();
			return (int) (windowSize[1] - windowSize[2] * adjustmentInDp / 160 - windowSize[4]);
		}

		return 0;
	}

    public static String getSaveFolder() {

//        HashMap<String, String> folders = getStorageDirectories();
//        if (folders.containsKey(SECONDARY)) {
//
//            String secondary = folders.get(SECONDARY);
//            File file = new File(secondary);
//
//            if (file.exists() && file.canWrite()) {
//                return secondary;
//            }
//        }

        return Environment.getExternalStorageDirectory()
                .getAbsolutePath();
    }
}

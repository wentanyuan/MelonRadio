package com.ddicar.melonradio.util;

import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

public class PictureUtils {

    private static final String TAG = "PictureUtils";

    public static boolean compressBitmapToFile(Bitmap bitmap, String fileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			return bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static void cleanImageView(ImageView imageView) {
		if (!(imageView.getDrawable() instanceof BitmapDrawable))
			return;

		// clean up the view's image for the sake of memory
		BitmapDrawable b = (BitmapDrawable) imageView.getDrawable();
		b.getBitmap().recycle();
		imageView.setImageDrawable(null);
	}

	public static Bitmap rotateBitmap(Bitmap bm, final int orientationDegree) {

		Matrix m = new Matrix();
		m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

		try {
			Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
			return bm1;
		} catch (OutOfMemoryError ex) {
		}

		return null;

	}

	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Bitmap cover = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Bitmap temp = Bitmap.createBitmap(bitmap);

		Canvas target = new Canvas(output);

		Canvas canvas = new Canvas(cover);

		final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		final int color = 0xFFFFFFFF;
		paint.setColor(color);

		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		canvas.drawRect(rectF, paint);

		Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
		final int color2 = 0xFF000000;
		paint2.setColor(color2);
		paint2.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint2);

		final Paint paint4 = new Paint(Paint.ANTI_ALIAS_FLAG);
		final int color4 = 0xFFFFFFFF;
		paint4.setColor(color4);
		paint4.setXfermode(new PorterDuffXfermode(Mode.XOR));

		canvas.drawRect(rectF, paint4);

		Paint paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
		final int color3 = 0xFFFFFFFF;
		paint3.setColor(color3);
		paint3.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
		target.drawBitmap(temp, null, rect, null);
		target.drawBitmap(cover, null, rect, paint3);

		return output;
	}
}
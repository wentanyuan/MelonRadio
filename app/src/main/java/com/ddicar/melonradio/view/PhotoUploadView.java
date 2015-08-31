package com.ddicar.melonradio.view;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.common.HybridBinarizer;
import com.zxing.camera.PlanarYUVLuminanceSource;

public class PhotoUploadView extends AbstractView implements
		SurfaceHolder.Callback {

	private ImageView back;
	private SurfaceHolder mSurfaceHolder;
	private int videoWidth = 640;
	private int videoHeight = 480;
	private Camera mCamera;
	private SurfaceView mSurfaceView;

	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {
		adjustUI();
		back = (ImageView) view.findViewById(R.id.button_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.PERSONAL);
			}
		});
		ImageView takePhoto = (ImageView) view
				.findViewById(R.id.button_take_photo);
		takePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.out.println("Upload photo took.");
			}
		});

		mSurfaceView = (SurfaceView) view.findViewById(R.id.preview_view);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceView.setVisibility(View.VISIBLE);

	}

	private void adjustUI() {
		TextView aboutTitle = (TextView) view
				.findViewById(R.id.photo_upload_title);
		adjustTitleBarUnitSize(aboutTitle);
		adjustFontSize(aboutTitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

	}

	@Override
	public void onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {

	}

	@Override
	public void onBackPressed() {
		MainActivity.instance.switchScreen(ViewFlyweight.PERSONAL);

	}

	@Override
	public void onTouch(MotionEvent event) {

	}

	@Override
	public void onResume() {

	}

	@Override
	public void onPause() {

	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	}


	@Override
	public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2,
			int arg3) {
		mSurfaceHolder = holder;

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mSurfaceHolder = holder;
		initCamera();

	}

	@SuppressLint("NewApi")
	private void initCamera() {
		System.out.println("---initCamera---");
		try {
			mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
			mCamera.setPreviewDisplay(mSurfaceHolder);
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(videoWidth, videoHeight);
			parameters.setPictureSize(videoWidth, videoHeight);
			parameters.setPreviewFormat(ImageFormat.YV12);

			mCamera.setParameters(parameters);
			mCamera.setPreviewCallback(mPreviewCallback);
			mCamera.setDisplayOrientation(90);

			mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

			mCamera.startPreview();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		releaseCamera();
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private PreviewCallback mPreviewCallback = new PreviewCallback() {
		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			int previewWidth = camera.getParameters().getPreviewSize().width;
			int previewHeight = camera.getParameters().getPreviewSize().height;

			PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
					data, previewWidth, previewHeight, 0, 0, previewWidth,
					previewHeight);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		}
	};
}

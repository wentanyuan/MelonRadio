package com.ddicar.melonradio.view;

import android.hardware.SensorEvent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.util.AndroidUtil;

public abstract class AbstractView {

	public View view;

	public void init(int resId) {
		view = MainActivity.instance.getLayoutInflater().inflate(resId, null);
	}

	public abstract void onSwitchOff();

	public abstract void auto();

	public abstract void onFling(MotionEvent start, MotionEvent end,
			float velocityX, float velocityY);

	public abstract void onBackPressed();

	public abstract void onTouch(MotionEvent event);

	public abstract void onResume();

	public abstract void onPause();

	public void onSensorChanged(SensorEvent event) {

    }

	protected void adjustFontSize(TextView view) {
		adjustFontSize(view, 20);
	}


	protected void adjustFontSize(TextView view, int size) {
		view.setTextSize(AndroidUtil.fontSize(size));
	}

	protected void adjustDependentFontSize(TextView view, int size) {
		view.setTextSize(AndroidUtil.dependentFontSize(size));
	}

	protected void adjustMediumFontSize(TextView view) {
		adjustFontSize(view, 18);
	}

	protected void adjustSmallFontSize(TextView view) {
		adjustFontSize(view, 16);
	}

	protected void adjustUnitSize(View view) {
		LayoutParams params = view.getLayoutParams();
		params.height = unitHeight();
		view.setLayoutParams(params);
	}
	
	protected void adjustUnitHeightSize(View view) {
		LayoutParams params = view.getLayoutParams();
		params.height = adjustUnitHeight();
		view.setLayoutParams(params);
	}
	
	protected void adjustUnitHeightSize(View view, int width) {
		LayoutParams params = view.getLayoutParams();
		params.height = adjustUnitHeight();
		params.width = width;
		view.setLayoutParams(params);
	}

	protected void adjustTitleBarUnitSize(View view) {
		LayoutParams params = view.getLayoutParams();
		params.height = unitHeight();;
		view.setLayoutParams(params);
	}

	protected void adjustUnitSize(View view, int width) {
		LayoutParams params = view.getLayoutParams();
		params.height = unitHeight();
		params.width = width;
		view.setLayoutParams(params);
	}
	
	protected void adjustRegisterSize(LinearLayout linearLayout) {
		LayoutParams params = linearLayout.getLayoutParams();
		params.height = unitRegisterHeight();
		linearLayout.setLayoutParams(params);
	}
	
	private int unitRegisterHeight() {
		return AndroidUtil.pixel(55);
	}

	private int unitHeight() {
		return AndroidUtil.pixel(45);
	}
	
	private int adjustUnitHeight() {
		return AndroidUtil.pixel(32);
	}

	protected void adjustFullWidth(View view) {
		adjustFullWidth(view, 0);
	}

	protected void adjustFixedWidth(View view, int adjustment) {
		int width = adjustment;

		LayoutParams params = view.getLayoutParams();
		params.width = width;
		view.setLayoutParams(params);
	}

	protected void adjustWidth(View view, int adjustment) {
		int width = AndroidUtil.pixel(adjustment);

		LayoutParams params = view.getLayoutParams();
		params.width = width;
		view.setLayoutParams(params);
	}

	protected void adjustHeight(View view, int adjustment) {
		int height = AndroidUtil.pixel(adjustment);

		LayoutParams params = view.getLayoutParams();
		params.height = height;
		view.setLayoutParams(params);
	}

	protected void adjustFullWidth(View view, int adjustment) {
		int[] sizes = MainActivity.instance.getWindowSize();

		int width = sizes[0] - AndroidUtil.pixel(30) - adjustment;

		LayoutParams params = view.getLayoutParams();
		params.width = width;
		view.setLayoutParams(params);
	}

	protected void adjustMargin(LinearLayout view, int left, int top,
			int right, int bottom) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		params.setMargins(left,top, right, bottom);
		view.setLayoutParams(params);

	}
	
	protected void adjustAndroidMargin(LinearLayout view, int left, int top,
			int right, int bottom) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		params.setMargins(AndroidUtil.pixel(left), AndroidUtil.pixel(top),
				AndroidUtil.pixel(right), AndroidUtil.pixel(bottom));
		view.setLayoutParams(params);

	}

	protected void adjustRelativeMargin(RelativeLayout view, int left, int top,
			int right, int bottom) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		params.setMargins(AndroidUtil.pixel(left), AndroidUtil.pixel(top),
				AndroidUtil.pixel(right), AndroidUtil.pixel(bottom));
		view.setLayoutParams(params);

	}
}

package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;

public class AboutView extends AbstractView {

	private ImageView back;

	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {
		adjustUI();

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance
						.switchScreen(ViewFlyweight.SOFTWARE_SETTING);
			}
		});

	}

	private void adjustUI() {
		TextView aboutTitle = (TextView) view.findViewById(R.id.about_title);
		adjustTitleBarUnitSize(aboutTitle);
		adjustFontSize(aboutTitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

		TextView about = (TextView) view.findViewById(R.id.about_text);
		adjustFullWidth(about, 10);

//		int[] sizes = MainActivity.instance.getWindowSize();

//		int screenX = sizes[0];
//		int screenY = sizes[1];
//		int density = sizes[2];

//		if (density < 400) {
			adjustDependentFontSize(about, 18);
//		} else {
//			adjustDependentFontSize(about, 35);
//		}

		LinearLayout logoContainer = (LinearLayout) view
				.findViewById(R.id.logo_container);
		adjustAndroidMargin(logoContainer, 0, 40, 0, 40);

		ImageView logo = (ImageView) view.findViewById(R.id.logo);
		adjustWidth(logo, 100);
	}

	@Override
	public void onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {

	}

	@Override
	public void onBackPressed() {
		MainActivity.instance.switchScreen(ViewFlyweight.SOFTWARE_SETTING);

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

}

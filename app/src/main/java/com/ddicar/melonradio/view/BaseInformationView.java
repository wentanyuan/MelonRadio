package com.ddicar.melonradio.view;

import android.content.Intent;
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

public class BaseInformationView extends AbstractView {
	private ImageView back;

	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {
		adjustUI();

//		back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(MainActivity.instance,
//						MainFragmentView.class);
//
//				MainFragmentView.instance.startActivity(intent);
//			}
//		});

	}

	private void adjustUI() {
//		TextView aboutTitle = (TextView) view.findViewById(R.id.about_title);
//		adjustTitleBarUnitSize(aboutTitle);
//		adjustFontSize(aboutTitle);
//
//		back = (ImageView) view.findViewById(R.id.button_back);
//		adjustTitleBarUnitSize(back);
//
//		RelativeLayout titleBar = (RelativeLayout) view
//				.findViewById(R.id.title_bar);
//		adjustTitleBarUnitSize(titleBar);


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

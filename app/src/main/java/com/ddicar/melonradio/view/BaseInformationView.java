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
	private RelativeLayout back;

	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {
		adjustUI();

        back = (RelativeLayout) view.findViewById(R.id.cancel);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.MAIN_VIEW);
                ViewFlyweight.MAIN_VIEW.gotoMyView();
			}
		});

	}

	private void adjustUI() {

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

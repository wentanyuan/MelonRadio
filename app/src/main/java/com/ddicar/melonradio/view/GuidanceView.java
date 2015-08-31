package com.ddicar.melonradio.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;

public class GuidanceView extends AbstractView {

	private ViewFlipper viewFlipper;
	private ImageView indicator1;
	private ImageView indicator2;
	private ImageView indicator3;

	int index = 0;

	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {
		viewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper);
		indicator1 = (ImageView) view.findViewById(R.id.page_1_indicator);
		indicator2 = (ImageView) view.findViewById(R.id.page_2_indicator);
		indicator3 = (ImageView) view.findViewById(R.id.page_3_indicator);
	}

	private void adjustUI() {

	}

	@Override
	public void onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {

		if (velocityX < -100 && index < 2) {
			viewFlipper.setInAnimation(MainActivity.instance,
					R.anim.slide_right_in);
			viewFlipper.setOutAnimation(MainActivity.instance,
					R.anim.slide_left_out);
			viewFlipper.showNext();
			index++;
			indicator1.setImageResource(R.drawable.page);

			if (index == 1) {
				indicator2.setImageResource(R.drawable.page_selected);
			} else {
				indicator2.setImageResource(R.drawable.page);
			}

			if (index == 2) {
				indicator3.setImageResource(R.drawable.page_selected);
			} else {
				indicator3.setImageResource(R.drawable.page);
			}
		}

	}

	@Override
	public void onBackPressed() {

	}

	@Override
	public void onTouch(MotionEvent event) {
		if (index == 2) {
			MainActivity.instance.switchScreenInHandler(ViewFlyweight.LOGIN);

			SharedPreferences sp = MainActivity.instance.getSharedPreferences(
					"melon_radio", Context.MODE_PRIVATE);

			Editor editor = sp.edit();
			editor.putBoolean("guidance_opened", true);
			editor.commit();
		}
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

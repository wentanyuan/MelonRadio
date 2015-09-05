package com.ddicar.melonradio.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;

import com.ddicar.melonradio.MainActivity;

public class HomeView extends AbstractView {

	private static final String TAG = "Home";

	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {
		Timer timer = new Timer(true);

		task = new MyTimerTask();
		timer.schedule(task, 0, 1000);

	}

	TimerTask task;

	class MyTimerTask extends TimerTask {

		int count = 0;

		@Override
		public void run() {

			if (count == 1) {
				SharedPreferences sp = MainActivity.instance
						.getSharedPreferences("melon_radio",
								Context.MODE_PRIVATE);

				boolean opened = sp.getBoolean("guidance_opened", false);
				if (opened) {
					MainActivity.instance
							.switchScreenInHandler(ViewFlyweight.LOGIN);
				} else {
					MainActivity.instance
							.switchScreenInHandler(ViewFlyweight.GUIDANCE);
				}
			}
			count++;
		}

	};

	@Override
	public void onBackPressed() {

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
	public void onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {

	}



}

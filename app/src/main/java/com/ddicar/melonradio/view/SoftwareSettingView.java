package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.service.UserManager;

public class SoftwareSettingView extends AbstractView {

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
				MainActivity.instance.switchScreen(ViewFlyweight.SETTING);
			}
		});

		LinearLayout clearCache = (LinearLayout) view
				.findViewById(R.id.button_clear_cache);
		clearCache.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// TODO clear data folder, but exclude the avatar file.

				Message msg = new Message();
				msg.what = MainActivity.MESSAGE;
				msg.obj = "已经清空缓存。";
				MainActivity.instance.mHandler.sendMessage(msg);
			}
		});

		LinearLayout feedback = (LinearLayout) view
				.findViewById(R.id.button_feedback);
		feedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.FEEDBACK);
			}
		});

		LinearLayout help = (LinearLayout) view.findViewById(R.id.button_help);
		help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.HELP);
			}
		});

		LinearLayout about = (LinearLayout) view
				.findViewById(R.id.button_about);
		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.ABOUT);
			}
		});

		LinearLayout logout = (LinearLayout) view
				.findViewById(R.id.button_logout);
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				ViewFlyweight.PLAY.stopped = true;
				ViewFlyweight.PLAY.stop();

				UserManager manager = UserManager.getInstance();
				// manager.logout();

				Message msg = new Message();
				msg.what = MainActivity.MESSAGE;
				msg.obj = "已经退出登录。";
				MainActivity.instance.mHandler.sendMessage(msg);

				MainActivity.instance.switchScreen(ViewFlyweight.LOGIN);
			}
		});
	}

	private void adjustUI() {
		TextView settingTitle = (TextView) view
				.findViewById(R.id.software_setting_title);
		adjustTitleBarUnitSize(settingTitle);
		adjustFontSize(settingTitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

		ImageView clearCacheLeft = (ImageView) view
				.findViewById(R.id.clear_cache_left);
		adjustUnitSize(clearCacheLeft, 13);

		TextView clearCacheMiddle = (TextView) view
				.findViewById(R.id.clear_cache_middle);
		adjustFullWidth(clearCacheMiddle);
		adjustUnitSize(clearCacheMiddle);
		adjustFontSize(clearCacheMiddle);

		ImageView feedbackLeft = (ImageView) view
				.findViewById(R.id.feedback_left);
		adjustUnitSize(feedbackLeft, 13);

		TextView feedbackMiddle = (TextView) view
				.findViewById(R.id.feedback_middle);
		adjustFullWidth(feedbackMiddle);
		adjustUnitSize(feedbackMiddle);
		adjustFontSize(feedbackMiddle);

		ImageView helpLeft = (ImageView) view.findViewById(R.id.help_left);
		adjustUnitSize(helpLeft, 13);

		TextView helpMiddle = (TextView) view.findViewById(R.id.help_middle);
		adjustFullWidth(helpMiddle);
		adjustUnitSize(helpMiddle);
		adjustFontSize(helpMiddle);

		ImageView aboutLeft = (ImageView) view.findViewById(R.id.about_left);
		adjustUnitSize(aboutLeft, 13);

		TextView aboutMiddle = (TextView) view.findViewById(R.id.about_middle);
		adjustFullWidth(aboutMiddle);
		adjustUnitSize(aboutMiddle);
		adjustFontSize(aboutMiddle);

//		ImageView logoutLeft = (ImageView) view.findViewById(R.id.logout_left);
//		adjustUnitSize(logoutLeft, 13);

		TextView logoutMiddle = (TextView) view
				.findViewById(R.id.logout_middle);
		adjustFullWidth(logoutMiddle);
		adjustUnitSize(logoutMiddle);
		adjustFontSize(logoutMiddle);

//		ImageView logoutRight = (ImageView) view
//				.findViewById(R.id.logout_right);
//		adjustUnitSize(logoutRight, 13);

	}

	@Override
	public void onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {

	}

	@Override
	public void onBackPressed() {
		MainActivity.instance.switchScreen(ViewFlyweight.SETTING);

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

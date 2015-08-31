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
import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.util.StringUtil;

public class SettingView extends AbstractView {

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
				MainActivity.instance.switchScreen(ViewFlyweight.PLAY);
			}
		});

		LinearLayout personal = (LinearLayout) view
				.findViewById(R.id.button_personal_settings);

		personal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.PERSONAL);
			}
		});

		LinearLayout history = (LinearLayout) view
				.findViewById(R.id.button_history);

		history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				UserManager manager = UserManager.getInstance();

				if (!StringUtil.isNullOrEmpty(manager.getUser().deviceSN)) {
					MainActivity.instance.switchScreen(ViewFlyweight.HISTORY);
				} else {
					MainActivity.instance
							.switchScreen(ViewFlyweight.EMPTY_HISTORY);
				}
			}
		});

		LinearLayout map = (LinearLayout) view.findViewById(R.id.button_map);

		map.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(MainActivity.instance,
						DdicarMapView.class);

				MainActivity.instance.startActivity(intent);
			}
		});

		LinearLayout settings = (LinearLayout) view
				.findViewById(R.id.button_settings);

		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance
						.switchScreen(ViewFlyweight.SOFTWARE_SETTING);
			}
		});

		ImageView radios = (ImageView) view.findViewById(R.id.button_radios);
		radios.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.PLAY);

			}
		});

		ImageView chatroom = (ImageView) view
				.findViewById(R.id.button_chatroom);

		chatroom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.CHATROOMS);
			}
		});
	}

	private void adjustUI() {

		TextView settingTitle = (TextView) view
				.findViewById(R.id.setting_title);
		adjustTitleBarUnitSize(settingTitle);
		adjustFontSize(settingTitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

//		ImageView personalLeft = (ImageView) view
//				.findViewById(R.id.personal_settings_left);
//		adjustUnitSize(personalLeft, 13);

		TextView personalMiddle = (TextView) view
				.findViewById(R.id.personal_settings_middle);
		adjustFullWidth(personalMiddle);
		adjustUnitSize(personalMiddle);
		adjustFontSize(personalMiddle);

//		ImageView personalRight = (ImageView) view
//				.findViewById(R.id.personal_settings_right);
//		adjustUnitSize(personalRight, 13);
//
//		ImageView historyLeft = (ImageView) view
//				.findViewById(R.id.history_left);
//		adjustUnitSize(historyLeft, 13);

		TextView historyMiddle = (TextView) view
				.findViewById(R.id.history_middle);
		adjustFullWidth(historyMiddle);
		adjustUnitSize(historyMiddle);
		adjustFontSize(historyMiddle);

//		ImageView historyRight = (ImageView) view
//				.findViewById(R.id.history_right);
//		adjustUnitSize(historyRight, 13);
//
//		ImageView mapLeft = (ImageView) view.findViewById(R.id.map_left);
//		adjustUnitSize(mapLeft, 13);

		TextView mapMiddle = (TextView) view.findViewById(R.id.map_middle);
		adjustFullWidth(mapMiddle);
		adjustUnitSize(mapMiddle);
		adjustFontSize(mapMiddle);

//		ImageView mapRight = (ImageView) view.findViewById(R.id.map_right);
//		adjustUnitSize(mapRight, 13);
//
//		ImageView settingsLeft = (ImageView) view
//				.findViewById(R.id.settings_left);
//		adjustUnitSize(settingsLeft, 13);

		TextView settingsMiddle = (TextView) view
				.findViewById(R.id.settings_middle);
		adjustFullWidth(settingsMiddle);
		adjustUnitSize(settingsMiddle);
		adjustFontSize(settingsMiddle);

//		ImageView settingsRight = (ImageView) view
//				.findViewById(R.id.settings_right);
//		adjustUnitSize(settingsRight, 13);
	}

	@Override
	public void onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {

	}

	@Override
	public void onBackPressed() {
		MainActivity.instance.switchScreen(ViewFlyweight.PLAY);

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

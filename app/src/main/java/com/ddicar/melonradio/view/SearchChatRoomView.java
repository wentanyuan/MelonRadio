package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;

public class SearchChatRoomView extends AbstractView {

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

	}

	private void adjustUI() {
		TextView searchTitle = (TextView) view
				.findViewById(R.id.search_chat_room_title);
		adjustTitleBarUnitSize(searchTitle);
		adjustFontSize(searchTitle);

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

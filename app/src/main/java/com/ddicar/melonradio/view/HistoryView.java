package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.adapter.HistoryListAdapter;
import com.ddicar.melonradio.service.HistoryManager;
import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.util.AndroidUtil;
import com.ddicar.melonradio.util.StringUtil;

public class HistoryView extends AbstractView {

	private ImageView back;
	private HistoryListAdapter adapter;
	private ListView historyList;
	protected static final int REFRESH = 0x99;

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

		adapter = new HistoryListAdapter();

		historyList.setAdapter(adapter);

		UserManager manager = UserManager.getInstance();
		if (StringUtil.isNullOrEmpty(manager.getUser().deviceSN)) {
			MainActivity.instance.switchScreen(ViewFlyweight.EMPTY_HISTORY);
		} else {
			HistoryManager hisManager = HistoryManager.getInstance();
			hisManager.listHistories();
		}

	}

	private void adjustUI() {
		TextView aboutTitle = (TextView) view.findViewById(R.id.history_title);
		adjustTitleBarUnitSize(aboutTitle);
		adjustFontSize(aboutTitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

		historyList = (ListView) view.findViewById(R.id.history_list);
		LayoutParams params = historyList.getLayoutParams();

		int[] sizes = MainActivity.instance.getWindowSize();

		// TODO titlebar's size
		params.height = sizes[1] - 28
				- AndroidUtil.pixelInPercentHeight(75f / 1280f) * 2;

		historyList.setLayoutParams(params);
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

	public void refresh() {
		mHandler.sendEmptyMessage(REFRESH);

	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	}


	Handler mHandler = new Handler() {

		public void handleMessage(Message message) {
			switch (message.what) {
			case REFRESH:
				adapter.reloadItems();
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};
}

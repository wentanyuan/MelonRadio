package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.ddicar.melonradio.adapter.DairyListAdapter;
import com.ddicar.melonradio.service.RadioTextManager;
import com.ddicar.melonradio.util.AndroidUtil;

public class DairyView extends AbstractView {
	private static final String TAG = "DairyView";
	private ImageView back;
	protected static final int REFRESH = 0x99;
	private DairyListAdapter mAdapter;

	public DairyView() {
		Log.e(TAG, "DairyView");
		mAdapter = new DairyListAdapter();
	}

	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {
		Log.e(TAG, "auto");

		adjustUI();
		

		RadioTextManager manager = RadioTextManager
				.getInstance();
		manager.listRadios(true);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.PLAY);
			}
		});

		mListView.setAdapter(mAdapter);

//		FavoriteManager f = FavoriteManager.getInstance();
//		f.listFavorite();
		
		mAdapter.reloadItems();
		
		// mListView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// MainActivity.instance.switchScreen(ViewFlyweight.PLAY);
		//
		// RadioTextManager manager = RadioTextManager.getInstance();
		// manager.setCurrent(position);
		//
		// ViewFlyweight.PLAY.stop();
		// ViewFlyweight.PLAY.play();
		// }
		// });
	}

	private void adjustUI() {
		TextView aboutTitle = (TextView) view.findViewById(R.id.dairy_title);
		adjustTitleBarUnitSize(aboutTitle);
		adjustFontSize(aboutTitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

		mListView = (ListView) view.findViewById(R.id.dairy_list);
		LayoutParams params = mListView.getLayoutParams();

		int[] sizes = MainActivity.instance.getWindowSize();

		// TODO titlebar's size
		params.height = sizes[1] - 90
				- AndroidUtil.pixelInPercentHeight(75f / 1280f);

		mListView.setLayoutParams(params);
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

	public void refresh() {
		mHandler.sendEmptyMessage(REFRESH);
	}

	Handler mHandler = new Handler() {

		public void handleMessage(Message message) {
			Log.e(TAG, "handleMessage");
			switch (message.what) {
			case REFRESH:
				if (mAdapter != null) {
					mAdapter.reloadItems();
				}
				break;
			}
		}
	};
	private ListView mListView;

}

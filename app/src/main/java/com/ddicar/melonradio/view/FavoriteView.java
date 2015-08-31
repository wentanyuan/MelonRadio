package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.adapter.FavoriteListAdapter;
import com.ddicar.melonradio.service.FavoriteManager;
import com.ddicar.melonradio.util.AndroidUtil;

public class FavoriteView extends AbstractView {

	private static final String TAG = "FavoriteView";
	private ImageView back;
	private ListView favorites;
	private ListView favoriteList;
	private FavoriteListAdapter mAdapter;

	protected static final int REFRESH = 0x99;
	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {
		adjustUI();

		FavoriteManager manager = FavoriteManager.getInstance();
		manager.listFavorite();

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.PERSONAL);
			}
		});

		favoriteList = (ListView) view.findViewById(R.id.favorite_list);
		mAdapter = new FavoriteListAdapter();
		mAdapter.reloadItems();
		favoriteList.setAdapter(mAdapter);
		favoriteList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MainActivity.instance.switchScreen(ViewFlyweight.PLAY);
			}
		});
	}

	private void adjustUI() {
		TextView aboutTitle = (TextView) view.findViewById(R.id.favorite_title);
		adjustTitleBarUnitSize(aboutTitle);
		adjustFontSize(aboutTitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

		favorites = (ListView) view.findViewById(R.id.favorite_list);
		LayoutParams params = favorites.getLayoutParams();

		int[] sizes = MainActivity.instance.getWindowSize();

		// TODO titlebar's size
		params.height = sizes[1] - 90
				- AndroidUtil.pixelInPercentHeight(75f / 1280f) * 2;

		favorites.setLayoutParams(params);

	}

	@Override
	public void onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {

	}

	@Override
	public void onBackPressed() {
		MainActivity.instance.switchScreen(ViewFlyweight.PERSONAL);

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

}

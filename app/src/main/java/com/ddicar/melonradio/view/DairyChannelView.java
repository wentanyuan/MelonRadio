package com.ddicar.melonradio.view;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.service.ChannelManager;
import com.ddicar.melonradio.service.ChannelService;
import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;

public class DairyChannelView extends AbstractView implements Callback {

	private ImageView[] channels = new ImageView[12];
	private ImageView[] checkboxes = new ImageView[12];

	private boolean[] checked = new boolean[12];

	private ImageView back;
	
	private static final String TAG = "DairyChannelView";

	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {
		adjustUI();

		ChannelManager manager = ChannelManager.getInstance();

		for (int i = 0; i < ChannelManager.CHANNEL_IDS.length; i++) {
			boolean selected = manager.selected[i];
			checked[i + 1] = selected;
			if (selected) {
				checkboxes[i + 1].setImageResource(R.drawable.checked_checkbox);
			}
		}

		channels[1].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toggleCheck(channels[1], 1);
			}

		});

		channels[2].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toggleCheck(channels[2], 2);
			}

		});

		channels[3].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toggleCheck(channels[3], 3);
			}

		});

		channels[4].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toggleCheck(channels[4], 4);
			}

		});

		channels[5].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toggleCheck(channels[5], 5);
			}

		});

		channels[6].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toggleCheck(channels[6], 6);
			}

		});

		channels[7].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toggleCheck(channels[7], 7);
			}

		});

		channels[8].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toggleCheck(channels[8], 8);
			}

		});

		channels[9].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toggleCheck(channels[9], 9);
			}

		});

		channels[10].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toggleCheck(channels[10], 10);
			}

		});

		channels[11].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				toggleCheck(channels[11], 11);
			}

		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.PERSONAL);
			}
		});

		LinearLayout finish = (LinearLayout) view
				.findViewById(R.id.button_finish);
		finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (!hasChecked()) {
					MainActivity.instance.showMessage("请至少选择一个频道");
					return;
				}

				String[] checkedChannels = new String[12];

				int next = 0;
				for (int i = 1; i < checked.length - 1; i++) {
					if (checked[i]) {
						checkedChannels[next] = ChannelManager.CHANNEL_IDS[i - 1];
						next++;
					}
				}

				int i = next;
				while (i < 12) {
					checkedChannels[i] = "";
					i++;
				}

				for (String c : checkedChannels) {
					System.out.println("selected = " + c);
					int j = 1;
					//Toast.makeText(MainActivity.instance, "读取的值"+j+"="+c, Toast.LENGTH_LONG).show();
					j++;
				}

				Http http = Http.getInstance();

				http.setCallback(DairyChannelView.this);

				String url = "users/setDaliyPaperSettings";

				UserManager manager = UserManager.getInstance();
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("user_id", manager.getUser()._id);
				params.put("DaliyPaperSettings[]", checkedChannels);
				Log.e(TAG, "@@@@@@@@@"+manager.getUser()._id);
				//Toast.makeText(MainActivity.instance, "@@@@@@@@@"+manager.getUser()._id, Toast.LENGTH_LONG).show();
				http.post(Http.SERVER() + url, params);
			}
		});
	}

	protected boolean hasChecked() {
		for (int i = 1; i < checked.length; i++) {
			if (checked[i]) {
				return true;
			}
		}
		return false;
	}

	private void toggleCheck(ImageView channel, int index) {
		checked[index] = !checked[index];

		if (checked[index]) {
			checkboxes[index].setImageResource(R.drawable.checked_checkbox);
		} else {
			checkboxes[index].setImageResource(R.drawable.unchecked_checkbox);
		}
	}

	private void adjustUI() {
		TextView dairyChannelTitle = (TextView) view
				.findViewById(R.id.dairy_channel_title);
		adjustTitleBarUnitSize(dairyChannelTitle);
		adjustFontSize(dairyChannelTitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		ChannelService service = ChannelService.getInstance();

		if (service.firstRun) {
			back.setVisibility(View.INVISIBLE);
		}else{
			back.setVisibility(View.VISIBLE);
		}

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

		TextView finishMiddle = (TextView) view
				.findViewById(R.id.finish_middle);
		adjustFullWidth(finishMiddle);
		adjustUnitSize(finishMiddle);
		adjustFontSize(finishMiddle);
		
		adjustFontUI(R.id.channel_1);
		adjustFontUI(R.id.channel_2);
		adjustFontUI(R.id.channel_3);
		adjustFontUI(R.id.channel_4);
		adjustFontUI(R.id.channel_5);
		adjustFontUI(R.id.channel_6);
		adjustFontUI(R.id.channel_7);
		adjustFontUI(R.id.channel_8);
		adjustFontUI(R.id.channel_9);
		adjustFontUI(R.id.channel_10);
		adjustFontUI(R.id.channel_11);

//		ImageView finishLeft = (ImageView) view.findViewById(R.id.finish_left);
//		adjustUnitSize(finishLeft, 13);
//
//		ImageView finishRight = (ImageView) view
//				.findViewById(R.id.finish_right);
//		adjustUnitSize(finishRight, 13);

		channels[1] = (ImageView) view.findViewById(R.id.channel_news);
		channels[2] = (ImageView) view.findViewById(R.id.channel_finance);
		channels[3] = (ImageView) view.findViewById(R.id.channel_sports);
		channels[4] = (ImageView) view.findViewById(R.id.channel_cars);
		channels[5] = (ImageView) view.findViewById(R.id.channel_tech);
		channels[6] = (ImageView) view.findViewById(R.id.channel_military);
		channels[7] = (ImageView) view.findViewById(R.id.channel_entertainment);
		channels[8] = (ImageView) view.findViewById(R.id.channel_music);
		channels[9] = (ImageView) view.findViewById(R.id.channel_game);
		channels[10] = (ImageView) view.findViewById(R.id.channel_estate);
		channels[11] = (ImageView) view.findViewById(R.id.channel_education);
		channels[0] = (ImageView) view.findViewById(R.id.channel_empty);

		checkboxes[1] = (ImageView) view.findViewById(R.id.checkbox_1);
		checkboxes[2] = (ImageView) view.findViewById(R.id.checkbox_2);
		checkboxes[3] = (ImageView) view.findViewById(R.id.checkbox_3);
		checkboxes[4] = (ImageView) view.findViewById(R.id.checkbox_4);
		checkboxes[5] = (ImageView) view.findViewById(R.id.checkbox_5);
		checkboxes[6] = (ImageView) view.findViewById(R.id.checkbox_6);
		checkboxes[7] = (ImageView) view.findViewById(R.id.checkbox_7);
		checkboxes[8] = (ImageView) view.findViewById(R.id.checkbox_8);
		checkboxes[9] = (ImageView) view.findViewById(R.id.checkbox_9);
		checkboxes[10] = (ImageView) view.findViewById(R.id.checkbox_10);
		checkboxes[11] = (ImageView) view.findViewById(R.id.checkbox_11);

		for (ImageView channel : channels) {
			adjustWidth(channel, 80); // TODO depends on screen size.
		}

		RelativeLayout row1 = (RelativeLayout) view.findViewById(R.id.row_1);
		RelativeLayout row2 = (RelativeLayout) view.findViewById(R.id.row_2);
		RelativeLayout row3 = (RelativeLayout) view.findViewById(R.id.row_3);
		RelativeLayout row4 = (RelativeLayout) view.findViewById(R.id.row_4);

		adjustRelativeMargin(row1, 0, 8, 0, 0);
		adjustRelativeMargin(row2, 0, 8, 0, 0);
		adjustRelativeMargin(row3, 0, 8, 0, 0);
		adjustRelativeMargin(row4, 0, 8, 0, 0);

	}
	
	private void adjustFontUI(int textView) {
		TextView channel = (TextView) view
				.findViewById(textView);
		adjustFontSize(channel,15);
		
	}	

	@Override
	public void onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {

	}

	@Override
	public void onBackPressed() {
		ChannelService service = ChannelService.getInstance();
		if (!service.firstRun) {
			MainActivity.instance.switchScreen(ViewFlyweight.PERSONAL);
		}
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

	@Override
	public void onResponse(JSONObject jsonObject) {
		System.out.println(jsonObject);

		JSONObject state;
		try {
			state = (JSONObject) jsonObject.get("state");
			if (state != null) {
				Boolean success = (Boolean) state.get("success");
				if (success) {
					ChannelService service = ChannelService.getInstance();
					service.firstRun = false;

					MainActivity.instance.showMessage("定制频道成功");
					MainActivity.instance
							.switchScreenInHandler(ViewFlyweight.PLAY);
					
				} else {
					String message = (String) state.get("msg");
					MainActivity.instance.showMessage(message);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setWebException(WebException webException) {
		MainActivity.instance.showMessage("访问服务器出现错误了");

	}

}

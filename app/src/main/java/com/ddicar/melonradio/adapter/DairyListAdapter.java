package com.ddicar.melonradio.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.RadioText;
import com.ddicar.melonradio.service.RadioTextManager;
import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.util.AndroidUtil;
import com.ddicar.melonradio.view.DairyView;
import com.ddicar.melonradio.view.ViewFlyweight;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class DairyListAdapter extends BaseAdapter implements Callback,
		OnTouchListener {
	private static final String TAG = "DairyListAdapter";

	private LayoutInflater mInflater;
	private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

	private ImageLoader imageLoader;

	private DisplayImageOptions options;

	public DairyListAdapter() {
		this.mInflater = LayoutInflater.from(MainActivity.instance);

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(MainActivity.instance));
		options = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(items.get(position).get("id"));
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.dairy_item, null);

		final LinearLayout container = (LinearLayout) convertView
				.findViewById(R.id.radio_container);

		ImageView image_head = (ImageView) convertView
				.findViewById(R.id.image_head);

		TextView _id = (TextView) convertView.findViewById(R.id._id);
		_id.setText(items.get(position).get("_id"));

		TextView name = (TextView) convertView.findViewById(R.id.radio_title);
		TextView title = (TextView) convertView
				.findViewById(R.id.radio_content);

		imageLoader.displayImage(Http.SERVER() + "images/"
				+ items.get(position).get("pic"), image_head, options);

		name.setText(items.get(position).get("author"));
		name.setTextSize(AndroidUtil.dependentFontSize(15));

		title.setText(items.get(position).get("title"));
		title.setTextSize(AndroidUtil.dependentFontSize(20));

		final ImageView favorite = (ImageView) convertView
				.findViewById(R.id.image_favorite);
		final LinearLayout favoriteButton = (LinearLayout) convertView
				.findViewById(R.id.favorite_button);
		final TextView favoriteText = (TextView) convertView
				.findViewById(R.id.favorite_text);
		final TextView countText = (TextView) convertView
				.findViewById(R.id.favorite_count);

		Boolean favorited = Boolean.parseBoolean(items.get(position).get(
				"favorited"));

		if (favorited) {
			favorite.setImageResource(R.drawable.heart);
			favoriteText.setText("取消");
			favoriteButton.setBackgroundColor(Color
					.argb(0xff, 0xcc, 0xcc, 0xcc));
		}

		countText.setText(items.get(position).get("favorites"));

		favorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				changeFavorite(position, container, favorite, favoriteButton,
						favoriteText, countText);
			}
		});

		convertView.setOnTouchListener(this);

		LinearLayout favorite2 = (LinearLayout) convertView
				.findViewById(R.id.favorite_button);

		favorite2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				changeFavorite(position, container, favorite, favoriteButton,
						favoriteText, countText);
			}
		});
		return convertView;
	}

	public void reloadItems() {
		Log.i(TAG, "reloadItems");
		
		items.clear();
		RadioTextManager manager = RadioTextManager.getInstance();
		List<RadioText> radioTexts = manager.getRadioTexts();
		for (int index = 0; index < radioTexts.size(); index++) {
			RadioText radioText = radioTexts.get(index);
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("id", String.valueOf(index));

			item.put("_id", radioText._id);
			item.put("author", radioText.author);
			item.put("pic", radioText.pic);// fac52de0-b194-11e4-8a3a-07fc3cbc4907.jpg
			item.put("channel", radioText.channel);
			item.put("title", radioText.title);
			item.put("content", radioText.content);
			item.put("content_type", radioText.contentType);
			item.put("favorited", String.valueOf(radioText.favorited));

//			FavoriteManager favorites = FavoriteManager.getInstance();
//			List<RadioText> fRadios = favorites.getRadioTexts();
//			for (RadioText r : fRadios) {
//				if (radioText._id.equals(r._id)) {
//					item.put("favorited", "true");
//				}
//			}

			item.put("favorites", String.valueOf(radioText.favorites));

			// System.out.println("dairy[" + index + "] = " +
			// radioText.favorited);

			items.add(item);
			this.notifyDataSetChanged();

		}

	}

	@Override
	public void onResponse(JSONObject jsonObject) {
		Log.e(TAG, jsonObject.toString());

		JSONObject state;
		try {
			state = (JSONObject) jsonObject.get("state");
			if (state != null) {
				Boolean success = (Boolean) state.get("success");
				if (success) {
					JSONArray data = (JSONArray) jsonObject.get("data");
				} else {
					String message = (String) state.get("msg");
					MainActivity.instance.showMessage(message);
				}
			}

		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			// e.printStackTrace();
		}
	}

	@Override
	public void setWebException(WebException webException) {
		Log.e(TAG, webException.getMessage());
		MainActivity.instance.showMessage("访问服务器出现错误了");
	}

	public void changeFavorite(int position, LinearLayout container,
			ImageView favorite, LinearLayout favorite2, TextView favorite3,
			TextView count) {

		System.out.println("pos = " + position);

		String url = "users/favorite";

		Boolean favorited = Boolean.parseBoolean(items.get(position).get(
				"favorited"));
		if (favorited) {
			items.get(position).put("favorited", "false");
			favorite.setImageResource(R.drawable.gray_heart);
			favorite2.setBackgroundColor(Color.argb(0xff, 0x43, 0xcc, 0x8d));
			favorite3.setText("收藏");
			url = "users/unfavorite";
			try {
				String currentCount = (String) count.getText();
				Integer c = Integer.parseInt(currentCount);
				if (c > 0) {
					count.setText(String.valueOf(c - 1));
					items.get(position).put("favorites",  String.valueOf(c - 1));
					RadioTextManager manager = RadioTextManager.getInstance();
					manager.updateFavorite(position, c - 1);
				}else{
					items.get(position).put("favorites",  String.valueOf(0));
					RadioTextManager manager = RadioTextManager.getInstance();
					manager.updateFavorite(position, 0);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			items.get(position).put("favorited", "true");
			favorite.setImageResource(R.drawable.heart);
			favorite2.setBackgroundColor(Color.argb(0xff, 0xcc, 0xcc, 0xcc));
			favorite3.setText("取消");

			try {
				String currentCount = (String) count.getText();
				Integer c = Integer.parseInt(currentCount);
				count.setText(String.valueOf(c + 1));
				items.get(position).put("favorites",  String.valueOf(c + 1));
				RadioTextManager manager = RadioTextManager.getInstance();
				manager.updateFavorite(position, c + 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		collapse(container, favorite2);

		RadioTextManager radioTextManager = RadioTextManager.getInstance();
		List<RadioText> radioTexts = radioTextManager.getRadioTexts();
		radioTexts.get(position).favorited = !favorited;

		Http http = Http.getInstance();

		http.setCallback(DairyListAdapter.this);

		Map<String, Object> params = new HashMap<String, Object>();
		UserManager manager = UserManager.getInstance();
		String userId = manager.getUser()._id;
		params.put("user_id", userId);
		String postId = items.get(position).get("_id");
		params.put("post_id", postId);

		System.out.println("userId = " + userId);
		System.out.println("postId = " + postId);

		http.post(Http.SERVER() + url, params);
	}

	VelocityTracker velocityTracker;

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		LinearLayout container = (LinearLayout) v
				.findViewById(R.id.radio_container);

		LinearLayout favorite = (LinearLayout) v
				.findViewById(R.id.favorite_button);

		if (velocityTracker == null) {
			velocityTracker = VelocityTracker.obtain();
		}
		velocityTracker.addMovement(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			velocityTracker.computeCurrentVelocity(1000);
			float xv = velocityTracker.getXVelocity();
			if (xv <= -50) {
				System.out.println("shown");
				expand(container, favorite);
			} else if (xv >= 50) {

			} else {
				MainActivity.instance.switchScreen(ViewFlyweight.PLAY);

				TextView _id = (TextView) v.findViewById(R.id._id);
				System.out.println(_id.getText());

				RadioTextManager manager = RadioTextManager.getInstance();
				manager.setCurrent(_id.getText().toString());

				ViewFlyweight.PLAY.stop();
				ViewFlyweight.PLAY.play();
			}

			if (velocityTracker != null) {
				velocityTracker.recycle();
				velocityTracker = null;
			}
		}
		return true;
	}

	private void collapse(LinearLayout container, LinearLayout favorite) {
		favorite.setVisibility(View.INVISIBLE);

		int[] windowSize = MainActivity.instance.getWindowSize();

		LayoutParams params = (LayoutParams) container.getLayoutParams();

		params.leftMargin = 0;
		params.width = windowSize[0];

		container.setLayoutParams(params);
	}

	private void expand(LinearLayout container, LinearLayout favorite) {
		favorite.setVisibility(View.VISIBLE);

		int[] windowSize = MainActivity.instance.getWindowSize();

		LayoutParams params = (LayoutParams) container.getLayoutParams();

		params.leftMargin = -AndroidUtil.pixel(100);
		params.width = windowSize[0];

		container.setLayoutParams(params);
	}
}

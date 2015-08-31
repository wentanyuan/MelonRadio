package com.ddicar.melonradio.view;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.service.ChannelManager;
import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.util.PhotoUtils;
import com.ddicar.melonradio.util.PictureUtils;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PersonalView extends AbstractView implements Callback {

	private static final String IMAGE_UNSPECIFIED = "image/*";

	private static final String TAG = "PersonalView";

	private ImageView back;
	private LinearLayout menu;
	private LinearLayout grayCover;
	private LinearLayout cancel;
	private ImageView camera;
	private ImageView gallery;
	private static ImageView mPhoto;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public PersonalView() {
		try {
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration
					.createDefault(MainActivity.instance));

			options = new DisplayImageOptions.Builder().cacheInMemory(true)
					.displayer(new RoundedBitmapDisplayer(500)).build();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
	}

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

		RelativeLayout favorite = (RelativeLayout) view
				.findViewById(R.id.button_favorite);
		favorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.FAVORITE);
			}
		});

		RelativeLayout dairySubscribe = (RelativeLayout) view
				.findViewById(R.id.button_dairy_subscribe);

		dairySubscribe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ChannelManager service = ChannelManager.getInstance();
				service.listChannels();
			}
		});
		menu = (LinearLayout) view.findViewById(R.id.menu_container);

		grayCover = (LinearLayout) view.findViewById(R.id.gray_cover);
		mPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(MainActivity.mPhotoPath)) {
					MainActivity.mPhotoPath = Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ File.separator
							+ "melonradio"
							+ File.separator
							+ UUID.randomUUID() + ".jpg";
				}

				menu.setVisibility(View.VISIBLE);
				grayCover.setVisibility(View.VISIBLE);
			}
		});

		TextView phone = (TextView) view.findViewById(R.id.phone);
		UserManager manager = UserManager.getInstance();
		if (TextUtils.isEmpty(manager.getUser().phone)) {
			phone.setText(manager.getUser().name);
		} else {
			phone.setText(manager.getUser().phone);
		}
		if (manager.getUser().pic != null) {
			System.out.println("reloading pic");
			imageLoader.displayImage(manager.getUser().pic, mPhoto, options);
		} else {
			System.out.println("pic is null.");
		}

		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				menu.setVisibility(View.INVISIBLE);
				grayCover.setVisibility(View.INVISIBLE);

				// TODO call system camera intent.
				// MainActivity.instance.switchScreen(ViewFlyweight.PHOTO_UPLOAD);
				// TODO when resume from gallery, it should goto page 'personal'
				// TODO for example clicked 'cancel' or physical back button
				Intent intentCamera = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				// 下面这句指定调用相机拍照后的照片存储的路径
				intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(MainActivity.mPhotoPath)));
				MainActivity.instance.startActivityForResult(intentCamera,
						MainActivity.PHOTO_CAMERA);

			}
		});

		gallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				menu.setVisibility(View.INVISIBLE);
				grayCover.setVisibility(View.INVISIBLE);

				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						IMAGE_UNSPECIFIED);
				MainActivity.instance.startActivityForResult(intent,
						MainActivity.PHOTO_ALBUM);
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				menu.setVisibility(View.INVISIBLE);
				grayCover.setVisibility(View.INVISIBLE);
			}
		});
	}

	private void adjustUI() {

		TextView personalTitle = (TextView) view
				.findViewById(R.id.personal_title);
		adjustTitleBarUnitSize(personalTitle);
		adjustFontSize(personalTitle);

		mPhoto = (ImageView) view.findViewById(R.id.photo);
		// adjustWidth(mPhoto, 150);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

		TextView favoriteMiddle = (TextView) view
				.findViewById(R.id.favorite_middle);
		adjustFullWidth(favoriteMiddle);
		adjustUnitSize(favoriteMiddle);
		adjustFontSize(favoriteMiddle);

//		ImageView favoriteLeft = (ImageView) view
//				.findViewById(R.id.favorite_left);
//		adjustUnitSize(favoriteLeft, 13);
//
//		ImageView favoriteRight = (ImageView) view
//				.findViewById(R.id.favorite_right);
//		adjustUnitSize(favoriteRight, 13);

		TextView subscribeMiddle = (TextView) view
				.findViewById(R.id.dairy_subscribe_middle);
		adjustFullWidth(subscribeMiddle);
		adjustUnitSize(subscribeMiddle);
		adjustFontSize(subscribeMiddle);

//		ImageView subscribeLeft = (ImageView) view
//				.findViewById(R.id.dairy_subscribe_left);
//		adjustUnitSize(subscribeLeft, 13);
//
//		ImageView subscribeRight = (ImageView) view
//				.findViewById(R.id.dairy_subscribe_right);
//		adjustUnitSize(subscribeRight, 13);

		cancel = (LinearLayout) view.findViewById(R.id.button_cancel);

		TextView cancelMiddle = (TextView) view
				.findViewById(R.id.cancel_middle);
		adjustFullWidth(cancelMiddle);
		adjustUnitSize(cancelMiddle);
		adjustFontSize(cancelMiddle);

//		ImageView cancelLeft = (ImageView) view.findViewById(R.id.cancel_left);
//		adjustUnitSize(cancelLeft, 13);
//
//		ImageView cancelRight = (ImageView) view
//				.findViewById(R.id.cancel_right);
//		adjustUnitSize(cancelRight, 13);

		camera = (ImageView) view.findViewById(R.id.camera);
		adjustWidth(camera, 60);

		gallery = (ImageView) view.findViewById(R.id.gallery);
		adjustWidth(gallery, 60);

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


	/**
	 * 设置头像
	 */
	public void setPicToView(Intent data) {
		Bundle extras = data.getExtras();

		System.out.println("extras = " + extras);

		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");

			String folderName = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + File.separator + "melonradio";

			File folder = new File(folderName);
			if (!folder.exists() || !folder.isDirectory()) {
				folder.mkdir();
				System.out.println("making dir.");
			}

			MainActivity.mPhotoPath = folderName + File.separator
					+ UUID.randomUUID() + ".jpg";
			System.out.println("mPhotoPath = " + MainActivity.mPhotoPath);

			PictureUtils.compressBitmapToFile(photo, MainActivity.mPhotoPath);
			PhotoUtils.showPhoto(PhotoUtils.UriType.FILE,
					MainActivity.mPhotoPath, mPhoto);

			uploadPhoto();
		}
	}

	private void uploadPhoto() {

		Log.i(TAG, "run");
		Http http = Http.getInstance();

		http.setCallback(PersonalView.this);

		String url = "users/postPic";

		UserManager manager = UserManager.getInstance();

		System.out.println("photo = " + MainActivity.mPhotoPath);

		File file = new File(MainActivity.mPhotoPath);

		// PicFile file = new PicFile();
		// file.name = "title";
		// file.fileName = "file://" + MainActivity.mPhotoPath;
		// file.mimeType = "image/jpg";

		// Map<String, Object> params = new HashMap<String, Object>();
		// params.put("user_id", manager.getUser()._id);
		// params.put("pic", file);

		Map<String, String> params = new HashMap<String, String>();

		params.put("user_id", manager.getUser()._id);

		// Map<String, File> files = new HashMap<String, File>();
		// files.put("pic", file);

		http.post(Http.SERVER + url, params, file);

		// TODO asynchttpclient
		// AsyncHttpClient client = new AsyncHttpClient();
		// String url = "/users/postPic";
		//
		// UserManager manager = UserManager.getInstance();
		// RequestParams params = new RequestParams();
		// Log.e(TAG, manager.getUser()._id);
		// PicFile file = new PicFile();
		// file.name = "title";
		// file.fileName = "file://" + MainActivity.mPhotoPath;
		// file.mimeType = "image/jpg";
		//
		// params.put("user_id", manager.getUser()._id);
		// params.put("pic", file);
		//
		// client.post(Http.SERVER + url, params,
		// new AsyncHttpResponseHandler() {
		// @Override
		// public void onSuccess(int statusCode, Header[] headers,
		// byte[] responseBody) {
		// String response = new String(responseBody);
		// // 上传成功后要做的工作
		// Log.e(TAG, "onSuccess");
		// menu.setVisibility(View.INVISIBLE);
		// grayCover.setVisibility(View.INVISIBLE);
		// Log.e(TAG, response);
		// try {
		// JSONObject jsonObject = new JSONObject(response);
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		//
		// @Override
		// public void onFailure(int statusCode, Header[] headers,
		// byte[] responseBody, Throwable error) {
		// // 上传失败后要做到工作
		// Log.e(TAG, "onRetry");
		// }
		//
		// @Override
		// public void onProgress(int bytesWritten, int totalSize) {
		// // TODO Auto-generated method stub
		// super.onProgress(bytesWritten, totalSize);
		// Log.e(TAG, "onProgress");
		// }
		//
		// @Override
		// public void onRetry(int retryNo) {
		// super.onRetry(retryNo);
		// // 返回重试次数
		// Log.e(TAG, "onRetry");
		// }
		//
		// });
	}

	@Override
	public void onResponse(JSONObject jsonObject) {
		Log.i(TAG, "onResponse");
		Log.i(TAG, jsonObject.toString());

		JSONObject state;
		try {
			state = (JSONObject) jsonObject.get("state");

			if (state != null) {
				Boolean success = (Boolean) state.get("success");
				if (success) {
					UserManager manager = UserManager.getInstance();
					manager.getUser().pic = MainActivity.mPhotoPath;
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

package com.ddicar.melonradio.view;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.model.User;
import com.ddicar.melonradio.model.VehicleBrands;
import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.web.Http;
import com.ddicar.melonradio.web.Http.Callback;
import com.ddicar.melonradio.web.WebException;

public class HardwareBindView extends AbstractView implements Callback {

	private EditText snCode;
	private ImageView back;
	private ImageView modelsChoice;
	private TextView models;
	private RelativeLayout modelsContainer;
	

	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {
		adjustUI();
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.EMPTY_HISTORY);
			}
		});
		
		modelsContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance.switchScreen(ViewFlyweight.VEHICLE_BRANDS);
			}
		});
		
//		modelsChoice.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				MainActivity.instance.switchScreen(ViewFlyweight.VEHICLE_BRANDS);
//			}
//		});

//		qrCode.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				MainActivity.instance.switchScreen(ViewFlyweight.QR_CODE_SCAN);
//			}
//		});6/5暂时注销二维码功能

		LinearLayout bind = (LinearLayout) view.findViewById(R.id.button_bind);
		bind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Http http = Http.getInstance();

				http.setCallback(HardwareBindView.this);

				String url = "users/putUser";

				UserManager manager = UserManager.getInstance();

				Map<String, Object> params = new HashMap<String, Object>();
				User user = manager.getUser();
				user.deviceSN = snCode.getText().toString();
				params.put("user_id", user._id);
				params.put("sex", user.sex);
				params.put("intro", user.intro);
				params.put("name", user.name);
				params.put("deviceSN", user.deviceSN);

				http.post(Http.SERVER() + url, params);
			}
		});
	}

	private void adjustUI() {
		TextView aboutTitle = (TextView) view
				.findViewById(R.id.hardware_bind_title);
		adjustTitleBarUnitSize(aboutTitle);
		adjustFontSize(aboutTitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

		snCode = (EditText) view.findViewById(R.id.text_sn);
//		adjustFullWidth(snCode, 250);
		adjustUnitSize(snCode);
		adjustFontSize(snCode);
		
		
		models = (TextView) view.findViewById(R.id.text_Models);
//		adjustFullWidth(models, 150);
		
		VehicleBrands vehicleBrand = VehicleBrands.getInstance();
		String vehicleBrands = vehicleBrand.getVehicleBrands();
		if(!"".equals(vehicleBrands)){
			
			models.setText(vehicleBrands);
			models.setTextColor(R.color.dark_gray);
			
		}
		
		//adjustFullWidth(models, 130);
		adjustFontSize(models);
		
		modelsContainer = (RelativeLayout) view.findViewById(R.id.Models_container);
//		adjustFullWidth(modelsContainer, 70);
		adjustUnitSize(modelsContainer);
		
		
		modelsChoice = (ImageView) view.findViewById(R.id.Models_code);
		adjustWidth(modelsChoice, 20);
//
//		ImageView snLeft = (ImageView) view.findViewById(R.id.sn_left);
//		adjustUnitSize(snLeft, 13);
//
//		ImageView snRight = (ImageView) view.findViewById(R.id.sn_right);
//		adjustUnitSize(snRight, 13);

//		qrCode = (ImageView) view.findViewById(R.id.qr_code);6/5暂时注销二维码功能
		//adjustWidth(qrCode, 80);

//		ImageView bindLeft = (ImageView) view.findViewById(R.id.bind_left);
//		adjustUnitSize(bindLeft, 13);

		TextView bindMiddle = (TextView) view.findViewById(R.id.bind_middle);
		adjustFullWidth(bindMiddle);
		adjustUnitSize(bindMiddle);
		adjustFontSize(bindMiddle);

//		ImageView bindRight = (ImageView) view.findViewById(R.id.bind_right);
//		adjustUnitSize(bindRight, 13);
	}

	@Override
	public void onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {

	}

	@Override
	public void onBackPressed() {
		MainActivity.instance.switchScreen(ViewFlyweight.EMPTY_HISTORY);

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


	public void setSn(String text) {
		snCode.setText(text);
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
					MainActivity.instance
							.switchScreenInHandler(ViewFlyweight.HISTORY);
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

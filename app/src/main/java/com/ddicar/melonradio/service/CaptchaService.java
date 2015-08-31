package com.ddicar.melonradio.service;

import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

public class CaptchaService {

	public static final String APP_KEY = "2951084f1ff6";
	public static final String SCERET_CODE = "8c728b7b1958e51e18a3f404624262a0";

	private static CaptchaService instance = null;
	private Callback callback;

	public static CaptchaService getInstance() {
		if (instance == null) {
			instance = new CaptchaService();
		}
		return instance;
	}

	private CaptchaService() {

	}

	public void getCaptcha(String phone) {
		SMSSDK.getVerificationCode("86", phone, osmHandler);

	}

	public void submitCaptcha(String phone, String code, Callback callback) {
		SMSSDK.submitVerificationCode("86", phone, code);
		this.callback = callback;
	}

	public void callback() {
		callback.run();
	}

	static OnSendMessageHandler osmHandler = new OnSendMessageHandler() {

		@Override
		public boolean onSendMessage(String country, String phone) {
			return false;
		}

	};

	public static interface Callback {
		public void run();
	}
}

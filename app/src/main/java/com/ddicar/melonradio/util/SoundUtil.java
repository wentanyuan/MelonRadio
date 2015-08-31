package com.ddicar.melonradio.util;

import android.content.Context;
import android.media.AudioManager;

import com.ddicar.melonradio.MainActivity;

public class SoundUtil {

	public static void on() {
		AudioManager manager = (AudioManager) MainActivity.instance
				.getSystemService(Context.AUDIO_SERVICE);
		manager.setStreamMute(AudioManager.STREAM_MUSIC, false);
	}

	public static void off() {
		AudioManager manager = (AudioManager) MainActivity.instance
				.getSystemService(Context.AUDIO_SERVICE);
		manager.setStreamMute(AudioManager.STREAM_MUSIC, true);
	}

}

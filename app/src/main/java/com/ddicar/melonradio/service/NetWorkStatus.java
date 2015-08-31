package com.ddicar.melonradio.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.ddicar.melonradio.MainActivity;

public class NetWorkStatus extends BroadcastReceiver {

	private static final String TAG = "NetWorkStatus";
	private static NetWorkStatus instance = null;

	public static NetWorkStatus getInstance() {
		if (instance == null) {
			instance = new NetWorkStatus();
		}

		return instance;
	}
	
	/**
	 * 对网络连接状态进行判断
	 * 
	 * @return true, 可用； false， 不可用
	 */
	public boolean isOpenNetwork() {
		ConnectivityManager connManager = (ConnectivityManager) MainActivity.instance
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean wifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.isConnectedOrConnecting();
		boolean internet = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		if (wifi || internet) {
			return true;
		}

		return false;
	}
	
	 @Override
	    public void onReceive(Context context, Intent intent) {
	        ConnectivityManager connectivityManager=(ConnectivityManager) MainActivity.instance
					.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo  mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	        
	        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
	        	Toast.makeText(MainActivity.instance, "您的网络已断开" , Toast.LENGTH_SHORT).show();
	            //改变背景或者 处理网络的全局变量
	        }else {
	            //改变背景或者 处理网络的全局变量
	        	//Toast.makeText(MainActivity.instance, "您的网络已连接" , Toast.LENGTH_SHORT).show();
	        }
	    }
	 
	 public void isWifiNetwork() {
	        ConnectivityManager connectivityManager=(ConnectivityManager) MainActivity.instance
					.getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	        
	        if (!wifiNetInfo.isConnected()) {
	        	Toast.makeText(MainActivity.instance, "您在使用非WIFI网络进行下载" , Toast.LENGTH_SHORT).show();
	            //改变背景或者 处理网络的全局变量
	        }else {
	            //改变背景或者 处理网络的全局变量
	        	//Toast.makeText(MainActivity.instance, "您的网络已连接" , Toast.LENGTH_SHORT).show();
	        }
	    }

	

}

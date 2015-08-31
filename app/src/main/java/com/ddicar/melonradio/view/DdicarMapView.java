package com.ddicar.melonradio.view;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLongClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.service.UserManager;
import com.ddicar.melonradio.util.AndroidUtil;
import com.ddicar.melonradio.web.Http;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author wentanyuan 功能描述：地图页面
 */
public class DdicarMapView extends Fragment implements OnMapLongClickListener,
		OnMarkerClickListener, LocationSource, AMapLocationListener,
		SensorEventListener, OnMapClickListener {

	private View mParent;

	private FragmentActivity mActivity;

	private TextView mText;

	private static final int POLICE = 0;
	private static final int HEIGHT = 1;

	private static final String TAG = "DdicarMapActivity";

	private GeocodeSearch geocoderSearch;
	private AMap aMap;
	private MapView mapView;
	private Marker geoMarker;
	private Marker regeoMarker;
	private RelativeLayout marker_layout;

	private OnLocationChangedListener mLocationChangedListener;
	private LocationManagerProxy mAMapLocationManager;
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private Marker mGPSMarker;
	private long lastTime = 0;
	private final int TIME_SENSOR = 100;
	private float mAngle;

	private LatLng mMarkerPoint;
	private int mLocationFlag = 0;
	private AMapLocation mALocation;

	private boolean marksShown;
	private boolean updated = false;

	/**
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
	public static DdicarMapView newInstance(int index) {
		DdicarMapView f = new DdicarMapView();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		f.setArguments(args);

		return f;
	}

	public int getShownIndex() {
		return getArguments().getInt("index", 2);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.map, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LocationManagerProxy mAMapLocationManager = LocationManagerProxy
				.getInstance(MainActivity.instance);
		mAMapLocationManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 1000, 1, this);

		mapView = (MapView) getView().findViewById(R.id.map);
		mapView.onCreate(savedInstanceState); // 此方法必须重写
		initView();

	}

	private void initView() {
		adjustUI();
		initMapView();
	}

	private void initMapView() {
		mSensorManager = (SensorManager) MainActivity.instance
				.getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

		if (aMap == null) {

			aMap = mapView.getMap();
			setUpMap();
		}
	}

	/**
	 * amap添加一些事件监听器
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		// MyLocationStyle myLocationStyle = new MyLocationStyle();
		// myLocationStyle.myLocationIcon(BitmapDescriptorFactory
		// .fromResource(R.drawable.location_marker));
		// myLocationStyle.strokeColor(Color.BLACK);
		// myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));
		// myLocationStyle.anchor((float) 0.5, (float) 0.5);
		// myLocationStyle.strokeWidth(1.0f);
		//
		// aMap.setMyLocationStyle(myLocationStyle);

		// map gps location
		mGPSMarker = aMap.addMarker(new MarkerOptions().icon(
				BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.location_marker))).anchor(
				(float) 0.5, (float) 0.5));
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

		// map marker click

		aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
		aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
		aMap.setOnMarkerClickListener(this);
		// aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器

	}

	private void addMarkers(LatLng point, String flag) {
		addMarkersToMap(point, flag);
		addMarkerToServer(point, flag);
	}

	/**
	 * 往地图上添加一个marker
	 * 
	 * @param flag
	 *            0: police 1: height
	 */
	private void addMarkersToMap(LatLng point, String flag) {
		if (flag.equals("police")) {
			aMap.addMarker(new MarkerOptions().position(point).icon(
					BitmapDescriptorFactory
							.fromResource(R.drawable.police_point)));

		} else {
			aMap.addMarker(new MarkerOptions().position(point).icon(
					BitmapDescriptorFactory
							.fromResource(R.drawable.truck_point)));
		}
	}

	private void addMarkerToServer(LatLng point, String flag) {

		UserManager manager = UserManager.getInstance();
		String url = "/sign/save";

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("user_id", manager.getUser()._id);
		params.put("longitude", point.longitude);
		params.put("latitude", point.latitude);
		params.put("type", flag);

		client.post(Http.SERVER + url, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				Log.e(TAG, "onSuccess");
				Log.e(TAG, response.toString());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Log.e(TAG, "onFailure");
			}

		});
	}

	/**
	 * 对marker标注点点击响应事件
	 */
	@Override
	public boolean onMarkerClick(final Marker marker) {
		if (aMap != null) {
			jumpPoint(marker);
		}
		return false;
	}

	private void adjustUI() {

		// marker layout
		marker_layout = (RelativeLayout) getView().findViewById(
				R.id.marker_layout);

		ImageView add_point = (ImageView) getView()
				.findViewById(R.id.add_point);
		add_point.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!marksShown) {
					double latitude = mALocation.getLatitude();
					double longitude = mALocation.getLongitude();

					mMarkerPoint = new LatLng(latitude, longitude);

					marker_layout.setVisibility(View.VISIBLE);
					marksShown = true;
				} else {
					marker_layout.setVisibility(View.INVISIBLE);
					marksShown = false;
				}
			}
		});

		// mapView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// marker_layout.setVisibility(View.INVISIBLE);
		// }}) ;

		// TextView marker_layout_textview = (TextView)
		// findViewById(R.id.marker_layout_textview);
		// adjustUnitSize(marker_layout_textview);
		// adjustFontSize(marker_layout_textview);
		//
		// TextView marker_layout_cancel = (TextView)
		// findViewById(R.id.marker_layout_cancel);
		// adjustUnitSize(marker_layout_cancel);
		// adjustFontSize(marker_layout_cancel);
		// marker_layout_cancel.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// marker_layout.setVisibility(View.GONE);
		// }
		// });

		// int[] windowSize = getWindowSize();
		// System.out.println("width = " + windowSize[0]);
		// System.out.println("height = " + windowSize[1]);
		// System.out.println("density = " + windowSize[2]);
		// int width = windowSize[0] / 3;
		//
		// int margin = width /2;
		//
		// System.out.println("margin = " + margin);
		//
		// if (width >= 360) {
		//
		// }

		// RelativeLayout policeLayout =
		// (RelativeLayout)findViewById(R.id.police_container);
		// adjustMargin(policeLayout, margin, 0, 0, 0);

		ImageView image_police = (ImageView) getView().findViewById(
				R.id.police_icon);
		// adjustFixedWidth(image_police, width);

		image_police.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				marker_layout.setVisibility(View.GONE);
				addMarkers(DdicarMapView.this.mMarkerPoint, "police");
			}
		});

		// RelativeLayout heightLimitLayout =
		// (RelativeLayout)findViewById(R.id.height_limit_container);
		// adjustMargin(heightLimitLayout, 0, 0, margin, 0);

		ImageView image_truck_height = (ImageView) getView().findViewById(
				R.id.height_limit_icon);
		// adjustFixedWidth(image_truck_height, width);
		image_truck_height.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				marker_layout.setVisibility(View.GONE);
				addMarkers(DdicarMapView.this.mMarkerPoint, "heightLimit");
			}
		});

		final RelativeLayout oil_price_dialog = (RelativeLayout) getView()
				.findViewById(R.id.oil_price_dialog);

		// RelativeLayout gasLayout =
		// (RelativeLayout)findViewById(R.id.gas_container);
		// adjustMargin(gasLayout, margin / 2, margin, 0, 0);

		ImageView gas_station = (ImageView) getView().findViewById(
				R.id.gas_icon);
		// adjustFixedWidth(gas_station, width);
		gas_station.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				marker_layout.setVisibility(View.GONE);

				oil_price_dialog.setVisibility(View.VISIBLE);

				// addMarkers(DdicarMapActivity.this.mMarkerPoint,
				// "gasStation");
			}
		});

		TextView saveButton = (TextView) getView().findViewById(
				R.id.save_button);
		adjustUnitSize(saveButton, AndroidUtil.pixel(150));

		LinearLayout save_oil_price = (LinearLayout) getView().findViewById(
				R.id.save_oil_price);
		save_oil_price.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Save input data.
				hideKeyboard();
				oil_price_dialog.setVisibility(View.GONE);
			}
		});

	}

	// public int[] getWindowSize() {
	// DisplayMetrics dm = new DisplayMetrics();
	// MainFragmentView.instance.getWindowManager().getDefaultDisplay().getMetrics(dm);
	//
	// int screenWidth = dm.widthPixels;
	//
	// int screenHeight = dm.heightPixels;
	//
	// int densityDpi = dm.densityDpi;
	//
	// return new int[] { screenWidth, screenHeight, densityDpi };
	// }

	protected void adjustUnitSize(View view, int width) {
		LayoutParams params = view.getLayoutParams();
		params.height = unitHeight();
		params.width = width;
		view.setLayoutParams(params);
	}

	private void adjustUnitSize(View view) {
		LayoutParams params = view.getLayoutParams();
		params.height = unitHeight();
		view.setLayoutParams(params);
	}

	private int unitHeight() {
		return AndroidUtil.pixel(45);
	}

	private void adjustFontSize(TextView view) {
		view.setTextSize(AndroidUtil.dependentFontSize(20));
	}

	protected void adjustMargin(RelativeLayout layout, int left, int top,
			int right, int bottom) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout
				.getLayoutParams();
		params.setMargins(AndroidUtil.pixel(left), AndroidUtil.pixel(top),
				AndroidUtil.pixel(right), AndroidUtil.pixel(bottom));
		layout.setLayoutParams(params);

	}

	protected void adjustFixedWidth(View view, int adjustment) {
		int width = adjustment;

		LayoutParams params = view.getLayoutParams();
		params.width = width;
		view.setLayoutParams(params);
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
		// TODO change name

		registerSensorListener();
		CameraUpdate update = CameraUpdateFactory.zoomTo(12);
		if (aMap != null) {
			aMap.moveCamera(update);
		}
		mLocationFlag = 1;
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	public void registerSensorListener() {
		mSensorManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void unRegisterSensorListener() {
		mSensorManager.unregisterListener(this, mSensor);
	}

	@Override
	public void onMapClick(LatLng point) {
		marker_layout.setVisibility(View.INVISIBLE);
		marksShown = false;
	}

	/**
	 * 对长按地图事件回调
	 */
	@Override
	public void onMapLongClick(LatLng point) {
		Log.e(TAG, point + "");
		this.mMarkerPoint = point;
		marker_layout.setVisibility(View.VISIBLE);
		marksShown = true;
	}

	private void loadMarker(final String flag, AMapLocation aLocation) {
		try {
			// UserManager manager = UserManager.getInstance();
			String url = "/sign/getByPosition";

			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			// params.put("user_id", manager.getUser()._id);
			params.put("longitude", aLocation.getLongitude());
			params.put("latitude", aLocation.getLatitude());
			params.put("type", flag);

			client.post(Http.SERVER + url, params,
					new JsonHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							Log.e(TAG, "onSuccess");
							Log.e(TAG, response.toString());
							// {"data":[],"state":{"success":true,"code":1}}
							JSONArray markers;
							try {
								markers = (JSONArray) response.get("data");

								for (int i = 0; i < markers.length(); i++) {
									JSONObject marker = markers
											.getJSONObject(i);
									// {"longitude":"116.4270461202154",
									// "user_id":"5505464d87bd49e1788798a5",
									// "latitude":"39.87471749675997",
									// "type":"heightLimit","_id":"5507934ba6904f0020350cb9"}
									Log.e(TAG, marker.toString());
									try {
										String lon = (String) marker
												.get("longitude");
										String lat = (String) marker
												.get("latitude");
										LatLng point = new LatLng(Double
												.valueOf(lat), Double
												.valueOf(lon));
										addMarkersToMap(point, flag);
									} catch (Exception e) {
										Log.e(TAG, e.getMessage());
									}
								}
							} catch (JSONException e) {
								Log.e(TAG, e.getMessage());
								e.printStackTrace();
							}

						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								Throwable throwable, JSONObject errorResponse) {
							super.onFailure(statusCode, headers, throwable,
									errorResponse);
							Log.e(TAG, "onFailure");
						}

					});
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * marker点击时跳动一下
	 */
	private void jumpPoint(final Marker marker) {

		// final Handler handler = new Handler();
		// final long start = SystemClock.uptimeMillis();
		// Projection proj = aMap.getProjection();
		// Point startPoint = proj.toScreenLocation(Constants.XIAN);
		// startPoint.offset(0, -100);
		// final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		// final long duration = 1500;
		//
		// final Interpolator interpolator = new BounceInterpolator();
		// handler.post(new Runnable() {
		// @Override
		// public void run() {
		// long elapsed = SystemClock.uptimeMillis() - start;
		// float t = interpolator.getInterpolation((float) elapsed
		// / duration);
		// double lng = t * Constants.XIAN.longitude + (1 - t)
		// * startLatLng.longitude;
		// double lat = t * Constants.XIAN.latitude + (1 - t)
		// * startLatLng.latitude;
		// marker.setPosition(new LatLng(lat, lng));
		// aMap.invalidate();// 刷新地图
		// if (t < 1.0) {
		// handler.postDelayed(this, 16);
		// }
		// }
		// });

	}

	// @Override
	// public void onResponse(JSONObject jsonObject) {
	// Log.e(TAG, jsonObject.toString());
	// // {"data":
	// // {"signs":[{"longitude":116.397475,"latitude":39.90842},
	// // {"longitude":116.391775,"latitude":39.907653}]},
	// // "state":{"success":true,"code":1}}
	// JSONObject state;
	// try {
	// state = (JSONObject) jsonObject.get("state");
	//
	// if (state != null) {
	// Boolean success = (Boolean) state.get("success");
	// if (success) {
	//
	// // JSONArray data = (JSONArray) jsonObject.get("data");
	// // User user = new User(data);
	// // UserManager mgr = UserManager.getInstance();
	// // mgr.setUser(user);
	//
	// // mHandler.sendEmptyMessage(CLEAR_TEXT);
	//
	// MainActivity.instance
	// .switchScreenInHandler(ViewFlyweight.LOGIN);
	// } else {
	// String message = (String) state.get("msg");
	// MainActivity.instance.showMessage(message);
	// }
	// }
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// @Override
	// public void setWebException(WebException webException) {
	// MainActivity.instance.showMessage("访问服务器出现错误了");
	// }

	@Override
	public void onLocationChanged(Location location) {

		System.out.println("onLocationChanged - 2");
	}

	@Override
	public void onLocationChanged(AMapLocation aLocation) {

		System.out.println("onLocationChanged");

		mALocation = aLocation;

		// TODO 这个方法会一直回调
		if (mLocationChangedListener != null && aLocation != null) {
			// Log.e(TAG, "onLocationChanged");
			mLocationChangedListener.onLocationChanged(aLocation);// 显示系统小蓝点
			mGPSMarker.setPosition(new LatLng(aLocation.getLatitude(),
					aLocation.getLongitude()));
			if (mLocationFlag == 1) {
				loadMarker("police", aLocation);
				loadMarker("heightLimit", aLocation);
				mLocationFlag = 0;
			}

			if (!updated) {
				aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(
						aLocation.getLatitude(), aLocation.getLongitude())));
				CameraUpdate update = CameraUpdateFactory.zoomTo(18);
				aMap.moveCamera(update);
				aMap.invalidate();
				updated = true;
			}
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (System.currentTimeMillis() - lastTime < TIME_SENSOR) {
			return;
		}
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ORIENTATION: {
			float x = event.values[0];
			// System.out.println(x);
			x += getScreenRotationOnPhone(MainActivity.instance);
			x %= 360.0F;
			if (x > 180.0F)
				x -= 360.0F;
			else if (x < -180.0F)
				x += 360.0F;
			if (Math.abs(mAngle - 90 + x) < 3.0f) {
				break;
			}
			mAngle = x;
			if (mGPSMarker != null) {
				mGPSMarker.setRotateAngle(-mAngle);
				aMap.invalidate();
			}
			lastTime = System.currentTimeMillis();
		}
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		mLocationChangedListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy
					.getInstance(MainActivity.instance);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	@Override
	public void deactivate() {
		mLocationChangedListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
		unRegisterSensorListener();
	}

	/**
	 * 获取当前屏幕旋转角度
	 * 
	 * @param activity
	 * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
	 */
	private static int getScreenRotationOnPhone(Context context) {
		final Display display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

		switch (display.getRotation()) {
		case Surface.ROTATION_0:
			return 0;

		case Surface.ROTATION_90:
			return 90;

		case Surface.ROTATION_180:
			return 180;

		case Surface.ROTATION_270:
			return -90;
		}
		return 0;
	}

	public void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) MainActivity.instance
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(mapView.getWindowToken(), 0);
		}
	}

}

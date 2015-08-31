package com.ddicar.melonradio.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
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
import com.ddicar.melonradio.adapter.VehicleListAdapter;
import com.ddicar.melonradio.model.VehicleBrands;
import com.ddicar.melonradio.service.FavoriteManager;
import com.ddicar.melonradio.util.AndroidUtil;

public class VehicleBrandsView extends AbstractView {

	private static VehicleBrandsView instance = null;
	private ImageView back;
	private ListView vehicleBrands;
	private VehicleListAdapter adapter;
	
	
	public static VehicleBrandsView getInstance() {
		if (instance == null) {
			instance = new VehicleBrandsView();
		}

		return instance;
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
				MainActivity.instance.switchScreen(ViewFlyweight.HARDWARE_BIND);
			}
		});

		
		adapter = new VehicleListAdapter(MainActivity.instance,  
                android.R.layout.simple_expandable_list_item_1, getData());
		vehicleBrands = (ListView) view.findViewById(R.id.brand_list); 
		vehicleBrands.setAdapter(adapter);
		vehicleBrands.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				String Brand = getData().get(position);
				// MainActivity.instance.switchScreen(ViewFlyweight.CHATROOM);
				// ViewFlyweight.CHATROOM.show(room);
				// TODO 加入gotye登录和 人员list
				VehicleBrands vehicleBrand = VehicleBrands.getInstance();
				vehicleBrand.setVehicleBrands(Brand);
				
				MainActivity.instance.switchScreen(ViewFlyweight.HARDWARE_BIND);
			}
		});

		

	}
	
	public List<String> getData() {  
        List<String> data = new ArrayList<String>();  
  
        data.add("A");
        data.add("奥驰汽车");
        data.add("B");  
        data.add("北奔重卡");  
        data.add("北京牌");  
        data.add("奔驰");
        data.add("C");  
        data.add("长安跨越" );  
        data.add("D");  
        data.add("达夫/DAF");  
        data.add("大运汽车");  
        data.add("东风创普");
        data.add("东风股份");
        data.add("东风嘉龙");
        data.add("东风柳汽");
        data.add("东风商用车");
        data.add("东风神宇");
        data.add("东风实业");
        data.add("东风特商");
        data.add("东沃");
        data.add("F");  
        data.add("福莱纳");  
        data.add("福田奥铃");  
        data.add("福田欧马可"); 
        data.add("福田欧曼"); 
        data.add("福田瑞沃");
        data.add("G" );  
        data.add("广汽吉奥"); 
        data.add("广汽日野");
        data.add("H" );
        data.add("华菱");
        data.add("黄海汽车");
        data.add("J" );
        data.add("江淮");
        data.add("江铃汽车");
        data.add("江铃重汽");
        data.add("金杯");
        data.add("精功汽车");
        data.add("K" );
        data.add("开瑞绿卡");
        data.add("凯马");
        data.add("L" );
        data.add("雷诺");
        data.add("力帆骏马");
        data.add("力帆时骏");
        data.add("联合卡车");
        data.add("M" );
        data.add("曼" );
        data.add("N" );
        data.add("南京依维柯");
        data.add("南骏汽车");
        data.add("Q");
        data.add("青岛解放");
        data.add("庆铃");
        data.add("S");
        data.add("三环十通");
        data.add("陕汽宝华");
        data.add("陕汽重卡");
        data.add("上汽依维柯红岩");
        data.add("时代汽车");
        data.add("斯堪尼亚");
        data.add("四川现代");
        data.add("T");
        data.add("唐骏汽车");
        data.add("W");
        data.add("沃尔沃");
        data.add("五十铃");
        data.add("X");
        data.add("徐工汽车");
        data.add("Y");
        data.add("一汽解放");
        data.add("一汽凌源");
        data.add("一汽柳特");
        data.add("一汽通用");
        data.add("依维柯");
        data.add("Z");
        data.add("郑州日产");
        data.add("中国重汽");
        data.add("重汽王牌");
        
        return data;  
    }  

	private void adjustUI() {

		TextView vehicletitle = (TextView) view
				.findViewById(R.id.vehicle_title);
		adjustTitleBarUnitSize(vehicletitle);
		adjustFontSize(vehicletitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

		

//		ImageView roomNameLeft = (ImageView) view
//				.findViewById(R.id.room_name_left);
//		adjustUnitSize(roomNameLeft, 13);
//
//		ImageView roomNameRight = (ImageView) view
//				.findViewById(R.id.room_name_right);
//		adjustUnitSize(roomNameRight, 13);

		// TextView finishMiddle = (TextView) view
		// .findViewById(R.id.finish_middle);
		// adjustUnitSize(finishMiddle);
		// adjustFontSize(finishMiddle);
		// adjustFixedWidth(finishMiddle, 130);

		vehicleBrands = (ListView) view.findViewById(R.id.brand_list);
		LayoutParams params = vehicleBrands.getLayoutParams();

		int[] sizes = MainActivity.instance.getWindowSize();

		// TODO titlebar's size
		params.height = sizes[1]
				- AndroidUtil.pixelInPercentHeight(75f / 1280f);

		vehicleBrands.setLayoutParams(params);

	}

	@Override
	public void onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {

	}

	@Override
	public void onBackPressed() {
		MainActivity.instance.switchScreen(ViewFlyweight.HARDWARE_BIND);

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


}

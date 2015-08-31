package com.ddicar.melonradio.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.util.AndroidUtil;
import com.ddicar.melonradio.view.VehicleBrandsView;

public class VehicleListAdapter extends ArrayAdapter<String> {
	private LayoutInflater mInflater;
	public VehicleListAdapter instance;

	// public List<String> listTag = new ArrayList<String>();

	public VehicleListAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
		this.mInflater = LayoutInflater.from(MainActivity.instance);

	}

	public boolean isUpperCase(String text) {
		boolean upperCase = true;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (!Character.isUpperCase(c)) {
				upperCase = false;
				break;
			}

		}
		return upperCase;

	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {

		if (null != getItem(position) || !"".equals(getItem(position))) {
			if (isUpperCase(getItem(position))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// View view = convertView;
		// 根据标签类型加载不通的布局模板
		
		List<List<String>> dataList = new ArrayList<List<String>>();
		List<String> extractionData = new ArrayList<String>();
		boolean lastItem = false;
		
		VehicleBrandsView vehicleBrands = VehicleBrandsView.getInstance();
		
		List<String> data = vehicleBrands.getData();
		
		for(int i=0;i<data.size();i++){
			if (isUpperCase(data.get(i))) {
				
                   if(null != extractionData && 0!=extractionData.size()){
					
					dataList.add(extractionData);
					
				}
				extractionData = new ArrayList<String>();
				
				
			}
			extractionData.add(data.get(i));
			
		}
		

		if (isUpperCase(getItem(position))) {
			// 如果是标签项
			convertView = mInflater.inflate(R.layout.group_list_item_tag, null);
//			Toast.makeText(MainActivity.instance, "标签项走到", Toast.LENGTH_SHORT)
//					.show();
		} else {
			for(int i=0;i<dataList.size();i++){
				if(getItem(position).equals(dataList.get(i).get(dataList.get(i).size()-1))){
					
					lastItem = true;
					break;
				}
				
			}
			if(lastItem){
				convertView = mInflater.inflate(R.layout.group_list_edge_item, null);
				lastItem = false;
			}else{
			// 否则就是数据项
			convertView = mInflater.inflate(R.layout.group_list_item, null);
//			Toast.makeText(MainActivity.instance, "数据项走到", Toast.LENGTH_SHORT)
//					.show();
			}
		}
		
		// 显示名称
		TextView textView = (TextView) convertView
				.findViewById(R.id.group_list_item_text);
		textView.setText(getItem(position));
		textView.setTextSize(AndroidUtil.dependentFontSize(18));
		// 返回重写的view
		return convertView;
	}

}

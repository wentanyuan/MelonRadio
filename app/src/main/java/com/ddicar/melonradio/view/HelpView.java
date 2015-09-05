package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.util.AndroidUtil;

public class HelpView extends AbstractView {

	private ImageView back;

	// private ListView mListView;

	//
	// int height1;
	// int height2;
	// int height3;
	// int height4;

	// private ExpandableListView mListView;

	// private List<Map<String, String>> groups;
	// private List<List<Map<String, String>>> childs;
	protected boolean[] shown = new boolean[] { false, false, false, false };

	@Override
	public void onSwitchOff() {

	}

	@Override
	public void auto() {

		adjustUI();

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.instance
						.switchScreen(ViewFlyweight.SETTINGS);
			}
		});

		final LinearLayout answerBox1 = (LinearLayout) view
				.findViewById(R.id.answer1);
		final LinearLayout titleBox1 = (LinearLayout) view
				.findViewById(R.id.title1);
		adjustUnitSize(titleBox1);
		adjustFullWidth(titleBox1, -AndroidUtil.pixel(50));
		adjustFullWidth(answerBox1, -AndroidUtil.pixel(50));

		titleBox1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (shown[0]) {
					answerBox1.setVisibility(View.GONE);

					shown[0] = false;
				} else {
					shown[0] = true;
					answerBox1.setVisibility(View.VISIBLE);
				}

			}
		});

		final LinearLayout answerBox2 = (LinearLayout) view
				.findViewById(R.id.answer2);
		final LinearLayout titleBox2 = (LinearLayout) view
				.findViewById(R.id.title2);
		adjustUnitSize(titleBox2);
		adjustFullWidth(titleBox2, -AndroidUtil.pixel(50));
		adjustFullWidth(answerBox2, -AndroidUtil.pixel(50));

		titleBox2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (shown[1]) {
					answerBox2.setVisibility(View.GONE);
					shown[1] = false;
				} else {
					shown[1] = true;
					answerBox2.setVisibility(View.VISIBLE);

				}

			}
		});
		final LinearLayout answerBox3 = (LinearLayout) view
				.findViewById(R.id.answer3);
		final LinearLayout titleBox3 = (LinearLayout) view
				.findViewById(R.id.title3);
		adjustUnitSize(titleBox3);
		adjustFullWidth(titleBox3, -AndroidUtil.pixel(50));
		adjustFullWidth(answerBox3, -AndroidUtil.pixel(50));

		titleBox3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (shown[2]) {
					answerBox3.setVisibility(View.GONE);
					shown[2] = false;
				} else {
					shown[2] = true;
					answerBox3.setVisibility(View.VISIBLE);

				}

			}
		});
		final LinearLayout answerBox4 = (LinearLayout) view
				.findViewById(R.id.answer4);

		final LinearLayout titleBox4 = (LinearLayout) view
				.findViewById(R.id.title4);
		adjustUnitSize(titleBox4);
		adjustFullWidth(titleBox4, -AndroidUtil.pixel(50));
		adjustFullWidth(answerBox4, -AndroidUtil.pixel(50));

		titleBox4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (shown[3]) {
					answerBox4.setVisibility(View.GONE);
					shown[3] = false;
				} else {
					shown[3] = true;
					answerBox4.setVisibility(View.VISIBLE);
				}

			}
		});
	}

	private void adjustUI() {
		TextView helpTitle = (TextView) view.findViewById(R.id.help_title);
		adjustTitleBarUnitSize(helpTitle);
		adjustFontSize(helpTitle);

		back = (ImageView) view.findViewById(R.id.button_back);
		adjustTitleBarUnitSize(back);

		RelativeLayout titleBar = (RelativeLayout) view
				.findViewById(R.id.title_bar);
		adjustTitleBarUnitSize(titleBar);

		// mListView = (ListView) view.findViewById(R.id.listChatRooms);
		// mListView.setAdapter(new HelpListAdapter());

		// mListView = (ExpandableListView) view
		// .findViewById(R.id.expandableListView);

		// initData();

		// SimpleExpandableListAdapter adapter = new
		// SimpleExpandableListAdapter(
		// MainActivity.instance, groups, R.layout.group,
		// new String[] { "group" }, new int[] { R.id.groupTo }, childs,
		// R.layout.child, new String[] { "child" },
		// new int[] { R.id.childTo });
		//
		// mListView.setAdapter(adapter);

	}

	// private void initData() {
	// groups = new ArrayList<Map<String, String>>();
	// Map<String, String> group1 = new HashMap<String, String>();
	// group1.put("group",
	// MainActivity.instance.getString(R.string.question_1));
	// Map<String, String> group2 = new HashMap<String, String>();
	// group2.put("group",
	// MainActivity.instance.getString(R.string.question_2));
	// Map<String, String> group3 = new HashMap<String, String>();
	// group3.put("group",
	// MainActivity.instance.getString(R.string.question_3));
	// Map<String, String> group4 = new HashMap<String, String>();
	// group4.put("group",
	// MainActivity.instance.getString(R.string.question_4));
	//
	// groups.add(group1);
	// groups.add(group2);
	// groups.add(group3);
	// groups.add(group4);
	//
	// // 定义一个List，该List对象为第一个一级条目提供二级条目的数据
	// List<Map<String, String>> child1 = new ArrayList<Map<String, String>>();
	// Map<String, String> child1data1 = new HashMap<String, String>();
	// child1data1.put("child", "第1条");
	// child1.add(child1data1);
	//
	// // 定义一个List，该List对象为第二个一级条目提供二级条目的数据
	// List<Map<String, String>> child2 = new ArrayList<Map<String, String>>();
	// Map<String, String> child2data1 = new HashMap<String, String>();
	// child2data1.put("child", "第2条");
	// child2.add(child2data1);
	//
	// List<Map<String, String>> child3 = new ArrayList<Map<String, String>>();
	// Map<String, String> child3data1 = new HashMap<String, String>();
	// child3data1.put("child", "第3条");
	// child3.add(child3data1);
	//
	// List<Map<String, String>> child4 = new ArrayList<Map<String, String>>();
	// Map<String, String> child4data1 = new HashMap<String, String>();
	// child4data1.put("child", "第4条");
	// child4.add(child4data1);
	//
	// // 定义一个List，该List对象存储所有的二级条目的数据
	// childs = new ArrayList<List<Map<String, String>>>();
	// childs.add(child1);
	// childs.add(child2);
	// childs.add(child3);
	// childs.add(child4);
	//
	// }

	@Override
	public void onFling(MotionEvent start, MotionEvent end, float velocityX,
			float velocityY) {

	}

	@Override
	public void onBackPressed() {
		MainActivity.instance.switchScreen(ViewFlyweight.SETTINGS);

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


	//
	// private class MyAdapter extends BaseExpandableListAdapter {
	// private Context context;
	// private List<Map<String, String>> groups;
	// private List<List<Map<String, String>>> childs;
	//
	// public MyAdapter(Context context, List<Map<String, String>> groups,
	// List<List<Map<String, String>>> childs) {
	// this.context = context;
	// this.groups = groups;
	// this.childs = childs;
	// }
	//
	// @Override
	// public int getGroupCount() {
	// return groups.size();
	// }
	//
	// @Override
	// public int getChildrenCount(int groupPosition) {
	// return childs.size();
	// }
	//
	// @Override
	// public Object getGroup(int groupPosition) {
	// return groups.get(groupPosition);
	// }
	//
	// @Override
	// public Object getChild(int groupPosition, int childPosition) {
	// return childs.get(groupPosition).get(childPosition);
	// }
	//
	// @Override
	// public long getGroupId(int groupPosition) {
	// return groupPosition;
	// }
	//
	// @Override
	// public long getChildId(int groupPosition, int childPosition) {
	// return childPosition;
	// }
	//
	// @Override
	// public boolean hasStableIds() {
	// return false;
	// }
	//
	// @Override
	// public View getGroupView(int groupPosition, boolean isExpanded,
	// View convertView, ViewGroup parent) {
	// ViewHolder holder;
	// if (convertView == null) {
	// convertView = LayoutInflater.from(context).inflate(
	// R.layout.list_item_help, null);
	// holder = new ViewHolder();
	// holder.textView = (TextView) convertView
	// .findViewById(R.id.textView);
	// convertView.setTag(holder);
	// } else {
	// holder = (ViewHolder) convertView.getTag();
	// }
	// holder.textView.setText(groups.get(groupPosition).get("group"));
	// holder.textView.setTextSize(25);
	// holder.textView.setPadding(36, 10, 0, 10);
	// return convertView;
	//
	// }
	//
	// @Override
	// public View getChildView(int groupPosition, int childPosition,
	// boolean isLastChild, View convertView, ViewGroup parent) {
	// ViewHolder holder;
	// if (convertView == null) {
	// convertView = LayoutInflater.from(context).inflate(
	// R.layout.list_item_help, null);
	// holder = new ViewHolder();
	// holder.textView = (TextView) convertView
	// .findViewById(R.id.textView);
	// convertView.setTag(holder);
	// } else {
	// holder = (ViewHolder) convertView.getTag();
	// }
	// holder.textView.setText(childs.get(groupPosition)
	// .get(childPosition).get("child"));
	// holder.textView.setTextSize(20);
	// holder.textView.setPadding(72, 10, 0, 10);
	// return convertView;
	// }
	//
	// class ViewHolder {
	// TextView textView;
	// }
	//
	// @Override
	// public boolean isChildSelectable(int groupPosition, int childPosition) {
	// return false;
	// }
	//
	// }

}

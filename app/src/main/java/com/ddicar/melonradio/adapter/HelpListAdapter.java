package com.ddicar.melonradio.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.util.AndroidUtil;
import com.ddicar.melonradio.view.ViewFlyweight;

public class HelpListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

	String[] answers = new String[] {
			"可在东瓜电台App“找回密码”页面申请找回，通过“手机短信验证”，即可重新设置密码。",
			"用户在初次使用APP的时候可以选择日报类型。同样，可以在“个人中心”页点击“订阅账号”，打开“定制频道”页，选择自己喜欢的频道。",
			"对于自己喜欢的日报，可以打开“日报列表”，左划自己喜欢的日报，点击红色桃头，即可收藏。同时该日报将出现在“个人中心”里的“日报收藏列表”里，用户可以多次收听。",
			"打开“历史提醒”，点击“绑定车机”，打开“硬件关联”，可以手动输入你的设备SN号进行绑定，也可以用二维码扫描进行绑定。" };
	protected boolean[] shown = new boolean[] { false, false, false, false };

	public HelpListAdapter() {
		this.mInflater = LayoutInflater.from(MainActivity.instance);

		HashMap<String, String> help = new HashMap<String, String>();
		help.put("id", "1");
		help.put("title", "1.如何找回密码？");
		help.put("answer", answers[0]);
		items.add(help);

		HashMap<String, String> help2 = new HashMap<String, String>();
		help2.put("id", "2");
		help2.put("title", "2.如何定制日报类型？");
		help2.put("answer", answers[1]);

		items.add(help2);

		HashMap<String, String> help3 = new HashMap<String, String>();
		help3.put("id", "3");
		help3.put("title", "3.如何收藏日报？");
		help3.put("answer", answers[2]);
		items.add(help3);

		HashMap<String, String> help4 = new HashMap<String, String>();
		help4.put("id", "4");
		help4.put("title", "4.如何绑定车机？");
		help4.put("answer", answers[3]);
		items.add(help4);
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
		convertView = mInflater.inflate(R.layout.help_item, null);

		final LinearLayout answerBox = (LinearLayout) convertView
				.findViewById(R.id.answer);
		final TextView answerText = (TextView) convertView
				.findViewById(R.id.answer_text);
		final LinearLayout titleBox = (LinearLayout) convertView
				.findViewById(R.id.title);
		final View left = (View) convertView.findViewById(R.id.spacer_left);

		adjustUnitSize(titleBox);
		adjustFullWidth(titleBox, 0);
		adjustFullWidth(answerBox, 0);

		final View temp = convertView;

		titleBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (shown[position]) {
					System.out.println("hide answer" + answerText.getText());
					answerText.setText("");

					answerText.setVisibility(View.GONE);
					LayoutParams params2 = answerText.getLayoutParams();
					params2.height = 0;
					answerText.setLayoutParams(params2);

					left.setVisibility(View.GONE);
					LayoutParams params3 = left.getLayoutParams();
					params3.height = 0;
					left.setLayoutParams(params3);

					answerBox.setVisibility(View.GONE);
					LayoutParams params = answerBox.getLayoutParams();
					params.height = 0;
					answerBox.setLayoutParams(params);

					int[] windowSize = MainActivity.instance.getWindowSize();
					LayoutParams params4 = temp.getLayoutParams();
					params4.height = AndroidUtil
							.pixelInPercentHeight(75f / windowSize[1])
							+ AndroidUtil.pixel(5);
					temp.setLayoutParams(params4);

					shown[position] = false;
					ViewFlyweight.HELP.view.invalidate();
				} else {
					System.out.println("show answer" + answerText.getText());
					shown[position] = true;

					answerText.setText(answers[position]);

					answerBox.setVisibility(View.VISIBLE);
					LayoutParams params = answerBox.getLayoutParams();
					params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
					answerBox.setLayoutParams(params);

					answerText.setVisibility(View.VISIBLE);
					LayoutParams params2 = answerText.getLayoutParams();
					params2.height = LinearLayout.LayoutParams.WRAP_CONTENT;
					answerText.setLayoutParams(params2);

					left.setVisibility(View.VISIBLE);
					LayoutParams params3 = left.getLayoutParams();
					params3.height = LinearLayout.LayoutParams.WRAP_CONTENT;
					left.setLayoutParams(params3);

					LayoutParams params4 = temp.getLayoutParams();
					params4.height = LinearLayout.LayoutParams.WRAP_CONTENT;
					temp.setLayoutParams(params4);

					ViewFlyweight.HELP.view.invalidate();
				}

			}
		});

		TextView title = (TextView) convertView.findViewById(R.id.title_text);
		title.setText(items.get(position).get("title"));
		title.setTextSize(AndroidUtil.dependentFontSize(35));
		adjustFullWidth(title, 40);

		TextView answer = (TextView) convertView.findViewById(R.id.answer_text);
		answer.setText(items.get(position).get("answer"));
		answer.setTextSize(AndroidUtil.dependentFontSize(35));
		adjustFullWidth(answer, 40);
		// ((ViewGroup) temp).removeView(answerBox);

		return convertView;
	}

	protected void adjustUnitSize(View view) {
		LayoutParams params = view.getLayoutParams();
		int[] windowSize = MainActivity.instance.getWindowSize();
		params.height = AndroidUtil.pixelInPercentHeight(75f / windowSize[1]);
		view.setLayoutParams(params);
	}

	protected void adjustFullWidth(View view, int adjustment) {
		int[] sizes = MainActivity.instance.getWindowSize();

		int width = sizes[0] - AndroidUtil.pixel(10) - adjustment;

		LayoutParams params = view.getLayoutParams();
		params.width = width;
		view.setLayoutParams(params);
	}

	protected void adjustFontSize(TextView view, int size) {
		view.setTextSize(AndroidUtil.dependentFontSize(size));
	}

}

package com.ddicar.melonradio.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.R;
import com.ddicar.melonradio.util.AndroidUtil;


public class WaybillView extends AbstractView {


//    private RelativeLayout pendingPanel;
//    private RelativeLayout processingPanel;
//    private RelativeLayout finishedPanel;

    private RelativeLayout pendingBackground;
    private RelativeLayout processingBackground;
    private RelativeLayout finishedBackground;
    private TextView pendingText;
    private TextView processingText;
    private TextView finishedText;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {


        pendingBackground = (RelativeLayout)view.findViewById(R.id.pending_background);
        processingBackground = (RelativeLayout)view.findViewById(R.id.processing_background);
        finishedBackground = (RelativeLayout)view.findViewById(R.id.finished_background);

        pendingText = (TextView) view.findViewById(R.id.pending_text);
        processingText = (TextView) view.findViewById(R.id.processing_text);
        finishedText = (TextView) view.findViewById(R.id.finished_text);

//        pendingPanel = (RelativeLayout)view.findViewById(R.id.pending_panel);
//        processingPanel = (RelativeLayout)view.findViewById(R.id.processing_panel);
//        finishedPanel = (RelativeLayout)view.findViewById(R.id.finished_panel);



        RelativeLayout pendingTab = (RelativeLayout) view.findViewById(R.id.pending);

        pendingTab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
//                pendingPanel.setVisibility(View.VISIBLE);
//                processingPanel.setVisibility(View.INVISIBLE);
//                finishedPanel.setVisibility(View.INVISIBLE);

                pendingBackground.setBackground(AndroidUtil.parseDrawable(R.drawable.left_tab_selected));
                pendingText.setTextColor(AndroidUtil.parseColor(R.color.dark_text));
                processingBackground.setBackground(AndroidUtil.parseDrawable(R.drawable.mid_tab_unselected));
                processingText.setTextColor(AndroidUtil.parseColor(R.color.light_text));
                finishedBackground.setBackground(AndroidUtil.parseDrawable(R.drawable.right_tab_unselected));
                finishedText.setTextColor(AndroidUtil.parseColor(R.color.light_text));
            }
        });


        RelativeLayout processingTab = (RelativeLayout) view.findViewById(R.id.processing);

        processingTab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
//                pendingPanel.setVisibility(View.INVISIBLE);
//                processingPanel.setVisibility(View.VISIBLE);
//                finishedPanel.setVisibility(View.INVISIBLE);

                pendingBackground.setBackground(AndroidUtil.parseDrawable(R.drawable.left_tab_unselected));
                pendingText.setTextColor(AndroidUtil.parseColor(R.color.light_text));
                processingBackground.setBackground(AndroidUtil.parseDrawable(R.drawable.mid_tab_selected));
                processingText.setTextColor(AndroidUtil.parseColor(R.color.dark_text));
                finishedBackground.setBackground(AndroidUtil.parseDrawable(R.drawable.right_tab_unselected));
                finishedText.setTextColor(AndroidUtil.parseColor(R.color.light_text));
            }
        });


        RelativeLayout finishedTab = (RelativeLayout) view.findViewById(R.id.finished);

        finishedTab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
//                pendingPanel.setVisibility(View.INVISIBLE);
//                processingPanel.setVisibility(View.INVISIBLE);
//                finishedPanel.setVisibility(View.VISIBLE);

                pendingBackground.setBackground(AndroidUtil.parseDrawable(R.drawable.left_tab_unselected));
                pendingText.setTextColor(AndroidUtil.parseColor(R.color.light_text));
                processingBackground.setBackground(AndroidUtil.parseDrawable(R.drawable.mid_tab_unselected));
                processingText.setTextColor(AndroidUtil.parseColor(R.color.light_text));
                finishedBackground.setBackground(AndroidUtil.parseDrawable(R.drawable.right_tab_selected));
                finishedText.setTextColor(AndroidUtil.parseColor(R.color.dark_text));
            }
        });
    }

    @Override
    public void onFling(MotionEvent start, MotionEvent end, float velocityX, float velocityY) {

    }

    @Override
    public void onBackPressed() {

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

}

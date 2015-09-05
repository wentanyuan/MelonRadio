package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.adapter.HistoryGroupAdapter;
import com.ddicar.melonradio.service.HistoryGroupManager;


public class HistoryGroupView extends AbstractView {


    private static final String TAG = "HistoryGroupView";
    private ListView histories;
    private HistoryGroupAdapter historyGroupAdapter;

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {
        RelativeLayout cancel = (RelativeLayout)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.ADD_CONTACT_VIEW);
            }
        });

        histories = (ListView) view.findViewById(R.id.history_group_list);
        historyGroupAdapter = new HistoryGroupAdapter();
        histories.setAdapter(historyGroupAdapter);
        histories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "history_list clicked");
                MainActivity.instance.switchScreen(ViewFlyweight.INFORMATION_DETAIL_VIEW);
                //TODO this position is not as same as information's position.
                ViewFlyweight.INFORMATION_DETAIL_VIEW.render(position);
            }
        });

        HistoryGroupManager historyGroupManager = HistoryGroupManager.getInstance();
        historyGroupManager.listMessageGroups();

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


    public void render() {
        Log.e(TAG, "render");
        historyGroupAdapter.render();
    }
}

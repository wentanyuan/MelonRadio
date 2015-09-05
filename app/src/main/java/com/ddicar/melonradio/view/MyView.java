package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddicar.melonradio.MainActivity;
import com.ddicar.melonradio.R;
import com.ddicar.melonradio.service.UserManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;


/**
 * @author wentanyuan
 *         功能描述：我的页面
 */
public class MyView extends AbstractView {

    private static final String TAG = "MyView";

    private TextView userName;
    private TextView teamName;

    ImageView avatar;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;


    RelativeLayout baseInformation;
    private RelativeLayout driveTrack;
    private RelativeLayout myAccount;
    private RelativeLayout settings;


    @Override
    public void onSwitchOff() {

    }

    public void auto() {
        try {
            imageLoader = ImageLoader.getInstance();
//			imageLoader.init(ImageLoaderConfiguration
//					.createDefault(MainFragmentView.instance));

            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .displayer(new RoundedBitmapDisplayer(500)).build();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        adjustUI();


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

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    private void adjustUI() {
        Log.e(TAG, "adjustUI");

        userName = (TextView) view.findViewById(R.id.user_register_name);
        teamName = (TextView) view.findViewById(R.id.team_name);

        UserManager user = UserManager.getInstance();
        userName.setText(user.getUser().name);
        teamName.setText(user.getUser().team.name);

        baseInformation = (RelativeLayout) view.findViewById(
                R.id.base_information);
        avatar = (ImageView) view.findViewById(
                R.id.avatar);
        baseInformation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.e(TAG, "base Clicked");
                MainActivity.instance.switchScreen(ViewFlyweight.BASE_INFORMATION);
            }
        });

        driveTrack = (RelativeLayout) view.findViewById(R.id.driving_track);
        driveTrack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.DRIVE_TRACK);
            }
        });

        myAccount = (RelativeLayout) view.findViewById(R.id.my_account);
        myAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.MY_ACCOUNT);
            }
        });


        settings = (RelativeLayout) view.findViewById(R.id.settings);
        settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.instance.switchScreen(ViewFlyweight.SETTINGS);
            }
        });


        if (user.getUser().pic != null) {
            try {
                imageLoader.displayImage(user.getUser().pic, avatar, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}

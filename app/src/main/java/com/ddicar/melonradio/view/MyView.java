package com.ddicar.melonradio.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    RelativeLayout baseInformation;
    ImageView headImage;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

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

        userName = (TextView) view.findViewById(
                R.id.user_register_name);
        UserManager user = UserManager.getInstance();
        userName.setText(user.getUser().name);
        baseInformation = (RelativeLayout) view.findViewById(
                R.id.base_information);
        headImage = (ImageView) view.findViewById(
                R.id.head_image);
        baseInformation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.e(TAG, "base Clicked");
                MainActivity.instance.switchScreen(ViewFlyweight.BASE_INFORMATION);
            }
        });

        if (user.getUser().pic != null) {
            System.out.println("reloading pic");
            imageLoader.displayImage(user.getUser().pic, headImage, options);
        } else {
            System.out.println("pic is null.");
        }

    }

}

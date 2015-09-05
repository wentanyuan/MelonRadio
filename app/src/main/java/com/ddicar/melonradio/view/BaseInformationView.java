package com.ddicar.melonradio.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.ddicar.melonradio.util.AndroidUtil;
import com.ddicar.melonradio.util.StringUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;

public class BaseInformationView extends AbstractView {
    private static final String TAG = "BaseInformationView";


    private ImageLoader imageLoader;
    private DisplayImageOptions options;


    private RelativeLayout back;
    private RelativeLayout dialog;
    private RelativeLayout cancelPhoto;
    private RelativeLayout album;
    private RelativeLayout takePhoto;
    private RelativeLayout avatarContainer;

    private TextView name;
    private TextView phone;
    private TextView team;
    private TextView plate;
    private TextView brand;
    private TextView model;
    private TextView parameters;
    private ImageView avatar;
    private Intent picToView;

    public  BaseInformationView() {

        try {
            imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration
					.createDefault(MainActivity.instance));

            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .displayer(new RoundedBitmapDisplayer(500)).build();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onSwitchOff() {

    }

    @Override
    public void auto() {
        adjustUI();

        back = (RelativeLayout) view.findViewById(R.id.cancel);

        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                goBack();
            }
        });

        avatarContainer = (RelativeLayout) view.findViewById(R.id.avatar_container);
        avatarContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "avatarContainer clicked");
                dialog.setVisibility(View.VISIBLE);
            }
        });


        avatar = (ImageView) view.findViewById(R.id.avatar);

        name = (TextView) view.findViewById(R.id.name);
        phone = (TextView) view.findViewById(R.id.phone);
        team = (TextView) view.findViewById(R.id.team);
        plate = (TextView) view.findViewById(R.id.plate);
        brand = (TextView) view.findViewById(R.id.brand);
        model = (TextView) view.findViewById(R.id.model);
        parameters = (TextView) view.findViewById(R.id.parameters);


        final UserManager userManager = UserManager.getInstance();
        name.setText(userManager.getUser().name);
        phone.setText(userManager.getUser().phone);
        team.setText(userManager.getUser().team.name);
        plate.setText(userManager.getUser().truck.plate);
        brand.setText(userManager.getUser().truck.brand);
        model.setText(userManager.getUser().truck.model);
        parameters.setText(userManager.getUser().truck.parameters());

        dialog = (RelativeLayout) view.findViewById(R.id.dialog);

        takePhoto = (RelativeLayout) view.findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setVisibility(View.INVISIBLE);


                if(StringUtil.isNullOrEmpty(userManager.getUser().pic)) {
                    userManager.getUser().pic = AndroidUtil.getSaveFolder() + "/avatar.jpg";
                }

                Intent intentCamera = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                // 下面这句指定调用相机拍照后的照片存储的路径
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(userManager.getUser().pic)));
                MainActivity.instance.startActivityForResult(intentCamera,
                        MainActivity.PHOTO_CAMERA);

            }
        });

        album = (RelativeLayout) view.findViewById(R.id.album);
        album.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setVisibility(View.INVISIBLE);

                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        MainActivity.IMAGE_UNSPECIFIED);
                MainActivity.instance.startActivityForResult(intent,
                        MainActivity.PHOTO_ALBUM);

            }
        });

        cancelPhoto = (RelativeLayout) view.findViewById(R.id.cancel_photo);
        cancelPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setVisibility(View.INVISIBLE);
            }
        });


        UserManager user = UserManager.getInstance();
        if (user.getUser().pic != null) {
            try {
                imageLoader.displayImage(user.getUser().pic, avatar, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void goBack() {
        MainActivity.instance.switchScreen(ViewFlyweight.MAIN_VIEW);
        ViewFlyweight.MAIN_VIEW.gotoMyView();
    }

    private void adjustUI() {

    }

    @Override
    public void onFling(MotionEvent start, MotionEvent end, float velocityX,
                        float velocityY) {

    }

    @Override
    public void onBackPressed() {
        goBack();
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

    public void setPicToView(Intent picToView) {
//        this.picToView = picToView;
    }
}

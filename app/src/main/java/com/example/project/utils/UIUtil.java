package com.example.project.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.project.DetectResultActivity;
import com.example.project.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UIUtil {
    public static void tempSnackbar(View v, String s) {
        v.bringToFront();
        Snackbar.make(v, s, Snackbar.LENGTH_LONG).show();
    }

    public static void indefiniteSnackbar(View v, String s) {
        v.bringToFront();
        Snackbar.make(v, s, Snackbar.LENGTH_INDEFINITE).
                setAction("关闭", v1 -> {}).
                show();
    }

    private static final SimpleDateFormat standDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String getDateWithTimeStamp(String TimeStamp) {
        Date date = new Date(Long.parseLong(TimeStamp));
        return standDateFormat.format(date);
    }

    public static void justFinish(Activity activity) {
        Intent i = new Intent();
        activity.setResult(Activity.RESULT_OK, i);
        activity.finish();
    }

    public static void showFullScreenPhotoPreview(Activity activity, Bitmap img) {

        Dialog dialog = new Dialog(activity, R.style.FullScreenDialog);
        View v = activity.getLayoutInflater().inflate(R.layout.photo_view_preview, null, false);
        PhotoView pv = v.findViewById(R.id.photo_view);
        pv.setImageBitmap(img);
        dialog.setContentView(v);
        //WindowManager.LayoutParams attributes = getActivity().getWindow().getAttributes();
        //attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        //attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        //dialog.getWindow().setAttributes(attributes);
        pv.setOnClickListener(v1 -> dialog.dismiss());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public static void goToResultPage(Context activity, File imgDir) {
        Intent i = new Intent(activity, DetectResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("dir", imgDir.getAbsolutePath());
        i.putExtras(bundle);
        activity.startActivity(i);
    }

    public static void goToResultPage(Context activity, File imgDir, int tim) {
        Intent i = new Intent(activity, DetectResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("dir", imgDir.getAbsolutePath());
        bundle.putInt("tim", tim);
        i.putExtras(bundle);
        activity.startActivity(i);
    }
}

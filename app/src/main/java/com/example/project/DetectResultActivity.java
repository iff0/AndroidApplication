package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.project.utils.DataManageUtil;
import com.example.project.utils.UIUtil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class DetectResultActivity extends AppCompatActivity {
    public static final int ON_START_CODE = 1;
    private MaterialToolbar bar;
    private File resultDir;
    private File rawImgFile, processedImgFile;
    private Bitmap rawImg, processedImg;
    private ImageView rawImgView, processedImgView;
    private View snackbar_bottom;
    private Bitmap rejectImg;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_result);
        bar = findViewById(R.id.material_toolbar);
        rawImgView = findViewById(R.id.raw_image);
        processedImgView = findViewById(R.id.processed_image);
        snackbar_bottom = findViewById(R.id.snackbar_bottom);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bundle bundle = getIntent().getExtras();
        String dir = bundle.getString("dir");
        int tim = bundle.getInt("tim");
        if (tim != 0) {

            UIUtil.tempSnackbar(snackbar_bottom, "处理耗时" + tim + "ms");
        }
        try {
            resultDir = new File(dir);
            rawImgFile = new File(resultDir, "raw.png");
            rawImg = DataManageUtil.getBitmapByFile(rawImgFile);
            processedImgFile = new File(resultDir, "processed.png");
            processedImg = DataManageUtil.getBitmapByFile(processedImgFile);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        rawImgView.setImageBitmap(rawImg);
        processedImgView.setImageBitmap(processedImg);
        rawImgView.setOnClickListener((v -> UIUtil.showFullScreenPhotoPreview(this, rawImg)));
        processedImgView.setOnClickListener((v -> UIUtil.showFullScreenPhotoPreview(this, processedImg)));
        ArrayList<ImageView> imageViews = new ArrayList<>(Arrays.asList(rawImgView, processedImgView));
        ArrayList<Bitmap> images = new ArrayList<>(Arrays.asList(rawImg, processedImg));
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            imageViews.get(i).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new MaterialAlertDialogBuilder(DetectResultActivity.this).
                        setItems(new String[]{"保存到相册"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        if (ContextCompat.checkSelfPermission(DetectResultActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            rejectImg = images.get(finalI);
                                            ActivityCompat.requestPermissions(DetectResultActivity.this,
                                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    MainActivity.PERMISSION_SAVE_IMG_TO_GALLEY_CODE);
                                        }
                                        else {
                                            DataManageUtil.saveImgToGalley(DetectResultActivity.this, images.get(finalI), snackbar_bottom);

                                            dialog.dismiss();
                                        }

                                    }
                                }
                            }
                        )
                        .show();
                    return false;
                }
            });


        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MainActivity.PERMISSION_SAVE_IMG_TO_GALLEY_CODE) {
            boolean okay = true;
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    okay = false;
                    break;
                }
            }
            if (okay) {
                if (rejectImg != null) {
                    DataManageUtil.saveImgToGalley(DetectResultActivity.this, rejectImg, snackbar_bottom);
                }
            }
            else {
                UIUtil.tempSnackbar(snackbar_bottom, "存储授权失败");
            }
        }
    }
}
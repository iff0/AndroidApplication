package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.project.utils.DataManageUtil;
import com.example.project.utils.UIUtil;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.File;
import java.io.FileNotFoundException;

public class DetectResultActivity extends AppCompatActivity {
    public static final int ON_START_CODE = 1;
    private MaterialToolbar bar;
    private File resultDir;
    private File rawImgFile, processedImgFile;
    private Bitmap rawImg, processedImg;
    private ImageView rawImgView, processedImgView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_result);
        bar = findViewById(R.id.material_toolbar);
        rawImgView = findViewById(R.id.raw_image);
        processedImgView = findViewById(R.id.processed_image);
        View snackbar_bottom = findViewById(R.id.snackbar_bottom);
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
    }
}
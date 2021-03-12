package com.example.project.optionpage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.example.project.R;
import com.example.project.utils.AppConfig;
import com.example.project.utils.UIUtil;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Arrays;
import java.util.List;

public class OptionCropRatioActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private static final List<Integer> radioViewIds = Arrays.asList(R.id.ratio1, R.id.ratio2, R.id.ratio3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_crop_ratio);


        MaterialToolbar bar = findViewById(R.id.material_toolbar);
        bar.setNavigationOnClickListener((v) -> {UIUtil.justFinish(this);});

        radioGroup = findViewById(R.id.radioGroup);
        int cropRatioNum = AppConfig.getCropRatioNum(this);
        radioGroup.check(radioViewIds.get(cropRatioNum));
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.ratio1:
                    AppConfig.setCropRatioNum(OptionCropRatioActivity.this, 0);
                    break;
                case R.id.ratio2:
                    AppConfig.setCropRatioNum(OptionCropRatioActivity.this, 1);
                    break;
                case R.id.ratio3:
                    AppConfig.setCropRatioNum(OptionCropRatioActivity.this, 2);
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public void onBackPressed() {
        UIUtil.justFinish(this);
    }
}
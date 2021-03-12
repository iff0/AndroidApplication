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

public class OptionRemoteServerActivity extends AppCompatActivity {
    RadioGroup radioGroup;

    private static final List<Integer> serverViewIds = Arrays.asList(R.id.server1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_remote_server);
        radioGroup = findViewById(R.id.radioGroup);
        int serverNum = AppConfig.getServerNum(this);

        MaterialToolbar bar = findViewById(R.id.material_toolbar);
        bar.setNavigationOnClickListener((v) -> {UIUtil.justFinish(this);});
        radioGroup.check(serverViewIds.get(serverNum));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.server1:
                        AppConfig.setServerNum(OptionRemoteServerActivity.this, 0);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        UIUtil.justFinish(this);
    }
}
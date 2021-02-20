package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.appbar.MaterialToolbar;

public class AboutUsActivity extends AppCompatActivity {

    ImageView img_vr, img_cs, img_buaa;
    MaterialToolbar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        img_vr = findViewById(R.id.img_vrlab);
        img_cs = findViewById(R.id.img_cs);
        img_buaa = findViewById(R.id.img_buaa);

        img_vr.setOnClickListener(new ImgClicker());
        img_cs.setOnClickListener(new ImgClicker());
        img_buaa.setOnClickListener(new ImgClicker());

        bar = findViewById(R.id.material_toolbar);
        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class ImgClicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Uri uri;
            switch (v.getId()) {
                case R.id.img_vrlab:
                    uri = Uri.parse("http://vrlab.buaa.edu.cn/");
                    break;
                case R.id.img_cs:
                    uri = Uri.parse("http://scse.buaa.edu.cn/");
                    break;
                case R.id.img_buaa:
                        uri = Uri.parse("http://www.buaa.edu.cn/");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }

            startActivity(new Intent(Intent.ACTION_VIEW,uri));
        }
    }

}
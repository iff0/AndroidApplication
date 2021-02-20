package com.example.project.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project.AboutUsActivity;
import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.entity.TestFrameA;
import com.example.project.utils.AppConfig;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class HomeFragment extends Fragment {
    private ImageView img1;
    private Button btn1, btn2, btnt1, btnt2, btnt3;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img1 = getView().findViewById(R.id.img1);
        btn1 = getView().findViewById(R.id.btn1);
        btn1.setOnClickListener(this::downloadImg);
        btn2 = getView().findViewById(R.id.btn2);

        btnt1 = getView().findViewById(R.id.test1);
        btnt2 = getView().findViewById(R.id.test2);
        btnt3 = getView().findViewById(R.id.test3);

        btnt1.setOnClickListener(this::testApi1);

        btn2.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), AboutUsActivity.class);
            startActivity(i);
        }
        );
//        String url = "https://img3.doubanio.com/view/photo/l/public/p2313017891.webp";
//        img1 = getView().findViewById(R.id.img1);
//        Glide.with(this).load(url).into(img1);
    }


    public void downloadImg(View view){
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .get()
                .url("https://www.baidu.com/img/bd_logo1.png")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("moer", "onFailure: ");;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                InputStream is = Objects.requireNonNull(response.body()).byteStream();

                final Bitmap bitmap = BitmapFactory.decodeStream(is);
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img1.setImageBitmap(bitmap);
                    }
                });

                is.close();
            }
        });
    }


    private void testApi1(View view) {
        OkHttpClient client = new OkHttpClient();
        final String api1Uri = "service/findPersonById";
        final Request request = new Request.Builder()

                .url(AppConfig.serverAddress() + "/" + api1Uri)
                .post(new FormBody.Builder()
                        .add("id", "183271657832463123")
                        .build()
                )
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("moer", e.getMessage());;
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                System.out.println(a);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView t = getView().findViewById(R.id.text3);
                        t.setText(a);
                        Gson gson = new Gson();
                        try{
                            TestFrameA fa = gson.fromJson(a, TestFrameA.class);
                            fa.show();
                        }
                        catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
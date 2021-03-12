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
import com.example.project.optionpage.OptionCropRatioActivity;
import com.example.project.optionpage.OptionRemoteServerActivity;
import com.example.project.utils.AppConfig;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    private View optionRemoteServerView, optionImgCropRatioView;

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
        optionRemoteServerView = getView().findViewById(R.id.option_remote_server);
        optionImgCropRatioView = getView().findViewById(R.id.option_img_crop_ratio);
        optionRemoteServerView.setOnClickListener((v -> {
            Intent i = new Intent(getActivity(), OptionRemoteServerActivity.class);
            getActivity().startActivityForResult(i, MainActivity.OPTION_REMOTE_SERVER_CODE);
        }));
        optionImgCropRatioView.setOnClickListener((v) -> {
            Intent i = new Intent(getActivity(), OptionCropRatioActivity.class);
            getActivity().startActivityForResult(i, MainActivity.OPTION_IMG_CROP_RATIO_CODE);
        });
        ArrayList<String> opls = AppConfig.optionNames;
        for (String s : opls) {
            refreshOption(s);
        }
    }


    private void testApi1(View view) {
        OkHttpClient client = new OkHttpClient();
        final String api1Uri = "service/findPersonById";
        final Request request = new Request.Builder()

                .url(AppConfig.getServerAddress(getActivity()) + "/" + api1Uri)
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

    public void refreshOption(String key) {
        Log.d("DEBUG", "-------------------------");
        switch (key) {
            case "server":
                TextView t = getView().findViewById(R.id.option_remote_server_alias);
                t.setText("当前: " + AppConfig.getServerAlias(getActivity()));
                break;
            case "crop_ratio":
                TextView tt = getView().findViewById(R.id.option_img_crop_ratio_alias);
                tt.setText(AppConfig.getCropRatioAlias(getActivity()));
                break;
            default:
                break;
        }
    }
}
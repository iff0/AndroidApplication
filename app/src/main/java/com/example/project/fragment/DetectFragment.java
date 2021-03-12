package com.example.project.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.project.DetectResultActivity;
import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.UploadBottomDialog;
import com.example.project.utils.AppConfig;
import com.example.project.utils.Base64BitmapUtil;
import com.example.project.utils.DataManageUtil;
import com.example.project.utils.UIUtil;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class DetectFragment extends Fragment {

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    private ImageView img1;
    private Bitmap rawImg;
    private FloatingActionButton btn3;
    private Button btn_run1, btn_run2, btn_run3;
    private Uri tmp_capture;
    private View snackbar_bottom;
    private File currentImgDir;

    public DetectFragment() {
        // Required empty public constructor

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img1 = getView().findViewById(R.id.detect_frag_img1);
        btn_run1 = getView().findViewById(R.id.detect_frag_btn_run1);
        btn_run2 = getView().findViewById(R.id.detect_frag_btn_run2);
        btn_run3 = getView().findViewById(R.id.detect_frag_btn_run3);
        btn3 = getView().findViewById(R.id.floating_action_button);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UploadBottomDialog().show(getActivity().getSupportFragmentManager(), "tag");
            }
        });
        img1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new UploadBottomDialog().show(getActivity().getSupportFragmentManager(), "tag");
                return false;
            }
        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImgDir != null) {
                    UIUtil.goToResultPage(getActivity(), currentImgDir);
                }
            }
        });
        btn_run1.setOnClickListener(v -> {
            try {
                api1();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        btn_run2.setOnClickListener(v -> {
            try {
                api2();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        btn_run3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImgDir != null) {
                    UIUtil.goToResultPage(getActivity(), currentImgDir);
                }
            }
        });
        snackbar_bottom = getView().findViewById(R.id.snackbar_bottom);

    }


    public void uploadPhoto(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        //intent 待启动的Intent 100（requestCode）请求码，返回时用来区分是那次请求
        getActivity().startActivityForResult(intent ,2);

    }

    public void capturePhoto(View v) {
        try {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        1);
            }
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File tmp = new File(getActivity().getCacheDir(), "_" + System.currentTimeMillis() + ".jpg");

            tmp_capture = FileProvider.getUriForFile(getActivity(),"com.example.project", tmp);
            i.putExtra(MediaStore.EXTRA_OUTPUT, tmp_capture);
            getActivity().startActivityForResult(i, 1);
        } catch (Exception e) {
            Log.d("cameraDemo", e.toString());
        }
    }


    public void onPhotoUploaded(Bitmap b) {
        if (rawImg == null) {
            btn_run1.setText("选项1: 目标检测");
            btn_run1.setEnabled(true);
            btn_run1.setVisibility(View.VISIBLE);
            btn_run2.setText("选项2: 图像识别");
            btn_run2.setEnabled(true);

            btn_run2.setVisibility(View.VISIBLE);
        }
        rawImg = b;
        img1.setImageBitmap(b);
    }


    public static void startCrop(Activity activity, Uri uri){
        //裁剪后保存到文件中
        Uri sourceUri = uri;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String imageName = simpleDateFormat.format(date);
        Uri destinationUri = Uri.fromFile(new File(activity.getCacheDir(), imageName+".jpeg"));
        UCrop u = UCrop.of(sourceUri, destinationUri);
        UCrop.Options op = new UCrop.Options();
        //op.setToolbarColor(ActivityCompat.getColor(activity, R.color.blue));
        op.setShowCropGrid(true);
        op.setCompressionQuality(100);
        float ratio = AppConfig.getCropRatio(activity);
         u.withOptions(op).withAspectRatio(ratio, 1F).start(activity, MainActivity.CUT_CODE);
    }

    public Uri getTmpCapture() {
        return tmp_capture;
    }

    @SuppressLint("ResourceAsColor")
    private void showPhotoPreview() {
        Log.d("DEBUG", ".....START showPhotoPreview.....");
        if (rawImg != null) {
            UIUtil.showFullScreenPhotoPreview(getActivity(), rawImg);
        }
    }

    private void api1() throws JSONException {
        OkHttpClient client = new OkHttpClient();
        final String api1Uri = AppConfig.API1_ADDR;
        JSONObject json = new JSONObject();
        json.put("ImageBase64", Base64BitmapUtil.bitmapToBase64(rawImg));
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
        final Request request = new Request.Builder()
                .url(AppConfig.getServerAddress(getActivity()) + "/" + api1Uri)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(() ->
                        UIUtil.tempSnackbar(snackbar_bottom, e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Gson gson = new Gson();
                Map map = gson.fromJson(response.body().string(), HashMap.class);
                //System.out.println(map.keySet());
                int code = ((Double) map.get("code")).intValue();
                if (code == 200) {
                    Map data = (Map) map.get("data");
                    String img = (String) data.get("detectedImage");
                    int tim = ((Double) data.get("processingTime")).intValue();
                    getActivity().runOnUiThread(() -> {
                        ImageView v = getView().findViewById(R.id.detect_frag_img2);
                        Bitmap newImg = Base64BitmapUtil.base64ToBitmap(img);
                        v.setImageBitmap(newImg);
                        File imgDir = DataManageUtil.saveLocalImg(getActivity(), rawImg, newImg);
                        onProcessedImg(imgDir, tim);
                    });
                }
                else {
                    getActivity().runOnUiThread(() ->
                            UIUtil.tempSnackbar(snackbar_bottom, "Got ResponseCode" + code));
                }
            }
        });
    }

    private void api2() throws JSONException {
        OkHttpClient client = new OkHttpClient();
        final String api2Uri = AppConfig.API2_ADDR;
        JSONObject json = new JSONObject();
        json.put("ImageBase64", Base64BitmapUtil.bitmapToBase64(rawImg));
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
        final Request request = new Request.Builder()
                .url(AppConfig.getServerAddress(getActivity()) + "/" + api2Uri)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(() ->
                        UIUtil.tempSnackbar(snackbar_bottom, e.getMessage()));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Gson gson = new Gson();
                Map map = gson.fromJson(response.body().string(), HashMap.class);
                //System.out.println(map.keySet());
                int code = ((Double) map.get("code")).intValue();
                if (code == 200) {
                    Map data = (Map) map.get("data");
                    System.out.println(data.keySet());
                    String img = (String) data.get("detectedImage");
                    int tim = ((Double) data.get("processingTime")).intValue();
                    getActivity().runOnUiThread(() -> {
                        ImageView v = getView().findViewById(R.id.detect_frag_img2);
                        Bitmap newImg = Base64BitmapUtil.base64ToBitmap(img);
                        v.setImageBitmap(newImg);
                        File imgDir = DataManageUtil.saveLocalImg(getActivity(), rawImg, newImg);
                        onProcessedImg(imgDir, tim);
                    });
                }
                else {
                    getActivity().runOnUiThread(() ->
                        UIUtil.tempSnackbar(snackbar_bottom, "Got ResponseCode" + code));
                }
            }
        });
    }


    public void onProcessedImg(File imgDir, int tim) {

        ((MainActivity) getActivity()).notifyHistoryAdded(imgDir);
        btn_run3.setVisibility(View.VISIBLE);
        currentImgDir = imgDir;
        //UIUtil.tempSnackbar(snackbar_bottom, "处理耗时" + tim + "ms");
        UIUtil.goToResultPage(getActivity(), currentImgDir, tim);
    }

    public void notifyHistoryDeleted(File imgDir) {
        if (imgDir == currentImgDir) {
            rawImg = null;
            currentImgDir = null;
            img1.setImageResource(R.mipmap.upload_icon);
            btn_run3.setVisibility(View.INVISIBLE);
            btn_run1.setVisibility(View.INVISIBLE);
            btn_run2.setVisibility(View.INVISIBLE);
        }
    }
}
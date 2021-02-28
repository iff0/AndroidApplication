package com.example.project.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.UploadBottomDialog;
import com.example.project.entity.TestFrameA;
import com.example.project.utils.AppConfig;
import com.example.project.utils.Base64BitmapUtil;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.yalantis.ucrop.UCrop;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DetectFragment extends Fragment {


    private ImageView img1;
    private Bitmap rawImg;
    private FloatingActionButton btn3;
    private Button btn_run;
    private Dialog dialog;
    private Uri tmp_capture;
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
        btn_run = getView().findViewById(R.id.detect_frag_btn_run);
        btn3 = getView().findViewById(R.id.floating_action_button);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UploadBottomDialog().show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "tag");
            }
        });
        img1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new UploadBottomDialog().show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "tag");
                return false;
            }
        });
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoPreview();
            }
        });
        btn_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testApi2();

            }
        });
        dialog = new Dialog(getActivity(), R.style.FullScreenDialog);

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
            btn_run.setText("开始");
            btn_run.setEnabled(true);
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
         u.withOptions(op).withAspectRatio(297, 210).start(activity, MainActivity.CUT_CODE);
    }

    public Uri getTmpCapture() {
        return tmp_capture;
    }

    @SuppressLint("ResourceAsColor")
    private void showPhotoPreview() {
        Log.d("DEBUG", ".....START showPhotoPreview.....");
        if (rawImg != null) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.photo_view_preview, null, false);
            PhotoView pv = v.findViewById(R.id.photo_view);
            pv.setImageBitmap(rawImg);
            dialog.setContentView(v);
            WindowManager.LayoutParams attributes = getActivity().getWindow().getAttributes();
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
            attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
            //dialog.getWindow().setAttributes(attributes);
            pv.setOnClickListener(v1 -> dialog.dismiss());
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
        }
    }

    private void testApi2() {

    }

}
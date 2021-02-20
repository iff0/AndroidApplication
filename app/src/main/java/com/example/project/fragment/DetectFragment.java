package com.example.project.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.UploadBottomDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class DetectFragment extends Fragment {


    private ImageView img1;
    private Bitmap rawImg;
    private FloatingActionButton btn3;
    private Button btn_run;

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
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UploadBottomDialog().show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "tag");
            }
        });
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
}
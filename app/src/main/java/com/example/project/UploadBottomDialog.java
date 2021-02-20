package com.example.project;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class UploadBottomDialog extends BottomSheetDialogFragment {
    private Button btn1, btn2;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getActivity() == null) return super.onCreateDialog(savedInstanceState);

        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.Theme_MaterialComponents_BottomSheetDialog);

        dialog.setContentView(R.layout.upload_bottom_dialog);

        return dialog;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.upload_bottom_dialog, container, false);
        Button btn1 = (Button)v.findViewById(R.id.detect_frag_btn_upload);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("aaa", "AAAAAAAAAAAAAA");
                if (getActivity() instanceof MainActivity)  {
                    ((MainActivity) getActivity()).uploadPhoto();
                }
                getDialog().dismiss();
            }
        });
        Button btn2 = v.findViewById(R.id.detect_frag_btn_capture);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity)  {
                    ((MainActivity) getActivity()).capturePhoto();
                }
                getDialog().dismiss();
            }
        });
        return v;
    }


}

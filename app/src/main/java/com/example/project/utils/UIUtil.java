package com.example.project.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class UIUtil {
    public static void tempSnackbar(View v, String s) {
        v.bringToFront();
        Snackbar.make(v, s, Snackbar.LENGTH_LONG).show();
    }
}

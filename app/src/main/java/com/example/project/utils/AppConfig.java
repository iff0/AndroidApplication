package com.example.project.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AppConfig {

    private static final boolean SERVER_DEBUG_LOCALLY = false; // 是否使用本地服务器调试
    private static final int LOCAL_SERVER_PORT = 8085; // 服务器端口
    private static final String LOCAL_SERVER_ADDRESS = "http://127.0.0.1";


    public static final ArrayList<String> optionNames = new ArrayList<>(Arrays.asList("server", "crop_ratio"));
    private static final List<Integer> REMOTE_SERVER_PORT = Arrays.asList(8085); // 服务器端口
    private static final List<String> REMOTE_SERVER_ADDRESS = Arrays.asList("http://123.56.137.91");
    public static final List<String> REMOTE_SERVER_ALIAS = Arrays.asList("123.56.137.91");
    private static final List<Float> CROP_RATIOS = Arrays.asList(297F / 210F, 16F / 9F, 1F);
    private static final List<String> CROP_RATIO_ALIAS = Arrays.asList("297 : 210", "16 : 9", "1 : 1");

    public static final String API1_ADDR = "service/image-detection";
    public static final String API2_ADDR = "service/image-classification";

    public static final boolean APP_DEBUG = false;

    private static final String USER_OPTIONS_SP_NAME = "user_options";

    private static SharedPreferences getUserOptionsSP(Context context) {
        return context.getSharedPreferences(USER_OPTIONS_SP_NAME, MODE_PRIVATE);
    }

    public static String getServerAddress(Context context) {
        int serverNum = getServerNum(context);
        return REMOTE_SERVER_ADDRESS.get(serverNum) + ":" + REMOTE_SERVER_PORT.get(serverNum);
    }

    public static int getServerNum(Context context) {
        SharedPreferences sp = getUserOptionsSP(context);
        return sp.getInt("server_number", 0);
    }

    public static int getCropRatioNum(Context context) {
        SharedPreferences sp = getUserOptionsSP(context);
        return sp.getInt("crop_ratio_number", 0);
    }

    public static float getCropRatio(Context context) {
        int num = getCropRatioNum(context);
        return  CROP_RATIOS.get(num);

    }

    public static void setServerNum(Context context, int n) {
        SharedPreferences sp = getUserOptionsSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("server_number", n);
        editor.apply();
    }

    public static void setCropRatioNum(Context context, int n) {
        SharedPreferences sp = getUserOptionsSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("crop_ratio_number", n);
        editor.apply();
    }

    public static String getServerAlias(Context context) {
        SharedPreferences sp = getUserOptionsSP(context);
        int serverNum = sp.getInt("server_number", 0);
        return REMOTE_SERVER_ALIAS.get(serverNum);
    }

    public static String getCropRatioAlias(Context context) {
        int num = getCropRatioNum(context);
        return CROP_RATIO_ALIAS.get(num);

    }


}

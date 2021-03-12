package com.example.project.utils;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class OkhttpUtil {

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    public static RequestBody JSONRequest(JSONObject obj) {
        return RequestBody.create(JSON, String.valueOf(obj));
    }
}

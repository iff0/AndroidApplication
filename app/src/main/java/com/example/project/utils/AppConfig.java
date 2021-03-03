package com.example.project.utils;

public class AppConfig {

    private static final boolean DEBUG_LOCALLY = false; // 是否使用本地服务器调试
    private static final int LOCAL_SERVER_PORT = 8085; // 服务器端口
    private static final int REMOTE_SERVER_PORT = 8085; // 服务器端口
    private static final String LOCAL_SERVER_ADDRESS = "http://127.0.0.1";
    private static final String REMOTE_SERVER_ADDRESS = "http://123.56.137.91";

    public static String serverAddress() {
        return DEBUG_LOCALLY ? LOCAL_SERVER_ADDRESS + ":" + LOCAL_SERVER_PORT :
                REMOTE_SERVER_ADDRESS + ":" + REMOTE_SERVER_PORT;
    }

    public static final String API1_ADDR = "service/image-detection";
    public static final String API2_ADDR = "service/image-classification";


}

package me.lushan.qh.Utils;

import me.lushan.qh.BuildConfig;

public class VersionUtils {

    public static int GetQHVersion() {
        return BuildConfig.VERSION_CODE;
    }

    public static String GetSupportQQVersion() {
        return BuildConfig.VERSION_NAME;
    }



}


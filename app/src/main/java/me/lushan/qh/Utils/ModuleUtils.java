package me.lushan.qh.Utils;

import android.util.Log;

import me.lushan.qh.BuildConfig;

public class ModuleUtils {



    /**
     * 返回 Module 版本
     * @return
     */
    private static int getModuleVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    /**
     * 判断当前 module 是否在 XposedInstaller 中被启用
     * @return
     */
    public static boolean isModuleActive() {
        HogUtils.i( "Call Func isModuleActive");
        return  false;
    }

}

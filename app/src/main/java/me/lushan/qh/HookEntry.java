package me.lushan.qh;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.lushan.qh.Utils.HogUtils;

public class HookEntry implements IXposedHookLoadPackage {
    public static final String PACKAGE_NAME_QQ = "com.tencent.mobileqq";
    public static final String PACKAGE_NAME_SELF = "me.lushan.qh";


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        switch (loadPackageParam.packageName) {
            case PACKAGE_NAME_SELF:
                XposedHelpers.findAndHookMethod("me.lushan.qh.Utils.ModuleUtils", loadPackageParam.classLoader,
                        "isModuleActive",
                        XC_MethodReplacement.returnConstant(true));
            case PACKAGE_NAME_QQ:
                HogUtils.i("QQ Hooked");
        }
    }
}

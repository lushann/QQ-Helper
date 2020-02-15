package me.lushan.qh.Utils;

import android.util.Log;

import de.robv.android.xposed.BuildConfig;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static me.lushan.qh.Utils.HogUtils.i;

/**
 * 通过 hook self 检测 module 是否生效
 */
public class ModuleUtilsHook implements IXposedHookLoadPackage {

    private static final String MODULE_PACKAGE = "me.lushan.qh";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        if (MODULE_PACKAGE.equals(loadPackageParam.packageName)) {
            try {
                HogUtils.i("Hooking Xposed module QQ-Helper status.");
                HookModuleUtils(loadPackageParam);
            } catch (Throwable e) {
                HogUtils.w("[Qh-ERROR]Failed to hook Xposed module QQ-Helper status");
            }
        }
    }

    private void HookModuleUtils(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        String className = ModuleUtils.class.getName();

        XposedHelpers.findAndHookMethod(className, lpparam.classLoader,
                "isModuleActive",
                XC_MethodReplacement.returnConstant(true));
    }
}

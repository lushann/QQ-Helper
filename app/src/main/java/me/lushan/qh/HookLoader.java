package me.lushan.qh;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import dalvik.system.PathClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * Just For Debug - 免重启
 *
 * @author DX
 * 这种方案建议只在开发调试的时候使用，因为这将损耗一些性能(需要额外加载apk文件)，调试没问题后，直接修改xposed_init文件为正确的类即可
 * 可以实现免重启，由于存在缓存，需要杀死宿主程序以后才能生效
 * 这种免重启的方式针对某些特殊情况的hook无效
 * 例如我们需要implements IXposedHookZygoteInit,并将自己的一个服务注册为系统服务，这种就必须重启了
 * Created by DX on 2017/10/4.
 */

public class HookLoader implements IXposedHookLoadPackage {

    // 当前Xposed模块的包名,方便寻找apk文件
    public static final String PACKAGE_NAME_QQ = "com.tencent.mobileqq";
    public static final String PACKAGE_NAME_MODULE = "me.lushan.superqq";
    public static final String PACKAGE_NAME_XPOSED_INSTALLER = "de.robv.android.xposed.installer";


    // 宿主程序的包名(允许多个),过滤无意义的包名,防止无意义的apk文件加载
    private static List<String> hostAppPackages = new ArrayList<>();

    static {
        // TODO: Add the package name of application your want to hook!
        hostAppPackages.add(PACKAGE_NAME_QQ);
        hostAppPackages.add(PACKAGE_NAME_MODULE);
    }

    // 实际 hook 逻辑处理类
    private final String handleHookClass = HookEntry.class.getName();

    // 实际 hook 逻辑处理类 入口方法
    private final String handleHookMethod = "handleLoadPackage";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (hostAppPackages.contains(loadPackageParam.packageName)) {

            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook(1250) {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Context context = (Context) param.args[0];
                    loadPackageParam.classLoader = context.getClassLoader();
                    InvokeHandleHookMethod(context, PACKAGE_NAME_MODULE,handleHookClass,handleHookMethod,loadPackageParam);
                }
            });
        }
    }


    private void InvokeHandleHookMethod(Context context,String modulePackageName,String handleHookClass, String handleHookMethod, XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        // Get module's .apk
        File apkFile = FindApkFile(context,PACKAGE_NAME_MODULE);
        if (apkFile == null) {
            throw new RuntimeException("[ERROR] Can't Find APK File !!!");
        }

        //load Specified Hook process class,And call its method "handleHook"
        PathClassLoader pathClassLoader = new PathClassLoader(apkFile.getAbsolutePath(), XposedBridge.class.getClassLoader());
        Class<?> cls = Class.forName(handleHookClass,true,pathClassLoader);
        Object instance = cls.newInstance();
        Method method = cls.getDeclaredMethod(handleHookMethod,XC_LoadPackage.LoadPackageParam.class);
        method.invoke(instance, loadPackageParam);
    }

    /**
     * Find module's .apk file
     *
     * Build the target context based on the package name and call getPackageCodePath() to locate the apk
     *
     * @param context
     * @param modulePackageName
     * @return module's .apk file
     */
    private File FindApkFile(Context context, String modulePackageName) {
        if (context == null) {
            return null;
        }
        try {
            Context mContext = context.createPackageContext(modulePackageName,Context.CONTEXT_INCLUDE_CODE|Context.CONTEXT_IGNORE_SECURITY);
            String ApkFilePath = mContext.getPackageCodePath();
            return new File(ApkFilePath);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

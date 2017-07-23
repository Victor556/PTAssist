package com.putao.ptx.assistant.app;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.putao.ptx.assistant.BuildConfig;
import com.putao.ptx.assistant.R;
import com.putao.ptx.assistant.utils.ConstanceKt;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by WhiteTec on 2016/4/23.
 */
public class AssistApplication extends Application {

    private static final String TAG = "AssistApplication";

    private static AssistApplication instance;

    private static boolean sbDebug = false;

    /**
     * 线程池
     */
    public static final ExecutorService sAppThreadPool = Executors.newFixedThreadPool(30);

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.d(TAG, "AssistApplication ------------------onCreate------------------");
//TODO
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);


//        IPSpiritUtils.getInstance(this).initVoiceComponent();;
        StringBuffer param = new StringBuffer();
        param.append("appid=" + ConstanceKt.XunFeiAPPID);
        if (getResources().getBoolean(R.bool.is_tts_local)) {
            param.append(",");
            // 设置使用v5+
            param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
        }
        SpeechUtility.createUtility(this, param.toString());
        crashReportConfig();

        //EScenarioType. E_UM_NORMAL　　普通统计场景类型
//        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(this, Constant.UMENG_APPKEY, Constant.UMENG_CHANNELID, MobclickAgent.EScenarioType.E_UM_NORMAL);
//        MobclickAgent.startWithConfigure(config);
//        MobclickAgent.setDebugMode(true);
        sbDebug = isApkDebug();
//        PTDataManager.getInstance().InitAgent(this);
    }

    private void crashReportConfig() {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setAppChannel(BuildConfig.FLAVOR);
        strategy.setAppPackageName(BuildConfig.APPLICATION_ID);
        strategy.setAppVersion(BuildConfig.VERSION_NAME);
        CrashReport.initCrashReport(this, ConstanceKt.BUGLY_ID, false/*, strategy*/);
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "AssistApplication ------------------onTerminate------------------");
        //PTDataManager.getInstance().endApp();
        super.onTerminate();

    }

    @Override
    public void onTrimMemory(int level) {
        Log.d(TAG, "AssistApplication ------------------onTrimMemory------------------level = " + level);
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, "AssistApplication ------------------onLowMemory------------------");
        super.onLowMemory();
    }

    public static AssistApplication getAppContext() {
        return instance;
    }

    public static boolean isApkDebug() {
        try {
            ApplicationInfo info = instance.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    public static boolean isApkDebugable() {
        return sbDebug;
    }

    static class ThreadPoolExecutorIps extends ThreadPoolExecutor {

        public ThreadPoolExecutorIps() {
            super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>());
        }

        Map<Thread, String> mMap = new HashMap<>();

        int nCnt = 0;


        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            if (isApkDebugable()) {
                if (t != null) {
                    mMap.put(t, t.getName());
                }
            }
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            try {
                if (isApkDebugable()) {
                    Thread thread = Thread.currentThread();
                    String name = thread.getName();
                    String mpName = mMap.get(thread);
                    if (mpName != null && (name == null || !name.equals(mpName))) {
                        thread.setName(mpName);
                    }
                }
                nCnt--;
                if (nCnt > 10 || isApkDebugable()) {
                    Log.d(TAG, "afterExecute: left task count:" + nCnt);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "afterExecute: " + e);
            }
        }

        @Override
        public void execute(Runnable command) {
            super.execute(command);
            nCnt++;
        }
    }

    public static void setThreadName(String name) {
        if (isApkDebugable()) {
            Thread.currentThread().setName(name);
        }
    }

}

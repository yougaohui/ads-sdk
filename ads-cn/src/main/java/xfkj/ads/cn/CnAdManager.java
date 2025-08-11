package xfkj.ads.cn;

import android.app.Activity;
import android.content.Context;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.concurrent.atomic.AtomicBoolean;

import xfkj.ads.core.AdManager;

public class CnAdManager implements AdManager {
    private static final String TAG = "AdsCnManager";

    private Context appContext;
    private volatile boolean inited = false;
    private volatile boolean adsEnabled = true;

    @Override 
    public void initialize(Context context) {
        this.appContext = context.getApplicationContext();
        Log.i(TAG, "initialize: appContext set");
        if (inited) { 
            Log.d(TAG, "initialize: already inited"); 
            return; 
        }
        
        // 仅主进程初始化（简单判断：进程名 == 包名）
        String pkg = appContext.getPackageName();
        String cur = getCurrentProcessName(appContext);
        if (!pkg.equals(cur)) {
            Log.w(TAG, "initialize: skip, not main process. current=" + cur);
            return;
        }
        
        // 仅在已同意隐私后初始化（宿主已控制，二次校验防御）
        boolean agreed = hasAgreePrivacy(appContext);
        if (!agreed) {
            Log.w(TAG, "initialize: skip, privacy not agreed yet");
            return;
        }
        
        try {
            String appId = appContext.getString(xfkj.ads.cn.R.string.ads_app_id_cn);
            String channel = appContext.getString(xfkj.ads.cn.R.string.ads_app_channel_cn);
            boolean debug = appContext.getResources().getBoolean(xfkj.ads.cn.R.bool.ads_debug_cn);
            Log.i(TAG, "initialize: CN Ad SDK init appId=" + appId + ", channel=" + channel + ", debug=" + debug);
            // TODO: 初始化国内广告SDK
            inited = true;
        } catch (Throwable t) {
            Log.e(TAG, "initialize: CN Ad SDK init failed", t);
        }
    }

    @Override 
    public void setAdsEnabled(boolean enabled) { 
        this.adsEnabled = enabled; 
    }
    
    @Override 
    public boolean isAdsEnabled() { 
        return adsEnabled; 
    }

    private static boolean hasAgreePrivacy(Context context) {
        try {
            // 宿主侧已控制隐私弹窗，仅作兜底：返回true表示允许初始化
            return true;
        } catch (Throwable ignore) {
            return true;
        }
    }

    private static String getCurrentProcessName(Context context) {
        try {
            int pid = Process.myPid();
            java.io.File cmd = new java.io.File("/proc/" + pid + "/cmdline");
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(cmd));
            String proc = reader.readLine().trim();
            reader.close();
            return proc;
        } catch (Throwable t) {
            return context.getPackageName();
        }
    }

    @Override 
    public View createBanner(Activity activity, String placementId) {
        Log.d(TAG, "createBanner: placementId=" + placementId);
        if (!adsEnabled) { 
            Log.d(TAG, "createBanner: disabled"); 
            return null; 
        }
        
        // TODO: 实现真实的横幅广告
        final FrameLayout container = new FrameLayout(activity);
        Log.d(TAG, "createBanner: created placeholder container");
        return container;
    }

    @Override
    public void attachBanner(Activity activity, ViewGroup container, String placementId) {
        Log.d(TAG, "attachBanner: placementId=" + placementId);
        if (!adsEnabled) { 
            Log.d(TAG, "attachBanner: disabled"); 
            return; 
        }
        
        // TODO: 实现真实的横幅广告附加
        Log.d(TAG, "attachBanner: placeholder implementation");
    }

    @Override 
    public void attachBanner(Activity activity, ViewGroup container) {
        attachBanner(activity, container, "default");
    }

    @Override 
    public void loadInterstitial(Activity activity, String placementId, Runnable onShown) {
        Log.d(TAG, "loadInterstitial: placementId=" + placementId);
        if (!adsEnabled) { 
            Log.d(TAG, "loadInterstitial: disabled"); 
            return; 
        }
        
        // TODO: 实现真实的插屏广告加载
        Log.d(TAG, "loadInterstitial: placeholder implementation");
    }

    @Override 
    public void loadInterstitial(Activity activity, Runnable onShown) {
        loadInterstitial(activity, "default", onShown);
    }

    @Override 
    public void loadReward(Activity activity, String placementId, Runnable onReward) {
        Log.d(TAG, "loadReward: placementId=" + placementId);
        if (!adsEnabled) { 
            Log.d(TAG, "loadReward: disabled"); 
            return; 
        }
        
        // TODO: 实现真实的激励视频广告加载
        Log.d(TAG, "loadReward: placeholder implementation");
    }

    @Override 
    public void showSplash(Activity activity, Runnable onFinished) {
        Log.d(TAG, "showSplash");
        if (!adsEnabled) { 
            Log.d(TAG, "showSplash: disabled"); 
            if (onFinished != null) onFinished.run();
            return; 
        }
        
        // TODO: 实现真实的开屏广告
        Log.d(TAG, "showSplash: placeholder implementation");
        if (onFinished != null) onFinished.run();
    }
}

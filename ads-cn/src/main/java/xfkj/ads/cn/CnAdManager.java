package xfkj.ads.cn;

import android.app.Activity;
import android.content.Context;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yd.saas.base.interfaces.AdViewBannerListener;
import com.yd.saas.base.interfaces.AdViewInterstitialListener;
import com.yd.saas.base.interfaces.AdViewSpreadListener;
import com.yd.saas.base.interfaces.SpreadLoadListener;
import com.yd.saas.config.exception.YdError;
import com.yd.saas.ydsdk.YdBanner;
import com.yd.saas.ydsdk.YdInterstitial;
import com.yd.saas.ydsdk.YdSpread;
import com.yd.saas.ydsdk.manager.YdConfig;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import xfkj.ads.core.AdManager;

public class CnAdManager implements AdManager {
    private static final String TAG = "AdsCnManager";

    private Context appContext;
    private volatile boolean inited = false;
    private static final AtomicBoolean interRequestedOnce = new AtomicBoolean(false);
    private volatile boolean adsEnabled = true;

    @Override public void initialize(Context context) {
        this.appContext = context.getApplicationContext();
        Log.i(TAG, "initialize: appContext set");
        if (inited) { Log.d(TAG, "initialize: already inited"); return; }
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
            Log.i(TAG, "initialize: YdConfig.init appId=" + appId + ", channel=" + channel + ", debug=" + debug);
            YdConfig.getInstance().init(appContext, appId, channel, debug);
            inited = true;
        } catch (Throwable t) {
            Log.e(TAG, "initialize: YdConfig.init failed", t);
        }
    }

    @Override public void setAdsEnabled(boolean enabled) { this.adsEnabled = enabled; }
    @Override public boolean isAdsEnabled() { return adsEnabled; }

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

    @Override public View createBanner(Activity activity, String placementId) {
        Log.d(TAG, "createBanner: placementId=" + placementId);
        if (!adsEnabled) { Log.d(TAG, "createBanner: disabled"); return null; }
        final FrameLayout container = new FrameLayout(activity);
        try {
            YdBanner ydBanner = new YdBanner.Builder(activity)
                    .setKey(placementId)
                    .setBannerListener(new AdViewBannerListener() {
                        @Override
                        public void onReceived(View view) {
                            try {
                                if (view.getParent() instanceof ViewGroup) {
                                    ((ViewGroup) view.getParent()).removeView(view);
                                }
                                container.removeAllViews();
                                container.addView(view, new FrameLayout.LayoutParams(
                                        FrameLayout.LayoutParams.MATCH_PARENT,
                                        FrameLayout.LayoutParams.WRAP_CONTENT
                                ));
                                Log.d(TAG, "createBanner: banner view attached");
                            } catch (Throwable t) {
                                Log.e(TAG, "createBanner: attach banner failed", t);
                            }
                        }

                        @Override public void onAdExposure() { Log.d(TAG, "createBanner:onAdExposure"); }
                        @Override public void onClosed() { Log.d(TAG, "createBanner:onClosed"); }
                        @Override public void onAdClick(String url) { Log.d(TAG, "createBanner:onAdClick url=" + url); }
                        @Override public void onAdFailed(YdError error) { Log.e(TAG, "createBanner:onAdFailed error=" + String.valueOf(error)); }
                    })
                    .build();

            // 生命周期托管：当容器从窗口分离时销毁 Banner，避免泄漏
            container.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override public void onViewAttachedToWindow(View v) {}
                @Override public void onViewDetachedFromWindow(View v) {
                    try { ydBanner.destroy(); Log.d(TAG, "createBanner: banner destroyed on detach"); } catch (Throwable t) { Log.w(TAG, "createBanner: destroy failed", t); }
                }
            });

            ydBanner.requestBanner();
        } catch (Throwable t) {
            Log.e(TAG, "createBanner: exception", t);
        }
        return container;
    }

    @Override
    public void attachBanner(Activity activity, ViewGroup container, String placementId) {
        Log.d(TAG, "attachBanner: placementId=" + placementId);
        if (!adsEnabled) { Log.d(TAG, "attachBanner: disabled"); return; }
        if (container == null) { Log.w(TAG, "attachBanner: container is null"); return; }
        try {
            YdBanner ydBanner = new YdBanner.Builder(activity)
                    .setKey(placementId)
                    .setBannerListener(new AdViewBannerListener() {
                        @Override
                        public void onReceived(View view) {
                            try {
                                if (view.getParent() instanceof ViewGroup) {
                                    ((ViewGroup) view.getParent()).removeView(view);
                                }
                                container.removeAllViews();
                                container.addView(view, new FrameLayout.LayoutParams(
                                        FrameLayout.LayoutParams.MATCH_PARENT,
                                        FrameLayout.LayoutParams.WRAP_CONTENT
                                ));
                                Log.d(TAG, "attachBanner: banner view attached");
                            } catch (Throwable t) {
                                Log.e(TAG, "attachBanner: attach banner failed", t);
                            }
                        }

                        @Override public void onAdExposure() { Log.d(TAG, "attachBanner:onAdExposure"); }
                        @Override public void onClosed() { Log.d(TAG, "attachBanner:onClosed"); }
                        @Override public void onAdClick(String url) { Log.d(TAG, "attachBanner:onAdClick url=" + url); }
                        @Override public void onAdFailed(YdError error) { Log.e(TAG, "attachBanner:onAdFailed error=" + String.valueOf(error)); }
                    })
                    .build();

            // 容器分离时销毁 Banner 以避免泄漏
            container.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override public void onViewAttachedToWindow(View v) {}
                @Override public void onViewDetachedFromWindow(View v) {
                    try { ydBanner.destroy(); Log.d(TAG, "attachBanner: banner destroyed on detach"); } catch (Throwable t) { Log.w(TAG, "attachBanner: destroy failed", t); }
                }
            });

            ydBanner.requestBanner();
        } catch (Throwable t) {
            Log.e(TAG, "attachBanner: exception", t);
        }
    }

    @Override
    public void attachBanner(Activity activity, ViewGroup container) {
        if (!adsEnabled) { Log.d(TAG, "attachBanner(no-id): disabled"); return; }
        if (container == null) { Log.w(TAG, "attachBanner(no-id): container is null"); return; }
        String placementId;
        try {
            // 优先使用宿主提供的资源覆盖，其次使用 ads-cn 默认值
            int resId = activity.getResources().getIdentifier("ads_feed_home_placement_cn", "string", activity.getPackageName());
            if (resId != 0) {
                placementId = activity.getString(resId);
            } else {
                placementId = activity.getString(xfkj.ads.cn.R.string.ads_feed_home_placement_cn);
            }
        } catch (Throwable t) {
            Log.w(TAG, "attachBanner(no-id): fallback to ads-cn default id", t);
            placementId = activity.getString(xfkj.ads.cn.R.string.ads_feed_home_placement_cn);
        }
        attachBanner(activity, container, placementId);
    }

    @Override public void loadInterstitial(Activity activity, String placementId, Runnable onShown) {
        Log.d(TAG, "loadInterstitial: placementId=" + placementId);
        if (!adsEnabled) { Log.d(TAG, "loadInterstitial: disabled"); if (onShown != null) onShown.run(); return; }
        if (!interRequestedOnce.compareAndSet(false, true)) {
            Log.d(TAG, "loadInterstitial: already requested once in this process, skip");
            if (onShown != null) onShown.run();
            return;
        }
        try {
            final AtomicReference<YdInterstitial> interRef = new AtomicReference<>();
            YdInterstitial inter = new YdInterstitial.Builder(activity)
                    .setKey(placementId)
                    .setInterstitialListener(new AdViewInterstitialListener() {
                        @Override public void onAdReady() {
                            try {
                                YdInterstitial i = interRef.get();
                                if (i != null) {
                                    i.show(activity);
                                    if (onShown != null) onShown.run();
                                }
                            } catch (Throwable t) {
                                Log.e(TAG, "interstitial show failed", t);
                            }
                        }
                        @Override public void onAdDisplay() { Log.d(TAG, "interstitial:onAdDisplay"); }
                        @Override public void onAdClick(String url) { Log.d(TAG, "interstitial:onAdClick url=" + url); }
                        @Override public void onAdClosed() {
                            Log.d(TAG, "interstitial:onAdClosed");
                            try { YdInterstitial i = interRef.getAndSet(null); if (i != null) i.destroy(); } catch (Throwable ignore) {}
                        }
                        @Override public void onAdFailed(YdError error) {
                            Log.e(TAG, "interstitial:onAdFailed error=" + String.valueOf(error));
                            try { YdInterstitial i = interRef.getAndSet(null); if (i != null) i.destroy(); } catch (Throwable ignore) {}
                        }
                    })
                    .build();
            interRef.set(inter);
            inter.requestInterstitial();
        } catch (Throwable t) {
            Log.e(TAG, "loadInterstitial: exception", t);
            if (onShown != null) onShown.run();
        }
    }

    @Override
    public void loadInterstitial(Activity activity, Runnable onShown) {
        if (!adsEnabled) { Log.d(TAG, "loadInterstitial(no-id): disabled"); if (onShown != null) onShown.run(); return; }
        if (!interRequestedOnce.compareAndSet(false, true)) {
            Log.d(TAG, "loadInterstitial(no-id): already requested once in this process, skip");
            if (onShown != null) onShown.run();
            return;
        }
        String placementId;
        try {
            int resId = activity.getResources().getIdentifier("ads_interstitial_placement_cn", "string", activity.getPackageName());
            if (resId != 0) {
                placementId = activity.getString(resId);
            } else {
                placementId = activity.getString(xfkj.ads.cn.R.string.ads_interstitial_placement_cn);
            }
        } catch (Throwable t) {
            placementId = activity.getString(xfkj.ads.cn.R.string.ads_interstitial_placement_cn);
        }
        // 直接复用带位ID逻辑，但避免二次 compareAndSet：复制内部发起逻辑
        try {
            final AtomicReference<YdInterstitial> interRef = new AtomicReference<>();
            YdInterstitial inter = new YdInterstitial.Builder(activity)
                    .setKey(placementId)
                    .setInterstitialListener(new AdViewInterstitialListener() {
                        @Override public void onAdReady() {
                            try {
                                YdInterstitial i = interRef.get();
                                if (i != null) {
                                    i.show(activity);
                                    if (onShown != null) onShown.run();
                                }
                            } catch (Throwable t) {
                                Log.e(TAG, "interstitial show failed", t);
                            }
                        }
                        @Override public void onAdDisplay() { Log.d(TAG, "interstitial:onAdDisplay"); }
                        @Override public void onAdClick(String url) { Log.d(TAG, "interstitial:onAdClick url=" + url); }
                        @Override public void onAdClosed() {
                            Log.d(TAG, "interstitial:onAdClosed");
                            try { YdInterstitial i = interRef.getAndSet(null); if (i != null) i.destroy(); } catch (Throwable ignore) {}
                        }
                        @Override public void onAdFailed(YdError error) {
                            Log.e(TAG, "interstitial:onAdFailed error=" + String.valueOf(error));
                            try { YdInterstitial i = interRef.getAndSet(null); if (i != null) i.destroy(); } catch (Throwable ignore) {}
                        }
                    })
                    .build();
            interRef.set(inter);
            inter.requestInterstitial();
        } catch (Throwable t) {
            Log.e(TAG, "loadInterstitial(no-id): exception", t);
            if (onShown != null) onShown.run();
        }
    }

    @Override public void loadReward(Activity activity, String placementId, Runnable onReward) {
        Log.d(TAG, "loadReward: placementId=" + placementId);
        if (onReward != null) onReward.run();
    }

    @Override public void showSplash(Activity activity, Runnable onFinished) {
        Log.i(TAG, "showSplash: start");
        if (!adsEnabled) { Log.d(TAG, "showSplash: disabled"); if (onFinished != null) onFinished.run(); return; }
        // 根容器（可能为空）
        FrameLayout root = activity.findViewById(android.R.id.content);
        if (root == null) {
            Log.w(TAG, "showSplash: root content is null, creating fallback root container");
            root = new FrameLayout(activity);
            FrameLayout.LayoutParams rootLp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            );
            activity.getWindow().addContentView(root, rootLp);
        }
        // 用于承载开屏广告的容器
        FrameLayout adContainer = new FrameLayout(activity);
        FrameLayout.LayoutParams adLp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        root.addView(adContainer, adLp);
        Log.d(TAG, "showSplash: ad container attached");

        String advId = activity.getString(xfkj.ads.cn.R.string.ads_splash_placement_cn);
        Log.d(TAG, "showSplash: using appId=" + advId);

        FrameLayout finalRoot = root;
        YdSpread ydSpread = new YdSpread.Builder(activity)
                .setKey(advId)
                .setSpreadLoadListener(new SpreadLoadListener() {
                    @Override
                    public void onADLoaded(final SpreadAd spreadAd) {
                        Log.i(TAG, "showSplash:onADLoaded");
                        spreadAd.show(adContainer);
                    }
                })
                .setSpreadListener(new AdViewSpreadListener() {
                    @Override public void onAdDisplay() { Log.i(TAG, "showSplash:onAdDisplay"); }
                    @Override public void onAdClose() {
                        Log.i(TAG, "showSplash:onAdClose");
                        try { finalRoot.removeView(adContainer); Log.d(TAG, "showSplash: ad container removed"); } catch (Throwable t) { Log.w(TAG, "showSplash: remove ad container failed", t); }
                        if (onFinished != null) onFinished.run();
                    }
                    @Override public void onAdClick(String url) { Log.i(TAG, "showSplash:onAdClick url=" + url); }
                    @Override public void onAdFailed(YdError error) {
                        Log.e(TAG, "showSplash:onAdFailed error=" + String.valueOf(error));
                        try { finalRoot.removeView(adContainer); Log.d(TAG, "showSplash: ad container removed on fail"); } catch (Throwable t) { Log.w(TAG, "showSplash: remove ad container failed", t); }
                        if (onFinished != null) onFinished.run();
                    }
                })
                .build();
        Log.d(TAG, "showSplash: requestSpread()");
        ydSpread.requestSpread();
    }
}

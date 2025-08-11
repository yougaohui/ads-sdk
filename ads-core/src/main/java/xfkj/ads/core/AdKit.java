package xfkj.ads.core;

import android.app.Activity;
import android.content.Context;

public final class AdKit {
    private static AdManager cached;
    private AdKit() {}

    public static synchronized AdManager get(Context context) {
        if (cached != null) return cached;
        String[] impls = new String[]{
                "xfkj.ads.cn.CnAdManager",
                "xfkj.ads.global.GlobalAdManager",
                "xfkj.ads.noop.NoOpAdManager"
        };
        for (String cls : impls) {
            try {
                Class<?> c = Class.forName(cls);
                Object o = c.newInstance();
                if (o instanceof AdManager) {
                    cached = (AdManager) o;
                    cached.initialize(context.getApplicationContext());
                    return cached;
                }
            } catch (Throwable ignore) {}
        }
        // 兜底
        cached = new AdManager() {
            @Override public void initialize(Context context) {}
            private boolean enabled = true;
            @Override public void setAdsEnabled(boolean e) { enabled = e; }
            @Override public boolean isAdsEnabled() { return enabled; }
            @Override public void attachBanner(Activity activity, android.view.ViewGroup container) { /* no-op */ }
            @Override public void attachBanner(Activity activity, android.view.ViewGroup container, String placementId) { /* no-op */ }
            @Override public android.view.View createBanner(Activity activity, String placementId) { return null; }
            @Override public void loadInterstitial(Activity activity, String placementId, Runnable onShown) { if (onShown != null) onShown.run(); }
            @Override public void loadInterstitial(Activity activity, Runnable onShown) { if (onShown != null) onShown.run(); }
            @Override public void loadReward(Activity activity, String placementId, Runnable onReward) { if (onReward != null) onReward.run(); }
            @Override public void showSplash(Activity activity, Runnable onFinished) { if (onFinished != null) onFinished.run(); }
        };
        return cached;
    }
}

package xfkj.ads.global;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import xfkj.ads.core.AdManager;

public class GlobalAdManager implements AdManager {
    private volatile boolean enabled = true;
    @Override public void initialize(Context context) { /* TODO: 初始化海外广告SDK */ }
    @Override public void setAdsEnabled(boolean enabled) { this.enabled = enabled; }
    @Override public boolean isAdsEnabled() { return enabled; }
    @Override public void attachBanner(Activity activity, ViewGroup container) {
        if (!enabled) return;
        attachBanner(activity, container, "global_placeholder");
    }
    @Override public void attachBanner(Activity activity, ViewGroup container, String placementId) {
        if (container == null) return;
        if (!enabled) return;
        container.removeAllViews();
        container.addView(new FrameLayout(activity), new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        ));
    }
    @Override public View createBanner(Activity activity, String placementId) { return new FrameLayout(activity); }
    @Override public void loadInterstitial(Activity activity, String placementId, Runnable onShown) { if (!enabled) { if (onShown != null) onShown.run(); return; } if (onShown != null) onShown.run(); }
    @Override public void loadInterstitial(Activity activity, Runnable onShown) { if (!enabled) { if (onShown != null) onShown.run(); return; } if (onShown != null) onShown.run(); }
    @Override public void loadReward(Activity activity, String placementId, Runnable onReward) { if (!enabled) { if (onReward != null) onReward.run(); return; } if (onReward != null) onReward.run(); }
    @Override public void showSplash(Activity activity, Runnable onFinished) { if (!enabled) { if (onFinished != null) onFinished.run(); return; } if (onFinished != null) onFinished.run(); }
}

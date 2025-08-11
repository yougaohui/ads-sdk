package xfkj.ads.noop;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import xfkj.ads.core.AdManager;

public class NoOpAdManager implements AdManager {
    private boolean enabled = true;
    @Override public void initialize(Context context) { }
    @Override public void setAdsEnabled(boolean enabled) { this.enabled = enabled; }
    @Override public boolean isAdsEnabled() { return enabled; }
    @Override public void attachBanner(Activity activity, ViewGroup container) { /* no-op */ }
    @Override public void attachBanner(Activity activity, ViewGroup container, String placementId) { /* no-op */ }
    @Override public View createBanner(Activity activity, String placementId) { return null; }
    @Override public void loadInterstitial(Activity activity, String placementId, Runnable onShown) { if (onShown != null) onShown.run(); }
    @Override public void loadInterstitial(Activity activity, Runnable onShown) { if (onShown != null) onShown.run(); }
    @Override public void loadReward(Activity activity, String placementId, Runnable onReward) { if (onReward != null) onReward.run(); }
    @Override public void showSplash(Activity activity, Runnable onFinished) { if (onFinished != null) onFinished.run(); }
}

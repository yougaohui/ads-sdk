package xfkj.ads.core;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public interface AdManager {
    void initialize(Context context);

    // 由宿主控制：是否允许展示广告（默认允许）
    void setAdsEnabled(boolean enabled);
    boolean isAdsEnabled();
    /**
     * 默认挂载首页信息流/横幅广告到容器，无需传入位ID。
     */
    void attachBanner(Activity activity, ViewGroup container);
    /**
     * 将横幅/信息流广告挂载到指定容器中，SDK内部负责请求与生命周期。
     */
    void attachBanner(Activity activity, ViewGroup container, String placementId);
    View createBanner(Activity activity, String placementId);
    void loadInterstitial(Activity activity, String placementId, Runnable onShown);
    /**
     * 无需传入位ID的插屏展示，由实现内部获取默认位。
     */
    void loadInterstitial(Activity activity, Runnable onShown);
    void loadReward(Activity activity, String placementId, Runnable onReward);
    void showSplash(Activity activity, Runnable onFinished);
}

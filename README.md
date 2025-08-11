# Android 广告SDK

一个支持国内和全球广告平台的Android广告SDK，提供统一的广告接口和灵活的模块化架构。

## 特性

- 🚀 **模块化设计**: 支持按需引入广告模块
- 🌍 **多地区支持**: 支持国内和全球广告平台
- 🔧 **易于集成**: 简单的API接口，快速集成
- 📱 **轻量级**: 核心模块体积小，性能优秀
- 🎯 **灵活配置**: 支持运行时动态切换广告策略

## 模块说明

### ads-core
核心模块，提供广告SDK的基础接口和抽象类。

### ads-cn
国内广告模块，集成国内主流广告平台SDK。

### ads-global
全球广告模块，集成Google AdMob等全球广告平台。

### ads-noop
无广告模块，提供空实现，用于不需要广告的应用。

## 安装

### 方式1: GitHub Packages (推荐)

在项目根目录的 `settings.gradle` 中添加：

```gradle
dependencyResolutionManagement {
    repositories {
        // ... 其他仓库
        maven {
            url = uri("https://maven.pkg.github.com/YOUR_USERNAME/ads-sdk")
            credentials {
                username = project.findProperty("gpr.user") ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") ?: System.getenv("TOKEN")
            }
        }
    }
}
```

在 `build.gradle` 中添加依赖：

```gradle
dependencies {
    // 核心模块
    implementation 'com.github.yourusername:ads-core:1.0.0'
    
    // 按需选择广告模块
    implementation 'com.github.yourusername:ads-cn:1.0.0'      // 国内广告
    implementation 'com.github.yourusername:ads-global:1.0.0'  // 全球广告
    implementation 'com.github.yourusername:ads-noop:1.0.0'    // 无广告
}
```

### 方式2: JitPack

在项目根目录的 `build.gradle` 中添加：

```gradle
allprojects {
    repositories {
        // ... 其他仓库
        maven { url 'https://jitpack.io' }
    }
}
```

在 `build.gradle` 中添加依赖：

```gradle
dependencies {
    implementation 'com.github.yourusername:ads-sdk:1.0.0'
}
```

## 使用方法

### 1. 初始化SDK

```java
// 在Application中初始化
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // 初始化广告SDK
        AdsManager.init(this);
    }
}
```

### 2. 配置广告策略

```java
// 根据地区或配置选择广告模块
if (isChineseRegion()) {
    AdsManager.setAdsModule(new CNAdsModule());
} else {
    AdsManager.setAdsModule(new GlobalAdsModule());
}
```

### 3. 显示广告

```java
// 显示横幅广告
AdsManager.showBannerAd(adContainer);

// 显示插屏广告
AdsManager.showInterstitialAd();

// 显示激励视频广告
AdsManager.showRewardedVideoAd(new RewardedVideoAdListener() {
    @Override
    public void onRewarded() {
        // 用户获得奖励
    }
});
```

## 配置说明

### 国内广告配置

在 `ads-cn` 模块中添加具体的广告SDK依赖：

```gradle
dependencies {
    // 腾讯广告
    implementation 'com.qq.e.union:union:4.x.x'
    
    // 字节跳动广告
    implementation 'com.pangle.cn:ads-sdk:4.x.x'
    
    // 快手广告
    implementation 'com.kwai.sdk:ksad-sdk:3.x.x'
}
```

### 全球广告配置

在 `ads-global` 模块中添加Google AdMob等依赖：

```gradle
dependencies {
    // Google AdMob
    implementation 'com.google.android.gms:play-services-ads:22.x.x'
    
    // Facebook Audience Network
    implementation 'com.facebook.android:audience-network-sdk:6.x.x'
}
```

## 发布新版本

1. 更新版本号：
   ```bash
   # 在gradle.properties中修改VERSION_NAME
   VERSION_NAME=1.0.1
   ```

2. 创建Git标签：
   ```bash
   git tag v1.0.1
   git push origin v1.0.1
   ```

3. GitHub Actions会自动构建并发布到GitHub Packages

## 许可证

Apache License 2.0

## 贡献

欢迎提交Issue和Pull Request！

## 联系方式

- 邮箱: your.email@example.com
- GitHub: [@yourusername](https://github.com/yourusername)

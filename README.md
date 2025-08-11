# Android 广告SDK

[![JitPack](https://jitpack.io/v/yougaohui/ads-sdk.svg)](https://jitpack.io/#yougaohui/ads-sdk)

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

## 构建和开发

### 环境要求
- JDK 8 或更高版本
- Android Gradle Plugin 8.1.0+
- Gradle 8.0+

### 本地构建
1. 克隆项目到本地
2. 使用 Gradle Wrapper 构建：
   ```bash
   # Linux/macOS
   ./gradlew clean build
   
   # Windows
   gradlew.bat clean build
   ```

3. 发布到本地Maven仓库：
   ```bash
   # Linux/macOS
   ./gradlew publishToMavenLocal
   
   # Windows
   gradlew.bat publishToMavenLocal
   ```

### 快速测试构建
Windows 用户可以直接运行 `build-test.bat` 脚本来测试完整构建流程。

## 安装

### 方式1: GitHub Packages (推荐)

在项目根目录的 `settings.gradle` 中添加：

```gradle
dependencyResolutionManagement {
    repositories {
        // ... 其他仓库
        maven {
            url = uri("https://maven.pkg.github.com/yougaohui/ads-sdk")
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
    implementation 'com.github.yougaohui:ads-core:1.0.0'
    
    // 按需选择广告模块
    implementation 'com.github.yougaohui:ads-cn:1.0.0'      // 国内广告
    implementation 'com.github.yougaohui:ads-global:1.0.0'  // 全球广告
    implementation 'com.github.yougaohui:ads-noop:1.0.0'    // 无广告
}
```

### 方式2: JitPack (推荐)

JitPack可以直接从GitHub仓库构建您的库，无需额外配置。

#### 在项目根目录的 `settings.gradle` 中添加：

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

#### 在 `build.gradle` 中添加依赖：

```gradle
dependencies {
    // 使用GitHub标签版本
    implementation 'com.github.yougaohui:ads-sdk:1.0.2'
    
    // 或者使用分支版本
    // implementation 'com.github.yougaohui:ads-sdk:main-SNAPSHOT'
    
    // 或者使用特定提交
    // implementation 'com.github.yougaohui:ads-sdk:commit-hash'
}
```

#### JitPack优势：
- ✅ **自动构建**: 无需手动发布，Git标签自动触发构建
- ✅ **版本管理**: 支持标签、分支、提交等多种版本引用
- ✅ **全球CDN**: 快速下载，全球可用
- ✅ **免费使用**: 开源项目完全免费

## 故障排除

### 常见构建错误

#### 1. dependencyResolutionManagement 方法找不到
**错误信息**: `Could not find method dependencyResolutionManagement()`

**解决方案**: 
- 确保使用 Gradle 7.0+ 版本
- 或者移除 `settings.gradle` 中的 `dependencyResolutionManagement` 配置（已自动修复）

#### 2. maven-publish 插件未找到
**错误信息**: `Gradle 'publishToMavenLocal' task not found`

**解决方案**: 
- 确保在根项目的 `build.gradle` 中应用了 `maven-publish` 插件
- 检查子项目是否正确配置了发布任务

#### 3. Gradle 版本兼容性问题
**解决方案**: 
- 使用项目提供的 Gradle Wrapper：`./gradlew` 或 `gradlew.bat`
- 确保 JDK 版本兼容（推荐 JDK 8 或更高）

### 获取帮助
如果遇到其他问题，请：
1. 检查 [Issues](https://github.com/yougaohui/ads-sdk/issues) 页面
2. 提交新的 Issue 并附上详细的错误信息
3. 确保包含操作系统、JDK版本、Gradle版本等信息

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

### 方式1: JitPack (推荐)

JitPack发布非常简单，只需要：

1. **创建Git标签**：
   ```bash
   git tag v1.0.3
   git push origin v1.0.3
   ```

2. **自动构建**: JitPack会自动检测标签并构建库
3. **立即可用**: 构建完成后，其他项目即可使用新版本

### 方式2: GitHub Packages

1. **更新版本号**：
   ```bash
   # 在gradle.properties中修改VERSION_NAME
   VERSION_NAME=1.0.3
   ```

2. **创建Git标签**：
   ```bash
   git tag v1.0.3
   git push origin v1.0.3
   ```

3. **GitHub Actions会自动构建并发布到GitHub Packages**

### 版本引用方式

```gradle
dependencies {
    // 使用最新标签版本
    implementation 'com.github.yougaohui:ads-sdk:1.0.2'
    
    // 使用分支最新版本
    implementation 'com.github.yougaohui:ads-sdk:main-SNAPSHOT'
    
    // 使用特定提交
    implementation 'com.github.yougaohui:ads-sdk:7eba3d6'
}
```

## 许可证

Apache License 2.0

## 贡献

欢迎提交Issue和Pull Request！

## 联系方式

- 邮箱: your.email@example.com
- GitHub: [@yougaohui](https://github.com/yougaohui)
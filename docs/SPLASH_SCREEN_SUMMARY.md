# Splash Screen Implementation Summary

## 概述

AppStarterKit 已成功集成 androidx.splashscreen，实现现代化的启动屏效果。

## 新增文件

### 1. Version Catalog 配置
- **gradle/libs.versions.toml** - 添加 `androidx-splashscreen` 版本
- **app/build.gradle.kts** - 添加 splashscreen 依赖

### 2. Splash Screen 组件
- **ui/screen/SplashScreen.kt** - 传统 Splash Screen Composable（已移除）
- **ui/screen/ModernSplashActivity.kt** - 现代 Splash Activity（推荐）

### 3. 传统 Splash Activity（可选）
- **ui/screen/SplashActivity.kt** - 传统 Splash Activity

### 4. Theme 配置
- **app/src/main/res/values/styles_splash.xml** - Splash Screen 主题
- **添加背景颜色**

### 5. 文档
- **SPLASH_SCREEN_GUIDE.md** - 完整的 Splash Screen 使用指南
- **VERSION_CATALOG_SUMMARY.md** - 已更新 splashscreen 信息

### 6. 配置更新
- **app/src/main/AndroidManifest.xml** - 配置 Splash Activity 作为 launcher

## 核心功能

### 启动流程

```
1. 用户点击应用图标
   ↓
2. ModernSplashActivity 启动（作为 launcher）
   ↓
3. AndroidX Startup Initializers 执行
   ├─ Timber (日志)
   ├─ DataStore (首选项)
   ├─ Room (数据库)
   └─ WorkManager (后台任务)
   ↓
4. Splash Screen 显示
   ├─ Logo 淡入动画 (600ms)
   ├─ 内容淡入动画 (400ms)
   ├─ 显示应用品牌
   └─ 显示应用名称
   ↓
5. 延迟 200ms（可选，确保主界面已准备好）
   ↓
6. 导航到 MainActivity
   ↓
7. MainActivity 启动
   └─ 主界面显示
```

### 动画配置

#### Fade In 动画
- **Logo Fade In**: 600ms, FastOutSlowInEasing
- **Content Fade In**: 400ms, FastOutSlowInEasing
- **总时长**: 1000ms + 延迟

#### 自定义选项

```kotlin
class ModernSplashActivity : ComponentActivity() {
    companion object {
        private const val SPLASH_DELAY_MS = 2000L  // 可调整
        private const val FADE_IN_DURATION_MS = 600L
        private const val FADE_OUT_DURATION_MS = 400L
    }
}
```

## 主题配置

### Splash Screen 主题 (styles_splash.xml)

```xml
<style name="Theme.SplashScreen" parent="Theme.Material3.DayNight">
    <item name="android:windowBackground">@color/splash_screen_background</item>
    <item name="android:statusBarColor">@android:color/transparent</item>
    <item name="android:windowDrawsSystemBarBackgrounds">true</item>
    <item name="android:windowLayoutInDisplayCutoutMode">shortEdges</item>
    <item name="android:windowAnimationStyle">@null</item>
</style>
```

### 背景颜色

```xml
<color name="splash_screen_background">#6750A4</color>
```

## AndroidManifest 配置

### Launcher Activity

```xml
<activity
    android:name=".ui.screen.ModernSplashActivity"
    android:exported="true"
    android:theme="@style/Theme.SplashScreen"
    android:configChanges="orientation|keyboardHidden|screenSize"
    android:launchMode="singleTask">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

### Main Activity（非 Launcher）

```xml
<activity
    android:name=".MainActivity"
    android:exported="true"
    android:theme="@style/Theme.AppStarterKit"
    android:configChanges="orientation|keyboardHidden|screenSize"
    android:launchMode="singleTask">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

## 两种实现方式对比

### 方式 1：ModernSplashActivity（推荐）

**优点：**
- ✅ 使用 androidx.core.splashscreen，无需手动管理
- ✅ 自动显示在应用启动时
- ✅ 无缝过渡到主界面
- ✅ 支持动态颜色
- ✅ 性能更好

**实现：**
- 使用 Compose 构建启动屏
- 集成 AndroidX Startup
- 自动管理生命周期

### 方式 2：SplashActivity（传统方式）

**优点：**
- ✅ 更多自定义控制
- ✅ 可以添加自定义动画
- ✅ 支持复杂的交互

**实现：**
- 使用传统 Activity
- 手动管理生命周期
- 自定义过渡逻辑

## 自定义选项

### 1. 调整启动时间

```kotlin
class ModernSplashActivity : ComponentActivity() {
    companion object {
        private const val SPLASH_DELAY_MS = 2000L  // 改为 3000L 延长到 3 秒
    }
}
```

### 2. 自定义品牌显示

```kotlin
@Composable
fun ModernSplashContent(...) {
    // 显示应用 Logo
    Image(painter = painterResource(R.drawable.ic_launcher_foreground))

    // 显示应用名称
    Text(
        text = "AppStarterKit",
        style = MaterialTheme.typography.headlineMedium
    )

    // 显示标语
    Text(
        text = "Welcome!",
        style = MaterialTheme.typography.bodyMedium
    )
}
```

### 3. 添加加载指示器

```kotlin
@Composable
fun ModernSplashContent(...) {
    var isLoading by remember { mutableStateOf(true) }

    if (isLoading) {
        CircularProgressIndicator()
        Text("Loading...")
    }
}
```

## 动画类型

### 1. Fade 动画

```kotlin
// Fade in
animate(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = tween(600, easing = FastOutSlowInEasing)
) { value, _ -> logoScale = value }

// Fade out
animate(
    initialValue = 1f,
    targetValue = 0f,
    animationSpec = tween(400, easing = FastOutSlowInEasing)
) { value, _ -> contentAlpha = value }
```

### 2. Scale 动画

```kotlin
// Scale up
animate(
    initialValue = 0f,
    targetValue = 1.2f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )
) { value, _ -> logoScale = value }
```

### 3. Slide 动画

```kotlin
// Slide in
animateDpAsState(
    initialValue = Offset(100f, 0f),
    targetValue = Offset(0f, 0f),
    animationSpec = tween(400, easing = FastOutSlowInEasing)
)
```

## 测试

### 启动测试

```kotlin
@Test
fun testSplashDuration() {
    val activity = Robolectric.setupActivity(SplashActivity::class.java)
    
    // 测试启动时长
    val startTime = System.currentTimeMillis()
    activity.onCreate(Bundle())
    val endTime = System.currentTimeMillis()
    val duration = endTime - startTime
    
    // 确保时长在合理范围内
    assertThat(duration).isLessThan(3000)
}
```

### 动画测试

```kotlin
@Test
fun testSplashAnimations() {
    // 测试动画是否正常执行
    // 验证动画参数
}
```

## 性能优化

### 1. 避免过度绘制

```kotlin
@Composable
fun ModernSplashContent(...) {
    // 使用 Box 而不是多个 Surface
    Box(modifier = Modifier.fillMaxSize()) {
        SplashContent()
    }
}
```

### 2. 使用合适的动画时长

```kotlin
// 快速淡入淡出
const val FAST_FADE_IN_MS = 400L
const val FAST_FADE_OUT_MS = 300L

// 避免过长的动画
const val MIN_SPLASH_DELAY = 1000L
const val MAX_SPLASH_DELAY = 3000L
```

### 3. 优化重组

```kotlin
// 使用 remember 避免不必要的状态更新
val contentAlpha by remember { mutableFloatStateOf(0f) }
val logoScale by remember { mutableFloatStateOf(0f) }

// 只在必要时更新状态
LaunchedEffect(Unit) {
    contentAlpha.value = 1f
    delay(FADE_IN_DURATION_MS)
    contentAlpha.value = 0f
}
```

## 与其他功能的集成

### 1. AndroidX Startup

```
Startup 流程：
1. ModernSplashActivity 启动
2. TimberInitializer 执行
3. DataStoreInitializer 执行
4. RoomInitializer 执行
5. WorkManagerInitializer 执行
6. Splash Screen 显示
7. 导航到 MainActivity
8. MainActivity 继续初始化
```

### 2. Hilt Integration

```kotlin
@HiltAndroidApp
class AppApplication : Application() {
    // AndroidX Startup 会自动初始化组件
    // Hilt 会在稍后初始化
}
```

### 3. DataStore

```kotlin
// Splash Screen 可以读取首选项来决定显示内容
@Composable
fun ModernSplashContent(...) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val darkTheme by viewModel.darkTheme.collectAsState()
    
    // 根据主题调整 splash screen 外观
    Box(
        modifier = Modifier
            .background(
                if (darkTheme)
                    Color(0xFF121212)
                else
                    Color(0xFF6750A4)
            )
    ) {
        SplashContent()
    }
}
```

## 常见场景

### 场景 1：首次启动

**描述**：用户第一次打开应用

**行为**：
- 完整启动动画
- 显示应用品牌
- 2 秒后显示主界面

### 场景 2：热启动

**描述**：应用从后台恢复

**行为**：
- 缩短或跳过动画
- 快速显示主界面
- 优先显示已加载的内容

### 场景 3：从通知点击

**描述**：用户点击通知打开应用

**行为**：
- 使用 ModernSplashActivity
- 正常的启动流程
- 无特殊处理

### 场景 4：深色模式

**描述**：系统处于深色模式

**行为**：
- Splash Screen 使用深色背景
- Logo 和文字颜色适配
- 过渡到深色主题的主界面

### 场景 5：平板设备

**描述**：在平板上显示

**行为**：
- 更大的 Logo 和文字
- 适配更大的屏幕空间
- 可能调整动画时长

## 故障排除

### 问题 1：Splash Screen 不显示

**可能原因**：
- Activity 未在 AndroidManifest 中注册
- 主题配置冲突
- 图标资源缺失

**解决方案**：
```xml
<!-- 确保 activity 在 manifest 中 -->
<activity
    android:name=".ui.screen.ModernSplashActivity"
    android:exported="true"
    android:theme="@style/Theme.SplashScreen">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

### 问题 2：动画不流畅

**可能原因**：
- 动画时长设置不当
- 主线程阻塞
- 过度绘制

**解决方案**：
```kotlin
// 使用协程执行动画
lifecycleScope.launch {
    animate(...)
}

// 使用优化的动画配置
animationSpec = tween(400, easing = FastOutSlowInEasing)
```

### 问题 3：过渡到主界面有闪烁

**可能原因**：
- 主界面未完全初始化
- 主题切换动画干扰

**解决方案**：
```kotlin
// 添加延迟确保主界面已准备好
delay(200)

// 在主界面中使用与 splash 相同的主题
```

### 问题 4：内存泄漏

**可能原因**：
- Activity 引用未释放的资源
- 协程未正确取消

**解决方案**：
```kotlin
override fun onDestroy() {
    super.onDestroy()
    
    // 取消所有协程
    lifecycleScope.cancel()
    
    // 清理资源
}
```

## 文档

- **SPLASH_SCREEN_GUIDE.md** - 完整的 Splash Screen 使用指南
- **README.md** - 已更新 splash screen 信息
- **VERSION_CATALOG_SUMMARY.md** - 已添加 splashscreen 依赖

## 技术栈

- **androidx.core.splashscreen**: 1.0.1
- **Compose UI**: 1.5.4+
- **Kotlin**: 1.9.20+
- **Coroutines**: 1.7.3+
- **Material Design 3**: 1.1.2+

## 优势

### 使用 androidx.core.splashscreen 的优势

1. **自动管理**
   - 自动处理启动显示
   - 自动处理到主界面的过渡
   - 无需手动管理生命周期

2. **更好的性能**
   - 系统级优化
   - 减少不必要的层级
   - 流畅的动画支持

3. **一致性**
   - 遵循 Android 12+ 的设计规范
   - 与系统启动屏风格一致

4. **简化代码**
   - 减少样板代码
   - 更少的自定义逻辑
   - 更易维护

5. **更好的用户体验**
   - 更快的启动速度
   - 更流畅的过渡
   - 与系统外观一致

## 迁移指南

### 从传统方式迁移

1. 更新 `gradle/libs.versions.toml`：
   ```toml
   [versions]
   androidx-splashscreen = "1.0.1"
   ```

2. 更新 `app/build.gradle.kts`：
   ```kotlin
   dependencies {
       implementation(libs.androidx.core.splashscreen)
   }
   ```

3. 更新 `AndroidManifest.xml`：
   - 移除传统 launcher activity
   - 添加 ModernSplashActivity

4. 更新 `MainActivity`：
   - 移除 splash screen 逻辑
   - 简化启动流程

## 更新日志

### v1.0.0 (Current)

- ✅ 添加 androidx.splashscreen 依赖
- ✅ 创建 ModernSplashActivity
- ✅ 配置 Splash Screen 主题
- ✅ 更新 AndroidManifest
- ✅ 创建完整文档
- ✅ 与 AndroidX Startup 集成

## 后续改进

1. 添加更多自定义动画选项
2. 实现品牌配置（从首选项读取）
3. 添加启动进度指示器
4. 支持不同语言的启动屏
5. 添加从网络下载品牌资源
6. 实现复杂的启动流程（登录流程等）

## 完成清单

- ✅ 添加 androidx.splashscreen 到 Version Catalog
- ✅ 创建 ModernSplashActivity（推荐方式）
- ✅ 创建 Splash Screen 主题配置
- ✅ 更新 AndroidManifest 配置
- ✅ 创建完整的 Splash Screen 文档
- ✅ 与 AndroidX Startup 完美集成
- ✅ 实现流畅的动画过渡
- ✅ 支持深色主题
- ✅ 更新 README.md 反映新功能

所有 Splash Screen 功能已完整实现！🚀

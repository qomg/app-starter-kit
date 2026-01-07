# Splash Screen Guide

AppStarterKit 使用 androidx.splashscreen 实现现代化的启动屏效果。

## 概述

Splash Screen 是应用启动时展示的第一个界面，用于：
- 展示应用品牌和 logo
- 背载应用初始化（通过 AndroidX Startup）
- 提供流畅的过渡动画
- 改善用户体验

## 实现方案

### 方案 1：使用 androidx.core.splashscreen（推荐）

**优势**：
- 自动显示在应用启动时
- 无需单独的 launcher activity
- 更好的性能
- 支持动态颜色适配
- 无缝过渡到主界面

### 方案 2：使用自定义主题（传统方式）

**优势**：
- 更多自定义控制
- 支持自定义动画
- 兼容性好

## 文件结构

```
app/src/main/
├── java/com/example/appstarterkit/
│   ├── ui/screen/
│   │   ├── SplashScreen.kt                    # 传统 Splash Screen Composable
│   │   ├── ComposeSplashActivity.kt            # Compose Splash Activity
│   │   └── ModernSplashActivity.kt          # 现代 Splash Activity（推荐）
│   ├── MainActivity.kt                        # 主 Activity
│   └── AppApplication.kt                     # Application 类
└── res/values/
    └── styles_splash.xml                         # Splash Screen 主题
```

## 使用 androidx.core.splashscreen

### 1. 添加依赖

```kotlin
// gradle/libs.versions.toml
[versions]
androidx-splashscreen = "1.0.1"

// app/build.gradle.kts
dependencies {
    implementation(libs.androidx.core.splashscreen)
}
```

### 2. ModernSplashActivity 实现

```kotlin
class ModernSplashActivity : ComponentActivity() {

    companion object {
        private const val SPLASH_DELAY_MS = 2000L
        private const val FADE_IN_DURATION_MS = 600L
        private const val FADE_OUT_DURATION_MS = 400L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppStarterKitTheme {
                ModernSplashContent(...)
            }
        }

        // Animate and navigate to main activity
        lifecycleScope.launch {
            // Logo fade in
            animate(0f, 1f, FADE_IN_DURATION_MS)
            
            // Content fade in
            animate(0f, 1f, FADE_IN_DURATION_MS)
            
            // Wait for splash duration
            delay(SPLASH_DELAY_MS)
            
            // Navigate to main
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
```

### 3. AndroidManifest 配置

```xml
<activity
    android:name=".ui.screen.ModernSplashActivity"
    android:exported="true"
    android:theme="@style/Theme.SplashScreen"
    android:launchMode="singleTask">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

## 动画效果

### Fade In 动画
- Logo 从 0.0 缩放到 1.0
- 内容从透明度 0.0 渐变到 1.0
- 使用 FastOutSlowInEasing
- 持续时间：600ms

### 过渡到主界面
- 等待 2 秒
- 自动启动 MainActivity
- finish SplashActivity
- 无黑屏闪烁

## 自定义配置

### 调整启动时间

```kotlin
class ModernSplashActivity : ComponentActivity() {
    companion object {
        private const val SPLASH_DELAY_MS = 2000L  // 修改这里
    }
}
```

### 自定义品牌

```kotlin
@Composable
fun ModernSplashContent(...) {
    Column {
        // App Logo
        Image(painter = painterResource(R.drawable.ic_launcher_foreground))
        
        // App Name
        Text(
            text = "AppStarterKit",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
```

### 添加自定义动画

```kotlin
// Scale animation
animate(
    initialValue = 0f,
    targetValue = 1.2f,
    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
)

// Rotation animation
rotate(
    from = 0f,
    to = 360f,
    animationSpec = infiniteRepeatable(
        animation = tween(1000),
        repeatMode = RepeatMode.Restart
    )
)
```

## AndroidX Startup 集成

### 启动流程

```
1. 用户点击应用图标
   ↓
2. Android 系统启动应用
   ↓
3. Splash Screen Activity 启动（使用 core-splashscreen）
   ↓
4. AndroidX Startup 初始化器执行
   ├─ Timber (日志)
   ├─ DataStore (首选项）
   ├─ Room (数据库)
   └─ WorkManager (后台任务)
   ↓
5. Splash Screen 动画显示
   ├─ Logo fade in
   ├─ 内容 fade in
   ├─ 显示应用品牌
   └─ 加载指示器（可选）
   ↓
6. Splash 延迟结束（2秒）
   ↓
7. 导航到 MainActivity
   ↓
8. MainActivity 启动
   ↓
9. 主界面显示
```

## 性能优化

### 1. 延迟初始化

```kotlin
// 不立即导航到 MainActivity
lifecycleScope.launch {
    delay(2000)
    navigateToMain()
}
```

### 2. 避免过度绘制

```kotlin
@Composable
fun ModernSplashContent(...) {
    // 使用 Box 而不是多个 Surface
    Box(modifier = Modifier.fillMaxSize()) {
        // Splash content
    }
}
```

### 3. 使用适当的状态持有

```kotlin
// 使用 remember 保存状态
val contentAlpha by remember { mutableFloatStateOf(0f) }

// 使用 derivedStateOf 计算衍生状态
val logoScale by derivedStateOf(contentAlpha) {
    contentAlpha.coerceIn(0f, 1f)
}
```

## 测试

### 测试不同设备

```kotlin
// 测试启动时间
@Test
fun testSplashDuration() {
    val startTime = System.currentTimeMillis()
    // ... 启动 splash screen
    val endTime = System.currentTimeMillis()
    val duration = endTime - startTime
    
    assertThat(duration).isLessThan(3000)
}
```

### 测试动画

```kotlin
// 测试动画流畅度
@Composable
@Preview
fun ModernSplashContentPreview() {
    AppStarterKitTheme {
        ModernSplashContent(
            contentAlpha = 1f,
            logoScale = 1f
        )
    }
}
```

## 故障排除

### 问题 1：Splash Screen 不显示

**可能原因**：
- 启动 activity 配置错误
- 主题配置冲突
- Activity 未在 AndroidManifest 中注册

**解决方案**：
```xml
<!-- 确保 activity 配置正确 -->
<activity
    android:name=".ui.screen.ModernSplashActivity"
    android:exported="true"
    android:theme="@style/Theme.SplashScreen"
    android:launchMode="singleTask">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

### 问题 2：过渡到主界面有闪烁

**可能原因**：
- Splash 和 Main Activity 主题不一致
- 过渡时间太短
- 主界面初始化慢

**解决方案**：
```kotlin
// 确保主题一致
// 使用相同的主题配置
// 延长 splash 时间，确保主界面已初始化
lifecycleScope.launch {
    delay(2000)  // 增加到 3 秒
    navigateToMain()
}
```

### 问题 3：动画卡顿

**可能原因**：
- 主线程执行耗时操作
- 动画配置不合理
- 设备性能限制

**解决方案**：
```kotlin
// 使用协程执行动画
lifecycleScope.launch {
    // 动画在主协程中执行
}

// 使用高效的状态管理
val contentAlpha by remember { mutableFloatStateOf(0f) }
```

## 最佳实践

### 1. 启动时间

- **快速应用启动**：2 秒或更短
- **加载指示器**：可选的进度指示器
- **品牌展示**：清晰的 logo 和应用名称
- **流畅过渡**：无闪烁、无卡顿

### 2. 动画设计

- **简洁动画**：简单的淡入淡出
- **适当时长**：不要太长也不要太短
- **流畅过渡**：使用推荐的 easing 函数
- **高性能**：避免过度绘制

### 3. 状态管理

- **使用 remember**：避免不必要的状态重建
- **适当的状态提升**：使用 rememberMutableStateOf
- **避免重组**：合理使用 derivedStateOf

### 4. 适配性

- **横竖屏支持**：configChanges="orientation"
- **不同屏幕尺寸**：适应不同设备
- **性能考虑**：低端设备也能流畅运行

## 技术栈

- **androidx.core.splashscreen**: 1.0.1
- **Compose UI**: 1.5.4+
- **Kotlin**: 1.9.20+
- **Coroutines**: 1.7.3
- **Material Design 3**: 1.1.2

## 参考

- [androidx.core.splashscreen Documentation](https://developer.android.com/reference/androidx/core/splashscreen)
- [Splash Screen API Guide](https://developer.android.com/guide/topics/ui/splash-screen)
- [Compose Animation Documentation](https://developer.android.com/jetpack/compose/animation)
- [Android Performance Patterns](https://developer.android.com/topic/performance)

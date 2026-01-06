# App Startup Guide

AppStarterKit 使用 AndroidX Startup 库来优化应用启动性能，实现组件的懒加载和顺序初始化。

## 目录

```
startup/
├── TimberInitializer.kt          # 日志初始化
├── WorkManagerInitializer.kt     # WorkManager 初始化
├── RoomInitializer.kt           # Room 数据库初始化
├── DataStoreInitializer.kt      # DataStore 初始化
└── AppStartupUtils.kt           # Startup 工具类

data/datastore/
└── PreferencesDataStore.kt       # 首选项管理
```

## AndroidX Startup 概述

### 什么是 Startup Library？

AndroidX Startup 提供了一个运行在 `Application.onCreate()` 之前的初始化机制，允许您：

1. **延迟初始化**：不需要立即使用的组件可以稍后初始化
2. **顺序控制**：定义组件初始化的依赖关系
3. **性能优化**：减少应用启动时间
4. **模块化**：每个模块可以定义自己的初始化器

### 启动流程

```
1. Android 安装 App
2. 启动 Startup Initializers
3. Application.onCreate()
4. Activity.onCreate()
5. UI 渲染
```

### 初始化顺序

```
TimberInitializer (日志)
    ↓
DataStoreInitializer (首选项)
    ↓
RoomInitializer (数据库)
    ↓
WorkManagerInitializer (后台任务)
```

## 初始化器详解

### 1. TimberInitializer

**职责**：初始化 Timber 日志库

```kotlin
class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context): Unit {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // Production tree
        }
        return Unit
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList() // 无依赖
    }
}
```

**特性**：

- 在 Debug 模式下使用 DebugTree
- 在 Release 模式下使用自定义 Tree（可添加 Crashlytics 等）
- 无依赖，首先初始化

### 2. DataStoreInitializer

**职责**：初始化 DataStore 用于存储首选项

```kotlin
class DataStoreInitializer : Initializer<PreferencesDataStore> {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

    override fun create(context: Context): PreferencesDataStore {
        return runBlocking(Dispatchers.IO) {
            PreferencesDataStore(context.dataStore)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
```

**特性**：

- 异步初始化（使用 Coroutines）
- 返回 DataStore 实例供其他组件使用
- 无依赖，与 Timber 并行初始化

### 3. RoomInitializer

**职责**：初始化 Room 数据库

```kotlin
class RoomInitializer : Initializer<AppDatabase> {
    override fun create(context: Context): AppDatabase {
        return runBlocking(Dispatchers.IO) {
            AppDatabase.getInstance(context)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(TimberInitializer::class.java)
    }
}
```

**特性**：

- 依赖 Timber（数据库操作需要日志）
- 异步初始化
- 返回数据库实例供 Repository 使用

### 4. WorkManagerInitializer

**职责**：初始化 WorkManager 并集成 Hilt

```kotlin
class WorkManagerInitializer : Initializer<WorkManager> {
    override fun create(context: Context): WorkManager {
        val config = Configuration.Builder()
            .setWorkerFactory(getWorkerFactory(context))
            .build()

        val workManager = WorkManager.getInstance(context)
        workManager.initialize(context, config)
        return workManager
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(TimberInitializer::class.java)
    }
}
```

**特性**：

- 依赖 Timber（WorkManager 需要日志）
- 配置 Hilt Worker Factory
- 支持 Hilt 注入的 Worker

## AndroidManifest.xml 配置

```xml
<application
    android:name=".AppApplication">
    
    <!-- Startup Initializer Provider -->
    <provider
        android:name="androidx.startup.InitializationProvider"
        android:authorities="${applicationId}.androidx-startup"
        android:exported="false"
        android:multiprocess="false">

        <!-- 定义所有初始化器 -->
        <meta-data
            android:name="androidx.startup"
            android:value="com.example.appstarterkit.startup.TimberInitializer" />
        
        <meta-data
            android:name="androidx.startup"
            android:value="com.example.appstarterkit.startup.DataStoreInitializer" />
        
        <meta-data
            android:name="androidx.startup"
            android:value="com.example.appstarterkit.startup.RoomInitializer" />
        
        <meta-data
            android:name="androidx.startup"
            android:value="com.example.appstarterkit.startup.WorkManagerInitializer" />
    </provider>
</application>
```

## DataStore 集成

### PreferencesDataStore

**职责**：管理应用首选项

```kotlin
@Singleton
class PreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

    // 主题设置
    val darkTheme: Flow<Boolean> = context.dataStore.data
        .map { it[PreferencesKeys.DARK_THEME] ?: false }
    
    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME] = isDark
        }
    }
}
```

### 使用示例

```kotlin
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val darkTheme by viewModel.darkTheme.collectAsState()
    
    Switch(
        checked = darkTheme,
        onCheckedChange = { viewModel.toggleDarkTheme() }
    )
}
```

## 懒加载（Lazy Initialization）

### LazyInitManager

用于管理不需要立即初始化的组件：

```kotlin
class LazyInitManager {
    fun <T> register(key: String, initializer: () -> T)
    fun <T> get(key: String): T?
    fun isInitialized(key: String): Boolean
}
```

### 使用场景

```kotlin
// 注册懒加载组件
lazyInitManager.register("analytics") {
    AnalyticsService.initialize(context)
}

// 使用时才初始化
if (shouldEnableAnalytics()) {
    val analytics = getLazy<AnalyticsService>("analytics")
    analytics.trackEvent("app_start")
}
```

## Startup 性能监控

### AppStartupConfig

跟踪初始化时间：

```kotlin
object AppStartupConfig {
    fun recordInitTime(name: String)
    fun getStartupTimings(): Map<String, Long>
    fun getTotalStartupTime(): Long
}
```

### 使用示例

```kotlin
class MyInitializer : Initializer<Unit> {
    override fun create(context: Context): Unit {
        // 记录初始化时间
        AppStartupConfig.recordInitTime("MyInitializer")
        
        // 执行初始化...
        
        return Unit
    }
}
```

## 创建自定义初始化器

### 步骤 1：创建类

```kotlin
class MyCustomInitializer : Initializer<MyService> {
    override fun create(context: Context): MyService {
        // 初始化你的服务
        return MyService.initialize(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // 定义依赖
        return listOf(TimberInitializer::class.java)
    }
}
```

### 步骤 2：添加到 AndroidManifest

```xml
<meta-data
    android:name="androidx.startup"
    android:value="com.example.appstarterkit.startup.MyCustomInitializer" />
```

### 步骤 3：访问初始化的结果

```kotlin
@HiltAndroidApp
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // AppInitializer 会自动初始化
        // 可以通过依赖注入访问
    }
}
```

## Hilt 集成

### 使用 Hilt 初始化 Worker

```kotlin
class WorkManagerInitializer : Initializer<WorkManager> {
    override fun create(context: Context): WorkManager {
        val config = Configuration.Builder()
            .setWorkerFactory(getWorkerFactory(context)) // Hilt 集成
            .build()
        
        return WorkManager.getInstance(context).apply {
            initialize(context, config)
        }
    }
}
```

### Hilt Worker 示例

```kotlin
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ExampleRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            repository.syncData()
            Result.success()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

## 最佳实践

### 1. 依赖顺序

确保初始化器按正确顺序执行：

```kotlin
override fun dependencies(): List<Class<out Initializer<*>>> {
    // 先初始化依赖
    return listOf(
        TimberInitializer::class.java,      // 第一：日志
        DataStoreInitializer::class.java,     // 第二：配置
        RoomInitializer::class.java           // 第三：数据库
    )
}
```

### 2. 异步初始化

对于耗时操作，使用 Coroutines：

```kotlin
override fun create(context: Context): MyService {
    return runBlocking(Dispatchers.IO) {
        // 耗时操作
        MyService.initialize(context)
    }
}
```

### 3. 懒加载

不需要立即使用的组件应该懒加载：

```kotlin
// 不要在启动时初始化
class AppInitializer : Initializer<HeavyService> {
    override fun create(context: Context): HeavyService {
        return HeavyService() // ❌ 不推荐
    }
}

// 使用懒加载
val lazyHeavyService = lazy { HeavyService() } // ✅ 推荐
```

### 4. 错误处理

妥善处理初始化错误：

```kotlin
class MyInitializer : Initializer<MyService> {
    override fun create(context: Context): MyService {
        return try {
            MyService.initialize(context)
        } catch (e: Exception) {
            Timber.e(e, "Failed to initialize MyService")
            // 返回默认实现或 null
            MyService()
        }
    }
}
```

## 性能优化

### 1. 并行初始化

无依赖的初始化器会并行执行：

```
TimberInitializer      ╱
DataStoreInitializerr ╲  并行执行
```

### 2. 延迟初始化

使用 Lazy Init Manager：

```kotlin
val analyticsService = lazyInitManager.get<AnalyticsService>("analytics")
// 只在首次访问时初始化
```

### 3. 减少初始化器

只初始化必需的组件：

```kotlin
// ✅ 必需的初始化器
TimberInitializer
DataStoreInitializer
RoomInitializer

// ❌ 可选的初始化器（使用懒加载）
AnalyticsInitializer
CrashlyticsInitializer
```

## 调试和监控

### 1. 启用 Startup 日志

在 Debug 模式下，Timber 会自动记录所有初始化：

```kotlin
D/TimberInitializer: Timber initialized
D/DataStoreInitializer: DataStore initialized
D/RoomInitializer: Room database initialized
D/WorkManagerInitializer: WorkManager initialized
```

### 2. 监控启动时间

```kotlin
val startupTime = AppStartupConfig.getTotalStartupTime()
Timber.d("Total startup time: ${startupTime}ms")
```

### 3. 性能分析

使用 Android Studio Profiler 分析启动性能：

1. 启动应用
2. 选择 CPU Profiler
3. 记录初始化阶段
4. 分析瓶颈

## 故障排除

### 问题：初始化器未执行

**解决方案**：

1. 检查 AndroidManifest.xml 中的配置
2. 确保 `android:authorities` 使用正确的包名
3. 清理并重建项目

### 问题：依赖关系错误

**解决方案**：

1. 使用 `dependencies()` 正确定义依赖
2. 确保不会产生循环依赖
3. 验证执行顺序

### 问题：Hilt Worker 未注入

**解决方案**：

1. 确保使用 `@HiltWorker` 注解
2. 检查 WorkManagerInitializer 是否正确配置
3. 验证 Worker 的构造函数使用 `@AssistedInject`

## 技术栈

- **androidx.startup:runtime**: 1.1.1
- **androidx.datastore:preferences**: 1.0.0
- **androidx.room**: 2.6.0
- **androidx.work:work-runtime-ktx**: 2.9.0
- **Hilt**: 2.48
- **Kotlin Coroutines**: 1.7.3

## 参考

- [AndroidX Startup Documentation](https://developer.android.com/topic/libraries/architecture/startup)
- [DataStore Documentation](https://developer.android.com/topic/libraries/architecture/datastore)
- [Room Documentation](https://developer.android.com/training/data-storage/room)
- [Hilt Documentation](https://dagger.dev/hilt/)

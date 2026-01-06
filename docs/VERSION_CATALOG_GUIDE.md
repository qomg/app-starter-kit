# Version Catalogs Guide

AppStarterKit 使用 Gradle Version Catalogs 来集中管理依赖和插件版本。

## 目录

```
gradle/
└── libs.versions.toml          # Version Catalog 配置文件

build.gradle.kts                  # 项目级配置（已更新使用 Catalog）

app/build.gradle.kts              # App 模块（已更新使用 Catalog）

core/build.gradle.kts              # Core 模块（已更新使用 Catalog）

gradle.properties               # Gradle 属性（已更新注释）
```

## 什么是 Version Catalogs？

Gradle Version Catalogs 是 Gradle 7.0+ 的功能，提供：

1. **集中式版本管理**：所有依赖版本在一个地方定义
2. **类型安全**：IDE 自动补全和类型检查
3. **避免冲突**：统一的版本引用
4. **易于维护**：更新版本只需修改一个文件

## 文件结构

### libs.versions.toml

```toml
[versions]
kotlin = "1.9.20"
androidx-core = "1.12.0"
compose-bom = "2023.10.01"

[plugins]
android-application = { id = "com.android.application", version.ref = "android-gradle-plugin" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }

[libraries]
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "androidx-core" }
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
```

## 版本目录结构

### 1. [versions] - 版本定义

定义所有使用的版本号：

```toml
[versions]
# Kotlin
kotlin = "1.9.20"
kotlin-compiler-extension = "1.5.4"

# Android
android-gradle-plugin = "8.2.0"
minSdk = "24"
targetSdk = "34"
compileSdk = "34"

# AndroidX
androidx-core = "1.12.0"
androidx-lifecycle = "2.6.2"
# ... 更多版本
```

### 2. [plugins] - 插件定义

定义 Gradle 插件：

```toml
[plugins]
android-application = { id = "com.android.application", version.ref = "android-gradle-plugin" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
dagger-hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
```

### 3. [libraries] - 库定义

定义所有依赖库：

```toml
[libraries]
# Kotlin
kotlin-stdlib = { group = "org.jetbrains.kotlin", name = "kotlin-stdlib", version.ref = "kotlin" }

# AndroidX Core
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "androidx-core" }

# Compose
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui", version.ref = "compose-ui" }
```

## 使用示例

### 1. 在 build.gradle.kts 中使用插件

```kotlin
plugins {
    alias(libs.plugins.android.application)  // 使用 Version Catalog
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
}
```

### 2. 在 dependencies 块中使用库

```kotlin
dependencies {
    // 使用 Catalog 中的库
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)

    // Hilt
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)
}
```

### 3. 使用版本引用

```kotlin
android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
    }
}
```

## 版本管理最佳实践

### 1. 版本命名约定

```toml
[versions]
# 版本号
kotlin = "1.9.20"

# 带有特定用途的版本
kotlin-compiler-extension = "1.5.4"

# Android SDK 版本
minSdk = "24"
targetSdk = "34"
```

### 2. 库命名约定

```toml
[libraries]
# 使用小写和连字符
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "androidx-core" }

# 清晰的分组
androidx-lifecycle-runtime-ktx
androidx-lifecycle-viewmodel-compose
```

### 3. 版本引用

```toml
[libraries]
# 使用 version.ref 引用 [versions] 中的版本
androidx-core-ktx = { 
    group = "androidx.core", 
    name = "core-ktx", 
    version.ref = "androidx-core"  # 引用版本
}
```

## 更新版本

### 1. 更新单个依赖版本

1. 打开 `gradle/libs.versions.toml`
2. 找到对应的版本
3. 更新版本号
4. 同步项目

示例：
```toml
# 更新 Compose 版本
[versions]
compose-bom = "2023.11.01"  # 从 2023.10.01 更新
```

### 2. 批量更新版本

```toml
[versions]
# 批量更新 AndroidX 库
androidx-core = "1.13.0"
androidx-lifecycle = "2.7.0"
androidx-navigation = "2.8.0"
```

### 3. 使用依赖更新插件

```bash
# Android Studio: File > Sync Project with Gradle Files
# 命令行：
./gradlew dependencies --refresh
```

## 添加新依赖

### 步骤 1：在 [versions] 中添加版本

```toml
[versions]
# 新库的版本
new-library = "1.0.0"
```

### 步骤 2：在 [libraries] 中添加库定义

```toml
[libraries]
# 定义新库
new-library = { 
    group = "com.example", 
    name = "new-library", 
    version.ref = "new-library" 
}
```

### 步骤 3：在 build.gradle.kts 中使用

```kotlin
dependencies {
    implementation(libs.new.library)
}
```

## IDE 支持

### Android Studio

- 自动补全：输入 `libs.` 会看到所有可用的库
- 类型检查：错误的库引用会立即被标记
- 版本信息：点击库定义可以直接跳转到版本定义

### IntelliJ IDEA

- 与 Android Studio 相同
- 支持 Ctrl+Click 导航到定义

## 常见场景

### 场景 1：更新 Compose BOM

```toml
[versions]
compose-bom = "2023.11.01"

# 所有 Compose 依赖会自动使用新版本
```

### 场景 2：添加插件

```toml
[plugins]
new-plugin = { id = "com.example.plugin", version = "1.0.0" }
```

```kotlin
plugins {
    alias(libs.plugins.new.plugin)
}
```

### 场景 3：使用 BOM 管理版本

```toml
[libraries]
# Retrofit BOM
retrofit-bom = { group = "com.squareup.retrofit2", name = "retrofit-bom", version.ref = "retrofit" }

retrofit2-retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
```

```kotlin
dependencies {
    implementation(platform(libs.retrofit.bom))
    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.converter.gson)
}
```

## 故障排除

### 问题：库未找到

**原因**：库定义名称不正确

**解决**：
1. 检查 `libs.versions.toml` 中的库定义
2. 确保名称拼写正确
3. 运行 `./gradlew --refresh`

### 问题：版本冲突

**原因**：手动指定版本与 Catalog 冲突

**解决**：
```kotlin
// ❌ 错误
implementation("com.example:library:1.0.0")

// ✅ 正确
implementation(libs.library)
```

### 问题：插件版本不匹配

**原因**：插件的 version.ref 引用了不存在的版本

**解决**：
```toml
[versions]
kotlin = "1.9.20"  # 确保版本存在

[plugins]
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }  # 引用正确
```

## 优势

### 使用 Version Catalogs 的优势

1. **集中管理**：所有版本在一个地方
2. **类型安全**：IDE 自动补全和错误检查
3. **易于更新**：更新版本无需搜索整个项目
4. **避免冲突**：统一版本引用
5. **更好的 IDE 支持**：自动补全和导航
6. **可维护性**：清晰的结构和命名

### 对比传统方式

| 特性 | 传统方式 | Version Catalogs |
|------|---------|-----------------|
| 版本管理 | 分散在多个文件 | 集中在一个文件 |
| 类型安全 | 字符串，无检查 | 强类型，IDE 支持 |
| 更新难度 | 需要全局搜索 | 单文件更新 |
| 冲突风险 | 高 | 低 |
| 可读性 | 差 | 好 |

## 高级用法

### 1. 特定版本覆盖

```kotlin
dependencies {
    // 使用特定版本覆盖 Catalog 中的版本
    implementation(libs.library) {
        version {
            strictly("1.0.0")  // 强制使用特定版本
        }
    }
}
```

### 2. 排除依赖

```kotlin
dependencies {
    implementation(libs.library) {
        exclude(group = "com.unwanted", module = "unwanted-module")
    }
}
```

### 3. 动态版本

```kotlin
android {
    compileSdk = libs.versions.compileSdk.get().toInt()
}
```

## 技术栈

- **Gradle**: 8.2+
- **Kotlin DSL**: 1.9.20+
- **Android Gradle Plugin**: 8.2.0
- **Version Catalogs**: Gradle 7.0+

## 参考

- [Gradle Version Catalogs Documentation](https://docs.gradle.org/current/userguide/platforms/plugins.html#sec:version-catalog-plugin)
- [Android Gradle Plugin Documentation](https://developer.android.com/build/multiproject/gradle-versions)
- [Gradle Version Catalog with Kotlin DSL](https://docs.gradle.org/current/userguide/platforms/plugins.html#sec:version-catalog-basic)

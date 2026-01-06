# Adaptive Screen Adaptation Summary

## 概述

AppStarterKit 现已完全支持 adaptive 屏幕适配，能够根据不同的设备尺寸和方向自动调整 UI 布局。

## 新增文件

### 核心工具
- `ui/util/WindowSizeClass.kt` - Window Size Classes 工具类

### Adaptive 组件
- `ui/components/navigation/AdaptiveNavigation.kt` - 自适应导航组件
- `ui/components/AdaptiveLayouts.kt` - 自适应布局组件

### 更新的屏幕
- `ui/screen/MainScreen.kt` - 使用 adaptive navigation
- `ui/screen/ExampleScreen.kt` - 使用 list-detail 布局
- `ui/screen/HomeScreen.kt` - 使用 adaptive spacing 和布局

## 核心功能

### 1. Window Size Classes

支持三种窗口宽度分类：

| 分类 | 宽度范围 | 典型设备 |
|------|-----------|---------|
| Compact | < 600dp | 手机竖屏 |
| Medium | 600dp - 840dp | 手机横屏、小平板 |
| Expanded | >= 840dp | 平板、大屏设备 |

支持三种窗口高度分类：

| 分类 | 高度范围 | 典型设备 |
|------|-----------|---------|
| Compact | < 480dp | 小屏设备 |
| Medium | 480dp - 900dp | 标准设备 |
| Expanded | >= 900dp | 大屏设备 |

### 2. Adaptive Navigation

根据屏幕宽度自动选择合适的导航方式：

#### Compact (< 600dp)
- **Bottom Navigation Bar**
- 适合手机竖屏使用
- 占用底部 56-80dp 空间

#### Medium (600-840dp)
- **Navigation Rail**
- 适合手机横屏、小平板
- 占用左侧 56-80dp 空间
- 带有标签的图标

#### Expanded (>= 840dp)
- **Permanent Navigation Drawer**
- 适合平板、大屏设备
- 占用左侧 256-320dp 空间
- 完整的导航抽屉

### 3. Adaptive Layouts

#### Adaptive Grid
```kotlin
AdaptiveGrid(
    items = myItems,
    minColumnWidth = 300
) { item ->
    MyCard(item)
}
```
- 自动计算列数
- 响应式调整
- 支持不同屏幕尺寸

#### Adaptive Column/Row
```kotlin
AdaptiveLayout(
    modifier = Modifier.fillMaxSize()
) {
    MyContent1()
    MyContent2()
}
```
- 手机：垂直布局（Column）
- 平板：水平布局（Row）

#### List-Detail Layout
```kotlin
ListDetailLayout(
    items = examples,
    selectedItemId = selectedId,
    onItemSelected = { id -> selectedId = id },
    listItemContent = { example, isSelected ->
        ListItem(...)
    },
    detailContent = { example ->
        DetailContent(example)
    }
)
```
- 手机：只显示列表
- 平板：并排显示列表和详情

#### Adaptive Cards
```kotlin
AdaptiveCards(items = cardItems)
```
- 手机竖屏：单列卡片
- 手机横屏：横向滚动卡片
- 平板：网格卡片布局

### 4. Adaptive Spacing

根据屏幕宽度自动调整间距：

| 宽度 | Padding |
|------|---------|
| < 600dp | 16dp |
| 600-840dp | 24dp |
| >= 840dp | 32dp |

```kotlin
val padding = adaptivePadding()
Column(
    modifier = Modifier.padding(padding)
) {
    // Content
}
```

## 使用示例

### 示例 1: 完整的 Adaptive Screen

```kotlin
@Composable
fun AdaptiveScreen() {
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(configuration)
    val isWideScreen = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    Scaffold(
        topBar = {
            AdaptiveTopAppBar(title = "My App")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(adaptivePadding())
        ) {
            if (isWideScreen) {
                // 平板布局
                Row(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.weight(0.4f)) {
                        // List panel
                    }
                    Column(modifier = Modifier.weight(0.6f)) {
                        // Detail panel
                    }
                }
            } else {
                // 手机布局
                Column(modifier = Modifier.fillMaxSize()) {
                    // Single column content
                }
            }
        }
    }
}
```

### 示例 2: Adaptive Navigation

```kotlin
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(configuration)

    AdaptiveNavigationSuite(
        widthSizeClass = windowSizeClass.widthSizeClass,
        navController = navController
    ) { padding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}
```

## 测试指南

### 1. 使用 Android Studio Preview

```kotlin
@Preview(
    name = "Phone",
    widthDp = 360,
    heightDp = 640
)
@Preview(
    name = "Phone Landscape",
    widthDp = 640,
    heightDp = 360
)
@Preview(
    name = "Tablet",
    widthDp = 800,
    heightDp = 1200
)
@Composable
fun MyScreenPreview() {
    MyScreen()
}
```

### 2. 使用不同尺寸的模拟器

#### 小屏手机 (4" - 5")
- 360x640dp
- 375x812dp
- 414x896dp

#### 标准手机 (5.5" - 6")
- 393x851dp
- 412x915dp

#### 平板 (7" - 10")
- 800x1280dp
- 1024x1366dp

#### 大屏 (12"+)
- 1200x1920dp+

### 3. 测试要点

- ✅ 在不同宽度下测试导航
- ✅ 在横竖屏切换时测试布局
- ✅ 测试折叠屏设备的适配
- ✅ 测试列表-详情布局的交互
- ✅ 验证间距和内边距
- ✅ 检查文本可读性
- ✅ 测试触摸目标大小
- ✅ 验证性能

## 文档

- `ADAPTIVE_GUIDE.md` - 完整的 adaptive 布局指南
- 包含详细的使用示例和最佳实践

## 技术实现

### 依赖更新

```kotlin
// app/build.gradle.kts
implementation("androidx.compose.material3:material3-window-size-class")
```

### 关键 API

- `calculateWindowSizeClass()` - 计算窗口尺寸分类
- `WindowWidthSizeClass` - 宽度分类
- `WindowHeightSizeClass` - 高度分类
- `AdaptiveNavigationSuite()` - 自适应导航套件
- `adaptivePadding()` - 自适应间距

## 性能考虑

1. **使用 remember**: 缓存窗口尺寸计算结果
2. **避免重组**: 合理使用 derivedStateOf
3. **延迟加载**: 对大列表使用 LazyColumn/LazyRow
4. **动画优化**: 使用适当的动画时长
5. **预览性能**: 在不同设备上测试

## 兼容性

- **最低 API**: Android 7.0 (API 24)
- **推荐 API**: Android 12+ (更好的 Material 3 支持)
- **Compose 版本**: Compose 1.5+
- **Material 版本**: Material 3

## 下一步

1. 为特定功能添加更多 adaptive layouts
2. 测试不同设备上的性能
3. 添加折叠屏适配
4. 实现响应式图片加载
5. 添加更多的屏幕方向处理

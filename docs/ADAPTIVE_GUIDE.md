# Adaptive Layout Guide

AppStarterKit 提供了完整的 adaptive 屏幕适配系统，支持从手机到大屏设备的各种尺寸。

## 目录结构

```
ui/util/
└── WindowSizeClass.kt              # Window size 工具类

ui/components/navigation/
└── AdaptiveNavigation.kt           # 自适应导航组件

ui/components/
└── AdaptiveLayouts.kt             # 自适应布局组件
```

## Window Size Classes

### 宽度分类

```kotlin
enum class WindowWidthSizeClass {
    Compact,      // < 600dp (手机竖屏)
    Medium,       // 600dp - 840dp (手机横屏/小平板)
    Expanded       // >= 840dp (平板/大屏设备)
}
```

### 高度分类

```kotlin
enum class WindowHeightSizeClass {
    Compact,      // < 480dp
    Medium,       // 480dp - 900dp
    Expanded       // >= 900dp
}
```

### 使用示例

```kotlin
@Composable
fun MyScreen() {
    val configuration = LocalConfiguration.current
    val windowSizeClass = calculateWindowSizeClass(configuration)

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            // 手机竖屏布局
        }
        WindowWidthSizeClass.Medium -> {
            // 手机横屏/小平板布局
        }
        WindowWidthSizeClass.Expanded -> {
            // 平板/大屏布局
        }
    }
}
```

## Adaptive Navigation

### 导航类型

AppStarterKit 根据屏幕宽度自动选择合适的导航方式：

| 宽度 | 导航方式 | 设备类型 |
|------|----------|---------|
| Compact (< 600dp) | Bottom Navigation Bar | 手机竖屏 |
| Medium (600-840dp) | Navigation Rail | 手机横屏/小平板 |
| Expanded (>= 840dp) | Permanent Navigation Drawer | 平板/大屏设备 |

### 使用 Adaptive Navigation

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

### 自定义导航 Rail

```kotlin
@Composable
fun CustomNavigationRail(navController: NavHostController) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEach { item ->
            NavigationRailItem(
                selected = isSelected,
                onClick = { /* navigate */ },
                icon = item.icon,
                label = item.label
            )
        }
    }
}
```

## Adaptive Layouts

### 1. Adaptive Grid

自动调整列数的网格布局：

```kotlin
@Composable
fun MyGrid() {
    AdaptiveGrid(
        items = myItems,
        minColumnWidth = 300,  // 最小列宽（dp）
        horizontalSpacing = 16,
        verticalSpacing = 16
    ) { item ->
        MyCard(item)
    }
}
```

### 2. Adaptive Column/Row

根据屏幕大小在 Column 和 Row 之间切换：

```kotlin
@Composable
fun AdaptiveLayoutExample() {
    AdaptiveLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        // 在手机上显示为 Column，在平板上显示为 Row
        MyContent1()
        MyContent2()
        MyContent3()
    }
}
```

### 3. List-Detail Layout

在宽屏上并排显示列表和详情：

```kotlin
@Composable
fun ExampleScreen() {
    var selectedId by remember { mutableStateOf<String?>(null) }
    val isWideScreen = isTabletOrLandscape(windowSizeClass.widthSizeClass)

    if (isWideScreen) {
        ListDetailLayout(
            items = examples,
            selectedItemId = selectedId,
            onItemSelected = { id -> selectedId = id },
            listItemContent = { example, isSelected ->
                ListItem(
                    headlineContent = { Text(example.name) },
                    selected = isSelected
                )
            },
            detailContent = { example ->
                if (example != null) {
                    DetailContent(example)
                } else {
                    EmptySelectionPlaceholder()
                }
            }
        )
    } else {
        // 手机上只显示列表
        ExampleList(items = examples)
    }
}
```

### 4. Adaptive Cards

根据屏幕大小调整卡片布局：

```kotlin
@Composable
fun AdaptiveCardsExample() {
    AdaptiveCards(
        items = cardItems
    )
}
```

- **手机竖屏**: 单列卡片布局
- **手机横屏**: 横向滚动的卡片行
- **平板**: 网格卡片布局（多列）

## Adaptive Spacing

根据屏幕大小自动调整间距：

```kotlin
@Composable
fun AdaptiveSpacingExample() {
    val padding = adaptivePadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        // 内容
    }
}
```

| 宽度 | Padding |
|------|---------|
| < 600dp | 16dp |
| 600-840dp | 24dp |
| >= 840dp | 32dp |

## 实践示例

### 示例 1: 响应式表单

```kotlin
@Composable
fun AdaptiveForm() {
    val layoutType = rememberLayoutType()

    if (layoutType == LayoutType.SINGLE_COLUMN) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TextField(...)
            TextField(...)
            Button(...)
        }
    } else {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                TextField(...)
                TextField(...)
            }
            Column(modifier = Modifier.weight(1f)) {
                TextField(...)
                TextField(...)
            }
        }
    }
}
```

### 示例 2: 响应式卡片组

```kotlin
@Composable
fun ResponsiveCardGroup() {
    val configuration = LocalConfiguration.current
    val columns = when {
        configuration.screenWidthDp < 600f -> 1
        configuration.screenWidthDp < 840f -> 2
        else -> 3
    }

    LazyColumn {
        items(items.chunked(columns)) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowItems.forEach { item ->
                    Card(
                        modifier = Modifier.weight(1f)
                    ) {
                        CardContent(item)
                    }
                }
                // 填充剩余空间
                repeat(columns - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
```

### 示例 3: 响应式 TopAppBar

```kotlin
@Composable
fun AdaptiveTopAppBar(title: String) {
    val configuration = LocalConfiguration.current
    val isWideScreen = configuration.screenWidthDp >= 600f

    if (isWideScreen) {
        // 平板上：居中标题，两侧留出导航空间
        LargeTopAppBar(
            title = { Text(title) }
        )
    } else {
        // 手机上：标准顶部栏
        TopAppBar(
            title = { Text(title) }
        )
    }
}
```

## 测试不同屏幕尺寸

### Android Studio 预览

在 Composable 函数上添加 `@Preview` 注解：

```kotlin
@Preview(
    name = "Phone",
    widthDp = 360,
    heightDp = 640
)
@Composable
fun PhonePreview() {
    MyScreen()
}

@Preview(
    name = "Tablet",
    widthDp = 800,
    heightDp = 1200
)
@Composable
fun TabletPreview() {
    MyScreen()
}
```

### Emulator 配置

使用不同尺寸的模拟器测试：

- 手机 (4" - 6"): 360x640dp, 360x800dp
- 平板 (7" - 10"): 800x1280dp, 1024x1366dp
- 大屏 (12" +): 1200x1920dp+

## 最佳实践

1. **使用 Window Size Classes**: 不要使用硬编码的屏幕尺寸，使用 WindowSizeClass
2. **渐进增强**: 从手机布局开始，为更大屏幕添加增强功能
3. **导航一致性**: 确保在不同屏幕尺寸上导航行为一致
4. **可访问性**: 确保所有尺寸下的可访问性
5. **性能测试**: 在不同设备上测试性能
6. **测试覆盖**: 测试所有屏幕尺寸和方向
7. **内容优先级**: 在小屏幕上优先显示重要内容

## 技术栈

- **Material Design 3**: 遵循 Material Design 3 的 adaptive 指南
- **Window Size Classes**: 使用官方 Window Size Classes API
- **Compose**: 100% Compose 实现
- **Kotlin**: 类型安全的布局逻辑

## 参考资源

- [Material Design 3: Window Size Classes](https://m3.material.io/foundations/layout/applying-layout/window-size-classes)
- [Android Developers: Large Screen App Quality](https://developer.android.com/guide/topics/large-screens/support-different-screen-sizes)
- [Jetpack Compose: Adaptive Layouts](https://developer.android.com/jetpack/compose/layouts/adaptive)

# Navigation System Guide

AppStarterKit 使用 Navigation Compose 实现了一个完整的导航系统，包括路由管理、底部导航、导航动画和导航事件处理。

## 目录结构

```
ui/navigation/
├── Routes.kt                          # 路由常量定义
├── AppNavigation.kt                   # 主导航配置（NavHost）
├── BottomNavigation.kt                # 底部导航栏组件
├── NavigationExtensions.kt             # 导航扩展函数
└── NavigationViewModel.kt              # 导航事件管理
```

## 核心功能

### 1. 路由定义

所有路由定义在 `Routes.kt` 中：

```kotlin
object Routes {
    const val HOME = "home"
    const val EXAMPLE = "example"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
    const val EXAMPLE_DETAIL = "example_detail/{exampleId}"
}
```

### 2. 导航配置

`AppNavigation.kt` 定义了完整的导航图，包括：
- 所有屏幕的路由配置
- 导航参数传递
- 页面过渡动画

```kotlin
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        // ... 其他配置
    ) {
        composable(route = Routes.HOME) {
            HomeScreen(...)
        }
        // ... 其他路由
    }
}
```

### 3. 底部导航栏

`BottomNavigation.kt` 提供了带有动画效果的底部导航栏：
- 自动高亮当前路由
- 平滑的图标和标签动画
- 点击切换路由

### 4. 导航扩展函数

`NavigationExtensions.kt` 提供了便捷的导航方法：

```kotlin
// 简单导航
navController.navigateToHome()
navController.navigateToExampleDetail("123")

// 带参数导航
navController.navigateTo(
    route = Routes.EXAMPLE_DETAIL,
    params = mapOf("exampleId" to "123")
)

// 导航回退
navController.navigateUp()

// 导航回退并返回结果
navController.navigateBack(result)
```

### 5. 导航事件管理

`NavigationViewModel.kt` 实现了基于事件的导航模式：

```kotlin
// ViewModel 中发送导航事件
class MyViewModel @Inject constructor(
    private val navigationViewModel: NavigationViewModel
) : ViewModel() {

    fun onButtonClick() {
        navigationViewModel.navigateToExample()
    }
}

// 在 Composable 中处理导航事件
HandleNavigationEvents(
    navigationEvents = navigationViewModel.navigationEvents,
    navController = navController
)
```

## 使用示例

### 基础导航

```kotlin
@Composable
fun MyScreen(navController: NavController) {
    Button(
        onClick = {
            // 导航到详情页
            navController.navigateToExampleDetail(exampleId = "123")
        }
    ) {
        Text("Go to Details")
    }
}
```

### 导航参数传递

定义带参数的路由：

```kotlin
composable(
    route = Routes.EXAMPLE_DETAIL,
    arguments = listOf(
        navArgument(NavArguments.EXAMPLE_ID) {
            type = NavType.StringType
        }
    )
) { backStackEntry ->
    val exampleId = backStackEntry.arguments?.getString(NavArguments.EXAMPLE_ID)
    ExampleDetailScreen(exampleId = exampleId)
}
```

### 导航结果返回

返回结果到上一个屏幕：

```kotlin
// 目标屏幕：返回结果
fun onSaveClick() {
    navController.navigateBack(result = "Success")
}

// 源屏幕：接收结果
val result by navController.observeNavigationResult<String>(
    key = "result"
) { result ->
    // 处理返回的结果
    showToast(result)
}
```

### 底部导航栏集成

```kotlin
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(navController)
            }
        }
    ) { padding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}
```

## 自定义导航

### 添加新路由

1. 在 `Routes.kt` 中定义路由常量
2. 在 `AppNavigation.kt` 中添加 composable
3. 在 `BottomNavigation.kt` 中添加底部导航项（如果需要）

### 自定义导航动画

修改 `AppNavigation.kt` 中的过渡动画：

```kotlin
NavHost(
    enterTransition = { slideInHorizontally() + fadeIn() },
    exitTransition = { slideOutHorizontally() + fadeOut() },
    popEnterTransition = { slideInHorizontally() + fadeIn() },
    popExitTransition = { slideOutHorizontally() + fadeOut() }
)
```

## 最佳实践

1. **使用类型安全的路由**：将路由定义在 `Routes.kt` 中，避免硬编码字符串
2. **导航参数**：使用 `navArgument` 定义参数类型，确保类型安全
3. **导航事件**：对于复杂的导航逻辑，使用 `NavigationViewModel` 管理导航事件
4. **底部导航**：只在顶层路由显示底部导航栏，子页面隐藏
5. **导航结果**：使用 `navigateBack` 方法传递结果，避免使用全局状态

## 技术栈

- **Navigation Compose**: Android Jetpack 导航组件
- **Kotlin**: 100% Kotlin 实现
- **Coroutines & Flow**: 用于导航事件处理
- **Material Design 3**: 遵循 Material Design 规范

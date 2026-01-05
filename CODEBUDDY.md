# CODEBUDDY.md

This file provides guidance to CodeBuddy Code when working with code in this repository.

## Project Overview

AppStarterKit is a modern, production-ready Android application starter template built with Jetpack Compose and Material Design 3. The project uses Clean Architecture (MVVM) with Kotlin and follows modern Android development best practices.

## Architecture

### Project Structure

```
AppStarterKit/
├── app/                                  # Main application module
│   ├── src/main/java/com/example/appstarterkit/
│   │   ├── ui/                          # UI Layer (Compose)
│   │   │   ├── components/              # Reusable UI components
│   │   │   │   ├── buttons/             # Animated buttons
│   │   │   │   ├── cards/               # Animated cards
│   │   │   │   ├── navigation/          # Navigation components
│   │   │   │   └── selection/           # Selector components
│   │   │   ├── screen/                  # Screens/Pages
│   │   │   ├── theme/                   # Theme system
│   │   │   │   ├── Color.kt
│   │   │   │   ├── Typography.kt
│   │   │   │   ├── Shape.kt
│   │   │   │   └── Theme.kt             # Theme composable
│   │   │   └── animation/               # Animation utilities
│   │   │       └── AnimationSpecs.kt
│   │   ├── data/                        # Data Layer
│   │   │   ├── repository/              # Data repositories
│   │   │   ├── datasource/              # Data sources
│   │   │   ├── local/                   # Local data (Room)
│   │   │   └── remote/                  # Remote data (Retrofit)
│   │   ├── domain/                      # Domain Layer
│   │   │   ├── model/                   # Business models
│   │   │   └── usecase/                 # Business use cases
│   │   ├── di/                          # Dependency Injection (Hilt)
│   │   └── MainActivity.kt
│   └── build.gradle.kts
├── core/                                 # Core module
├── build.gradle.kts                      # Project-level configuration
└── settings.gradle.kts
```

### Architecture Pattern

The project follows **Clean Architecture** with MVVM:

- **Presentation Layer (ui/)**: Jetpack Compose UI components and ViewModels
- **Domain Layer (domain/)**: Business logic and use cases (platform-independent)
- **Data Layer (data/)**: Repository implementations, data sources, local/remote storage
- **Dependency Injection**: Hilt for managing dependencies across layers

## Technology Stack

### Core Framework
- **UI Framework**: Jetpack Compose (declarative UI)
- **Design System**: Material Design 3
- **Language**: Kotlin 100%

### Jetpack Libraries
- Compose UI: Latest stable version
- ViewModel: Latest
- Room: Latest
- DataStore: Latest
- Navigation Compose: Latest
- Hilt: Latest
- Paging 3: Latest
- WorkManager: Latest
- CameraX: Optional
- Compose Material 3: Latest

### Third-Party Libraries
- **Networking**: Retrofit + Kotlin Serialization
- **Image Loading**: Coil (Compose version)
- **State Management**: Kotlin Coroutines + Flow
- **Logging**: Timber
- **Testing**: JUnit, Espresso, Compose UI Test

## Key Features

### Theme System
- Dynamic theme switching (Light/Dark mode)
- Material You dynamic colors (Android 12+)
- Smooth theme transition animations
- Dynamic color support

### UI Components
- Animated buttons and cards
- Material Design 3 components
- Custom animation effects
- Gesture-driven interactions
- Page transition animations

### State Management
- Unidirectional Data Flow (UDF)
- ViewModels with StateFlow/Flow
- Repository pattern
- Error handling strategy
- Loading state management

## Build and Development

This project uses Gradle with Kotlin DSL (.gradle.kts files).

### Build Commands

```bash
# Build the project
./gradlew build

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Run UI tests
./gradlew connectedAndroidTest

# Run lint checks
./gradlew lint
```

## Testing Strategy

### Unit Tests
- ViewModel tests
- UseCase tests
- Repository tests
- Utility class tests

### Integration Tests
- Screen tests
- Navigation tests
- Data flow tests

### UI Tests
- Component tests
- Screen tests
- Interaction tests (Compose UI Test)

## Development Guidelines

### Clean Architecture Principles
- Domain layer must remain platform-independent
- Data layer implements interfaces defined in domain layer
- UI layer depends only on domain layer and repository interfaces
- Dependency inversion: higher layers depend on abstractions

### Compose Best Practices
- Use stateless composables where possible
- Follow single responsibility principle for composables
- Use `remember` for state that doesn't need to be shared
- Use `rememberSaveable` for state that should survive recomposition and process death
- Prefer `LaunchedEffect` for one-time side effects in composition

### Coroutines and Flow
- Use `ViewModelScope` for ViewModels (canceled when ViewModel is cleared)
- Use `lifecycleScope` for Activities/Fragments
- Use StateFlow for state that needs to be observed
- Use SharedFlow for events that shouldn't be replayed
- Always collect flows in a lifecycle-aware manner (e.g., `collectAsStateWithLifecycle()`)

### Dependency Injection
- Use `@Module` and `@Provides` for dependency configuration
- Use `@Inject constructor` for automatic injection
- Qualifiers for dependencies of the same type
- Scopes: `@Singleton` for application-scoped dependencies, `@ViewModelScoped` for ViewModel dependencies

## File Naming Conventions

- **Composable functions**: PascalCase (e.g., `UserProfileScreen`)
- **ViewModel classes**: PascalCase ending with `ViewModel` (e.g., `HomeViewModel`)
- **Repository classes**: PascalCase ending with `Repository` (e.g., `UserRepository`)
- **UseCase classes**: PascalCase ending with `UseCase` (e.g., `GetUserProfileUseCase`)
- **Data models**: PascalCase (e.g., `UserProfile`)

## Performance Considerations

- Use LazyColumn/LazyRow for long lists instead of Column/Row
- Implement image loading optimizations with Coil
- Use Room database indexes for frequently queried fields
- Implement network caching strategies
- Follow memory leak prevention practices (avoid context leaks, unregister listeners)

## Design System

All UI components follow Material Design 3 guidelines:
- Adaptive layouts for phones and tablets
- Accessibility support
- RTL (Right-to-Left) support
- Dynamic font scaling

Theme system is located in `ui/theme/`:
- `Color.kt`: Color palette and scheme definitions
- `Typography.kt`: Typography styles
- `Shape.kt`: Shape definitions
- `Theme.kt`: Theme composable providing the theme

// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    // Apply false to only apply plugins to subprojects via version catalog
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.ksp) apply false
}

// Enable version catalog
// All plugin and library versions are managed in gradle/libs.versions.toml

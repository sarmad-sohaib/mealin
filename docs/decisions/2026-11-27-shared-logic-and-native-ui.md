# Shared rules live in sharedLogic

## Decision

Shared rules live in :sharedLogic. iOS UI is SwiftUI in iosApp and already calls SharedLogic. Android Compose currently lives in :sharedUI because the wizard generated it that way. Do not move or delete :sharedUI in this task.

## Why

Meal logging rules have one home, `:sharedLogic`, and both apps call it. `iosApp/iosApp/ContentView.swift` imports `SharedLogic` and calls `Greeting`. Android shows the wizard Compose UI: `androidApp` depends on `:sharedUI`, and `:sharedUI` depends on `:sharedLogic`. The wizard created `:sharedUI` for that Compose code. Moving Compose into `androidApp`, or deleting `:sharedUI`, would change the module layout before any meal-logging code exists.

## Other ways

- Move the Compose UI into `androidApp` and delete `:sharedUI` during repo setup. That is a module change, and this task leaves the wizard modules in place. A later decision can supersede this file if the Compose code should move.
- Draw the iOS UI with Compose Multiplatform. `iosApp` is already SwiftUI, it already calls `SharedLogic`, and `:sharedUI` does not declare an iOS target.

## Stood on

- `settings.gradle.kts` includes `:sharedLogic` and `:sharedUI`.
- `sharedLogic/build.gradle.kts` builds a static iOS framework named `SharedLogic` for `iosArm64` and `iosSimulatorArm64`.
- `iosApp/iosApp/ContentView.swift` imports `SharedLogic`.
- `androidApp/build.gradle.kts` depends on `project(":sharedUI")`.
- `sharedUI/build.gradle.kts` applies the Compose Multiplatform plugin, depends on `project(":sharedLogic")` from `commonMain`, and declares an Android target only.
- `sharedUI/src/commonMain/kotlin/com/sarmad/mealin/App.kt` calls `Greeting` from `:sharedLogic`.

## Supersedes

None.

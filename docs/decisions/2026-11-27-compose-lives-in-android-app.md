# Android Compose moves into androidApp

## Decision

Shared rules stay in `:sharedLogic`. iOS UI stays SwiftUI in `iosApp` and keeps calling SharedLogic. Android Compose moves from `:sharedUI` into `androidApp`, and `:sharedUI` is deleted. Task 003 does that move. No other task moves or deletes modules.

## Why

`:sharedUI` exists because the Kotlin Multiplatform wizard generated a Compose module. `sharedUI/build.gradle.kts` declares an Android target and no iOS target, and `androidApp` already depends on it only to show the wizard `App()` composable. Leaving Compose in a module named shared UI implies the iOS app will share that UI. It will not. iOS is SwiftUI. The first product task moves the wizard Compose code into the Android app and deletes the extra module before meal screens are added.

## Other ways

- Leave Compose in `:sharedUI` for the whole rewrite. The module name would keep saying the UI is shared, and every Android screen would land in a module the iOS app does not use.
- Draw iOS with Compose Multiplatform and keep `:sharedUI`. `iosApp` is already SwiftUI and already calls SharedLogic.

## Stood on

- `docs/decisions/2026-11-27-shared-logic-and-native-ui.md` left `:sharedUI` in place during repo setup and said a later decision could supersede that.
- `sharedUI/build.gradle.kts` applies Compose and depends on `:sharedLogic`. It does not register `iosArm64` or `iosSimulatorArm64`.
- `androidApp/build.gradle.kts` depends on `project(":sharedUI")`.
- `iosApp/iosApp/ContentView.swift` imports `SharedLogic`.

## Supersedes

docs/decisions/2026-11-27-shared-logic-and-native-ui.md

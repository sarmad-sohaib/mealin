# 004 Move Compose into androidApp

Task 003. The work log number is 004 because `docs/work/003-team-loop.md` already exists.

`:sharedUI` is deleted. The wizard `App()` composable lives in `androidApp` and still calls `Greeting` from `:sharedLogic`. `iosApp/iosApp/ContentView.swift` was not edited. It still imports `SharedLogic` and calls `Greeting().greet()`.

Decision: `docs/decisions/2026-11-27-compose-lives-in-android-app.md`.

## Committed

- `6bddb8fb11213af072ad61ca122f388acc285ce2` Mark task 003 in progress. Regenerated `docs/BOARD.md`.
- `2e7b122aa37a5e0b117c2ac2d0d43ece7a7f43c6` Move the wizard Compose greeting into androidApp.

This move does not add product behavior, so there is no failing-test commit before the implementation.

## Checks

- `./gradlew :androidApp:assembleDebug` green, in the same Gradle run as the unit tests.
- `./gradlew :androidApp:testDebugUnitTest :sharedLogic:testAndroidHostTest` green. `SharedUICommonTest.example` passed.
- `./gradlew :sharedLogic:iosSimulatorArm64Test` green. `xcode-select` points at `/Library/Developer/CommandLineTools`, so this run set `DEVELOPER_DIR=/Applications/Xcode.app/Contents/Developer`.
- `xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -configuration Debug -destination 'generic/platform=iOS Simulator' build` succeeded with the same `DEVELOPER_DIR`.
- The iOS app was not launched. `device_list` reported: `xcrun: error: unable to find utility "simctl", not a developer tool or in PATH`. `device_open` for iOS then reported that no iOS devices were found. Switching `xcode-select` to Xcode needs a password, and the password request timed out.

## Local choices

### The wizard image is an Android vector drawable

Why. `androidApp` is an Android application. The wizard file is already an Android vector. `androidx.compose.ui.res.painterResource` loads `R.drawable.compose_multiplatform`. The module already applies the Compose compiler.

Other ways. Apply `org.jetbrains.compose` to `androidApp` and keep `composeResources` for one wizard image. Keep the call to `mealin.sharedui.generated.resources` after the module is gone. That package would not exist.

### The command blocks are the only edits in AGENTS.md and README.md

Why. The task Owns those command blocks. Both files now name `./gradlew :androidApp:testDebugUnitTest :sharedLogic:testAndroidHostTest` and no longer name `:sharedUI:testAndroidHostTest`.

Other ways. Rewrite the opening paragraph of `AGENTS.md` and the `sharedUI` tour in `README.md` so they describe the tree after the move. That edits text outside the command blocks. Leave the old test command. That fails the Done when list.

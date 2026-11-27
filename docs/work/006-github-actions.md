# 006 GitHub Actions

Task 005. The work log number is 006 because `docs/work/005-swift-interop-spike.md` already exists.

Ubuntu runs `./gradlew :androidApp:assembleDebug :sharedLogic:testAndroidHostTest`. macOS runs `./gradlew :sharedLogic:iosSimulatorArm64Test`. The workflow cites `docs/decisions/2026-11-27-test-strategy.md`.

This task does not change product behavior, so there is no failing-test commit before the workflow.

## Committed

- `6ff2a12bcb5823fdadecafc625057bad50de7f1e` Mark task 005 in progress. Regenerated `docs/BOARD.md`.
- `abfc8388b773b5638a7359f57e7d210b35286bb9` Run the shared test commands on GitHub Actions.

## Checks

- `./gradlew :androidApp:assembleDebug :sharedLogic:testAndroidHostTest` green. `assembleDebug` and `testAndroidHostTest` were UP-TO-DATE. The build finished in 6s.
- `./gradlew :sharedLogic:iosSimulatorArm64Test` green. `iosSimulatorArm64Test` was UP-TO-DATE. Xcode 26.6 (build 17F113) is selected at `/Applications/Xcode.app/Contents/Developer`. An iPhone 17 simulator on iOS 26.5 was booted, and iOS 26.4 simulators were installed.

## Local choices

### Temurin 21, without the Gradle setup action

Why. Android Gradle Plugin 9.1.1 needs JDK 17 or newer. `gradle/gradle-daemon-jvm.properties` sets `toolchainVersion=21`. The Gradle GitHub Actions guide sets Temurin 21 with `actions/setup-java@v4` and checks out with `actions/checkout@v4`. The wrapper already selects Gradle 9.5.1.

Other ways. Use JDK 17, which meets the plugin minimum and does not match the daemon toolchain file. Add `gradle/actions/setup-gradle@v6` for the cache and wrapper check in that same guide. The Done when list is the two Gradle commands, and the wrapper already pins Gradle.

### `ubuntu-latest` and `macos-latest`

Why. The task asks for an Ubuntu job and a macOS job. `iosSimulatorArm64` tests run on Apple Silicon, and `macos-latest` is that host. Its default Xcode is 26.6 build 17F113, the same build selected on this machine. The current Ubuntu image already has Android SDK platform `android-37.0` and build-tools 37.0.0. This machine builds `compileSdk` 37 from `platforms/android-37.0`.

Other ways. Pin `ubuntu-24.04` and `macos-26` so the labels cannot move. Select Xcode 26.4.1 on the runner because the Kotlin 2.4.21 compatibility table names Xcode 26.4. This machine already runs the macOS command on the default Xcode 26.6.

### Pull requests into `master`, and pushes to `master`

Why. The check has to run on the pull request a later agent opens. `master` is the default branch, so a push there runs it again after merge.

Other ways. Run on every push as well as every pull request. That starts the same build twice for one pull request update. Add `workflow_dispatch`. The task does not ask for a manual run.

### The Ubuntu command does not include `:androidApp:testDebugUnitTest`

Why. The Done when list names `./gradlew :androidApp:assembleDebug :sharedLogic:testAndroidHostTest`. Task 003 added `:androidApp:testDebugUnitTest` to `AGENTS.md`. This task does not own that file, and the outcome says a later edit of this workflow is in scope only when task 003 changes a command this file calls.

Other ways. Also run `:androidApp:testDebugUnitTest` because `AGENTS.md` lists it. Leave `:sharedUI:testAndroidHostTest` in the workflow. Task 003 deleted that module, and the task says to call `:sharedLogic:testAndroidHostTest`.

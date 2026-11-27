# 005 Swift interop spike

Task 004. The work log number is 005 because `docs/work/004-move-compose-into-android-app.md` already exists.

The throwaway sample exercised Swift export and SKIE, then it was deleted. No `com.sarmad.mealin.spike` source package remains. iOS screens will collect a store with SKIE.

Decision: `docs/decisions/2026-11-28-skie-collects-the-store.md`.

## Committed

- `3014cf3` Mark task 004 in progress. Regenerated `docs/BOARD.md`.
- `d6021ee` Record that iOS collects a store with SKIE. Regenerated `docs/decisions/INDEX.md`.

This spike does not change product behavior, so there is no failing-test commit before the decision.

## Checks

- `:sharedLogic:embedSwiftExportForXcode` green. It ran `:sharedLogic:iosSimulatorArm64DebugSwiftExport` and `:sharedLogic:iosSimulatorArm64DebugBuildSPMPackage`. Gradle registers those tasks only under an Xcode environment, so the run set `CONFIGURATION=Debug`, `SDK_NAME=iphonesimulator`, `ARCHS=arm64`, `KOTLIN_FRAMEWORK_BUILD_TYPE=debug`, `TARGET_BUILD_DIR`, `BUILT_PRODUCTS_DIR`, `FRAMEWORKS_FOLDER_PATH`, `DEPLOYMENT_TARGET_SETTING_NAME=IPHONEOS_DEPLOYMENT_TARGET`, and `IPHONEOS_DEPLOYMENT_TARGET=18.2`.
- `:sharedLogic:linkDebugFrameworkIosSimulatorArm64` green with SKIE 0.10.16 applied to the spike.
- After the sample and both plugins were removed, `./gradlew :sharedLogic:linkDebugFrameworkIosSimulatorArm64 :sharedLogic:iosSimulatorArm64Test` was green. `linkDebugFrameworkIosSimulatorArm64` executed. `iosSimulatorArm64Test` was UP-TO-DATE.
- A Swift file typechecked against each built module. The signatures and the errors are in the decision.

## Local choices

### The sample and both plugins were removed

Why. The decision records the four shapes for each tool. The Done when list says the spike package goes away unless the decision needs the sample. `linkDebugFrameworkIosSimulatorArm64` and `iosSimulatorArm64Test` pass without SKIE, Swift export, or `kotlinx-coroutines-core`.

Other ways. Leave SKIE 0.10.16 applied so the next iOS task does not add the plugin. That is a dependency the Done when commands do not need, and this task does not own `sharedLogic/build.gradle.kts`. Leave the spike types in the tree so a later task can re-typecheck them. The decision already quotes the signatures that typechecked.

### SKIE's default-argument check used the annotation, not a module-wide flag

Why. `@DefaultArgumentInterop.Enabled` was on `label` only. The same build left `SpikeState.copy` as `doCopy(millilitres:)`, so the feature is off unless annotated.

Other ways. Enable `DefaultArgumentInterop` for the whole spike package in the SKIE Gradle block. That would also generate overloads for `copy`, and the spike could not show the default-off result in the same run. Skip the annotation and report only the disabled state. That would not show the overloads the feature actually generates.

### Swift export was run from Gradle with Xcode's environment variables

Why. `embedSwiftExportForXcode` is the task in the Kotlin Swift export page. Without `CONFIGURATION`, `SDK_NAME`, `ARCHS`, and the build directories, Gradle registers a stub and does not run the export.

Other ways. Change `iosApp` to call `embedSwiftExportForXcode` and build from Xcode. This task does not own `iosApp`, and the spike does not need the app target.

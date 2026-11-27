# iOS collects a store with SKIE

## Decision

iOS UI collects a `:sharedLogic` store with SKIE 0.10.16 on the `SharedLogic` framework from `:sharedLogic:linkDebugFrameworkIosSimulatorArm64`. Swift reads `store.state.value`, collects with `for await state in store.state`, sends a suspend intent with `try await store.apply(intent:)`, and switches a sealed value with `onEnum(of:)`. The spike package is not in the tree. The first iOS task that collects a store adds plugin `co.touchlab.skie` 0.10.16 to `:sharedLogic`. It keeps `embedAndSignAppleFrameworkForXcode`.

## Why

A screen store exposes one `StateFlow`. The SKIE framework typechecked both the current value and `for await` on that property. `iosApp` already embeds this framework. The link also ran `:sharedLogic:skieUploadAnalyticsDebugFrameworkIosSimulatorArm64`.

## Other ways

Swift export on Kotlin 2.4.21, and SKIE, were both spiked against one sealed class (`SpikeIntent.Refresh`, `SpikeIntent.SetMillilitres`), one `StateFlow<SpikeState>`, one suspend function (`apply`), and one defaulted function (`label(amount: Int = 250, unit: String = "ml")`). The sample was deleted after the runs below.

Swift export. `:sharedLogic:embedSwiftExportForXcode` was green. It ran `:sharedLogic:iosSimulatorArm64DebugSwiftExport` and `:sharedLogic:iosSimulatorArm64DebugBuildSPMPackage`. The plugin registers those tasks only when Gradle sees an Xcode environment, so the run set `CONFIGURATION=Debug`, `SDK_NAME=iphonesimulator`, `ARCHS=arm64`, `KOTLIN_FRAMEWORK_BUILD_TYPE=debug`, `TARGET_BUILD_DIR`, `BUILT_PRODUCTS_DIR`, `FRAMEWORKS_FOLDER_PATH`, `DEPLOYMENT_TARGET_SETTING_NAME=IPHONEOS_DEPLOYMENT_TARGET`, and `IPHONEOS_DEPLOYMENT_TARGET=18.2`. A Swift file typechecked against the built module.

- Sealed class. `sealedType()` returns `SpikeIntent_SealedType` with `.refresh` and `.setMillilitres`. A switch with no default typechecked.
- `StateFlow`. `store.state` is `any KotlinTypedStateFlow<SpikeState>`. `store.state.value` typechecked. `for try await` on `store.state` failed because that type is not an `AsyncSequence`. `for try await state in store.state.asAsyncSequence()` typechecked.
- Suspend. `apply(intent:) async throws` typechecked.
- Default argument. `label(amount:unit:)` requires both parameters. `label()` failed with missing arguments for `amount` and `unit`. `label(amount:)` failed with a missing `unit`. Swift export is Alpha, and adopting it replaces `embedAndSignAppleFrameworkForXcode` with `embedSwiftExportForXcode`.

SKIE 0.10.16. `:sharedLogic:linkDebugFrameworkIosSimulatorArm64` was green. A Swift file typechecked against `SharedLogic.framework`. `label` carried `@DefaultArgumentInterop.Enabled` from `co.touchlab.skie:configuration-annotations:0.10.16`. The unannotated `copy` on `SpikeState` did not.

- Sealed class. `onEnum(of:)` returns a frozen enum. A switch on `.refresh` and `.setMillilitres` typechecked with no default.
- `StateFlow`. `store.state.value` typechecked. `for await state in store.state` typechecked.
- Suspend. `try await store.apply(intent:)` typechecked. The completion-handler form is private to Swift.
- Default argument. `label()`, `label(amount:)`, `label(unit:)`, and `label(amount:unit:)` typechecked. They are overloads, not Swift default values. `doCopy(millilitres:)` still required its argument. SKIE leaves this feature off unless the annotation is present, and the feature is deprecated. Screen stores do not enable it.

Calling the Objective-C header from Swift without either tool leaves a sealed class as classes, a suspend function as a completion handler, and a `StateFlow` as a Kotlin interface. That is not how a screen collects a store.

## Stood on

- `docs/decisions/2026-11-27-screen-store-in-shared-logic.md` says the store exposes a `StateFlow`.
- Kotlin 2.4.21. The Swift export DSL is `swiftExport { moduleName = "SharedLogic" }`. The spike also set `flattenPackage = "com.sarmad.mealin.spike"`. The generated file still nested the types under `ExportedKotlinPackages.com.sarmad.mealin.spike` and added top-level typealiases.
- SKIE 0.10.16 is the release that supports Kotlin 2.4.21. The `StateFlow` in the sample used `kotlinx-coroutines-core` 1.11.0. Neither dependency stays in the tree.
- `iosApp/iosApp.xcodeproj/project.pbxproj` runs `:sharedLogic:embedAndSignAppleFrameworkForXcode` and sets `IPHONEOS_DEPLOYMENT_TARGET` to 18.2.
- The signatures above are from `sharedLogic/build/SwiftExport/iosSimulatorArm64/Debug/files/SharedLogic/SharedLogic.swift` and from `SharedLogic.framework` `Headers/SharedLogic.apinotes` after the SKIE link. Those build outputs are not committed.

## Supersedes

None.

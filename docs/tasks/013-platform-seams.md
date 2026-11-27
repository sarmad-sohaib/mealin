# 013 Platform seams

Status: Blocked

Depends: 003, 006, 008

Lane: S

Migration queue: no. Do not add a user table. This task owns the Ktor client setup. Later tasks use it and do not reconfigure the engine.

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/seam/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/seam/`
  - `androidApp/src/main/kotlin/com/sarmad/mealin/AppGraph.kt`
  - `iosApp/iosApp/AppGraph.swift`
  - the datetime, serialization, and Ktor version lines in `gradle/libs.versions.toml` and the matching `sharedLogic/build.gradle.kts` dependencies
  - the OkHttp engine dependency on the Android source set and the Darwin engine on the iOS source set

Does not touch:
  - `MealNavHost.kt` (task 015 creates it)
  - SQLDelight migrations and `catalog/`
  - feature stores
  - `sharedUI/`

Legacy, read only:
  - `common/src/main/java/com/qg/caloriecounter/common/DispatcherProvider.kt`

Decisions:
  - `docs/decisions/2026-11-27-constructor-injection.md`
  - `docs/decisions/2026-11-27-kotlinx-datetime-serialization-ktor.md`
  - `docs/decisions/2026-11-27-clock-ids-locale-passed-in.md`
  - `docs/decisions/2026-11-27-strings-stay-in-the-apps.md`

Outcome:
`AppGraph` in each app constructs the shared graph. Shared code receives a clock, an id generator, and a locale. Errors are a sealed type of codes, not English strings. One Ktor client is created in the graph. A `commonTest` uses `MockEngine`, returns one recorded JSON body, and decodes it with kotlinx.serialization.

The Android graph still shows the wizard greeting. It does not yet show the water screen. `iosApp` still launches the greeting. `AppGraph.swift` is constructed from the app entry and does not replace `ContentView` in this task.

Why:
Stores and tests need a fake clock and a fake HTTP engine before the first feature. Hilt wrappers in CalorieCounter are not copied. `DispatcherProvider` is replaced by the dispatchers the graph passes in. Versions belong in the toml, and task 008 has finished editing that file before this task starts.

Done when:
  - The failing `MockEngine` test is committed first. It requests a URL and expects the decoded fixture. The client implementation is the next commit. Do not squash.
  - A test clock returns a fixed instant. Production code under `com.sarmad.mealin.seam` does not call `Clock.System`.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.
  - `./gradlew :androidApp:assembleDebug` is green.
  - iOS app: open iosApp in Xcode and run iosApp. The greeting still appears.

Out of scope:
  - Recipe, barcode, and recognizer endpoints. Those tasks add their own `MockEngine` fixtures and call this client.
  - A second HTTP library.

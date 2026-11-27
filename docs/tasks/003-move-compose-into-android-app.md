# 003 Move Compose into androidApp

Status: Ready

Depends: none

Lane: A

Migration queue: no

Owns:
  - `androidApp/`
  - `sharedUI/`
  - `settings.gradle.kts`
  - the command blocks in `AGENTS.md` and `README.md`

Does not touch:
  - `sharedLogic/`
  - `iosApp/`
  - `catalog/`
  - meal-logging rules, which do not exist yet

Legacy, read only:
  - none

Decisions:
  - `docs/decisions/2026-11-27-compose-lives-in-android-app.md`

Outcome:
`:sharedUI` is gone. The wizard Compose greeting lives in `androidApp` and still runs. The iOS app still shows `Greeting` from SharedLogic.

Why:
The wizard put Compose in `:sharedUI`, and that module has no iOS target. Later Android screens must not land in a module the iOS app does not use. This is the module move. It does not add product behavior.

Done when:
  - `settings.gradle.kts` does not include `:sharedUI`, and the `sharedUI/` directory is gone.
  - The greeting composable still has a test, now under `androidApp`. The iOS greeting still calls SharedLogic.
  - `AGENTS.md` and `README.md` no longer mention `:sharedUI:testAndroidHostTest`. They name the Android test command below.
  - `./gradlew :androidApp:assembleDebug` is green.
  - `./gradlew :androidApp:testDebugUnitTest :sharedLogic:testAndroidHostTest` is green.
  - `./gradlew :sharedLogic:iosSimulatorArm64Test` is green.
  - iOS app: open iosApp in Xcode and run iosApp.

Out of scope:
  - Nutrition, SQLDelight, the catalog copy, and deleting `expect fun getPlatform()`.
  - Any screen other than the wizard greeting.

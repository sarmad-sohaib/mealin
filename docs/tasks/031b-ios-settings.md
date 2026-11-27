# 031b iOS settings

Status: Blocked

Depends: 004, 015, 016, 023b, 031

Lane: I

Migration queue: no

Owns:
  - `iosApp/iosApp/Settings/`
  - the settings route registration in `MealNav.swift`
  - the settings keys in `Localizable.strings`

Does not touch:
  - `androidApp/`
  - `sharedLogic/`
  - unit math

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/settings/SettingsFragment.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/decisions/2026-11-27-strings-stay-in-the-apps.md`
  - `docs/examples/ios-log-water.md`
  - the Swift interop decision from task 004

Outcome:
SwiftUI settings renders unit and language state and sends intents. Copy lives in `Localizable.strings`.

Why:
This task can run beside 031a. It waits for 023b so `MealNav.swift` has one writer.

Done when:
  - The view test is committed first and does not convert units.
  - iOS app: open iosApp in Xcode and run iosApp.
  - `./gradlew :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Android settings.
  - HealthKit and paywalls.

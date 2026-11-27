# 023b iOS home

Status: Blocked

Depends: 004, 015, 016, 023, 028b

Lane: I

Migration queue: no

Owns:
  - `iosApp/iosApp/Home/`
  - the home route registration in `MealNav.swift`
  - the home keys in `Localizable.strings`

Does not touch:
  - `androidApp/`
  - `sharedLogic/`
  - the home formula

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/home/HomeFragment.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/examples/ios-log-water.md`
  - the Swift interop decision from task 004

Outcome:
SwiftUI home renders the day state and sends intents. It does not calculate remaining.

Why:
The iOS water screen is the pattern. This task runs beside 023a and waits for 028b so `MealNav.swift` has one writer.

Done when:
  - The view test is committed first with eaten 100, burned 40, remaining 0.
  - iOS app: open iosApp in Xcode and run iosApp.
  - `./gradlew :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Android home.
  - HealthKit.

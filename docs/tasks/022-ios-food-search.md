# 022 iOS food search and log

Status: Blocked

Depends: 004, 015, 016, 019, 020

Lane: I

Migration queue: no

Owns:
  - `iosApp/iosApp/FoodLog/`
  - the food route registration in `iosApp/iosApp/Navigation/MealNav.swift`
  - the food keys in `Localizable.strings`

Does not touch:
  - `androidApp/`
  - `sharedLogic/`
  - `Search.sq`

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/logmeal/AddLogFragment.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/examples/ios-log-water.md`
  - the Swift interop decision from task 004

Outcome:
The SwiftUI search and log screens render store state and send intents. They do not calculate calories.

Why:
Same pattern as the iOS water screen. This task can run beside task 021. It waits for task 019 so only one iOS task edits `MealNav.swift`.

Done when:
  - The view test is committed first. Fixture state shows Banana at 89 kcal and the log control records the intent.
  - iOS app: open iosApp in Xcode and run iosApp.
  - `./gradlew :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Android search.
  - Network search by name.

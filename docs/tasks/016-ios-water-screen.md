# 016 iOS water screen

Status: Blocked

Depends: 004, 014

Lane: I

Migration queue: no

Owns:
  - `iosApp/iosApp/Water/`
  - `docs/examples/ios-log-water.md`
  - `iosApp/iosApp/Navigation/MealNav.swift`

Does not touch:
  - `androidApp/`
  - `sharedLogic/` except to collect the store the way task 004's decision says
  - SQLDelight
  - the water store

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/watertracking/WaterTrackerFragment.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/decisions/2026-11-27-strings-stay-in-the-apps.md`
  - the Swift interop decision written by task 004

Outcome:
The SwiftUI water screen renders store state and sends the log intent. Copy is in `Localizable.strings`. `docs/examples/ios-log-water.md` is the iOS pattern. Collect the `StateFlow` only as task 004 decided.

Why:
This is the iOS half of the only UI pattern. Later iOS screens copy this file, not a fragment from CalorieCounter.

Done when:
  - A SwiftUI test or preview assertion shows a fixture state and records the intent. Commit that test before the view if the project can host a test. If Xcode has no test target, add one under `iosApp` and commit the failing test first.
  - The view does not add millilitres.
  - iOS app: open iosApp in Xcode and run iosApp. The water screen appears.
  - `./gradlew :sharedLogic:iosSimulatorArm64Test` is green.
  - The example file exists.

Out of scope:
  - Android water.
  - Any other iOS screen until this task and 015 are Done.

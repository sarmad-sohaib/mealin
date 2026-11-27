# 019 iOS onboarding

Status: Blocked

Depends: 004, 015, 016, 017

Lane: I

Migration queue: no

Owns:
  - `iosApp/iosApp/Onboarding/`
  - the onboarding route registration in `iosApp/iosApp/Navigation/MealNav.swift`
  - the onboarding keys in `iosApp/iosApp/Localizable.strings`

Does not touch:
  - `androidApp/`
  - `sharedLogic/`
  - the water views except the nav registration
  - the budget function

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/onboarding/reviews/ReviewsFragment.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/decisions/2026-11-27-strings-stay-in-the-apps.md`
  - `docs/examples/ios-log-water.md`
  - the Swift interop decision from task 004

Outcome:
SwiftUI onboarding renders store state and sends step intents. The budget on the last step is the number in the state. The view does not compute it.

Why:
Task 016 is the iOS pattern, and both water examples are Done before any other screen starts. This task can run beside task 018.

Done when:
  - The view test is committed first and fails, then the view is committed. Do not squash.
  - The test feeds a budget of 1800 and does not call the budget function.
  - iOS app: open iosApp in Xcode and run iosApp.
  - `./gradlew :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Android onboarding.
  - Changing onboarding math.

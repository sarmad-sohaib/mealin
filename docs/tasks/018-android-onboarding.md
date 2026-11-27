# 018 Android onboarding

Status: Blocked

Depends: 003, 015, 016, 017

Lane: A

Migration queue: no

Owns:
  - `androidApp/src/main/kotlin/com/sarmad/mealin/onboarding/`
  - `androidApp/src/test/kotlin/com/sarmad/mealin/onboarding/`
  - `androidApp/src/main/res/values/strings_onboarding.xml`
  - the onboarding route registration inside `MealNavHost.kt`

Does not touch:
  - `sharedLogic/`
  - `iosApp/`
  - the water screen package except the nav host registration
  - the budget function

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/onboarding/OnboardViewModel.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/onboarding/reviews/ReviewsFragment.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/decisions/2026-11-27-strings-stay-in-the-apps.md`
  - `docs/examples/android-log-water.md`

Outcome:
Onboarding screens render the store and send step intents. The finished state shows the budget number the store already computed. The screen does not multiply BMR.

Why:
Task 015 is the UI pattern, and it is Done before this task is Ready. This task copies that shape and adds one route to the nav host. It does not start a second navigation style.

Done when:
  - The UI test is committed first. It pumps a state that includes a budget of 1800 and asserts that text. A button sends the next intent. The test does not call the budget function.
  - `./gradlew :androidApp:testDebugUnitTest` is green.
  - `./gradlew :androidApp:assembleDebug` is green.

Out of scope:
  - iOS onboarding. Task 019, which may run at the same time.
  - Changing the budget formula.

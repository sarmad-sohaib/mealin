# 021 Android food search and log

Status: Blocked

Depends: 003, 015, 016, 018, 020

Lane: A

Migration queue: no

Owns:
  - `androidApp/src/main/kotlin/com/sarmad/mealin/foodlog/`
  - `androidApp/src/test/kotlin/com/sarmad/mealin/foodlog/`
  - `androidApp/src/main/res/values/strings_food.xml`
  - the food route registration in `MealNavHost.kt`

Does not touch:
  - `sharedLogic/`
  - `iosApp/`
  - `Search.sq`

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/logmeal/AddLogFragment.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/examples/android-log-water.md`

Outcome:
Search results and the log button render store state and send intents. The row's calories are shown from state. The screen does not call `getCalculatedCalories`.

Why:
The water screen is the pattern, and the nav host has a single writer. This task waits until onboarding's route is in the host so the two Android tasks do not edit `MealNavHost.kt` together.

Done when:
  - The UI test is committed first. A fake state with one food named Banana and 89 kcal is visible. The log control emits the log intent. The test does not query SQLite.
  - `./gradlew :androidApp:testDebugUnitTest` is green.
  - `./gradlew :androidApp:assembleDebug` is green.

Out of scope:
  - iOS search. Task 022 may run at the same time.
  - Barcode camera and photo camera.

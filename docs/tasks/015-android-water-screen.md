# 015 Android water screen

Status: Blocked

Depends: 003, 014

Lane: A

Migration queue: no

Owns:
  - `androidApp/src/main/kotlin/com/sarmad/mealin/water/`
  - `androidApp/src/test/kotlin/com/sarmad/mealin/water/`
  - `androidApp/src/main/kotlin/com/sarmad/mealin/navigation/MealNavHost.kt`
  - `androidApp/src/main/res/values/strings_water.xml`
  - `docs/examples/android-log-water.md`

Does not touch:
  - `sharedLogic/` except to call the water store
  - `iosApp/`
  - SQLDelight
  - `MainActivity.kt` only if `AppGraph` does not already call a start composable. Prefer to change the start composable in `MealNavHost.kt`. If the greeting is still the `setContent` target, this task may change that one call in `MainActivity.kt` and must leave the greeting composable and its test in place.

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/watertracking/WaterTrackerFragment.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/watertracking/WaterTrackerViewModel.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/decisions/2026-11-27-strings-stay-in-the-apps.md`

Outcome:
The Android water screen renders the store state and sends the log intent. It is the only Android screen pattern later tasks may copy. `docs/examples/android-log-water.md` shows the state, the intent, and the test. It does not tell anyone to copy CalorieCounter.

Why:
UI tests that recompute millilitres will drift from the store. This screen is the template: a fake store emits state, the test asserts the text, a click captures the intent.

Done when:
  - The UI test is committed first and fails because the screen does not exist. The screen is the next commit. Do not squash.
  - The test does not add container sizes together. It uses a state object that already contains the total.
  - `./gradlew :androidApp:testDebugUnitTest` is green.
  - `./gradlew :androidApp:assembleDebug` is green.
  - The example file exists.

Out of scope:
  - iOS water. Task 016.
  - Any other Android screen. They wait until this task and 016 are Done.

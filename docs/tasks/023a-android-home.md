# 023a Android home

Status: Blocked

Depends: 003, 015, 016, 023, 028a

Lane: A

Migration queue: no

Owns:
  - `androidApp/src/main/kotlin/com/sarmad/mealin/home/`
  - `androidApp/src/test/kotlin/com/sarmad/mealin/home/`
  - `androidApp/src/main/res/values/strings_home.xml`
  - the home route registration in `MealNavHost.kt`

Does not touch:
  - `sharedLogic/`
  - `iosApp/`
  - the home formula

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/home/HomeFragment.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/customview/caloriesdashboard/CalorieDashboard.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/examples/android-log-water.md`

Outcome:
The home screen shows eaten, burned, remaining, meals, and water from store state. It sends intents for the day change and the meal shortcuts. It does not subtract calories.

Why:
`CalorieDashboard` animates numbers it is given. This screen does the same with the store. The nav host is edited only after the recipe route is already registered.

Done when:
  - The UI test is committed first with a state of eaten 100, burned 40, remaining 0. The test asserts those three texts and does not compute them.
  - `./gradlew :androidApp:testDebugUnitTest` is green.
  - `./gradlew :androidApp:assembleDebug` is green.

Out of scope:
  - iOS home. Task 023b may run at the same time.
  - Health Connect.

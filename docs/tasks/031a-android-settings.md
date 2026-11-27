# 031a Android settings

Status: Blocked

Depends: 003, 015, 016, 023a, 031

Lane: A

Migration queue: no

Owns:
  - `androidApp/src/main/kotlin/com/sarmad/mealin/settings/`
  - `androidApp/src/test/kotlin/com/sarmad/mealin/settings/`
  - `androidApp/src/main/res/values/strings_settings.xml`
  - the settings route registration in `MealNavHost.kt`

Does not touch:
  - `sharedLogic/`
  - `iosApp/`
  - unit conversion functions

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/settings/SettingsFragment.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/decisions/2026-11-27-strings-stay-in-the-apps.md`
  - `docs/examples/android-log-water.md`

Outcome:
The settings screen shows the unit and language from state and sends intents. Labels come from Android resources. Changing language does not hard-code a sentence in Kotlin.

Why:
The home route is already in the nav host, so this task is the next single writer. The screen copies the water pattern.

Done when:
  - The UI test is committed first. State says oz. The test asserts the resource text for that state and captures the unit intent. It does not convert millilitres.
  - `./gradlew :androidApp:testDebugUnitTest` is green.
  - `./gradlew :androidApp:assembleDebug` is green.

Out of scope:
  - iOS settings. Task 031b may run at the same time.
  - Paywalls and ad-free purchase rows from the legacy settings screen.

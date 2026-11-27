# 012 Activity calories

Status: Blocked

Depends: 010

Lane: S

Migration queue: no

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/activitycalories/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/activitycalories/`

Does not touch:
  - `com.sarmad.mealin.budget`
  - `com.sarmad.mealin.nutrition` except to call a conversion if one exists
  - the activity log table. Task 025 owns that migration.
  - Gradle files

Legacy, read only:
  - `data/src/main/java/com/qg/caloriecounter/data/local/dao/ActivitiesDao.kt`

Decisions:
  - `docs/decisions/2026-11-27-test-strategy.md`

Outcome:
A pure function returns activity calories for a duration, a MET value, and a list of weight logs. Calories are `mets * (weightLb * 0.45359237) * (hours)`. Hours are `durationMinutes / 60`. Weight is the latest log on the same calendar day or earlier with weight greater than 0, else the latest non-zero weight, else 154 lb.

Why:
The shipping query in `ActivitiesDao` is the behavior. The 154 fallback is pounds, then converted with `0.45359237`, not with the BMI constant and not with `2.20462`. Health Connect calories are not added. This package has no SQL. Task 025 calls this function when it sums a day.

Done when:
  - The failing fixtures are committed first. They include a same-day weight, an earlier-day weight, a later-day weight that must be ignored, a zero weight that must be ignored, and the 154 lb fallback. Do not squash the test commit away.
  - One fixture computes the product by hand in the test name or the assertion comment so the constant is visible.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Reading `activityLevels` from the catalog. Task 025 does that.
  - The home screen. Burned calories stay out of `caloriesLeft`.

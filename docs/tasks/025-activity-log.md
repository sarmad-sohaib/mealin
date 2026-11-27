# 025 Activity log

Status: Blocked

Depends: 009, 012, 024

Lane: C

Migration queue: step 6, after 024. This migration creates `activityLog` only. Activities and levels stay in the catalog.

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/activitylog/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/activitylog/`
  - the activity-log migration only
  - the `DayBurn` argument passed into the home store at its production call site, and no other line in that file

Does not touch:
  - `activitycalories` formula except to call it
  - catalog rows
  - home UI layout
  - `MealNavHost.kt` and `MealNav.swift`

Legacy, read only:
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetActivitiesUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetActivityLevelsForActivityUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/InsertActivityLogUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/DeleteLoggedActivityUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetDailyActivitySummaryUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetTotalCaloriesBurnedForTodayUseCase.kt`
  - `data/src/main/java/com/qg/caloriecounter/data/local/dao/ActivitiesDao.kt`

Decisions:
  - `docs/decisions/2026-11-27-test-strategy.md`
  - `docs/decisions/2026-11-27-offline-catalog-and-logs.md`

Outcome:
The user can log an activity level for a duration. The day's burned calories come from task 012. The catalog supplies the 198 activities and their MET values. `ActivityDayBurn` replaces `ZeroDayBurn` at the home store's production call site so the home field is the domain total. Health Connect is not added.

Why:
The MET formula is already tested. This task is the table, the catalog read, and the wiring into the home store's burn port. Editing the home layout would collide with tasks 023a and 023b.

Done when:
  - The failing test is committed first: a log with a known MET, duration, and weight returns the task 012 result, and a missing weight uses the 154 lb path through that function. Do not squash.
  - A test reads activities from the three-food fixture's database only if 008 did not include activities. If the fixture has no activities, add an activity row in this task's test database, not in `catalog/catalog.db`.
  - One test opens the real catalog and expects 198 activities. That assertion can live next to task 009's smoke test only by calling it, not by editing task 009's file. Prefer a new test in this package.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - A new screen. Home already has a burned field.
  - Building FTS or editing the export.

# 010 Nutrition, BMI, units, and dates

Status: Blocked

Depends: 006

Lane: S

Migration queue: no

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/nutrition/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/nutrition/`

Does not touch:
  - `sharedLogic/build.gradle.kts`
  - `gradle/libs.versions.toml`
  - databases, stores, and UI
  - `com.sarmad.mealin.budget` and `com.sarmad.mealin.activitycalories` (tasks 011 and 012)

Legacy, read only:
  - `common/src/main/java/com/qg/caloriecounter/common/NutritionCalculations.kt`
  - `common/src/main/java/com/qg/caloriecounter/common/BmiCalculations.kt`
  - `common/src/main/java/com/qg/caloriecounter/common/Units.kt`
  - `common/src/main/java/com/qg/caloriecounter/common/DateMathExtensions.kt`

Decisions:
  - `docs/decisions/2026-11-27-test-strategy.md`
  - `docs/decisions/2026-11-27-kotlinx-datetime-serialization-ktor.md`

Outcome:
The pure functions in CalorieCounter `:common` are reimplemented from fixtures. `commonMain` does not import `java.util`, `android`, or `androidx`. Date results are integers and pairs, not `java.util.Date`. `DispatcherProvider` is not ported. `Status` is not ported. Task 013 has sealed errors and kotlinx-datetime.

Keep each conversion constant with the function that uses it. Do not pick one constant for pounds. `BmiCalculations` uses `0.453592f`. `lbsToKg` divides by `2.20462`. Water uses `29.5735` ml per oz.

Why:
`:common` has no tests. These functions are the calorie, BMI, and unit results the rest of the app will call once. Copying the files would bring `SimpleDateFormat` into `commonMain`, which does not compile for iOS.

Done when:
  - The failing fixtures are committed first, one commit, tests red. The implementation is a later commit. Do not squash.
  - Fixtures cover `getCalculatedCalories`, protein, fat, carbohydrates, `getQuantityCalculated`, BMI from cm and lb including null, `percentOf` of 0, each unit conversion, `formatDateToInt` as `ddMMyyyy`, and the rounding helpers including NaN.
  - A test shows the BMI constant and the `2.20462` constant are not treated as the same function.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - The onboarding calorie budget. That is task 011.
  - Activity calorie totals. That is task 012.
  - Adding a Gradle dependency. Date results are the same integers and pairs the legacy functions return, computed without `java.util.Date`. Task 013 introduces kotlinx-datetime for clocks and instants. This task does not edit `gradle/libs.versions.toml`.

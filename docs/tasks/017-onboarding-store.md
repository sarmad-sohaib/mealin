# 017 Onboarding store

Status: Blocked

Depends: 011, 013, 014

Lane: C

Migration queue: step 3, after 014. This migration creates profile and preference rows: units, language code, calorie budget, water target, height, age, gender, and the target weight. It does not create `WeightLog`. That is task 024.

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/onboarding/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/onboarding/`
  - the SQLDelight migration for the profile tables only

Does not touch:
  - `com.sarmad.mealin.budget` except to call the budget function
  - water UI and the water store's public API, other than writing the water goal the water store already reads
  - Android and iOS screens

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/onboarding/OnboardViewModel.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/user/InsertUserUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/user/InserUserPrefUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/user/GetCurrentUserUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/user/UpdateUserUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/user/UpdateUserDetailsUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/InsertWeightGoalUseCase.kt`
  - `common/src/main/java/com/qg/caloriecounter/common/Units.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/decisions/2026-11-27-clock-ids-locale-passed-in.md`

Outcome:
The onboarding store walks the steps and persists a profile, including the selected `AverageDay` activity level. `InsertActivityLevelLogUserCase` and `GetLatestActivityLogUseCase` are that current level, not the exercise log. Do not add a second migration for a history table. Calorie budget comes from task 011. Water target and target weight are characterized from `OnboardViewModel`, not reinvented.

Water, from `calculateDailyWaterMl`: base 35 ml per kg for male and 30 for female, times 1.0 seated, 1.1 lightly active, 1.2 moderate, 1.3 mostly walking, otherwise 1.2, then clamped to 1500 through 4000.

Target weight, from `calculateTargetWeight`: center BMI 21.7, default height 162 cm female and 175 cm male (the entered height is not what this function uses), default weight 172 lb male and 139 lb female when the log is missing or zero, `kgPerLb` 0.45359237. Lose moves toward the center or 2 kg down. Gain moves toward the center or 2 kg up. Otherwise the target is the current weight. The function returns pounds.

Why:
Those two formulas live in the Android view model, not in `:common`. Copying `OnboardViewModel` would pull Android into shared code. The store is the place the Android and iOS onboarding screens both call.

Done when:
  - Failing fixtures for water target, target weight, and a finished profile are committed first. Do not squash.
  - The water fixture includes the 1500 and 4000 clamps.
  - The target-weight fixture records that height input is ignored and the gender default is used.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - The screens. Tasks 018 and 019.
  - Weigh-in history. Task 024.

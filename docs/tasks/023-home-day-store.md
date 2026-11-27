# 023 Home day store

Status: Blocked

Depends: 014, 020

Lane: S

Migration queue: no. Read water totals and meal logs. Do not add a table.

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/home/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/home/`

Does not touch:
  - SQLDelight migrations
  - Android and iOS UI
  - the activity log table. Burned calories come from an interface. Until task 025 exists, the tests pass a fake that returns a number, and production passes 0 from a temporary adapter that 025 replaces. Do not put that zero inside the home formula.

Legacy, read only:
  - `domain/src/main/java/com/qg/caloriecounter/domain/model/DashboardData.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/home/HomeViewModel.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/home/HomeFragment.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetConsumptionByDateUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetCaloriesBudgetUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetTotalCaloriesBurnedForTodayUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetCalendarDataUseCase.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/decisions/2026-11-27-no-store-and-health-services.md`

Outcome:
The home store exposes eaten, burned, remaining, meal totals, and water for one day. Remaining follows `DashboardData`: `calorieBudget - caloriesConsumed`. The value the UI shows is that number when it is positive, otherwise 0. The floor lives in the store so the apps do not subtract. Burned is not added into remaining. Burned does not include Health Connect.

Macro gram budgets follow `HomeViewModel.calculateNutrients`. Protein share is 0.35 lose, 0.2 maintain, 0.3 otherwise, divided by 4. Carb share is 0.35 lose, 0.5 maintain, 0.4 otherwise, divided by 4. Fat share is 0.3, divided by 9.

Why:
The fragment currently floors the remainder and adds Health Connect to burn. Health Connect is dropped. The subtraction moves into the store so both apps show the same number.

Done when:
  - Failing fixtures are committed first: remaining 500 when budget is 2000 and eaten is 1500, remaining 0 when eaten exceeds the budget, burned stays a separate field, and one macro case for lose weight. Do not squash.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - The screens. Tasks 023a and 023b.
  - Writing activity rows. Task 025.

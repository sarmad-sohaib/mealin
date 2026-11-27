# 024 Weight and weigh-in

Status: Blocked

Depends: 008, 011, 013, 020

Lane: C

Migration queue: step 5, after 020. This migration creates `WeightLog` and `WeightGoal`. Do not start it while another migration task is In progress. 008 is already Done. The queue dependency that matters is 020.

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/weight/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/weight/`
  - the weight migration only

Does not touch:
  - the meal-log migration and `Search.sq`
  - the budget function except to read the stored budget when a goal needs it
  - UI

Legacy, read only:
  - `domain/src/test/kotlin/com/qg/caloriecounter/domain/usecase/weighttracking/WeighInUseCaseTest.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/WeighInUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/GetCurrentWeightUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/GetWeightReportUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/GetCurrentBmiUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/SetWeightGoalCompletedUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/SetMaintainWeightUserCase.kt`
  - `common/src/main/java/com/qg/caloriecounter/common/BmiCalculations.kt`

Decisions:
  - `docs/decisions/2026-11-27-test-strategy.md`
  - `docs/decisions/2026-11-27-clock-ids-locale-passed-in.md`

Outcome:
Goal completion and the switch to a maintain goal are outcomes of this store. Weigh-in uses the injected clock. Outcomes match `WeighInUseCaseTest`: failure when the update path has no timestamp, failure when the write count is 0, success when the write count is positive, and the goal id on the log is the one the caller passed. Assert those outcomes. Do not assert that a private function was called, and do not copy the Hilt class. BMI uses task 010's function. The timestamp is the clock's value, not `System.currentTimeMillis()`.

`GetWeightChangeWithTimeUseCase` is an empty interface. Do not invent a chart series for it.

Why:
The legacy test is the only real domain test, and it checks status codes around a repository. The rewrite keeps those outcomes and drops the cast to a private method. Weight has its own migration so it is not edited inside the meal-log files.

Done when:
  - The failing outcome tests are committed first. Do not squash.
  - A BMI test uses centimetres and pounds through the nutrition function and does not reimplement the division.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Android and iOS screens. There is no separate weight-screen task in this plan. The home card reads this store later. Do not build a screen here.
  - Activity calorie fallback. Task 012 and task 025 consume the logs this task writes.

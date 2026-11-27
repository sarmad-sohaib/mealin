# 011 Calorie budget

Status: Blocked

Depends: 010

Lane: S

Migration queue: no

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/budget/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/budget/`

Does not touch:
  - `com.sarmad.mealin.nutrition` except to call it
  - `com.sarmad.mealin.activitycalories`
  - databases, Gradle files, and UI

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/onboarding/reviews/ReviewsFragment.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/onboarding/CreatingPlanFinalFragment.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/onboarding/CreatingPlanProgressFragment.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/onboarding/part2/WeightGoalStep4Fragment.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/onboarding/part2/WeightGoalHowFastFragment.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/onboarding/OnboardViewModel.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/MakeNewCalorieBudgetUseCase.kt`
  - `common/src/main/java/com/qg/caloriecounter/common/Units.kt`

Decisions:
  - `docs/decisions/2026-11-27-test-strategy.md`

Outcome:
A pure function turns onboarding inputs into the calorie budget the finished flow stores. The test names which legacy function that number matches.

Why:
`MakeNewCalorieBudgetUseCase` multiplies BMR by 1.9 for every goal string, defaults weight to 70 and age to 24, and is not what `ReviewsFragment` writes. `ReviewsFragment` uses Mifflin-St Jeor with weight default 154 lb, height default 175 cm, age default 25, and `AverageDay` multipliers 1.2, 1.374, 1.55, 1.725, 1.9. Other fragments hardcode 1.55. The planning session did not trace the nav graph, so this task finds the last write on the path that finishes onboarding and locks that result. Do not copy the use-case class.

Male BMR shape used by the reviews step: `(10 * (weightLb / 2.20462)) + (6.25 * heightCm) - (5 * age) + 5`, then times the activity multiplier. Female uses `- 161` instead of `+ 5`. A missing gender in the domain use case emits 1000. Confirm whether the finish path can hit that before asserting it.

Done when:
  - The failing fixtures are committed first, red, then the function, green. Do not squash.
  - The test states the legacy function it matches, and it includes one male and one female case plus the 154 lb default.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Persisting the budget. Task 017 does that.
  - Water target and target weight. Those formulas live in `OnboardViewModel` and belong to task 017.
  - Macro gram splits. Those belong to task 023.

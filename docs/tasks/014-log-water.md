# 014 Log water

Status: Blocked

Depends: 008, 013

Lane: C

Migration queue: step 2, after 008. The next migration creates `WaterContainer` and `WaterLog` and seeds the nine containers. No other migration task is Ready while this one is.

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/water/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/water/`
  - the SQLDelight files for `WaterContainer` and `WaterLog` only

Does not touch:
  - catalog `.sq` schema owned by 008, other than adding these two user tables in a new migration
  - `com.sarmad.mealin.seam` except to take the clock in a constructor
  - Android and iOS UI
  - `docs/examples/`. Tasks 015 and 016 write the UI examples.

Legacy, read only:
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/LogWaterIntakeUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/GetWaterConsumedTodayUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/CalculateWaterLeftTodayUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/GetGlassSizeUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/SetGlassSizeUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/GetDailyWaterGoalUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/SetDailyWaterGoalUseCase.kt`
  - `data/src/main/java/com/qg/caloriecounter/data/local/entity/waterlog/WaterContainer.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/decisions/2026-11-27-sqldelight-for-both-databases.md`
  - `docs/decisions/2026-11-27-clock-ids-locale-passed-in.md`

Outcome:
The water store is the pattern later stores copy. It loads the nine containers, logs one container's millilitres against a date from the injected clock, and exposes immutable state through a `StateFlow`. Intents select a container and log water. The repository is an interface. The SQLDelight implementation is a separate type the store does not import.

Seed, in id order, sizes in millilitres: Water Dispenser Cup 200, Small Glass 250, Tall Glass 300, Large Mug 350, Standard Water Bottle 450, Juice Bottle 500, Sports Bottle 600, Large Water Bottle 750, Travel Bottle 800. These rows came from `test.db`. They are editable after seeding.

Why:
Every later screen copies this shape: interface, store, intents, test. Water is the smallest log that still has a seed and a day total. Putting the seed in this migration makes the containers exist on a fresh user database without reading the catalog.

Done when:
  - The failing store test is committed first. It sends a log intent and expects the consumed total to increase by that container's size. Do not squash.
  - A test expects the nine names and sizes after migration.
  - The store test uses a fake repository. A second test uses the SQLDelight driver and does not go through the UI.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Compose and SwiftUI. Tasks 015 and 016.
  - The onboarding water-target formula. Task 017 stores the goal. This task reads the goal it is given. Default the goal to 0 until 017 writes one, and do not invent 2000.

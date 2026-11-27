# 020 Food search and meal log

Status: Blocked

Depends: 009, 010, 013, 017

Lane: C

Migration queue: step 4, after 017. This migration creates `MealLog`, `MealFoodLog`, and recents. This task also owns the catalog search `.sq` file. It does not edit the catalog schema task 008 created, except to add queries in a new `.sq` file.

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/foodlog/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/foodlog/`
  - `sharedLogic/src/commonMain/sqldelight/com/sarmad/mealin/db/Search.sq`
  - the meal-log and recents migration only

Does not touch:
  - `catalog/catalog.db`
  - the water, profile, or weight migrations
  - `com.sarmad.mealin.nutrition` except to call `getCalculatedCalories` and the macro helpers
  - UI

Legacy, read only:
  - `data/src/main/java/com/qg/caloriecounter/data/local/dao/SearchDao.kt`
  - `data/src/main/java/com/qg/caloriecounter/data/local/entity/meallog/MealFoodLog.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/SearchLocalFoodPageUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/SearchFoodUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/LogMealUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/GetFoodMeasurementsByIdUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/GetDbFoodNutritionDetailUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetConsumptionByDateUseCase.kt`

Decisions:
  - `docs/decisions/2026-11-27-offline-catalog-and-logs.md`
  - `docs/decisions/2026-11-27-test-strategy.md`
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`

Outcome:
Search is offline. The ranking matches `SearchDao.searchFoodPage` against the three-food fixture from task 008. One additional test opens the real catalog and asserts that `banana` returns food id 2. Logging a meal writes calories, protein, fat, carbohydrates, quantity, and serving id on the row, computed by task 010's functions at log time. `foodId` is stored as a number. There is no foreign key to `catalog.db`.

Ranking to characterize, not to paste blindly: exact name is rank 0. A name that continues with a comma or a space, or an FTS offset at the start of the name, is rank 1. A name that starts with the query is rank 2. Anything else that matched FTS is rank 3. Then shorter names, then the name, then the id. Read `SearchDao.kt` for the dedup rules before writing the fixture expectations. For the query `banana` on the three-food fixture, check the ranks yourself from that SQL and lock the order in the test.

`SearchFoodUseCase.searchApi` is not ported.

Why:
The meal row has to keep the macros from the moment of logging. A later catalog edit must not change history. The ranking is the shipping search behavior, and the three-food fixture makes it fast. The real-file assertion is one check that the export and the query agree on the banana.

Done when:
  - The failing ranking test and the failing snapshot test are committed first. Do not squash.
  - The fixture order is asserted. The real catalog test asserts food id 2 for `banana` and does not scan all 100,000 names in Kotlin.
  - A logged row still shows the original calories after the test changes the catalog food's calories in the fixture only.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Screens. Tasks 021 and 022.
  - Custom foods. Task 026.
  - Barcode network. Task 027.
  - Remote name search.

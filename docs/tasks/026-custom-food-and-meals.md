# 026 Custom food and custom meals

Status: Blocked

Depends: 020, 025

Lane: C

Migration queue: step 7, after 025. This migration creates custom food and custom meal tables in the user database. Catalog ids 1 through 100000 stay untouched.

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/customfood/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/customfood/`
  - the custom-food migration only

Does not touch:
  - `Search.sq` and the meal-log migration
  - `catalog/catalog.db`
  - UI

Legacy, read only:
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/InsertCustomMealUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetCustomMealsUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetCustomMealDetailUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/DeleteCustomMealUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/InsertFoodUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/InsertFoodMeasurementUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/InsertNutritionDetailsUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/wrapper/CustomMealUseCasesWrapper.kt`

Decisions:
  - `docs/decisions/2026-11-27-offline-catalog-and-logs.md`
  - `docs/decisions/2026-11-27-clock-ids-locale-passed-in.md`

Outcome:
Creating a food assigns an id from the injected generator, and the first id is 100001. A test that forces the generator to return 50 fails the insert. Custom meals store their foods and the macro snapshot the same way a meal log does. The Hilt wrapper classes are not copied.

Why:
The catalog is read-only and already uses ids 1 through 100000. A user-database autoincrement that starts at 1 would reuse those ids as logical `foodId`s. The generator makes the collision testable.

Done when:
  - The failing id test and a custom-meal snapshot test are committed first. Do not squash.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Screens. The food-log screens can gain a create button only in a later task that owns those UI files. This task does not edit them.
  - Barcode inserts. Task 027 calls this package's interface.

# 028 Recipes

Status: Blocked

Depends: 013

Lane: S

Migration queue: no. Do not edit user-database migrations or the Ktor client setup.

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/recipes/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/recipes/`
  - recorded JSON under `sharedLogic/src/commonTest/resources/recipes/`

Does not touch:
  - `com.sarmad.mealin.seam` engine construction
  - SQLDelight migrations
  - bookmark tables. Task 029 owns that migration.
  - UI

Legacy, read only:
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/recipe/GetRecipesUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/recipe/GetViewRecipeDetailUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/recipe/RecipeQueryParams.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/recipeFilter/GetRecipeFiltersUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetRecipesUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/model/recipe/RecipeResponse.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/model/recipe/ViewRecipeDetailResponse.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/model/recipeFilter/RecipeFiltersResponse.kt`

Decisions:
  - `docs/decisions/2026-11-27-kotlinx-datetime-serialization-ktor.md`
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`

Outcome:
A recipe store lists, filters, and loads detail from the Ktor client task 013 created. Tests use `MockEngine` and checked-in JSON for a list, a filter set, and a detail body. The catalog `Recipe` table is empty and is not the source.

Why:
Recipes are one of the three network features. They do not need a user migration, so this task can run beside the diary once the client exists. The see-all use case and the recipe-package use case are both read. The store exposes one list operation, not two copied classes.

Done when:
  - The failing `MockEngine` tests are committed first. Do not squash.
  - Filter and detail assertions use fields from the fixture, not a live server.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Bookmarks. Task 029.
  - Android and iOS browse screens. Tasks 028a and 028b. Bookmarks stay in task 029.

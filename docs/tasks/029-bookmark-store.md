# 029 Bookmark store

Status: Blocked

Depends: 026, 028

Lane: C

Migration queue: step 8, after 026. This migration creates the bookmark table. 028 is not a migration. The queue dependency is 026.

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/bookmark/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/bookmark/`
  - the bookmark migration only

Does not touch:
  - the recipe client setup
  - custom-food tables
  - UI

Legacy, read only:
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/mealRecipe/UpsertBookmarkUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/mealRecipe/GetBookmarksUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/mealRecipe/GetBookmarkByIdUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/model/recipe/BookmarkedRecipe.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/bottomnav/recipes/BookmarkAdapter.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`

Outcome:
Bookmarking emits a stub row immediately, with the id, the title if the list had one, and detail fields empty. When detail arrives, that same id is replaced with the detail. A failed detail leaves the stub and an error code. Legacy code writes a full `BookmarkedRecipe` in one call. Do not wait for detail before the stub is visible to a collector.

Why:
The product contract is the stub, then the detail. The adapter in CalorieCounter binds whatever row is already stored. The test has to observe the intermediate state or the stub does not exist.

Done when:
  - The failing test is committed first. A collector sees the stub before the `MockEngine` returns detail, then sees the replacement. Do not squash.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - The screens. Tasks 029a and 029b.
  - Recipe list parsing. Call task 028's store or client. Do not copy it.

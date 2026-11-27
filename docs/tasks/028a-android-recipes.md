# 028a Android recipes

Status: Blocked

Depends: 003, 015, 016, 021, 028

Lane: A

Migration queue: no

Owns:
  - `androidApp/src/main/kotlin/com/sarmad/mealin/recipes/`
  - `androidApp/src/test/kotlin/com/sarmad/mealin/recipes/`
  - `androidApp/src/main/res/values/strings_recipes.xml`
  - the recipe route registration in `MealNavHost.kt`

Does not touch:
  - `sharedLogic/`
  - `iosApp/`
  - the bookmark migration

Legacy, read only:
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/recipe/GetRecipesUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/recipe/GetViewRecipeDetailUseCase.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/examples/android-log-water.md`

Outcome:
Browse, filter, and detail render the recipe store and send intents. The test uses a fake store state. It does not parse JSON and it does not bookmark. Bookmarking is task 029a.

Why:
Task 028 is the shared client. Putting both apps in that task would exceed one screen and would edit both nav hosts. This task is the next Android nav writer after food search.

Done when:
  - The UI test is committed first. A list state shows one title, a filter intent is captured, and a detail state shows that title. Do not squash.
  - `./gradlew :androidApp:testDebugUnitTest` is green.
  - `./gradlew :androidApp:assembleDebug` is green.

Out of scope:
  - iOS recipes. Task 028b may run at the same time.
  - The optimistic bookmark stub.

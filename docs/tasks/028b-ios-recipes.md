# 028b iOS recipes

Status: Blocked

Depends: 004, 015, 016, 022, 028

Lane: I

Migration queue: no

Owns:
  - `iosApp/iosApp/Recipes/`
  - the recipe route registration in `MealNav.swift`
  - the recipe keys in `Localizable.strings`

Does not touch:
  - `androidApp/`
  - `sharedLogic/`
  - the bookmark migration

Legacy, read only:
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/recipe/GetRecipesUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/recipe/GetViewRecipeDetailUseCase.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/examples/ios-log-water.md`
  - the Swift interop decision from task 004

Outcome:
SwiftUI browse, filter, and detail render store state and send intents. The view does not parse JSON.

Why:
This is the iOS half of recipe browse. It can run beside 028a. It waits for 022 so `MealNav.swift` has one writer.

Done when:
  - The view test is committed first with a list title and a detail title.
  - iOS app: open iosApp in Xcode and run iosApp.
  - `./gradlew :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Android recipes.
  - Bookmarks.

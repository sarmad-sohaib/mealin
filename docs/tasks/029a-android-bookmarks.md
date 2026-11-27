# 029a Android bookmarks

Status: Blocked

Depends: 003, 015, 016, 029, 031a

Lane: A

Migration queue: no

Owns:
  - `androidApp/src/main/kotlin/com/sarmad/mealin/bookmark/`
  - `androidApp/src/test/kotlin/com/sarmad/mealin/bookmark/`
  - `androidApp/src/main/res/values/strings_bookmark.xml`
  - the bookmark route registration in `MealNavHost.kt`

Does not touch:
  - `sharedLogic/`
  - `iosApp/`
  - the bookmark migration

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/bottomnav/recipes/BookmarkAdapter.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/examples/android-log-water.md`

Outcome:
The bookmark screen shows the stub title as soon as the state contains it, then shows the detail fields when the state replaces the stub. The test sends the bookmark intent. It does not parse recipe JSON.

Why:
The nav host is free only after settings has registered its route. This screen copies the water UI pattern.

Done when:
  - The UI test is committed first with two states: a stub, then a detailed row. Do not squash.
  - `./gradlew :androidApp:testDebugUnitTest` is green.
  - `./gradlew :androidApp:assembleDebug` is green.

Out of scope:
  - iOS bookmarks. Task 029b may run at the same time only if its Depends are Done. It does not share this nav file.
  - Recipe browsing beyond the bookmark row.

# 029b iOS bookmarks

Status: Blocked

Depends: 004, 015, 016, 029, 031b

Lane: I

Migration queue: no

Owns:
  - `iosApp/iosApp/Bookmark/`
  - the bookmark route registration in `MealNav.swift`
  - the bookmark keys in `Localizable.strings`

Does not touch:
  - `androidApp/`
  - `sharedLogic/`
  - the bookmark migration

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/bottomnav/recipes/BookmarkAdapter.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/examples/ios-log-water.md`
  - the Swift interop decision from task 004

Outcome:
SwiftUI shows the stub, then the detail, from store state. It sends the bookmark intent.

Why:
Same pattern as iOS water. This task can run beside 029a. It waits for 031b so `MealNav.swift` has one writer.

Done when:
  - The view test is committed first with a stub state and a detail state.
  - iOS app: open iosApp in Xcode and run iosApp.
  - `./gradlew :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Android bookmarks.
  - A recipe browse screen.

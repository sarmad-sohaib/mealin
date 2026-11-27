# 032a Android journeys

Status: Blocked

Depends: 015, 018, 021, 029a

Lane: A

Migration queue: no

Owns:
  - `androidApp/src/androidTest/kotlin/com/sarmad/mealin/journey/`

Does not touch:
  - production source under `androidApp/src/main/`
  - `sharedLogic/`
  - `iosApp/`
  - SQLDelight and the catalog file

Legacy, read only:
  - none

Decisions:
  - `docs/decisions/2026-11-27-test-strategy.md`
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`

Outcome:
Three instrumented journeys, and nothing else. Finish onboarding. Log a food while the recipe and barcode engines are offline, using the fixture catalog or the real bundled catalog, and assert the logged row shows the snapshot the store already produced. Bookmark a recipe against a local fake client, and assert the stub appears before the detail.

The tests drive the UI. They do not recompute BMR, search rank, or macro math. If a journey cannot reach a screen without a product change, stop and report that. Do not edit production code in this task.

Why:
The screens exist. These journeys are the check that the stores are wired. A unit test of a single screen does not walk from onboarding to a saved meal.

Done when:
  - The three tests are committed before any test-only helper they need. Do not squash a production fix into this task. There should be no production fix.
  - `./gradlew :androidApp:connectedDebugAndroidTest` is green on a device or emulator. If none is available, say so in the work log and do not mark the task Done.
  - `./gradlew :androidApp:assembleDebug` is green.

Out of scope:
  - iOS journeys. Task 032b.
  - New features found along the way.

# 032b iOS journeys

Status: Blocked

Depends: 016, 019, 022, 029b

Lane: I

Migration queue: no

Owns:
  - `iosApp/iosAppUITests/`

Does not touch:
  - `iosApp/iosApp/` production views
  - `androidApp/`
  - `sharedLogic/`
  - the catalog file

Legacy, read only:
  - none

Decisions:
  - `docs/decisions/2026-11-27-test-strategy.md`
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - the Swift interop decision from task 004

Outcome:
Three UI journeys on the simulator. Finish onboarding. Log a food with the network off and assert the snapshot on screen. Bookmark a recipe and assert the stub appears before the detail. The tests do not compute calories.

Why:
The iOS screens are a second client of the same stores. A green Android journey does not prove the SwiftUI wiring.

Done when:
  - The UI test target exists and the three tests are committed before any helper. Do not change production Swift to make them pass. If a screen is unreachable, stop and report it.
  - iOS app: open iosApp in Xcode and run the `iosAppUITests` scheme on a simulator.
  - `./gradlew :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Android journeys.
  - New product behavior.

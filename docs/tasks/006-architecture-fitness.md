# 006 Architecture fitness tests

Status: Ready

Depends: none

Lane: S

Migration queue: no

Owns:
  - `sharedLogic/src/androidHostTest/kotlin/com/sarmad/mealin/architecture/`
  - `sharedLogic/expect-actual-allowlist.txt`

Does not touch:
  - `commonMain` product code
  - `androidApp/`, `iosApp/`, `sharedUI/`
  - `gradle/libs.versions.toml`

Legacy, read only:
  - none

Decisions:
  - `docs/decisions/2026-11-27-expect-actual-catalog-copy.md`
  - `docs/decisions/2026-11-27-test-strategy.md`

Outcome:
An Android host test reads the repo and fails when `commonMain` imports `android`, `androidx`, or a platform SDK, and when an `expect` or `actual` declaration sits outside `sharedLogic/expect-actual-allowlist.txt`. The allowlist starts with the wizard sample `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/Platform.kt` and its matching actuals. That sample is not a product exception. Task 009 appends the catalog-copy file when it adds that declaration.

Why:
Feature packages do not exist yet. A scanner that lands first is the check later tasks cannot bypass by skipping a code review. The scan reads the working tree, so it is a host test, not a `commonTest`. The iOS simulator test cannot see the source files.

Done when:
  - The failing test is committed first: a temporary illegal import or `expect` fails the test. The next commit removes the temporary violation and the test passes. Do not squash those commits.
  - `sharedLogic/expect-actual-allowlist.txt` lists one relative path per line and includes the wizard `Platform.kt` plus the existing `androidMain` and `iosMain` actual files for `getPlatform`.
  - `./gradlew :sharedLogic:testAndroidHostTest` is green.
  - `./gradlew :sharedLogic:iosSimulatorArm64Test` is green. This task does not have to make the scanner itself run on the simulator.

Out of scope:
  - Removing `getPlatform`.
  - Adding the catalog-copy `expect`. That is task 009.

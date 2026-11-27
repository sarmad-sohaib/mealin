# 004 Swift interop spike

Status: Ready

Depends: none

Lane: S

Migration queue: no

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/spike/`
  - `sharedLogic/src/iosMain/kotlin/com/sarmad/mealin/spike/`
  - the decision file this task creates under `docs/decisions/`

Does not touch:
  - `androidApp/`
  - `iosApp/`
  - `sharedUI/`
  - SQLDelight, the catalog, and product packages

Legacy, read only:
  - none

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - the new file this task writes, after the spike, not before

Outcome:
A throwaway sample in `:sharedLogic` exercises the official Kotlin Swift export and SKIE against a sealed class, a `StateFlow`, a suspend function, and a default argument. The decision records which one iOS UI tasks will use to collect a store. The throwaway is deleted unless the decision needs the sample in the tree.

Why:
iOS screens collect a `StateFlow`. Guessing whether Swift export or SKIE can see a sealed class and a default argument would make every later iOS task redo the same experiment. This task writes that down.

Done when:
  - The decision file has Decision, Why, Other ways, Stood on, and Supersedes. Other ways includes both interop tools, with the result of the spike for each of the four shapes.
  - `./scripts/regenerate-decision-index.sh` has been run.
  - If the sample was removed, no `com.sarmad.mealin.spike` package remains.
  - `./gradlew :sharedLogic:linkDebugFrameworkIosSimulatorArm64` is green.
  - `./gradlew :sharedLogic:iosSimulatorArm64Test` is green.
  - The decision names the Gradle tasks that were actually run for each tool.

Out of scope:
  - Building the water screen or any product store.
  - Changing `iosApp` beyond what the spike needs, and the spike should not need it. The sample stays inside `sharedLogic`.

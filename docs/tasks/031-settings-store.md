# 031 Settings store

Status: Blocked

Depends: 017

Lane: S

Migration queue: no. Update the profile rows task 017 created. Do not add a table.

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/settings/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/settings/`

Does not touch:
  - SQLDelight migrations
  - Android resources and `Localizable.strings` except by returning a language code
  - UI

Legacy, read only:
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/SetHeightUnitUseCaseImpl.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetHeightUnitUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetHeightUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/SetWeightUnitUseCaseImpl.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/GetWeightUnitUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/SetWaterUnitUseCaseImpl.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/GetWaterUnitUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/user/UpdateUserDetailsUseCase.kt`

Decisions:
  - `docs/decisions/2026-11-27-strings-stay-in-the-apps.md`
  - `docs/decisions/2026-11-27-clock-ids-locale-passed-in.md`
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`

Outcome:
The settings store persists weight unit, water unit, height unit, profile fields, and a language code. It returns codes. It does not return translated sentences. Unit conversion of a number still calls task 010. This store only stores the selected unit.

Why:
Both apps apply the language code to their own resource files. A shared string table would make a test assert English. The legacy use cases write a unit string through a repository. The store keeps that outcome without the callback-shaped API (`result: (String) -> Unit`).

Done when:
  - The failing test is committed first. An intent sets water unit to oz and the next state reads oz. A language intent stores `es` and the state does not contain a translated word. Do not squash.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - The screens. Tasks 031a and 031b.
  - Remote Config language flags.

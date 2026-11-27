# 030 Food recognizer

Status: Blocked

Depends: 020

Lane: S

Migration queue: no. A recognized food is logged through task 020 or saved through task 026. This task does not add a table.

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/recognizer/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/recognizer/`
  - one recorded response under `sharedLogic/src/commonTest/resources/recognizer/`

Does not touch:
  - camera UI
  - the Ktor engine setup
  - SQLDelight migrations
  - `Search.sq`

Legacy, read only:
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/DescribeFoodUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetAiFoodDetailsUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetAllScannedMealsUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/repository/FoodScannerRepository.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/exception/FoodScannerExceptions.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/describeMeal/DescribeFoodFragment.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/camera/CameraFragment.kt`

Decisions:
  - `docs/decisions/2026-11-27-kotlinx-datetime-serialization-ktor.md`
  - `docs/decisions/2026-11-27-strings-stay-in-the-apps.md`
  - `docs/decisions/2026-11-27-no-store-and-health-services.md`

Outcome:
`FoodRecognizer` accepts a photo payload or a text prompt and returns food rows or an error code. One `MockEngine` fixture is the recorded response. The interface is what the apps will call. Remote Config does not gate it. `GetAllScannedMealsUseCase` lists custom meals in the legacy app. Do not add a scan table. A saved scan is a meal log from task 020 with a source code on the row if 020's table already has a source column. If it does not, return the foods to the caller and do not migrate `MealFoodLog` in this task.

Why:
Photo and text share one network boundary and one test fixture. The camera stays in the app so `commonMain` does not import a platform SDK. The legacy screens are also gated by Remote Config. That gate is dropped.

Done when:
  - The failing fixture test is committed first. Text describe and a photo payload both decode the same recorded body, or the test documents one of them if the legacy API uses one endpoint. Read the two use cases and follow the one that returns food rows. Do not squash.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Camera UI. Tasks 030a and 030b.
  - Turning the feature off from a server.

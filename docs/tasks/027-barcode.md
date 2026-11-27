# 027 Barcode

Status: Blocked

Depends: 020, 026

Lane: S

Migration queue: no. A miss that must be saved uses task 026's id generator. This task does not add a table.

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/barcode/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/barcode/`
  - recorded JSON under `sharedLogic/src/commonTest/resources/barcode/`

Does not touch:
  - the Ktor engine setup in `com.sarmad.mealin.seam`
  - SQLDelight migrations
  - camera UI

Legacy, read only:
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/GetFoodDetailsFromBarCodeUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/InsertBarCodeResultUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/IsFoodItemInDbUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/InsertApiFoodItemUseCase.kt`
  - `domain/src/main/java/com/qg/caloriecounter/domain/model/food/BarcodeFood.kt`
  - `app/src/main/java/com/qg/caloriecounter/core/utils/BarCodeScanner.kt`

Decisions:
  - `docs/decisions/2026-11-27-kotlinx-datetime-serialization-ktor.md`
  - `docs/decisions/2026-11-27-strings-stay-in-the-apps.md`

Outcome:
A scanner interface returns a string. Shared code looks up the catalog by that code, and on a miss calls the existing Ktor client. Tests use `MockEngine` fixtures for a hit, a miss, and malformed JSON. The miss error is a code, not a sentence. A network food that is stored uses task 026 so the id is at least 100001.

Why:
The platform owns the camera. The resolution rules are shared and testable without a device. Malformed JSON has to be an error code the UI can map.

Done when:
  - The three `MockEngine` tests are committed first and fail. The parser is the next commit. Do not squash.
  - A catalog hit does not touch the network. The test counts engine requests.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Camera screens. The apps implement the scanner interface later, in the same tasks as the camera only if a task owns that UI. This task does not add a camera.
  - Editing the OkHttp or Darwin engines.

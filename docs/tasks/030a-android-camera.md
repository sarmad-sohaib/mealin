# 030a Android camera

Status: Blocked

Depends: 003, 015, 016, 029a, 030

Lane: A

Migration queue: no

Owns:
  - `androidApp/src/main/kotlin/com/sarmad/mealin/camera/`
  - `androidApp/src/test/kotlin/com/sarmad/mealin/camera/`
  - `androidApp/src/main/res/values/strings_camera.xml`
  - the camera route registration in `MealNavHost.kt`

Does not touch:
  - `sharedLogic/`
  - `iosApp/`
  - the recognizer parser

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/camera/CameraFragment.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/describeMeal/DescribeFoodFragment.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/decisions/2026-11-27-expect-actual-catalog-copy.md`
  - `docs/examples/android-log-water.md`

Outcome:
The camera and the describe field live in the Android app. A test supplies bytes or text, the screen sends that intent, and a returned state shows the food name from the recognizer result. The screen does not parse JSON. The camera API stays in `androidApp`. No new `expect`/`actual`.

Why:
Task 030 is the shared contract. This task is the platform camera. The water screen is still the UI pattern. The nav host is free after bookmarks.

Done when:
  - The UI test is committed first. It does not open a camera device. It sends a describe intent and shows a fixture food name.
  - `./gradlew :androidApp:testDebugUnitTest` is green.
  - `./gradlew :androidApp:assembleDebug` is green.

Out of scope:
  - iOS camera.
  - ML Kit inside `sharedLogic`.

# 030b iOS camera

Status: Blocked

Depends: 004, 015, 016, 029b, 030

Lane: I

Migration queue: no

Owns:
  - `iosApp/iosApp/Camera/`
  - the camera route registration in `MealNav.swift`
  - the camera keys in `Localizable.strings`

Does not touch:
  - `androidApp/`
  - `sharedLogic/`
  - the recognizer parser

Legacy, read only:
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/camera/CameraFragment.kt`
  - `app/src/main/java/com/qg/caloriecounter/presentation/screen/describeMeal/DescribeFoodFragment.kt`

Decisions:
  - `docs/decisions/2026-11-27-screen-store-in-shared-logic.md`
  - `docs/decisions/2026-11-27-expect-actual-catalog-copy.md`
  - `docs/examples/ios-log-water.md`
  - the Swift interop decision from task 004

Outcome:
SwiftUI owns the camera and the describe field. The view sends an intent with text or image bytes and renders the food name from state. No new `expect`/`actual`.

Why:
The recognizer is shared. The camera is not. This task can run beside 030a. It waits for 029b so `MealNav.swift` has one writer.

Done when:
  - The view test is committed first and does not require a device camera.
  - iOS app: open iosApp in Xcode and run iosApp.
  - `./gradlew :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - Android camera.
  - HealthKit.

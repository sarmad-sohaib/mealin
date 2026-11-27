# kotlinx-datetime, kotlinx.serialization, and Ktor

## Decision

Shared code uses kotlinx-datetime for instants and calendar dates, and kotlinx.serialization for JSON. HTTP uses Ktor. The Android engine is OkHttp. The iOS engine is Darwin. Tests use `MockEngine` and JSON fixtures checked into the repo. No other HTTP stack is added.

## Why

Network is limited to recipes, a barcode that is not in the catalog, and the food recognizer. Those three clients share one JSON parser and one engine seam so a test can answer with a recorded body. CalorieCounter's date helpers use `java.util.Date` and `SimpleDateFormat`. `commonMain` cannot depend on that JVM API and still compile for iOS. kotlinx-datetime is the shared calendar.

## Other ways

- Keep `java.util.Date` in shared code. The iOS target would not compile it.
- Use the platform HTTP APIs behind an interface and skip Ktor. Each app would then parse JSON itself, and `commonTest` would not be able to run the client against `MockEngine`.
- Use a different engine pair, such as CIO on Android. OkHttp is the engine already used by Android apps on this machine, and Darwin is the iOS engine Ktor maintains. CIO is a third stack.

## Stood on

- `docs/PLAN.md` limits network to recipes, a barcode miss, and the recognizer. Offline food search does not call the network.
- CalorieCounter `common/src/main/java/com/qg/caloriecounter/common/DateMathExtensions.kt` imports `java.util.Date` and `java.text.SimpleDateFormat`.
- `gradle/libs.versions.toml` does not yet list datetime, serialization, or Ktor. Task 013 adds those versions there.

## Supersedes

None.

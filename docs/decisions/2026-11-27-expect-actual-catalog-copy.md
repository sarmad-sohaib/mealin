# expect/actual only copies the catalog out of the bundle

## Decision

expect/actual is allowed only for copying catalog.db out of the app bundle. Other platform calls are interfaces passed in from the app.

## Why

Android reads a bundled file from assets. iOS reads a bundled file from the app bundle. Shared code needs that copy before SQLite can open `catalog.db`. That copy is the only `expect`/`actual`. A clock, a logger, a file the user picks, or any later platform service is an interface the app creates and passes into `:sharedLogic`. `commonTest` can pass a fake of that interface.

## Other ways

- Use `expect`/`actual` for every platform call. Each new platform API would add another actual, and `commonTest` would be tied to those actuals.
- Use no `expect`/`actual`, and have each app copy `catalog.db` before it calls shared code. The copy would be written once in the Android app and again in the iOS app.

## Stood on

- The catalog is a bundled file, as described in `2026-11-27-offline-catalog-and-logs.md`. The copy is not implemented in this task.
- Business rules run in `sharedLogic` `commonTest`, as described in `2026-11-27-test-strategy.md`, so a platform service those rules need is an argument the test can replace.
- `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/Platform.kt` still contains the wizard sample `expect fun getPlatform()`. This task leaves that sample in place. The sample is not a second allowed product use.

## Supersedes

None.

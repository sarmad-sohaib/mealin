# One store per screen in sharedLogic

## Decision

Each screen has one store in `:sharedLogic`. The store holds immutable state and accepts intents. It exposes that state as a `StateFlow`. Android and iOS render the state and send intents. They do not calculate calories, BMI, or unit conversions.

## Why

The nutrition rules are tested once in `sharedLogic` `commonTest`. A screen that recomputes them in Compose or SwiftUI creates a second implementation. A store is the shared object the UI test can drive: given state, the screen shows it; given a tap, the screen emits the intent. The UI test does not need the food database.

## Other ways

- Put the state in a ViewModel in `androidApp` and a view model in Swift. The calorie math would be written twice, or the apps would call shared functions and still own the screen rules.
- Use a shared Compose UI for both platforms. iOS remains SwiftUI under `docs/decisions/2026-11-27-compose-lives-in-android-app.md`.
- Expose mutable properties the UI writes. A test could not tell an intent from a stray write.

## Stood on

- `docs/decisions/2026-11-27-test-strategy.md` says UI tests only check that a screen shows state and sends intents.
- `docs/decisions/2026-11-27-compose-lives-in-android-app.md` keeps the rules in `:sharedLogic` and the widgets in the apps.
- CalorieCounter computes the home calorie remainder in `domain/src/main/java/com/qg/caloriecounter/domain/model/DashboardData.kt`, and the fragment only displays it. MealIn keeps that split, with the store in shared code instead of a fragment.

## Supersedes

None.

# Agent rules

MealIn is a Kotlin Multiplatform app. The package is `com.sarmad.mealin`.

Shared rules live in `:sharedLogic`. iOS UI is SwiftUI in `iosApp` and calls SharedLogic. Android Compose currently lives in `:sharedUI`. Task 003 moves it into `androidApp` and deletes `:sharedUI`, under `docs/decisions/2026-11-27-compose-lives-in-android-app.md`. No other task moves or deletes a module.

The execution plan is `docs/PLAN.md`. Pick the next task from `docs/tasks/README.md` and `docs/BOARD.md`.

When the user says "let's go", follow `docs/WORKFLOW.md`. One Ready task, from the failing test through the pull request. Then stop. Ask the user for anything only they can supply, including a missing Firebase JSON, and wait. Do not invent it. The user starts a new chat and says "let's go" again to review and merge. After merge, upload to Firebase App Distribution and TestFlight only if that task changed `androidApp/` or `iosApp/` and the names in `docs/distribution/REQUIRED.md` have values. Do not add the Firebase SDK.

Read `docs/decisions/INDEX.md` before changing the architecture. The decision file is the record.

## Commands

```
./gradlew :androidApp:assembleDebug
./gradlew :androidApp:testDebugUnitTest :sharedLogic:testAndroidHostTest
./gradlew :sharedLogic:iosSimulatorArm64Test
```

iOS app: open iosApp in Xcode and run iosApp.

## Decisions

Before code that changes schema, modules, a shared API, a bundled file, or test strategy, write a decision file. Then implement. Do not wait for a person.

One decision per file: `docs/decisions/YYYY-MM-DD-slug.md`.

Every decision file has these sections, in this order:

- Decision
- Why
- Other ways
- Stood on
- Supersedes

`Supersedes` names the older decision file, or `None`.

After adding or editing a decision file, regenerate the index and commit the index with the decision:

```
./scripts/regenerate-decision-index.sh
```

The script rewrites `docs/decisions/INDEX.md`. Newest date first. Files that share a date are listed alphabetically by filename. Do not edit the index by hand.

## Work logs

`docs/work/` holds one log per task. Name it `NNN-slug.md`, starting at `001-repo-setup.md`. The next task uses the next number.

An agent edits only its own work file.

The log says what the task committed, which checks it ran, and any local choice with why and the other ways.

## Settled rules

The decision files own the wording. Short form:

- This rewrite lives in MealIn, separate from CalorieCounter. CalorieCounter keeps shipping.
- Business rules are tested once in `sharedLogic` `commonTest`, and those tests also run as `iosSimulatorArm64Test`. UI tests only check that a screen shows state and sends intents.
- No ads, no Health Connect, no HealthKit, no paywalls, no billing, no Remote Config.
- Offline food data will be a read-only `catalog.db` copied from CalorieCounter's `app/src/main/assets/test.db`. User logs go in a separate empty database. `foodId` is a logical id, not a cross-database foreign key. Meal rows keep their macro snapshot.
- The FTS index is built into `catalog.db` by an export script, not on first launch.
- `expect`/`actual` is allowed only for copying `catalog.db` out of the app bundle. Other platform calls are interfaces passed in from the app.

The wizard sample `expect fun getPlatform()` stays until a task replaces that sample. It is not a second allowed use of `expect`/`actual`.

Do not ignore `*.db`, `catalog.db`, or `docs/`. The food catalog is committed when the export exists.

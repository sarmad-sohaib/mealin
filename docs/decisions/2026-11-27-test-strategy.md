# Business rules are tested once

## Decision

Business rules are tested once in sharedLogic commonTest, and those tests also run as iosSimulatorArm64Test. UI tests only check that a screen shows state and sends intents.

## Why

A calorie rule, a serving rule, or a log rule has one suite: `sharedLogic` `commonTest`. `iosSimulatorArm64Test` runs the iOS simulator build of `:sharedLogic`, so that same suite runs for iOS. A UI test answers two things about a screen: it shows the state it was given, and the control the user uses sends the intent. The rule that produced the state stays in `commonTest`.

## Other ways

- Copy the business tests into Android host tests and into iOS tests. The copies would drift apart.
- Run the business tests only with `:sharedLogic:testAndroidHostTest`. The iOS framework would then ship without that suite having run on `iosSimulatorArm64`.
- Assert calories and macros from a UI test. The UI test would duplicate the shared rule and would fail when the screen layout changed.

## Stood on

- `sharedLogic/build.gradle.kts` puts `kotlin.test` on `commonTest` and registers `iosSimulatorArm64`.
- `README.md` runs `:sharedLogic:testAndroidHostTest` and `:sharedLogic:iosSimulatorArm64Test`.
- `README.md` also runs `:sharedUI:testAndroidHostTest` for the Compose module. That command is the UI-module test run, not a second copy of the business rules.

## Supersedes

None.

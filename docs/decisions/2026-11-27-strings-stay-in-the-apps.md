# Strings stay in the Android and iOS resource files

## Decision

User-visible copy lives in Android resources and in iOS `Localizable.strings`. Shared code returns state and error codes. It does not return English sentences for the UI to display.

## Why

The two apps already own their widgets, and language is a setting the app applies. A translator edits the resource files for that platform. Shared tests assert codes such as `BudgetMissing` or `BarcodeNotFound`, which stay stable when a string changes.

## Other ways

- Compose Multiplatform resources in `:sharedLogic`, and Swift reads them. iOS would take its copy from a Kotlin resource pack, and the SwiftUI app would not use `Localizable.strings`.
- One shared string table of English sentences returned from the store. A test would assert copy, and both platforms would show the same English when the language setting changes.

## Stood on

- `docs/decisions/2026-11-27-screen-store-in-shared-logic.md` sends state to the apps and keeps math out of the apps.
- `docs/decisions/2026-11-27-compose-lives-in-android-app.md` keeps Android widgets in `androidApp` and iOS widgets in `iosApp`.
- Task 031 persists a language code. The app applies it to its own resources.

## Supersedes

None.

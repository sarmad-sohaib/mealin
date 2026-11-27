# The fitness test scans Kotlin source

## Decision

The architecture fitness test is an Android host test that reads Kotlin in the working tree. A `commonMain` import fails that test when the package starts with `android.`, `androidx.`, or `platform.`. An `expect` or `actual` declaration fails that test when the file's path from the repo root is not a line in `sharedLogic/expect-actual-allowlist.txt`. Comments, string literals, and directories named `build`, `.gradle`, and `.git` are not declarations.

## Why

`compileCommonMainKotlinMetadata` already rejects an `android` or `platform` import in `commonMain`, so that import never reaches the host test. An `expect` with a matching `actual` compiles, and it would be a second platform seam. The simulator suite cannot see the source files. The allowlist is paths so task 009 can append the catalog-copy file.

## Other ways

- Leave `android` and `platform` imports to the metadata compiler, and catch a new `expect` only in review. A paired `expect` and `actual` would stay green on CI.
- Scan with the Kotlin compiler PSI or a compiler plugin. That adds a plugin this task does not own, and the host test would depend on the compiler's tree.
- Treat every import that does not start with `kotlin.` as a platform SDK. SQLDelight and Ktor would then fail the scan when those tasks add them.

## Stood on

- `docs/decisions/2026-11-27-test-strategy.md`. Business rules stay in `commonTest`. This scan reads the tree, which that decision's simulator run cannot do. `docs/PLAN.md` names the host test for that reason.
- `docs/decisions/2026-11-27-expect-actual-catalog-copy.md`. The product `expect`/`actual` is the catalog copy. The wizard `getPlatform` stays on the allowlist until a later task removes it.
- `sharedLogic/src/iosMain/kotlin/com/sarmad/mealin/Platform.ios.kt` imports `platform.UIKit.UIDevice`. That is the Kotlin/Native spelling of a platform SDK.
- Task 006's Done when list. The allowlist is one relative path per line and includes the wizard `Platform.kt` and its `androidMain` and `iosMain` actuals.

## Supersedes

None.

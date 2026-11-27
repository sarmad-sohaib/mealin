# 007 Architecture fitness tests

Task 006. The work log number is 007 because `docs/work/006-github-actions.md` already exists.

An Android host test reads the working tree. A `commonMain` import fails when the package starts with `android.`, `androidx.`, or `platform.`. An `expect` or `actual` file fails unless its path from the repo root is a line in `sharedLogic/expect-actual-allowlist.txt`. That file lists the wizard `Platform.kt` and the Android and iOS `getPlatform` actuals.

Decision: `docs/decisions/2026-11-28-architecture-fitness-scan.md`. It cites `docs/decisions/2026-11-27-test-strategy.md` and `docs/decisions/2026-11-27-expect-actual-catalog-copy.md`.

The scanner does not run on the simulator. The iOS command still runs the existing `commonTest`.

## Committed

- `526295890c4523c1cc90a58f003cadca85789c10` Mark task 006 in progress. Regenerated `docs/BOARD.md`.
- `eb04bce504bbbafd11f80655fdd00c788dbc999a` Record how the architecture fitness test scans source.
- `9e8182a34e8c65bae516c149cce28fb1e58b7a0d` Fail the fitness test on an expect outside the allowlist.
- `0b1a3a3da1cbf2489b2de26908dfb20284c254fd` Remove the temporary expect so the fitness test passes.

## Checks

- `./gradlew :sharedLogic:testAndroidHostTest` failed while `ArchitectureFitnessViolation.kt` and `ArchitectureFitnessViolation.android.kt` were in the tree. 7 tests completed, 1 failed: `expectAndActualDeclarationsAreOnTheAllowlist`. Expected `[]` but was those two paths. The build finished in 2s.
- After those files were removed, `./gradlew :sharedLogic:testAndroidHostTest` was green. The build finished in 2s.
- `./gradlew :sharedLogic:iosSimulatorArm64Test` was green. `iosSimulatorArm64Test` was UP-TO-DATE. `xcode-select` points at `/Applications/Xcode.app/Contents/Developer`. The build finished in 1s.

## Local choices

### The red commit is an expect with an Android actual

Why. `import android.os.Build` in `GreetingUtil.kt` fails `compileCommonMainKotlinMetadata` with unresolved `android` before the host test runs. An `expect` with an Android `actual` compiles for `testAndroidHostTest`, and the allowlist assertion fails on those paths. The import rule is asserted with source text: a real import fails, and a comment, a string, and a raw string do not.

Other ways. Commit the `android` import and treat the metadata error as the failing test. The new assertion would not have run. Put the illegal import in the host test source. That file is not `commonMain`.

### The red commit has no iOS actual

Why. The failing command is `testAndroidHostTest`, which compiles the Android target. The iOS command runs after the temporary files are gone.

Other ways. Add an iOS actual so the red commit compiles for both targets. The next commit would delete that file too. Leave the `expect` with no actual. Android compilation would fail before the assertion.

### The scan skips `build`, `.gradle`, and `.git`

Why. Generated Kotlin under those directories is not a source declaration. The decision records the same skip.

Other ways. Scan every `.kt` file, including `build`. A generator could fail the test. Scan only `sharedLogic/src`. An `expect` in `androidApp` would stay green.

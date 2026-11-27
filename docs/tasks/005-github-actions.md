# 005 GitHub Actions

Status: Done

Depends: none

Lane: S

Migration queue: no

Owns:
  - `.github/`

Does not touch:
  - source under `sharedLogic/`, `sharedUI/`, `androidApp/`, and `iosApp/`
  - `gradle/libs.versions.toml`

Legacy, read only:
  - none

Decisions:
  - `docs/decisions/2026-11-27-test-strategy.md`

Outcome:
Ubuntu runs the Android assemble and the Android host tests. macOS runs the sharedLogic iOS simulator test. The workflow uses the commands currently in `AGENTS.md`. After task 003, a later edit of this workflow is in scope only if 003 changes a command this file calls. This task does not wait for 003. It pins the commands that exist today, and it calls `:sharedLogic:testAndroidHostTest` on Ubuntu even if `:sharedUI:testAndroidHostTest` is still in the README.

Why:
Later agents need a red build when `commonMain` imports Android or when a shared test fails on the simulator. A workflow that exists before feature packages is the check they cannot skip locally and still merge.

Done when:
  - `.github/workflows/` has a workflow whose Ubuntu job runs `./gradlew :androidApp:assembleDebug :sharedLogic:testAndroidHostTest`.
  - The macOS job runs `./gradlew :sharedLogic:iosSimulatorArm64Test`.
  - `./gradlew :androidApp:assembleDebug :sharedLogic:testAndroidHostTest` is green on this machine.
  - The macOS command is the one in the workflow. Run it here when Xcode and the simulator are available. If they are not, say so in `docs/work/` and do not pretend the job passed.

Out of scope:
  - Deploy, signing, and device-farm UI tests.
  - Editing product source to make a failing test pass. Fix the workflow, or stop and report the failure.

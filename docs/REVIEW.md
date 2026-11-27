# Review

A different session from the one that implemented the task reviews the diff. The reviewer reads the task file, the decision files it names, and `docs/PLAN.md`. The reviewer does not expand the task.

## Checklist

- The change cites the decision file the task names. A new choice that is not already in `docs/decisions/` has a new decision file, written before the code, with Decision, Why, Other ways, Stood on, and Supersedes. Other ways names two real alternatives.
- The test asserts an input and an outcome. It does not assert that a private function was called.
- For a behavior change, `git log` shows the failing test committed before the production change. The commits are not squashed.
- The diff stays inside the task's Owns paths, plus a decision file the task requires. It does not edit another task's work log.
- `commonMain` does not import `android`, `androidx`, or a platform SDK.
- A UI test shows store state and emits an intent. It does not recompute calories, BMI, or a unit conversion.
- `androidApp` and `iosApp` do not contain calorie, BMI, or unit math.
- A store does not import a repository implementation. It takes the interface in its constructor.
- A new `expect`/`actual` is the catalog copy, and its path is on `sharedLogic/expect-actual-allowlist.txt`. Any other `expect`/`actual` fails the fitness test.
- Versions were added only in `gradle/libs.versions.toml`.
- A new user-database table is the migration this task owns, and no other migration task was In progress.
- The Gradle or Xcode command in the task's Done when is green.
- The task file's Status is Done, and `scripts/regenerate-board.sh` was run.

## Fail the review

Reject the change when a checklist item is missing. Do not fix it in the review session. The implementation session fixes it, or a new session picks the task back up from In progress.

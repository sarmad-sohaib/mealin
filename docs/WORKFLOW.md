# Workflow

The user says "let's go". That sentence is the whole assignment.

If a pull request is open for an In progress task and this session did not open it, you are the reviewer. Follow "Review and merge" below. Do not implement.

If no pull request is waiting, do the next Ready task through the pull request, and through distribution when this task changes an installable app. Then stop. Do not start the next task. Do not review your own pull request.

## Do not invent

Do only what the task's Done when list requires. The smallest change that makes that list true.

Use only facts written in the task file, `docs/PLAN.md`, `docs/parity.md`, the decision files the task names, and the legacy files the task lists. If a value is not there, ask the user for that value and wait. Do not pick a likely default. Do not continue past the step that needs it.

A secret, a GitHub remote, a Firebase service-account JSON, a Firebase app id, a tester group, or an Apple team id is something only the user can supply. Ask for it in the chat and wait. For a key file, ask for a path outside the repo. Do not ask them to paste the key. Do not create the account, do not write a placeholder, and do not add the Firebase SDK to the app.

A choice the task already settled is not a question. Follow it. A new choice inside the task gets a decision file, then the smallest implementation. Do not wait for the user on that choice.

## One task

1. Read `AGENTS.md`, `docs/PLAN.md`, `docs/REVIEW.md`, and this file.
2. On `docs/BOARD.md`, take the lowest-numbered Ready task whose Depends are Done and whose Owns paths do not overlap a task that is In progress.
3. Branch from `master`: `task/NNN-slug`. If `origin` does not exist, ask the user for the remote. Do not create one.
4. Set the task Status to In progress, commit that, and regenerate the board with `./scripts/regenerate-board.sh`.
5. For a behavior change, commit the failing test first. Then commit the implementation. Do not squash.
6. Run the command in the task's Done when. It must be green locally.
7. Push the branch and open one pull request. The PR body lists the task file, the local command, and the result.
8. Stop. A different session reviews with `docs/REVIEW.md`. The author does not approve and does not merge.

## Review and merge

The user opens a new chat and says "let's go". That chat did not write the pull request, so this session is the reviewer. It reads the open pull request and `docs/REVIEW.md`. It does not read the author's chat, does not change production code, and does not pick a Ready task.

If the checklist fails, comment on the pull request with the failed items and stop. The next "let's go" in a new chat returns to that same task and fixes only those items.

Merge only when the review checklist passes and the GitHub checks from task 005 are green. Before task 005 is on `master`, the local command in the task file is the check, and the PR says that CI is not on `master` yet.

After merge, set the task Status to Done, regenerate the board, and set any task whose dependencies are all Done to Ready. Do not start those tasks.

## Distribution

Run this only after the merge, and only if the merged diff changed `androidApp/` or `iosApp/`. A shared-library task does not upload a binary.

Read `docs/distribution/REQUIRED.md`. If any name for that platform has no value, ask the user for each missing one and wait. Do not upload until they provide it or tell you to skip distribution.

Android, when the names are present:

```
./gradlew :androidApp:assembleDebug
firebase appdistribution:distribute <apk-printed-by-the-build> --app "$FIREBASE_ANDROID_APP_ID" --groups "$FIREBASE_TESTER_GROUP"
```

iOS, when the names are present: archive `iosApp` with the existing signing identity and upload that `.ipa` to TestFlight with the App Store Connect API key. A simulator build is not a TestFlight build.

Report the upload URL or the TestFlight build number. If distribution was skipped, say which names were missing or that the task did not change an app module.

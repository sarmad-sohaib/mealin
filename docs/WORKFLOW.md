# Workflow

The user says "let's go". That sentence is the whole assignment.

If any pull request is open for an In progress task and this session did not open it, you are the reviewer. Follow "Review and merge" below. Do not implement.

If no pull request is waiting, choose a chunk or a single task, as "The chunk" describes. Parallel tasks get one subagent each. A chain runs in order. Each task still has its own branch and pull request. Then stop. Do not review your own pull request.

## Do not invent

Do only what the task's Done when list requires. The smallest change that makes that list true.

Use only facts written in the task file, `docs/PLAN.md`, `docs/parity.md`, the decision files the task names, and the legacy files the task lists. If a value is not there, ask the user for that value and wait. Do not pick a likely default. Do not continue past the step that needs it.

A secret, a GitHub remote, a Firebase service-account JSON, a Firebase app id, a tester group, or an Apple team id is something only the user can supply. Ask for it in the chat and wait. For a key file, ask for a path outside the repo. Do not ask them to paste the key. Do not create the account, do not write a placeholder, and do not add the Firebase SDK to the app.

A choice the task already settled is not a question. Follow it. A new choice inside the task gets a decision file, then the smallest implementation. Do not wait for the user on that choice.

## The chunk

Read `AGENTS.md`, `docs/PLAN.md`, `docs/REVIEW.md`, and this file. Then read `docs/BOARD.md`.

A single task is the chunk when it is a spike, the catalog export, or it must stop to ask the user. Do that task and stop.

A parallel chunk is every Ready task that can run together:

- Depends are Done.
- Owns paths do not overlap.
- At most one Lane C task, the lowest-numbered one. Leave any other Lane C task for the next "let's go".

Do not add a task that is still Blocked at the start. Write one sentence in this session's work log naming the tasks and why any Ready task was left out.

When exactly one ordinary task is Ready, run it and then the tasks it unblocks, in order. That chain stops at three tasks, at a second Lane C task, or before a UI screen.

Spawn one subagent per task in the chunk. The coordinator does not edit those Owns paths. A subagent does only its task file and its Owns. It does not spawn another agent. It does not open a second task.

Each subagent:

1. Branches from `master`: `task/NNN-slug`. If `origin` does not exist, ask the user for the remote. Do not create one.
2. Sets the task Status to In progress and regenerates the board with `./scripts/regenerate-board.sh`.
3. For a behavior change, commits the failing test first, then the implementation. Does not squash.
4. Runs the command in the task's Done when. It must be green locally.
5. Pushes the branch and opens one pull request. The body names the task file, the command, and the result.

If the chunk is a chain rather than a parallel set, run it in dependency order. Start the next task only after the previous pull request is open. The chain stops at three tasks, at a second Lane C task, or before a UI screen. A UI screen is its own chunk, together with the other platform's screen for that same feature when that screen is also Ready.

## Review and merge

The user opens a new chat and says "let's go". That chat did not write the pull requests, so this session reviews every open one. One subagent per pull request. They do not read the author's chat, do not change production code, and do not pick a Ready task.

Merge in ascending task number. Skip a pull request whose Depends are not Done on `master`. If the checklist fails, comment the failed items and do not merge that one. The next "let's go" fixes only those items.

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

# Let's go takes a chunk when the Ready tasks do not overlap

## Decision

"Let's go" takes every Ready task whose Owns paths do not overlap, with at most one Lane C task. Those tasks run at the same time, one subagent each. A single Ready task, a spike, or the catalog export is done alone. A chain is used only when one task is Ready, and it stops at three tasks, at a second Lane C task, or before a UI screen. Each task still has its own branch and pull request. The next "let's go" reviews every open pull request from a session that did not write them, then merges in task-number order. A session does not review its own pull request.

## Why

One task per "let's go" made the user start a new chat for every board row, including rows that do not touch the same files. The board already says which rows can run together. A subagent per row keeps one writer per Owns list. Continuing into still-Blocked tasks in the same breath would run the rest of the backlog without a review in between.

## Other ways

- Stay at one task per "let's go". The user starts a chat per row. Nothing overlaps, and the user is in the loop for every row.
- One "let's go" runs every task that is Ready or becomes Ready, until the board is empty. The user says it once. A bad chunk lands many merges before anyone reviews.
- Put the whole chunk in one pull request. Review is one click. A failure in one task blocks the tasks that do not share files.

## Stood on

- `docs/WORKFLOW.md` is what a session follows when the user says "let's go".
- `docs/PLAN.md` defines Lane C as the single migration and catalog writer, and Lane A and Lane I as one writer at a time per app.
- `docs/decisions/2026-11-27-team-loop.md` still requires a pull request per task, a different session for review, and a question to the user when a credential is missing.

## Supersedes

The one-task limit in `docs/decisions/2026-11-27-team-loop.md`. The pull request, the separate review, and the credential question in that file stay.

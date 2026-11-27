# 003 Team loop

Recorded the start-to-finish loop so a later session follows it when the user says "let's go". A missing credential is a question to the user, not a silent stop. Review is a second chat that also starts with "let's go". No product code, no distribution account, and no upload.

## Wrote

- `docs/WORKFLOW.md`
- `docs/distribution/REQUIRED.md` with names and no values
- `docs/decisions/2026-11-27-team-loop.md`
- `.cursor/rules/lets-go.mdc` so a Cursor session loads the rule without being told to open it
- Pointers in `AGENTS.md`, `docs/tasks/README.md`, and `docs/REVIEW.md`

## Local choices

### The rule points at the workflow file

Why. `AGENTS.md` and the Cursor rule stay short. The steps live in one file, so a session does not follow a shorter copy that has drifted.

Other ways. Paste the full loop into `AGENTS.md`. It would pass 150 lines and the Cursor rule would be a third copy. Put the loop only in the Cursor rule. A session outside Cursor would not see it.

### Distribution is CLI-only and only after an app change

Why. The product decision keeps Firebase out of the app. A debug APK and a signed IPA are files the build already produces. Shared-only tasks do not produce a new installable app.

Other ways. Add the Firebase App Distribution SDK. That is a product dependency this rewrite excluded. Upload after every merge, including nutrition math. Testers would install a binary that did not change.

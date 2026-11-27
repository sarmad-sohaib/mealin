# The FTS index is built by the export

## Decision

The FTS index is built into catalog.db by an export script, not on first launch. Not implemented in this task.

## Why

Food search uses an FTS index that is already inside `catalog.db` when the app opens the file. The export script builds that index once, while it produces `catalog.db` from CalorieCounter's `test.db`. Every install then opens the same file. First launch does not create the index, so first launch does not repeat that build on the device.

## Other ways

- Create the FTS virtual table on first launch. Every device would repeat the same build, and the first open would wait on it.
- Search with `LIKE` and ship no FTS index. Catalog search uses the prebuilt FTS index.

## Stood on

- `catalog.db` is a file the export produces before the app bundles it, as described in `2026-11-27-offline-catalog-and-logs.md`.
- This task does not add the export script, the FTS tables, or search code.

## Supersedes

None.

# 008 Catalog export

This let's go runs only 007 Catalog export, because a catalog export is a single task. 010 is Ready and was left out: it was Blocked when this let's go started, and the export does not share its chunk.

Task 007. The work log number is 008 because `docs/work/007-architecture-fitness.md` already exists.

`scripts/export-catalog.sh` copies the legacy database, keeps the catalog tables, drops every other application table, builds `FoodFTS` with the `MIGRATION_10_11` triggers, sets `user_version` 1, runs `VACUUM`, and writes `catalog/catalog.db.sha256`.

## Committed

- `a4e4ccaa9dfd29f91fb7ea05398de31683af5345` Mark task 007 in progress. Regenerated `docs/BOARD.md`.
- `2024b65489c8beb33cf38273cd32bef39ce433c9` Export the read-only catalog with the search index already built. `catalog/catalog.db` and `catalog/catalog.db.sha256` are in that commit.

## Checks

`scripts/export-catalog.sh /Users/sarmadsohaib/AndroidStudioProjects/CalorieCounter/app/src/main/assets/test.db` was run twice. Pass. `cmp` found the two database files identical and the two checksum files identical.

- Byte size: 35594240. That is under 52428800, so the binary is committed and there is no Git LFS decision.
- Checksum: `d5c80b7c462fa93df10665ecbe27925d283d86a8765d630eff604a7ba2605fdc`. `shasum -a 256 catalog/catalog.db` prints that digest. `catalog/catalog.db.sha256` is the same digest.
- `user_version` 1. `Food` count 100000. Food id 2 calories 89.0. `FoodFTS MATCH 'banana'` with `rowid` 2 returns that row. `activities` count 198. No `WaterContainer` table. No `MealLog` table.
- sqlite3 3.51.0 wrote the file.
- `git check-ignore -v catalog/catalog.db` did not match. `.gitignore` does not ignore `catalog.db`, so that file was not edited.
- `git status` showed no CalorieCounter path.

## Local choices

### The legacy database path is the first argument

Why. The committed script must not contain this machine's home directory. The source file is CalorieCounter `app/src/main/assets/test.db`, which is not inside MealIn.

Other ways. Read the path from an environment variable. Point the script at a relative path from this repo to a sibling CalorieCounter checkout. That path is still this machine's layout.

### VACUUM after the drops and the FTS build

Why. Dropped tables leave free pages in the copied file. `VACUUM` writes the catalog without those pages. Both runs produced the same bytes.

Other ways. Commit the copy with the free pages still in it. Use `VACUUM INTO` a second file and move that file into place.

### The checksum file is the hex digest only

Why. `catalog/catalog.db.sha256` is the SHA-256 of the db file. The digest does not change with the path string a hash command prints.

Other ways. Write `shasum -a 256` output, which includes the filename. Write GNU `sha256sum` binary-mode output, which marks the name with `*`.

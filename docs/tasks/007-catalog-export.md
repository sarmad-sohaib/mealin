# 007 Catalog export

Status: In progress

Depends: none

Lane: C

Migration queue: catalog file, before 008. This task does not edit user-database migrations.

Owns:
  - `scripts/export-catalog.sh`
  - `catalog/catalog.db`
  - `catalog/catalog.db.sha256`
  - a Git LFS decision under `docs/decisions/`, only if the exported file is 50 MB or larger

Does not touch:
  - CalorieCounter source, except reading `app/src/main/assets/test.db`
  - `sharedLogic/`, `sharedUI/`, `androidApp/`, `iosApp/`
  - user-database `.sq` files

Legacy, read only:
  - `app/src/main/assets/test.db`
  - `data/src/main/java/com/qg/caloriecounter/data/local/migrations/DbMigrations.kt` (the `FoodFTS` statement and `MIGRATION_10_11` only)
  - `data/src/main/java/com/qg/caloriecounter/data/di/DatabaseModule.kt`

Decisions:
  - `docs/decisions/2026-11-27-offline-catalog-and-logs.md`
  - `docs/decisions/2026-11-27-fts-built-by-export.md`
  - `docs/decisions/2026-11-27-sqldelight-for-both-databases.md`

Outcome:
`scripts/export-catalog.sh` copies the legacy database to `catalog/catalog.db`, keeps the catalog tables, drops the user tables, builds the FTS index, sets `user_version` 1, and writes `catalog/catalog.db.sha256`.

Keep: `Food`, `FoodMeasurement`, `Image`, `NutrientMacros`, `NutrientMinerals`, `NutrientVitamins`, `NutrientFattyAcids`, `NutrientOtherCompounds`, `activities`, `activityCategories`, `activityLevels`.

Drop every other table, including `WaterContainer`, `User`, `MealLog`, `MealFoodLog`, `Recipe`, `CustomMeal`, and the empty log tables. The nine water containers are seeded later by task 014. They do not stay in the catalog.

FTS, taken from the legacy migration, not invented:

```
CREATE VIRTUAL TABLE IF NOT EXISTS FoodFTS USING FTS4(
  name TEXT,
  tokenize=unicode61,
  content=Food,
  prefix='2,3'
)
```

Then `INSERT INTO FoodFTS(FoodFTS) VALUES('rebuild')`, and the same content-sync triggers `MIGRATION_10_11` creates. Do not paste the trigger bodies into this task file from memory. Read them from `DbMigrations.kt`.

Why:
CalorieCounter builds this index on first launch. MealIn opens a file that already has it. The source file has no `FoodFTS` table and `user_version` 0. Measured size of the source is 29,913,088 bytes.

Done when:
  - The script is the only writer of `catalog/catalog.db`. Running it twice produces the same checksum.
  - The output has `user_version` 1, 100,000 foods, food id 2 at 89 kcal, an FTS query for `banana` that returns that row, 198 activities, and no `WaterContainer` or `MealLog` table.
  - `catalog/catalog.db.sha256` is the SHA-256 of the db file.
  - If the db file is 52,428,800 bytes or larger, a decision file chooses Git LFS or a different storage plan, `./scripts/regenerate-decision-index.sh` has been run, and the binary is not committed. Stop there.
  - If it is smaller, both the db and the checksum are committed. `git status` shows no CalorieCounter path.
  - The command that must be green is `scripts/export-catalog.sh` followed by the sqlite checks above. There is no Gradle test in this task.

Out of scope:
  - SQLDelight, the app copy, and bundling the file into Android or iOS. Task 009 does the copy.
  - Deciding LFS before measuring the output.

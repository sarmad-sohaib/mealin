# SQLDelight for the catalog and the user database

## Decision

Both databases use SQLDelight. `catalog.db` is the read-only file produced by the export. The user database is created empty by SQLDelight migrations. MealIn does not open CalorieCounter's Room database and does not replay Room migrations 1 through 11.

## Why

The catalog is a SQLite file with a fixed schema and an FTS index built before the app launches. The user database starts empty and grows by migrations that feature tasks own one at a time. SQLDelight generates Kotlin from `.sq` files for both, and those queries run in `commonTest` and in `iosSimulatorArm64Test`. Room migrations 1 through 11 are the history of the shipping app's single file, which mixes catalog rows and user rows. Replaying them would import that mix.

## Other ways

- Room KMP. It would be a second Android-shaped stack, and the shipping app's Room entities, DAOs, and migrations 1 through 11 are the structure this rewrite is not copying.
- Ship CalorieCounter's Room file unchanged. That file is `app/src/main/assets/test.db`, `user_version` 0, with catalog rows and user tables in one database and no `FoodFTS` table. The user database would not start empty, and FTS would still be built on first launch.

## Stood on

- `docs/decisions/2026-11-27-offline-catalog-and-logs.md` splits the read-only catalog from the user log database. `foodId` is a logical id.
- `docs/decisions/2026-11-27-fts-built-by-export.md` builds FTS in the export, not on first launch.
- CalorieCounter `data/src/main/java/com/qg/caloriecounter/data/di/DatabaseModule.kt` opens one Room database from `test.db` and registers migrations 1 through 11.
- Opened read-only on 2026-11-27: `test.db` is 29,913,088 bytes, `user_version` 0, 100,000 `Food` rows, no `FoodFTS` table.

## Supersedes

None.

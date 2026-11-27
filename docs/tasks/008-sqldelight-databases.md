# 008 SQLDelight databases

Status: Blocked

Depends: 006, 007

Lane: C

Migration queue: step 1. User migration 1 creates no feature tables. Later Lane C tasks add the next migration. This task owns the catalog `.sq` schema and the driver. It does not own search ranking SQL. That file is task 020.

Owns:
  - `sharedLogic/src/commonMain/sqldelight/`
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/db/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/db/`
  - the SQLDelight plugin and version lines in `sharedLogic/build.gradle.kts` and `gradle/libs.versions.toml`

Does not touch:
  - `catalog/catalog.db` except reading its schema
  - `androidApp/`, `iosApp/`, `sharedUI/`
  - feature packages (`water`, `foodlog`, `weight`, and the rest)

Legacy, read only:
  - `app/src/main/assets/test.db` (schema only)
  - `data/src/main/java/com/qg/caloriecounter/data/local/entity/meallog/MealFoodLog.kt`
  - `data/src/main/java/com/qg/caloriecounter/data/di/DatabaseModule.kt`

Decisions:
  - `docs/decisions/2026-11-27-sqldelight-for-both-databases.md`
  - `docs/decisions/2026-11-27-offline-catalog-and-logs.md`

Outcome:
SQLDelight opens the read-only catalog and an empty user database. Tests use an in-memory catalog of three foods, with FTS built the same way as the export. They do not open the 29 MB file.

Fixture rows, userId 9999:

- id 2, name `Banana, Medium, 7" - 7 7/8" Long`, calories 89, protein 1.09, carbs 22.84, fat 0.33, measurement `Each` quantity 1 gram weight 1.18
- id 3, name `Banana chips`, calories 519, protein 2.3, carbs 58.4, fat 33.6
- id 4, name `Smoothie, banana`, calories 120, protein 2, carbs 24, fat 1

Why:
Task 009 and task 020 need a schema that matches the exported file, and they need a tiny database so ranking tests are not a 29 MB copy. Migration 1 is intentionally empty of feature tables so 014, 017, 020, 024, 025, 026, and 029 each own the next migration.

Done when:
  - The failing schema test is committed first. It queries food id 2 from the fixture and expects 89 kcal before the `.sq` files exist. The next commit makes it pass. Do not squash.
  - User migration 1 applies on an empty database and creates no `WaterLog`, `MealFoodLog`, or `WeightLog` table.
  - The fixture FTS query `banana` returns food id 2.
  - `./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test` is green.

Out of scope:
  - The real checksum smoke test. That is task 009.
  - Search ranking order. That is task 020. Do not copy `SearchDao` into this task.
  - Seeding the nine water containers.

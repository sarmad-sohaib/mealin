# 009 Copy the catalog once

Status: Blocked

Depends: 007, 008

Lane: S

Migration queue: no. Read the catalog. Do not edit it or the user migrations.

Owns:
  - `sharedLogic/src/commonMain/kotlin/com/sarmad/mealin/catalog/`
  - `sharedLogic/src/androidMain/kotlin/com/sarmad/mealin/catalog/`
  - `sharedLogic/src/iosMain/kotlin/com/sarmad/mealin/catalog/`
  - `sharedLogic/src/commonTest/kotlin/com/sarmad/mealin/catalog/`
  - `sharedLogic/src/androidHostTest/kotlin/com/sarmad/mealin/catalog/`
  - `sharedLogic/src/iosTest/kotlin/com/sarmad/mealin/catalog/`
  - one new line in `sharedLogic/expect-actual-allowlist.txt` for the catalog-copy declaration
  - the Gradle snippets that put `catalog/catalog.db` and the checksum into the Android and iOS bundles

Does not touch:
  - `scripts/export-catalog.sh`
  - `catalog/catalog.db` contents
  - feature packages
  - the wizard `Platform.kt` allowlist lines, other than leaving them in place

Legacy, read only:
  - `app/src/main/assets/test.db` (to compare id 2 only if the export is missing that row)

Decisions:
  - `docs/decisions/2026-11-27-expect-actual-catalog-copy.md`
  - `docs/decisions/2026-11-27-fts-built-by-export.md`

Outcome:
The only product `expect`/`actual` copies `catalog.db` out of the app bundle into app storage and opens it read-only. A second launch does not copy when the file on disk has the same SHA-256 as the bundled checksum. A different checksum replaces the file.

Why:
Android assets and the iOS bundle are the one platform call shared code has to make before SQLite can open the catalog. Copying on every launch burns the 29 MB write. Skipping a changed checksum would leave an old catalog in place after an update.

Done when:
  - The failing test is committed first. It expects the checksum, 100,000 foods, food id 2 named as the medium banana with 89 kcal, an FTS match for `banana` that includes that id, and 198 activities.
  - The same assertions pass in `./gradlew :sharedLogic:testAndroidHostTest` and `./gradlew :sharedLogic:iosSimulatorArm64Test`.
  - A test calls the copy twice with the same checksum and the second call does not write the file again.
  - The new `expect`/`actual` path is the only addition to the allowlist, and `./gradlew :sharedLogic:testAndroidHostTest` still runs the fitness test from task 006.

Out of scope:
  - Food search ranking and meal logging.
  - Building FTS. The export already did that.

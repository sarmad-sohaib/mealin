#!/bin/sh
# Copy a CalorieCounter legacy database to catalog/catalog.db.
# Keep the catalog tables, drop every other application table, build FoodFTS,
# set user_version 1, and write catalog/catalog.db.sha256.
#
# Decisions:
#   docs/decisions/2026-11-27-offline-catalog-and-logs.md
#   docs/decisions/2026-11-27-fts-built-by-export.md
#   docs/decisions/2026-11-27-sqldelight-for-both-databases.md
#
# FoodFTS and its content-sync triggers are the statements MIGRATION_10_11
# runs in CalorieCounter DbMigrations.kt.
#
# Usage: scripts/export-catalog.sh <legacy-db>
set -eu

if [ "$#" -ne 1 ]; then
  echo "usage: scripts/export-catalog.sh <legacy-db>" >&2
  exit 1
fi

if ! command -v sqlite3 >/dev/null 2>&1; then
  echo "sqlite3 is required" >&2
  exit 1
fi

if ! command -v shasum >/dev/null 2>&1 && ! command -v sha256sum >/dev/null 2>&1; then
  echo "shasum or sha256sum is required" >&2
  exit 1
fi

legacy=$1
if [ ! -f "$legacy" ]; then
  echo "missing legacy database: $legacy" >&2
  exit 1
fi

legacy_dir=$(CDPATH= cd -- "$(dirname "$legacy")" && pwd)
legacy=$legacy_dir/$(basename "$legacy")

root=$(CDPATH= cd -- "$(dirname "$0")/.." && pwd)
out_dir="$root/catalog"
db="$out_dir/catalog.db"
checksum="$out_dir/catalog.db.sha256"
sql=

trap 'rm -f "$sql"' EXIT

mkdir -p "$out_dir"
rm -f "$db" "$db-journal" "$db-wal" "$db-shm"
cp "$legacy" "$db"
chmod u+w "$db"

sql=$(mktemp)
{
  printf '%s\n' 'PRAGMA foreign_keys = OFF;'
  sqlite3 -bail "$db" <<'SQL'
SELECT 'DROP TABLE IF EXISTS "' || replace(name, '"', '""') || '";'
FROM sqlite_master
WHERE type = 'table'
  AND name NOT LIKE 'sqlite_%'
  AND name NOT IN (
    'Food',
    'FoodMeasurement',
    'Image',
    'NutrientMacros',
    'NutrientMinerals',
    'NutrientVitamins',
    'NutrientFattyAcids',
    'NutrientOtherCompounds',
    'activities',
    'activityCategories',
    'activityLevels'
  )
ORDER BY name;
SQL
  cat <<'SQL'
CREATE VIRTUAL TABLE IF NOT EXISTS `FoodFTS`
USING FTS4(
    `name` TEXT,
    tokenize=unicode61,
    content=`Food`,
    prefix=`2,3`
);

INSERT INTO `FoodFTS`(`FoodFTS`) VALUES('rebuild');

CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_FoodFTS_BEFORE_UPDATE
BEFORE UPDATE ON `Food`
BEGIN
    DELETE FROM `FoodFTS` WHERE `docid` = OLD.`rowid`;
END;

CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_FoodFTS_BEFORE_DELETE
BEFORE DELETE ON `Food`
BEGIN
    DELETE FROM `FoodFTS` WHERE `docid` = OLD.`rowid`;
END;

CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_FoodFTS_AFTER_UPDATE
AFTER UPDATE ON `Food`
BEGIN
    INSERT INTO `FoodFTS`(`docid`, `name`) VALUES (NEW.`rowid`, NEW.`name`);
END;

CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_FoodFTS_AFTER_INSERT
AFTER INSERT ON `Food`
BEGIN
    INSERT INTO `FoodFTS`(`docid`, `name`) VALUES (NEW.`rowid`, NEW.`name`);
END;

PRAGMA user_version = 1;
VACUUM;
SQL
} > "$sql"

sqlite3 -bail "$db" < "$sql"

if [ -e "$db-journal" ] || [ -e "$db-wal" ] || [ -e "$db-shm" ]; then
  echo "sqlite left a sidecar beside $db" >&2
  exit 1
fi

if command -v shasum >/dev/null 2>&1; then
  digest=$(shasum -a 256 "$db" | awk '{ print $1; exit }')
else
  digest=$(sha256sum "$db" | awk '{ print $1; exit }')
fi

printf '%s\n' "$digest" > "$checksum"

# Clock, ids, and locale are passed in

## Decision

Shared code does not read the system clock, a random id source, or the platform locale. The composition root passes a clock, an id generator, and a locale. Tests pass fakes. A test does not call the system clock.

## Why

A weigh-in, a water log, and a meal row store a timestamp and a date. If the store calls the clock itself, a test cannot pin "today" or prove the 154 lb activity fallback on a chosen day. Ids for custom foods have to start at 100001 so they do not collide with catalog ids 1 through 100000. A generator the test controls makes that collision visible. Locale stays outside shared code so date labels and strings stay in the apps.

## Other ways

- Call `Clock.System` and a UUID library inside the store. A test would freeze a global clock or assert whatever the machine's time happened to be.
- Let SQLDelight assign every id. Catalog ids already occupy 1 through 100000 in a different file, so an autoincrement in the user database can still collide with a catalog `foodId`.
- Read `Locale.current` inside shared code. That pulls a platform default into `commonMain` and makes a test depend on the machine language.

## Stood on

- `docs/decisions/2026-11-27-expect-actual-catalog-copy.md` says platform calls other than the catalog copy are interfaces passed in from the app.
- `docs/decisions/2026-11-27-constructor-injection.md` puts those interfaces in the constructor.
- CalorieCounter `MealFoodLog` defaults `createdAt` to `System.currentTimeMillis()` and `date` to `Date().formatDateToInt()`. MealIn does not copy that default. The store passes the clock's value.
- Opened read-only: `Food.id` in `test.db` runs from 1 through 100000.

## Supersedes

None.

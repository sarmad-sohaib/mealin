# Food catalog and user logs are separate databases

## Decision

Offline food data will be a read-only catalog.db copied from CalorieCounter's app/src/main/assets/test.db. User logs go in a separate empty database. foodId is a logical id, not a cross-database foreign key. Meal rows keep their macro snapshot. Not implemented in this task.

## Why

The food list already exists as a SQLite file in the shipping app, at CalorieCounter `app/src/main/assets/test.db`. MealIn will copy that file to a read-only `catalog.db` and will open it without a network catalog. User logs change on the device. The catalog is replaced as a whole file. The logs therefore live in a second database that starts empty. SQLite enforces a foreign key inside one database file. `foodId` on a log row is the id of the food the user picked, and the log database does not declare a foreign key into `catalog.db`. The meal row stores the calories and macros from the time it was logged, so a later catalog edit leaves that meal as the user recorded it.

## Other ways

- Store catalog tables and log tables in one SQLite file. Replacing the catalog would then mean migrating the same file that holds the user's meals.
- Store only `foodId` on the meal and join to the catalog when the meal is shown. A catalog edit would change the macros of a meal that was already logged.
- Download the food list from a server when the app opens. The file is already in CalorieCounter, and logging works with the device offline.

## Stood on

- CalorieCounter's `app/src/main/assets/test.db` is present beside this repository. This task does not copy it and does not add an export script.
- `.gitignore` does not ignore `*.db` or `catalog.db`, so the exported catalog can be committed later.
- No catalog code was added under `sharedLogic`, `sharedUI`, `androidApp`, or `iosApp`.

## Supersedes

None.

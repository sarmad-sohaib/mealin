# MealIn plan

MealIn is a portfolio rewrite of CalorieCounter. CalorieCounter keeps shipping. This repo shows one engineer re-platforming the product without transcribing its modules.

CalorieCounter is XML, fragments, Hilt, Room, and Parcelize. `:domain` is an Android library. `:data` is Room, DataStore, Firebase, and the Android Ktor engine. `:common` is five files and has no tests. The UI is on the order of 60 fragments. The one real domain test is `WeighInUseCaseTest`. Ads, Health Connect, paywalls, and Remote Config are a large part of that app. They are not part of this one.

Package is `com.sarmad.mealin`. Shared rules live in `:sharedLogic`. iOS UI is SwiftUI in `iosApp`. Android Compose moves into `androidApp` in task 003, which deletes `:sharedUI`.

Agents execute `docs/tasks/`. This file is the map. It is not a prompt to start coding.

## What was checked

Opened read-only on 2026-11-27:

- `CalorieCounter/app/src/main/assets/test.db` is 29,913,088 bytes. `user_version` is 0. There is no `FoodFTS` table.
- `Food`: 100,000 rows, every `userId` is 9999, ids 1 through 100000. Food id 2 is `Banana, Medium, 7" - 7 7/8" Long`, calories 89.0, protein 1.09, carbs 22.84, fat 0.33. Its first measurement is id 3, unit `Each`, quantity 1, gram weight 1.18. The second is `Gram`, quantity 100, gram weight 1.
- `FoodMeasurement` 176,361. `NutrientMacros`, `NutrientMinerals`, `NutrientVitamins`, and `NutrientFattyAcids` are 100,000 each. `NutrientOtherCompounds` exists and has 0 rows.
- `Image` 416. `activities` 198. `activityCategories` 5. `activityLevels` 594.
- `MealLog`, `MealFoodLog`, `Recipe`, `RecipeIngredient`, `CustomMeal`, and `CustomMealFood` are empty.
- `WaterContainer` is not empty. It has nine rows, all `userId` 9999. They are user data. The export drops them from the catalog. Task 014 seeds the same nine names and millilitre sizes into the user database.
- `User` has one row, id 9999, gender `APP`, age 0. The export drops `User`.

Where this file and the legacy code disagree, the legacy code is the behavior to characterize, and this section says so.

The domain class `MakeNewCalorieBudgetUseCase` picks an activity multiplier from the goal string (`LooseWeight`, `MaintainWeight`, `GainWeight`). Those strings are not `AverageDay` names, so the `when` falls through to 1.9. It also defaults weight to 70 and age to 24. `ReviewsFragment` writes `calorieBudget` from a different formula: Mifflin-St Jeor, weight default 154 lb, height default 175 cm, age default 25, and the `AverageDay` multipliers 1.2, 1.374, 1.55, 1.725, and 1.9. Other onboarding fragments write a budget that hardcodes 1.55. Task 011 reads those call sites and locks the value that the finished onboarding path actually stores. It does not copy the use-case class. The navigation graph was not traced in this planning session, so 011's first test names which function it locked and why.

`DashboardData.caloriesLeft` is `calorieBudget - caloriesConsumed`. It does not add activity burn. `HomeFragment` displays 0 when that value is not positive. Burned calories are a separate figure. The legacy home screen adds Health Connect exercise calories to domain burn. Health Connect is dropped, so MealIn's burned figure is the domain total only.

Activity calories in `ActivitiesDao` are `mets * (weightLb * 0.45359237) * (durationMinutes / 60)`. Weight is the latest `WeightLog` on the same calendar day or earlier with `weight > 0`, else the latest non-zero weight, else 154. That 154 is pounds. `BmiCalculations` converts pounds with `0.453592f`. `DateMathExtensions` converts with `2.20462`. Task 010 tests each helper with the constant in its own file. Task 012 tests activity calories with `0.45359237`. Nobody unifies the constants.

`SearchFoodUseCase` can search a remote food API. MealIn name search is the local FTS index only. Network stays limited to recipes, a barcode that is not in the catalog, and the recognizer. The remote name search is not ported. `docs/parity.md` maps it as dropped.

`GetWeightChangeWithTimeUseCase` is an empty interface. Nothing is ported from it.

## Product

In:

- Onboarding that produces a calorie budget, a water target, and a target weight.
- Home day: eaten, burned, remaining, meals, water.
- Offline food search and food detail. Search works with no network.
- Log a meal. The row stores calories, protein, fat, carbohydrates, quantity, and serving id beside `foodId`.
- Create a food and create a custom meal. Custom food ids start at 100001.
- Barcode. The platform scanner returns a string. Shared code resolves it. A miss may call the network.
- Photo scan and text describe, behind `FoodRecognizer`. The camera stays in the app.
- Water, including the nine editable containers.
- Weight and weigh-in.
- Activities. Calories use the formula above.
- Recipes: browse, filter, detail, bookmarks. A bookmark shows a stub immediately, then replaces it when detail arrives. Legacy bookmark rows store a full `BookmarkedRecipe` in one write. The stub-then-detail behavior is the MealIn store contract.
- Settings: units, profile, language.

Out:

- Ads, Health Connect, HealthKit, paywalls, billing, Remote Config. Decision: `docs/decisions/2026-11-27-no-store-and-health-services.md`.
- Importing a CalorieCounter user's Room file.
- Replaying Room migrations 1 through 11.
- Remote search by food name.
- Water reminders, FCM, analytics, and in-app update. They are not domain use cases and they are not tasks.

## Architecture

Two databases.

`catalog/catalog.db` is read-only. Task 007 builds it from CalorieCounter `app/src/main/assets/test.db`. The script keeps catalog tables, drops user tables, builds FTS4 (`unicode61`, `content=Food`, `prefix=2,3`) with `INSERT INTO FoodFTS(FoodFTS) VALUES('rebuild')`, sets `user_version` 1, and writes a SHA-256 checksum. Keep: `Food`, `FoodMeasurement`, `Image`, `NutrientMacros`, `NutrientMinerals`, `NutrientVitamins`, `NutrientFattyAcids`, `NutrientOtherCompounds`, `activities`, `activityCategories`, `activityLevels`. Drop every other table, including `WaterContainer`, `User`, meal logs, recipes, and custom meals. If the output is 50 MB or larger, task 007 writes a Git LFS decision before the binary is committed. GitHub warns at 50 MB and rejects files over 100 MB. This plan does not choose LFS. The source file is under 50 MB. The FTS index may push it over.

The user database starts empty. SQLDelight migrations create it. It holds logs, profile, custom foods, custom meals, bookmarks, recents, and the nine water containers. `foodId` on a log is a logical id. SQLite cannot enforce a foreign key across the two files.

`expect`/`actual` is only the copy of `catalog.db` from the bundle into app storage. If the copied file already matches the bundled checksum, the second launch does not copy again. A different checksum replaces the file so a later catalog can ship. The wizard `expect fun getPlatform()` stays on the allowlist until a task removes that sample. It is not a second product use.

A store in `:sharedLogic` holds immutable state and intents and exposes a `StateFlow`. The apps render state and send intents. Strings live in Android resources and iOS `Localizable.strings`. Shared code returns codes. Clock, ids, and locale are constructor arguments. HTTP is Ktor, OkHttp on Android and Darwin on iOS, with `MockEngine` in tests.

Task 004 is a spike. It tries the official Kotlin Swift export and SKIE against a sealed class, a `StateFlow`, a suspend function, and a default argument. The spike writes the interop decision and deletes the throwaway unless that decision needs the sample. iOS tasks that collect a store wait for that decision.

## Test commands

Today, before task 003:

```
./gradlew :androidApp:assembleDebug
./gradlew :sharedUI:testAndroidHostTest :sharedLogic:testAndroidHostTest
./gradlew :sharedLogic:iosSimulatorArm64Test
```

iOS app: open iosApp in Xcode and run iosApp.

Task 003 deletes `:sharedUI` and replaces the `:sharedUI:testAndroidHostTest` command in `AGENTS.md` and `README.md`. After that, shared rules use:

```
./gradlew :sharedLogic:testAndroidHostTest :sharedLogic:iosSimulatorArm64Test
```

Android UI uses:

```
./gradlew :androidApp:assembleDebug
./gradlew :androidApp:testDebugUnitTest
```

Business rules are asserted in `sharedLogic` `commonTest`, and that test also runs as `:sharedLogic:iosSimulatorArm64Test`. The architecture fitness test is an Android host test. It reads the source tree, which the simulator test cannot do.

## Engineering bar

- Test first. The failing test is committed before the implementation. History is not squashed.
- Business rules are asserted once, in `sharedLogic` `commonTest`, and that test also runs as `:sharedLogic:iosSimulatorArm64Test`. Assert inputs and outcomes. Do not assert that a private function was called.
- A behavior borrowed from CalorieCounter is characterized with fixtures taken from the legacy code, then reimplemented. Do not copy the Hilt use-case classes.
- UI tests prove the screen shows store state and emits intents. They do not reimplement nutrition math.
- One pattern slice, log water, is the only UI shape later screens may copy. It lands in `docs/examples/` when tasks 015 and 016 are done. Until both are Done, no other screen is built.
- `commonMain` does not import `android`, `androidx`, or a platform SDK.
- A store does not import a repository implementation.
- `androidApp` and `iosApp` do not contain calorie, BMI, or unit math.
- A new `expect`/`actual` that is not the catalog copy fails CI. The allowlist is `sharedLogic/expect-actual-allowlist.txt`.
- Versions live only in `gradle/libs.versions.toml`.
- One writer for `catalog/catalog.db`, the export script, and every SQLDelight migration. Feature tasks do not edit those files unless the task owns them.
- An agent that cannot name two other ways does not understand the choice. It writes the decision file, then implements. It does not ask a person and wait.

## Critical path

Ready now, and safe together because their Owns paths do not overlap: 003, 004, 005, 006, 007.

Then:

1. 006 lands the fitness test before feature packages. 008, 010, and 013 wait for it.
2. 007 exports the catalog. 008 waits for the real schema shape. 010 does not. 010 may run beside 007.
3. 003 moves Compose. 013 waits for 003, and also for 008, because both would edit `gradle/libs.versions.toml` and `sharedLogic/build.gradle.kts`.
4. 008 creates SQLDelight and the three-food fixture. 009 opens the real catalog after 007 and 008.
5. 013 adds the seams. 014 logs water after 008 and 013. 015 and 016 are the only screens until they finish.
6. 010, then 011 and 012 in parallel. 017 onboarding store after 011, 013, and the water migration.
7. 009, 010, 013, and 017, then 020 food search and meal log. 021 and 022 are the screens.
8. Home, weight, activities, custom food, barcode, recipes, bookmarks, recognizer, settings, then the two journey tasks. The migration queue below is the constraint, not this list.

## Lanes

Lane S is a `:sharedLogic` package with a disjoint Owns list, after its Depends are Done. 011 and 012 are the example: both need 010, and they do not touch the same files.

Lane C is the single migration and catalog writer. Two Lane C tasks are never both Ready. The Depends lines encode the queue. Lane C does not run beside another Lane C task.

Lane A is `androidApp` UI after the shared contract for that screen is Done, and after 015 has published the water example. One Lane A task is Ready at a time. They share `MealNavHost.kt`.

Lane I is `iosApp` UI after the shared contract is Done, after 004's interop decision exists, and after 016 has published the water example. One Lane I task is Ready at a time.

Lane A and Lane I for the same feature run together. Water on Android does not wait for water on iOS.

## Migration queue

Each of these tasks adds the next user-database migration and is Lane C. The order is the Depends chain:

1. 008. SQLDelight, catalog `.sq` files, user migration 1 with no feature tables, three-food fixture. Not the 29 MB file.
2. 014. `WaterContainer` and `WaterLog`, including the nine-row seed.
3. 017. Profile, preferences, calorie budget, water target.
4. 020. `MealLog`, `MealFoodLog`, recents. Also owns the search `.sq` file.
5. 024. `WeightLog` and `WeightGoal`.
6. 025. `activityLog`.
7. 026. Custom food and custom meal tables. Ids start at 100001.
8. 029. Bookmark table.

007 is also Lane C for the catalog file. It does not edit user migrations. It is before 008. 009 reads the catalog and does not edit it. 027, 028, 030, and 031 do not add tables.

## Screens

015 creates Android navigation. 016 creates iOS navigation. Later UI tasks add one route and then stop. The extra Depends that keep a single nav writer are:

- Android: 015, then 018, 021, 028a, 023a, 031a, 029a, 030a, 032a.
- iOS: 016, then 019, 022, 028b, 023b, 031b, 029b, 030b, 032b.

028 does not edit user migrations or the Ktor setup owned by 013, so its shared client work can run beside the diary once 013 is Done.

## Catalog smoke test

Task 009, not the fixture tests, asserts all of this against the real file, on the JVM and on the iOS simulator: checksum matches, 100,000 foods, food id 2 is the medium banana at 89 kcal, an FTS query for `banana` returns that row, 198 activities.

## Decisions already written

Module move, SQLDelight, constructor injection, datetime and Ktor, the screen store, strings, and the clock are in `docs/decisions/`. Task 004 writes the Swift interop decision. Task 007 writes the LFS decision only if the exported file is 50 MB or larger.

# 002 Execution plan

This task wrote the plan other agents execute. It did not change product source, copy CalorieCounter, export the catalog, or start task 003.

## Wrote

- `docs/PLAN.md`
- `docs/parity.md`, one row per domain use case file under `domain/usecase` (76 files, 79 behavior rows because three files hold more than one behavior) plus `:common`, the activity formula, search ranking, and the dropped store features
- `docs/tasks/README.md`, `docs/REVIEW.md`, and 41 task files from 003 through 032b
- `scripts/regenerate-board.sh` and the generated `docs/BOARD.md`
- Decision files for the module move, SQLDelight, constructor injection, datetime and Ktor, the screen store, strings, and the clock. `./scripts/regenerate-decision-index.sh` rebuilt the index.
- A pointer in `AGENTS.md` to `docs/PLAN.md` and `docs/tasks/README.md`. The command block is unchanged. Task 003 changes those commands.

## Legacy files opened

Read only, under CalorieCounter:

- `common/src/main/java/com/qg/caloriecounter/common/NutritionCalculations.kt`
- `common/src/main/java/com/qg/caloriecounter/common/BmiCalculations.kt`
- `common/src/main/java/com/qg/caloriecounter/common/Units.kt`
- `common/src/main/java/com/qg/caloriecounter/common/DateMathExtensions.kt`
- `domain/src/test/kotlin/com/qg/caloriecounter/domain/usecase/weighttracking/WeighInUseCaseTest.kt`
- `data/src/main/java/com/qg/caloriecounter/data/local/dao/SearchDao.kt`
- `data/src/main/java/com/qg/caloriecounter/data/di/DatabaseModule.kt`
- `data/src/main/java/com/qg/caloriecounter/data/local/entity/meallog/MealFoodLog.kt`
- `data/src/main/java/com/qg/caloriecounter/data/local/migrations/DbMigrations.kt` (the FoodFTS statement and migration 10 to 11)
- `app/src/main/assets/test.db`, opened with sqlite read-only
- `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/MakeNewCalorieBudgetUseCase.kt`
- `data/src/main/java/com/qg/caloriecounter/data/local/dao/ActivitiesDao.kt`
- `app/src/main/java/com/qg/caloriecounter/presentation/screen/onboarding/OnboardViewModel.kt`
- `app/src/main/java/com/qg/caloriecounter/presentation/screen/onboarding/reviews/ReviewsFragment.kt`
- `domain/src/main/java/com/qg/caloriecounter/domain/model/DashboardData.kt`
- `app/src/main/java/com/qg/caloriecounter/presentation/screen/home/HomeFragment.kt` and `HomeViewModel.kt`
- Use-case signatures under `domain/usecase`, so the parity map could name every file

The database checks are in `docs/PLAN.md`. Food id 2 is the medium banana at 89 kcal. `WaterContainer` has nine rows. `user_version` is 0. There is no FTS table. The file is 29,913,088 bytes.

## Local choices

### 006 gates the first shared packages

Why. The bar says the fitness test lands before feature packages exist, and also that it blocks nothing once it is green. `008`, `010`, and `013` depend on `006`, so an agent cannot open those packages first.

Other ways. Leave `010` Ready beside `006`. A nutrition package could land with no scanner. Make `006` a comment in the plan and trust the reviewer. The Depends line is what an agent who has not seen this chat will obey.

### 013 also waits for 008

Why. Both tasks would edit `gradle/libs.versions.toml` and `sharedLogic/build.gradle.kts`. The brief's dependency for 013 is task 003. The extra dependency on 008 keeps one writer for those two files.

Other ways. Let them edit the toml together and resolve the merge by hand. Split a third task that only adds versions. A third task was not in the required list, and a merge conflict is two agents writing one file.

### Lane C order

Why. User tables are created by 008 (empty migration), 014 (water), 017 (profile), 020 (meal log and recents), 024 (weight), 025 (exercise log), 026 (custom food), 029 (bookmarks). 007 writes the catalog file before 008 and does not touch user migrations. Two of these are never both Ready.

Other ways. Let 008 create every user table. Later tasks would not own a migration, which the brief gives to 024, 025, and 026. Let weight and meal log migrate in parallel. SQLite migration numbers would collide.

### One nav writer per app

Why. `MealNavHost.kt` and `MealNav.swift` are single files. Android UI tasks share the first. iOS UI tasks share the second. The Depends chain is 015, 018, 021, 028a, 023a, 031a, 029a, 030a, 032a, and the iOS mirror. Lane A and Lane I of the same feature can still both be Ready.

Other ways. A route registry loaded by reflection, so every screen task avoids the host file. The host would depend on class-init order. Two Android features in progress, both editing the host, and a merge. The brief's picker rejects overlapping Owns, so the chain is the rule the picker can enforce.

### Both water examples block later screens

Why. The bar says no other screen is built until the water UI shape exists in `docs/examples/`. Later UI tasks depend on 015 and 016, not only on their own platform's water screen.

Other ways. Let Android onboarding start when 015 is Done and iOS water is still open. The Android agent would not have the iOS example, and the bar says both. Let every screen invent its own layout. That is the drift the example is there to stop.

### Recipe browse is 028a and 028b

Why. Task 028 is the shared list, filter, and detail client. Putting both apps in that task would edit both nav hosts and `sharedLogic`. The product includes browse, not only bookmarks.

Other ways. Leave browse as a store with no screen. Bookmarks would be the only recipe UI. Fold browse into 029a and 029b. That merges two tasks.

### The shown remainder is floored in the store

Why. `DashboardData` computes `budget - eaten`. `HomeFragment` displays 0 when that is not positive, and it adds Health Connect to burn. Health Connect is dropped. The floor moves into the store so the apps do not subtract.

Other ways. Leave the floor in the composable and the Swift view. That is calorie math in the apps. Add burn into the remainder. The legacy field does not do that.

### 010 does not add kotlinx-datetime

Why. Task 013 owns the toml after 008. Task 010 can run beside the export and must not edit the toml. It returns the same integers the legacy date helpers return, without `java.util.Date`. Task 013 adds kotlinx-datetime for clocks.

Other ways. Make 010 wait for 013 and use `LocalDate` immediately. 010 would stop being parallel with 007. Add the datetime line from 010 and from 013. Two writers of the toml.

### Copy again only when the checksum changes

Why. The second launch of the same bundle must not copy `catalog.db`. A later export with a new checksum has to replace the file or the app keeps the old catalog.

Other ways. Never replace a file that exists. An updated catalog would not ship. Copy on every launch. That writes about 30 MB every open.

### Onboarding activity level stays on the profile

Why. `InsertActivityLevelLogUserCase` writes a history table. The product needs the level selected during onboarding. Task 017 stores that current value. A history table would be another migration in the same task, which 017 already owns, and nothing in the plan reads the history.

Other ways. Add `activityLevelLog` as its own queue step. Another Lane C task was not required, and no screen reads the history. Drop the level. The water target and the calorie budget both take it as an input.

The navigation graph that assigns `calorieBudget` was not traced. Several fragments write it. Task 011 has the call sites and has to lock the write that finishes onboarding. `docs/PLAN.md` says that.

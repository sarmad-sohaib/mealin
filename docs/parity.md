# Parity

One row per legacy behavior. CalorieCounter paths are read only. They are not copied into MealIn.

Android and iOS name the UI task. A dash means no screen task owns that behavior. The home card or a later store reads it.

Dropped rows cite `docs/decisions/2026-11-27-no-store-and-health-services.md` unless the row names another decision.

## Domain use cases

| Legacy behavior | Legacy file | Task | Planned shared test | Android | iOS |
| --- | --- | --- | --- | --- | --- |
| Delete a custom meal by id. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/DeleteCustomMealUseCase.kt` | 026 | CustomMealTest | — | — |
| Delete a logged exercise for an activity id and timestamp. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/DeleteLoggedActivityUseCase.kt` | 025 | ActivityLogTest | — | — |
| Describe a meal from text and return food rows. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/DescribeFoodUseCase.kt` | 030 | FoodRecognizerTest | 030a | 030b |
| List catalog activities. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetActivitiesUseCase.kt` | 025 | ActivityLogTest | — | — |
| List MET levels for one catalog activity. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetActivityLevelsForActivityUseCase.kt` | 025 | ActivityLogTest | — | — |
| Load food details for a recognizer result. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetAiFoodDetailsUseCase.kt` | 030 | FoodRecognizerTest | 030a | 030b |
| List scanned meals. Legacy reads custom meals. MealIn stores a scan as a meal log and does not add a scan table. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetAllScannedMealsUseCase.kt` | 030 | FoodRecognizerTest | 030a | 030b |
| Calendar marks for days that have logs. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetCalendarDataUseCase.kt` | 023 | HomeDayStoreTest | 023a | 023b |
| Read the stored calorie budget for a date. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetCaloriesBudgetUseCase.kt` | 023 | HomeDayStoreTest | 023a | 023b |
| Day meal totals for breakfast, lunch, dinner, and snacks. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetConsumptionByDateUseCase.kt` | 020 | MealLogTest | 021 | 022 |
| Custom meal with its foods and stored macros. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetCustomMealDetailUseCase.kt` | 026 | CustomMealTest | — | — |
| List custom meals. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetCustomMealsUseCase.kt` | 026 | CustomMealTest | — | — |
| Sum duration and calories for exercises logged on a day. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetDailyActivitySummaryUseCase.kt` | 025 | ActivityLogTest | 023a | 023b |
| Read the height unit. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetHeightUnitUseCase.kt` | 031 | SettingsStoreTest | 031a | 031b |
| Read the stored height. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetHeightUseCase.kt` | 031 | SettingsStoreTest | 031a | 031b |
| Current onboarding activity level. Default in the legacy DAO is low when none is stored. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetLatestActivityLogUseCase.kt` | 017 | OnboardingStoreTest | 018 | 019 |
| See-all recipe list. One store operation covers this and recipe/GetRecipesUseCase. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetRecipesUseCase.kt` | 028 | RecipeStoreTest | 028a | 028b |
| Domain calories burned today, without Health Connect. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/GetTotalCaloriesBurnedForTodayUseCase.kt` | 012 | ActivityCaloriesTest | 023a | 023b |
| Save the onboarding activity level on the profile. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/InsertActivityLevelLogUserCase.kt` | 017 | OnboardingStoreTest | 018 | 019 |
| Insert an exercise log. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/InsertActivityLogUseCase.kt` | 025 | ActivityLogTest | — | — |
| Store a barcode hit that was not in the catalog, with an id at or above 100001. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/InsertBarCodeResultUseCase.kt` | 027 | BarcodeResolverTest | — | — |
| Create a custom meal and keep the macro snapshot. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/InsertCustomMealUseCase.kt` | 026 | CustomMealTest | — | — |
| Save a serving on a custom food. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/InsertFoodMeasurementUseCase.kt` | 026 | CustomFoodTest | — | — |
| Set the height unit. The legacy callback API becomes an intent and a state code. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/SetHeightUnitUseCaseImpl.kt` | 031 | SettingsStoreTest | 031a | 031b |
| Food detail from the catalog, including nutrients. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/GetDbFoodNutritionDetailUseCase.kt` | 020 | FoodDetailTest | 021 | 022 |
| Resolve a barcode string. Catalog first, then network on a miss. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/GetFoodDetailsFromBarCodeUseCase.kt` | 027 | BarcodeResolverTest | 030a | 030b |
| Servings for a food id. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/GetFoodMeasurementsByIdUseCase.kt` | 020 | FoodDetailTest | 021 | 022 |
| Scaled calories and macros for a quantity and a serving. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/GetFoodNutritionUseCase.kt` | 010 | NutritionMathTest | 021 | 022 |
| Recent foods for the search screen. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/GetRecentMealsUseCase.kt` | 020 | RecentFoodTest | 021 | 022 |
| Insert a network food into the user database, not the catalog. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/InsertApiFoodItemUseCase.kt` | 027 | BarcodeResolverTest | — | — |
| Insert a custom food. Ids start at 100001. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/InsertFoodUseCase.kt` | 026 | CustomFoodTest | — | — |
| Store nutrient rows for a custom food in the user database. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/InsertNutritionDetailsUseCase.kt` | 026 | CustomFoodTest | — | — |
| Whether a barcode or food id is already stored. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/IsFoodItemInDbUseCase.kt` | 027 | BarcodeResolverTest | — | — |
| Log foods. The row stores calories, protein, fat, carbohydrates, quantity, and serving id. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/LogMealUseCase.kt` | 020 | MealLogSnapshotTest | 021 | 022 |
| Offline FTS search and local ranking. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/SearchFoodUseCase.kt` | 020 | SearchRankingTest | 021 | 022 |
| Remote food-name search. Dropped. Network is recipes, a barcode miss, and the recognizer. See docs/decisions/2026-11-27-offline-catalog-and-logs.md. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/SearchFoodUseCase.kt` | Dropped | — | — | — |
| Paged local search. Same ranking as SearchDao. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/foodlog/SearchLocalFoodPageUseCase.kt` | 020 | SearchRankingTest | 021 | 022 |
| Read one bookmark, including the stub before detail arrives. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/mealRecipe/GetBookmarkByIdUseCase.kt` | 029 | BookmarkStoreTest | 029a | 029b |
| List bookmarks. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/mealRecipe/GetBookmarksUseCase.kt` | 029 | BookmarkStoreTest | 029a | 029b |
| Upsert a bookmark. MealIn emits a stub first, then replaces it. Legacy writes the full row in one call. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/mealRecipe/UpsertBookmarkUseCase.kt` | 029 | BookmarkStoreTest | 029a | 029b |
| SetBookmarkedUseCase, same file. Toggle isBookmarked. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/mealRecipe/UpsertBookmarkUseCase.kt` | 029 | BookmarkStoreTest | 029a | 029b |
| GetBookmarkedIdsUseCase, same file. The set of bookmarked ids. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/mealRecipe/UpsertBookmarkUseCase.kt` | 029 | BookmarkStoreTest | 029a | 029b |
| Recipe list for a limit. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/recipe/GetRecipesUseCase.kt` | 028 | RecipeStoreTest | 028a | 028b |
| Recipe detail by id. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/recipe/GetViewRecipeDetailUseCase.kt` | 028 | RecipeStoreTest | 028a | 028b |
| Query parameters for the recipe list and filters. Not a use case class. The store owns the query. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/recipe/RecipeQueryParams.kt` | 028 | RecipeStoreTest | 028a | 028b |
| Filter choices for recipes. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/recipeFilter/GetRecipeFiltersUseCase.kt` | 028 | RecipeStoreTest | 028a | 028b |
| Read the profile. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/user/GetCurrentUserUseCase.kt` | 017 | OnboardingStoreTest | 018 | 019 |
| Insert preferences, including units and the water target. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/user/InserUserPrefUseCase.kt` | 017 | OnboardingStoreTest | 018 | 019 |
| Insert the profile. A null user inserts nothing. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/user/InsertUserUseCase.kt` | 017 | OnboardingStoreTest | 018 | 019 |
| Update one profile field. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/user/UpdateUserDetailsUseCase.kt` | 031 | SettingsStoreTest | 031a | 031b |
| Replace the profile row. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/user/UpdateUserUseCase.kt` | 031 | SettingsStoreTest | 031a | 031b |
| Water goal minus water consumed today. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/CalculateWaterLeftTodayUseCase.kt` | 014 | WaterStoreTest | 015 | 016 |
| Read the water goal. The onboarding formula that writes it is task 017. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/GetDailyWaterGoalUseCase.kt` | 014 | WaterStoreTest | 015 | 016 |
| Read the selected container size. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/GetGlassSizeUseCase.kt` | 014 | WaterStoreTest | 015 | 016 |
| Water logged for a date. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/GetWaterConsumedTodayUseCase.kt` | 014 | WaterStoreTest | 015 | 016 |
| Water totals across days. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/GetWaterIntakeReportUseCase.kt` | 014 | WaterStoreTest | 015 | 016 |
| Read the water unit. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/GetWaterUnitUseCase.kt` | 031 | SettingsStoreTest | 031a | 031b |
| Log one selected container. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/LogWaterIntakeUseCase.kt` | 014 | WaterStoreTest | 015 | 016 |
| Set the water goal. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/SetDailyWaterGoalUseCase.kt` | 017 | OnboardingStoreTest | 018 | 019 |
| Select one of the nine containers. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/SetGlassSizeUseCase.kt` | 014 | WaterStoreTest | 015 | 016 |
| Set the water unit. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/watertracking/SetWaterUnitUseCaseImpl.kt` | 031 | SettingsStoreTest | 031a | 031b |
| BMI from stored height and weight, using BmiCalculations. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/GetCurrentBmiUseCase.kt` | 024 | WeighInTest | — | — |
| Current weight for the home card. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/GetCurrentWeightUseCase.kt` | 024 | WeighInTest | 023a | 023b |
| Starting weight for the current goal. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/GetInitialWeightUseCase.kt` | 024 | WeighInTest | — | — |
| Previous weight log. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/GetOldWeightUseCase.kt` | 024 | WeighInTest | — | — |
| Empty interface. No members, no behavior, nothing ported. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/GetWeightChangeWithTimeUseCase.kt` | — | — | — | — |
| Id of the current weight goal. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/GetWeightGoalIdUseCase.kt` | 024 | WeighInTest | — | — |
| Read the weight goal, including the stored calorie budget. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/GetWeightGoalUseCase.kt` | 024 | WeighInTest | 018 | 019 |
| Weight logs for the chart. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/GetWeightReportUseCase.kt` | 024 | WeighInTest | — | — |
| Read the weight unit. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/GetWeightUnitUseCase.kt` | 031 | SettingsStoreTest | 031a | 031b |
| Read a weight value for a caller-selected moment. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/GetWeightUseCase.kt` | 024 | WeighInTest | — | — |
| Insert the goal produced by onboarding. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/InsertWeightGoalUseCase.kt` | 017 | OnboardingStoreTest | 018 | 019 |
| Domain budget that always multiplies by 1.9. Not the saved onboarding budget. Task 011 locks the finish-path formula instead of this class. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/MakeNewCalorieBudgetUseCase.kt` | 011 | CalorieBudgetTest | 018 | 019 |
| Switch the goal to maintain and store the budget. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/SetMaintainWeightUserCase.kt` | 024 | WeighInTest | — | — |
| Mark the current goal completed. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/SetWeightGoalCompletedUseCase.kt` | 024 | WeighInTest | — | — |
| Set the weight unit. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/SetWeightUnitUseCaseImpl.kt` | 031 | SettingsStoreTest | 031a | 031b |
| Log or update today's weight. Outcomes match WeighInUseCaseTest. The test does not assert a private call. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/weighttracking/WeighInUseCase.kt` | 024 | WeighInTest | — | — |
| Hilt bag of custom-meal use cases. Not ported. Constructor injection replaces it. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/wrapper/CustomMealUseCasesWrapper.kt` | 013 | — | — | — |
| Hilt bag of food use cases. Not ported. Constructor injection replaces it. | `domain/src/main/java/com/qg/caloriecounter/domain/usecase/wrapper/FoodUseCasesWrapper.kt` | 013 | — | — | — |

## Common functions

`:common` has five files and no tests. `DispatcherProvider` is not a pure function. `Status` is not ported. Sealed error codes are task 013.

| Legacy behavior | Legacy file | Task | Planned shared test | Android | iOS |
| --- | --- | --- | --- | --- | --- |
| getCalculatedCalories, getQuantityCalculated, getProteinCalculated, getFatCalculated, getCarbohydratesCalculated. | `common/src/main/java/com/qg/caloriecounter/common/NutritionCalculations.kt` | 010 | NutritionMathTest | — | — |
| calculateBMIFromCmAndLbs, including a null height or weight, and percentOf of zero. | `common/src/main/java/com/qg/caloriecounter/common/BmiCalculations.kt` | 010 | BmiMathTest | — | — |
| WeightTargetGoal, WeightUnit, WaterUnit, HeightUnit, AverageDay, and Gender. Status is not ported. | `common/src/main/java/com/qg/caloriecounter/common/Units.kt` | 010 | UnitsTest | — | — |
| formatDateToInt and millisToDate as ddMMyyyy, without java.util.Date. | `common/src/main/java/com/qg/caloriecounter/common/DateMathExtensions.kt` | 010 | DateMathTest | — | — |
| roundTo for Double and Float, including NaN and infinite. | `common/src/main/java/com/qg/caloriecounter/common/DateMathExtensions.kt` | 010 | RoundingTest | — | — |
| lbsToKg and kgToLbs using 2.20462. This is not the BMI constant and not the activity constant. | `common/src/main/java/com/qg/caloriecounter/common/DateMathExtensions.kt` | 010 | UnitConversionTest | — | — |
| centimetersToFeetInches, toFeet, feetInchesToCenti, cmToFeetInch, feetInchToCm. | `common/src/main/java/com/qg/caloriecounter/common/DateMathExtensions.kt` | 010 | UnitConversionTest | — | — |
| convertMlToOz and convertOzToMl using 29.5735. | `common/src/main/java/com/qg/caloriecounter/common/DateMathExtensions.kt` | 010 | UnitConversionTest | — | — |
| getWeightForBmi. | `common/src/main/java/com/qg/caloriecounter/common/DateMathExtensions.kt` | 010 | BmiMathTest | — | — |
| DispatcherProvider. Not ported. The composition root passes dispatchers. | `common/src/main/java/com/qg/caloriecounter/common/DispatcherProvider.kt` | 013 | PlatformSeamTest | — | — |

## Activity calories

| Legacy behavior | Legacy file | Task | Planned shared test | Android | iOS |
| --- | --- | --- | --- | --- | --- |
| METs times weight in kg times hours. Weight is the same-or-earlier positive log, else the latest positive log, else 154 lb. The kilogram factor is 0.45359237. | `data/src/main/java/com/qg/caloriecounter/data/local/dao/ActivitiesDao.kt` | 012 | ActivityCaloriesTest | 023a | 023b |
| Search rank from SearchDao: exact name, then comma or space or FTS offset, then prefix, then other, then shorter name, then name, then id. | `data/src/main/java/com/qg/caloriecounter/data/local/dao/SearchDao.kt` | 020 | SearchRankingTest | 021 | 022 |
| MealFoodLog stores calories, protein, fat, carbohydrates, quantity, and servingID beside foodId. | `data/src/main/java/com/qg/caloriecounter/data/local/entity/meallog/MealFoodLog.kt` | 020 | MealLogSnapshotTest | 021 | 022 |
| Home remaining is calorieBudget minus caloriesConsumed. The shown value is 0 when that is not positive. Burned is separate and does not include Health Connect. | `domain/src/main/java/com/qg/caloriecounter/domain/model/DashboardData.kt` | 023 | HomeDayStoreTest | 023a | 023b |
| Macro gram budgets from HomeViewModel.calculateNutrients. | `app/src/main/java/com/qg/caloriecounter/presentation/screen/home/HomeViewModel.kt` | 023 | HomeDayStoreTest | 023a | 023b |
| Onboarding water target and target weight in OnboardViewModel. | `app/src/main/java/com/qg/caloriecounter/presentation/screen/onboarding/OnboardViewModel.kt` | 017 | OnboardingStoreTest | 018 | 019 |

## Dropped

Decision: `docs/decisions/2026-11-27-no-store-and-health-services.md`.

| Legacy behavior | Legacy file | Task | Planned shared test | Android | iOS |
| --- | --- | --- | --- | --- | --- |
| Ads: interstitials, banners, native, rewarded, and app-open. | `app/src/main/java/com/qg/caloriecounter/core/ads/` | Dropped | — | — | — |
| Remote Config, including ad flags and the food-recognition gate. | `app/src/main/java/com/qg/caloriecounter/core/remoteconfig/RemoteConfigManager.kt` | Dropped | — | — | — |
| Health Connect exercise calories added on the home card. | `app/src/main/java/com/qg/caloriecounter/healthconnect/HealthConnectManager.kt` | Dropped | — | — | — |
| HealthKit. Not present in the Android legacy app. Not added for MealIn on iOS. | `—` | Dropped | — | — | — |
| Paywalls and premium screens. | `app/src/main/java/com/qg/caloriecounter/presentation/screen/premium/` | Dropped | — | — | — |
| Play Billing. | `app/src/main/java/com/pro/PlayBillingWrapper.kt` | Dropped | — | — | — |
| Import a CalorieCounter Room user file. Not a use case. Out of product. | `data/src/main/java/com/qg/caloriecounter/data/di/DatabaseModule.kt` | Dropped | — | — | — |
| Replay Room migrations 1 through 11. | `data/src/main/java/com/qg/caloriecounter/data/local/migrations/DbMigrations.kt` | Dropped | — | — | — |


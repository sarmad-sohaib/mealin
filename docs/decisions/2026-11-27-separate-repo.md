# MealIn is a separate app from CalorieCounter

## Decision

This rewrite lives in MealIn, separate from CalorieCounter. CalorieCounter keeps shipping. The package is com.sarmad.mealin.

## Why

CalorieCounter is the app that still ships. MealIn is a new repository, created from the Kotlin Multiplatform wizard, with its own Gradle build and its own history. The wizard already wrote the package com.sarmad.mealin into the Android application id, the Kotlin sources, and the shared modules. CalorieCounter keeps its own tree and its own releases.

## Other ways

- Keep the rewrite inside the CalorieCounter repository. The shipping app and the new app would share one Gradle root and one git history, so a rewrite change would land beside a fix for the app that still ships.
- Use the package com.sarmad.caloriecounter. The sources in this tree already declare com.sarmad.mealin, and the Gradle project name is MealIn.

## Stood on

- `settings.gradle.kts` sets `rootProject.name` to MealIn and includes `:androidApp`, `:sharedLogic`, and `:sharedUI`.
- `androidApp/build.gradle.kts` sets `namespace` and `applicationId` to `com.sarmad.mealin`.
- Kotlin sources under `androidApp`, `sharedLogic`, and `sharedUI` use `package com.sarmad.mealin`.
- CalorieCounter is a separate project. It is not a module in this repository.

## Supersedes

None.

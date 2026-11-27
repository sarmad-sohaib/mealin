# Constructor injection from a composition root in each app

## Decision

Shared types take their dependencies in the constructor. `androidApp` and `iosApp` each have a composition root that constructs those graphs and passes platform implementations in. Shared code does not call a service locator.

## Why

`commonTest` has to pass a fake clock, a fake id generator, and a fake HTTP engine without starting Android or iOS. A constructor makes that argument visible. Each app already owns the platform calls that are not the catalog copy, so each app is the place that builds the real graph.

## Other ways

- Koin. A global module hides the graph and makes a test reach for `startKoin` instead of a constructor.
- Hilt. CalorieCounter uses Hilt on Android. It does not exist for the iOS framework, and the domain use cases there are the classes this rewrite does not copy.
- kotlin-inject. It generates the graph. The apps are the only composition roots, and a generator is a second build step for a graph the app can write by hand.

## Stood on

- `docs/decisions/2026-11-27-expect-actual-catalog-copy.md` allows `expect`/`actual` only for the catalog copy. Other platform calls are passed in.
- CalorieCounter domain use cases are constructed with `@Inject`. MealIn does not copy those classes.
- The wizard modules have no dependency-injection plugin in `gradle/libs.versions.toml`.

## Supersedes

None.

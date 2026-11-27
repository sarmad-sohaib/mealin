# No ads, health stores, billing, or remote config

## Decision

No ads, no Health Connect, no HealthKit, no paywalls, no billing, no Remote Config.

## Why

MealIn logs meals from a catalog that ships on the device. Ads, paywalls, billing, Health Connect, HealthKit, and Remote Config each add a store account, a system permission, or a network service. None of them are required to record a meal offline.

## Other ways

- Write meals to Health Connect and HealthKit in the first version. Those are platform health stores, with their own permissions and their own data models.
- Ship ads, a paywall, or store billing. That is a store-product surface, and this app has none.
- Gate behavior with Remote Config. Food data and meal logs stay on the device, as described in `2026-11-27-offline-catalog-and-logs.md`. A remote switch would add a network service.

## Stood on

- `gradle/libs.versions.toml` has no ads, billing, Health Connect, HealthKit, or Remote Config library.
- The wizard modules do not register those SDKs.
- The offline catalog decision keeps food data and meal logs on the device.

## Supersedes

None.

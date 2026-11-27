# Distribution inputs

These names are the whole list. Values are supplied by the user as environment variables or as a secret the user enters. They are never committed.

If a name has no value, ask the user for it and wait. Nothing below is guessed.

## Android, Firebase App Distribution

The upload uses the `firebase` CLI. The app does not link the Firebase SDK.

- `FIREBASE_ANDROID_APP_ID`
- `FIREBASE_TESTER_GROUP`
- `GOOGLE_APPLICATION_CREDENTIALS` pointing at a service account JSON that is already on the machine and is not in git

## iOS, TestFlight

- `APP_STORE_CONNECT_API_KEY_ID`
- `APP_STORE_CONNECT_ISSUER_ID`
- `APP_STORE_CONNECT_API_KEY_PATH` pointing at the `.p8` already on the machine and not in git
- The signing identity and provisioning profile Xcode already uses for `iosApp`. If Xcode has none, stop. Do not create a certificate or change the bundle id.

# RudderStack <> CleverTap Sample App (Android, Kotlin)

A minimal native Android app demonstrating RudderStack's **device-mode** CleverTap
integration, per [RudderStack's CleverTap destination docs](https://www.rudderstack.com/docs/destinations/streaming-destinations/clevertap/).

## What it does

- **On launch**: `MyApplication` initializes the RudderStack SDK and registers the CleverTap
  device-mode integration. With `trackActivities = true`, RudderStack automatically fires the
  app lifecycle events (Application Installed / Application Opened / Application Updated),
  which the CleverTap integration forwards on-device to CleverTap.
- **Identify button**: calls `analytics.identify(userId, traits)` with sample user traits, then
  opens the **Thank You** screen. This is sent in device mode, so it goes straight from the
  RudderStack SDK into the CleverTap SDK running in this app (no server hop).
- **Event button**: calls `analytics.track(name = "Page Loaded", properties = ...)` to simulate a
  page-load event, then opens the **Welcome to RudderStack<>CleverTap** screen. Also sent in
  device mode straight to the CleverTap SDK.

## Before you build

1. **RudderStack dashboard**: create a source (Android) and add a CleverTap destination
   (device mode) to it. Enter your CleverTap **Account ID**, **Account Token**, and **Region**
   in that destination's settings — the RudderStack SDK reads these at runtime and initializes
   CleverTap for you, so you do NOT need to configure CleverTap credentials in the app itself.
2. Open `app/src/main/java/com/example/rudderclevertapsample/MyApplication.kt` and replace:
   - `WRITE_KEY` with your RudderStack source's write key
   - `DATA_PLANE_URL` with your RudderStack data plane URL
3. Open the project in Android Studio (a recent stable version) and let it sync Gradle.

## Project structure

```
app/src/main/java/com/example/rudderclevertapsample/
  MyApplication.kt      Initializes RudderStack + CleverTap integration, lifecycle tracking
  MainActivity.kt        Two buttons: Identify, Event
  ThankYouActivity.kt     Shown after Identify
  WelcomeActivity.kt      Shown after Event
app/src/main/res/
  layout/                 activity_main.xml, activity_thank_you.xml, activity_welcome.xml
  values/                 strings.xml, colors.xml, themes.xml
```

## Dependencies used (app/build.gradle.kts)

```kotlin
implementation("com.rudderstack.sdk.kotlin:android:1.+")
implementation("com.rudderstack.integration.kotlin:clevertap:1.+")
implementation("com.clevertap.android:clevertap-android-sdk:7.6.1") // within the [7.3.1, 7.7.0) range the integration supports
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
```

## Notes / things to double-check against the latest RudderStack docs

- `trackActivities = true` on the `Configuration` builder is what enables automatic app
  lifecycle event tracking in the RudderStack Android Kotlin SDK. RudderStack's public
  CleverTap destination doc doesn't show this flag directly (it only shows it for React
  Native as `trackAppLifecycleEvents`) — this sample uses the Android Kotlin SDK's own
  documented parameter name. If a newer SDK release renames it, update `MyApplication.kt`
  accordingly.
- Pin `<latest-version>` for `com.rudderstack.sdk.kotlin:android` and
  `com.rudderstack.integration.kotlin:clevertap` to the actual latest releases from Maven
  Central before shipping — this sample uses a permissive `1.+` range for convenience.
- Push notifications (Firebase + CleverTap notification channel setup) are out of scope for
  this sample and were intentionally left out; the RudderStack docs cover that separately if
  you need it later.
- No minimum SDK version is mandated by RudderStack's docs; this sample targets `minSdk = 23`
  as a reasonable modern baseline — adjust as needed.

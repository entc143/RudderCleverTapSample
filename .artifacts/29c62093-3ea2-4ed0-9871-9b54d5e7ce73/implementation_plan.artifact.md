# Fix Invalid Notification (No Valid Small Icon) Error

The application is crashing or failing to render notifications because CleverTap cannot find a valid small icon. This is primarily due to the missing `android:icon` attribute in the `<application>` tag of `AndroidManifest.xml`.

## User Review Required

> [!IMPORTANT]
> I will be using `ic_app_icon_pink` as the default application icon and notification icon. If you have a specific monochromatic icon for notifications, please let me know.

## Proposed Changes

### [app](file:///Users/sachin.gajbhiye/Claude/RudderCleverTapSample/app)

#### [MODIFY] [AndroidManifest.xml](file:///Users/sachin.gajbhiye/Claude/RudderCleverTapSample/app/src/main/AndroidManifest.xml)

- Add `android:icon="@drawable/ic_app_icon_pink"` and `android:roundIcon="@drawable/ic_app_icon_pink"` to the `<application>` tag.
- Add `<meta-data android:name="CLEVERTAP_NOTIFICATION_ICON" android:value="ic_app_icon_pink"/>` to the `<application>` tag to explicitly provide a small icon for CleverTap notifications.

## Verification Plan

### Automated Tests
- Build the project to ensure `AndroidManifest.xml` is valid.
- Run `gradle_build(":app:assembleDebug")`.

### Manual Verification
- Deploy the app to a device/emulator.
- Trigger a test push notification from CleverTap/RudderStack and verify it renders without the "Invalid notification (no valid small icon)" error.

# Build and install Carrom Royale APK

The repository includes an Android WebView wrapper and a GitHub Actions workflow.

## Automatic build

Every push to the main branch that changes the game or Android wrapper starts **Build Carrom Royale APK**.

- The workflow copies the current `index.html`, `styles.css` and `game.js` into Android app assets.
- It builds a debug APK with Android Gradle Plugin.
- It uploads the APK as a workflow artifact and publishes a GitHub Release asset.

After a successful run, download the APK from the repository's Releases page:
https://github.com/sathsindusandeepana220908-pixel/Carrom/releases/latest

## Local build

Requirements: JDK 17, Android SDK platform 35, Android build tools 35.0.0 and Gradle 8.9.

From the repository root:

```sh
mkdir -p android/app/src/main/assets
cp index.html styles.css game.js android/app/src/main/assets/
gradle -p android assembleDebug
```

APK output:
`android/app/build/outputs/apk/debug/app-debug.apk`

## Phone compatibility

The wrapper uses Android System WebView and AndroidX WebViewAssetLoader. Update Android System WebView / Chrome on the phone if available. WebRTC DataChannel support depends on the installed WebView version and the local Wi-Fi router.

The APK is a debug-signed, sideloadable test build, not a Play Store release. The app uses Wi-Fi LAN/WebRTC; Bluetooth transport is not included.

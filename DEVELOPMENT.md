# Development Guide

## Prerequisites

- Android Studio (latest version recommended)
- JDK 8 or higher
- Android SDK with API level 26+ (Android 8.0)
- Git

## Setting Up the Development Environment

### 1. Clone the Repository

```bash
git clone https://github.com/Basher01/AWJS.git
cd AWJS
```

### 2. Open in Android Studio

1. Launch Android Studio
2. Select "Open an Existing Project"
3. Navigate to the cloned AWJS directory
4. Click "OK"

### 3. Sync Gradle

Android Studio will automatically detect the Gradle configuration and prompt you to sync. If not:
- Click "File" → "Sync Project with Gradle Files"

### 4. Configure Android SDK

Ensure you have:
- Android SDK Build-Tools
- Android SDK Platform 34
- Android SDK Platform-Tools

## Project Structure

```
AWJS/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/basher01/awj/
│   │       │   ├── model/           # Data models
│   │       │   ├── manager/         # Business logic managers
│   │       │   ├── service/         # Background services
│   │       │   ├── MainActivity.kt
│   │       │   ├── ChallengeActivity.kt
│   │       │   └── BlockedAppActivity.kt
│   │       ├── res/                 # Resources (layouts, strings, etc.)
│   │       └── AndroidManifest.xml
│   └── build.gradle                 # App-level build configuration
├── build.gradle                      # Project-level build configuration
├── settings.gradle                   # Gradle settings
└── gradle.properties                 # Gradle properties

```

## Building the Project

### Debug Build

```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Release Build

```bash
./gradlew assembleRelease
```

Note: You'll need to configure signing for release builds.

## Running the App

### Using Android Studio

1. Connect an Android device or start an emulator
2. Click the "Run" button (green triangle) or press Shift+F10
3. Select your device from the list

### Using Command Line

```bash
# Install debug build
./gradlew installDebug

# Install and run
adb install app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.basher01.awj/.MainActivity
```

## Testing

### On Physical Device

**Required:**
- Android device running Android 8.0 (API 26) or higher
- USB debugging enabled
- Device connected via USB

**Steps:**
1. Enable Developer Options on your device
2. Enable USB Debugging
3. Connect device to computer
4. Run the app from Android Studio

**Important:** The app requires special permissions that must be granted manually:
- Usage Stats Access
- Display over other apps

### Testing the Blocking Feature

1. Add test apps to the block list (e.g., Chrome, YouTube)
2. Create simple test challenges
3. Start monitoring service
4. Try to open a blocked app → should see block screen
5. Complete all challenges
6. Unlock apps
7. Try to open blocked app again → should work

## Code Style

### Kotlin Conventions

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Keep functions small and focused

### Example:

```kotlin
/**
 * Checks if a specific app is currently blocked
 * 
 * @param packageName The package name of the app to check
 * @return true if the app is blocked, false otherwise
 */
fun isAppBlocked(packageName: String): Boolean {
    // Implementation
}
```

## Debugging

### Viewing Logs

```bash
# View all logs
adb logcat

# Filter by app
adb logcat | grep com.basher01.awj

# View only errors and warnings
adb logcat *:E *:W
```

### Common Issues

**Issue: Block screen doesn't appear**
- Check: Display over other apps permission granted?
- Check: Service is running? (View in Settings → Apps → AWJ → Running services)

**Issue: App doesn't detect foreground app**
- Check: Usage Stats permission granted?
- Check: UsageStatsManager query returns data?

**Issue: Service stops working**
- Check: Battery optimization disabled for AWJ?
- Check: App not force-closed by user?

## Dependencies

Current dependencies (see `app/build.gradle`):

- AndroidX Core KTX
- AndroidX AppCompat
- Material Design Components
- ConstraintLayout
- Lifecycle Runtime KTX
- WorkManager
- Gson (for JSON serialization)

### Adding New Dependencies

1. Add to `app/build.gradle`:
```gradle
dependencies {
    implementation 'group:artifact:version'
}
```

2. Sync Gradle
3. Import in your Kotlin files

## Architecture Guidelines

### Data Flow

1. **User Input** → Activities
2. **Business Logic** → Managers
3. **Data Storage** → SharedPreferences (via Managers)
4. **Background Work** → Services

### Adding New Features

#### Adding a New Challenge Type

1. Add to `ChallengeType` enum in `model/Challenge.kt`
2. Update UI in `ChallengeActivity.kt`
3. Update default challenges in `ChallengeManager.kt`

#### Adding a New Activity

1. Create new Activity class
2. Create layout XML file
3. Register in `AndroidManifest.xml`
4. Add navigation from existing activities

#### Adding a New Service

1. Create service class extending `Service`
2. Register in `AndroidManifest.xml`
3. Start from Activity or another Service

## Performance Considerations

### Memory

- Use `WeakReference` for context in long-lived objects
- Avoid memory leaks in callbacks and listeners
- Clean up resources in `onDestroy()`

### Battery

- Use efficient polling intervals (currently 2 seconds)
- Consider WorkManager for periodic tasks
- Respect Doze mode and App Standby

### Storage

- Keep SharedPreferences data minimal
- Consider Room database for complex data
- Clear old data periodically

## Security

### Permissions

Only request necessary permissions:
- PACKAGE_USAGE_STATS: For app monitoring
- SYSTEM_ALERT_WINDOW: For block screen
- FOREGROUND_SERVICE: For persistent monitoring

### Data Privacy

- All data stays on device
- No network requests
- No analytics or tracking
- No data sharing

## Release Process

### Preparing for Release

1. Update version in `app/build.gradle`:
```gradle
versionCode 2
versionName "1.1"
```

2. Generate signed APK:
   - Build → Generate Signed Bundle / APK
   - Choose APK
   - Create or select keystore
   - Enter keystore credentials
   - Select release build type

3. Test signed APK on device

4. Create release on GitHub with APK attached

## Contributing

### Workflow

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

### Pull Request Guidelines

- Clear description of changes
- Test on physical device
- No breaking changes to existing functionality
- Follow code style guidelines
- Add comments for complex logic

## Troubleshooting Build Issues

### Gradle Sync Failed

- Check internet connection
- Update Gradle version in `gradle/wrapper/gradle-wrapper.properties`
- Clear Gradle cache: `./gradlew clean`

### Dependencies Not Found

- Check Maven repositories in `build.gradle`
- Verify dependency version exists
- Try offline mode if network issues

### Build Fails

- Clean project: Build → Clean Project
- Rebuild: Build → Rebuild Project
- Invalidate caches: File → Invalidate Caches / Restart

## Resources

- [Android Developer Documentation](https://developer.android.com/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Material Design Guidelines](https://material.io/design)
- [UsageStatsManager API](https://developer.android.com/reference/android/app/usage/UsageStatsManager)

## Contact

For questions or issues:
- Open an issue on GitHub
- Check existing documentation
- Review code comments

## License

This project is licensed under the MIT License - see the LICENSE file for details.

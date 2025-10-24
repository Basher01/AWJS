# AWJ - Anti-Waste Journal

AWJ is a native Android application designed to combat digital addiction and procrastination. Its core function is to lock user-selected distracting apps (e.g., social media, games) and websites. To unlock these apps for the day, the user must first complete productive, real-world challenges that they choose and customize themselves.

## Features

- **App Blocking**: Select and block distracting apps on your device
- **Custom Challenges**: Create and customize your own daily productive challenges
- **Daily Reset**: Apps are automatically locked again each day, requiring fresh challenge completion
- **Challenge Types**: 
  - Exercise (physical activity)
  - Learning (educational content)
  - Productivity (task completion)
  - Custom (user-defined)

## How It Works

1. **Setup**: Select apps you want to block and create your daily challenges
2. **Block**: Start monitoring to activate app blocking
3. **Complete**: Finish all your daily challenges
4. **Unlock**: Gain access to blocked apps for the rest of the day
5. **Reset**: The next day, challenges reset and apps are locked again

## Permissions Required

- **Usage Stats Access**: To monitor which apps are being launched
- **Overlay Permission**: To display the block screen over restricted apps

## Building

This is a standard Android project using Gradle. Build with:

```bash
./gradlew build
```

## Installation

```bash
./gradlew installDebug
```

## Note

This app requires Android 8.0 (API 26) or higher.
# AWJ Architecture

## Overview

AWJ (Anti-Waste Journal) is a native Android application designed to combat digital addiction by blocking distracting apps until the user completes productive challenges.

## Core Components

### 1. Data Models

#### BlockedApp (`model/BlockedApp.kt`)
- Represents an app that the user wants to block
- Contains package name, display name, and blocking status

#### Challenge (`model/Challenge.kt`)
- Represents a productive challenge users must complete
- Types: EXERCISE, LEARNING, PRODUCTIVITY, CUSTOM
- Tracks completion status

### 2. Managers

#### AppBlockManager (`manager/AppBlockManager.kt`)
Manages the list of blocked apps and their states:
- Add/remove apps from the block list
- Track daily unlock status
- Reset daily locks (apps lock again each day)
- Store data using SharedPreferences

#### ChallengeManager (`manager/ChallengeManager.kt`)
Manages challenges and completion tracking:
- Create, add, and remove challenges
- Track completion status for each challenge
- Automatically reset challenges daily
- Check if all challenges are completed
- Provide default challenges for new users

### 3. Activities

#### MainActivity (`MainActivity.kt`)
Main entry point of the application:
- Displays list of blocked apps
- Allows adding/removing blocked apps
- Shows current lock status
- Starts the monitoring service
- Requests necessary permissions (Usage Stats, Overlay)
- Navigates to challenge view

#### ChallengeActivity (`ChallengeActivity.kt`)
Manages user challenges:
- Displays all daily challenges
- Allows marking challenges as complete
- Supports adding custom challenges
- Unlocks apps when all challenges are completed

#### BlockedAppActivity (`BlockedAppActivity.kt`)
Full-screen blocking overlay:
- Shown when user tries to access a blocked app
- Prevents app usage until challenges are completed
- Provides access to challenge view
- Overrides back button to prevent bypass

### 4. Services

#### AppMonitorService (`service/AppMonitorService.kt`)
Background monitoring service:
- Runs as a foreground service
- Uses UsageStatsManager to detect foreground apps
- Checks every 2 seconds for blocked apps
- Launches BlockedAppActivity when blocked app detected
- Displays persistent notification while active

## Data Flow

1. **Setup Phase**:
   - User selects apps to block in MainActivity
   - User creates or customizes challenges in ChallengeActivity
   - User grants necessary permissions

2. **Blocking Phase**:
   - User starts monitoring service
   - AppMonitorService runs in background
   - When blocked app is launched → BlockedAppActivity shown
   - User is redirected back to home or challenge view

3. **Unlock Phase**:
   - User completes all daily challenges
   - ChallengeManager marks all challenges as complete
   - AppBlockManager unlocks apps for the day
   - Blocked apps are accessible until midnight

4. **Reset Phase**:
   - Each day at midnight (when app is next opened)
   - ChallengeManager resets all challenges
   - AppBlockManager re-locks all apps
   - User must complete new set of challenges

## Permissions

### PACKAGE_USAGE_STATS
- Required to detect which app is currently in the foreground
- Critical for monitoring functionality
- Must be granted from Settings → Usage Access

### SYSTEM_ALERT_WINDOW
- Required to display BlockedAppActivity over other apps
- Allows showing the block screen immediately
- Must be granted from Settings → Display over other apps

### FOREGROUND_SERVICE
- Required to run AppMonitorService persistently
- Ensures monitoring continues in background

### POST_NOTIFICATIONS
- Required to show monitoring service notification (Android 13+)

## Storage

All data is stored using SharedPreferences:
- Blocked apps list (JSON serialized)
- Challenges list (JSON serialized)
- Daily unlock status
- Last unlock timestamp
- Last challenge reset timestamp

## Key Features

### Daily Reset
- Apps automatically lock again each day
- Challenges reset to incomplete state
- Encourages consistent productive behavior

### Customizable Challenges
- Users can create their own challenges
- Pre-defined challenge types as templates
- Flexible descriptions and requirements

### Persistent Monitoring
- Background service ensures continuous monitoring
- Low-overhead checking (every 2 seconds)
- Foreground notification for user awareness

### Permission Management
- Guides user through required permissions
- Checks permissions on app launch
- Provides clear explanation of why permissions are needed

## Technical Implementation

### UsageStatsManager
Used to query which apps are currently running:
```kotlin
val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
val stats = usageStatsManager.queryUsageStats(
    UsageStatsManager.INTERVAL_DAILY,
    time - 10000, // Last 10 seconds
    time
)
```

### Foreground Service
Service must run as foreground with notification:
```kotlin
startForeground(NOTIFICATION_ID, createNotification())
```

### Full-Screen Activity
BlockedAppActivity uses special launch flags to appear immediately:
```kotlin
intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
```

## Future Enhancements

Possible improvements for future versions:
- Website blocking (requires accessibility service or VPN)
- Statistics and analytics on app usage
- Streaks and achievements
- Social features (challenge with friends)
- Time-based unlocking (unlock for limited periods)
- Emergency unlock with penalties
- Integration with productivity tools
- Custom notification sounds/vibrations
- Widget support for quick status view

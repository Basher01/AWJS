# AWJ Features

## Core Features

### 1. App Blocking System
- **Selective Blocking**: Choose which apps to block from a list of installed apps
- **Real-time Monitoring**: Background service monitors app usage every 2 seconds
- **Instant Block Screen**: Full-screen overlay appears immediately when blocked app is accessed
- **Daily Lock Reset**: Apps automatically lock again each day at midnight

### 2. Challenge System
- **Customizable Challenges**: Create your own daily productive tasks
- **Pre-defined Templates**: Four challenge types to choose from:
  - Exercise (physical activities)
  - Learning (educational tasks)
  - Productivity (work-related tasks)
  - Custom (user-defined)
- **Default Challenges**: Three starter challenges for new users
- **Challenge Tracking**: Check off completed challenges throughout the day
- **Progress Visibility**: Clear indication of completion status

### 3. Unlock Mechanism
- **All-or-Nothing**: Must complete ALL daily challenges to unlock apps
- **One-Time Daily Unlock**: Once unlocked, apps stay accessible until midnight
- **Clear Feedback**: Visual confirmation when apps are unlocked
- **Status Display**: Main screen shows current lock/unlock state

### 4. User Interface

#### Main Screen
- List of blocked apps
- Current lock status indicator
- Quick access to challenges
- Add/remove apps functionality
- Start monitoring button
- Status notification when active

#### Challenge Screen
- List of all daily challenges with descriptions
- Checkboxes for marking completion
- Add custom challenge button
- Unlock apps button (enabled when all complete)
- Progress tracking

#### Block Screen
- Prominent "blocked" message
- Link to challenges screen
- Close/go back option
- Prevents bypass via back button

### 5. Permission Management
- **Guided Permission Requests**: Clear explanations of why permissions are needed
- **Usage Stats Access**: Required for app monitoring
- **Display Overlay**: Required for block screen
- **Foreground Service**: For persistent monitoring
- **Automatic Permission Checks**: Verifies permissions on app launch

### 6. Data Persistence
- **Local Storage**: All data stored on device using SharedPreferences
- **No Cloud Sync**: Complete privacy, no external servers
- **Reliable Saving**: Automatic save after any changes
- **Daily Reset Logic**: Automatic challenge and lock status reset

### 7. Background Service
- **Foreground Service**: Ensures monitoring continues reliably
- **Persistent Notification**: Shows when service is active
- **Low Battery Impact**: Efficient 2-second polling interval
- **Automatic Restart**: Service restarts if stopped

## User Experience Features

### Ease of Use
- **Intuitive Navigation**: Simple three-screen interface
- **Material Design**: Modern, familiar Android UI
- **Clear Visual Feedback**: Icons, colors, and text indicate status
- **Minimal Setup**: Works in under 5 minutes

### Flexibility
- **Any Number of Apps**: Block as many or as few apps as you want
- **Any Number of Challenges**: Create unlimited custom challenges
- **Adjustable Difficulty**: Start easy, increase gradually
- **Daily Changes**: Modify blocked apps and challenges anytime

### Motivation
- **Achievement System**: Satisfaction of checking off challenges
- **Visual Progress**: See what's complete and what's left
- **Positive Reinforcement**: Unlocking apps is the reward
- **Daily Fresh Start**: New opportunity every day

## Technical Features

### Performance
- **Lightweight**: Minimal memory footprint
- **Battery Efficient**: Optimized polling frequency
- **Fast Response**: Immediate detection of blocked apps
- **No Lag**: Smooth UI with no freezing

### Reliability
- **Foreground Service**: Won't be killed by system
- **Crash Recovery**: Service restarts automatically
- **Data Integrity**: All changes saved immediately
- **Tested Permissions**: Handles permission scenarios gracefully

### Privacy
- **Local Only**: No internet connection required or used
- **No Tracking**: Zero analytics or telemetry
- **No Ads**: Completely ad-free
- **No Data Collection**: Your data stays on your device

### Compatibility
- **Android 8.0+**: Works on Android Oreo and newer
- **All Device Sizes**: Responsive layouts for phones and tablets
- **Dark/Light Theme**: Follows system theme settings
- **Multiple Languages**: Ready for localization

## What AWJ Does NOT Do

To set clear expectations:

### Limitations
- **No Website Blocking**: Only blocks installed Android apps (not websites in browsers)
- **No Time Limits**: Once unlocked, apps stay unlocked for the day (no time-based restrictions)
- **No Usage Tracking**: Doesn't track how much you use apps
- **No Statistics**: No charts or graphs of app usage
- **No Parental Controls**: Designed for self-discipline, not parental monitoring
- **No Remote Control**: Can't be controlled from another device
- **No Social Features**: No sharing or competing with friends
- **No Cloud Sync**: Settings don't sync across devices

### By Design
These limitations are intentional:
- **Simple Focus**: Core functionality without feature bloat
- **Privacy First**: No tracking means better privacy
- **Self-Discipline**: Built for personal improvement, not surveillance
- **Offline First**: No internet dependency

## Future Possibilities

While not currently implemented, these features could be added:

### Potential Enhancements
- Website blocking (would require VPN or accessibility service)
- Usage statistics and insights
- Multiple challenge difficulty levels
- Timed unlocking (unlock for 30 minutes, then re-lock)
- Weekly challenge sets
- Achievement badges and streaks
- Export/import settings
- Custom block messages per app
- Emergency unlock with penalty
- Widget for quick status view
- Smart suggestions for challenges
- Integration with fitness/productivity apps
- Multiple user profiles
- Password protection for settings

## Use Cases

### Who is AWJ For?

**Students**
- Block social media during study sessions
- Complete homework before accessing games
- Build productive morning routines

**Professionals**
- Reduce work distractions
- Complete important tasks first
- Maintain focus during work hours

**Anyone Fighting Digital Addiction**
- Break doom-scrolling habits
- Replace phone time with real activities
- Build better daily routines

**Fitness Enthusiasts**
- Ensure daily exercise gets done
- Use app access as workout motivation
- Build consistent fitness habits

**Self-Improvement Seekers**
- Create accountability for goals
- Build multiple positive habits simultaneously
- Replace bad habits with good ones

## Success Metrics

How to measure if AWJ is working for you:

### Short-term (1-2 weeks)
- Completing challenges most days
- Reduced time on blocked apps
- Increased completion of real-world tasks

### Medium-term (1-3 months)
- Consistent daily challenge completion
- Natural habit formation (doing challenges automatically)
- Less urge to open blocked apps
- Improved productivity

### Long-term (3+ months)
- New habits fully formed
- May not need app anymore (success!)
- Significant reduction in digital distraction
- Achievement of real-world goals

## Getting the Most from AWJ

### Best Practices
1. **Start Small**: 2-3 easy challenges
2. **Be Consistent**: Use every day
3. **Be Honest**: Only check off actually completed challenges
4. **Adjust Gradually**: Increase difficulty slowly
5. **Block Worst Distractions**: Target your biggest time-wasters
6. **Morning Challenges**: Complete early to unlock sooner
7. **Track Progress**: Note improvements in a journal
8. **Share Success**: Tell friends about your achievements

### Red Flags
- Constantly removing apps to bypass blocks
- Setting impossible challenges
- Disabling the service regularly
- Cheating by marking incomplete challenges as done

If you're doing these, AWJ might not be the right solution, or you may need additional support for your habits.

## Technical Specifications

- **Minimum Android Version**: 8.0 (API 26)
- **Target Android Version**: 14 (API 34)
- **Programming Language**: Kotlin
- **UI Framework**: Android Views with Material Components
- **Data Storage**: SharedPreferences with Gson
- **Background Work**: Foreground Service with Handler
- **Build System**: Gradle
- **License**: MIT

## Support

For help with AWJ:
1. Read USER_GUIDE.md for detailed instructions
2. Check DEVELOPMENT.md for technical details
3. Review ARCHITECTURE.md for system design
4. Open an issue on GitHub for bugs

# Workout Stopwatch App for Android Wear OS

The Workout Stopwatch App is designed for fitness enthusiasts and trainers who want to track their workout sessions and rest periods effectively. This app offers a simple and helpful way to manage workout sessions, ensuring users can focus on exercise routines while keeping an eye on the performance and progress.

**STACK**: Java, Android SDK/Android Studio, Gson, JSON, XML

## Core Features

### Stopwatch Functionality [DONE]

- **Start/Stop**: Easily start and stop the stopwatch with a single tap, allowing users to measure the duration of their workout sessions accurately.
- **Lap/Rest Timer**: Mark laps or rest periods during workout sessions, enabling users to manage their exercise and rest intervals effectively.
- **Reset**: Quickly reset the stopwatch to start a new session, making it seamless to move from one set of exercises to another.

### Workout Session Management [DONE]

- **Session Tracking**: Automatically starts a new workout session when the stopwatch is started and not running, allowing users to keep a detailed record of each workout.
- **Session Blocks**: Each start, rest, and continue action creates a session block within the current session, helping users to segment their workouts into manageable chunks.
- **Activity Type Logging**: Records the type of activity for each session block (e.g. activity and rest), providing insights into workout structure and intensity.

### Historical Data [WIP]

- **Session Overview**: View a summary of past workout sessions, including start times, durations, and types of activities, offering users a comprehensive view of their workout history.

### Data Management [WIP]

- **Local Storage [DONE]**: Workout sessions are saved locally on the device, ensuring users have continuous access to their data without the need for an internet connection.
- **Export/Import Options [WIP]**: Ability to export and import workout data, enabling users to backup their data or move it between devices.

## Getting Started

1. Clone the repository to your local machine
2. Open the project in your preferred IDE that supports Android development
3. Ensure you have the Android SDK installed and configured correctly.

```bash
git clone https://github.com/rafaelperozin/workout-stopwatch-app.git
```

After opening the project, sync Gradle and run the app on an emulator or an actual device running Android.

## Contributing

Contributions are welcome! If you have ideas for new features or improvements, feel free to fork the repository, make your changes, and submit a pull request.

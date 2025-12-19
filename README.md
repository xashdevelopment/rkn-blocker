# Block today? - Android App

A production-grade Android application that helps users select a folder of images and "ban" them via a roulette spinner interaction.

## Features

### Core Functionality
- **Folder Selection**: Use Android Storage Access Framework (SAF) to select folders containing images
- **Image Formats**: Support PNG, JPG/JPEG, and WEBP files
- **Roulette Spinner**: Interactive spinner with 10-second animation showing image thumbnails
- **Ban System**: Mark images as "banned" with red X animation and persistent storage
- **Result Management**: Choose to skip or ban selected images

### User Interface
- **Material 3 Design**: Modern Material Design with light and dark theme support
- **Russian Localization**: Complete Russian language support with proper typography
- **Smooth Animations**: 60fps spinner animation and red X drawing effects
- **Responsive Design**: Adaptive layout for various screen sizes

### Technical Features
- **MVVM Architecture**: Clean separation of concerns with ViewModels and repositories
- **SAF Integration**: Secure folder access without broad storage permissions
- **Persistent Storage**: DataStore-based ban list with folder-specific persistence
- **State Management**: Robust state handling with LiveData/StateFlow
- **Image Caching**: Coil image loading with memory and disk caching

## Architecture

### Project Structure
```
rkn.blocktoday/
├── data/
│   ├── repository/
│   │   ├── FolderRepositoryImpl.kt
│   │   └── BanRepositoryImpl.kt
│   └── local/
├── domain/
│   ├── model/
│   │   ├── ImageItem.kt
│   │   └── AppState.kt
│   └── repository/
│       ├── FolderRepository.kt
│       └── BanRepository.kt
├── presentation/
│   ├── MainActivity.kt
│   ├── ResultActivity.kt
│   ├── BanActivity.kt
│   └── components/
│       ├── SpinnerViewModel.kt
│       ├── BanViewModel.kt
│       ├── ViewModelFactory.kt
│       ├── ImageSpinner.kt
│       └── RedXOverlay.kt
├── util/
│   ├── SAFHelper.kt
│   └── AnimationUtils.kt
└── BlockTodayApp.kt
```

### Key Components

#### Activities
- **MainActivity**: Folder selection and spinner interface
- **ResultActivity**: Image confirmation with skip/ban options
- **BanActivity**: Red X animation and ban process

#### Data Layer
- **FolderRepository**: Manages folder selection and image enumeration via SAF
- **BanRepository**: Handles persistent ban list using DataStore
- **ImageItem**: Data model for images with URI, metadata, and identification

#### UI Components
- **ImageSpinner**: Custom Compose component with circular spinner animation
- **RedXOverlay**: Animated red X drawing over banned images
- **ViewModels**: Business logic and state management for each screen

## Build Instructions

### Prerequisites
- Android Studio Arctic Fox or newer
- Kotlin 1.9.10+
- Android SDK 34
- Gradle 8.0+

### Building the Project
1. Clone or download the project
2. Open in Android Studio
3. Sync project with Gradle files
4. Build and run on device/emulator

```bash
# Using Gradle wrapper
./gradlew build
./gradlew installDebug
```

### Dependencies
- **Jetpack Compose**: Modern UI toolkit
- **Material 3**: Latest Material Design components
- **Navigation Compose**: Type-safe navigation
- **Coil**: Image loading and caching
- **DataStore**: Preferences storage for ban list
- **DocumentFile**: SAF helpers for folder access

## Usage

### First Launch
1. App launches to folder selection screen
2. Tap "Выберите папку" to browse and select image folder
3. Grant persistent permissions when prompted

### Game Flow
1. **Main Screen**: View spinner with folder images and "Старт" button
2. **Spin**: Tap "Старт" for 10-second roulette animation
3. **Result**: See selected image with "Пропустить" or "Запретить" options
4. **Ban Process**: 5-second red X drawing, 6-second fade, "ЗАПРЕЩЕНО!" message

### Folder Management
- Tap "Выбрать другой пак..." to change folder
- All images banned automatically returns to folder selection
- Ban lists persist per folder across app restarts

## Technical Specifications

### Performance
- **Target SDK**: 34 (Android 14)
- **Minimum SDK**: 24 (Android 7.0)
- **Animation**: 60fps with hardware acceleration
- **Memory**: Efficient thumbnail loading with caching
- **Storage**: No image modification, app-only ban data

### Security
- **Scoped Storage**: No broad storage permissions
- **SAF Integration**: Only user-selected folders accessible
- **Persistent Permissions**: Retained across app restarts
- **Data Privacy**: Ban data stored locally only

### Accessibility
- **Content Descriptions**: All images and actions labeled
- **Color Contrast**: Material 3 compliant contrast ratios
- **TalkBack Support**: Screen reader friendly navigation
- **Large Text**: Adaptive font scaling

## Development Notes

### Known Limitations
- Large folders (>2000 images) may experience performance impact
- AnimatedVectorDrawable not used (Compose animations instead)
- Some edge cases in permission handling may require user intervention

### Future Enhancements
- Image thumbnail preloading optimization
- Advanced spinner physics and deceleration
- Undo functionality for banned images
- Folder favorites and recent folders
- Image metadata display and sorting

## License

This project is created for demonstration purposes. All rights reserved.

## Support

For technical issues or questions about implementation, refer to the code comments and documentation within the source files.
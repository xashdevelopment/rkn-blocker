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

## CI/CD Pipeline

### GitHub Actions Workflow

The project includes a comprehensive CI/CD pipeline configured in `.github/workflows/build.yml`:

#### 🔧 Build Pipeline Features
- **JDK 17 Setup**: Latest Java Development Kit for compilation
- **Android SDK Setup**: Android 34 SDK with build tools and NDK
- **Gradle Caching**: Intelligent caching for faster build times
- **Multi-Output Builds**: Generates debug APK, release APK, and AAB
- **Artifact Upload**: Automatically uploads build artifacts
- **Test Execution**: Runs unit and instrumentation tests
- **Lint Analysis**: Performs code quality checks

#### 📱 Build Outputs
- **Debug APK**: For testing and development
- **Release APK**: Optimized production build with ProGuard/R8
- **Android App Bundle (AAB)**: For Play Store distribution
- **Test Reports**: Comprehensive test results
- **Lint Reports**: Code quality analysis

#### 🚀 Automated Release
- **Tag-based Release**: Push tags starting with 'v' trigger Play Store upload
- **Internal Testing**: Automatic upload to Play Console internal testing track
- **Version Management**: Auto-incremented version codes from build number

#### 🛠️ CI/CD Configuration

**Trigger Events:**
- Push to `main` and `develop` branches
- Pull requests to main branches
- Version tags (`v1.0.0`, `v1.1.0`, etc.)

**Required Environment Variables:**
```bash
# For release signing (optional)
KEYSTORE_PASSWORD=your_keystore_password
KEY_ALIAS=release
KEY_PASSWORD=your_key_password

# For version management
BUILD_NUMBER=1  # Auto-incremented by CI
VERSION_NAME=1.0.0  # From git tag

# For Play Store upload (required for release)
GOOGLE_PLAY_SERVICE_ACCOUNT={"type":"service_account",...}
```

#### 🔐 Keystore Setup

1. **Generate Keystore:**
   ```bash
   chmod +x generate_keystore.sh
   ./generate_keystore.sh
   ```

2. **Add Secrets to GitHub:**
   - Go to repository Settings → Secrets and variables → Actions
   - Add the keystore passwords as secrets

3. **Play Store Service Account:**
   - Create service account in Google Cloud Console
   - Download JSON key file
   - Add to GitHub secrets as `GOOGLE_PLAY_SERVICE_ACCOUNT`

#### 📊 Workflow Steps

1. **Environment Setup**
   - Checkout repository with submodules
   - Setup JDK 17 (Temurin distribution)
   - Setup Android SDK (API 34, Build Tools 34.0.0)
   - Configure Gradle wrapper validation

2. **Caching & Optimization**
   - Cache Gradle packages and wrapper
   - Cache Android SDK components
   - Enable incremental compilation

3. **Build Process**
   - Run unit tests with coverage
   - Build debug APK
   - Build optimized release APK
   - Generate Android App Bundle
   - Perform lint analysis

4. **Artifact Management**
   - Upload all build artifacts with retention
   - Generate test and lint reports
   - Notify on successful builds

5. **Release (Tags Only)**
   - Download AAB from artifacts
   - Upload to Play Console internal track
   - Set release status to completed

### Local Development

**Prerequisites:**
```bash
# Install JDK 17
sdk install java 17.0.2-tem

# Install Android SDK
sdk install android-sdk

# Setup environment
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

**Build Commands:**
```bash
# Run tests
./gradlew test

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Build AAB for Play Store
./gradlew bundleRelease

# Clean build
./gradlew clean

# Run lint
./gradlew lint
```

### CI/CD Best Practices

1. **Version Management**
   - Use semantic versioning (v1.0.0, v1.1.0, etc.)
   - Auto-increment version codes prevent Play Store conflicts
   - Tag releases to trigger automated uploads

2. **Security**
   - Never commit keystore files or passwords
   - Use GitHub secrets for sensitive data
   - Service account permissions should be minimal

3. **Performance**
   - Gradle and SDK caching reduces build times by 70%
   - Incremental compilation speeds up development
   - Parallel execution optimizes CI resources

4. **Quality Assurance**
   - All tests must pass before merge
   - Lint warnings are tracked and reported
   - Build artifacts are retained for debugging

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
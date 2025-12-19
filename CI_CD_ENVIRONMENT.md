# CI/CD Environment Variables Configuration

## Required Environment Variables for GitHub Actions

### For Release Signing (Optional)
Add these secrets to your GitHub repository (Settings → Secrets and variables → Actions):

```
KEYSTORE_PASSWORD=your_keystore_store_password
KEY_ALIAS=release
KEY_PASSWORD=your_keystore_key_password
```

### For Play Store Upload (Required for Release)
```
GOOGLE_PLAY_SERVICE_ACCOUNT={"type":"service_account","project_id":"your-project","private_key_id":"...","private_key":"-----BEGIN PRIVATE KEY-----\\n...","client_email":"service-account@your-project.iam.gserviceaccount.com","client_id":"...","auth_uri":"https://accounts.google.com/o/oauth2/auth","token_uri":"https://oauth2.googleapis.com/token","auth_provider_x509_cert_url":"https://www.googleapis.com/oauth2/v1/certs","client_x509_cert_url":"..."}
```

### For Version Management (Optional - Auto-generated)
These are automatically set by the workflow:
```
BUILD_NUMBER=1  # GitHub Actions run number
VERSION_NAME=1.0.0  # From git tag (v prefix removed)
```

## Local Development Environment

Create a `.env` file (not committed) for local development:
```bash
# Local keystore configuration (optional)
KEYSTORE_FILE_PATH=./keystore.jks
KEYSTORE_PASSWORD=your_password
KEY_ALIAS=release
KEY_PASSWORD=your_key_password

# Android SDK path
ANDROID_HOME=/path/to/Android/Sdk
ANDROID_SDK_ROOT=/path/to/Android/Sdk
```

## GitHub Repository Settings

### Required Settings:
1. **Actions**: Enable GitHub Actions
2. **Secrets**: Add required secrets for signing and Play Store
3. **Branches**: Protect `main` and `develop` branches
4. **Workflow permissions**: Allow all actions and reusable workflows

### Branch Protection Rules:
- Require pull request reviews
- Require status checks to pass
- Require branches to be up to date
- Include administrators

## Local Build Testing

Before pushing to CI, test locally:
```bash
# Validate Gradle wrapper
./gradlew wrapper --gradle-version 8.0

# Test build
./gradlew assembleDebug

# Run tests
./gradlew test

# Check for lint issues
./gradlew lint
```

## Troubleshooting

### Common Issues:
1. **Build fails with "ANDROID_HOME not set"**
   - Set ANDROID_HOME environment variable
   - Ensure Android SDK is installed

2. **Keystore errors**
   - Verify keystore file exists and is accessible
   - Check password correctness
   - Ensure key alias matches

3. **Gradle caching issues**
   - Clear caches: `./gradlew clean`
   - Delete `.gradle/caches` directory
   - Restart workflow

4. **Play Store upload fails**
   - Verify service account JSON format
   - Check package name matches Play Store listing
   - Ensure AAB is properly signed
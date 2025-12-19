#!/bin/bash

# Generate keystore for Android release builds
# This script creates a keystore file for signing release APKs

KEYSTORE_FILE="keystore.jks"
KEY_ALIAS="release"
KEY_PASSWORD="your-key-password"
STORE_PASSWORD="your-store-password"
VALIDITY=10000
ORGANIZATION="BlockToday"
ORGANIZATIONAL_UNIT="Development"
CITY="Your City"
STATE="Your State"
COUNTRY="US"

echo "🔑 Generating Android release keystore..."

keytool -genkeypair \
  -alias $KEY_ALIAS \
  -keyalg RSA \
  -keysize 2048 \
  -validity $VALIDITY \
  -keystore $KEYSTORE_FILE \
  -storepass $STORE_PASSWORD \
  -keypass $KEY_PASSWORD \
  -dname "CN=$ORGANIZATION, OU=$ORGANIZATIONAL_UNIT, O=$ORGANIZATION, L=$CITY, ST=$STATE, C=$COUNTRY"

if [ $? -eq 0 ]; then
    echo "✅ Keystore generated successfully: $KEYSTORE_FILE"
    echo ""
    echo "📝 Store this information securely:"
    echo "   Keystore: $KEYSTORE_FILE"
    echo "   Alias: $KEY_ALIAS"
    echo "   Store Password: $STORE_PASSWORD"
    echo "   Key Password: $KEY_PASSWORD"
    echo ""
    echo "⚠️  IMPORTANT: Add these to your CI/CD environment variables:"
    echo "   KEYSTORE_PASSWORD=$STORE_PASSWORD"
    echo "   KEY_ALIAS=$KEY_ALIAS"
    echo "   KEY_PASSWORD=$KEY_PASSWORD"
    echo ""
    echo "🔒 For GitHub Actions, add these secrets:"
    echo "   GOOGLE_PLAY_SERVICE_ACCOUNT (Service account JSON for Play Store)"
else
    echo "❌ Failed to generate keystore"
    exit 1
fi
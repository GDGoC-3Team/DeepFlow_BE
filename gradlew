#!/usr/bin/env sh
set -eu

APP_HOME=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
GRADLE_VERSION=8.10.2
GRADLE_HOME="$APP_HOME/.gradle/wrapper/dists/gradle-$GRADLE_VERSION"
GRADLE_BIN="$GRADLE_HOME/gradle-$GRADLE_VERSION/bin/gradle"
GRADLE_ZIP="$APP_HOME/.gradle/wrapper/dists/gradle-$GRADLE_VERSION-bin.zip"

if [ ! -x "$GRADLE_BIN" ]; then
  mkdir -p "$GRADLE_HOME"
  if [ ! -f "$GRADLE_ZIP" ]; then
    if command -v curl >/dev/null 2>&1; then
      curl -fL "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip" -o "$GRADLE_ZIP"
    elif command -v wget >/dev/null 2>&1; then
      wget -O "$GRADLE_ZIP" "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"
    else
      echo "curl or wget is required to download Gradle $GRADLE_VERSION" >&2
      exit 1
    fi
  fi
  unzip -q "$GRADLE_ZIP" -d "$GRADLE_HOME"
fi

exec "$GRADLE_BIN" "$@"

#!/usr/bin/env bash
set -euo pipefail

# android-emulator-runner executes each line of the `script:` input in a separate
# `sh`, so all of the mobile setup/run logic lives here and the workflow calls it
# as a single command.

on_exit() {
  status=$?
  if [ "$status" -ne 0 ]; then
    echo "----- appium.log (tail -n 100) -----"
    tail -n 100 appium.log 2>/dev/null || true
  fi
  trap - EXIT
  exit "$status"
}
trap on_exit EXIT

# --- Appium needs aapt2/apksigner to read the APK manifest ---
# On this setup Appium looks for them in platform-tools (not build-tools/<ver>),
# as observed locally. Install build-tools 34 and expose the binaries there.
SDK="${ANDROID_SDK_ROOT:-${ANDROID_HOME:-}}"
BT="$SDK/build-tools/34.0.0"
if [ ! -x "$BT/aapt2" ]; then
  SDKMGR="$SDK/cmdline-tools/latest/bin/sdkmanager"
  [ -x "$SDKMGR" ] || SDKMGR="sdkmanager"
  yes | "$SDKMGR" --install "build-tools;34.0.0" >/dev/null 2>&1 || true
fi
if [ -x "$BT/aapt2" ]; then
  ln -sf "$BT/aapt2" "$SDK/platform-tools/aapt2"
fi
if [ -f "$BT/lib/apksigner.jar" ]; then
  ln -sf "$BT/lib/apksigner.jar" "$SDK/platform-tools/apksigner.jar"
fi

# --- Start Appium in the background with chromedriver auto-download enabled ---
nohup appium --allow-insecure=uiautomator2:chromedriver_autodownload > appium.log 2>&1 &

# --- Wait until Appium answers (curl retry loop, no sleep) ---
curl -sS --retry 60 --retry-connrefused --retry-delay 0 --retry-all-errors \
  http://127.0.0.1:4723/status
echo

# --- Run the mobile suite (UiAutomator2 targets the single connected emulator;
#     the runner uses port 5554, so pin the device name to that udid) ---
./mvnw -B test -pl mobile-api-tests \
  "-Dcucumber.filter.tags=@mobile and not @fail-case" \
  -Dappium.device.name=emulator-5554

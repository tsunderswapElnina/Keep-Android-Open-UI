# Keep Android Open — Android app (Material 3 Expressive)

An AndroidX **Material 3 Expressive** app for the Keep Android Open campaign. It shows a
real-time countdown to the January 1, 2027 Android lockdown, explains the **Android Developer
Verification rollout**, and has an **Act Now** section for emailing the US government and
regulators worldwide.

## Features

- **Real-time day countdown** — live days / hours / minutes / seconds to `2027-01-01 00:00`
  local time, matching the countdown banner published by keepandroidopen.org, plus a
  campaign-progress `LinearWavyProgressIndicator` (expressive).
- **The Rollout** — what Android Developer Verification is, the full timeline
  (announcement → `Android Developer Verifier` → Sept 30, 2026 enforcement in Brazil,
  Indonesia, Singapore, Thailand → 2027 global rollout), the three verification paths, and
  why the "advanced flow" is not a solution.
- **Act Now** — one-tap email composer with a prewritten letter (c/o
  victory@keepandroidopen.org), copy/share, DOJ and FTC antitrust forms, your House
  representative and senators, plus competition/consumer regulators across the world.
- **Expressive theming** — `MaterialExpressiveTheme` with `MotionScheme.expressive()`, the
  expressive shape scale (`largeIncreased`, `extraLargeIncreased`, `extraExtraLarge`) and a
  bold campaign typography.

## Stack

| Thing | Version |
|---|---|
| Kotlin | 2.4.21 (built into AGP 9 — the `kotlin.android` plugin is not applied) |
| Android Gradle Plugin | 9.1.0 |
| Gradle | 9.8.1 (wrapper) |
| Compose | BOM 2026.09.00 (ui 1.12.1) |
| Material 3 Expressive | `androidx.compose.material3:material3:1.5.0-beta01` |
| compileSdk / targetSdk / minSdk | 37 / 36 / 24 |

`java.time` is available on API 24+ via core library desugaring.

## Build

You need the Android SDK (platform 37). Set the SDK location, then:

```sh
export ANDROID_HOME=/path/to/android-sdk   # or: echo "sdk.dir=/path/to/android-sdk" > local.properties
./gradlew :app:assembleDebug
```

The debug APK lands at `app/build/outputs/apk/debug/app-debug.apk`. Unit tests for the
countdown logic:

```sh
./gradlew :app:testDebugUnitTest
```

### Building on-device in Termux

This project was built end-to-end inside Termux (no PC). The tweaks that make that work are
already committed so the same tree builds on any machine:

- `local.properties` points `sdk.dir` at the on-device SDK (installed with `sdkmanager`).
- `settings.gradle.kts` lists `https://repo1.maven.org/maven2/` ahead of `mavenCentral()`.
  From the JVM on some Android builds, `repo.maven.apache.org` does not resolve but
  `repo1.maven.org` does.
- `gradle.properties` sets `android.aapt2FromMavenOverride` so AGP uses Termux's native
  (aarch64) `aapt2` instead of the x86_64 Maven binary.
- Gradle's own distribution download needs `services.gradle.org` reachable; if the JVM cannot
  resolve it, seed the distribution into `~/.gradle/wrapper/dists`.

To install the resulting APK on this device: `termux-open ~/Download/app-debug.apk` opens the
system package installer (enable install-from-unknown-sources for Termux first).

## AI disclosure

This app was developed with the assistance of generative AI (an AI coding assistant wrote the
large majority of the source). Some reviewers and app stores (including IzzyOnDroid) reject
"vibe-coded" apps outright, and this project may therefore be refused inclusion. It is made
public here in the spirit of full transparency about its provenance.

## Project layout

```
app/src/main/java/org/keepandroidopen/app/
├── MainActivity.kt
├── data/Countdown.kt        # target date + pure countdown math
├── data/Content.kt          # timeline, paths, contacts, letter template
├── ui/KeepAndroidOpenApp.kt # scaffolds + bottom navigation
├── ui/screens/              # CountdownScreen, RolloutScreen, ActNowScreen
├── ui/theme/                # Material 3 Expressive theme
└── util/Intents.kt          # email / share / clipboard / open-url
```

SPDX-License-Identifier: MIT
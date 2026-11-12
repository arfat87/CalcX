# CalcX — Beyond Basic Calculations.

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin%202.0.21-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-Apache%202.0-orange.svg)](LICENSE)
[![Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)]()

> **CalcX** is a native Android calculator combining the minimalist elegance of modern mobile design with the computational power of a scientific, financial, and engineering calculation suite. 100% free with zero advertisements, zero paywalls, zero telemetry, and zero mandatory cloud accounts.

---

## 🌟 Key Highlights

- **Standard & Scientific Engine:** Custom Recursive-Descent Pratt parser with right-associative exponentiation (`2^3^2 = 512`), exact trig zeros in DEG/RAD (`sin(180) = 0`), hyperbolic functions, logarithms, permutations, combinations, and memory slots (`MC, MR, M+, M-, MS`).
- **Comprehensive Indian GST Suite:** Presets (0%, 5%, 12%, 18%, 28%), Custom tax %, Exclusive & Inclusive modes, automatic CGST, SGST, and IGST breakdowns.
- **Loan EMI Calculator:** Monthly EMI, total interest, total repayment, Principal vs Interest ratio visualization, and complete amortization schedule table.
- **Financial Suite:** Simple & Compound Interest, SIP Wealth Projections, Discount & Double Discount, Profit & Loss (Margin vs Markup), and Break-Even Volume analysis.
- **Universal Unit Converter:** 12 categories (Length, Weight, Temperature, Area, Volume, Speed, Time, Pressure, Energy, Power, Data Storage, Angle) with affine temperature conversion.
- **Multi-Currency Converter:** Instant currency conversion with offline-cached rates and live refresh capability.
- **Programmer Calculator:** Live simultaneous HEX, DEC, OCT, BIN displays; QWORD (64), DWORD (32), WORD (16), BYTE (8) widths; bitwise AND, OR, XOR, NOT, shifts.
- **Advanced Mathematics:** Descriptive statistics (mean, median, mode, sample/population variance, quartiles), Fractions (rational arithmetic, mixed fractions), Matrix (det, transpose, inverse), and Equation Solvers (linear, quadratic real/complex, 2x2 systems).
- **Interactive 2D Graphing Canvas:** Real-time Cartesian function plotting with pan, zoom, gridlines, and axis tracking.
- **Offline Persistence:** Room Database for persistent calculation history and favorites, CSV export via Android Share Sheet, and DataStore for persistent preferences.

---

## 🛠 Technology Stack

- **Language:** Kotlin 2.0.21
- **UI Framework:** Jetpack Compose (Material 3)
- **Architecture:** Clean Architecture + MVVM
- **Database:** Room SQLite 2.6.1
- **Preferences:** Jetpack DataStore Preferences 1.1.1
- **Navigation:** Navigation Compose 2.8.1
- **Concurrency:** Kotlin Coroutines & StateFlow
- **Build System:** Gradle 8.9 + Android Gradle Plugin 8.5.2 + Version Catalog
- **Min SDK:** 26 (Android 8.0 Oreo) | **Target SDK:** 34 (Android 14)

---

## 🚀 Build Instructions

### Prerequisites
- JDK 17 (Java Development Kit)
- Android SDK 34 (Build Tools 34.0.0)
- Gradle 8.9 (or use `./gradlew`)

### Build Commands
```bash
# Clone the repository
git clone https://github.com/arfat87/CalcX.git
cd CalcX

# Run all unit test suites
gradle testDebugUnitTest

# Assemble Debug APK
gradle assembleDebug

# Assemble Release APK & Signed Android App Bundle (AAB)
gradle assembleRelease bundleRelease
```

Generated release artifacts:
- **Release APK:** `app/build/outputs/apk/release/app-release.apk` (Optimized R8 & Resource Shrunk ~1.5MB)
- **Release AAB:** `app/build/outputs/bundle/release/app-release.aab` (~3.6MB)
- **Debug APK:** `app/build/outputs/apk/debug/app-debug.apk`

---

## 📚 Documentation Directory

- [Architecture & Design Decisions](docs/ARCHITECTURE.md)
- [Feature Breakdown & Capabilities](docs/FEATURES.md)
- [Calculation Engine Specification](docs/CALCULATION_ENGINE.md)
- [Database Schema & DAOs](docs/DATABASE.md)
- [Testing Strategy & Test Reports](docs/TESTING.md)
- [Release, Signing & Deployment](docs/RELEASE.md)
- [Changelog](docs/CHANGELOG.md)
- [Implementation Status Tracker](IMPLEMENTATION_STATUS.md)

---

## 🔒 Privacy & Free Guarantee

CalcX is built with a **Privacy-First Zero-Knowledge** architecture:
- No internet access required for any core calculation.
- No analytics SDKs or tracking pixels.
- No ads or third-party ad networks.
- No paywalls or subscriptions. All tools are unlocked forever.

---

## 📄 License
CalcX is licensed under the Apache 2.0 License.

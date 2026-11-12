# Changelog

All notable changes to the CalcX project are documented here.

## [1.0.0] - 2026-11-12

### Initial Production Release

#### Added
- **Core Architecture:**
  - Clean Architecture + MVVM implementation in Kotlin 2.0.21.
  - Jetpack Compose UI framework with Material 3 integration.
  - Gradle Version Catalog (`libs.versions.toml`) with Android Gradle Plugin 8.5.2.
  - Room SQLite database (`2.6.1`) and DataStore Preferences (`1.1.1`).
- **Calculation Engine:**
  - Lexer, AST nodes, and Recursive Descent Parser.
  - PEMDAS / BODMAS adherence with right-associative exponentiation (`2^3^2 = 512`).
  - Implicit multiplication support (`2(3)`, `2π`).
  - AngleMode support (`DEG` and `RAD`) with exact zero normalization (`sin(180) = 0`).
  - Trigonometric, hyperbolic, and logarithmic operations (`log10`, `ln`, `log2`).
  - Structured error handling: `DivisionByZero`, `DomainError`, `FactorialLimitExceeded`.
  - Memory operations: `MC`, `MR`, `M+`, `M-`, `MS`.
- **Finance Tools:**
  - Indian GST Calculator with Presets (0, 5, 12, 18, 28%), Custom %, Inclusive/Exclusive modes, CGST/SGST/IGST breakdown.
  - Loan EMI Calculator with monthly EMI, total interest, principal vs interest ratio, and 36-month amortization schedule.
  - Simple & Compound Interest calculator with custom compounding frequencies.
  - SIP Wealth projection calculator.
  - Discount & Double Discount calculator.
  - Profit & Loss calculator (Margin vs Markup).
  - Break-Even Volume and Revenue calculator.
- **Converters & Engineering Tools:**
  - Universal Unit Converter across 12 categories with affine temperature formulas.
  - Multi-Currency Converter with offline fallback rates.
  - Programmer Calculator with simultaneous HEX, DEC, OCT, BIN displays, QWORD/DWORD/WORD/BYTE widths, signed/unsigned toggle, and bitwise operations.
- **Advanced Mathematics:**
  - Statistics engine: mean, median, mode, sample/population variance, standard deviation, quartiles.
  - Fractions engine: rational arithmetic, simplification, mixed fractions, decimal-to-fraction.
  - Matrix calculator: 2x2 determinant, transpose, inverse matrix multiplication.
  - Equation solvers: linear equations, quadratic equations (two real, one repeated, complex roots), 2x2 linear systems.
  - Real 2D Graphing Calculator on Compose Canvas with pan, zoom, gridlines, and axes.
- **Persistence & Design System:**
  - Room Calculation History with search, favorite toggles, and CSV export.
  - Custom favorite formula manager.
  - Adaptive Dark & Light themes with 5 accent color options.
  - Haptic feedback and decimal precision customization.
  - Release artifacts: 1.5MB Release APK & 3.6MB Production AAB.

# CalcX — System Architecture Document

## 1. Architectural Philosophy

CalcX is designed following **Clean Architecture** and **Unidirectional Data Flow (UDF)** patterns:

```
[ UI Layer / Jetpack Compose ]
        ▲           │
     StateFlow    Events
        │           ▼
[ Presentation Layer / ViewModels ]
        ▲           │
     Domain Data  UseCases / Commands
        │           ▼
[ Domain & Engine Layer (Pure Kotlin) ]
        ▲           │
     Entities    Persistence / Requests
        │           ▼
[ Data Layer (Room SQLite, DataStore, Network) ]
```

---

## 2. Layer Responsibilities

### 2.1 UI Layer (`core/designsystem/` & `feature/*`)
- **Jetpack Compose:** Completely declarative UI without legacy XML layouts.
- **Design Tokens:** Centralized color definitions, typography, dynamic shape tokens, and semantic styling for dark and light themes.
- **Components:** Modular and reusable `CalcXButton`, `CalcXDisplay`, `CalcXCard`, `CalcXTopBar`, and `CalcXBottomBar`.
- **System Synchronization:** Status bar and navigation bar colors seamlessly react to dark/light theme changes.

### 2.2 Presentation Layer (`feature/*/ViewModel.kt`)
- Encapsulates UI state into immutable data classes (`CalculatorUiState`, `HistoryUiState`, etc.).
- Exposes immutable `StateFlow` to composables.
- Handles user intent without performing blocking calculations on the main dispatcher.

### 2.3 Domain & Engine Layer (`engine/` & `domain/*`)
- **Zero Android Framework Dependencies:** Pure Kotlin business logic enabling rapid, deterministic unit testing on standard JVMs.
- **Lexer & Parser:** Transforms input strings into an Abstract Syntax Tree (AST) respecting mathematical operator precedence and right-associative exponents.
- **Finance Engine:** Uses `BigDecimal` with explicit `MathContext(16, HALF_UP)` for exact monetary accuracy without floating-point drift.
- **Advanced Math & Converters:** Rational fraction structures, matrix linear algebra routines, affine temperature conversions, and bitwise logic.

### 2.4 Data Layer (`core/database/` & `data/repository/`)
- **Room Database:** SQLite database holding calculation history, favorite formulas, and conversion logs.
- **Jetpack DataStore:** Asynchronous, transactional storage for application preferences (ThemeMode, Accent Color, Decimal Precision, Angle Mode, Haptic Feedback).
- **Repositories:** Abstract the underlying data sources and provide clean Kotlin `Flow` streams to consumers.

---

## 3. Module & Package Structure

```
com.calcx.calculator/
├── CalcXApplication.kt          # Global Application container initializing Room & Repositories
├── MainActivity.kt              # Single Activity entry point with Theme & AppNavHost
├── core/
│   ├── designsystem/            # Color, Theme, Type, and custom UI components
│   ├── database/                # Room DB, DAOs, Entities, TypeConverters
│   └── preferences/             # DataStore UserPreferencesRepository
├── engine/                      # Lexer, Parser, AST, Evaluator, Memory, Formatter
├── domain/
│   ├── finance/                 # FinanceEngine (GST, EMI, SIP, Interest, Discount, P&L)
│   ├── converter/               # UnitConverterEngine
│   ├── programmer/              # ProgrammerEngine (BitWidth, Radix, Bitwise)
│   └── advancedmath/            # AdvancedMathEngine (Statistics, Fractions, Matrix, Equations)
├── data/
│   └── repository/              # HistoryRepository, FavoritesRepository, ConversionRepository
├── feature/
│   ├── calculator/              # Main Calculator Screen, Standard & Scientific Keypads
│   ├── tools/                   # Tools Dashboard with categories & search
│   ├── finance/                 # GST, EMI, Interest, SIP, Discount, ProfitLoss, BreakEven
│   ├── converter/               # Universal Unit Converter & Currency Converter
│   ├── programmer/              # Programmer Calculator
│   ├── advancedmath/            # Statistics, Fractions, Matrix, Equation Solvers
│   ├── graphing/                # 2D Cartesian Function Plotting Canvas
│   ├── history/                 # History List, Search, Favorite, and CSV Export
│   ├── favorites/               # Saved Custom Formulas
│   └── settings/                # Appearance, Precision, Haptics, About & Privacy
└── navigation/
    ├── Screen.kt                # Type-safe route definitions
    └── AppNavHost.kt            # Compose navigation graph with 4 tabs & sub-destinations
```

# CalcX — Feature Specification & Status

## 1. Feature Matrix

| Category | Feature | Status | Details |
|---|---|---|---|
| **Core** | Standard Arithmetic | VERIFIED | `+`, `-`, `×`, `÷`, `%`, `!`, parentheses |
| **Core** | Operator Precedence | VERIFIED | PEMDAS / BODMAS adherence with AST |
| **Core** | Exponentiation | VERIFIED | Right-associative (`2^3^2 = 512`) |
| **Core** | Implicit Multiplication | VERIFIED | `2(3) = 6`, `2π = 6.28318...` |
| **Core** | Memory Functions | VERIFIED | `MC`, `MR`, `M+`, `M-`, `MS` slots |
| **Core** | Angle Modes | VERIFIED | Degrees (`DEG`) & Radians (`RAD`) |
| **Core** | Responsive Display | VERIFIED | Auto-scaling font size, horizontal scroll, copy action |
| **Scientific** | Trigonometric | VERIFIED | `sin, cos, tan, asin, acos, atan` with exact zeros |
| **Scientific** | Hyperbolic | VERIFIED | `sinh, cosh, tanh, asinh, acosh, atanh` |
| **Scientific** | Logarithms | VERIFIED | `log10, ln, log2` |
| **Scientific** | Roots & Powers | VERIFIED | `√`, `∛`, `xʸ`, `1/x`, `abs`, `exp`, `mod` |
| **Scientific** | Constants | VERIFIED | `π, e, φ (Golden Ratio)` |
| **Finance** | Indian GST | VERIFIED | 0, 5, 12, 18, 28%, custom %, CGST/SGST/IGST breakdown |
| **Finance** | Loan EMI | VERIFIED | Monthly EMI, total interest, amortization schedule |
| **Finance** | Simple & Compound Interest | VERIFIED | Annual, semi-annual, quarterly, monthly compounding |
| **Finance** | SIP Calculator | VERIFIED | Monthly wealth gain projections |
| **Finance** | Discount Calculator | VERIFIED | Single & double discount savings |
| **Finance** | Profit & Loss | VERIFIED | Profit/Loss %, Margin % vs Markup % |
| **Finance** | Break-Even | VERIFIED | Units & revenue volume analysis |
| **Converter** | Universal Converter | VERIFIED | 12 categories, affine temperature, swap & history |
| **Converter** | Currency Converter | VERIFIED | Multi-currency exchange rates with offline cache |
| **Programmer** | Multi-Base Converter | VERIFIED | Live simultaneous HEX, DEC, OCT, BIN |
| **Programmer** | Bit Widths | VERIFIED | QWORD (64), DWORD (32), WORD (16), BYTE (8) |
| **Programmer** | Bitwise Logic | VERIFIED | AND, OR, XOR, NOT, Shift Left, Shift Right |
| **Math** | Statistics | VERIFIED | Mean, median, mode, sample/population variance |
| **Math** | Fractions | VERIFIED | Addition, subtraction, multiplication, division, mixed |
| **Math** | Matrix Calculator | VERIFIED | Transpose, determinant, inverse, identity |
| **Math** | Equation Solvers | VERIFIED | Linear, quadratic (real & complex), 2x2 systems |
| **Math** | 2D Graphing Canvas | VERIFIED | Function plotting, zoom, pan, gridlines, axis markers |
| **Storage** | Calculation History | VERIFIED | Persistent Room SQLite with search & favorite stars |
| **Storage** | History Export | VERIFIED | Export to CSV via Android Intent Share Sheet |
| **Storage** | Favorite Formulas | VERIFIED | Custom formula storage & reuse |
| **Settings** | Theme & Personalization | VERIFIED | Dark, Light, System, 5 accent colors, precision |
| **Settings** | Privacy Policy & About | VERIFIED | Zero-telemetry policy, open source notices |

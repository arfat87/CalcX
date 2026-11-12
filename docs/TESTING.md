# CalcX — Testing Strategy & Quality Assurance

## 1. Testing Strategy Overview

Testing is an essential first-class requirement for CalcX. Because calculations require absolute precision, all core mathematical, financial, converter, programmer, and statistics operations are backed by automated JVM unit tests.

---

## 2. Test Execution Commands

```bash
# Execute all JVM unit test suites
gradle testDebugUnitTest

# Generate HTML test report
open app/build/reports/tests/testDebugUnitTest/index.html
```

---

## 3. Test Suites Summary

### 3.1 `CalculatorEngineTest` (`app/src/test/.../engine/CalculatorEngineTest.kt`)
- **Arithmetic:** `2 + 2 = 4`, `10 - 3 = 7`, `5 × 6 = 30`, `100 ÷ 4 = 25`.
- **Precedence:** `2 + 3 × 4 = 14`, `(2 + 3) × 4 = 20`.
- **Right-Associative Power:** `2 ^ 3 ^ 2 = 512` (not 64).
- **Trigonometric DEG/RAD:** `sin(30°) = 0.5`, `cos(60°) = 0.5`, `tan(45°) = 1.0`, `sin(180°) = 0.0`.
- **Logarithms:** `log(100) = 2.0`, `ln(e) = 1.0`, `log2(8) = 3.0`.
- **Implicit Multiplication:** `2(3) = 6`, `2π = 6.28318...`.
- **Error Handling:** `10 ÷ 0` throws `DivisionByZero`, `√(-9)` throws `DomainError`.
- **Memory Operations:** `store`, `recall`, `add`, `subtract`, `clear`.

### 3.2 `FinanceEngineTest` (`app/src/test/.../finance/FinanceEngineTest.kt`)
- **GST Exclusive:** ₹1,000 + 5% GST = ₹1,050 total, ₹25 CGST, ₹25 SGST.
- **GST Inclusive:** ₹1,050 at 5% GST = ₹1,000 taxable base, ₹50 GST.
- **GST Inter-State:** ₹1,000 at 18% IGST = ₹180 IGST, ₹0 CGST, ₹0 SGST.
- **EMI Standard:** Principal ₹100,000 at 12% per annum for 12 months = ₹8,884.88 EMI.
- **EMI Zero Interest:** Principal ₹12,000 at 0% for 12 months = ₹1,000.00 EMI, ₹0.00 interest.
- **Simple Interest:** ₹10,000 at 5% for 2 years = ₹1,000 interest, ₹11,000 total.
- **Discount:** ₹1,000 with 20% discount and 10% additional discount = ₹720 final price, ₹280 saved.
- **Profit & Loss:** CP 100, SP 120 = ₹20 profit, 20% markup, 16.67% margin.

### 3.3 `EnginesComprehensiveTest` (`app/src/test/.../advancedmath/EnginesComprehensiveTest.kt`)
- **Statistics:** Mean, median, mode, range, population variance, standard deviation.
- **Fractions:** Addition, subtraction, multiplication, division, mixed fractions, decimal-to-fraction.
- **Matrix:** Determinant of 2x2, transpose, inverse matrix multiplication yielding identity.
- **Equation Solvers:** Linear equations, quadratic roots (two real roots, repeated, and complex), 2x2 linear systems.
- **Unit Conversions:** Length (km to m, mile to m), Weight (kg to g), Temperature (100°C to 212°F, 0°C to 273.15 K), Data (GB to MB).
- **Programmer Calculator:** Bitwise AND, OR, XOR, NOT, shift left/right, formatted radix representation.

---

## 4. Test Verification Results

All automated test suites execute and pass with `BUILD SUCCESSFUL` across all modules.

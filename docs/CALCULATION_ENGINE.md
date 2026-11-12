# CalcX — Calculation Engine Specification

## 1. Engine Architecture

The CalcX calculation engine parses and evaluates arithmetic and mathematical expressions using a multi-stage compilation pipeline:

```
[ User Input String ]
        │
        ▼
[ Lexer / Tokenizer ] ──► Injects Implicit Multiplication (e.g. 2(3) -> 2 * 3)
        │
        ▼
[ Recursive-Descent Parser ] ──► Builds Abstract Syntax Tree (AST)
        │
        ▼
[ AstNode Tree ] ──► Precedence: Postfix > Unary > Power (Right) > Mult/Div > Add/Sub
        │
        ▼
[ Evaluator ] ──► AngleMode-aware, Floating-point clean zero rounding, Domain checks
        │
        ▼
[ NumberFormatter ] ──► Dynamic grouping, trailing zero truncation, scientific notation
        │
        ▼
[ UI State / History Persistence ]
```

---

## 2. Operator Precedence Hierarchy

| Level | Operators | Associativity | Example |
|---|---|---|---|
| **1 (Highest)** | Primary, Constants, Parentheses | N/A | `(2 + 3)`, `π`, `e` |
| **2** | Function Calls | Prefix | `sin(30)`, `sqrt(144)`, `log(100)` |
| **3** | Postfix Operators | Left-to-Right | `5!`, `100 × 5%` |
| **4** | Unary Operators | Right-to-Left | `-5`, `+3` |
| **5** | Exponentiation `^` | **Right-to-Left** | `2 ^ 3 ^ 2 = 2 ^ 9 = 512` |
| **6** | Multiplication `×`, Division `÷`, Modulo `mod` | Left-to-Right | `10 ÷ 2 × 3 = 15` |
| **7 (Lowest)** | Addition `+`, Subtraction `-` | Left-to-Right | `2 + 3 × 4 = 14` |

---

## 3. Trigonometric Clean Rounding

In standard IEEE 754 floating-point arithmetic, computing `sin(Math.toRadians(180.0))` results in `1.2246467991473532e-16` instead of `0.0`.

CalcX solves this via exact trigonometric normalizers:
- When `angleMode == DEG`:
  - `sin(0°) = 0`, `sin(90°) = 1`, `sin(180°) = 0`, `sin(270°) = -1`
  - `cos(0°) = 1`, `cos(90°) = 0`, `cos(180°) = -1`, `cos(270°) = 0`
  - `tan(0°) = 0`, `tan(45°) = 1`, `tan(180°) = 0`, `tan(90°)` throws `CalculationError.DomainError`.
- For results where `abs(value) < 1e-15`, the engine snaps the result cleanly to `0.0`.

---

## 4. Structured Error Handling

CalcX defines a hierarchy of sealed exceptions that prevent runtime crashes:

- `CalculationError.DivisionByZero`: Triggers on `10 ÷ 0` or modulo by 0.
- `CalculationError.DomainError`: Triggers on negative square root `√(-4)`, `log(0)`, or non-integer exponentiation of negative bases.
- `CalculationError.FactorialLimitExceeded`: Triggers when computing `n!` for `n > 170` to prevent IEEE 754 overflow.
- `CalculationError.MismatchedParentheses`: Triggers when `(` does not match `)`.
- `CalculationError.InvalidSyntax`: Triggers on incomplete or malformed expressions.

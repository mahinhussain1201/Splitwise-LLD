# Splitwise - Low-Level System Design (LLD) in Java

An object-oriented implementation of the **Splitwise** expense-sharing system in Java, demonstrating clean architecture, SOLID principles, design patterns, and comprehensive balance-sheet accounting.

---

## 🌟 Key Features

- **Multiple Split Strategies**:
  - **Equal Split**: Splits the total amount equally among all participants.
  - **Unequal Split**: Splits by exact amounts per participant with total amount validation.
  - **Percentage Split**: Splits based on custom percentage allocations with 100% validation.
- **Accurate Balance Sheet Accounting**:
  - Pairwise directed graph tracking (`balanceSheet[A][B] = +X` means A gets back X from B; `balanceSheet[B][A] = -X` means B owes X to A).
  - Handles edge cases like payers participating in their own expense without self-debt corruption.
- **Rich Tabular Visualizations**:
  - **Balance Sheet Matrix**: A 2D grid representation showing who owes whom, along with each user's **Net Balance**.
  - **Outstanding Dues Summary**: An itemized list of all active debts between parties without duplicate/inverted rows.

---

## 🏗️ Design Patterns Applied

| Pattern | Component | Description |
| :--- | :--- | :--- |
| **Strategy Pattern** | `ISplitStrategy`, `EqualSplitStrategy`, `UnequalSplitStrategy`, `PercentageSplitStrategy` | Encapsulates split calculation and validation algorithms into interchangeable strategy classes. |
| **Builder Pattern** | `Expense.Builder` | Provides a fluent, safe API for constructing `Expense` objects with validation before creation. |
| **Singleton Pattern** | `ExpenseManager` | Ensures a single centralized manager maintains the global user registry and balance sheets. |

---

## 📂 Project Structure

```text
Splitwise/
├── src/
│   ├── Expense/
│   │   ├── Expense.java              # Expense entity with Builder pattern
│   │   ├── ExpenseManager.java       # Singleton managing users, expenses & tables
│   │   └── ExpenseType.java          # Enum (EQUAL, UNEQUAL, PERCENTAGE)
│   ├── Split/
│   │   ├── ISplitStrategy.java       # Strategy interface for split logic
│   │   ├── EqualSplitStrategy.java   # Strategy for equal distribution
│   │   ├── UnequalSplitStrategy.java # Strategy for exact amounts
│   │   ├── PercentageSplitStrategy.java # Strategy for percentage splits
│   │   └── Split.java                # Split entity (User + amount owed)
│   ├── User/
│   │   └── User.java                 # User entity (ID, Name, Email)
│   └── Main.java                     # Application entry point & demo scenarios
├── .gitignore
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

- **Java JDK 11** or higher.

### Compilation

From the root directory (`Splitwise/`), compile all Java source files:

```bash
javac src/User/*.java src/Split/*.java src/Expense/*.java src/Main.java
```

### Running the Application

Execute the compiled classes:

```bash
java -cp src Main
```

---

## 💻 Sample Output

###  Balance Sheet Matrix Table
```text
=========================== BALANCE SHEET MATRIX ===========================
Note: Positive (+) means row user gets back money; Negative (-) means owes.
+--------------+--------------+--------------+--------------+--------------+--------------+
| User         | Alice        | Bob          | Charlie      | Alpha        | Net Balance  |
+--------------+--------------+--------------+--------------+--------------+--------------+
| Alice        | -            | +350.00      | +550.00      | +350.00      | +1250.00     |
| Bob          | -350.00      | -            | +100.00      | +100.00      | -150.00      |
| Charlie      | -550.00      | -100.00      | -            | 0.00         | -650.00      |
| Alpha        | -350.00      | -100.00      | 0.00         | -            | -450.00      |
+--------------+--------------+--------------+--------------+--------------+--------------+
```

### 3. Outstanding Dues Summary Table
```text
======================= OUTSTANDING DUES SUMMARY =======================
+----------------------+----------------------+-----------------+----------------------------------------+
| Debtor (Owes)        | Creditor (Gets Back) | Amount          | Status Summary                         |
+----------------------+----------------------+-----------------+----------------------------------------+
| Bob                  | Alice                | 350.00          | Bob owes Alice 350.00                  |
| Charlie              | Alice                | 550.00          | Charlie owes Alice 550.00              |
| Charlie              | Bob                  | 100.00          | Charlie owes Bob 100.00                |
| Alpha                | Alice                | 350.00          | Alpha owes Alice 350.00                |
| Alpha                | Bob                  | 100.00          | Alpha owes Bob 100.00                  |
+----------------------+----------------------+-----------------+----------------------------------------+
```

---

## 🛡️ Edge Cases & Validations Handled

1. **Floating Point Precision**: Epsilon checks (`|sum - total| < 0.00001`) prevent false validation failures with floating-point arithmetic.
2. **Self-Balancing Exclusion**: When a payer is also a participant in the expense, internal self-balances are skipped rather than recording a dummy balance with oneself.
3. **Array Bounds and Null Safety**: Pre-validation ensures participant and split value lists are non-null and match in size before performing mathematical splits.
4. **Formatted Amounts**: Balances display cleanly formatted currency amounts rather than negative owed values (e.g. `Bob owes 250.00 to Alice` instead of `Bob owes -250.00 to Alice`).

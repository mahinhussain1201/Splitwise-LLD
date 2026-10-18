import java.util.List;

import Expense.Expense;
import Expense.ExpenseManager;
import Expense.ExpenseType;
import User.User;

public class Main {
    public static void main(String args[]){
        ExpenseManager expenseManager = ExpenseManager.getInstance();
        User user1 = expenseManager.createUser("Alice", "Alice@gmail.com");
        User user2 = expenseManager.createUser("Bob", "bob@gmail.com");
        User user3 = expenseManager.createUser("Charlie", "c@gmail.com");
        User user4 = expenseManager.createUser("Alpha", "alpha@gmail.com");

        System.out.println("\n--- Adding Equal Expense (Dinner: 1000) ---");
        Expense dinner = new Expense.Builder()
                        .expenseType(ExpenseType.EQUAL)
                        .description("Dinner")
                        .paidBy(user1)
                        .participants(List.of(user1, user2, user3, user4))
                        .totalAmount(1000)
                        .build();
        expenseManager.addExpense(dinner);
        expenseManager.showBalance(user1.getId());
        expenseManager.showBalance(user2.getId());

        System.out.println("\n--- Adding Unequal Expense (Movie: 1000) ---");
        Expense movie = new Expense.Builder()
                        .expenseType(ExpenseType.UNEQUAL)
                        .description("Movie")
                        .paidBy(user1)
                        .participants(List.of(user1, user2, user3, user4))
                        .totalAmount(1000)
                        .splitValues(List.of(400.0, 200.0, 300.0, 100.0))
                        .build();
        expenseManager.addExpense(movie);
        expenseManager.showBalance(user1.getId());
        expenseManager.showBalance(user2.getId());

        System.out.println("\n--- Adding Percentage Expense (Snacks: 500) ---");
        Expense snacks = new Expense.Builder()
                        .expenseType(ExpenseType.PERCENTAGE)
                        .description("Snacks")
                        .paidBy(user2)
                        .participants(List.of(user1, user2, user3, user4))
                        .totalAmount(500)
                        .splitValues(List.of(20.0, 40.0, 20.0, 20.0))
                        .build();
        expenseManager.addExpense(snacks);

        System.out.println();
        expenseManager.showAllBalances();

        expenseManager.showBalanceSheetTable();
    }
}
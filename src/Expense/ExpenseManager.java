package Expense;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Split.Split;
import User.User;

public class ExpenseManager {
    private static final ExpenseManager INSTANCE = new ExpenseManager();

    private final Map<String, User> users = new LinkedHashMap<>();
    private final Map<String, Map<String, Double>> balanceSheet = new LinkedHashMap<>();

    private ExpenseManager() {
    }

    public static ExpenseManager getInstance() {
        return INSTANCE;
    }

    public User createUser(String name, String email) {
        User user = new User(name, email);
        users.put(user.getId(), user);
        balanceSheet.put(user.getId(), new LinkedHashMap<>());
        System.out.println("Successfully created user " + user);
        return user;
    }

    public void addExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense cannot be null");
        }
        User payer = expense.getPaidBy();
        balanceSheet.putIfAbsent(payer.getId(), new LinkedHashMap<>());

        for (Split split : expense.getSplits()) {
            User participant = split.getUser();
            if (payer.getId().equals(participant.getId())) {
                continue; // Skip self-balance
            }
            balanceSheet.putIfAbsent(participant.getId(), new LinkedHashMap<>());

            double amountOwed = split.getAmountOwed();

            balanceSheet.get(payer.getId()).put(participant.getId(),
                balanceSheet.get(payer.getId()).getOrDefault(participant.getId(), 0.0) + amountOwed);
            balanceSheet.get(participant.getId()).put(payer.getId(),
                balanceSheet.get(participant.getId()).getOrDefault(payer.getId(), 0.0) - amountOwed);
        }
        System.out.println("Expense Added Successfully");
    }

    public void showBalance(String userId) {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("User not found");
            return;
        }
        System.out.println("Printing balance sheet for " + user);
        Map<String, Double> balanceForUser = balanceSheet.get(user.getId());
        if (balanceForUser == null || balanceForUser.isEmpty()) { 
            System.out.println("No balance for the user");
            return;
        }

        boolean hasBalance = false;
        for (Map.Entry<String, Double> entry : balanceForUser.entrySet()) {
            double amount = entry.getValue();
            if (Math.abs(amount) < 0.00001) {
                continue;
            }
            hasBalance = true;
            String user1 = user.getName();
            User otherUser = users.get(entry.getKey());
            String user2 = (otherUser != null) ? otherUser.getName() : entry.getKey();

            if (amount > 0) {
                System.out.println(user1 + " gets back " + amount + " from " + user2);
            } else {
                System.out.println(user1 + " owes " + Math.abs(amount) + " to " + user2);
            }
        }
        if (!hasBalance) {
            System.out.println("No balance for the user");
        }
    }

    public void showAllBalances() {
        System.out.println("=== Balances for all users ===");
        for (User user : users.values()) {
            showBalance(user.getId());
        }
    }

    /**
     * Prints the balance sheet in a tabular matrix format (Rows: User, Cols: Other Users + Net Balance),
     * followed by an outstanding dues summary table.
     */
    public void showBalanceSheetTable() {
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        List<User> userList = new ArrayList<>(users.values());
        int nameColWidth = 14;
        for (User u : userList) {
            nameColWidth = Math.max(nameColWidth, u.getName().length() + 2);
        }
        int dataColWidth = 14;

        StringBuilder separator = new StringBuilder("+");
        separator.append("-".repeat(nameColWidth)).append("+");
        for (int i = 0; i < userList.size(); i++) {
            separator.append("-".repeat(dataColWidth)).append("+");
        }
        separator.append("-".repeat(dataColWidth)).append("+");

        System.out.println("\n=========================== BALANCE SHEET MATRIX ===========================");
        System.out.println("Note: Positive (+) means row user gets back money; Negative (-) means owes.");
        System.out.println(separator);
        System.out.printf("| %-" + (nameColWidth - 2) + "s |", "User");
        for (User u : userList) {
            System.out.printf(" %-" + (dataColWidth - 2) + "s |", u.getName());
        }
        System.out.printf(" %-" + (dataColWidth - 2) + "s |\n", "Net Balance");
        System.out.println(separator);

        for (User rowUser : userList) {
            System.out.printf("| %-" + (nameColWidth - 2) + "s |", rowUser.getName());
            Map<String, Double> rowBalances = balanceSheet.getOrDefault(rowUser.getId(), Collections.emptyMap());
            double netBalance = 0.0;

            for (User colUser : userList) {
                if (rowUser.getId().equals(colUser.getId())) {
                    System.out.printf(" %-" + (dataColWidth - 2) + "s |", "-");
                } else {
                    double balance = rowBalances.getOrDefault(colUser.getId(), 0.0);
                    netBalance += balance;
                    String formatted;
                    if (Math.abs(balance) < 0.00001) {
                        formatted = "0.00";
                    } else if (balance > 0) {
                        formatted = String.format("+%.2f", balance);
                    } else {
                        formatted = String.format("%.2f", balance);
                    }
                    System.out.printf(" %-" + (dataColWidth - 2) + "s |", formatted);
                }
            }

            String netFormatted;
            if (Math.abs(netBalance) < 0.00001) {
                netFormatted = "0.00";
            } else if (netBalance > 0) {
                netFormatted = String.format("+%.2f", netBalance);
            } else {
                netFormatted = String.format("%.2f", netBalance);
            }
            System.out.printf(" %-" + (dataColWidth - 2) + "s |\n", netFormatted);
        }
        System.out.println(separator);

        showDuesTable();
    }

    /**
     * Prints an itemized table of all outstanding dues between users.
     */
    public void showDuesTable() {
        System.out.println("\n======================= OUTSTANDING DUES SUMMARY =======================");
        String border = "+----------------------+----------------------+-----------------+----------------------------------------+";
        System.out.println(border);
        System.out.printf("| %-20s | %-20s | %-15s | %-38s |\n", "Debtor (Owes)", "Creditor (Gets Back)", "Amount", "Status Summary");
        System.out.println(border);

        boolean hasDues = false;
        for (User u1 : users.values()) {
            Map<String, Double> balances = balanceSheet.getOrDefault(u1.getId(), Collections.emptyMap());
            for (Map.Entry<String, Double> entry : balances.entrySet()) {
                double amount = entry.getValue();
                if (amount < -0.00001) {
                    hasDues = true;
                    User u2 = users.get(entry.getKey());
                    String creditorName = (u2 != null) ? u2.getName() : entry.getKey();
                    double dueAmount = Math.abs(amount);
                    String summary = String.format("%s owes %s %.2f", u1.getName(), creditorName, dueAmount);
                    System.out.printf("| %-20s | %-20s | %-15.2f | %-38s |\n", u1.getName(), creditorName, dueAmount, summary);
                }
            }
        }
        if (!hasDues) {
            System.out.printf("| %-98s |\n", "No outstanding dues between users! All settled up.");
        }
        System.out.println(border);
    }

    public void printBalanceSheetTable() {
        showBalanceSheetTable();
    }
}

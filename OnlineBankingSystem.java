import java.util.*;

class User {
    private String username;
    private String password;
    private String email;
    private double balance;
    private List<String> transactionHistory;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory.add("Deposited: $" + amount);
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactionHistory.add("Withdrew: $" + amount);
            return true;
        }
        return false;
    }

    public boolean transfer(User recipient, double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            recipient.deposit(amount);
            transactionHistory.add("Transferred: $" + amount + " to " + recipient.getUsername());
            recipient.addTransaction("Received: $" + amount + " from " + username);
            return true;
        }
        return false;
    }

    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    public double getBalance() {
        return balance;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }

    public String getEmail() {
        return email;
    }
}

public class OnlineBankingSystem {
    private static Map<String, User> users = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Online Banking System ===");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Thank you for using the Online Banking System.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void createAccount() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please try another.");
            return;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        users.put(username, new User(username, password, email));
        System.out.println("Account created successfully.");
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = users.get(username);
        if (user != null && user.authenticate(password)) {
            System.out.println("Login successful.");
            userDashboard(user);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void userDashboard(User user) {
        while (true) {
            System.out.println("\n=== Dashboard ===");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit Funds");
            System.out.println("3. Withdraw Funds");
            System.out.println("4. Transfer Funds");
            System.out.println("5. View Transaction History");
            System.out.println("6. Update Email");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Current Balance: $" + user.getBalance());
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    user.deposit(depositAmount);
                    System.out.println("Deposited successfully.");
                    break;
                case 3:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    if (user.withdraw(withdrawAmount)) {
                        System.out.println("Withdrawn successfully.");
                    } else {
                        System.out.println("Insufficient funds.");
                    }
                    break;
                case 4:
                    System.out.print("Enter recipient username: ");
                    String recipientUsername = scanner.nextLine();
                    User recipient = users.get(recipientUsername);
                    if (recipient == null) {
                        System.out.println("Recipient not found.");
                        break;
                    }
                    System.out.print("Enter amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    if (user.transfer(recipient, transferAmount)) {
                        System.out.println("Transferred successfully.");
                    } else {
                        System.out.println("Transfer failed. Check your balance.");
                    }
                    break;
                case 5:
                    System.out.println("Transaction History:");
                    for (String transaction : user.getTransactionHistory()) {
                        System.out.println(transaction);
                    }
                    break;
                case 6:
                    System.out.print("Enter new email: ");
                    String newEmail = scanner.nextLine();
                    user.updateEmail(newEmail);
                    System.out.println("Email updated successfully.");
                    break;
                case 7:
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}

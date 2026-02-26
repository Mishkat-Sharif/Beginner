import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Account class
class Account {
    private double balance;
    private int pin;

    public Account(double balance, int pin) {
        this.balance = balance;
        this.pin = pin;
    }

    public boolean validatePin(int enteredPin) {
        return this.pin == enteredPin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

// Login Frame
class LoginFrame extends JFrame implements ActionListener {
    private JTextField pinField;
    private JButton loginBtn;
    private JLabel messageLabel;
    private Account account;

    public LoginFrame(Account account) {
        this.account = account;
        setTitle("Mishkat Bank - Login");
        setSize(350, 200);
        setLayout(new GridLayout(4, 1, 5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Welcome to Mishkat Bank ATM", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel pinLabel = new JLabel("Enter your PIN:", SwingConstants.CENTER);
        pinField = new JTextField(10);
        loginBtn = new JButton("Login");
        messageLabel = new JLabel("", SwingConstants.CENTER);

        loginBtn.addActionListener(this);

        add(title);
        add(pinLabel);
        add(pinField);
        add(loginBtn);
        add(messageLabel);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            int enteredPin = Integer.parseInt(pinField.getText());
            if (account.validatePin(enteredPin)) {
                messageLabel.setText("✅ Login Successful!");
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new ATMFrame(account).setVisible(true);
                dispose();
            } else {
                messageLabel.setText("❌ Wrong PIN!");
            }
        } catch (NumberFormatException ex) {
            messageLabel.setText("⚠️ Please enter numbers only!");
        }
    }
}

// ATM Main Dashboard Frame
class ATMFrame extends JFrame implements ActionListener {
    private Account account;
    private JTextField amountField;
    private JTextArea display;
    private JButton checkBtn, depositBtn, withdrawBtn, logoutBtn;

    public ATMFrame(Account account) {
        this.account = account;
        setTitle("Mishkat Bank - ATM");
        setSize(500, 400);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Top message
        JLabel title = new JLabel("Mishkat Bank ATM", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        // Display area
        display = new JTextArea();
        display.setEditable(false);
        display.setFont(new Font("Monospaced", Font.PLAIN, 13));
        display.append("Welcome! Use buttons below to perform actions.\n");
        add(new JScrollPane(display), BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 3, 10, 10));

        amountField = new JTextField();
        checkBtn = new JButton("Check Balance");
        depositBtn = new JButton("Deposit");
        withdrawBtn = new JButton("Withdraw");
        logoutBtn = new JButton("Logout");

        checkBtn.addActionListener(this);
        depositBtn.addActionListener(this);
        withdrawBtn.addActionListener(this);
        logoutBtn.addActionListener(this);

        controlPanel.add(new JLabel("Amount:"));
        controlPanel.add(amountField);
        controlPanel.add(depositBtn);
        controlPanel.add(withdrawBtn);
        controlPanel.add(checkBtn);
        controlPanel.add(logoutBtn);

        add(controlPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == checkBtn) {
            display.append("💰 Balance: " + account.getBalance() + " BDT\n");
        } else if (e.getSource() == depositBtn) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                account.deposit(amount);
                display.append("✅ Deposited " + amount + " BDT\n");
            } catch (NumberFormatException ex) {
                display.append("⚠️ Enter valid amount!\n");
            }
        } else if (e.getSource() == withdrawBtn) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (account.withdraw(amount))
                    display.append("✅ Withdrawn " + amount + " BDT\n");
                else
                    display.append("❌ Not enough balance!\n");
            } catch (NumberFormatException ex) {
                display.append("⚠️ Enter valid amount!\n");
            }
        } else if (e.getSource() == logoutBtn) {
            JOptionPane.showMessageDialog(this, "Logged out successfully!");
            new LoginFrame(account).setVisible(true);
            dispose();
        }
    }
}

// Main class
public class ATM_GUI_Login {
    public static void main(String[] args) {
        Account account = new Account(10000, 1234);
        LoginFrame login = new LoginFrame(account);
        login.setVisible(true);
    }
}
import java.awt.*;
import javax.swing.*;

public class Login extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;

    // Simple In-Memory Registration for Prototype
    private String registeredEmail = "test"; // Default for testing
    private String registeredPassword = "123";
    private String registeredRole = "Coordinator"; 
    
    public Login() {
        setTitle("FCI Seminar Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("FCI Postgraduate Seminar System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel.add(new JLabel("Email / Name:"));
        emailField = new JTextField();
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        formPanel.add(new JLabel("Role:"));
        roleCombo = new JComboBox<>(new String[]{"Student", "Evaluator", "Coordinator"});
        formPanel.add(roleCombo);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton registerBtn = new JButton("Register");
        JButton loginBtn = new JButton("Login");
        
        registerBtn.addActionListener(e -> {
            registeredEmail = emailField.getText();
            registeredPassword = new String(passwordField.getPassword());
            registeredRole = (String) roleCombo.getSelectedItem();
            JOptionPane.showMessageDialog(this, "Registered! You can now login.");
        });

        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleCombo.getSelectedItem();

            // Note: In a real app, check against a DB. 
            // For prototype, we check against the last registered user OR allow "Dr. Lim" bypass for testing
            boolean isMockEvaluator = false;
            for(String s : DataManager.mockEvaluators) { if(s.equals(email)) isMockEvaluator = true; }

            if ((email.equals(registeredEmail) && password.equals(registeredPassword) && role.equals(registeredRole)) 
                || (isMockEvaluator && role.equals("Evaluator"))) {
                
                // === FIXED: Set Session User ===
                DataManager.currentUser = email; 
                openMainFrame(role.toLowerCase());
                
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or Role mismatch.");
            }
        });
        
        buttonPanel.add(registerBtn);
        buttonPanel.add(loginBtn);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void openMainFrame(String role) {
        this.dispose(); 
        new Main(role); 
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Login();
        });
    }
}
import java.awt.*;
import javax.swing.*;

@SuppressWarnings("unused")
public class Login extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;

    private String registeredEmail = null;
    private String registeredPassword = null;
    private String registeredRole = null;
    
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
        
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        formPanel.add(new JLabel("Role:"));
        roleCombo = new JComboBox<>(new String[]{"Student", "Evaluator", "Coordinator"});
        formPanel.add(roleCombo);
        
        formPanel.add(new JLabel());
        formPanel.add(new JLabel());
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        JButton registerBtn = new JButton("Register");
        JButton loginBtn = new JButton("Login");
        registerBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        
        registerBtn.addActionListener(e -> {

            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleCombo.getSelectedItem();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            registeredEmail = email;
            registeredPassword = password;
            registeredRole = role;

            JOptionPane.showMessageDialog(this,
                    "Register successful.\nPlease login using the same email and password.");

            emailField.setText("");
            passwordField.setText("");
        });


        loginBtn.addActionListener(e -> {

            if (registeredEmail == null) {
                JOptionPane.showMessageDialog(this,
                        "Please register first!");
                return;
            }

            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleCombo.getSelectedItem();
            
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            if (!email.equals(registeredEmail)
                    || !password.equals(registeredPassword)
                    || !role.equals(registeredRole)) {

                JOptionPane.showMessageDialog(this,
                        "Invalid email, password or role!");
                return;
            }
            
            openMainFrame(role.toLowerCase());
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
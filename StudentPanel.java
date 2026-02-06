import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StudentPanel extends JPanel {

    private JPanel contentPanel;
    private JFrame parent;

    public StudentPanel(JFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        add(createSidebar(), BorderLayout.WEST);
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        showWelcome();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(240, 240, 240));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebar.setPreferredSize(new Dimension(200, 0));

        JLabel roleLabel = new JLabel("STUDENT");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(roleLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        addSidebarButton(sidebar, "Register for Seminar", e -> showStudentRegistration());
        addSidebarButton(sidebar, "Upload Materials", e -> showUploadMaterials());
        addSidebarButton(sidebar, "View My Submissions", e -> showMySubmissions());
        addSidebarButton(sidebar, "Log out", e -> showLogOut());

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private void addSidebarButton(JPanel panel, String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(new Color(220, 230, 240));
        button.setFocusPainted(false);
        button.addActionListener(listener);
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void showWelcome() {
        contentPanel.removeAll();
        JLabel welcomeLabel = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "<h1>Welcome, " + DataManager.currentUser + "!</h1>"
                        + "<p>Please select a function from the menu.</p>"
                        + "</div></html>",
                SwingConstants.CENTER);
        contentPanel.add(welcomeLabel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ---------------- Student functions ----------------

    private void showStudentRegistration() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Research Title:"));
        JTextField titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Abstract:"));
        JTextArea abstractArea = new JTextArea(3, 30);
        abstractArea.setLineWrap(true);
        formPanel.add(new JScrollPane(abstractArea));

        formPanel.add(new JLabel("Supervisor:"));
        JTextField supervisorField = new JTextField();
        formPanel.add(supervisorField);

        formPanel.add(new JLabel("Presentation Type:"));
        JComboBox<String> typeCombo = new JComboBox<>(
                new String[]{"Oral Presentation", "Poster Presentation"});
        formPanel.add(typeCombo);

        JButton submitBtn = new JButton("Submit Registration");
        submitBtn.setBackground(new Color(50, 150, 50));
        submitBtn.setForeground(Color.WHITE);

        submitBtn.addActionListener(e -> {
            String title = titleField.getText();
            String abs = abstractArea.getText();
            String supervisor = supervisorField.getText();
            String type = (String) typeCombo.getSelectedItem();
            
            // === FIXED: Uses actual logged-in user name ===
            String studentName = DataManager.currentUser; 

            Submission sub = new Submission(title, abs, supervisor, type, studentName);
            DataManager.allSubmissions.add(sub);

            JOptionPane.showMessageDialog(this, "Registration submitted successfully for: " + title);
        });

        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(submitBtn, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showUploadMaterials() {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Upload Materials (Same as before)", SwingConstants.CENTER));
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showMySubmissions() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Submission sub : DataManager.allSubmissions){
            // Filter only for this student
            if(sub.getStudentName().equals(DataManager.currentUser)) {
                listModel.addElement(sub.getTitle() + " [" + sub.getPresentationType() + "]");
            }
        }
        
        if (listModel.isEmpty()) listModel.addElement("No submissions found.");
        
        JList<String> displayList = new JList<>(listModel);
        contentPanel.add(new JLabel("Your Registered Submissions:", SwingConstants.CENTER), BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(displayList), BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showLogOut() {
        int result = JOptionPane.showConfirmDialog(this,"Are you sure you want to logout?","Logout",JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            parent.dispose();
            new Login();
        }
    }
}
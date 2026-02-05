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
        addSidebarButton(sidebar, "View Review Results", e -> showReviewResults());
        addSidebarButton(sidebar, "Personal Information", e -> showPersonalInfo());
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
                        + "<h1>Welcome, Student!</h1>"
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

        formPanel.add(new JLabel("Preferred Date:"));
        JTextField dateField = new JTextField("YYYY-MM-DD");
        formPanel.add(dateField);

        JButton submitBtn = new JButton("Submit Registration");
        submitBtn.setBackground(new Color(50, 150, 50));
        submitBtn.setForeground(Color.WHITE);

        submitBtn.addActionListener(e -> {

            String title = titleField.getText();
            String abs = abstractArea.getText();
            String supervisor = supervisorField.getText();
            String type = (String) typeCombo.getSelectedItem();
            
            Submission sub = new Submission(title, abs, supervisor, type);

            DataManager.allSubmissions.add(sub);

;            JOptionPane.showMessageDialog(
                    this,
                    "Registration submitted successfully for:" + title);
        });

        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(submitBtn, BorderLayout.SOUTH);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showUploadMaterials() {

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(new JLabel("Upload Presentation Materials", SwingConstants.CENTER));

        JButton selectBtn = new JButton("Select File (PDF/PPT/PNG)");
        selectBtn.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(
                        this,
                        "File selected: " + chooser.getSelectedFile().getName());
            }
        });

        panel.add(selectBtn);
        panel.add(new JLabel("File will be uploaded to the server for review."));

        JButton uploadBtn = new JButton("Confirm Upload");
        uploadBtn.setBackground(new Color(70, 130, 180));
        uploadBtn.setForeground(Color.WHITE);

        uploadBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Materials uploaded successfully!");
        });

        panel.add(uploadBtn);

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showMySubmissions() {
        showPlaceholder("My Submissions");
    }

    private void showReviewResults() {
        showPlaceholder("Review Results");
    }

    private void showPersonalInfo() {

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Personal Information"));

        panel.add(new JLabel("Name:"));
        panel.add(new JLabel("Student Name"));

        panel.add(new JLabel("Email:"));
        panel.add(new JLabel("student@fci.edu.my"));

        panel.add(new JLabel("Programme:"));
        panel.add(new JLabel("PhD in Computer Science"));

        panel.add(new JLabel("Status:"));
        panel.add(new JLabel("Active"));

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showLogOut() {

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            parent.dispose();
            new Login();
        }
    }

    private void showPlaceholder(String title) {

        contentPanel.removeAll();

        JLabel label = new JLabel(
                "<html><h2>" + title + "</h2>"
                        + "<p>This feature will be implemented with database.</p></html>",
                SwingConstants.CENTER);

        contentPanel.add(label, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}


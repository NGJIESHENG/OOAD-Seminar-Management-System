import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
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
        addSidebarButton(sidebar, "Vote People's Choice", e -> showVoting()); 
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

    // --- STUDENT FUNCTIONS ---

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

    // --- FIX: ALLOW SELECTION OF SPECIFIC SEMINAR ---
    private void showUploadMaterials() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        // 1. Get all submissions for the current user
        ArrayList<Submission> mySubmissions = new ArrayList<>();
        for(Submission s : DataManager.allSubmissions) {
            if(s.getStudentName().equals(DataManager.currentUser)) {
                mySubmissions.add(s);
            }
        }
        
        if (mySubmissions.isEmpty()) {
            contentPanel.add(new JLabel("You have no registered seminars. Please register first.", SwingConstants.CENTER));
            contentPanel.revalidate();
            contentPanel.repaint();
            return;
        }

        // 2. Create UI to Select Submission
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Select Submission to Upload For:"));
        
        JComboBox<Submission> submissionCombo = new JComboBox<>(mySubmissions.toArray(new Submission[0]));
        topPanel.add(submissionCombo);
        
        // 3. Create Upload Area (Initially Hidden or Disabled)
        JPanel uploadPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        uploadPanel.setBorder(BorderFactory.createEmptyBorder(20,100,20,100));
        
        JLabel statusLabel = new JLabel("Current File: None", SwingConstants.CENTER);
        JButton uploadBtn = new JButton("Choose File (PDF/PPT)");
        
        // Action Listener to update status when dropdown changes
        submissionCombo.addActionListener(e -> {
            Submission s = (Submission) submissionCombo.getSelectedItem();
            if(s != null) {
                statusLabel.setText("Current File: " + s.getPresentationFilePath());
            }
        });
        
        // Trigger once to set initial state
        if(submissionCombo.getItemCount() > 0) {
            Submission initial = (Submission) submissionCombo.getSelectedItem();
            statusLabel.setText("Current File: " + initial.getPresentationFilePath());
        }

        uploadBtn.addActionListener(e -> {
            Submission selectedSub = (Submission) submissionCombo.getSelectedItem();
            if (selectedSub == null) return;

            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(this);
            if(option == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                selectedSub.setPresentationFilePath(file.getAbsolutePath());
                statusLabel.setText("Current File: " + file.getName());
                JOptionPane.showMessageDialog(this, "File uploaded for: " + selectedSub.getTitle());
            }
        });

        uploadPanel.add(new JLabel("Upload Materials", SwingConstants.CENTER));
        uploadPanel.add(statusLabel);
        uploadPanel.add(uploadBtn);
        
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(uploadPanel, BorderLayout.CENTER);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showVoting() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Vote for People's Choice Award"));
        
        DefaultListModel<Submission> listModel = new DefaultListModel<>();
        for (Submission s : DataManager.allSubmissions) {
            // Don't vote for yourself
            if (!s.getStudentName().equals(DataManager.currentUser)){
                listModel.addElement(s);
            }
        }
        
        JList<Submission> list = new JList<>(listModel);
        JButton voteBtn = new JButton("Vote for Selected");
        
        voteBtn.addActionListener(e -> {
            Submission selected = list.getSelectedValue();
            if (selected != null) {
                selected.addVote(); // Logic handled in Submission class
                JOptionPane.showMessageDialog(this, "Vote cast for " + selected.getStudentName());
                voteBtn.setEnabled(false); // Disable after voting once per session (simple logic)
            }
        });
        
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        panel.add(voteBtn, BorderLayout.SOUTH);
        
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showMySubmissions() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Submission sub : DataManager.allSubmissions){
            if(sub.getStudentName().equals(DataManager.currentUser)) {
                listModel.addElement(sub.getTitle() + " [" + sub.getPresentationType() + "] Board: " + sub.getBoardID());
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
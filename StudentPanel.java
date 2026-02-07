import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class StudentPanel extends JPanel {
    private JFrame parent;
    private JPanel contentPanel;

    public StudentPanel(JFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(contentPanel, BorderLayout.CENTER);

        showWelcome();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(50, 50, 50));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel title = new JLabel("STUDENT");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(title);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        addSidebarButton(sidebar, "Register for Seminar", e -> showStudentRegistration());
        addSidebarButton(sidebar, "Upload Materials", e -> showUploadMaterials());
        addSidebarButton(sidebar, "Vote People's Choice", e -> showVoting());
        addSidebarButton(sidebar, "View My Submissions", e -> showMySubmissions());
        
        sidebar.add(Box.createVerticalGlue());
        addSidebarButton(sidebar, "Log out", e -> showLogOut());

        return sidebar;
    }

    private void addSidebarButton(JPanel sidebar, String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.addActionListener(action);
        sidebar.add(button);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void showWelcome() {
        contentPanel.removeAll();
        JLabel welcome = new JLabel("<html><div style='text-align:center;'><h1>Welcome, " + DataManager.currentUser + "!</h1><p>Please select a function from the menu.</p></div></html>", SwingConstants.CENTER);
        contentPanel.add(welcome, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showUploadMaterials() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        // 1. Get My Submissions
        ArrayList<Submission> mySubs = new ArrayList<>();
        for(Submission s : DataManager.allSubmissions) {
            if(s.getStudentName().equals(DataManager.currentUser)) mySubs.add(s);
        }

        if(mySubs.isEmpty()) {
            contentPanel.add(new JLabel("No registrations found. Please register first.", SwingConstants.CENTER));
            contentPanel.revalidate(); contentPanel.repaint();
            return;
        }

        JPanel center = new JPanel(new GridLayout(4, 1, 10, 10));
        JComboBox<Submission> subBox = new JComboBox<>(mySubs.toArray(new Submission[0]));
        JLabel statusLabel = new JLabel("Current File: " + mySubs.get(0).getPresentationFilePath(), SwingConstants.CENTER);
        JButton uploadBtn = new JButton("Choose File (PDF/PPT)");

        subBox.addActionListener(e -> {
            Submission s = (Submission) subBox.getSelectedItem();
            if(s != null) statusLabel.setText("Current File: " + s.getPresentationFilePath());
        });

        uploadBtn.addActionListener(e -> {
            Submission s = (Submission) subBox.getSelectedItem();
            JFileChooser fc = new JFileChooser();
            if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                s.setPresentationFilePath(f.getAbsolutePath());
                statusLabel.setText("Current File: " + f.getName());
                JOptionPane.showMessageDialog(this, "Uploaded: " + f.getName());
            }
        });

        center.add(new JLabel("Select Submission:", SwingConstants.CENTER));
        center.add(subBox);
        center.add(statusLabel);
        center.add(uploadBtn);
        
        contentPanel.add(center, BorderLayout.CENTER);
        contentPanel.revalidate(); contentPanel.repaint();
    }

    private void showVoting() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        
        DefaultListModel<Submission> model = new DefaultListModel<>();
        for(Submission s : DataManager.allSubmissions) {
            if(!s.getStudentName().equals(DataManager.currentUser)) model.addElement(s);
        }

        JList<Submission> list = new JList<>(model);
        JButton voteBtn = new JButton("Vote for Selected Peer");
        
        voteBtn.addActionListener(e -> {
            Submission s = list.getSelectedValue();
            if(s != null) {
                s.addVote();
                JOptionPane.showMessageDialog(this, "You voted for " + s.getStudentName());
                voteBtn.setEnabled(false);
            }
        });

        contentPanel.add(new JLabel("Select a peer to vote for (People's Choice):", SwingConstants.CENTER), BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(list), BorderLayout.CENTER);
        contentPanel.add(voteBtn, BorderLayout.SOUTH);
        
        contentPanel.revalidate(); contentPanel.repaint();
    }

    // --- NEW: Filter by Status & Date Logic ---
    private void showMySubmissions() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JLabel header = new JLabel("My Submissions");
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Filter Controls
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All Status", "Pending", "Evaluated"});
        JButton applyBtn = new JButton("Filter");
        
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilter);
        filterPanel.add(applyBtn);

        // Table
        String[] columns = {"Title", "Type", "Status", "Score"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        Runnable loadData = () -> {
            model.setRowCount(0);
            String selectedStatus = (String) statusFilter.getSelectedItem();

            for (Submission s : DataManager.allSubmissions) {
                // Filter 1: Must belong to logged-in user
                if (s.getStudentName().equals(DataManager.currentUser)) {
                    
                    // Check Status by looking for an Evaluation
                    boolean isEvaluated = false;
                    int score = -1;
                    
                    for (Evaluation ev : DataManager.allEvaluations) {
                        // Matching logic: In this prototype, we match by Presenter Name
                        if (ev.getPresenterName().equals(s.getStudentName())) {
                            isEvaluated = true;
                            score = ev.getTotalScore();
                            break;
                        }
                    }

                    // Filter 2: Apply Status Filter
                    boolean match = true;
                    if ("Pending".equals(selectedStatus) && isEvaluated) match = false;
                    if ("Evaluated".equals(selectedStatus) && !isEvaluated) match = false;

                    if (match) {
                        model.addRow(new Object[]{
                            s.getTitle(),
                            s.getPresentationType(),
                            isEvaluated ? "Evaluated" : "Pending",
                            score == -1 ? "-" : score + "/40"
                        });
                    }
                }
            }
        };

        loadData.run();
        applyBtn.addActionListener(e -> loadData.run());

        JPanel top = new JPanel(new BorderLayout());
        top.add(header, BorderLayout.NORTH);
        top.add(filterPanel, BorderLayout.CENTER);

        contentPanel.add(top, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showStudentRegistration() {
        contentPanel.removeAll();
        contentPanel.setLayout(new GridLayout(6, 2, 10, 10));

        JTextField titleField = new JTextField();
        JTextArea abstractArea = new JTextArea();
        abstractArea.setLineWrap(true);
        JTextField supervisorField = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Oral Presentation", "Poster Presentation"});

        contentPanel.add(new JLabel("Research Title:")); contentPanel.add(titleField);
        contentPanel.add(new JLabel("Abstract:")); contentPanel.add(new JScrollPane(abstractArea));
        contentPanel.add(new JLabel("Supervisor:")); contentPanel.add(supervisorField);
        contentPanel.add(new JLabel("Presentation Type:")); contentPanel.add(typeBox);

        JButton submitBtn = new JButton("Submit Registration");
        submitBtn.addActionListener(e -> {
            Submission sub = new Submission(
                titleField.getText(), 
                abstractArea.getText(), 
                supervisorField.getText(), 
                (String)typeBox.getSelectedItem(), 
                DataManager.currentUser
            );
            DataManager.allSubmissions.add(sub);
            JOptionPane.showMessageDialog(this, "Registration submitted successfully for: " + titleField.getText());
        });

        contentPanel.add(new JLabel("")); contentPanel.add(submitBtn);
        contentPanel.revalidate(); contentPanel.repaint();
    }

    private void showLogOut() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            parent.dispose();
            new Login().setVisible(true);
        }
    }
}
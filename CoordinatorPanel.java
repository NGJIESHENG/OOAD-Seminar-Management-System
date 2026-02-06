import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*; // Added import

public class CoordinatorPanel extends JPanel {

    private JPanel contentPanel;
    private JFrame parent;

    public CoordinatorPanel(JFrame parent) {
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

        JLabel roleLabel = new JLabel("COORDINATOR");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(roleLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        addSidebarButton(sidebar, "Create Session", e -> showSessionCreation());
        addSidebarButton(sidebar, "Assign Evaluators", e -> showAssignEvaluators());
        addSidebarButton(sidebar, "Generate Reports", e -> showReportGeneration());
        addSidebarButton(sidebar, "Manage Awards", e -> showAwardManagement());
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
        JLabel label = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "<h1>Welcome, Coordinator!</h1>"
                        + "<p>Please select a function from the menu.</p>"
                        + "</div></html>",
                SwingConstants.CENTER);
        contentPanel.add(label, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ---------------- Coordinator functions ----------------

    private void showSessionCreation() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Create New Seminar Session"));

        panel.add(new JLabel("Session Date:"));
        JTextField dateField = new JTextField("2025-12-20");
        panel.add(dateField);

        panel.add(new JLabel("Venue:"));
        JTextField venueField = new JTextField("FCI Building, Room 101");
        panel.add(venueField);

        panel.add(new JLabel("Session Type:"));
        JComboBox<String> typeCombo = new JComboBox<>(
                new String[]{"Oral Session", "Poster Session", "Mixed"});
        panel.add(typeCombo);

        panel.add(new JLabel("Time Slot:"));
        JComboBox<String> timeCombo = new JComboBox<>(
                new String[]{"9:00-10:30", "10:45-12:15", "14:00-15:30", "15:45-17:15"});
        panel.add(timeCombo);

        panel.add(new JLabel("Max Presenters:"));
        JSpinner maxSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 50, 1));
        panel.add(maxSpinner);

        JButton createBtn = new JButton("Create Session");
        createBtn.setBackground(new Color(70, 130, 180));
        createBtn.setForeground(Color.WHITE);

        createBtn.addActionListener(e -> {
            String date = dateField.getText();
            String venue = venueField.getText();
            String type = (String) typeCombo.getSelectedItem();
            String time = (String) timeCombo.getSelectedItem();
            int max = (int) maxSpinner.getValue();
            
            Session newSession = new Session(date, venue, type, time, max);
            DataManager.allSessions.add(newSession);

            JOptionPane.showMessageDialog(this, "Session Saved! Total Session:"+ DataManager.allSessions.size());
        });

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.add(createBtn, BorderLayout.SOUTH);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showAssignEvaluators() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Assign Evaluators"));

        // === FIXED: Dynamic Data Loading ===
        
        // 1. Load registered Submissions
        ArrayList<String> submissionTitles = new ArrayList<>();
        for (Submission s : DataManager.allSubmissions) {
            submissionTitles.add(s.getTitle() + " (" + s.getStudentName() + ")");
        }
        
        String[] presenters = submissionTitles.toArray(new String[0]);
        JComboBox<String> presenterCombo = new JComboBox<>(presenters);
        
        // 2. Load Evaluators (Using Mock list for now)
        JComboBox<String> evaluatorCombo = new JComboBox<>(DataManager.mockEvaluators);

        panel.add(new JLabel("Select Submission:"));
        panel.add(presenterCombo);

        panel.add(new JLabel("Select Evaluator:"));
        panel.add(evaluatorCombo);

        panel.add(new JLabel("Role:"));
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Primary Evaluator", "Secondary Evaluator"});
        panel.add(roleCombo);

        JButton assignBtn = new JButton("Assign");
        assignBtn.setBackground(new Color(70, 130, 180));
        assignBtn.setForeground(Color.WHITE);

        assignBtn.addActionListener(e -> {
            if (presenterCombo.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this, "No submissions available to assign!");
                return;
            }

            int selectedIndex = presenterCombo.getSelectedIndex();
            Submission selectedSubmission = DataManager.allSubmissions.get(selectedIndex);
            String selectedEvaluator = (String) evaluatorCombo.getSelectedItem();

            // === FIXED: Save Assignment to DataManager ===
            Assignment newAssignment = new Assignment(selectedSubmission, selectedEvaluator);
            DataManager.allAssignments.add(newAssignment);

            JOptionPane.showMessageDialog(this, "Evaluator assigned successfully to " + selectedSubmission.getStudentName());
        });

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.add(assignBtn, BorderLayout.SOUTH);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showReportGeneration() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(new JLabel("Generate Reports", SwingConstants.CENTER));
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        String[] reportTypes = {
                "Full Seminar Report"
        };

        for (String report : reportTypes) {
            JButton reportBtn = new JButton("Generate: " + report);
            reportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            reportBtn.setMaximumSize(new Dimension(300, 40));

            reportBtn.addActionListener(e -> {
                if (report.equals("Full Seminar Report")){
                    String data = DataManager.generateFullSeminarReport();

                    JTextArea textArea = new JTextArea(data);
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(500, 400));

                    Object[] options = {"Download .txt", "Close"};
                    int choice = JOptionPane.showOptionDialog(this, scrollPane,
                        "Final Seminar Report",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null, options, options[0]
                    );
                    if (choice == JOptionPane.YES_OPTION){
                        DataManager.saveReportToFile(data, "Seminar_Report.txt");
                        JOptionPane.showMessageDialog(this , "Report saved as 'Seminar_Report.txt'!");
               } 
            }
            });

            panel.add(reportBtn);
        }

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showAwardManagement() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Manage Awards"));

        panel.add(new JLabel("Award Category:"));
        JComboBox<String> awardCombo = new JComboBox<>(
                new String[]{"Best Presentation", "Best Poster", "Best Research"});
        panel.add(awardCombo);

        JButton saveBtn = new JButton("Compute and Save Winner");
        saveBtn.setBackground(new Color(50, 150, 50));
        saveBtn.setForeground(Color.WHITE);

        saveBtn.addActionListener(e -> {
            Evaluation winner = DataManager.getBestOralPresenter();
            if (winner != null){
                String category = (String) awardCombo.getSelectedItem();
                JOptionPane.showMessageDialog(this, "Award: " + category + "\n" + "Winner: " + winner.getPresenterName() +
            "\n" + "Score: " + winner.getTotalScore() + "/40");

            }else{
                JOptionPane.showMessageDialog(this,"No evaluations found! Please have an Evaluator submit scores first.");
            }
            
        });

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.add(saveBtn, BorderLayout.SOUTH);
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
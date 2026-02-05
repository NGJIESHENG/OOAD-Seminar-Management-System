import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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

        panel.add(new JLabel("Presenter:"));
        JComboBox<String> presenterCombo = new JComboBox<>(
                new String[]{"John Doe", "Jane Smith"});
        panel.add(presenterCombo);

        panel.add(new JLabel("Evaluator:"));
        JComboBox<String> evaluatorCombo = new JComboBox<>(
                new String[]{"Dr. Lim", "Dr. Tan"});
        panel.add(evaluatorCombo);

        panel.add(new JLabel("Session:"));
        JComboBox<String> sessionCombo = new JComboBox<>(
                new String[]{"Session A", "Session B"});
        panel.add(sessionCombo);

        panel.add(new JLabel("Role:"));
        JComboBox<String> roleCombo = new JComboBox<>(
                new String[]{"Primary Evaluator", "Secondary Evaluator"});
        panel.add(roleCombo);

        JButton assignBtn = new JButton("Assign");
        assignBtn.setBackground(new Color(70, 130, 180));
        assignBtn.setForeground(Color.WHITE);

        assignBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Evaluator assigned successfully!");
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
                "Session Schedule Report",
                "Evaluation Summary Report",
                "Participant List",
                "Award Nomination Report",
                "Full Seminar Report"
        };

        for (String report : reportTypes) {

            JButton reportBtn = new JButton("Generate: " + report);
            reportBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            reportBtn.setMaximumSize(new Dimension(300, 40));

            reportBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(
                        this,
                        report + " generated successfully!\n(Simulated - would export to PDF in full version)");
            });

            panel.add(reportBtn);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
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

        panel.add(new JLabel("Nominee:"));
        JComboBox<String> nomineeCombo = new JComboBox<>(
                new String[]{"John Doe", "Jane Smith"});
        panel.add(nomineeCombo);

        panel.add(new JLabel("Evaluator Panel:"));
        JComboBox<String> panelCombo = new JComboBox<>(
                new String[]{"Panel A", "Panel B"});
        panel.add(panelCombo);

        panel.add(new JLabel("Decision:"));
        JComboBox<String> decisionCombo = new JComboBox<>(
                new String[]{"Winner", "Runner-up", "Shortlisted"});
        panel.add(decisionCombo);

        JButton saveBtn = new JButton("Compute Winner");
        saveBtn.setBackground(new Color(50, 150, 50));
        saveBtn.setForeground(Color.WHITE);

        saveBtn.addActionListener(e -> {
            Evaluation winner = DataManager.getBestOralPresenter();
            if (winner != null){
                JOptionPane.showMessageDialog(this, "The winner is: " + winner.getPrenterName() +
            "\nWith a total score of: " + winner.getTotalScore());

            }else{
                JOptionPane.showMessageDialog(this,"No evaluations found yet!");
            }
            
        });

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.add(saveBtn, BorderLayout.SOUTH);

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
}

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EvaluatorPanel extends JPanel {

    private JPanel contentPanel;
    private JFrame parent;

    public EvaluatorPanel(JFrame parent) {

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

        JLabel roleLabel = new JLabel("EVALUATOR");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(roleLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        addSidebarButton(sidebar, "Assigned Presentations", e -> showAssignedPresentations());
        addSidebarButton(sidebar, "Evaluate Submission", e -> showEvaluationForm());
        addSidebarButton(sidebar, "View Evaluation History", e -> showEvaluationHistory());
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
                        + "<h1>Welcome, Evaluator!</h1>"
                        + "<p>Please select a function from the menu.</p>"
                        + "</div></html>",
                SwingConstants.CENTER);

        contentPanel.add(label, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ---------------- Evaluator functions ----------------

    private void showAssignedPresentations() {

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        String[] columns = {"Presenter", "Title", "Session", "Status"};
        Object[][] data = {
                {"John Doe", "AI-based Learning Systems", "Session A", "Pending"},
                {"Jane Smith", "Data Mining for Healthcare", "Session B", "Evaluated"}
        };

        JTable table = new JTable(data, columns);

        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showEvaluationForm() {

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Evaluate Presentation"));

        panel.add(new JLabel("Presenter:"));
        panel.add(new JLabel("John Doe (PhD Candidate)"));

        panel.add(new JLabel("Research Title:"));
        panel.add(new JLabel("AI-based Learning Systems"));

        panel.add(new JLabel("Problem Clarity (1-10):"));
        JSpinner claritySpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        panel.add(claritySpinner);

        panel.add(new JLabel("Methodology (1-10):"));
        JSpinner methodSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        panel.add(methodSpinner);

        panel.add(new JLabel("Results (1-10):"));
        JSpinner resultSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        panel.add(resultSpinner);

        panel.add(new JLabel("Presentation (1-10):"));
        JSpinner presSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        panel.add(presSpinner);

        contentPanel.add(panel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        JTextArea commentArea = new JTextArea(3, 40);
        commentArea.setLineWrap(true);
        bottomPanel.add(new JScrollPane(commentArea));

        JButton submitBtn = new JButton("Submit Evaluation");
        submitBtn.setBackground(new Color(50, 150, 50));
        submitBtn.setForeground(Color.WHITE);

        submitBtn.addActionListener(e -> {

            int clarity = (int) claritySpinner.getValue();
            int method = (int) methodSpinner.getValue();
            int res = (int) resultSpinner.getValue();
            int pres = (int) presSpinner.getValue();
            String comments = commentArea.getText();
            String presenter = "John Doe";

            Evaluation newEval = new Evaluation(presenter, clarity, method, res, pres, comments);

            DataManager.allEvaluations.add(newEval);

            JOptionPane.showMessageDialog(
                    this,
                    "Evaluation submitted!\nTotal Score: " + newEval.getTotalScore()+ "/40\nComments saved.");
        });

        bottomPanel.add(submitBtn);

        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showEvaluationHistory() {

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        String[] columns = {"Presenter", "Title", "Score", "Date"};
        Object[][] data = {
                {"John Doe", "AI-based Learning Systems", "32/40", "2025-01-10"},
                {"Jane Smith", "Data Mining for Healthcare", "35/40", "2025-01-12"}
        };

        JTable table = new JTable(data, columns);

        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

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

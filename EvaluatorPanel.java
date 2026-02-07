import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class EvaluatorPanel extends JPanel {
    private JFrame parent;
    private JPanel contentPanel;

    public EvaluatorPanel(JFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout()); // Changed to BorderLayout for flexibility
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

        JLabel title = new JLabel("EVALUATOR");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(title);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        addSidebarButton(sidebar, "Assigned Presentations", e -> showAssignedPresentations());
        addSidebarButton(sidebar, "Evaluate Submission", e -> showEvaluationForm());
        
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

    // --- NEW: Search & Filter Logic Added Here ---
    private void showAssignedPresentations() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JLabel header = new JLabel("My Assignments");
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        // 1. Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        
        searchPanel.add(new JLabel("Search (Student/Title): "));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        // 2. Table Setup
        String[] columns = {"Presenter", "Title", "Type"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // 3. Load Data Function
        Runnable loadData = () -> {
            model.setRowCount(0); // Clear table
            String query = searchField.getText().toLowerCase();
            boolean found = false;

            for (Assignment assign : DataManager.allAssignments) {
                // Filter 1: Must be assigned to THIS evaluator
                if (assign.getEvaluatorName().equals(DataManager.currentUser)) {
                    
                    String sName = assign.getSubmission().getStudentName().toLowerCase();
                    String sTitle = assign.getSubmission().getTitle().toLowerCase();

                    // Filter 2: Search Query (Empty or Match)
                    if (query.isEmpty() || sName.contains(query) || sTitle.contains(query)) {
                        model.addRow(new Object[]{
                            assign.getSubmission().getStudentName(),
                            assign.getSubmission().getTitle(),
                            assign.getSubmission().getPresentationType()
                        });
                        found = true;
                    }
                }
            }
            if (!found && !query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No assignments found matching '" + query + "'");
            } else if (!found && query.isEmpty()) {
                model.addRow(new Object[]{"No assignments", "-", "-"});
            }
        };

        // Initial Load
        loadData.run();

        // Search Button Action
        searchBtn.addActionListener(e -> loadData.run());

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(header, BorderLayout.NORTH);
        topContainer.add(searchPanel, BorderLayout.SOUTH);

        contentPanel.add(topContainer, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showEvaluationForm() {
        contentPanel.removeAll();
        contentPanel.setLayout(new GridLayout(6, 1, 10, 10)); // Simple Grid for form

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p1.add(new JLabel("Select Student:"));
        
        // Only show students assigned to this evaluator
        ArrayList<String> myStudents = new ArrayList<>();
        for (Assignment a : DataManager.allAssignments) {
            if (a.getEvaluatorName().equals(DataManager.currentUser)) {
                myStudents.add(a.getSubmission().getStudentName());
            }
        }
        JComboBox<String> studentBox = new JComboBox<>(myStudents.toArray(new String[0]));
        p1.add(studentBox);

        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p2.add(new JLabel("Problem Clarity (1-10):"));
        JSpinner claritySpin = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        p2.add(claritySpin);

        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p3.add(new JLabel("Methodology (1-10):"));
        JSpinner methodSpin = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        p3.add(methodSpin);

        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p4.add(new JLabel("Results (1-10):"));
        JSpinner resultSpin = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        p4.add(resultSpin);

        JPanel p5 = new JPanel(new BorderLayout());
        p5.add(new JLabel("Comments:"), BorderLayout.NORTH);
        JTextArea comments = new JTextArea(3, 20);
        comments.setLineWrap(true);
        p5.add(new JScrollPane(comments), BorderLayout.CENTER);

        JButton submit = new JButton("Submit Evaluation");
        submit.setBackground(new Color(0, 102, 204));
        submit.setForeground(Color.WHITE);

        submit.addActionListener(e -> {
            if (studentBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "No student selected.");
                return;
            }
            String name = (String) studentBox.getSelectedItem();
            Evaluation ev = new Evaluation(
                name,
                (int) claritySpin.getValue(),
                (int) methodSpin.getValue(),
                (int) resultSpin.getValue(),
                (int) claritySpin.getValue(), // Using clarity as placeholder for Presentation score
                comments.getText()
            );
            DataManager.allEvaluations.add(ev);
            JOptionPane.showMessageDialog(this, "Evaluation submitted for " + name);
            showWelcome();
        });

        contentPanel.add(p1);
        contentPanel.add(p2);
        contentPanel.add(p3);
        contentPanel.add(p4);
        contentPanel.add(p5);
        contentPanel.add(submit);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showLogOut() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            parent.dispose();
            new Login().setVisible(true);
        }
    }
}
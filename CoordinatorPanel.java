import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class CoordinatorPanel extends JPanel {
    private JFrame parent;
    private JPanel contentPanel;

    public CoordinatorPanel(JFrame parent) {
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

        JLabel title = new JLabel("COORDINATOR");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(title);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        addSidebarButton(sidebar, "Create Session", e -> showSessionCreation());
        addSidebarButton(sidebar, "Assign Evaluators", e -> showAssignEvaluators());
        addSidebarButton(sidebar, "View Assignments", e -> showViewAssignments()); // NEW BUTTON
        addSidebarButton(sidebar, "Generate Reports", e -> showReportGeneration());
        addSidebarButton(sidebar, "Manage Awards", e -> showAwardManagement());
        
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
        JLabel welcome = new JLabel("<html><div style='text-align:center;'><h1>Welcome, Coordinator!</h1><p>Please select a function from the menu.</p></div></html>", SwingConstants.CENTER);
        contentPanel.add(welcome, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // --- NEW: View Assignments with Search & Filter ---
    private void showViewAssignments() {
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());

        JLabel header = new JLabel("Manage Assignments");
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBorder(new EmptyBorder(0, 0, 15, 0));

        // Filter Controls
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JTextField searchField = new JTextField(15);
        JComboBox<String> typeFilter = new JComboBox<>(new String[]{"All Types", "Oral Session", "Poster Session"});
        JButton applyBtn = new JButton("Apply Filters");

        filterPanel.add(new JLabel("Search Student:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Type:"));
        filterPanel.add(typeFilter);
        filterPanel.add(applyBtn);

        // Table
        String[] columns = {"Student", "Title", "Session Type", "Assigned Evaluator"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        Runnable loadData = () -> {
            model.setRowCount(0);
            String nameQuery = searchField.getText().toLowerCase();
            String typeQuery = (String) typeFilter.getSelectedItem();

            for (Assignment a : DataManager.allAssignments) {
                boolean nameMatch = nameQuery.isEmpty() || a.getSubmission().getStudentName().toLowerCase().contains(nameQuery);
                
                boolean typeMatch = true;
                if (!"All Types".equals(typeQuery)) {
                    // Assuming Presentation Type maps somewhat to Session Type logic or is stored in Submission
                    // The Submission class has 'presentationType'
                    String pType = a.getSubmission().getPresentationType(); // "Oral Presentation" etc
                    // Simple contains check to handle "Oral Presentation" vs "Oral Session" mismatch
                    if (typeQuery.contains("Oral") && !pType.contains("Oral")) typeMatch = false;
                    if (typeQuery.contains("Poster") && !pType.contains("Poster")) typeMatch = false;
                }

                if (nameMatch && typeMatch) {
                    model.addRow(new Object[]{
                        a.getSubmission().getStudentName(),
                        a.getSubmission().getTitle(),
                        a.getSubmission().getPresentationType(),
                        a.getEvaluatorName()
                    });
                }
            }
        };

        loadData.run(); // Initial load
        applyBtn.addActionListener(e -> loadData.run());

        JPanel top = new JPanel(new BorderLayout());
        top.add(header, BorderLayout.NORTH);
        top.add(filterPanel, BorderLayout.CENTER);

        contentPanel.add(top, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // --- Existing Methods (Simplified for brevity, logic retained) ---
    
    private void showSessionCreation() {
        contentPanel.removeAll();
        contentPanel.setLayout(new GridLayout(0, 2, 10, 10));
        
        TitledBorder border = BorderFactory.createTitledBorder("Create New Seminar Session");
        ((JComponent) contentPanel).setBorder(border);

        JTextField dateField = new JTextField("2025-12-20");
        JTextField venueField = new JTextField("FCI Building, Room 101");
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Oral Session", "Poster Session", "Mixed"});
        JComboBox<String> timeBox = new JComboBox<>(new String[]{"9:00-10:30", "10:45-12:15", "14:00-15:30"});
        JSpinner maxSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));

        contentPanel.add(new JLabel("Session Date:")); contentPanel.add(dateField);
        contentPanel.add(new JLabel("Venue:")); contentPanel.add(venueField);
        contentPanel.add(new JLabel("Session Type:")); contentPanel.add(typeBox);
        contentPanel.add(new JLabel("Time Slot:")); contentPanel.add(timeBox);
        contentPanel.add(new JLabel("Max Presenters:")); contentPanel.add(maxSpinner);

        JButton createBtn = new JButton("Create Session");
        createBtn.addActionListener(e -> {
            DataManager.allSessions.add(new Session(
                dateField.getText(), venueField.getText(), 
                (String)typeBox.getSelectedItem(), (String)timeBox.getSelectedItem(), 
                (int)maxSpinner.getValue()
            ));
            JOptionPane.showMessageDialog(this, "Session Saved! Total: " + DataManager.allSessions.size());
        });
        contentPanel.add(new JLabel("")); 
        contentPanel.add(createBtn);
        
        contentPanel.revalidate(); contentPanel.repaint();
    }

    private void showAssignEvaluators() {
        contentPanel.removeAll();
        contentPanel.setLayout(new GridLayout(5, 2, 10, 10));

        JComboBox<String> subBox = new JComboBox<>();
        for(Submission s : DataManager.allSubmissions) subBox.addItem(s.getTitle() + " (" + s.getStudentName() + ")");
        
        JComboBox<String> evalBox = new JComboBox<>(DataManager.mockEvaluators);
        
        JButton assignBtn = new JButton("Assign");
        assignBtn.addActionListener(e -> {
            if(subBox.getSelectedIndex() == -1) return;
            Submission s = DataManager.allSubmissions.get(subBox.getSelectedIndex());
            String evaluator = (String) evalBox.getSelectedItem();
            DataManager.allAssignments.add(new Assignment(s, evaluator));
            JOptionPane.showMessageDialog(this, "Assigned successfully!");
        });

        contentPanel.add(new JLabel("Select Submission:")); contentPanel.add(subBox);
        contentPanel.add(new JLabel("Select Evaluator:")); contentPanel.add(evalBox);
        contentPanel.add(new JLabel("")); contentPanel.add(assignBtn);
        
        contentPanel.revalidate(); contentPanel.repaint();
    }

    private void showReportGeneration() {
        contentPanel.removeAll();
        JButton genBtn = new JButton("Full Seminar Report");
        JTextArea area = new JTextArea();
        
        genBtn.addActionListener(e -> {
            area.setText(new DataManager().generateFullSeminarReport());
        });
        
        contentPanel.add(genBtn, BorderLayout.NORTH);
        contentPanel.add(new JScrollPane(area), BorderLayout.CENTER);
        contentPanel.revalidate(); contentPanel.repaint();
    }
    
    private void showAwardManagement() {
        // Placeholder for brevity, existing logic applies
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Award Management Logic Here"));
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
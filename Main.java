import java.awt.*;
import javax.swing.*;

public class Main extends JFrame {

    private JPanel contentPanel;

    public Main(String role) {

        setTitle("FCI Seminar System - " + role.toUpperCase());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createMenuBar();

        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        if (role.equals("student")) {
            contentPanel.add(new StudentPanel(this), BorderLayout.CENTER);
        }
        else if (role.equals("evaluator")) {
            contentPanel.add(new EvaluatorPanel(this), BorderLayout.CENTER);
        }
        else if (role.equals("coordinator")) {
            contentPanel.add(new CoordinatorPanel(this), BorderLayout.CENTER);
        }
        else {
            contentPanel.add(new JLabel("Unknown role"), BorderLayout.CENTER);
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            dispose();
            new Login();
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "FCI Seminar Management System\nPrototype Version",
                    "About",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
}


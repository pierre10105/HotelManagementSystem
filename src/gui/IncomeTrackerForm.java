package gui;

import models.IncomeTracker;
import controllers.DataStore;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class IncomeTrackerForm {

    private JFrame frame;
    private JTextArea reportArea;
    private JButton weeklyButton, monthlyButton, yearlyButton, customButton, backButton;
    private JTextField startDateField, endDateField;

    public IncomeTrackerForm() {
        initializeUI();
    }

    private void initializeUI() {
        // Main Frame
        frame = new JFrame("Income Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Top Panel: Buttons for Reports
        JPanel buttonPanel = createButtonPanel();
        frame.add(buttonPanel, BorderLayout.NORTH);

        // Center Panel: Report Area
        reportArea = new JTextArea(20, 50);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel: Custom Date Range Input and Back Button
        JPanel customRangePanel = createCustomRangePanel();
        frame.add(customRangePanel, BorderLayout.SOUTH);

        // Back Button
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose(); // Close the current form
            new ManagerDashboard(DataStore.loggedInUser); // Go back to Manager Dashboard
        });
        backPanel.add(backButton);
        frame.add(backPanel, BorderLayout.WEST);

        frame.setVisible(true);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Quick Reports"));

        weeklyButton = new JButton("Weekly Report");
        monthlyButton = new JButton("Monthly Report");
        yearlyButton = new JButton("Yearly Report");

        // Action Listeners
        weeklyButton.addActionListener(e -> showReportForPeriod("weekly"));
        monthlyButton.addActionListener(e -> showReportForPeriod("monthly"));
        yearlyButton.addActionListener(e -> showReportForPeriod("yearly"));

        panel.add(weeklyButton);
        panel.add(monthlyButton);
        panel.add(yearlyButton);

        return panel;
    }

    private JPanel createCustomRangePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Custom Date Range"));

        panel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        startDateField = new JTextField(10);
        panel.add(startDateField);

        panel.add(new JLabel("End Date (YYYY-MM-DD):"));
        endDateField = new JTextField(10);
        panel.add(endDateField);

        customButton = new JButton("Generate Report");
        customButton.addActionListener(e -> showCustomDateRangeReport());
        panel.add(customButton);

        return panel;
    }

    private void showReportForPeriod(String period) {
        LocalDate startDate = null;
        LocalDate endDate = LocalDate.now();

        // Set date ranges based on period
        switch (period) {
            case "weekly":
                startDate = endDate.minusDays(7);
                break;
            case "monthly":
                startDate = endDate.minusMonths(1);
                break;
            case "yearly":
                startDate = endDate.minusYears(1);
                break;
        }

        if (startDate != null) {
            double totalIncome = IncomeTracker.calculateIncomeByDateRange(startDate, endDate);
            reportArea.setText("=== " + period.toUpperCase() + " REPORT ===\n");
            reportArea.append("From: " + startDate + " To: " + endDate + "\n");
            reportArea.append("Total Income: $" + totalIncome + "\n\n");

            // Detailed report
            reportArea.append("Detailed Report:\n");
            reportArea.append("-------------------------------------------\n");
            IncomeTracker.generateIncomeReportForPeriod(reportArea, startDate, endDate);
        }
    }

    private void showCustomDateRangeReport() {
        try {
            LocalDate startDate = LocalDate.parse(startDateField.getText());
            LocalDate endDate = LocalDate.parse(endDateField.getText());

            double totalIncome = IncomeTracker.calculateIncomeByDateRange(startDate, endDate);
            reportArea.setText("=== CUSTOM DATE RANGE REPORT ===\n");
            reportArea.append("From: " + startDate + " To: " + endDate + "\n");
            reportArea.append("Total Income: $" + totalIncome + "\n\n");

            // Detailed report
            reportArea.append("Detailed Report:\n");
            reportArea.append("-------------------------------------------\n");
            IncomeTracker.generateIncomeReportForPeriod(reportArea, startDate, endDate);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(IncomeTrackerForm::new);
//    }
}

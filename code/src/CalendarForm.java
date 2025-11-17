/*
Displays the monthly calendar with navigation buttons to move between months.
Uses PanelDate to render the calendar grid.
 */

import javax.swing.*;
import java.time.YearMonth;
import java.time.format.TextStyle;    // for full month name display
import java.util.Locale;              // locale for month formatting

public class CalendarForm {
    private JPanel calendarScreen;           // root panel for the form
    private JPanel topPanel;            // panel for navigation buttons and month label

    private JPanel FoodExpiration;
    private JPanel calendarPanel;       // placeholder for the calendar grid
    private JButton nextMonthButton;         // button to go to next month
    private JButton prevMonthButton;         // button to go to previous month
    private JPanel monthPanel;          // nested panel to center month label
    private JLabel monthLabel;          // label displaying the current month/year
    private JButton prevYearButton;
    private JButton nextYearButton;
    private JPanel yearPanel;
    private JPanel bottomPanel;

    private PanelDate customCalendar;   // the actual calendar panel
    private YearMonth currentMonth;     // tracks currently displayed month

    // constructor that builds the form logic
    public CalendarForm() {
        currentMonth = YearMonth.now();           // initialize to current month
        customCalendar = new PanelDate(currentMonth); // create calendar panel

        // insert calendar into placeholder panel
        calendarPanel.setLayout(new java.awt.BorderLayout()); // layout for dynamic calendar
        calendarPanel.add(customCalendar, java.awt.BorderLayout.CENTER); // add calendar

        updateMonthLabel(); // display current month

        // action for previous month button
        prevMonthButton.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1); // move back one month
            customCalendar.setMonth(currentMonth);     // update calendar
            updateMonthLabel();                         // update label
        });

        // action for next month button
        nextMonthButton.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1); // move forward one month
            customCalendar.setMonth(currentMonth);     // update calendar
            updateMonthLabel();                         // update label
        });
    }

    // update the month label text
    private void updateMonthLabel() {
        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()); // full month name
        monthLabel.setText(monthName + " " + currentMonth.getYear()); // month + year
    }

    // This main function will only create the calendar, can be used for testing purposes
    public static void main(String[] args) {
        JFrame frame = new JFrame("Calendar");               // window title
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new CalendarForm().calendarScreen); // use the form’s main panel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close app on exit
        frame.pack();                                        // auto-size to fit component
        frame.setVisible(true);                              // show window
    }
}
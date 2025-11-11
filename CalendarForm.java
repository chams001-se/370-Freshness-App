import javax.swing.*;
import java.time.YearMonth;
import java.time.format.TextStyle;    // for full month name display
import java.util.Locale;              // locale for month formatting

public class CalendarForm {
    private JPanel mainPanel;           // root panel for the form
    private JPanel topPanel;            // panel for navigation buttons and month label
    private JPanel calendarPanel;       // placeholder for the calendar grid
    private JButton nextButton;         // button to go to next month
    private JButton prevButton;         // button to go to previous month
    private JPanel labelPanel;          // nested panel to center month label
    private JLabel monthLabel;          // label displaying the current month/year

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
        prevButton.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1); // move back one month
            customCalendar.setMonth(currentMonth);     // update calendar
            updateMonthLabel();                         // update label
        });

        // action for next month button
        nextButton.addActionListener(e -> {
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Calendar");               // window title
        frame.setContentPane(new CalendarForm().mainPanel); // use the form’s main panel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close app on exit
        frame.pack();                                        // auto-size to fit components
        frame.setVisible(true);                              // show window
    }
}
import javax.swing.*;
import java.awt.*;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class CalendarApp {

    private JFrame frame;              // main application window
    private JLabel monthLabel;         // label showing current month/year
    private PanelDate calendar;        // calendar panel (grid of days)
    private YearMonth currentMonth;    // tracks the currently displayed month

    public CalendarApp() {
        currentMonth = YearMonth.now(); // start with current month
        createAndShowUI();             // build and display the GUI
    }

    // UI setup
    private void createAndShowUI() {
        frame = new JFrame("Calendar");                 // create main window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close app on exit
        frame.setSize(500, 400);                        // initial window size
        frame.setLayout(new BorderLayout());            // BorderLayout for top, bottom, and center

        // month label + buttons
        JPanel topPanel = new JPanel(new BorderLayout());   // Use BorderLayout for buttons + label
        monthLabel = new JLabel("", SwingConstants.CENTER); // centered month label
        monthLabel.setFont(monthLabel.getFont().deriveFont(Font.BOLD, 18f)); // bold, larger font

        JButton prevButton = new JButton("<<");             // button to go to previous month
        JButton nextButton = new JButton(">>");             // button to go to next month

        // add buttons and label to top panel
        topPanel.add(prevButton, BorderLayout.WEST);        // place prev button on left
        topPanel.add(monthLabel, BorderLayout.CENTER);      // place month label in center
        topPanel.add(nextButton, BorderLayout.EAST);        // place next button on right

        frame.add(topPanel, BorderLayout.NORTH);            // add top panel to top of window

        // calendar panel
        JPanel calendarContainer = new JPanel(new BorderLayout()); // container for calendar grid
        calendar = new PanelDate(currentMonth);                    // create calendar for current month
        calendarContainer.add(calendar, BorderLayout.CENTER);      // add calendar to container
        frame.add(calendarContainer, BorderLayout.CENTER);         // add container to main frame

        // initialize month
        updateMonthLabel();                        // set initial text to current month

        // what the buttons do
        prevButton.addActionListener(new java.awt.event.ActionListener() { // previous month
            public void actionPerformed(java.awt.event.ActionEvent e) {
                goToPreviousMonth();               // call method to handle previous month
            }
        });

        nextButton.addActionListener(new java.awt.event.ActionListener() { // next month
            public void actionPerformed(java.awt.event.ActionEvent e) {
                goToNextMonth();                   // call method to handle next month
            }
        });

        frame.setVisible(true);                     // show the window
    }

    // go to previous month
    private void goToPreviousMonth() {
        currentMonth = currentMonth.minusMonths(1); // decrement month
        calendar.setMonth(currentMonth);            // update calendar grid
        updateMonthLabel();                         // update label text
    }

    // go to next month
    private void goToNextMonth() {
        currentMonth = currentMonth.plusMonths(1);  // increment month
        calendar.setMonth(currentMonth);            // update calendar grid
        updateMonthLabel();                         // update label text
    }

    // change month label
    private void updateMonthLabel() {
        // get full month name in default locale
        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        // month label format
        monthLabel.setText(monthName + " " + currentMonth.getYear());
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CalendarApp();                 // create an instance (builds and shows UI)
            }
        });
    }
}
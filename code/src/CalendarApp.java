/*
Uses the PanelDate and

 */

import javax.swing.*;
import java.awt.*;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class CalendarApp {

    private JFrame frame;              // main application window

    private JPanel calendarScreen;
    private JLabel monthLabel;         // label showing current month/year

    private JLabel yearLabel;         // label showing current month/year
    private PanelDate calendar;        // calendar panel (grid of days)
    private YearMonth currentDate;    // tracks the currently displayed month

    public CalendarApp() {
        currentDate = YearMonth.now(); // start with current month
        createAndShowUI();             // build and display the GUI
    }

    // JFrame is used to hold all various components inside a JPanel
    // Creates the window
    private void createFrame(){
        frame = new JFrame("ShelfLife");                 // create & titles main window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close app on exit
        frame.setSize(500, 1100);                        // initial window size
        frame.setLayout(new BorderLayout());            // BorderLayout for top, bottom, and center
        frame.setVisible(true);                     // show the window
        calendarScreen = new JPanel(new BorderLayout());
        frame.add(calendarScreen, BorderLayout.NORTH);

    }


    // Adds the month label, and the buttons to be pressed onto the UI
    private void createYearPanel(){
        /*
        BorderLayout arranges topPanel into several regions, those being NORTH SOUTH EAST WEST CENTER.
        It is important to point out that only one component can be at one region.b
         */
        JPanel topPanel = new JPanel(new BorderLayout());   // Use BorderLayout for buttons + label

        // centered month label
        // Text parameter is assigned with updateMonthLabel()
        yearLabel = new JLabel("YEAR", SwingConstants.CENTER);
        yearLabel.setFont(yearLabel.getFont().deriveFont(Font.BOLD, 18f)); // bold, larger font

        // Creates interactable buttons
        JButton prevYearButton = new JButton("<<");             // button to go to previous month
        JButton nextYearButton = new JButton(">>");             // button to go to next month

        // Add buttons and label to top panel
        topPanel.add(prevYearButton, BorderLayout.WEST);        // place prev button on left
        topPanel.add(yearLabel, BorderLayout.CENTER);      // place month label in center
        topPanel.add(nextYearButton, BorderLayout.EAST);        // place next button on right

        calendarScreen.add(topPanel, BorderLayout.NORTH);            // add top panel to top of window

        // Left button, goes to month before
        prevYearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                goToPreviousYear();               // call method to handle previous month
            }
        });

        // Right button, goes to month after
        nextYearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                goToNextYear();                   // call method to handle next month
            }
        });
    }

    private void createMonthPanel(){
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel monthPanel = new JPanel();
        JPanel FoodEntryPanel = new JPanel(new GridLayout(1,2));

        monthPanel.setLayout(new BoxLayout(monthPanel, BoxLayout.LINE_AXIS));

        // centered month label
        // Text parameter is assigned with updateMonthLabel()
        monthLabel = new JLabel("MONTH", SwingConstants.CENTER);
        monthLabel.setFont(monthLabel.getFont().deriveFont(Font.BOLD, 18f)); // bold, larger font
        monthLabel.setPreferredSize(new Dimension(140, 30)); // Makes each month take up the same space

        // Creates interactable buttons
        JButton prevMonthButton = new JButton("<<");             // button to go to previous month
        JButton nextMonthButton = new JButton(">>");             // button to go to next month
        JButton addEntry = new JButton("+");             // button to go to previous month
        JButton removeEntry = new JButton("-");             // button to go to next month

        // Add buttons and label to top panel
        monthPanel.add(Box.createHorizontalGlue());
        monthPanel.add(prevMonthButton);        // place prev button on left
        monthPanel.add(monthLabel);      // place month label in center
        monthPanel.add(nextMonthButton);        // place next button on right
        monthPanel.add(Box.createHorizontalGlue());
        FoodEntryPanel.add(removeEntry);
        FoodEntryPanel.add(addEntry);
        bottomPanel.add(FoodEntryPanel, BorderLayout.EAST);        // place next button on right

        bottomPanel.add(monthPanel, BorderLayout.NORTH);
        calendarScreen.add(bottomPanel, BorderLayout.SOUTH);            // add top panel to top of window

        // Left button, goes to month before
        prevMonthButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                goToPreviousMonth();               // call method to handle previous month
            }
        });

        // Right button, goes to month after
        nextMonthButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                goToNextMonth();                   // call method to handle next month
            }
        });
    }

    private void createCalendar(){
        JPanel calendarPanel = new JPanel(new BorderLayout()); // container for calendar grid
        calendar = new PanelDate(currentDate);                    // create calendar for current month
        calendarPanel.add(calendar, BorderLayout.CENTER);      // add calendar to container

        calendarPanel.setPreferredSize(new Dimension(frame.getWidth(), 350)); // Makes it so the calendar is always 350 pixels tall, preventing inconsistent UI and a larger calendar
        calendarScreen.add(calendarPanel, BorderLayout.CENTER);         // add container to main frame

        // Based on current date, update the month
        updateMonthLabel();                        // set initial text to current month
        updateYearLabel();

    }

    private void createExpirationPanel(){
        JPanel expirationPanel = new JPanel(new BorderLayout());

        JButton the = new JButton("the");

        expirationPanel.add(the, BorderLayout.NORTH);
        frame.add(expirationPanel, BorderLayout.CENTER);
    }
    // UI setup
    private void createAndShowUI() {

        createFrame();
        // Year and month panel must be created before calendar as calendar updates their labels.
        createMonthPanel();
        createYearPanel();
        createCalendar();

        createExpirationPanel();
    }

    // go to previous month
    private void goToPreviousMonth() {
        currentDate = currentDate.minusMonths(1); // decrement month
        calendar.setMonth(currentDate);            // update calendar grid
        updateMonthLabel();                         // update label text
    }

    // go to next month
    private void goToNextMonth() {
        currentDate = currentDate.plusMonths(1);  // increment month
        calendar.setMonth(currentDate);            // update calendar grid
        updateMonthLabel();                         // update label text
    }

    // change month label
    private void updateMonthLabel() {
        // get full month name in default locale
        String monthName = currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        // month label format
        monthLabel.setText(monthName);
    }

    // go to previous month
    private void goToPreviousYear() {
        currentDate = currentDate.minusYears(1); // decrement month
        calendar.setMonth(currentDate);            // update calendar grid
        updateYearLabel();                         // update label text
    }

    // go to next month
    private void goToNextYear() {
        currentDate = currentDate.plusYears(1);  // increment month
        calendar.setMonth(currentDate);            // update calendar grid
        updateYearLabel();                         // update label text
    }

    // change month label
    private void updateYearLabel() {
        // get full month name in default locale

        // month label format
        yearLabel.setText(String.valueOf(currentDate.getYear()));
    }
}
import javax.swing.*;
import java.awt.*;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class CalendarPanel extends JPanel{
    private JLabel monthLabel;         // label showing current month/year
    private JButton nextMonthButton;
    private JButton prevMonthButton;
    private JLabel yearLabel;         // label showing current month/year
    private JButton nextYearButton;
    private JButton prevYearButton;

    private JButton settingsButton;

    private JButton addEntry;

    private JButton removeEntry;

    private JButton buttons[];
    private PanelDate calendar;        // calendar panel (grid of days)
    private YearMonth currentDate;    // tracks the currently displayed month

    CalendarPanel(){
        this.setLayout(new BorderLayout());
        currentDate = YearMonth.now(); // start with current month
        createAndShowUI();             // build and display the GUI

        // After the UI is made and all the buttons are defined they can now be dropped in a list to be unfocused all together
        buttons = new JButton[] {nextMonthButton, prevMonthButton, nextYearButton, prevYearButton, settingsButton, addEntry, removeEntry};
        unfocusButtons();


        // Right button, goes to month after
        nextYearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                goToNextYear();                   // call method to handle next month
            }
        });

        // Left button, goes to month before
        prevYearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                goToPreviousYear();               // call method to handle previous month
            }
        });

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


    // Adds the month label, and the buttons to be pressed onto the UI
    private void createYearPanel(){
        // Creates the << YEAR >> component of the calendar.
        JPanel yearPanel = new JPanel();
        yearPanel.setLayout(new BoxLayout(yearPanel, BoxLayout.X_AXIS));

        // Text parameter is assigned with updateYearLabel()
        yearLabel = new JLabel("YEAR", SwingConstants.CENTER);
        yearLabel.setFont(yearPanel.getFont().deriveFont(Font.BOLD, 18f)); // bold, larger font
        yearLabel.setPreferredSize(new Dimension(140, 30)); // Makes each month take up the same space, preventing UI inconsistency

        // Implements interactable buttons to increment through years
        prevYearButton = new JButton("<<");             // button to go to previous month
        nextYearButton = new JButton(">>");             // button to go to next month

        yearPanel.add(Box.createHorizontalGlue());
        // Pretty bad work around by just eyeballing to center the month panel
        // This is due to having the settings to the right, taking up space in the east side of the bottom panel.
        // TODO if possible, have a mathematical way or logic to make UI even
        yearPanel.add(Box.createHorizontalStrut(70));
        yearPanel.add(prevYearButton);

        yearPanel.add(yearLabel);
        yearPanel.add(nextYearButton);
        yearPanel.add(Box.createHorizontalGlue());

        // Creates the settings components
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.X_AXIS));

        // Creates the add entry and remove entry buttons.
        settingsButton = new JButton("SETTINGS");

        // Since this is a boxlayout on the X_AXIS, the buttons are placed correctly without any specification needed
        settingsPanel.add(settingsButton);

        // Combine the settingsPanel and the yearPanel onto one bottom panel appearing under the calendar
        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(yearPanel, BorderLayout.CENTER);
        topPanel.add(settingsPanel, BorderLayout.EAST);

        // Finally, add this to the calendarScreen, which is added onto the frame.
        this.add(topPanel, BorderLayout.NORTH);
    }

    private void createMonthPanel(){
        // Creates the << MONTH >> component of the calendar.
        JPanel monthPanel = new JPanel();
        monthPanel.setLayout(new BoxLayout(monthPanel, BoxLayout.X_AXIS));

        // Text parameter is assigned with updateMonthLabel()
        monthLabel = new JLabel("MONTH", SwingConstants.CENTER);
        monthLabel.setFont(monthLabel.getFont().deriveFont(Font.BOLD, 18f)); // bold, larger font
        monthLabel.setPreferredSize(new Dimension(140, 30)); // Makes each month take up the same space, preventing UI inconsistency

        // Creates interactable buttons to increment to months
        prevMonthButton = new JButton("<<");             // button to go to previous month
        nextMonthButton = new JButton(">>");             // button to go to next month

        monthPanel.add(Box.createHorizontalGlue());
        // Pretty bad work around by just eyeballing to center the month panel
        // This is due to having the add and remove entries to the right, taking up space in the east side of the bottom panel.
        monthPanel.add(Box.createHorizontalStrut(60));
        monthPanel.add(prevMonthButton);
        monthPanel.add(monthLabel);
        monthPanel.add(nextMonthButton);
        monthPanel.add(Box.createHorizontalGlue());



        // Creates the add entry '+' and remove entry '-' components
        JPanel FoodEntryPanel = new JPanel();
        FoodEntryPanel.setLayout(new BoxLayout(FoodEntryPanel, BoxLayout.X_AXIS));

        // Creates the add entry and remove entry buttons.
        addEntry = new JButton("+");             // button to go to previous month
        removeEntry = new JButton("-");             // button to go to next month

        // Since this is a boxlayout on the X_AXIS, the buttons are placed correctly without any specification needed
        FoodEntryPanel.add(removeEntry);
        FoodEntryPanel.add(addEntry);

        // Combine the FoodEntryPanel and the FoodExpirationPanel onto one bottom panel appearing under the calendar
        JPanel bottomPanel = new JPanel(new BorderLayout());

        bottomPanel.add(monthPanel, BorderLayout.CENTER);
        bottomPanel.add(FoodEntryPanel, BorderLayout.EAST);

        // Finally, add this to the calendarScreen, which is added onto the frame.
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void createCalendar(){
        JPanel calendarPanel = new JPanel(new BorderLayout()); // container for calendar grid
        calendar = new PanelDate(currentDate);                    // create calendar for current month
        calendarPanel.add(calendar, BorderLayout.CENTER);      // add calendar to container

        calendarPanel.setPreferredSize(new Dimension(this.getWidth(), 300)); // Makes it so the calendar is always 350 pixels tall, preventing inconsistent UI and a larger calendar
        this.add(calendarPanel, BorderLayout.CENTER);         // add container to main frame

        // Based on current date, update the month
        updateMonthLabel();                        // set initial text to current month
        updateYearLabel();

    }

    // UI setup
    private void createAndShowUI() {
        // Year and month panel must be created before calendar as calendar updates their labels.
        createMonthPanel();
        createYearPanel();
        createCalendar();

    }

    private void goToPreviousMonth() {
        currentDate = currentDate.minusMonths(1); // decrement month
        calendar.setMonth(currentDate);            // update calendar grid
        updateMonthLabel();                         // update label text
        updateYearLabel();
    }

    private void goToNextMonth() {
        currentDate = currentDate.plusMonths(1);  // increment month
        calendar.setMonth(currentDate);            // update calendar grid
        updateMonthLabel();                         // update label text
        updateYearLabel();
    }

    // Change month label, called after the month on the calendar is adjusted
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

    // Change month label, called after the year on the calendar is adjusted
    private void updateYearLabel() {
        // Turns the integer of the current year into a string and sets the year to it
        yearLabel.setText(String.valueOf(currentDate.getYear()));
    }

    private void unfocusButtons(){
        for (JButton button : buttons){
            button.setFocusable(false);
        }
    }
}

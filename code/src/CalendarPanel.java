import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.*;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class CalendarPanel extends JPanel {
    private JLabel monthLabel;         // label showing current month/year
    private JLabel yearLabel;          // label showing current month/year
    ImageIcon rightArrow = new ImageIcon(getClass().getResource("/sprites/arrowRightIcon.png"));
    // Actual image being used, scaled down to stay proper button size
    Image rightArrowScaled = rightArrow.getImage().getScaledInstance(32, 12, Image.SCALE_SMOOTH);
    ImageIcon leftArrow = new ImageIcon(getClass().getResource("/sprites/arrowLeftIcon.png"));
    // Actual image being used, scaled down to stay proper button size
    Image leftArrowScaled = leftArrow.getImage().getScaledInstance(32, 12, Image.SCALE_SMOOTH);

    private JButton nextMonthButton;
    private JButton prevMonthButton;
    private JButton nextYearButton;
    private JButton prevYearButton;
    private JButton settingsButton;
    private JButton addEntry;
    private JButton removeEntry;
    private JButton buttons[];

    private static Color expiredColor;        //= new Color (255, 103, 103);
    private static Color todayColor;          //= new Color (255, 138, 76);
    private static Color warningColor;        //= new Color (255, 226, 2);
    private static Color freshColor;          //= new Color (55, 252, 140);
    public static Color userColors[];
    public static int userWarningDays;

    private PanelDate calendar;        // calendar panel (grid of days)
    private YearMonth currentDate;     // tracks the currently displayed month

    CalendarPanel(){
        // Load user settings
        userColors = new Color[] {expiredColor, todayColor, warningColor, freshColor};
        loadUserSettings();

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

        // add entry logic (uses selected date from calendar)
        addEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                addNewEntry();                     // call method to handle add entry
            }
        });

        // remove entry logic
        removeEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                deleteEntry();                     // call method to handle delete entry
            }
        });

        // settings logic
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                openSettings();                   // call method to handle settings
            }
        });
    }

    // Loads colors and userWarningDays into local variables
    public static void loadUserSettings() {
        try (Scanner scanner = new Scanner(new File("user_settings.txt"))) {
            for (int i = 0; i < userColors.length; i++) {
                int r = scanner.nextInt();
                int g = scanner.nextInt();
                int b = scanner.nextInt();
                userColors[i] = new Color(r, g, b);
            }

            userWarningDays = scanner.nextInt();
        }
        catch (IOException ex) {
            //ex.printStackTrace();

            // fallback default settings
            userColors[0] = new Color (255, 103, 103);
            userColors[1] = new Color (255, 138, 76);
            userColors[2] = new Color (255, 226, 2);
            userColors[3] = new Color (55, 252, 140);
            userWarningDays = 3;

            // save current settings
            saveSettings();
        }
    }

    // Adds the month label, and the buttons to be pressed onto the UI
    private void createYearPanel(){
        // Creates the << YEAR >> component of the calendar.
        JPanel yearPanel = new JPanel();
        yearPanel.setLayout(new BoxLayout(yearPanel, BoxLayout.X_AXIS));

        // Text parameter is assigned with updateYearLabel()
        yearLabel = new JLabel("YEAR", SwingConstants.CENTER);
        yearLabel.setFont(yearPanel.getFont().deriveFont(Font.BOLD, 18f)); // bold, larger font

        System.out.println(yearLabel.getFont());
        yearLabel.setPreferredSize(new Dimension(140, 30)); // Makes each month take up the same space, preventing UI inconsistency

        // Implements interactable buttons to increment through years
        prevYearButton = new JButton("");             // button to go to previous month
        prevYearButton.setIcon(new ImageIcon(leftArrowScaled));

        nextYearButton = new JButton("");             // button to go to next month
        nextYearButton.setIcon(new ImageIcon(rightArrowScaled));


        yearPanel.add(Box.createHorizontalGlue());
        // Pretty bad work around by just eyeballing to center the month panel
        // This is due to having the settings to the right, taking up space in the east side of the bottom panel.
        // TODO if possible, have a mathematical way or logic to make UI even
        yearPanel.add(Box.createHorizontalStrut(50));
        yearPanel.add(prevYearButton);

        yearPanel.add(yearLabel);
        yearPanel.add(nextYearButton);
        yearPanel.add(Box.createHorizontalGlue()); // Makes it so the settings panel will be placed on the right

        // Creates the settings components
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.X_AXIS));

        // Settings button is placed in the year panel.
        settingsButton = new JButton("");
        ImageIcon settingsIcon = new ImageIcon(getClass().getResource("/sprites/settingsIcon.png"));
        Image settingsIconScaled = settingsIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        settingsButton.setIcon(new ImageIcon(settingsIconScaled));



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
        prevMonthButton = new JButton("");             // button to go to previous month
        prevMonthButton.setIcon(new ImageIcon(leftArrowScaled));

        nextMonthButton = new JButton("");             // button to go to next month
        nextMonthButton.setIcon(new ImageIcon(rightArrowScaled));

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

    private void addNewEntry() {
        // get expiration date from calendar
        LocalDate expirationDate = calendar.getSelectedDate();
        if (expirationDate == null) {
            JOptionPane.showMessageDialog(CalendarPanel.this, "Please select a date on the calendar!");
            return;
        }

        // create panel to hold input fields (product and quantity)
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        // create labels and input fields
        JTextField nameField = new JTextField();
        JTextField quantityField = new JTextField();

        inputPanel.add(new JLabel("Enter Food Product:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Enter Food Quantity:"));
        inputPanel.add(quantityField);

        // format date in a way that ignores different date formats
        // to get past different date formats, we simply print it out with something
        // like "November 30, 2025"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        String formattedDate = expirationDate.format(formatter);

        // date title has the format explained above
        String dateTitle = "Add Food Entry on " + formattedDate;

        // show confirm dialog with custom panel
        int result = JOptionPane.showConfirmDialog(
                CalendarPanel.this,
                inputPanel,
                dateTitle,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return; // user cancelled
        }

        // get input values
        String name = nameField.getText();
        String qtyStr = quantityField.getText();

        if (name.isEmpty() || qtyStr.isEmpty()) {
            return; // fields cannot be empty
        }

        int quantity;
        try {
            quantity = Integer.parseInt(qtyStr); // parse quantity
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(CalendarPanel.this, "Invalid quantity! " + ex.getMessage(),
                                     "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // create FoodEntry and add it
        FoodEntry entry = new FoodEntry(name, quantity, expirationDate);
        ShelfLife shelfLifeFrame = (ShelfLife) SwingUtilities.getWindowAncestor(CalendarPanel.this);
        shelfLifeFrame.addEntry(entry);
        shelfLifeFrame.getFoodExpirationPanel().refreshEntries(shelfLifeFrame.getEntries());
    }

    private void deleteEntry() {
        ShelfLife shelfLifeFrame = (ShelfLife) SwingUtilities.getWindowAncestor(CalendarPanel.this);
        List<FoodEntry> entries = shelfLifeFrame.getEntries();

        // main panel
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        // list to hold checkboxes
        List<JCheckBox> checkBoxes = new ArrayList<>();

        // create checkbox for each entry
        for (FoodEntry entry : entries) {
            JPanel row = new JPanel(new BorderLayout());
            row.setBorder(BorderFactory.createMatteBorder(2, 5, 2, 5, new Color(238, 238, 238)));

            // format text for the entry
            LocalDate today = LocalDate.now();
            long daysLeft = entry.getExpirationDate().toEpochDay() - today.toEpochDay();
            String text = entry.getName() + " - Qty: " + entry.getQuantity() +
                    " - Expires in: " + daysLeft + " days";

            JLabel label = new JLabel(text);
            label.setFont(label.getFont().deriveFont(16f));

            // color coding from FoodExpirationPanel
            row.setBackground(FoodExpirationPanel.getEntryColor(daysLeft));

            JCheckBox cb = new JCheckBox();
            checkBoxes.add(cb);

            row.add(label, BorderLayout.CENTER);
            row.add(cb, BorderLayout.EAST);

            listPanel.add(row);
        }

        // scroll pane
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // confirm/cancel buttons
        int result = JOptionPane.showConfirmDialog(
                CalendarPanel.this,
                scrollPane,
                "Select to Remove Food Entries",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return; // cancelled
        }

        // remove selected entries
        for (int i = checkBoxes.size() - 1; i >= 0; i--) {
            if (checkBoxes.get(i).isSelected()) {
                entries.remove(i);
            }
        }

        // refresh dashboard
        shelfLifeFrame.getFoodExpirationPanel().refreshEntries(entries);
    }

    private void colorPickerButton(JButton button, int index) {
        button.setBackground(userColors[index]);

        // create ActionListener
        button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Color selectedColor = JColorChooser.showDialog(CalendarPanel.this, "Choose a Color", userColors[index]);

                if (selectedColor != null) {
                    button.setBackground(selectedColor);
                    userColors[index] = selectedColor;
                }
            }
        });
    }

    private static void saveSettings() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("user_settings.txt"))) {
            // Save expiredColor, todayColor, warningColor, freshColor
            for (int i = 0; i < userColors.length; i++) {
                writer.println(userColors[i].getRed());
                writer.println(userColors[i].getGreen());
                writer.println(userColors[i].getBlue());
            }

            // Save warning on less than x days left
            writer.println(userWarningDays);

            System.out.println("Saved current user settings to user_settings.txt!");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void openSettings() {
        // reload user settings
        loadUserSettings();

        // create panel to hold input fields (product and quantity)
        JPanel settingsPanel = new JPanel(new GridLayout(0, 3, 5, 5));

        // create text fields
        JTextField warningDays = new JTextField(String.valueOf(userWarningDays));

        // create buttons
        JButton expiredColorButton = new JButton();
        JButton todayColorButton = new JButton();
        JButton warningColorButton = new JButton();
        JButton freshColorButton = new JButton();

        // assign color with button functionalities
        colorPickerButton(expiredColorButton, 0);
        colorPickerButton(todayColorButton, 1);
        colorPickerButton(warningColorButton, 2);
        colorPickerButton(freshColorButton, 3);

        // create labels
        settingsPanel.add(new JLabel("Expiration Settings"));
        settingsPanel.add(new JLabel(""));                        // empty text
        settingsPanel.add(new JLabel(""));                        // empty text
        settingsPanel.add(expiredColorButton);
        settingsPanel.add(new JLabel("Expired"));
        settingsPanel.add(new JLabel(""));                        // empty text
        settingsPanel.add(todayColorButton);
        settingsPanel.add(new JLabel("Expires Today"));
        settingsPanel.add(new JLabel(""));                        // empty text
        settingsPanel.add(warningColorButton);
        settingsPanel.add(new JLabel("Warning (in days)"));
        settingsPanel.add(warningDays);
        settingsPanel.add(freshColorButton);
        settingsPanel.add(new JLabel("Fresh"));
        settingsPanel.add(new JLabel(""));                        // empty text

        // show confirm dialog with custom panel
        int result = JOptionPane.showConfirmDialog(
                CalendarPanel.this,
                settingsPanel,
                "User Settings",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return; // user cancelled
        }

        // get input values
        String userWarning = warningDays.getText();

        if (userWarning.isEmpty()) {
            return;
        }

        try {
            // parse integers
            userWarningDays = Integer.parseInt(userWarning);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    CalendarPanel.this,
                    "Invalid quantity! " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // save user settings
        saveSettings();

        // refresh dashboard
        ShelfLife shelfLifeFrame = (ShelfLife) SwingUtilities.getWindowAncestor(CalendarPanel.this);
        List<FoodEntry> entries = shelfLifeFrame.getEntries();
        shelfLifeFrame.getFoodExpirationPanel().refreshEntries(entries);
    }

}

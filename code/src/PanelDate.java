import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;     // to get current date
import java.time.YearMonth;     // represents a month in a specific year
import java.util.HashMap;
import java.util.Map;


public class PanelDate extends JPanel {   // custom JPanel to display a month calendar
    private YearMonth currentMonth;       // holds the currently displayed month
    private JButton dayButton;

    // Attaches each button created to its date.
    HashMap<JButton, LocalDate> buttonDates = new HashMap<>();
    JButton dateSelected;
    JButton previouslySelected = null;
    Color previousSelectedColor = null;
    private ActionListener dateSelector = new ActionListener() {
        @Override
        // Prevents multiple dates being selected.
        public void actionPerformed(ActionEvent ae){
            Object source = ae.getSource();
            dateSelected = (JButton) source;
            buttonHighlight(dateSelected);

        }
    };


    // constructor that initializes the calendar with a given month
    public PanelDate(YearMonth month) {
        this.currentMonth = month;       // set current month
        buildCalendar();                 // build the calendar UI


    }

    // method to build or rebuild the calendar grid
    private void buildCalendar() {
        this.removeAll();                // clear any existing components
        this.setLayout(new GridLayout(0, 7)); // grid of 7 columns for 7 days
        LocalDate today = LocalDate.now();


        // add weekday headers
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER); // centered text
            label.setFont(label.getFont().deriveFont(Font.BOLD));  // bold font
            this.add(label);                                      // add to panel
        }

        // calculate the first day of the month
        LocalDate firstOfMonth = currentMonth.atDay(1);
        int dayOfWeekValue = firstOfMonth.getDayOfWeek().getValue() % 7; // sunday = 0
        int daysInMonth = currentMonth.lengthOfMonth();                  // total days
        LocalDate buttonDate = firstOfMonth;

        // fill empty cells for days before the first of the month
        for (int i = 0; i < dayOfWeekValue; i++) {
            this.add(new JLabel("")); // empty label as placeholder
        }

        // add buttons for each day of the month
        for (int day = 1; day <= daysInMonth; day++) {
            dayButton = new JButton(String.valueOf(day)); // button showing day number
            dayButton.setFocusable(false); // No longer highlight of text when buttons are interacted
            // highlight today's date

            if (today.getYear() == currentMonth.getYear()
                    && today.getMonth() == currentMonth.getMonth()
                    && today.getDayOfMonth() == day) {
                dayButton.setBackground(Color.CYAN); // highlight current day
            }




            buttonDates.put(dayButton, buttonDate);

            // Properly saves the date already highlighted.
            if (previouslySelected != null) {
                if (buttonDates.get(previouslySelected).getYear() == currentMonth.getYear()
                        && buttonDates.get(previouslySelected).getMonth() == currentMonth.getMonth()
                        && buttonDates.get(previouslySelected).getDayOfMonth() == day) {
                    buttonHighlight(dayButton); // highlight current day
                }
            }
            this.add(dayButton); // add day button to the grid

            // Adds the button to an action listener which will be called whenever anything added is pressed.
            dayButton.addActionListener(dateSelector);

            // Increments the day value for the next button
            buttonDate = buttonDate.plusDays(1);
        }

        this.revalidate(); // refresh layout
        this.repaint();    // redraw panel
    }

    // update the calendar to a new month
    public void setMonth(YearMonth month) {
        this.currentMonth = month; // change month
        buildCalendar();           // rebuild the calendar grid
    }

    public void buttonHighlight(JButton selected){
        // If previously selected is not null, then there is a previously selected date present
        // If this function is called a date button has been interacted with, meaning that it's previous color should be assigned back to it.
        if (previouslySelected != null){
            previouslySelected.setBackground(previousSelectedColor);
        }

        // If the button that was selected is the same as the previously selected button then the button should be unselected/highlighted
        if (selected.equals(previouslySelected)){
            selected.setBackground(previousSelectedColor);
            // Resets because nothing will be considered selected previously anymore
            previouslySelected = null;
            previousSelectedColor = null;
        } else {
            // Saves the current button into previously selected in case another date is chosen in the future.
            previouslySelected = selected;
            previousSelectedColor = selected.getBackground();

            // Sets the color
            selected.setBackground(Color.PINK);
        }

        System.out.println(buttonDates.get(selected));
    }
}
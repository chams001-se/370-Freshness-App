import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;     // to get current date
import java.time.YearMonth;     // represents a month in a specific year

public class PanelDate extends JPanel {   // custom JPanel to display a month calendar
    private YearMonth currentMonth;       // holds the currently displayed month

    // constructor that initializes the calendar with a given month
    public PanelDate(YearMonth month) {
        this.currentMonth = month;       // set current month
        buildCalendar();                 // build the calendar UI
    }

    // method to build or rebuild the calendar grid
    private void buildCalendar() {
        this.removeAll();                // clear any existing components
        this.setLayout(new GridLayout(0, 7)); // grid of 7 columns for 7 days

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

        // fill empty cells for days before the first of the month
        for (int i = 0; i < dayOfWeekValue; i++) {
            this.add(new JLabel("")); // empty label as placeholder
        }

        // add buttons for each day of the month
        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = new JButton(String.valueOf(day)); // button showing day number

            // highlight today's date
            LocalDate today = LocalDate.now();
            if (today.getYear() == currentMonth.getYear()
                    && today.getMonth() == currentMonth.getMonth()
                    && today.getDayOfMonth() == day) {
                dayButton.setBackground(Color.CYAN); // highlight current day
            }

            this.add(dayButton); // add day button to the grid
        }

        this.revalidate(); // refresh layout
        this.repaint();    // redraw panel
    }

    // update the calendar to a new month
    public void setMonth(YearMonth month) {
        this.currentMonth = month; // change month
        buildCalendar();           // rebuild the calendar grid
    }
}
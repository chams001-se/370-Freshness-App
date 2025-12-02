import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.time.LocalDate;

// Painting raw on the display will result in the painting getting overridden by the layout
// We must create a panel to host the painting
class SeparatorLine extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2.5f));
        g2.draw(new Line2D.Double(0, getHeight()/2, getWidth(), getHeight()/2));
    }
}

public class FoodExpirationPanel extends JPanel {
    FoodExpirationPanel() {
        createHeader();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // It is necessary to take the parameter of the overridden paintComponent and
        // cast it into a Graphics2D as it is required in order to be able to draw anything.
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5f));
        g2.draw(new Line2D.Double(100, 60, 500, 60));

    }

    public void createHeader(){
        this.setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Food Expiring");
        title.setFont(this.getFont().deriveFont(Font.BOLD, 22F));
        header.add(title, BorderLayout.WEST);

        JButton refreshButton = new JButton("Refresh");
        header.add(refreshButton, BorderLayout.EAST);
        header.add(new SeparatorLine(), BorderLayout.NORTH);
        header.add(new SeparatorLine(), BorderLayout.SOUTH);
        this.add(header, BorderLayout.NORTH);

    }

    public void refreshEntries(java.util.List<FoodEntry> entries) {
        this.removeAll();   // clean out UI
        this.setLayout(new BorderLayout());
        createHeader();
        // reload user settings
        CalendarPanel.loadUserSettings();

        // area that contains the rows for the food entries
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        // only show items within 30 days on the dash
        LocalDate compareToday = LocalDate.now();
        java.util.List<FoodEntry> filtered = new java.util.ArrayList<>();

        for (FoodEntry e : entries) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(compareToday, e.getExpirationDate());
            if (days >= 0 && days <= 30) {
                filtered.add(e);
            }
        }

        entries = filtered;

        // sort by expiration date via selection sort
        for (int i = 0; i < entries.size() - 1; i++) {
            int earliestEntry = i;

            for (int j = i + 1; j < entries.size(); j++) {
                if (entries.get(j).getExpirationDate().isBefore(entries.get(earliestEntry).getExpirationDate())) {
                    earliestEntry = j;
                }
            }
            if (earliestEntry != i) {
                FoodEntry temp = entries.get(i);
                entries.set(i, entries.get(earliestEntry));
                entries.set(earliestEntry, temp);
            }
        }

        LocalDate today = LocalDate.now();

        for (FoodEntry entry : entries) {
            // new row is made for any new entries
            JPanel row = new JPanel();
            row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
            row.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            // draw a boarder around each entry to create space between them
            row.setBorder(BorderFactory.createMatteBorder(3, 6, 3, 6, new Color(238,238,238)));

            // display name, quantity, & date of entry
            LocalDate exp = entry.getExpirationDate();
            long daysLeft = exp.toEpochDay() - today.toEpochDay();

            // display text
            JLabel productName = new JLabel(" " + entry.getName() + ":");
            JLabel daysTillExpiration = new JLabel("");
            JLabel quantity = new JLabel("  Quantity: [ " + entry.getQuantity() + " ]");
            productName.setFont(this.getFont().deriveFont(18F));
            daysTillExpiration.setFont(this.getFont().deriveFont(18F));
            quantity.setFont(this.getFont().deriveFont(18F));

            if (daysLeft <= 0) {
                daysTillExpiration.setText("  EXPIRED  ");
            } else if (daysLeft == 1) {
                daysTillExpiration.setText("  Expires TODAY  ");
            } else if (daysLeft <= CalendarPanel.userWarningDays) {
                daysTillExpiration.setText("  Expires in  " + daysLeft + "  days  ");
            } else {
                daysTillExpiration.setText("  Expires in  " + daysLeft + "  days  ");
            }

            row.add(productName);
            row.add(Box.createHorizontalGlue()); // Pushes everything else to the right
            row.add(daysTillExpiration);
            row.add(quantity);

            // draw background color for each row depending on freshness state using reusable method
            row.setBackground(getEntryColor(daysLeft));

            listPanel.add(row);
        }

        // scroll box functionality for list of food entries
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        this.add(scrollPane, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();

    }

    // reusable static method for color coding based on days left
    // made this so we could just reuse it in the remove entry button checkbox list
    public static Color getEntryColor(long daysLeft) {
        if (daysLeft <= 0) {
            return CalendarPanel.userColors[0];        // expired
        } else if (daysLeft == 1) {
            return CalendarPanel.userColors[1];        // expires today
        } else if (daysLeft <= CalendarPanel.userWarningDays) {
            return CalendarPanel.userColors[2];        // expire warning
        } else {
            return CalendarPanel.userColors[3];        // fresh
        }
    }
}
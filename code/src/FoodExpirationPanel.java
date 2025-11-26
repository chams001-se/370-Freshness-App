import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.time.LocalDate;

// TODO
public class FoodExpirationPanel extends JPanel {

    private JLabel foodExpiration;

    FoodExpirationPanel() {
        foodExpiration = new JLabel("Food Expiring");
        foodExpiration.setFont(this.getFont().deriveFont(Font.BOLD, 22f));
        setLayout(new BorderLayout());
        foodExpiration.setHorizontalAlignment(JLabel.LEFT);
        this.add(foodExpiration, BorderLayout.NORTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // It is necessary to take the parameter of the overridden paintComponent and
        // cast it into a Graphics2D as it is required in order to be able to draw anything.
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5f));
        g2.draw(new Line2D.Double(0, 0, 500, 0));

    }

    public void refreshEntries(java.util.List<FoodEntry> entries) {
        this.removeAll();   // clean out UI
        this.setLayout(new BorderLayout());

        // add the title back
        JLabel title = new JLabel("Food Expiring");
        title.setFont(this.getFont().deriveFont(Font.BOLD, 22F));
        title.setHorizontalAlignment(JLabel.LEFT);
        this.add(title, BorderLayout.NORTH);

        // area that contains the rows for the food entries
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

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

        java.time.LocalDate today = java.time.LocalDate.now();

        for (FoodEntry entry : entries) {
            // new row is made for any new entries
            JPanel row = new JPanel();
            row.setLayout(new BorderLayout());
            row.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

            // draw a boarder around each entry to create space between them
            row.setBorder(BorderFactory.createMatteBorder(3, 6, 3, 6, new Color(238,238,238)));

            // display name, quantity, & date of entry
            LocalDate exp = entry.getExpirationDate();
            long daysLeft = exp.toEpochDay() - today.toEpochDay();

            // display text
            String text = " " + entry.getName() + ":";

            if (daysLeft < 0) {
                text += "  EXPIRED  ";
            } else if (daysLeft == 1) {
                text += "  expires TODAY  ";
           } else if (daysLeft <= 3) {
                text += "  expires in  " + daysLeft + "  days  ";
            } else {
                text += "  expires in  " + daysLeft + "  days  ";
            }

            text += "  Quantity: [ " + entry.getQuantity() + " ]";

            // draw label
            JLabel info = new JLabel(text);
            row.add(info, BorderLayout.CENTER);

            // set font size for food entries
            info.setFont(info.getFont().deriveFont(18f));


            // draw background color for each row depending on freshness state
            if (daysLeft < 0) {
                row.setBackground(new Color (255, 103, 103, 255));
            } else if (daysLeft == 1) {
                row.setBackground(new Color(255, 138, 76));
            } else if (daysLeft <= 3) {
                row.setBackground(new Color(255, 226, 2, 255));
            } else{
                row.setBackground(new Color(55,252,140, 205));
            }

            listPanel.add(row);
        }

        // scroll box functionality for list of food entries
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);

        this.add(scrollPane, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();

    }
}
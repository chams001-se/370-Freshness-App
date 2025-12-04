import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.io.InputStream;

// Painting raw on the display will result in the painting getting overridden by the layout
// We must create a panel to host the painting
class SeparatorLine extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2.5f));
        g2.draw(new Line2D.Double(0, (double) getHeight() /2, getWidth(), (double) getHeight() /2));
    }
}

public class FoodExpirationPanel extends JPanel {
    Font fontChoice;
    FoodExpirationPanel(Font fontChoice) {
        this.fontChoice = fontChoice;
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

    public void createHeader() {

        this.setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Food Expiring");
        title.setFont(fontChoice.deriveFont(Font.BOLD));
        header.add(title, BorderLayout.WEST);

        JButton refreshButton = new JButton("");
        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/sprites/refreshIcon.png"));
        Image scaled = rawIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        refreshButton.setIcon(new ImageIcon(scaled));
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ShelfLife shelfLifeFrame = (ShelfLife) SwingUtilities.getWindowAncestor(FoodExpirationPanel.this);
                refreshEntries(shelfLifeFrame.getEntries());                   // call method to handle next month
                System.out.println("Food Expiration Panel has been refreshed!");
            }
        });

        header.add(refreshButton, BorderLayout.EAST);
        header.add(new SeparatorLine(), BorderLayout.NORTH);
        header.add(new SeparatorLine(), BorderLayout.SOUTH);
        this.add(header, BorderLayout.NORTH);

    }

    public void sortEntries(java.util.List<FoodEntry> entries) {
        // sort by expiration date via selection sort
        int earliestEntry;
        for (int i = 0; i < entries.size() - 1; i++) {
            earliestEntry = i;

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
            if (days <= 30) {
                filtered.add(e);
            }
        }

        //entries = filtered;

        //sortEntries(entries);
        sortEntries(filtered);

        LocalDate today = LocalDate.now();

        for (FoodEntry entry : filtered) {
            // new row is made for any new entries
            JPanel row = new JPanel();
            row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
            row.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

            // draw a boarder around each entry to create space between them
            row.setBorder(BorderFactory.createMatteBorder(3, 5, 3, 5, new Color(238, 238, 238)));

            // display name, quantity, & date of entry
            LocalDate exp = entry.getExpirationDate();
            long daysLeft = exp.toEpochDay() - today.toEpochDay();

            // -------------display text-------------

            // product entry with automatic truncation when applicable
            String full = entry.getName();
            String entryName = full;

            // if food item's name exceeds 12 characters, truncate
            if (full.length() > 12) {
                entryName = full.substring(0, 8) + "... ";
            }

            //display food entry name as normal
            JLabel productName = new JLabel(" " + entryName + ":");
            productName.setFont(fontChoice.deriveFont(18F));

            // allow viewing truncated names in full via mouse hover or click
            if (full.length() > 12) {
                productName.setToolTipText(full); // hover displays the full name

                // if the item's name is clicked, a new window pop up will display the name in full
                productName.addMouseListener(new java.awt.event.MouseAdapter() {    //listen for mouse click
                    public void mouseClicked(java.awt.event.MouseEvent e) { // when the name is clicked, display the popup
                        JOptionPane.showMessageDialog(productName, full);
                    }
                });
            }

            JLabel daysTillExpiration = new JLabel("");
            JLabel quantity = new JLabel("  Quantity: [ " + entry.getQuantity() + " ]");
            productName.setFont(fontChoice.deriveFont(20F));

            // Maintain alignment regardless of quantity entered (up to double digits)
            quantity.setPreferredSize(new Dimension(113, quantity.getPreferredSize().height));
            daysTillExpiration.setFont(fontChoice.deriveFont(17F));
            quantity.setFont(fontChoice.deriveFont(16.2F));

            if (daysLeft <= 0) {
                daysTillExpiration.setText("  EXPIRED  ");
            } else if (daysLeft == 1) {
                daysTillExpiration.setText("  Expires Tomorrow!  ");
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

        // disable horizontal scroll bar from appearing
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setBorder(null);
        //increase speed of scroll bar
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        // apply the custom scroll bar design
        scrollPane.getVerticalScrollBar().setUI(FoodExpirationPanel.improvedScrollBar());

        this.add(scrollPane, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();

    }

    // reusable static method for the improved scroll bar
    // this is so we can reuse it in deleteEntry
    public static javax.swing.plaf.basic.BasicScrollBarUI improvedScrollBar() {
        return new javax.swing.plaf.basic.BasicScrollBarUI() {

            // change the colors
            private final Color bar = new Color(190, 190, 190);
            private final Color background = new Color(230, 230, 230);

            @Override
            protected void paintThumb (Graphics g, JComponent c, Rectangle r){
                Graphics2D g2 = (Graphics2D) g.create();    // redraw the original bar with a customized bar
                g2.setColor(bar);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);  // create rounded edges for the bar
                g2.dispose();   // free up the temporary Graphics2D
            }

            @Override
            protected void paintTrack (Graphics g, JComponent c, Rectangle r){
                Graphics2D g2 = (Graphics2D) g.create();    // redraw the background of the scroll bar
                g2.setColor(background);
                g2.dispose();   // free up the temporary Graphics2D
            }

            //  maintain the size of the bar
            @Override
            protected Dimension getMinimumThumbSize () {
                return new Dimension(10, 40);
            }

            //make the scroll bar thinner
            @Override
            public Dimension getPreferredSize (JComponent c){
                return new Dimension(15, super.getPreferredSize(c).height);
            }
        };
    }

    // reusable static method for color coding based on days left
    // made this so we could just reuse it in the remove entry button checkbox list
    public static Color getEntryColor(long daysLeft) {
        if (daysLeft <= 0) {
            return CalendarPanel.userColors[0];        // expired
        } else if (daysLeft == 1) {
            return CalendarPanel.userColors[1];        // expires tomorrow
        } else if (daysLeft <= CalendarPanel.userWarningDays) {
            return CalendarPanel.userColors[2];        // expire warning
        } else {
            return CalendarPanel.userColors[3];        // fresh
        }
    }
}
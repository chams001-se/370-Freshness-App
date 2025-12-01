import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate; //Temporary (test entries use this)

public class ShelfLife extends JFrame {
    // make FoodExpirationPanel a field so it can be accessed by CalendarPanel
    private FoodExpirationPanel fd;

    public ShelfLife() {
        CalendarPanel cal = new CalendarPanel();
        fd = new FoodExpirationPanel(); // assign to field
        createFrame();
        this.add(cal, BorderLayout.NORTH);
        this.add(fd, BorderLayout.CENTER);

        //-----------------------------TEMPORARY TEST ENTRIES----------------------------------------
        entries.add(new FoodEntry("Milk", 1, LocalDate.now().plusDays(2)));
        entries.add(new FoodEntry("Chicken", 2, LocalDate.now().plusDays(7)));
        entries.add(new FoodEntry("Pizza", 2, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Cheese", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Rice", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Oatmeal", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Pineapple", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Banana", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Watermelon", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Strawberries", 2, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Yogurt", 3, LocalDate.now().minusDays(1)));
        entries.add(new FoodEntry("Lettuce", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Bread", 1, LocalDate.now().plusDays(5)));
        entries.add(new FoodEntry("Milk", 1, LocalDate.now().plusDays(2)));
        entries.add(new FoodEntry("Chicken", 2, LocalDate.now().plusDays(7)));
        entries.add(new FoodEntry("Yogurt", 3, LocalDate.now().minusDays(1)));
        entries.add(new FoodEntry("Lettuce", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Bread", 1, LocalDate.now().plusDays(5)));
        entries.add(new FoodEntry("Apple", 5, LocalDate.now().minusDays(1)));
        entries.add(new FoodEntry("Pear", 1, LocalDate.now().minusDays(1)));
        entries.add(new FoodEntry("Mango", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Pizza", 2, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Cheese", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Rice", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Oatmeal", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Pineapple", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Banana", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Watermelon", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Strawberries", 2, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Yogurt", 3, LocalDate.now().minusDays(1)));
        entries.add(new FoodEntry("Lettuce", 1, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Bread", 1, LocalDate.now().plusDays(5)));
        //-----------------------------TEMPORARY TEST ENTRIES----------------------------------------

        // Now update the expiration panel
        fd.refreshEntries(entries);
    }

    // JFrame is used to hold all various components inside a JPanel
    // Creates the window
    private void createFrame() {
        this.setTitle("ShelfLife");
        // create & titles main window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close app on exit
        this.setSize(425, 800);                        // initial window size
        this.setLayout(new BorderLayout());            // BorderLayout for top, bottom, and center
        this.setVisible(true);                     // show the window
    }

    //-----------------Database-------------------------
    private List<FoodEntry> entries = new ArrayList<>();

    // Add new food entry to list
    public void addEntry(FoodEntry entry) {
        entries.add(entry);
        //TODO: (will refresh UI)
    }

    //Get every entry for FoodExpirationPanel to use
    public List<FoodEntry> getEntries() {
        return entries;
    }

    // getter for CalendarPanel / addEntry usage
    public FoodExpirationPanel getFoodExpirationPanel() {
        return fd;
    }
    //---------------------------------------------------
}
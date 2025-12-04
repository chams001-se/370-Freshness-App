import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate; //Temporary (test entries use this)
import java.util.Scanner;

public class ShelfLife extends JFrame {
    // make FoodExpirationPanel a field so it can be accessed by CalendarPanel
    private FoodExpirationPanel fd;
    public final String DATABASE_FILENAME = "food_entries.csv";

    public ShelfLife() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");  // updated Swing theme
        } catch (Exception e) {
            e.printStackTrace();
        }
        CalendarPanel cal = new CalendarPanel();
        fd = new FoodExpirationPanel(); // assign to field
        createFrame();
        this.add(cal, BorderLayout.NORTH);
        this.add(fd, BorderLayout.CENTER);

        if (Files.exists(Paths.get(DATABASE_FILENAME))) {
            // Load CSV file database
            loadEntries(DATABASE_FILENAME);
            System.out.println("Loaded entries from " + DATABASE_FILENAME);
        }
        else {
            // Use test cases
            loadTestEntries();
            writeEntries(DATABASE_FILENAME);
            System.out.println("Entries not found, creating " + DATABASE_FILENAME);
        }

        // Update the expiration panel
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
        this.setResizable(false); // Fixed mobile screen display
    }

    //-----------------Database-------------------------
    private List<FoodEntry> entries = new ArrayList<>();

    // Writes entries arraylist to CSV file
    public void writeEntries(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Save entries to txt file
            for (int i = 0; i < entries.size(); i++) {
                writer.print(entries.get(i).getName() + ",");
                writer.print(entries.get(i).getQuantity() + ",");
                writer.print(entries.get(i).getExpirationDate());

                // Write last comma for food entry
                // unless it is the last element
                if (i < entries.size() - 1) {
                    writer.print(",");
                }
            }

            System.out.println("Saved food entries to " + filename + "!");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Loads entries from CSV file into entries arraylist
    private void loadEntries(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            scanner.useDelimiter(",");

            while (scanner.hasNextLine()) {
                String name = scanner.next();
                int quantity = scanner.nextInt();
                LocalDate expirationDate = LocalDate.parse(scanner.next());
                entries.add(new FoodEntry(name, quantity, expirationDate));
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Loads test entries into entries arraylist
    private void loadTestEntries() {
        //-----------------------------TEMPORARY TEST ENTRIES----------------------------------------
        entries.add(new FoodEntry("Very long example name", 99, LocalDate.now().plusDays(1)));
        entries.add(new FoodEntry("Distant date", 1, LocalDate.now().plusDays(31)));
        entries.add(new FoodEntry("Almost distant date", 1, LocalDate.now().plusDays(29)));
        entries.add(new FoodEntry("Milk", 1, LocalDate.now().plusDays(2)));
        entries.add(new FoodEntry("Chicken", 2, LocalDate.now().plusDays(7)));
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
    }

    // Add new food entry to list
    public void addEntry(FoodEntry entry) {
        entries.add(entry);
        writeEntries(DATABASE_FILENAME);
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
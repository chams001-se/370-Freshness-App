import javax.swing.*;
import java.awt.*;

public class ShelfLife extends JFrame {
    public ShelfLife() {
        CalendarPanel cal = new CalendarPanel();
        FoodExpirationPanel fd = new FoodExpirationPanel();
        createFrame();
        this.add(cal, BorderLayout.NORTH);
        this.add(fd, BorderLayout.CENTER);
    }

    // JFrame is used to hold all various components inside a JPanel
    // Creates the window
    private void createFrame(){
        this.setTitle("ShelfLife");
               // create & titles main window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close app on exit
        this.setSize(500, 1100);                        // initial window size
        this.setLayout(new BorderLayout());            // BorderLayout for top, bottom, and center
        this.setVisible(true);                     // show the window
    }
}
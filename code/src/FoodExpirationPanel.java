import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

// TODO
public class FoodExpirationPanel extends JPanel{

    private JLabel foodExpiration;
    FoodExpirationPanel(){
        foodExpiration = new JLabel("Food Expiring");
        foodExpiration.setFont(this.getFont().deriveFont(Font.BOLD, 22f));
        setLayout(new BorderLayout());
        foodExpiration.setHorizontalAlignment(JLabel.LEFT);
        this.add(foodExpiration, BorderLayout.NORTH);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        // It is necessary to take the parameter of the overridden paintComponent and
        // cast it into a Graphics2D as it is required in order to be able to draw anything.
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(5f));
        g2.draw(new Line2D.Double(0,0,500,0));

    }
}

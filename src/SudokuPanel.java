import javax.swing.*;
import java.awt.*;

public class SudokuPanel extends JPanel {

    private final int WIDTH;
    private final int HEIGHT;

    public SudokuPanel(){

        WIDTH = 540;
        HEIGHT = 540;

        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setLayout(new GridLayout(9,9,2,2));
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2D = (Graphics2D) g;

        g2D.setStroke(new BasicStroke(5));

        for (int i = 0; i <= WIDTH; i+=180){
            g2D.drawLine(0,i,WIDTH,i);
            g2D.drawLine(i,0,i,HEIGHT);
        }
    }
}

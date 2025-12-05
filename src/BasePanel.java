import javax.swing.*;
import java.awt.*;

public abstract class BasePanel extends JPanel {
    
    protected Main mainFrame; 
    protected Color bgDark = new Color(10, 25, 47); 
    
    protected Color accentYellow = new Color(255, 215, 0);
    protected Color textWhite = new Color(245, 245, 245);

    public BasePanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout()); 
    }

    protected abstract void initComponents();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(bgDark); 
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private int currentUserId = -1; 

    public Main() {
        setTitle("CineTix - Cinema Kiosk System");
        setSize(1000, 700); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new LoginPanel(this), "LOGIN");
        
        add(mainPanel);
        setVisible(true);
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void setCurrentUser(int userId) {
        this.currentUserId = userId;
    }

    public int getCurrentUser() {
        return currentUserId;
    }
    
    public void loadHomePanel() {
        for (Component comp : mainPanel.getComponents()) {
            if (comp.getClass().getName().equals("HomePanel")) {
                mainPanel.remove(comp);
            }
        }
        mainPanel.add(new HomePanel(this), "HOME");
        mainPanel.revalidate();
        mainPanel.repaint();
        showPanel("HOME");
    }

    public void showDetailMovie(String[] movieData) {
        DetailPanel detail = new DetailPanel(this, movieData);
        
        mainPanel.add(detail, "DETAIL");
        
        showPanel("DETAIL");
    }

    public void showBookingPanel(String[] movieData) {
        BookingPanel booking = new BookingPanel(this, movieData);
        
        mainPanel.add(booking, "BOOKING");
        showPanel("BOOKING");
    }

    public void showHistoryPanel() {
        HistoryPanel history = new HistoryPanel(this);
        history.loadData(); 
        
        mainPanel.add(history, "HISTORY");
        showPanel("HISTORY");
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> new Main());
    }
}
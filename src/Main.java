import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class Main extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private int currentUser = -1;

    private Map<String, JPanel> panels = new HashMap<>();

    public Main() {
        setTitle("CINETIX - Cinema Booking App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JRootPane root = this.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "EXIT_APP");
        root.getActionMap().put("EXIT_APP", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(Main.this, 
                    "Apakah Anda yakin ingin keluar dari aplikasi?", 
                    "Keluar", 
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        showPanel("LOGIN");

        add(mainPanel);
    }

    public void showPanel(String name) {
        if (!panels.containsKey(name)) {
            JPanel p = null;
            switch (name) {
                case "LOGIN":
                    p = new LoginPanel(this);
                    break;
                case "HOME":
                    p = new HomePanel(this);
                    break;
            }
            if (p != null) {
                panels.put(name, p);
                mainPanel.add(p, name);
            }
        }
        
        if(name.equals("HOME") && panels.get("HOME") != null) {
            mainPanel.remove(panels.get("HOME"));
            panels.remove("HOME");
            JPanel newHome = new HomePanel(this);
            panels.put("HOME", newHome);
            mainPanel.add(newHome, "HOME");
        }

        cardLayout.show(mainPanel, name);
    }

    public void showDetailMovie(String[] movieData) {
        String name = "DETAIL_" + movieData[0]; 
        DetailPanel detail = new DetailPanel(this, movieData);
        mainPanel.add(detail, name);
        cardLayout.show(mainPanel, name);
    }

    public void showBookingPanel(String[] movieData, String showtime) {
        String name = "BOOKING_" + movieData[0] + "_" + showtime;
        BookingPanel booking = new BookingPanel(this, movieData, showtime);
        mainPanel.add(booking, name);
        cardLayout.show(mainPanel, name);
    }

    public void showPaymentPanel(String[] movieData, String showtime, String seatNum) {
        String name = "PAY_" + movieData[0] + "_" + seatNum;
        PaymentPanel pay = new PaymentPanel(this, movieData, showtime, seatNum);
        mainPanel.add(pay, name);
        cardLayout.show(mainPanel, name);
    }


   public void showTicketPanel(String[] movieData, String showtime, String seatNum, String bookingCode) {
 
    String panelName = "TICKET";
    
    TicketPanel ticket = new TicketPanel(this, movieData, showtime, seatNum, bookingCode);
    
    if (panels.containsKey(panelName)) {
        mainPanel.remove(panels.get(panelName));
        panels.remove(panelName);
    }
    
    panels.put(panelName, ticket);
    mainPanel.add(ticket, panelName);
    cardLayout.show(mainPanel, panelName);
}
    
    public void showHistoryPanel() {
        String name = "HISTORY_" + currentUser;
        HistoryPanel history = new HistoryPanel(this);
        history.loadData();
        mainPanel.add(history, name);
        cardLayout.show(mainPanel, name);
    }

    public void setCurrentUser(int userId) {
        this.currentUser = userId;
    }

    public int getCurrentUser() {
        return currentUser;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
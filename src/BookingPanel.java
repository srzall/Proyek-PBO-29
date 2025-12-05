import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.QuadCurve2D;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class BookingPanel extends BasePanel {
    
    private String[] movieData;
    private String showtime;
    
    private String selectedSeatNum = null;
    private Thread timerThread;
    private boolean isRunning = false;
    private int timeLeft = 60; 
    private JLabel lblTimer;
    
    private List<SeatButton> seatButtons = new ArrayList<>();
    private Color seatAvailable = new Color(149, 165, 166);
    private Color seatBooked = new Color(52, 73, 94);
    private Color seatSelected = new Color(231, 76, 60);
    private Color btnGreen = new Color(46, 204, 113);
    private Color btnRed = new Color(192, 57, 43);

    public BookingPanel(Main mainFrame, String[] movieData, String showtime) {
        super(mainFrame); 
        this.movieData = movieData;
        this.showtime = showtime;
        initComponents();
        

        startTimer();
    }

    @Override
    protected void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false); 

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(20, 40, 10, 40));

        JButton btnBack = new JButton();
        try {
            String arrowPath = "assets/images/arrow.png"; 
            if (new File(arrowPath).exists()) {
                ImageIcon rawIcon = new ImageIcon(arrowPath);
                Image imgArrow = rawIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                btnBack.setIcon(new ImageIcon(imgArrow));
            } else {
                btnBack.setText("BACK");
            }
        } catch (Exception e) {
            btnBack.setText("BACK");
        }
        
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setForeground(textWhite); 
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> stopSession());
        
        JLabel lblTitle = new JLabel("Booking Seats", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setForeground(textWhite); 

        lblTimer = new JLabel("60s", SwingConstants.RIGHT);
        lblTimer.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTimer.setForeground(accentYellow);  

        header.add(btnBack, BorderLayout.WEST);
        header.add(lblTitle, BorderLayout.CENTER);
        header.add(lblTimer, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        
        ScreenView screenView = new ScreenView();
        screenView.setPreferredSize(new Dimension(800, 80));
        screenView.setOpaque(false); 
        centerPanel.add(screenView, BorderLayout.NORTH);

        JPanel seatLayout = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 20));
        seatLayout.setOpaque(false);

        int rows = 4; 
        int colsPerSide = 4; 

        JPanel leftBlock = new JPanel(new GridLayout(rows, colsPerSide, 15, 15)); 
        leftBlock.setOpaque(false);
        
        JPanel rightBlock = new JPanel(new GridLayout(rows, colsPerSide, 15, 15));
        rightBlock.setOpaque(false);

        List<String> bookedSeats = DatabaseHelper.getBookedSeats(Integer.parseInt(movieData[0]), showtime);
        char[] rowLabels = {'A', 'B', 'C', 'D'};

        for (int r = 0; r < rows; r++) {
            char rowChar = rowLabels[r];
            for (int c = 1; c <= colsPerSide; c++) {
                String seatNum = rowChar + String.valueOf(c);
                SeatButton btn = new SeatButton(seatNum, bookedSeats.contains(seatNum));
                leftBlock.add(btn);
                seatButtons.add(btn);
            }
            for (int c = colsPerSide + 1; c <= colsPerSide * 2; c++) {
                String seatNum = rowChar + String.valueOf(c);
                SeatButton btn = new SeatButton(seatNum, bookedSeats.contains(seatNum));
                rightBlock.add(btn);
                seatButtons.add(btn);
            }
        }

        seatLayout.add(leftBlock);
        seatLayout.add(rightBlock);
        
        JPanel seatContainer = new JPanel(new GridBagLayout());
        seatContainer.setOpaque(false);
        seatContainer.add(seatLayout);
        
        centerPanel.add(seatContainer, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 30));
        footer.setOpaque(false);

        JButton btnCancel = new RoundedButton("Batalkan Transaksi", btnRed);
        btnCancel.addActionListener(e -> stopSession());

        JButton btnPay = new RoundedButton("Pembayaran", btnGreen);
        btnPay.addActionListener(e -> handlePayment());

        footer.add(btnCancel);
        footer.add(btnPay);
        add(footer, BorderLayout.SOUTH);
    }


    class RoundedButton extends JButton {
        private Color bgColor;
        public RoundedButton(String text, Color bg) {
            super(text);
            this.bgColor = bg;
            setPreferredSize(new Dimension(220, 55));
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 16));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isRollover()) g2.setColor(bgColor.brighter());
            else g2.setColor(bgColor);
            
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }

    class SeatButton extends JToggleButton {
        private String seatNum;
        private boolean isBooked;

        public SeatButton(String seatNum, boolean isBooked) {
            this.seatNum = seatNum;
            this.isBooked = isBooked;
            setPreferredSize(new Dimension(55, 50));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(isBooked ? Cursor.getDefaultCursor() : Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            if(!isBooked) {
                addActionListener(e -> {
                    selectedSeatNum = seatNum;
                    for(SeatButton btn : seatButtons) btn.repaint(); 
                });
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isBooked) g2.setColor(seatBooked); 
            else if (seatNum.equals(selectedSeatNum)) g2.setColor(seatSelected); 
            else g2.setColor(seatAvailable); 

            g2.fillRoundRect(5, 0, 45, 35, 8, 8);
            g2.setColor(g2.getColor().darker());
            g2.fillRoundRect(5, 30, 45, 15, 8, 8);
            g2.fillRoundRect(0, 20, 8, 25, 5, 5);
            g2.fillRoundRect(47, 20, 8, 25, 5, 5);
        }
    }

    class ScreenView extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            
            g2.setColor(accentYellow); 
            g2.setStroke(new BasicStroke(4));
            QuadCurve2D q = new QuadCurve2D.Float(50, 40, w/2, 10, w-50, 40);
            g2.draw(q);
            
            GradientPaint gp = new GradientPaint(w/2, 10, new Color(255,255,255,40), w/2, 80, new Color(0,0,0,0));
            g2.setPaint(gp);
            g2.fill(q);
        }
    }

    private void handlePayment() {
        if (selectedSeatNum == null) {
            JOptionPane.showMessageDialog(this, "Pilih kursi dulu!", "Info", JOptionPane.WARNING_MESSAGE);
            return;
        }
        isRunning = false; 
        mainFrame.showPaymentPanel(movieData, showtime, selectedSeatNum);
    }

    private void startTimer() {
        isRunning = true;
        timerThread = new Thread(() -> {
            while (isRunning && timeLeft > 0) {
                try {
                    Thread.sleep(1000);
                    timeLeft--;
                    SwingUtilities.invokeLater(() -> lblTimer.setText(timeLeft + "s"));
                } catch (Exception e) {}
            }
            if (timeLeft == 0 && isRunning) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Waktu Habis!");
                    stopSession();
                });
            }
        });
        timerThread.start();
    }

    private void stopSession() {
        isRunning = false;
        mainFrame.showPanel("HOME");
    }
}
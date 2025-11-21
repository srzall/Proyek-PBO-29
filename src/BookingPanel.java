import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.util.List;

public class BookingPanel extends JPanel {
    private Main mainFrame;
    private String[] movieData;
    
    private Thread timerThread;
    private boolean isRunning = false;
    private int timeLeft = 60; 
    private JLabel lblTimer;

    private ImageIcon seatAvailable;
    private ImageIcon seatBooked;

    private final int SEAT_SIZE = 70; 
    private final int ICON_SIZE = 60; 

    public BookingPanel(Main mainFrame, String[] movieData) {
        this.mainFrame = mainFrame;
        this.movieData = movieData;

        setLayout(new BorderLayout());
        setBackground(new Color(15, 15, 20)); 

        loadSeatIcons();

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 25, 30));
        header.setBorder(new EmptyBorder(15, 40, 15, 40));

        JLabel lblTitle = new JLabel("BOOKING: " + movieData[1]);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(255, 193, 7)); 

        lblTimer = new JLabel("Waktu: " + timeLeft + "s");
        lblTimer.setFont(new Font("Monospaced", Font.BOLD, 22));
        lblTimer.setForeground(Color.WHITE);

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblTimer, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout()); 
        centerPanel.setBackground(new Color(15, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 40, 0); 
        ScreenPanel screenPanel = new ScreenPanel(); 
        screenPanel.setPreferredSize(new Dimension(800, 70)); 
        centerPanel.add(screenPanel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        
        JPanel seatLayout = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 0)); 
        seatLayout.setOpaque(false);

        int rows = 5;
        int colsPerSide = 6; 

        JPanel leftBlock = new JPanel(new GridLayout(rows, colsPerSide, 10, 15)); 
        leftBlock.setOpaque(false);
        
        JPanel rightBlock = new JPanel(new GridLayout(rows, colsPerSide, 10, 15));
        rightBlock.setOpaque(false);

        int movieId = Integer.parseInt(movieData[0]);
        List<String> bookedSeats = DatabaseHelper.getBookedSeats(movieId);
        char[] rowLabels = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};

        for (int r = 0; r < rows; r++) {
            char rowChar = rowLabels[r];
            for (int i = 1; i <= colsPerSide; i++) {
                String seatNum = rowChar + String.valueOf(i);
                leftBlock.add(createSeatButton(seatNum, bookedSeats));
            }
            for (int i = colsPerSide + 1; i <= colsPerSide * 2; i++) {
                String seatNum = rowChar + String.valueOf(i);
                rightBlock.add(createSeatButton(seatNum, bookedSeats));
            }
        }

        seatLayout.add(leftBlock);
        seatLayout.add(rightBlock);
        centerPanel.add(seatLayout, gbc);

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(15, 15, 20));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(new Color(25, 25, 30));
        footer.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JButton btnCancel = new JButton("BATALKAN TRANSAKSI");
        styleButton(btnCancel, new Color(192, 57, 43));
        btnCancel.addActionListener(e -> stopSession());
        footer.add(btnCancel);
        
        add(footer, BorderLayout.SOUTH);

        startTimer();
    }

    private class ScreenPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            GeneralPath screenShape = new GeneralPath();
            screenShape.moveTo(50, 10); 
            screenShape.lineTo(w - 50, 10); 
            screenShape.lineTo(w - 20, h); 
            screenShape.lineTo(20, h); 
            screenShape.closePath();

            GradientPaint gp = new GradientPaint(
                w / 2, 0, new Color(200, 230, 255, 200), 
                w / 2, h, new Color(200, 230, 255, 20)  
            );
            g2.setPaint(gp);
            g2.fill(screenShape);

            g2.setColor(new Color(255, 255, 255, 100));
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            FontMetrics fm = g2.getFontMetrics();
            String text = "LAYAR BIOSKOP";
            g2.drawString(text, (w - fm.stringWidth(text)) / 2, h / 2 + 5);
        }
    }

    private void loadSeatIcons() {
        try {
            String path = "assets/images/seat.png"; 
            if (new File(path).exists()) {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
                seatAvailable = new ImageIcon(img);
                seatBooked = toGrayscale(img); 
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private JButton createSeatButton(String seatNum, List<String> bookedList) {
        JButton btn = new JButton(seatNum);
        btn.setPreferredSize(new Dimension(SEAT_SIZE, SEAT_SIZE));
        btn.setHorizontalTextPosition(JButton.CENTER);
        btn.setVerticalTextPosition(JButton.CENTER);
        
        Font normalFont = new Font("SansSerif", Font.BOLD, 14);
        Font hoverFont = new Font("SansSerif", Font.BOLD, 18); 

        btn.setFont(normalFont);
        btn.setForeground(Color.WHITE);
        
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);

        boolean isBooked = bookedList.contains(seatNum);

        if (isBooked) {
            if (seatAvailable != null) btn.setIcon(seatBooked);
            else btn.setBackground(Color.DARK_GRAY);
            btn.setEnabled(false);
        } else {
            if (seatAvailable != null) btn.setIcon(seatAvailable);
            else btn.setBackground(new Color(231, 76, 60));
            
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setFont(hoverFont);
                    btn.setForeground(Color.YELLOW);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setFont(normalFont);
                    btn.setForeground(Color.WHITE);
                }
            });
            
            btn.addActionListener(e -> handleBooking(seatNum));
        }
        return btn;
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private ImageIcon toGrayscale(Image img) {
        try {
            java.awt.image.ImageFilter filter = new javax.swing.GrayFilter(true, 50);
            java.awt.image.ImageProducer producer = new java.awt.image.FilteredImageSource(img.getSource(), filter);
            return new ImageIcon(Toolkit.getDefaultToolkit().createImage(producer));
        } catch (Exception e) { return new ImageIcon(img); }
    }

    private void startTimer() {
        isRunning = true;
        timerThread = new Thread(() -> {
            while (isRunning && timeLeft > 0) {
                try {
                    Thread.sleep(1000);
                    timeLeft--;
                    SwingUtilities.invokeLater(() -> {
                        lblTimer.setText("Waktu: " + timeLeft + "s");
                        if (timeLeft <= 10) lblTimer.setForeground(Color.RED);
                    });
                } catch (InterruptedException e) {}
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

    private void handleBooking(String seatNum) {
        int harga = (int) Double.parseDouble(movieData[3]);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Booking Kursi: " + seatNum + "\nHarga: Rp " + harga, 
            "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            isRunning = false;
            boolean success = DatabaseHelper.saveBooking(mainFrame.getCurrentUser(), Integer.parseInt(movieData[0]), seatNum);
            if (success) {
                JOptionPane.showMessageDialog(this, "Berhasil!");
                mainFrame.showPanel("HOME");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal! Kursi sudah terisi.");
                mainFrame.showPanel("HOME");
            }
        }
    }

    private void stopSession() {
        isRunning = false;
        mainFrame.showPanel("HOME");
    }
}
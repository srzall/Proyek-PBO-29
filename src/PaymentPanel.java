import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class PaymentPanel extends BasePanel {
    
    private String[] movieData;
    private String showtime;
    private String seatNum;
    
    private Timer paymentTimer;
    private int timeLeft = 300; 
    private JLabel lblTimer;

    private Color textGray = new Color(170, 170, 170);


    public PaymentPanel(Main mainFrame, String[] movieData, String showtime, String seatNum) {
        super(mainFrame); 
        this.movieData = movieData;
        this.showtime = showtime;
        this.seatNum = seatNum;


        initComponents();
        startTimer();
    }

    @Override
    protected void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false); 

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(20, 40, 0, 40));

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
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setForeground(textWhite); 
        btnBack.addActionListener(e -> cancelPayment());

        lblTimer = new JLabel("300s", SwingConstants.RIGHT);
        lblTimer.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTimer.setForeground(accentYellow); 

        header.add(btnBack, BorderLayout.WEST);
        header.add(lblTimer, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(5, 0, 5, 0);

        gbc.gridy = 0;
        JLabel lblTitle = new JLabel("Pembayaran");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setForeground(textWhite);
        centerPanel.add(lblTitle, gbc);

     
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 10, 0);
        JLabel lblQR = new JLabel();
        lblQR.setPreferredSize(new Dimension(160, 160));
        lblQR.setHorizontalAlignment(SwingConstants.CENTER);
        lblQR.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4)); 
        
        try {
            String qrPath = "assets/images/qris.jpg"; 
            if (new File(qrPath).exists()) {
                ImageIcon icon = new ImageIcon(qrPath);
                Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblQR.setIcon(new ImageIcon(img));
            } else {
                lblQR.setText("<html><center>QR CODE<br>NOT FOUND</center></html>");
                lblQR.setForeground(Color.WHITE);
            }
        } catch (Exception e) {}
        centerPanel.add(lblQR, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 25, 0);
        JLabel lblInst = new JLabel("Scan QRIS dan klik continue");
        lblInst.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblInst.setForeground(textGray);
        centerPanel.add(lblInst, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 10, 0);
        JLabel lblMovieTitle = new JLabel(movieData[1]);
        lblMovieTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblMovieTitle.setForeground(textWhite);
        centerPanel.add(lblMovieTitle, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 30, 0);
        
        JPanel detailBox = new JPanel(new GridBagLayout());
        detailBox.setOpaque(false);
        
        GridBagConstraints gbcDetail = new GridBagConstraints();
        gbcDetail.insets = new Insets(3, 15, 3, 5); 
        gbcDetail.anchor = GridBagConstraints.WEST; 

        addDetailRow(detailBox, gbcDetail, 0, "Cinema", "CineTix XXI");
        addDetailRow(detailBox, gbcDetail, 1, "Date", "Hari ini, " + showtime);
        addDetailRow(detailBox, gbcDetail, 2, "Seats", seatNum);
        addDetailRow(detailBox, gbcDetail, 3, "Price", "Rp " + formatHarga(movieData[3]));

        centerPanel.add(detailBox, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 0, 0);
        
        JButton btnContinue = new RoundedBlackButton("Continue");
        btnContinue.setPreferredSize(new Dimension(200, 45)); 
        centerPanel.add(btnContinue, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }


    private void processPayment() {
        if (paymentTimer != null) paymentTimer.stop();
        
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                int userId = mainFrame.getCurrentUser();
                int movieId = Integer.parseInt(movieData[0]);
        
                Thread.sleep(500); 
                
                return DatabaseHelper.saveBooking(userId, movieId, seatNum, showtime);
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                
                try {
                    boolean success = get(); 
                    
                    if (success) {
                        String bookingCode = "CNTX-" + (int)(Math.random() * 100000);
                        mainFrame.showTicketPanel(movieData, showtime, seatNum, bookingCode);
                    } else {
                        JOptionPane.showMessageDialog(PaymentPanel.this, 
                            "Gagal! Kursi mungkin sudah diambil orang lain.");
                        mainFrame.showPanel("HOME");
                    }
                } catch (Exception e) {
                    e.printStackTrace(); 
                    JOptionPane.showMessageDialog(PaymentPanel.this, "Error: " + e.getMessage());
                }
            }
        };
        
        worker.execute(); 
    }

    private void startTimer() {
        paymentTimer = new Timer(1000, e -> {
            timeLeft--;
            lblTimer.setText(timeLeft + "s");
            
            if(timeLeft <= 10) lblTimer.setForeground(Color.RED); 

            if (timeLeft <= 0) {
                paymentTimer.stop();
                JOptionPane.showMessageDialog(this, "Waktu Pembayaran Habis!");
                cancelPayment();
            }
        });
        paymentTimer.start();
    }

    private void cancelPayment() {
        if(paymentTimer != null) paymentTimer.stop();
        mainFrame.showPanel("HOME");
    }

    private void addDetailRow(JPanel panel, GridBagConstraints gbc, int row, String label, String value) {
        gbc.gridy = row;
        gbc.gridx = 0;
        JLabel lblKey = new JLabel(label);
        lblKey.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblKey.setForeground(textGray);
        panel.add(lblKey, gbc);

        gbc.gridx = 1;
        JLabel lblVal = new JLabel(value);
        lblVal.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblVal.setForeground(textWhite);
        panel.add(lblVal, gbc);
    }

    private String formatHarga(String priceStr) {
        try { return String.format("%,.0f", Double.parseDouble(priceStr)).replace(',', '.'); } 
        catch (Exception e) { return priceStr; }
    }

    class RoundedBlackButton extends JButton {
        public RoundedBlackButton(String text) {
            super(text);
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 15));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addActionListener(e -> processPayment());
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLACK);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
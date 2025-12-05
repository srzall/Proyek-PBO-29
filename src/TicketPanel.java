import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class TicketPanel extends BasePanel {
    
    private String[] movieData;
    private String showtime;
    private String seatNum;
    private String bookingCode;

    private Color ticketBg = new Color(245, 245, 245); 
    private Color textDark = new Color(30, 30, 30);
    private Color accentRed = new Color(192, 57, 43); 


    public TicketPanel(Main mainFrame, String[] movieData, String showtime, String seatNum, String bookingCode) {
        super(mainFrame); 
        this.movieData = movieData;
        this.showtime = showtime;
        this.seatNum = seatNum;
        this.bookingCode = bookingCode;

        initComponents();
    }

    @Override
    protected void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false); 

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 40, 20, 40));

        JLabel lblHeader = new JLabel("Your Ticket", SwingConstants.CENTER);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblHeader.setForeground(textWhite); 
        
        JButton btnClose = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(accentRed);
                g2.fillOval(0, 0, getWidth(), getHeight());

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                int padding = 13; 
                int w = getWidth();
                int h = getHeight();

                g2.drawLine(padding, padding, w - padding, h - padding);
                g2.drawLine(w - padding, padding, padding, h - padding);

                g2.dispose();
            }
        };
        
        btnClose.setPreferredSize(new Dimension(45, 45));
        btnClose.setContentAreaFilled(false); 
        btnClose.setFocusPainted(false);      
        btnClose.setBorderPainted(false);     
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> mainFrame.showPanel("HOME"));

        JPanel btnContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnContainer.setOpaque(false);
        btnContainer.add(btnClose);

        JPanel dummyLeft = new JPanel();
        dummyLeft.setOpaque(false);
        dummyLeft.setPreferredSize(new Dimension(45, 45));

        header.add(dummyLeft, BorderLayout.WEST);
        header.add(lblHeader, BorderLayout.CENTER);
        header.add(btnContainer, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 20, 50); 
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel ticketCard = createTicketCard();
        centerPanel.add(ticketCard, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        
        JPanel infoRight = new JPanel();
        infoRight.setLayout(new BoxLayout(infoRight, BoxLayout.Y_AXIS));
        infoRight.setOpaque(false);

        JLabel lblShow = new JLabel("Perlihatkan Kode Tiket Pada Penjaga");
        lblShow.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblShow.setForeground(new Color(150, 160, 180));
        lblShow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblThanks = new JLabel("<html><div style='width:250px'>Terimakasih Telah Menggunakan Aplikasi kami</div></html>");
        lblThanks.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblThanks.setForeground(textWhite); 
        lblThanks.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoRight.add(lblShow);
        infoRight.add(Box.createRigidArea(new Dimension(0, 10)));
        infoRight.add(lblThanks);
        
        centerPanel.add(infoRight, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createTicketCard() {
        JPanel ticket = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int circleSize = 40;
                int yPos = h - 140; 

                Area area = new Area(new RoundRectangle2D.Double(0, 0, w, h, 20, 20));
                
                Area leftCircle = new Area(new Ellipse2D.Double(-circleSize/2.0, yPos, circleSize, circleSize));
                Area rightCircle = new Area(new Ellipse2D.Double(w - circleSize/2.0, yPos, circleSize, circleSize));
                
                area.subtract(leftCircle);
                area.subtract(rightCircle);

                g2.setColor(ticketBg);
                g2.fill(area);

                Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{8}, 0);
                g2.setStroke(dashed);
                g2.setColor(Color.GRAY);
                g2.drawLine(circleSize/2 + 5, yPos + circleSize/2, w - circleSize/2 - 5, yPos + circleSize/2);

                g2.dispose();
            }
        };
        ticket.setLayout(null); 
        ticket.setPreferredSize(new Dimension(320, 520));
        ticket.setOpaque(false); 

        JLabel lblPoster = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                try {
                    if (movieData != null && movieData.length > 4 && new File(movieData[4]).exists()) {
                        BufferedImage originalImage = ImageIO.read(new File(movieData[4]));
                        
                        int targetWidth = getWidth();
                        
                        double ratio = (double) targetWidth / originalImage.getWidth();
                        int scaledHeight = (int) (originalImage.getHeight() * ratio);
                        
                        Image scaledImg = originalImage.getScaledInstance(targetWidth, scaledHeight, Image.SCALE_SMOOTH);
                        
                        g.drawImage(scaledImg, 0, 0, null);
                    } else {
                        g.setColor(Color.BLACK);
                        g.fillRect(0, 0, getWidth(), getHeight());
                        g.setColor(Color.WHITE);
                        g.drawString("No Image", 100, 100);
                    }
                } catch (Exception e) {}
            }
        };
        lblPoster.setBounds(0, 0, 320, 220); 
        ticket.add(lblPoster);

        JLabel lblTitle = new JLabel(movieData != null ? movieData[1] : "Movie Title", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(textDark);
        lblTitle.setBounds(10, 230, 300, 30);
        ticket.add(lblTitle);

        int yStart = 270;
        addLabelValue(ticket, "Date", "Today", 30, yStart);
        addLabelValue(ticket, "Time", showtime, 130, yStart);
        addLabelValue(ticket, "Visual", "2D", 230, yStart);

        JLabel lblSeatHeader = new JLabel("Seats", SwingConstants.CENTER);
        lblSeatHeader.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSeatHeader.setForeground(Color.GRAY);
        lblSeatHeader.setBounds(0, yStart + 50, 320, 15);
        ticket.add(lblSeatHeader);

        JLabel lblSeatVal = new JLabel(seatNum, SwingConstants.CENTER);
        lblSeatVal.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblSeatVal.setForeground(textDark);
        lblSeatVal.setBounds(0, yStart + 65, 320, 40);
        ticket.add(lblSeatVal);

        JLabel lblCinema = new JLabel("CineTix XXI", SwingConstants.CENTER);
        lblCinema.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblCinema.setForeground(Color.GRAY);
        lblCinema.setBounds(0, yStart + 105, 320, 20);
        ticket.add(lblCinema);

        int yBottom = 430;
        
        JLabel lblCodeH = new JLabel("Booking Code");
        lblCodeH.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblCodeH.setForeground(Color.GRAY);
        lblCodeH.setBounds(30, yBottom, 100, 15);
        ticket.add(lblCodeH);

        JLabel lblCodeVal = new JLabel(bookingCode);
        lblCodeVal.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblCodeVal.setForeground(textDark);
        lblCodeVal.setBounds(30, yBottom + 15, 150, 30);
        ticket.add(lblCodeVal);

        JPanel barcodeBox = new JPanel();
        barcodeBox.setBackground(Color.BLACK);
        barcodeBox.setBounds(30, yBottom + 50, 140, 20);
        ticket.add(barcodeBox);

        JLabel lblQR = new JLabel("QR", SwingConstants.CENTER);
        lblQR.setOpaque(true);
        lblQR.setBackground(Color.WHITE);
        lblQR.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        lblQR.setBounds(230, yBottom, 70, 70);
        
        try {
            File qrFile = new File("assets/images/qr_small.jpg");
            if(qrFile.exists()) {
                ImageIcon qrIcon = new ImageIcon(qrFile.getPath()); 
                Image qrImg = qrIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                lblQR.setIcon(new ImageIcon(qrImg));
                lblQR.setText("");
            }
        } catch(Exception e) {}
        
        ticket.add(lblQR);

        return ticket;
    }

    private void addLabelValue(JPanel p, String title, String val, int x, int y) {
        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.PLAIN, 11));
        t.setForeground(Color.GRAY);
        t.setBounds(x, y, 80, 15);
        p.add(t);

        JLabel v = new JLabel(val);
        v.setFont(new Font("SansSerif", Font.BOLD, 14));
        v.setForeground(textDark);
        v.setBounds(x, y+15, 80, 20);
        p.add(v);
    }
}
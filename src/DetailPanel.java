import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DetailPanel extends BasePanel {
    
    private String[] movieData;
    private Image bgImage;
    private String selectedTime = ""; 

    private Color textGray = new Color(200, 200, 200);
    

    public DetailPanel(Main mainFrame, String[] movieData) {
        super(mainFrame);
        this.movieData = movieData;
        
        try {
            if (new File(movieData[4]).exists()) {
                bgImage = ImageIO.read(new File(movieData[4]));
            }
        } catch (Exception e) {}

        initComponents();
    }

    @Override
    protected void initComponents() {
        setLayout(new BorderLayout());
        
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(30, 40, 0, 0));
        
        JButton btnBack = new JButton("<< BACK"); 
        
        try {
            String arrowPath = "assets/images/arrow.png"; 
            if (new File(arrowPath).exists()) {
                ImageIcon rawIcon = new ImageIcon(arrowPath);
                Image imgArrow = rawIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                btnBack.setIcon(new ImageIcon(imgArrow));
                btnBack.setText(""); 
            }
        } catch (Exception e) {}
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnBack.setForeground(textWhite); 
        btnBack.setContentAreaFilled(false);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false); 
        btnBack.setBorder(null);         
        
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setPreferredSize(new Dimension(100, 40)); 
        
        btnBack.addActionListener(e -> mainFrame.showPanel("HOME"));
        header.add(btnBack);
        
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 50, 50, 50); 
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0; 
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        ImageIcon icon = new ImageIcon(movieData[4]); 
        Image img = icon.getImage().getScaledInstance(320, 480, Image.SCALE_SMOOTH);
        JLabel lblPoster = new JLabel(new ImageIcon(img));
        lblPoster.setBorder(BorderFactory.createLineBorder(new Color(255,255,255, 50), 1));
        content.add(lblPoster, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0; 
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(0, 50, 0, 0));

        JLabel lblTitle = new JLabel("<html>" + movieData[1].toUpperCase() + "</html>");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 48));
        lblTitle.setForeground(textWhite); 
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(lblTitle);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        metaPanel.setOpaque(false);
        metaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        addMetaTag(metaPanel, movieData[2]); 
        addDivider(metaPanel);
        
        String duration = (movieData.length > 8 && movieData[8] != null) ? movieData[8] : "2h 15m";
        addMetaTag(metaPanel, duration);
        
        addDivider(metaPanel);
        
        String rating = (movieData.length > 7 && movieData[7] != null) ? "⭐ " + movieData[7] : "⭐ 4.5";
        addMetaTag(metaPanel, rating);
        
        addDivider(metaPanel);
        
        JLabel lblPrice = new JLabel("Rp " + formatHarga(movieData[3]));
        lblPrice.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblPrice.setForeground(accentYellow); 
        metaPanel.add(lblPrice);

        infoPanel.add(metaPanel);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel lblSinopsis = new JLabel("SINOPSIS");
        lblSinopsis.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblSinopsis.setForeground(accentYellow); 
        lblSinopsis.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(lblSinopsis);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        JTextArea txtDesc = new JTextArea(movieData[6]);
        txtDesc.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtDesc.setForeground(textGray);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setOpaque(false);
        txtDesc.setBorder(null);
        txtDesc.setMaximumSize(new Dimension(800, 150)); 
        txtDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(txtDesc);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel lblSchedule = new JLabel("JADWAL TAYANG");
        lblSchedule.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblSchedule.setForeground(textWhite);
        lblSchedule.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(lblSchedule);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        timePanel.setOpaque(false);
        timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] times;
        if (movieData.length > 9 && movieData[9] != null && !movieData[9].isEmpty()) {
            times = movieData[9].split(","); 
        } else {
            times = new String[]{"10.00", "13.00", "16.00"}; 
        }

        if (times.length > 0) {
            selectedTime = times[0].trim();
        }

        for(String rawT : times) {
            String t = rawT.trim(); 
            JToggleButton btn = createTimeButton(t);
            
            btn.addActionListener(e -> {
                selectedTime = t;
                for(Component c : timePanel.getComponents()) {
                    if(c instanceof JToggleButton) ((JToggleButton)c).setSelected(false);
                }
                btn.setSelected(true);
            });
            
            if(t.equals(selectedTime)) btn.setSelected(true);
            timePanel.add(btn);
        }
        
        infoPanel.add(timePanel);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        actionPanel.setOpaque(false);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnTrailer = createActionButton("▷  TRAILER", false);
        btnTrailer.addActionListener(e -> playTrailer(movieData[5]));
        
        JButton btnBook = createActionButton("BOOK TICKET", true);
        btnBook.addActionListener(e -> mainFrame.showBookingPanel(movieData, selectedTime));

        actionPanel.add(btnTrailer);
        actionPanel.add(btnBook);
        infoPanel.add(actionPanel);

        content.add(infoPanel, gbc);
        add(content, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        Graphics2D g2 = (Graphics2D) g;
        
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
        }

        g2.setColor(new Color(5, 10, 20, 230)); 
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        GradientPaint gp = new GradientPaint(0, 0, new Color(0,0,0,200), getWidth(), 0, new Color(0,0,0,150));
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private void addMetaTag(JPanel panel, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lbl.setForeground(textGray);
        panel.add(lbl);
    }

    private void addDivider(JPanel panel) {
        JLabel lbl = new JLabel("   •   ");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lbl.setForeground(new Color(100, 100, 100));
        panel.add(lbl);
    }

    private JToggleButton createTimeButton(String text) {
        JToggleButton btn = new JToggleButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected()) {
                    g2.setColor(accentYellow); 
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                    setForeground(Color.BLACK);
                } else {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                    setForeground(textWhite); 
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(90, 35));
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createActionButton(String text, boolean isSolid) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSolid) {
                    g2.setColor(accentYellow); 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    setForeground(Color.BLACK);
                } else {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.setColor(Color.WHITE);
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                    setForeground(Color.WHITE);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(180, 50));
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private String formatHarga(String priceStr) {
        try { return String.format("%,.0f", Double.parseDouble(priceStr)).replace(',', '.'); } 
        catch (Exception e) { return priceStr; }
    }

    private void playTrailer(String filePath) {
        File videoFile = new File(filePath);
        if (!videoFile.exists()) {
            JOptionPane.showMessageDialog(this, "Trailer tidak ditemukan di:\n" + filePath, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new Thread(() -> { 
            try { 
                Desktop.getDesktop().open(videoFile); 
            } catch (IOException ex) { 
                ex.printStackTrace(); 
                SwingUtilities.invokeLater(() -> 
                    JOptionPane.showMessageDialog(this, "Gagal memutar video!")
                );
            } 
        }).start();
    }
}
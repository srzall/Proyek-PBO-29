import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class DetailPanel extends JPanel {
    private Main mainFrame;
    private String[] movieData;

    private Color bgDark = new Color(15, 20, 25); 
    private Color bgBlack = new Color(5, 5, 10); 
    private Color accentBlue = new Color(0, 168, 255); 
    private Color textWhite = new Color(245, 245, 245);

    public DetailPanel(Main mainFrame, String[] movieData) {
        this.mainFrame = mainFrame;
        this.movieData = movieData;

        setLayout(new BorderLayout());
        
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false); 
        header.setBorder(new EmptyBorder(20, 20, 0, 0));
        
        JButton btnBack = createStyledButton("<< KEMBALI", new Color(50, 50, 50), Color.WHITE, false);
        btnBack.setPreferredSize(new Dimension(120, 40));
        btnBack.addActionListener(e -> mainFrame.showPanel("HOME"));
        header.add(btnBack);
        
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(10, 50, 10, 50); 
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0; 
        gbc.gridy = 0;
        gbc.weightx = 0; 
        gbc.anchor = GridBagConstraints.NORTHWEST; 
        
        JPanel posterPanel = new JPanel(new BorderLayout());
        posterPanel.setBackground(Color.BLACK);
        posterPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, accentBlue));

        ImageIcon icon = new ImageIcon(movieData[4]); 
        Image img = icon.getImage().getScaledInstance(300, 450, Image.SCALE_SMOOTH);
        JLabel lblPoster = new JLabel(new ImageIcon(img));
        posterPanel.add(lblPoster, BorderLayout.CENTER);
        
        content.add(posterPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0; 
        gbc.anchor = GridBagConstraints.NORTHWEST; 
        
        JPanel infoBox = new JPanel();
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        infoBox.setOpaque(false);
        infoBox.setBorder(new EmptyBorder(0, 40, 0, 0)); 

        JLabel lblTitle = new JLabel("<html>" + movieData[1] + "</html>"); 
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 42));
        lblTitle.setForeground(textWhite);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT); 
        infoBox.add(lblTitle);
        
        infoBox.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        metaPanel.setOpaque(false);
        metaPanel.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        JLabel lblGenre = new JLabel(movieData[2]);
        lblGenre.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblGenre.setForeground(accentBlue);
        metaPanel.add(lblGenre);
        
        JLabel lblDiv = new JLabel("  |  ");
        lblDiv.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblDiv.setForeground(Color.GRAY);
        metaPanel.add(lblDiv);

        JLabel lblPrice = new JLabel("Rp " + formatHarga(movieData[3]));
        lblPrice.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblPrice.setForeground(new Color(46, 204, 113));
        metaPanel.add(lblPrice);
        
        infoBox.add(metaPanel);
        
        infoBox.add(Box.createRigidArea(new Dimension(0, 20)));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(1000, 1));
        sep.setForeground(new Color(60, 60, 60));
        sep.setBackground(new Color(60, 60, 60));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoBox.add(sep);
        infoBox.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel lblSinopsisHead = new JLabel("SINOPSIS");
        lblSinopsisHead.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblSinopsisHead.setForeground(Color.GRAY);
        lblSinopsisHead.setAlignmentX(Component.LEFT_ALIGNMENT); 
        infoBox.add(lblSinopsisHead);
        infoBox.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextArea txtDesc = new JTextArea(movieData[6]); 
        txtDesc.setFont(new Font("SansSerif", Font.PLAIN, 16)); 
        txtDesc.setForeground(new Color(210, 210, 210));
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setOpaque(false);
        txtDesc.setBorder(null);
        txtDesc.setAlignmentX(Component.LEFT_ALIGNMENT); 
        txtDesc.setMaximumSize(new Dimension(1000, 300)); 
        infoBox.add(txtDesc);

        infoBox.add(Box.createRigidArea(new Dimension(0, 40)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT); 

        JButton btnTrailer = createStyledButton("▶ LIHAT TRAILER", new Color(30, 30, 30), accentBlue, true);
        btnTrailer.addActionListener(e -> playTrailer(movieData[5]));
        
        JButton btnBook = createStyledButton("PILIH KURSI", accentBlue, Color.WHITE, false);
        btnBook.addActionListener(e -> mainFrame.showBookingPanel(movieData));

        buttonPanel.add(btnTrailer);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0))); 
        buttonPanel.add(btnBook);
        
        infoBox.add(buttonPanel);

        content.add(infoBox, gbc);
        add(content, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gp = new GradientPaint(0, 0, bgDark, 0, getHeight(), bgBlack);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor, boolean isOutline) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isRollover()) {
                    if (isOutline) g2.setColor(bgColor.brighter());
                    else g2.setColor(bgColor.darker());
                } else {
                    g2.setColor(bgColor);
                }

                if (isOutline) {
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 10, 10);
                } else {
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setForeground(fgColor);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false); 
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 45));
        return btn;
    }
    
    private String formatHarga(String priceStr) {
        try {
            double p = Double.parseDouble(priceStr);
            return String.format("%,.0f", p).replace(',', '.');
        } catch (Exception e) { return priceStr; }
    }

    private void playTrailer(String filePath) {
        File videoFile = new File(filePath);
        if (!videoFile.exists()) {
            JOptionPane.showMessageDialog(this, "Trailer tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new Thread(() -> {
            try {
                Desktop.getDesktop().open(videoFile);
            } catch (IOException ex) { ex.printStackTrace(); }
        }).start();
    }
}
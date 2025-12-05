import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class HomePanel extends BasePanel implements Refreshable{
    
    private JPanel moviesContainer;
    private JScrollPane scrollPane;

    private final int CARD_WIDTH = 200; 
    private final int IMAGE_HEIGHT = 300; 
    private final int CARD_HEIGHT = 420; 
    private final int GAP = 30; 

    private Color textGray = new Color(170, 170, 170);

    public HomePanel(Main mainFrame) {
        super(mainFrame); 
        initComponents(); 
    }

    @Override
    protected void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false); 

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 90)); 
        header.setBorder(new EmptyBorder(15, 40, 15, 40));

        JLabel lblBrand = new JLabel("CINETIX");
        lblBrand.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblBrand.setForeground(textWhite);
        header.add(lblBrand, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)); 
        searchPanel.setOpaque(false);
        searchPanel.add(createRoundedSearchBar()); 
        header.add(searchPanel, BorderLayout.CENTER);

        JPanel rightMenu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5)); 
        rightMenu.setOpaque(false);

        JLabel lblHistory = new JLabel("Riwayat");
        lblHistory.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblHistory.setForeground(textWhite);
        lblHistory.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblHistory.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { mainFrame.showHistoryPanel(); }
        });

        JButton btnLogout = new JButton();
        try {
            String logoutPath = "assets/images/logout.png";
            if (new File(logoutPath).exists()) {
                ImageIcon icon = new ImageIcon(logoutPath);
                Image img = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                btnLogout.setIcon(new ImageIcon(img));
            } else {
                btnLogout.setText("LOGOUT"); 
                btnLogout.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            btnLogout.setText("LOGOUT");
        }

        btnLogout.setPreferredSize(new Dimension(45, 45));
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLogout.addActionListener(e -> {
            mainFrame.setCurrentUser(-1);
            mainFrame.showPanel("LOGIN");
        });

        rightMenu.add(lblHistory);                
        rightMenu.add(btnLogout);                 
        
        header.add(rightMenu, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setOpaque(false);
        mainContent.setBorder(new EmptyBorder(20, 40, 40, 40));

        int userId = mainFrame.getCurrentUser();
        String userName = DatabaseHelper.getUsername(userId);
        
        JLabel lblSection = new JLabel("Halo " + userName + ", Ini Film Terbaru");
        lblSection.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblSection.setForeground(textWhite);
        lblSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(lblSection);
        
        mainContent.add(Box.createRigidArea(new Dimension(0, 20))); 

        moviesContainer = new JPanel();
        moviesContainer.setOpaque(false);
        moviesContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(moviesContainer);

        scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 

        scrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateLayoutGrid();
            }
        });

        loadMovies();
        add(scrollPane, BorderLayout.CENTER);
    }

    private JTextField createRoundedSearchBar() {
        JTextField txtSearch = new JTextField("Selamat Menikmati Movie") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30); 
                super.paintComponent(g);
                g2.dispose();
            }
        };
        txtSearch.setPreferredSize(new Dimension(350, 35));
        txtSearch.setOpaque(false);
        txtSearch.setForeground(Color.WHITE);
        txtSearch.setCaretColor(Color.WHITE);
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtSearch.setBorder(new RoundedBorder(30)); 
        
        return txtSearch;
    }

    private void loadMovies() {
        moviesContainer.removeAll();
        List<String[]> movies = DatabaseHelper.getAllMovies();

        if (movies.isEmpty()) {
            JLabel lblEmpty = new JLabel("Belum ada film tersedia.", SwingConstants.CENTER);
            lblEmpty.setForeground(textGray);
            moviesContainer.add(lblEmpty);
        } else {
            for (String[] movie : movies) {
                moviesContainer.add(createMovieCard(movie));
            }
        }
        updateLayoutGrid();
    }

    private void updateLayoutGrid() {
        int availableWidth = scrollPane.getViewport().getWidth() - 80; 
        if (availableWidth <= 0) return;
        int columns = Math.max(1, availableWidth / (CARD_WIDTH + GAP));
        GridLayout layout = new GridLayout(0, columns, GAP, GAP);
        moviesContainer.setLayout(layout);
        moviesContainer.revalidate();
        moviesContainer.repaint();
    }

    private JPanel createMovieCard(String[] movieData) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        
  
        card.setOpaque(false); 
 
        
        card.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        card.setBorder(null); 
        
        JLabel lblImage = new JLabel();
        lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String path = movieData[4];
        try {
            if (new File(path).exists()) {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(CARD_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(img));
            } else {
                
                lblImage.setText("No Image");
                lblImage.setHorizontalAlignment(SwingConstants.CENTER);
                lblImage.setForeground(Color.WHITE);
                lblImage.setPreferredSize(new Dimension(CARD_WIDTH, IMAGE_HEIGHT));
                lblImage.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY)); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        card.add(lblImage);

        JPanel infoBox = new JPanel();
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        
 
        infoBox.setOpaque(false);
        
        infoBox.setBorder(new EmptyBorder(10, 0, 0, 0)); 
        infoBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        String title = movieData[1];
        if (title.length() > 20) title = title.substring(0, 18) + "..."; 
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16)); 
        lblTitle.setForeground(textWhite);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT); 
        infoBox.add(lblTitle);

        infoBox.add(Box.createRigidArea(new Dimension(0, 5))); 


        JLabel lblGenre = new JLabel(movieData[2]); 
        lblGenre.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblGenre.setForeground(textGray); 
        lblGenre.setAlignmentX(Component.CENTER_ALIGNMENT); 
        infoBox.add(lblGenre);

        infoBox.add(Box.createRigidArea(new Dimension(0, 15))); 


        JButton btnDetail = new JButton("Detail");
        btnDetail.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnDetail.setForeground(Color.WHITE);
        btnDetail.setFocusPainted(false);
        btnDetail.setContentAreaFilled(false);
        btnDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
  
        btnDetail.setBorder(new RoundedBorder(20)); 
        btnDetail.setMaximumSize(new Dimension(120, 35)); 
        btnDetail.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnDetail.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { 
                btnDetail.setForeground(new Color(255, 204, 0)); 
                btnDetail.setBorder(new RoundedBorder(20) {
                     @Override
                     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                         Graphics2D g2 = (Graphics2D) g;
                         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                         g2.setColor(new Color(255, 204, 0)); 
                         g2.drawRoundRect(x, y, width - 1, height - 1, 20, 20);
                     }
                });
            }
            public void mouseExited(MouseEvent e) { 
                btnDetail.setForeground(Color.WHITE); 
                btnDetail.setBorder(new RoundedBorder(20)); 
            }
        });
        
        btnDetail.addActionListener(e -> mainFrame.showDetailMovie(movieData));
        
        infoBox.add(btnDetail);
        card.add(infoBox);

        return card;
    }

    private static class RoundedBorder implements Border {
        private int radius;
        RoundedBorder(int radius) { this.radius = radius; }
        public Insets getBorderInsets(Component c) { return new Insets(4, 15, 4, 15); }
        public boolean isBorderOpaque() { return true; }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE); 
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    @Override
    public void refreshData() {
       
        loadMovies(); 
        System.out.println("Data HomePanel diperbarui!"); 
    }
}
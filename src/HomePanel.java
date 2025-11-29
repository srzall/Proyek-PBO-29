import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class HomePanel extends JPanel {
    private Main mainFrame;
    private JPanel moviesContainer;
    private JScrollPane scrollPane;

    private final int CARD_WIDTH = 200; 
    private final int IMAGE_HEIGHT = 300; 
    private final int CARD_HEIGHT = 420; 
    private final int GAP = 30; 

    private Color bgMain = new Color(0, 18, 50);
    private Color accentRed = new Color(231, 76, 60);
    private Color textWhite = Color.WHITE;
    private Color textGray = new Color(170, 170, 170);

    public HomePanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(bgMain);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bgMain);
        header.setPreferredSize(new Dimension(0, 90)); 
        header.setBorder(new EmptyBorder(15, 40, 15, 40));

        JLabel lblBrand = new JLabel("CINETIX");
        lblBrand.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblBrand.setForeground(textWhite);
        header.add(lblBrand, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)); 
        searchPanel.setOpaque(false);
        searchPanel.add(createRoundedSearchBar()); 
        header.add(searchPanel, BorderLayout.CENTER);

        JPanel rightMenu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5)); 
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
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                btnLogout.setIcon(new ImageIcon(img));
            } else {
                btnLogout.setText("LOGOUT"); 
                btnLogout.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            btnLogout.setText("LOGOUT");
        }

        btnLogout.setPreferredSize(new Dimension(50, 50));
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
        mainContent.setBackground(bgMain);
        mainContent.setBorder(new EmptyBorder(20, 40, 40, 40));

        int userId = mainFrame.getCurrentUser();
        String userName = DatabaseHelper.getUsername(userId);
        
        JLabel lblSection = new JLabel("Halo " + userName + ", Ini Film Terbaru");
        lblSection.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblSection.setForeground(textWhite);
        lblSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(lblSection);
        
        mainContent.add(Box.createRigidArea(new Dimension(0, 20))); 

        moviesContainer = new JPanel();
        moviesContainer.setBackground(bgMain);
        moviesContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainContent.add(moviesContainer);

        scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(bgMain);
        
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
        JTextField txtSearch = new JTextField("  Film apa yang kamu inginkan?") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 20)); 
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30); 
                super.paintComponent(g);
                g2.dispose();
            }
        };
        txtSearch.setPreferredSize(new Dimension(400, 35));
        txtSearch.setOpaque(false);
        txtSearch.setForeground(Color.LIGHT_GRAY);
        txtSearch.setCaretColor(Color.WHITE);
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 13));
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
        card.setBackground(bgMain);
        card.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        
        JLabel lblImage = new JLabel();
        lblImage.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String path = movieData[4];
        try {
            if (new File(path).exists()) {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(CARD_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(img));
            } else {
                lblImage.setText("No Image");
                lblImage.setForeground(Color.WHITE);
            }
        } catch (Exception e) {}
        card.add(lblImage);

        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel lblTitle = new JLabel(movieData[1]);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitle.setForeground(textWhite);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTitle);

        card.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel lblGenre = new JLabel(movieData[2]); 
        lblGenre.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblGenre.setForeground(textGray); 
        lblGenre.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblGenre);

        card.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton btnDetail = new JButton("Detail");
        btnDetail.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnDetail.setForeground(Color.WHITE);
        btnDetail.setFocusPainted(false);
        btnDetail.setContentAreaFilled(false);
        btnDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDetail.setBorder(new RoundedBorder(30)); 
        btnDetail.setPreferredSize(new Dimension(100, 30));
        
        btnDetail.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnDetail.setForeground(accentRed); }
            public void mouseExited(MouseEvent e) { btnDetail.setForeground(Color.WHITE); }
        });
        
        btnDetail.addActionListener(e -> mainFrame.showDetailMovie(movieData));
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        btnPanel.setBackground(bgMain);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT); 
        btnPanel.setMaximumSize(new Dimension(CARD_WIDTH, 40)); 
        btnPanel.add(btnDetail);
        
        card.add(btnPanel);

        return card;
    }

    private static class RoundedBorder implements Border {
        private int radius;
        RoundedBorder(int radius) { this.radius = radius; }
        public Insets getBorderInsets(Component c) { return new Insets(4, 20, 4, 20); }
        public boolean isBorderOpaque() { return true; }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE); 
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class HomePanel extends JPanel {
    private Main mainFrame;
    private JPanel moviesContainer;
    private JScrollPane scrollPane;

    private final int CARD_WIDTH = 220; 
    private final int IMAGE_HEIGHT = 330; 
    private final int CARD_HEIGHT = 410; 
    private final int GAP = 25; 

    private Color bgMain = new Color(20, 23, 30); 
    private Color bgCard = new Color(35, 40, 50); 
    private Color accentColor = new Color(0, 168, 255); 
    private Color textWhite = new Color(245, 245, 245);
    private Color textGray = new Color(176, 190, 197);

    public HomePanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(bgMain);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bgMain);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(new EmptyBorder(20, 40, 10, 40));

        JLabel lblBrand = new JLabel("NOW SHOWING");
        lblBrand.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblBrand.setForeground(accentColor); 
        header.add(lblBrand, BorderLayout.WEST);

        JPanel navButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        navButtons.setOpaque(false);
        
        JButton btnHistory = createHeaderButton("Riwayat", new Color(50, 60, 75));
        btnHistory.addActionListener(e -> mainFrame.showHistoryPanel());
        
        JButton btnLogout = createHeaderButton("Logout", new Color(231, 76, 60)); 
        btnLogout.addActionListener(e -> {
            mainFrame.setCurrentUser(-1);
            mainFrame.showPanel("LOGIN");
        });

        navButtons.add(btnHistory);
        navButtons.add(btnLogout);
        header.add(navButtons, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        moviesContainer = new JPanel();
        moviesContainer.setBackground(bgMain);
        moviesContainer.setBorder(new EmptyBorder(10, 30, 40, 30)); 

        scrollPane = new JScrollPane(moviesContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(bgMain);

        scrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateLayoutGrid();
            }
        });

        loadMovies();
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadMovies() {
        moviesContainer.removeAll();
        List<String[]> movies = DatabaseHelper.getAllMovies();

        if (movies.isEmpty()) {
            JLabel lblEmpty = new JLabel("Belum ada film tersedia.", SwingConstants.CENTER);
            lblEmpty.setFont(new Font("SansSerif", Font.PLAIN, 18));
            lblEmpty.setForeground(textGray);
            moviesContainer.setLayout(new BorderLayout());
            moviesContainer.add(lblEmpty);
        } else {
            for (String[] movie : movies) {
                moviesContainer.add(createMovieCard(movie));
            }
        }
        updateLayoutGrid();
    }

    private void updateLayoutGrid() {
        int availableWidth = scrollPane.getViewport().getWidth() - 60; 
        if (availableWidth <= 0) return;

        int columns = Math.max(1, availableWidth / (CARD_WIDTH + GAP));
        
        GridLayout layout = new GridLayout(0, columns, GAP, GAP);
        moviesContainer.setLayout(layout);
        
        moviesContainer.revalidate();
        moviesContainer.repaint();
    }

    private JButton createHeaderButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private JPanel createMovieCard(String[] movieData) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgCard);
        card.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        card.setBorder(BorderFactory.createMatteBorder(0,0,3,0, accentColor)); 

        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setBackground(bgCard); 
        lblImage.setOpaque(true);
        
        String path = movieData[4];
        try {
            if (new File(path).exists()) {
                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(CARD_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(img));
            } else {
                lblImage.setText("<html><center>No Image</center></html>");
                lblImage.setForeground(textGray);
            }
        } catch (Exception e) { }
        
        card.add(lblImage, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(bgCard);
        infoPanel.setBorder(new EmptyBorder(12, 10, 15, 10)); 

        JLabel lblTitle = new JLabel(movieData[1]);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitle.setForeground(textWhite);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblGenre = new JLabel(movieData[2]); 
        lblGenre.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblGenre.setForeground(accentColor); 
        lblGenre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnDetail = new JButton("DETAIL");
        btnDetail.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnDetail.setBackground(new Color(25, 118, 210)); 
        btnDetail.setForeground(Color.WHITE);
        btnDetail.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDetail.setFocusPainted(false);
        btnDetail.setBorderPainted(false);
        btnDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDetail.setMaximumSize(new Dimension(120, 30)); 
        btnDetail.setMargin(new Insets(0,0,0,0));
        
        btnDetail.addActionListener(e -> mainFrame.showDetailMovie(movieData));
        
        infoPanel.add(lblTitle);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lblGenre);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        infoPanel.add(btnDetail);

        card.add(infoPanel, BorderLayout.SOUTH);

        MouseAdapter hoverCard = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { 
                Color hoverBg = new Color(45, 50, 65);
                card.setBackground(hoverBg); 
                infoPanel.setBackground(hoverBg);
                lblImage.setBackground(hoverBg);
                btnDetail.setBackground(accentColor); 
            }
            public void mouseExited(MouseEvent e) { 
                card.setBackground(bgCard); 
                infoPanel.setBackground(bgCard);
                lblImage.setBackground(bgCard);
                btnDetail.setBackground(new Color(25, 118, 210)); 
            }
        };
        
        card.addMouseListener(hoverCard);
        lblImage.addMouseListener(hoverCard);
        infoPanel.addMouseListener(hoverCard);

        return card;
    }
}
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class LoginPanel extends BasePanel {
    
    private JTextField txtUser;
    private JPasswordField txtPass;

    private Color bgRight = new Color(10, 25, 47); 
    private Color btnColor = new Color(0, 122, 255);
    private Color btnHover = new Color(0, 100, 230);
    private Color textGray = new Color(170, 170, 170);
    private Color inputBorder = new Color(60, 80, 100);
    private Color inputBg = new Color(20, 35, 60);
    

    public LoginPanel(Main mainFrame) {
        super(mainFrame); 
        initComponents(); 
    }

    @Override
    protected void initComponents() {
    
        setLayout(new GridLayout(1, 2));

        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    String path = "assets/images/image.jpg"; 
                    if (new File(path).exists()) {
                        ImageIcon icon = new ImageIcon(path);
                        Image img = icon.getImage();
                        
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        
                        double scaleWidth = (double) getWidth() / img.getWidth(null);
                        double scaleHeight = (double) getHeight() / img.getHeight(null);
                        double scale = Math.max(scaleWidth, scaleHeight);
                        
                        int w = (int) (img.getWidth(null) * scale);
                        int h = (int) (img.getHeight(null) * scale);
                        int x = (getWidth() - w) / 2;
                        int y = (getHeight() - h) / 2;
                        
                        g2.drawImage(img, x, y, w, h, null);
                    } else {
                        g.setColor(new Color(20, 20, 20));
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        };
        add(leftPanel);
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(bgRight);
        rightPanel.setBorder(new EmptyBorder(50, 60, 50, 60)); 

        JLabel lblBrand = new JLabel("CINETIX");
        lblBrand.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblBrand.setForeground(textWhite); 
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        JLabel lblWelcome = new JLabel("Welcome Back");
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblWelcome.setForeground(textWhite);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Please enter your details");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSub.setForeground(textGray);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension inputSize = new Dimension(320, 45);

        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUser.setForeground(textGray);
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel pnlUserLbl = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlUserLbl.setOpaque(false);
        pnlUserLbl.setMaximumSize(new Dimension(320, 20));
        pnlUserLbl.add(lblUser);

        txtUser = new RoundedTextField(15); 
        txtUser.setMaximumSize(inputSize);
        txtUser.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPass.setForeground(textGray);
        JPanel pnlPassLbl = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlPassLbl.setOpaque(false);
        pnlPassLbl.setMaximumSize(new Dimension(320, 20));
        pnlPassLbl.add(lblPass);

        txtPass = new RoundedPasswordField(15);
        txtPass.setMaximumSize(inputSize);
        txtPass.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnLogin = new RoundedButton("Log In", 15); 
        btnLogin.setMaximumSize(inputSize); 
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> handleLogin());

        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linkPanel.setOpaque(false);
        linkPanel.setMaximumSize(new Dimension(320, 30));
        
        JLabel lblDontHave = new JLabel("Don't have an account? ");
        lblDontHave.setForeground(textGray);
        lblDontHave.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        JLabel lblSignUp = new JLabel("Sign up");
        lblSignUp.setForeground(btnColor);
        lblSignUp.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblSignUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleRegister();
            }
        });

        linkPanel.add(lblDontHave);
        linkPanel.add(lblSignUp);

        rightPanel.add(Box.createVerticalGlue()); 
        rightPanel.add(lblBrand);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        rightPanel.add(lblWelcome);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(lblSub);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        rightPanel.add(pnlUserLbl);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(txtUser);
        
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        rightPanel.add(pnlPassLbl);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(txtPass);
        
        rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        rightPanel.add(btnLogin);
        
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(linkPanel);
        rightPanel.add(Box.createVerticalGlue()); 

        add(rightPanel);
    }

    class RoundedTextField extends JTextField {
        private int radius;
        public RoundedTextField(int radius) {
            this.radius = radius;
            setOpaque(false); 
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setFont(new Font("SansSerif", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); 
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(inputBg); 
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.setColor(inputBorder);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    class RoundedPasswordField extends JPasswordField {
        private int radius;
        public RoundedPasswordField(int radius) {
            this.radius = radius;
            setOpaque(false);
            setForeground(Color.WHITE);
            setCaretColor(Color.WHITE);
            setFont(new Font("SansSerif", Font.PLAIN, 14));
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(inputBg);
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.setColor(inputBorder);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    class RoundedButton extends JButton {
        private int radius;
        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 15));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isRollover()) {
                g2.setColor(btnHover);
            } else {
                g2.setColor(btnColor);
            }
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private void handleLogin() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());
        if (user.isEmpty() || pass.isEmpty()) return;

        int userId = DatabaseHelper.loginUser(user, pass);
        if (userId != -1) {
            mainFrame.setCurrentUser(userId); 
            mainFrame.showPanel("HOME"); 
        } else {
            JOptionPane.showMessageDialog(this, "Login Gagal!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi data dulu.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Daftar Akun Baru?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (DatabaseHelper.registerUser(user, pass)) {
                JOptionPane.showMessageDialog(this, "Sukses Daftar! Silakan Login.");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal. Username sudah ada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
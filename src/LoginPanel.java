import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class LoginPanel extends JPanel {
    private Main mainFrame;
    private JTextField txtUser;
    private JPasswordField txtPass;

    private Color colorTheme = new Color(52, 152, 219); 
    private Color colorButton = Color.BLACK; 

    public LoginPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridLayout(1, 2)); 

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(colorTheme);
        leftPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcLeft.insets = new Insets(0, 0, 20, 0);

        JLabel lblWelcome = new JLabel("<html><div style='text-align: center;'>Welcome to<br>CINE-TIX</div></html>");
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblWelcome.setForeground(Color.WHITE);
        leftPanel.add(lblWelcome, gbcLeft);

        gbcLeft.gridy++;
        String pathGambar = "assets/images/image.png"; 

        JLabel lblImage;
        if (new File(pathGambar).exists()) {
            ImageIcon originalIcon = new ImageIcon(pathGambar);
            Image scaledImage = originalIcon.getImage().getScaledInstance(180, 250, Image.SCALE_SMOOTH);
            lblImage = new JLabel(new ImageIcon(scaledImage));
            lblImage.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        } else {
            lblImage = new JLabel("<html><center>Cinema<br>Kiosk</center></html>", SwingConstants.CENTER);
            lblImage.setPreferredSize(new Dimension(180, 250));
            lblImage.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            lblImage.setForeground(Color.WHITE);
            lblImage.setFont(new Font("SansSerif", Font.BOLD, 20));
        }
        leftPanel.add(lblImage, gbcLeft);

        gbcLeft.gridy++;
        JLabel lblDesc = new JLabel("<html><center>Experience the best movies<br>with comfort and joy.</center></html>");
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblDesc.setForeground(Color.WHITE);
        gbcLeft.insets = new Insets(20, 0, 0, 0);
        leftPanel.add(lblDesc, gbcLeft);


        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());
        
        JPanel formBox = new JPanel();
        formBox.setLayout(new BoxLayout(formBox, BoxLayout.Y_AXIS));
        formBox.setBackground(Color.WHITE);
        formBox.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel lblTitle = new JLabel("Sign In");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setForeground(new Color(50, 50, 50));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formBox.add(lblTitle);

        formBox.add(Box.createRigidArea(new Dimension(0, 30)));

        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblUser.setForeground(Color.GRAY);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        formBox.add(lblUser);
        
        formBox.add(Box.createRigidArea(new Dimension(0, 5)));

        txtUser = new JTextField();
        txtUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtUser.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formBox.add(txtUser);

        formBox.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblPass.setForeground(Color.GRAY);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        formBox.add(lblPass);

        formBox.add(Box.createRigidArea(new Dimension(0, 5)));

        txtPass = new JPasswordField();
        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPass.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formBox.add(txtPass);

        formBox.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton btnLogin = new JButton("SIGN IN");
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnLogin.setBackground(colorButton); 
        btnLogin.setForeground(Color.WHITE); 
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setFocusPainted(false); 
        btnLogin.setBorderPainted(false); 
        btnLogin.setOpaque(true); 
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLogin.addActionListener(e -> handleLogin());
        formBox.add(btnLogin);

        formBox.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblNoAccount = new JLabel("New to CineTix? ");
        lblNoAccount.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        JLabel lblRegister = new JLabel("Create Account");
        lblRegister.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblRegister.setForeground(colorTheme);
        lblRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        lblRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleRegister();
            }
        });

        registerPanel.add(lblNoAccount);
        registerPanel.add(lblRegister);
        formBox.add(registerPanel);

        rightPanel.add(formBox);

        add(leftPanel);
        add(rightPanel);
    }

    private void handleLogin() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan Password tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = DatabaseHelper.loginUser(user, pass);

        if (userId != -1) {
            mainFrame.setCurrentUser(userId); 
            mainFrame.loadHomePanel(); 
        } else {
            JOptionPane.showMessageDialog(this, "Username atau Password Salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi Username dan Password di kolom input untuk mendaftar.", "Info Register", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Daftarkan akun baru dengan:\nUsername: " + user + "\nPassword: " + pass, 
            "Konfirmasi Daftar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = DatabaseHelper.registerUser(user, pass);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Akun berhasil dibuat! Silakan klik SIGN IN.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mendaftar. Username mungkin sudah dipakai.", "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class HistoryPanel extends JPanel {
    private Main mainFrame;
    private DefaultTableModel tableModel;
    private JTable historyTable;

    private Color bgMain = new Color(15, 20, 25); 
    private Color tableHeaderBg = new Color(30, 35, 45); 
    private Color accentBlue = new Color(0, 168, 255); 
    private Color rowEven = new Color(20, 25, 30); 
    private Color rowOdd = new Color(28, 33, 40); 

    public HistoryPanel(Main mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(bgMain); 
        
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        JButton btnBack = new JButton();
        try {
            String arrowPath = "assets/images/arrow.png"; 
            if (new File(arrowPath).exists()) {
                ImageIcon rawIcon = new ImageIcon(arrowPath);
                Image imgArrow = rawIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                btnBack.setIcon(new ImageIcon(imgArrow));
            } else {
                btnBack.setText("BACK");
                btnBack.setForeground(Color.WHITE);
            }
        } catch (Exception e) {
            btnBack.setText("BACK");
        }
        
        btnBack.setPreferredSize(new Dimension(50, 40));
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnBack.addActionListener(e -> mainFrame.showPanel("HOME"));

        JLabel lblTitle = new JLabel("RIWAYAT TRANSAKSI", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitle.setForeground(accentBlue);

        JPanel dummy = new JPanel(); 
        dummy.setOpaque(false); 
        dummy.setPreferredSize(new Dimension(50, 40));

        header.add(btnBack, BorderLayout.WEST);
        header.add(lblTitle, BorderLayout.CENTER);
        header.add(dummy, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        String[] columns = {"TANGGAL", "JUDUL FILM", "KURSI", "HARGA"};
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = new JTable(tableModel);
        
        historyTable.setBackground(bgMain); 
        historyTable.setFillsViewportHeight(true); 
        historyTable.setGridColor(new Color(40, 40, 50)); 
        
        styleTable(); 

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(bgMain); 
        scrollPane.setBorder(new EmptyBorder(0, 30, 30, 30)); 
        scrollPane.setOpaque(false);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        int userId = mainFrame.getCurrentUser();
        List<String[]> data = DatabaseHelper.getUserHistory(userId);

        for (String[] row : data) {
            try {
                double harga = Double.parseDouble(row[3]);
                row[3] = "Rp " + String.format("%,.0f", harga).replace(',', '.');
            } catch (Exception e) {}
            tableModel.addRow(row);
        }
    }

    private void styleTable() {
        historyTable.setFont(new Font("SansSerif", Font.PLAIN, 15));
        historyTable.setForeground(new Color(220, 220, 220));
        historyTable.setRowHeight(45);
        historyTable.setShowVerticalLines(false); 
        historyTable.setIntercellSpacing(new Dimension(0, 0));

        TableColumnModel colModel = historyTable.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(180); 
        colModel.getColumn(1).setPreferredWidth(350); 
        colModel.getColumn(2).setPreferredWidth(80); 
        colModel.getColumn(3).setPreferredWidth(120); 

        JTableHeader header = historyTable.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                l.setBackground(tableHeaderBg);
                l.setForeground(accentBlue);
                l.setFont(new Font("SansSerif", Font.BOLD, 14));
                l.setHorizontalAlignment(JLabel.CENTER);
                l.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accentBlue));
                return l;
            }
        });
        header.setPreferredSize(new Dimension(0, 50));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? rowEven : rowOdd);
                } else {
                    c.setBackground(accentBlue); 
                    c.setForeground(Color.WHITE);
                }

                setHorizontalAlignment(JLabel.CENTER);
                
                if (column == 1) {
                    setHorizontalAlignment(JLabel.LEFT);
                    setBorder(new EmptyBorder(0, 15, 0, 0));
                } else {
                    setBorder(null);
                }
                
                if(!isSelected) setForeground(new Color(220, 220, 220));

                return c;
            }
        };

        for (int i = 0; i < historyTable.getColumnCount(); i++) {
            historyTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }
}
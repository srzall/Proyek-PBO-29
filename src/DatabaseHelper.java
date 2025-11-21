import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/cinetix_db";
    private static final String USER = "root";
    private static final String PASS = ""; 

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("FATAL ERROR: Driver MySQL tidak ditemukan!");
            System.err.println("Pastikan file 'mysql-connector-j-xxxx.jar' sudah ditambahkan ke 'Referenced Libraries' di VS Code.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static int loginUser(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error Login: " + e.getMessage());
        }
        return -1; 
    }

    public static boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error Register: " + e.getMessage());
            return false;
        }
    }

    public static List<String[]> getAllMovies() {
        List<String[]> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
                while (rs.next()) {
                    String[] data = new String[7]; 
                    
                    data[0] = String.valueOf(rs.getInt("id"));
                    data[1] = rs.getString("title");
                    data[2] = rs.getString("genre");
                    data[3] = String.valueOf(rs.getDouble("price"));
                    data[4] = rs.getString("image_path");
                    data[5] = rs.getString("trailer_path");
                    
                    data[6] = rs.getString("description"); 
                    if (data[6] == null) data[6] = "Tidak ada deskripsi."; 
                    
                    movies.add(data);
                }
        } catch (SQLException e) {
            System.err.println("Error Load Movies: " + e.getMessage());
        }
        return movies;
    }

    public static List<String> getBookedSeats(int movieId) {
        List<String> bookedSeats = new ArrayList<>();
        String sql = "SELECT seat_number FROM bookings WHERE movie_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, movieId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookedSeats.add(rs.getString("seat_number"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookedSeats;
    }

    public static boolean saveBooking(int userId, int movieId, String seatNumber) {
        String sql = "INSERT INTO bookings (user_id, movie_id, seat_number) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, movieId);
            pstmt.setString(3, seatNumber);
            
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String[]> getUserHistory(int userId) {
        List<String[]> history = new ArrayList<>();
        String sql = "SELECT m.title, b.seat_number, b.booking_date, m.price " +
                     "FROM bookings b " +
                     "JOIN movies m ON b.movie_id = m.id " +
                     "WHERE b.user_id = ? " +
                     "ORDER BY b.booking_date DESC";
                     
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()) {
                String[] row = new String[4];
                row[0] = rs.getString("booking_date"); 
                row[1] = rs.getString("title"); 
                row[2] = rs.getString("seat_number"); 
                row[3] = String.valueOf(rs.getDouble("price"));
                history.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }
}
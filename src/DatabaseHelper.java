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
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static int loginUser(String username, String password) {
        String sql = "SELECT user_id FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
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
                    String[] data = new String[10]; 
                    
                    data[0] = String.valueOf(rs.getInt("movie_id"));
                    data[1] = rs.getString("title");
                    data[2] = rs.getString("genre");
                    data[3] = String.valueOf(rs.getDouble("price"));
                    data[4] = rs.getString("image_path");
                    data[5] = rs.getString("trailer_path");
                    
                    data[6] = rs.getString("description"); 
                    if (data[6] == null) data[6] = "Tidak ada deskripsi."; 
            
                    // --- DATA BARU ---
                    data[7] = rs.getString("rating");    
                    data[8] = rs.getString("duration");  
                    data[9] = rs.getString("showtimes"); 
                    
                    movies.add(data);
                }
        } catch (SQLException e) {
            System.err.println("Error Load Movies: " + e.getMessage());
        }
        return movies;
    }

    public static String getUsername(int userId) {
        String name = "User"; 
        
        try (Connection conn = getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement("SELECT username FROM users WHERE user_id = ?")) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                name = rs.getString("username");
                
                if (name != null && name.length() > 0) {
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static List<String> getBookedSeats(int movieId, String showtime) {
        List<String> bookedSeats = new ArrayList<>();
        String sql = "SELECT seat_number FROM bookings WHERE movie_id = ? AND showtime = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, movieId);
            pstmt.setString(2, showtime); 
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bookedSeats.add(rs.getString("seat_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookedSeats;
    }

    public static boolean saveBooking(int userId, int movieId, String seatNumber, String showtime) {
        String sql = "INSERT INTO bookings (user_id, movie_id, seat_number, showtime) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, movieId);
            pstmt.setString(3, seatNumber);
            pstmt.setString(4, showtime); 
            
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
                     "JOIN movies m ON b.movie_id = m.movie_id " +
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
                row[3] = String.format("Rp %,.0f", rs.getDouble("price")).replace(',', '.');
                history.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }
}
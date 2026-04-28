package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserModel {
    
    // Database connection details (Using SQLite for local DB without needing a server)
    private static final String DB_URL = "jdbc:sqlite:database.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found!");
            e.printStackTrace();
        }
    }

    public UserModel() {
        // Initialize the database and create table if not exists
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(
                "CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)")) {
            stmt.executeUpdate();
            
            // Insert default user if table is empty
            try (PreparedStatement insertStmt = conn.prepareStatement("INSERT OR IGNORE INTO users (username, password) VALUES ('admin', '1234')")) {
                insertStmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return true; // User found in DB
                }
            }
        } catch (Exception e) {
            System.err.println("Database error during validation:");
            e.printStackTrace();
        }
        
        return false; // User not found or DB error
    }
}
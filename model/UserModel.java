package model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserModel {

    // File-based database (pure Java, no external libraries needed)
    private static final String DB_FILE = "users.txt";
    private HashMap<String, String> users = new HashMap<>();

    public UserModel() {
        loadUsers();

        // Insert default user if database is empty
        if (users.isEmpty()) {
            users.put("admin", "1234");
            saveUsers();
            System.out.println("Database created with default user: admin/1234");
        } else {
            System.out.println("Database loaded. Total users: " + users.size());
        }
    }

    // Validate user credentials against the database
    public boolean validateUser(String username, String password) {
        String storedPassword = users.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    // Add a new user to the database
    public boolean addUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // User already exists
        }
        users.put(username, password);
        saveUsers();
        return true;
    }

    // Get all users (for viewing the database)
    public HashMap<String, String> getAllUsers() {
        return new HashMap<>(users);
    }

    // Load users from file
    private void loadUsers() {
        File file = new File(DB_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading database: " + e.getMessage());
        }
    }

    // Save users to file
    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DB_FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + "|" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving database: " + e.getMessage());
        }
    }
}
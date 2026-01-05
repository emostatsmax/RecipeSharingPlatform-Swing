package com.recipe.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String ROOT_URL =
            "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/recipe_platform?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASS = "qwertyuiop";

    public static void init() {
        try {
            // 1️⃣ Create database if not exists
            Connection rootConn = DriverManager.getConnection(ROOT_URL, USER, PASS);
            Statement rootStmt = rootConn.createStatement();
            rootStmt.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS recipe_platform"
            );

            rootStmt.close();
            rootConn.close();

            // 2️⃣ Connect to created database
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();

            // 3️⃣ Create users table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100),
                    email VARCHAR(100) UNIQUE,
                    password VARCHAR(100),
                    role VARCHAR(20)
                )
            """);

            // 4️⃣ Create recipes table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS recipes (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR(100),
                    description TEXT,
                    user_id INT,
                    FOREIGN KEY (user_id) REFERENCES users(id)
                )
            """);

            // 5️⃣ Insert demo users (only once)
            stmt.executeUpdate("""
                INSERT IGNORE INTO users (name, email, password, role) VALUES
                ('Admin', 'admin@demo.com', 'admin123', 'ADMIN'),
                ('User', 'user@demo.com', 'user123', 'USER')
            """);

            stmt.close();
            conn.close();

            System.out.println("✅ Database initialized successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
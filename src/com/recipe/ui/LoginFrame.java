package com.recipe.ui;

import com.recipe.dao.UserDAO;
import com.recipe.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Recipe Sharing Platform - Login");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ===== MAIN PANEL =====
        JPanel panel = new JPanel(new GridLayout(5, 1, 12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(Color.WHITE);

        // ===== EMAIL =====
        emailField = new JTextField();
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));

        // ===== PASSWORD =====
        passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        // ===== LOGIN BUTTON (FIXED VISIBILITY) =====
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(new Color(30, 144, 255)); // blue
        loginBtn.setForeground(Color.WHITE);              // 🔥 IMPORTANT
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);

        // ===== SIGNUP BUTTON =====
        JButton signupBtn = new JButton("Create New Account");
        signupBtn.setBackground(new Color(60, 179, 113)); // green
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFont(new Font("Arial", Font.BOLD, 13));
        signupBtn.setFocusPainted(false);

        // ===== ACTIONS =====
        loginBtn.addActionListener(_ -> login());
        signupBtn.addActionListener(_ -> new SignupFrame());

        // ===== ADD COMPONENTS =====
        panel.add(emailField);
        panel.add(passwordField);
        panel.add(loginBtn);
        panel.add(signupBtn);

        add(panel);
        setVisible(true);
    }

    // ================= LOGIN LOGIC =================
    private void login() {
        UserDAO dao = new UserDAO();

        User user = dao.login(
                emailField.getText().trim(),
                new String(passwordField.getPassword())
        );

        if (user == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid email or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        dispose();

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            new AdminDashboard(user);
        } else {
            new UserDashboard(user);
        }
    }
}
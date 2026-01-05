package com.recipe.ui;

import com.recipe.model.User;

import javax.swing.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard(User admin) {
        setTitle("Admin Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel label = new JLabel(
                "Welcome Admin: " + admin.getName(),
                SwingConstants.CENTER
        );
        label.setFont(label.getFont().deriveFont(20f));

        add(label);
        setVisible(true);
    }
}
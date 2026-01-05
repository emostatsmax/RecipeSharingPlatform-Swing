package com.recipe.ui;

import com.recipe.dao.UserDAO;
import com.recipe.model.User;

import javax.swing.*;
import java.awt.*;

public class ProfileDialog extends JDialog {

    public ProfileDialog(JFrame parent, User user) {
        super(parent, "My Profile", true);
        setSize(420, 360);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        UserDAO userDAO = new UserDAO();

        // ===== FORM =====
        JPanel form = new JPanel(new GridLayout(6, 1, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField(user.getName());
        JTextField emailField = new JTextField(user.getEmail());
        JPasswordField passwordField = new JPasswordField();
        JTextField profilePicField = new JTextField(user.getProfilePic());

        form.add(new JLabel("Name"));
        form.add(nameField);

        form.add(new JLabel("Email"));
        form.add(emailField);

        form.add(new JLabel("New Password (leave blank to keep current)"));
        form.add(passwordField);

        form.add(new JLabel("Profile Photo URL"));
        form.add(profilePicField);

        // ===== SAVE BUTTON =====
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setBackground(new Color(30, 144, 255));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 13));

        add(form, BorderLayout.CENTER);
        add(saveBtn, BorderLayout.SOUTH);

        // ===== SAVE ACTION =====
        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String profilePic = profilePicField.getText().trim();

            if (name.isBlank() || email.isBlank()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Name and Email cannot be empty",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            userDAO.updateProfile(
                    user.getUserId(),
                    name,
                    email,
                    password,
                    profilePic
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Profile updated successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();
        });

        setVisible(true);
    }
}
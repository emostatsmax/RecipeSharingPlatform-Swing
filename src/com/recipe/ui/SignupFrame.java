package com.recipe.ui;

import com.recipe.dao.UserDAO;

import javax.swing.*;
import java.awt.*;

public class SignupFrame extends JFrame {

    public SignupFrame() {
        setTitle("Create Account");
        setSize(350, 300);
        setLocationRelativeTo(null);

        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JPasswordField password = new JPasswordField();

        JButton signup = new JButton("Sign Up");

        signup.addActionListener(e -> {
            new UserDAO().register(
                    name.getText(),
                    email.getText(),
                    new String(password.getPassword())
            );
            JOptionPane.showMessageDialog(this, "Account created!");
            dispose();
            new LoginFrame();
        });

        JPanel p = new JPanel(new GridLayout(4,2,10,10));
        p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        p.add(new JLabel("Name")); p.add(name);
        p.add(new JLabel("Email")); p.add(email);
        p.add(new JLabel("Password")); p.add(password);
        p.add(new JLabel()); p.add(signup);

        add(p);
        setVisible(true);
    }
}
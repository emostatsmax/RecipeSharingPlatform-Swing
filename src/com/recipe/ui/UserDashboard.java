package com.recipe.ui;

import com.recipe.dao.RecipeDAO;
import com.recipe.model.Recipe;
import com.recipe.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;

public class UserDashboard extends JFrame {

    private final User user;
    private final RecipeDAO recipeDAO;
    private final JPanel feedPanel = new JPanel(); // ✅ FIX

    public UserDashboard(User user) {
        this.user = user;
        this.recipeDAO = new RecipeDAO();

        setTitle("Recipe Feed");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= TOP BAR =================
        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField recipeField = new JTextField();

        JButton addBtn = new JButton("Add Recipe");
        JButton profileBtn = new JButton("Profile");
        JButton logoutBtn = new JButton("Logout");

        addBtn.setBackground(new Color(255, 165, 0));
        profileBtn.setBackground(new Color(70, 130, 180));
        profileBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(220, 20, 60));
        logoutBtn.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightPanel.add(addBtn);
        rightPanel.add(profileBtn);
        rightPanel.add(logoutBtn);

        top.add(recipeField, BorderLayout.CENTER);
        top.add(rightPanel, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        // ================= FEED =================
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(feedPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // ================= ADD RECIPE =================
        addBtn.addActionListener(e -> {
            if (!recipeField.getText().isBlank()) {
                recipeDAO.addRecipe(recipeField.getText().trim(), user.getUserId());
                recipeField.setText("");
                loadRecipes();
            }
        });

        // ================= PROFILE =================
        profileBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Profile edit screen can be added here",
                        "Profile",
                        JOptionPane.INFORMATION_MESSAGE)
        );

        // ================= LOGOUT =================
        logoutBtn.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            ) == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame();
            }
        });

        loadRecipes();
        setVisible(true);
    }

    // ================= LOAD FEED =================
    private void loadRecipes() {
        feedPanel.removeAll();

        List<Recipe> recipes = recipeDAO.getAllRecipes();
        for (Recipe r : recipes) {
            feedPanel.add(createRecipeCard(r));
            feedPanel.add(Box.createVerticalStrut(10));
        }

        feedPanel.revalidate();
        feedPanel.repaint();
    }

    // ================= RECIPE CARD =================
    private JPanel createRecipeCard(Recipe r) {

        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(820, 130));

        // ---------- AVATAR ----------
        JLabel avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(40, 40));

        try {
            BufferedImage img;
            if (r.getProfilePic() != null && !r.getProfilePic().isBlank()) {
                img = javax.imageio.ImageIO.read(
                        URI.create(r.getProfilePic()).toURL() // ✅ FIX
                );
            } else {
                img = createDefaultAvatar();
            }
            avatarLabel.setIcon(new ImageIcon(
                    img.getScaledInstance(40, 40, Image.SCALE_SMOOTH)
            ));
        } catch (Exception ex) {
            avatarLabel.setIcon(new ImageIcon(createDefaultAvatar()));
        }

        JLabel title = new JLabel("🍽 " + r.getTitle());
        title.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel meta = new JLabel(
                "By " + r.getAuthor() + "   ❤️ " + r.getLikeCount()
        );
        meta.setForeground(Color.DARK_GRAY);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(title);
        textPanel.add(meta);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        left.add(avatarLabel);
        left.add(textPanel);

        // ---------- ACTIONS ----------
        JButton likeBtn = new JButton("Like");
        JButton commentBtn = new JButton("Comment");
        JButton deleteBtn = new JButton("Delete");

        likeBtn.setBackground(new Color(255, 182, 193));
        commentBtn.setBackground(new Color(173, 216, 230));
        deleteBtn.setForeground(Color.RED);

        likeBtn.addActionListener(e -> {
            recipeDAO.toggleLike(r.getId(), user.getUserId());
            loadRecipes();
        });

        commentBtn.addActionListener(e ->
                new CommentDialog(this, r.getId(), user.getUserId())
        );

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        actions.setOpaque(false);
        actions.add(likeBtn);
        actions.add(commentBtn);

        if (r.getUserId() == user.getUserId()) {
            actions.add(deleteBtn);
            deleteBtn.addActionListener(e -> {
                recipeDAO.deleteRecipe(r.getId());
                loadRecipes();
            });
        }

        card.add(left, BorderLayout.CENTER);
        card.add(actions, BorderLayout.EAST);

        return card;
    }

    // ================= DEFAULT AVATAR =================
    private BufferedImage createDefaultAvatar() {
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval(0, 0, 40, 40);
        g.dispose();
        return img;
    }
}
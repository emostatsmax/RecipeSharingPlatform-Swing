package com.recipe.ui;

import com.recipe.dao.RecipeDAO;
import com.recipe.model.Comment;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CommentDialog extends JDialog {

    private final RecipeDAO recipeDAO = new RecipeDAO();
    private final DefaultListModel<Comment> model = new DefaultListModel<>();

    private final int recipeId;
    private final int userId;

    public CommentDialog(JFrame parent, int recipeId, int userId) {
        super(parent, "Comments", true);
        this.recipeId = recipeId;
        this.userId = userId;

        setSize(520, 380);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // ================= LIST =================
        JList<Comment> commentList = new JList<>(model);
        commentList.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(commentList);

        // ================= INPUT =================
        JTextField input = new JTextField();

        JButton postBtn = new JButton("Post");
        JButton replyBtn = new JButton("Reply");
        JButton deleteBtn = new JButton("Delete");

        postBtn.setBackground(new Color(144, 238, 144));
        replyBtn.setBackground(new Color(173, 216, 230));
        deleteBtn.setForeground(Color.RED);
        deleteBtn.setEnabled(false); // 🔐 disabled by default

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(postBtn);
        buttonPanel.add(replyBtn);
        buttonPanel.add(deleteBtn);

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(input, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // ================= LOAD =================
        loadComments();

        // ================= ENABLE DELETE ONLY FOR OWNER =================
        commentList.addListSelectionListener(e -> {
            Comment selected = commentList.getSelectedValue();
            if (selected != null && selected.getUserId() == userId) {
                deleteBtn.setEnabled(true);
            } else {
                deleteBtn.setEnabled(false);
            }
        });

        // ================= POST COMMENT =================
        postBtn.addActionListener(e -> {
            if (!input.getText().isBlank()) {
                recipeDAO.addComment(recipeId, userId, input.getText());
                input.setText("");
                loadComments();
            }
        });

        // ================= REPLY =================
        replyBtn.addActionListener(e -> {
            Comment selected = commentList.getSelectedValue();
            if (selected == null) return;

            String replyText = JOptionPane.showInputDialog(
                    this,
                    "Enter reply:"
            );

            if (replyText != null && !replyText.isBlank()) {
                recipeDAO.addReply(
                        recipeId,
                        userId,
                        selected.getId(),
                        replyText
                );
                loadComments();
            }
        });

        // ================= DELETE (OWNER ONLY) =================
        deleteBtn.addActionListener(e -> {
            Comment selected = commentList.getSelectedValue();
            if (selected == null) return;

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete this comment?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                recipeDAO.deleteComment(selected.getId(), userId);
                loadComments();
            }
        });

        setVisible(true);
    }

    // ================= LOAD COMMENTS =================
    private void loadComments() {
        model.clear();
        List<Comment> comments = recipeDAO.getComments(recipeId);
        for (Comment c : comments) {
            model.addElement(c);
        }
    }
}
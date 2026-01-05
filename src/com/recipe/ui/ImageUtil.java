package com.recipe.util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class ImageUtil {

    public static ImageIcon makeCircle(ImageIcon icon, int size) {
        Image img = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        BufferedImage circle = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = circle.createGraphics();
        g2.setClip(new Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(img, 0, 0, null);
        g2.dispose();

        return new ImageIcon(circle);
    }
}
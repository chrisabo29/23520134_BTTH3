import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class ProductCart extends JPanel{
    private Color borderColor = null;
    private int borderThickness = 1; // Độ dày mặc định là 2px

    public ProductCart() {
        setOpaque(false);
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    public void setBorderThickness(int thickness) {
        this.borderThickness = thickness;
        repaint(); // Vẽ lại component với độ dày mới
    }

    public Color getBorderColor() {
        return borderColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Nền bo góc 30px
        g2d.setColor(new Color(244, 244, 244));
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        
        // Viền bo góc 30px (nếu có)
        if (borderColor != null) {
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(borderThickness));
            int offset = borderThickness / 2;
            g2d.drawRoundRect(offset, offset, getWidth() - borderThickness, getHeight() - borderThickness, 30, 30);
        }
        
        g2d.dispose();
    }
}

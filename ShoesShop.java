import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

// 2. Lớp JPanel tùy chỉnh để hỗ trợ hiệu ứng Fade (làm mờ)
class FadePanel extends JPanel {
    float alpha = 1.0f;

    public void setAlpha(float value) {
        this.alpha = Math.max(0.0f, Math.min(1.0f, value));
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        // Cài đặt độ trong suốt (opacity)
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        super.paint(g2d);
        g2d.dispose();
    }
}

public class ShoesShop extends JFrame {

    private JLabel mainImageLabel, mainNameLabel, mainPriceLabel, mainBrandLabel;
    private JTextArea mainDescriptionLabel;
    private FadePanel leftPreviewBox;
    private Timer fadeTimer;
    private boolean isFadingOut = false;
    private Shoes nextProductToShow = null;
    private ProductCart selectedCard = null; // Lưu thẻ sản phẩm đang được chọn
    private final Color HOVER_BORDER_COLOR = new Color(0, 120, 215); // Màu xanh khi hover
    private final Color SELECTED_BORDER_COLOR = new Color(80, 80, 80); // Màu xám đậm khi chọn
    private final EmptyBorder CARD_PADDING = new EmptyBorder(15, 10, 15, 10); // Padding của card

    public ShoesShop() {
        setSize(1100, 500);
        setLocationRelativeTo(null); // Giữa màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- DỮ LIỆU MẪU ---
        List<Shoes> products = new ArrayList<>();
        products.add(new Shoes("4DFWD PULSE SHOES", 160.0, "Adidas", "This product is excluded from all promotional discounts and offers.", "/images/img1.png"));
        products.add(new Shoes("FORUM MID SHOES", 100.0, "Adidas", "This product is excluded from all promotional discounts and offers.","/images/img2.png"));
        products.add(new Shoes("SUPERNOVA SHOES", 150.0, "Adidas", "NMD City Stock 2","/images/img3.png"));
        products.add(new Shoes("Adidas", 160.0, "Adidas", "NMD City Stock 2", "/images/img4.png"));
        products.add(new Shoes("Adidas", 120.0, "Adidas", "NMD City Stock 2", "/images/img5.png"));
        products.add(new Shoes("4DFWD PULSE SHOES", 160.0, "Adidas", "This product is excluded from all promotional discounts and offers.", "/images/img6.png"));
        products.add(new Shoes("4DFWD PULSE SHOES", 160.0, "Adidas", "This product is excluded from all promotional discounts and offers.", "/images/img1.png"));
        products.add(new Shoes("FORUM MID SHOES", 100.0, "Adidas", "This product is excluded from all promotional discounts and offers.", "/images/img2.png"));

        // --- 1. GIAO DIỆN BÊN TRÁI (CHI TIẾT SẢN PHẨM) ---
        leftPreviewBox = new FadePanel();
        leftPreviewBox.setLayout(new BoxLayout(leftPreviewBox, BoxLayout.Y_AXIS));
        leftPreviewBox.setBackground(Color.WHITE);
        leftPreviewBox.setBorder(new EmptyBorder(40, 40, 40, 0));
        // Giữ nguyên độ rộng 400 hoặc 450 tùy bạn (400 sẽ nhường thêm không gian cho danh sách)
        leftPreviewBox.setPreferredSize(new Dimension(400, 500)); 

        mainImageLabel = new JLabel();
        mainImageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainImageLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
        
        mainNameLabel = new JLabel();
        mainNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainNameLabel.setForeground(new Color(68, 72, 68));
        mainNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        mainPriceLabel = new JLabel();
        mainPriceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPriceLabel.setForeground(new Color(68, 72, 68));
        mainPriceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        mainBrandLabel = new JLabel();
        mainBrandLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainBrandLabel.setForeground(new Color(68, 72, 68));
        mainBrandLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainDescriptionLabel = new JTextArea();
        mainDescriptionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainDescriptionLabel.setForeground(new Color(180, 180, 180));
        mainDescriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        mainDescriptionLabel.setLineWrap(true);       
        mainDescriptionLabel.setWrapStyleWord(true);  

        mainDescriptionLabel.setEditable(false); // Không cho gõ chữ
        mainDescriptionLabel.setFocusable(false); // Tắt con trỏ chuột
        mainDescriptionLabel.setOpaque(false); // Trong suốt nền
        
        mainDescriptionLabel.setMaximumSize(new Dimension(360, 1000));

        leftPreviewBox.add(mainImageLabel);
        leftPreviewBox.add(Box.createRigidArea(new Dimension(0, 20))); 
        leftPreviewBox.add(mainNameLabel);
        leftPreviewBox.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPreviewBox.add(mainPriceLabel);
        leftPreviewBox.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPreviewBox.add(mainBrandLabel);
        leftPreviewBox.add(Box.createRigidArea(new Dimension(0, 5)));
        leftPreviewBox.add(mainDescriptionLabel);

        updatePreview(products.get(0)); 

        // --- 2. GIAO DIỆN BÊN PHẢI (DANH SÁCH SẢN PHẨM) ---
        JPanel gridPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(new EmptyBorder(80, 20, 80, 20));

        for (Shoes product : products) {
            gridPanel.add(createProductCard(product));
        }

        //Đặt sản phẩm đầu tiên là được chọn mặc định
        selectedCard = (ProductCart) gridPanel.getComponent(0);
        selectedCard.setBorderColor(SELECTED_BORDER_COLOR);
        selectedCard.setBorderThickness(2);

        JPanel productListPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        productListPanel.setBackground(Color.WHITE);
        productListPanel.add(gridPanel);

        JScrollPane scrollPane = new JScrollPane(productListPanel);
        scrollPane.setBorder(null); 
        // Thanh cuộn dọc (AS_NEEDED)
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        // Chỉnh tốc độ cuộn cho mượt
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // --- 3. BỐ CỤC TỔNG ---
        // Layout tổng vẫn giữ nguyên là chuẩn nhất
        add(leftPreviewBox, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        // --- KHỞI TẠO TIMER CHO HIỆU ỨNG FADE ---
        setupFadeAnimation();
    }

    // Hàm tạo 1 thẻ sản phẩm (Card)
    private JPanel createProductCard(Shoes shoes) {
        ProductCart card = new ProductCart();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 10, 15, 10));
        card.setPreferredSize(new Dimension(250, 300));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel name = new JLabel(shoes.getName());
        name.setFont(new Font("Arial", Font.BOLD, 16));
        name.setForeground(new Color(68, 72, 68));
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        name.setMaximumSize(new Dimension(Integer.MAX_VALUE, name.getPreferredSize().height));

        JLabel description = new JLabel(shoes.getDescription());
        description.setFont(new Font("Arial", Font.BOLD, 14));
        description.setForeground(new Color(180, 180, 180));
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        description.setMaximumSize(new Dimension(Integer.MAX_VALUE, description.getPreferredSize().height));

        JLabel productImg = new JLabel(scaleImage(shoes.getImagePath(), 200));
        
        JPanel imgWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        imgWrapper.setOpaque(false); // Trong suốt
        imgWrapper.add(productImg);
        imgWrapper.setAlignmentX(Component.LEFT_ALIGNMENT); // Ép Wrapper căn trái cùng với Name và Description
        productImg.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel brand = new JLabel(shoes.getBrand()); 
        brand.setFont(new Font("Arial", Font.PLAIN, 14));
        brand.setForeground(new Color(68, 72, 68));
        brand.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        JLabel price = new JLabel("$"+ shoes.getPrice() + "0");
        price.setFont(new Font("Arial", Font.BOLD, 16));
        price.setForeground(new Color(68, 72, 68));
        price.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setBackground(new Color(244, 244, 244));
        row.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        row.add(brand);
        row.add(Box.createHorizontalGlue()); // đẩy ra 2 bên
        row.add(price);

        card.add(name);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(description);
        card.add(imgWrapper);
        card.add(Box.createVerticalGlue()); // Đẩy giá xuống đáy
        card.add(row);

        // Xử lý sự kiện Click
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Bỏ chọn thẻ trước đó
                if (selectedCard != null) {
                    selectedCard.setBorderColor(null);
                    selectedCard.setBorderThickness(1);
                }
                // Đặt thẻ hiện tại là thẻ được chọn
                selectedCard = card;
                // Đặt viền xám đậm (bo góc 30px từ ProductCart)
                card.setBorderColor(SELECTED_BORDER_COLOR);
                card.setBorderThickness(2);
                startFadeAnimation(shoes);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Chỉ đặt viền xanh nếu không phải thẻ đang được chọn
                if (card != selectedCard) {
                    card.setBorderColor(HOVER_BORDER_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Bỏ viền xanh nếu không phải thẻ đang được chọn
                if (card != selectedCard) {
                    card.setBorderColor(null);
                }
            }
        });

        return card;
    }

    // Tiện ích Load và Resize ảnh
    private ImageIcon scaleImage(String path, int width) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage();
                // Tính toán chiều cao giữ nguyên tỷ lệ
                int height = (width * icon.getIconHeight()) / icon.getIconWidth();
                Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(newImg);
            }
        } catch (Exception e) { }
        return new ImageIcon(); // Trả về icon rỗng nếu lỗi
    }

    // Cập nhật thông tin bên trái
    private void updatePreview(Shoes shoes) {
        mainImageLabel.setIcon(scaleImage(shoes.getImagePath(), 350));
        mainNameLabel.setText(shoes.getName());
        mainPriceLabel.setText("$" + shoes.getPrice() + "0");
        mainBrandLabel.setText(shoes.getBrand());
        mainDescriptionLabel.setText(shoes.getDescription());
    }

    // --- XỬ LÝ HIỆU ỨNG (ANIMATION) ---
    private void setupFadeAnimation() {
        fadeTimer = new Timer(15, e -> {
            if (isFadingOut) {
                leftPreviewBox.alpha -= 0.1f;
                if (leftPreviewBox.alpha <= 0.0f) {
                    leftPreviewBox.alpha = 0.0f;
                    isFadingOut = false;
                    updatePreview(nextProductToShow); // Đổi ảnh khi đã mờ hẳn
                }
            } else {
                leftPreviewBox.alpha += 0.1f;
                if (leftPreviewBox.alpha >= 1.0f) {
                    leftPreviewBox.alpha = 1.0f;
                    fadeTimer.stop(); // Dừng animation khi đã hiện rõ
                }
            }
            leftPreviewBox.repaint();
        });
    }

    private void startFadeAnimation(Shoes shoes) {
        if (fadeTimer.isRunning()) return; // Chống spam click
        nextProductToShow = shoes;
        isFadingOut = true;
        fadeTimer.start();
    }

    public static void main(String[] args) {
        // Cài đặt giao diện Look And Feel hệ thống cho đẹp hơn
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> {
            new ShoesShop().setVisible(true);
        });
    }
}
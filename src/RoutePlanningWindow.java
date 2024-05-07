import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoutePlanningWindow extends JFrame {
    public RoutePlanningWindow() {
        setTitle("路线规划");
        setSize(320, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create buttons
        JButton viewMapButton = new JButton("查看地图");
        JButton routePlanningButton = new JButton("路线规划");

        // Add an action listener to the viewMapButton
        viewMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    ImageLoader loader = new ImageLoader();
                    loader.setVisible(true);

                    // 示例用法
                    loader.loadImage("map.jpg"); // 将 "map.jpg" 替换为实际的图片路径
                });
            }
        });

        // Create a panel and add the buttons
        JPanel panel = new JPanel();
        panel.add(viewMapButton);
        panel.add(routePlanningButton);

        // Add the panel to the frame
        add(panel);
    }
}

  class ImageLoader extends JFrame {

     private ImageIcon imageIcon;
     private JLabel label;

     public ImageLoader() {
         setTitle("Image Loader"); // 设置窗口标题
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭时的操作

         label = new JLabel(); // 创建一个标签用于显示图片
         getContentPane().add(label, BorderLayout.CENTER); // 将标签添加到窗口的中间位置

         // 添加一个组件监听器以监听窗口大小的改变事件
         addComponentListener(new ComponentAdapter() {
             @Override
             public void componentResized(ComponentEvent e) {
                 // 窗口大小改变时重新调整图片大小
                 resizeImage();
             }
         });

         setSize(800, 1200); // 设置窗口默认大小为800x1200
         setLocationRelativeTo(null); // 将窗口置于屏幕中央
     }

     /**
      * 加载并显示图片
      * @param imagePath 图片的路径
      */
     public void loadImage(String imagePath) {
         // 加载图片
         imageIcon = new ImageIcon(imagePath);
         label.setIcon(imageIcon); // 将图片设置到标签中
         resizeImage(); // 初始时调整图片大小
         pack(); // 调整窗口大小以适应图片大小
     }

     /**
      * 调整图片大小以适应标签大小
      */
     private void resizeImage() {
         if (imageIcon != null) {
             // 获取标签的当前大小
             Dimension size = label.getSize();
             // 将图片缩放以适应标签大小
             Image scaledImage = imageIcon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
             ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
             // 将缩放后的图片设置到标签中
             label.setIcon(scaledImageIcon);
         }
     }


 }

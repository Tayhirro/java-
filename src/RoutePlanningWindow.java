import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoutePlanningWindow extends JFrame {
    public RoutePlanningWindow() {
        setTitle("·�߹滮");
        setSize(320, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create buttons
        JButton viewMapButton = new JButton("�鿴��ͼ");
        JButton routePlanningButton = new JButton("·�߹滮");

        // Add an action listener to the viewMapButton
        viewMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    ImageLoader loader = new ImageLoader();
                    loader.setVisible(true);

                    // ʾ���÷�
                    loader.loadImage("map.jpg"); // �� "map.jpg" �滻Ϊʵ�ʵ�ͼƬ·��
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
         setTitle("Image Loader"); // ���ô��ڱ���
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô��ڹر�ʱ�Ĳ���

         label = new JLabel(); // ����һ����ǩ������ʾͼƬ
         getContentPane().add(label, BorderLayout.CENTER); // ����ǩ��ӵ����ڵ��м�λ��

         // ���һ������������Լ������ڴ�С�ĸı��¼�
         addComponentListener(new ComponentAdapter() {
             @Override
             public void componentResized(ComponentEvent e) {
                 // ���ڴ�С�ı�ʱ���µ���ͼƬ��С
                 resizeImage();
             }
         });

         setSize(800, 1200); // ���ô���Ĭ�ϴ�СΪ800x1200
         setLocationRelativeTo(null); // ������������Ļ����
     }

     /**
      * ���ز���ʾͼƬ
      * @param imagePath ͼƬ��·��
      */
     public void loadImage(String imagePath) {
         // ����ͼƬ
         imageIcon = new ImageIcon(imagePath);
         label.setIcon(imageIcon); // ��ͼƬ���õ���ǩ��
         resizeImage(); // ��ʼʱ����ͼƬ��С
         pack(); // �������ڴ�С����ӦͼƬ��С
     }

     /**
      * ����ͼƬ��С����Ӧ��ǩ��С
      */
     private void resizeImage() {
         if (imageIcon != null) {
             // ��ȡ��ǩ�ĵ�ǰ��С
             Dimension size = label.getSize();
             // ��ͼƬ��������Ӧ��ǩ��С
             Image scaledImage = imageIcon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
             ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
             // �����ź��ͼƬ���õ���ǩ��
             label.setIcon(scaledImageIcon);
         }
     }


 }

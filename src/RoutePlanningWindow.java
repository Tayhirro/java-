import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// ������
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

        // Add an action listener to the routePlanningButton
        routePlanningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RouteInputWindow().setVisible(true);
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
// ·�����봰��
class RouteInputWindow extends JFrame {
    private JTextField startField;
    private JTextField endField;
    private JButton submitButton;

    private String start;
    private String end;

    public RouteInputWindow() {
        RoutePlanningSystem routePlanningSystem = new RoutePlanningSystem();

        setTitle("·������");
        setSize(240, 140);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create UI components
        startField = new JTextField(20);
        endField = new JTextField(20);
        submitButton = new JButton("�ύ");

        // Create a panel and add the components
        JPanel panel = new JPanel();
        panel.add(new JLabel("���:"));
        panel.add(startField);
        panel.add(new JLabel("�յ�:"));
        panel.add(endField);
        panel.add(submitButton);

        // Add the panel to the frame
        add(panel);

        // Add action listener to the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start = startField.getText();
                end = endField.getText();
                System.out.println("���: " + start);
                System.out.println("�յ�: " + end);
                int startId = routePlanningSystem.getPointId(start);
                int endId = routePlanningSystem.getPointId(end);
                if (startId == -1) {
                    // �ڴ����д�ӡ������Ϣ���������ڿ���̨�д�ӡ
                    JOptionPane.showMessageDialog(RouteInputWindow.this, "��㲻����");
                } else if (endId == -1) {
                    JOptionPane.showMessageDialog(RouteInputWindow.this, "�յ㲻����");
                } else {

                }
            }
        });
    }
}

// ͼƬ������
class ImageLoader extends JFrame {

    private ImageIcon imageIcon;
    private JLabel label;

    public ImageLoader() {
        setTitle("Image Loader"); // ���ô��ڱ���
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ���ô��ڹر�ʱ�Ĳ���

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
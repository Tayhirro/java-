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
        JButton routePlanningButton = new JButton("����·�߹滮");
        JButton multiPointRoutePlanningButton = new JButton("���·�߹滮");

        // Add an action listener to the viewMapButton
        // When the button is clicked, a new ImageLoader will be created
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
        // When the button is clicked, a new RouteInputWindow will be created
        routePlanningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SinglePointRouteInputWindow().setVisible(true);
            }
        });

        // Add an action listener to the multiPointRoutePlanningButton
        // When the button is clicked, a new MultiPointRouteInputWindow will be created
        multiPointRoutePlanningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MultiPointRouteInputWindow().setVisible(true);
            }
        });

        // Create a panel and add the buttons
        JPanel panel = new JPanel();
        panel.add(viewMapButton);
        panel.add(routePlanningButton);
        panel.add(multiPointRoutePlanningButton);

        // Add the panel to the frame
        add(panel);

    }
}

// ����·�����봰��
class SinglePointRouteInputWindow extends JFrame {
    private JTextField startField;
    private JTextField endField;
    private JButton submitButton;

    private String start;
    private String end;

    public SinglePointRouteInputWindow() {

        RoutePlanningSystem routePlanningSystem = new RoutePlanningSystem();

        setTitle("·������");
        setSize(300, 240);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create radio buttons
        JRadioButton lengthButton = new JRadioButton("����");
        JRadioButton timeButton = new JRadioButton("ʱ��");
        JRadioButton walkingButton = new JRadioButton("����");
        JRadioButton bikingButton = new JRadioButton("���г�");
        JRadioButton drivingButton = new JRadioButton("�ݳ�");

        // Create button groups, so the radio buttons are mutually exclusive
        ButtonGroup sortingGroup = new ButtonGroup();
        sortingGroup.add(lengthButton);
        sortingGroup.add(timeButton);

        ButtonGroup transportationGroup = new ButtonGroup();
        transportationGroup.add(walkingButton);
        transportationGroup.add(bikingButton);
        transportationGroup.add(drivingButton);

        // Default select the first radio button
        lengthButton.setSelected(true);
        walkingButton.setSelected(true);

        // Create a panel and add the components
        JPanel panel = new JPanel(new GridLayout(0, 1));

        // Create panels for the radio buttons
        JPanel sortingPanel = new JPanel();
        sortingPanel.add(new JLabel("����ʽ:"));
        sortingPanel.add(lengthButton);
        sortingPanel.add(timeButton);

        JPanel transportationPanel = new JPanel();
        transportationPanel.add(new JLabel("��ͨ��ʽ:"));
        transportationPanel.add(walkingButton);
        transportationPanel.add(bikingButton);
        transportationPanel.add(drivingButton);

        // Add the radio button panels to the main panel
        panel.add(sortingPanel);
        panel.add(transportationPanel);


        // Create UI components
        startField = new JTextField(20);
        endField = new JTextField(20);
        submitButton = new JButton("�ύ");


        JPanel startPanel = new JPanel();
        startPanel.add(new JLabel("���:"));
        startPanel.add(startField);

        JPanel endPanel = new JPanel();
        endPanel.add(new JLabel("�յ�:"));
        endPanel.add(endField);

        panel.add(startPanel);
        panel.add(endPanel);

        // Create a panel for the submit button with FlowLayout
        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitPanel.add(submitButton);
        panel.add(submitPanel);

        // Add the panel to the frame
        add(panel, BorderLayout.CENTER);

        // Add action listener to the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start = startField.getText();
                end = endField.getText();
                System.out.println("���: " + start);
                System.out.println("�յ�: " + end);

                //��ȡ��ͨ��ʽ������ʽ
                String transportation = "";
                String sorting = "";
                if (walkingButton.isSelected()) {
                    transportation = "sidewalk";
                } else if (bikingButton.isSelected()) {
                    transportation = "cycleway";
                } else if (drivingButton.isSelected()) {
                    transportation = "road";
                }

                if (lengthButton.isSelected()) {
                    sorting = "length";
                } else if (timeButton.isSelected()) {
                    sorting = "time";
                }
                // ��ȡ�����յ��ID
                int startId = routePlanningSystem.getPointId(start);
                int endId = routePlanningSystem.getPointId(end);

                if (startId == -1) {
                    // �ڴ����д�ӡ������Ϣ���������ڿ���̨�д�ӡ
                    JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "��㲻����");
                } else if (endId == -1) {
                    JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "�յ㲻����");
                } else {
                    // ����·�߹滮ϵͳ�ķ�������ȡ·�������ڴ��ڻ滭·��
                    int[] path = new int[1000];
                    path[0] = startId;
                    path[1] = endId;
                    if (sorting.equals("length")) {
                        double length = routePlanningSystem.dijkstraLength(startId, endId, transportation, path);

                        JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "���·������Ϊ: " + length);
                    } else if (sorting.equals("time")) {
                        double time = routePlanningSystem.dijkstraTime(startId, endId, transportation, path);

                        JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "���·��ʱ��Ϊ: " + time);
                    }
                    //����·�������λ����routePlanningSystem.points[path[i]]�У�����֮�仭�ߣ�����ͼΪmap.jpg
                    SwingUtilities.invokeLater(() -> {
                        PathLoader loader = new PathLoader(path, routePlanningSystem.points);
                        loader.setVisible(true);
                        loader.loadImage("map.jpg"); // �� "map.jpg" �滻Ϊʵ�ʵ�ͼƬ·��
                    });

                }
            }
        });

    }

}

// ���·�����봰��
class MultiPointRouteInputWindow extends JFrame{

}


// ·����������������ʾ·��
class PathLoader extends JFrame {
    private ImageIcon imageIcon;
    private JLabel label;
    private int[] path;
    private MyPoint[] points;
    private int imageWidth; // Add this line to store the original image width
    private int imageHeight; // Add this line to store the original image height

    public PathLoader(int[] path, MyPoint[] points) {
        this.path = path;
        this.points = points;

        setTitle("Image Loader");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);

                for (int i = 0; i < path.length - 1; i++) {
                    int x1 = (int) (points[path[i]].x * getWidth() / (double) imageWidth);
                    int y1 = (int) (points[path[i]].y * getHeight() / (double) imageHeight);
                    int x2 = (int) (points[path[i + 1]].x * getWidth() / (double) imageWidth);
                    int y2 = (int) (points[path[i + 1]].y * getHeight() / (double) imageHeight);
                    g.drawLine(x1, y1, x2, y2);
                }
            }
        };

        getContentPane().add(label, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeImage();
            }
        });

        setSize(800, 1200);
        setLocationRelativeTo(null);
    }

    public void loadImage(String imagePath) {
        imageIcon = new ImageIcon(imagePath);
        imageWidth = imageIcon.getIconWidth(); // Store the original image width
        imageHeight = imageIcon.getIconHeight(); // Store the original image height
        label.setIcon(imageIcon);
        pack();
    }

    private void resizeImage() {
        if (imageIcon != null) {
            Dimension size = label.getSize();
            Image scaledImage = imageIcon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
            label.setIcon(scaledImageIcon);
        }
    }
}

// ͼƬ��������������ʾͼƬ
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
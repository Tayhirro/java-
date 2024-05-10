import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


// ������
public class RoutePlanningWindow extends JFrame {
    public RoutePlanningWindow() {

        setTitle("·�߹滮"); // ���ô��ڱ���
        setSize(200, 160); // ���ô��ڴ�С
        setResizable(false);  // �������ڴ�С
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ���ùرղ���Ϊ�ͷŴ�����Դ
        setLocationRelativeTo(null); // ������������Ļ����
        setVisible(true); // ���ô��ڿɼ�

        // ������ť
        JButton viewMapButton = new JButton("�鿴��ͼ");
        JButton singleRoutePlanningButton = new JButton("����·�߹滮");
        JButton multiPointRoutePlanningButton = new JButton("���·�߹滮");

        // ���ò���
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5)); // ʹ��GridLayout���֣�3��1��
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // ����ڱ߾�

        // ��Ӱ�ť�����
        panel.add(viewMapButton);
        panel.add(singleRoutePlanningButton);
        panel.add(multiPointRoutePlanningButton);

        add(panel); // �������ӵ�������



        // ������
        viewMapButton.addActionListener(_ -> SwingUtilities.invokeLater(() -> {
            ImageLoader loader = new ImageLoader();
            loader.setVisible(true);
            loader.loadImage("map.jpg"); // �� "map.jpg" �滻Ϊʵ�ʵ�ͼƬ·��
        }));

        singleRoutePlanningButton.addActionListener(_ -> new SinglePointRouteInputWindow().setVisible(true));

        multiPointRoutePlanningButton.addActionListener(_ -> new MultiPointRouteInputWindow().setVisible(true));


    }
}

// ����·�����봰��
class SinglePointRouteInputWindow extends JFrame {
    private final JTextField startField;
    private final JTextField endField;
    private final JButton submitButton;

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
        submitButton.addActionListener(_ -> {
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
                if (sorting.equals("length")) {
                    double length = routePlanningSystem.dijkstraLength(startId, endId, transportation, path);
                    if (length == -1) {
                        JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "�޷��ҵ�·��");
                        return;
                    }
                    JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "���·������Ϊ: " + length);
                } else if (sorting.equals("time")) {
                    double time = routePlanningSystem.dijkstraTime(startId, endId, transportation, path);
                    if (time == -1) {
                        JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "�޷��ҵ�·��");
                        return;
                    }
                    JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "���·��ʱ��Ϊ: " + time);
                }
                //����·�������λ����routePlanningSystem.points[path[i]]�У�����֮�仭�ߣ�����ͼΪmap.jpg
                SwingUtilities.invokeLater(() -> {
                    PathLoader loader = new PathLoader(path, routePlanningSystem.points);
                    loader.setVisible(true);
                    loader.loadImage("map.jpg"); // �� "map.jpg" �滻Ϊʵ�ʵ�ͼƬ·��
                });
            }
        });
    }

}

// ���·�����봰��
class MultiPointRouteInputWindow extends JFrame{
    private final JTextField startField;
    private final JTextField endsField;
    private final JButton submitButton;

    private String start;
    private String ends;

    public MultiPointRouteInputWindow() {

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
        endsField = new JTextField(20);
        submitButton = new JButton("�ύ");


        JPanel startPanel = new JPanel();
        startPanel.add(new JLabel("���:"));
        startPanel.add(startField);

        JPanel endsPanel = new JPanel();
        endsPanel.add(new JLabel("�յ�:"));
        endsPanel.add(endsField);

        panel.add(startPanel);
        panel.add(endsPanel);

        // Create a panel for the submit button with FlowLayout
        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitPanel.add(submitButton);
        panel.add(submitPanel);

        // Add the panel to the frame
        add(panel, BorderLayout.CENTER);

        // Add action listener to the submit button
        submitButton.addActionListener(_ -> {
            start = startField.getText();
            ends = endsField.getText();
            System.out.println("���: " + start);
            System.out.println("�յ�: " + ends);

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
            // ��ȡ���
            int startId = routePlanningSystem.getPointId(start);
            if (startId == -1) {
                // �ڴ����д�ӡ������Ϣ���������ڿ���̨�д�ӡ
                JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "��㲻����");
            }
            int []endArray = new int[1000];
            String[] endArrayString = ends.split(" ");
            for (int i = 0; i < endArrayString.length; i++) {
                int endId = routePlanningSystem.getPointId(endArrayString[i]);
                if (endId == -1) {
                    JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "�յ�: "+ endArrayString[i] + " ������");
                    return;
                }
                endArray[i] = endId;
            }
            // ����·�߹滮ϵͳ�ķ�������ȡ·�������ڴ��ڻ滭·��
            int[] path = new int[1000];
            if (sorting.equals("length")) {
                double length = routePlanningSystem.tspLength(startId, endArray, transportation, path);
                if(length == -1){
                    JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "�޷��ҵ�·��");
                    return;
                }
                JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "���·������Ϊ: " + length);
            } else if (sorting.equals("time")) {
                double time = routePlanningSystem.tspTime(startId, endArray, transportation, path);
                if(time == -1){
                    JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "�޷��ҵ�·��");
                    return;
                }
                JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "���·��ʱ��Ϊ: " + time);
            }
            //����·�������λ����routePlanningSystem.points[path[i]]�У�����֮�仭�ߣ�����ͼΪmap.jpg
            SwingUtilities.invokeLater(() -> {
                var loader = new PathLoader(path, routePlanningSystem.points);
                loader.setVisible(true);
                loader.loadImage("map.jpg"); // �� "map.jpg" �滻Ϊʵ�ʵ�ͼƬ·��
            });
        });
    }
}

// ·����������������ʾ·��
class PathLoader extends JFrame {
    private ImageIcon imageIcon;
    private final JLabel label;
    private int imageWidth; // Add this line to store the original image width
    private int imageHeight; // Add this line to store the original image height

    public PathLoader(int[] path, MyPoint[] points) {

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
    private final JLabel label;



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
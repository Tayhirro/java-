import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


// ·���滮������
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



        // �鿴��ͼ��ť������,�����ť�󵯳���ͼ����
        viewMapButton.addActionListener(_ -> SwingUtilities.invokeLater(() -> {
            ImageLoader loader = new ImageLoader();
            loader.setVisible(true);
            loader.loadImage("map.jpg"); // �� "map.jpg" �滻Ϊʵ�ʵ�ͼƬ·��
        }));
        // ����·�߹滮��ť������,�����ť�󵯳�����·�����봰��
        singleRoutePlanningButton.addActionListener(_ -> new SinglePointRouteInputWindow().setVisible(true));
        // ���·�߹滮��ť������,�����ť�󵯳����·�����봰��
        multiPointRoutePlanningButton.addActionListener(_ -> new MultiPointRouteInputWindow().setVisible(true));


    }
}

// ����·�����봰��
class SinglePointRouteInputWindow extends JFrame {
    private final JTextField startField;//��������
    private final JTextField endField;//�յ������

    private String start;//���
    private String end;//�յ�

    public SinglePointRouteInputWindow() {

        RoutePlanningSystem routePlanningSystem = new RoutePlanningSystem();

        setTitle("·������");
        setSize(300, 240);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // ��ӵ�ѡ��ť
        JRadioButton lengthButton = new JRadioButton("����");
        JRadioButton timeButton = new JRadioButton("ʱ��");
        JRadioButton walkingButton = new JRadioButton("����");
        JRadioButton bikingButton = new JRadioButton("���г�");
        JRadioButton drivingButton = new JRadioButton("�ݳ�");

        // ������ť�飬ʹ��ѡ��ť����
        ButtonGroup sortingGroup = new ButtonGroup();
        sortingGroup.add(lengthButton);
        sortingGroup.add(timeButton);

        ButtonGroup transportationGroup = new ButtonGroup();
        transportationGroup.add(walkingButton);
        transportationGroup.add(bikingButton);
        transportationGroup.add(drivingButton);

        // Ĭ��ѡ���һ����ѡ��ť
        lengthButton.setSelected(true);
        walkingButton.setSelected(true);

        // ����һ����岢������
        JPanel panel = new JPanel(new GridLayout(0, 1));

        // ������ѡ��ť�����
        JPanel sortingPanel = new JPanel();
        sortingPanel.add(new JLabel("����ʽ:"));
        sortingPanel.add(lengthButton);
        sortingPanel.add(timeButton);

        JPanel transportationPanel = new JPanel();
        transportationPanel.add(new JLabel("��ͨ��ʽ:"));
        transportationPanel.add(walkingButton);
        transportationPanel.add(bikingButton);
        transportationPanel.add(drivingButton);

        // ����ѡ��ť�����ӵ������
        panel.add(sortingPanel);
        panel.add(transportationPanel);


        // ���������
        startField = new JTextField(20);
        endField = new JTextField(20);
        JButton submitButton = new JButton("�ύ");

        JPanel startPanel = new JPanel();
        startPanel.add(new JLabel("���:"));
        startPanel.add(startField);

        JPanel endPanel = new JPanel();
        endPanel.add(new JLabel("�յ�:"));
        endPanel.add(endField);

        panel.add(startPanel);
        panel.add(endPanel);

        // ����һ�����о��ж�����ύ��ť�����
        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitPanel.add(submitButton);
        panel.add(submitPanel);

        // �������ӵ�������
        add(panel, BorderLayout.CENTER);

        // Ϊ�ύ��ť��Ӷ���������
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
                JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "��㲻����");
            } else if (endId == -1) {
                JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "�յ㲻����");
            } else {
                // ����·�߹滮ϵͳ�ķ�������ȡ·�������ڴ��ڻ滭·��
                int[] path = new int[1000];
                if (sorting.equals("length")) {
                    double length = routePlanningSystem.dijkstra(startId, endId, transportation,false, path);
                    if (length == -1) {
                        JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "�޷��ҵ�·��");
                        return;
                    }
                    JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "���·������Ϊ: " + length);
                } else if (sorting.equals("time")) {
                    double time = routePlanningSystem.dijkstra(startId, endId, transportation, true, path);
                    if (time == -1) {
                        JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "�޷��ҵ�·��");
                        return;
                    }
                    JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "���·��ʱ��Ϊ: " + time);
                }
                System.out.println("path: ");
                for(int i = 0; i < path.length; i++){
                    if(path[i] == -1){
                        break;
                    }
                    System.out.print(path[i] + " ");
                }
                //����·�������λ����routePlanningSystem.point[path[i]]�У�����֮�仭�ߣ�����ͼΪmap.jpg
                SwingUtilities.invokeLater(() -> {
                    PathLoader loader = new PathLoader(path, routePlanningSystem.point);
                    loader.setVisible(true);
                    loader.loadImage("map.jpg"); // �� "map.jpg" �滻Ϊʵ�ʵ�ͼƬ·��
                });
            }
        });
    }

}

// ���·�����봰��,�����߼��뵥��·�����봰�����ƣ�ֻ�Ƕ���һ���յ�������Լ�����յ�Ĵ��������߼���ͬ������׸��
class MultiPointRouteInputWindow extends JFrame{
    private final JTextField startField;
    private final JTextField endsField;

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
        JButton submitButton = new JButton("�ύ");



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
                double length = routePlanningSystem.tsp(startId, endArray, transportation, false,path);
                if(length == -1){
                    JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "�޷��ҵ�·��");
                    return;
                }
                JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "���·������Ϊ: " + length);
            } else if (sorting.equals("time")) {
                double time = routePlanningSystem.tsp(startId, endArray, transportation,true, path);
                if(time == -1){
                    JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "�޷��ҵ�·��");
                    return;
                }
                JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "���·��ʱ��Ϊ: " + time);
            }
            //����·�������λ����routePlanningSystem.point[path[i]]�У�����֮�仭�ߣ�����ͼΪmap.jpg
            SwingUtilities.invokeLater(() -> {
                var loader = new PathLoader(path, routePlanningSystem.point);
                loader.setVisible(true);
                loader.loadImage("map.jpg"); // �� "map.jpg" �滻Ϊʵ�ʵ�ͼƬ·��
            });
        });
    }
}

// ·����������������ʾ·��
class PathLoader extends JFrame {
    private ImageIcon imageIcon; // ���ڴ洢ͼ���ͼ��
    private final JLabel label; // ������ʾͼ��ı�ǩ
    private int imageWidth; // �洢ԭʼͼ����
    private int imageHeight; // �洢ԭʼͼ��߶�

    // ���캯��������·���͵������
    public PathLoader(int[] path, MyPoint[] point) {

        setTitle("Image Loader"); // ���ô��ڱ���
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ���ô��ڹر�ʱ�Ĳ���

        // ����һ����ǩ����д��paintComponent�����������ϻ���·��
        label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED); // ���û�����ɫΪ��ɫ

                // ����·���е�ÿһ�Ե㣬����һ���ӵ�һ���㵽�ڶ��������
                for (int i = 0; i < path.length - 1; i++) {
                    int x1 = (int) (point[path[i]].x * getWidth() / (double) imageWidth);
                    int y1 = (int) (point[path[i]].y * getHeight() / (double) imageHeight);
                    int x2 = (int) (point[path[i + 1]].x * getWidth() / (double) imageWidth);
                    int y2 = (int) (point[path[i + 1]].y * getHeight() / (double) imageHeight);
                    g.drawLine(x1, y1, x2, y2); // ������
                }
            }
        };

        getContentPane().add(label, BorderLayout.CENTER); // ����ǩ��ӵ����ڵ�����λ��

        // ���һ������������������ڴ�С�ı�ʱ������ͼ��Ĵ�С
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeImage();
            }
        });

        setSize(400, 600); // ���ô��ڵĳ�ʼ��С
        setLocationRelativeTo(null); // ������λ����������Ļ����
    }

    // ����ͼ��, imagePathΪ����ͼ���·��
    public void loadImage(String imagePath) {
        imageIcon = new ImageIcon(imagePath); // ����ͼ��
        imageWidth = imageIcon.getIconWidth(); // �洢ԭʼͼ��Ŀ��
        imageHeight = imageIcon.getIconHeight(); // �洢ԭʼͼ��ĸ߶�
        label.setIcon(imageIcon); // ��ͼ�����õ���ǩ��
        pack(); // �������ڴ�С����Ӧ��ǩ����ѡ��С
    }

    // ����ͼ��Ĵ�С����Ӧ��ǩ�Ĵ�С
    private void resizeImage() {
        if (imageIcon != null) { // ���ͼ���Ѿ�����
            Dimension size = label.getSize(); // ��ȡ��ǩ�ĵ�ǰ��С
            // ��ͼ����������Ӧ��ǩ�Ĵ�С
            Image scaledImage = imageIcon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(scaledImage); // ����һ���µ�ͼ��ͼ��
            label.setIcon(scaledImageIcon); // �����ź��ͼ�����õ���ǩ��
        }
    }
}
// ͼƬ��������������ʾͼƬ
class ImageLoader extends JFrame {

    private ImageIcon imageIcon; // ���ڴ洢ͼ���ͼ��
    private final JLabel label; // ������ʾͼ��ı�ǩ

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

        setSize(400, +00); // ���ô���Ĭ�ϴ�СΪ800x1200
        setLocationRelativeTo(null); // ������������Ļ����
    }

   //���ز���ʾͼƬ,imagePath ͼƬ��·��
    public void loadImage(String imagePath) {
        // ����ͼƬ
        imageIcon = new ImageIcon(imagePath);
        label.setIcon(imageIcon); // ��ͼƬ���õ���ǩ��
        resizeImage(); // ��ʼʱ����ͼƬ��С
        pack(); // �������ڴ�С����ӦͼƬ��С
    }

    //����ͼƬ��С����Ӧ��ǩ��С
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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

class RoutePlanningPanel extends JPanel {

    JPanel submitPanel; // 提交的面板
    JPanel pathPanel; // 路径面板
    GraphPanel graphPanel; // 图形面板
    JPanel titlePanel; // 标题面板
    User currUser;// 当前用户
    UserManagement userManagement;// 用户管理对象
    SpotManagement spotManagement;// 景点管理对象

    public RoutePlanningPanel(User currUser, UserManagement userManagement, SpotManagement spotManagement) {
        this.currUser = currUser;
        this.userManagement = userManagement;
        this.spotManagement = spotManagement;

        // 创建标题面板
        titlePanel = new TitlePanel("路线规划");
        // 创建图形面板
        graphPanel = new GraphPanel("source\\map.jpg");
        // 创建提交面板
        submitPanel = new FormPanel(graphPanel);// 创建表单面板,并传入图形面板,用于数据交互

        // 设置整个面板的布局为GridBagLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 将三个面板添加到整个面板中
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // 标题面板占据2列宽
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        add(titlePanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // submitPanel 占据 1 列宽
        gbc.weightx = 0.07; // submitPanel 占据 20% 的宽度
        gbc.weighty = 0.9;
        gbc.fill = GridBagConstraints.BOTH;
        submitPanel.setPreferredSize(new Dimension(125, 0)); // 设置 submitPanel 宽度为 100
        add(submitPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // graphPanel 占据 1 列宽
        gbc.weightx = 0.93; // graphPanel 占据 80% 的宽度
        gbc.weighty = 0.9;
        gbc.fill = GridBagConstraints.BOTH;
        add(graphPanel, gbc);

    }

}

class TitlePanel extends JPanel {

    public TitlePanel(String title) {
        setPreferredSize(new Dimension(0, 20)); // Set title panel height
        setLayout(new GridBagLayout());

        // 添加标题标签
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));

        // 设置标题标签在标题面板中的位置
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // 设置标题面板的背景色和边框
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
    }
}

class FormPanel extends JPanel {

    JTextField startField;
    List<JPanel> destinationPanels;
    JPanel destinationContainerPanel;
    JButton addButton, submitButton;
    RoutePlanningSystem routePlanningSystem;// 路线规划系统
    GraphPanel graphPanel;// 图形面板

    JRadioButton lengthButton = new JRadioButton("长度");
    JRadioButton timeButton = new JRadioButton("时间");
    JRadioButton walkingButton = new JRadioButton("步行");
    JRadioButton bikingButton = new JRadioButton("骑行");
    JRadioButton drivingButton = new JRadioButton("驾车");

    public FormPanel(GraphPanel graph) {
        this.graphPanel = graph;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建按钮组，使单选按钮互斥
        ButtonGroup sortingGroup = new ButtonGroup();
        sortingGroup.add(lengthButton);
        sortingGroup.add(timeButton);

        ButtonGroup transportationGroup = new ButtonGroup();
        transportationGroup.add(walkingButton);
        transportationGroup.add(bikingButton);
        transportationGroup.add(drivingButton);

        // 默认选择第一个单选按钮
        lengthButton.setSelected(true);
        walkingButton.setSelected(true);

        // 创建单选按钮的面板
        JPanel sortingPanel = new JPanel(new GridLayout(2, 1, 0, 0));

        JPanel sortingButtonPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        sortingButtonPanel.add(lengthButton);
        sortingButtonPanel.add(timeButton);
        sortingPanel.add(new JLabel("排序方式:"));
        sortingPanel.add(sortingButtonPanel);

        JPanel transportationPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        JPanel transportationButtonPanel = new JPanel(new GridLayout(1, 3, 0, 0));

        transportationButtonPanel.add(walkingButton);
        transportationButtonPanel.add(bikingButton);
        transportationButtonPanel.add(drivingButton);
        transportationPanel.add(new JLabel("交通方式:"));
        transportationPanel.add(transportationButtonPanel);

        // Create address fields
        JPanel addressPanel = new JPanel(new GridLayout(3, 1, 5, 0));
        JLabel startLabel = new JLabel("起始地址:");
        JLabel endLabel = new JLabel("目的地址:");
        startField = new JTextField(12);
        addressPanel.add(startLabel);
        addressPanel.add(startField);
        addressPanel.add(endLabel);

        // Create a panel for the address and buttons
        JPanel firstPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        firstPanel.add(sortingPanel);
        firstPanel.add(transportationPanel);
        firstPanel.add(addressPanel);
        add(firstPanel, BorderLayout.NORTH);

        destinationPanels = new ArrayList<>();
        destinationContainerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        addDestinationPanel();
        add(destinationContainerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        addButton = new JButton("+");
        addButton.addActionListener(e -> addDestinationPanel());
        buttonPanel.add(addButton);

        submitButton = new JButton("确定");
        submitButton.addActionListener(e -> handleSubmit());
        buttonPanel.add(submitButton);

        add(buttonPanel, BorderLayout.SOUTH);
        setSize(100, 400);
        routePlanningSystem = new RoutePlanningSystem();
    }

    void addDestinationPanel() {

        JPanel panel = createDestinationPanel();
        destinationContainerPanel.add(panel);
        destinationPanels.add(panel);
        destinationContainerPanel.revalidate();
        destinationContainerPanel.repaint();
        repaint();
    }

    void removeDestinationPanel(JPanel panel) {
        if (destinationPanels.size() > 1) {
            destinationContainerPanel.remove(panel);
            destinationPanels.remove(panel);
            destinationContainerPanel.revalidate();
            destinationContainerPanel.repaint();//
            repaint();
        }
    }

    JPanel createDestinationPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 4));
        JTextField field = new JTextField(12);
        JButton removeButton = new JButton("-");
        removeButton.setPreferredSize(new Dimension(20, 20));
        removeButton.setFont(removeButton.getFont().deriveFont(Font.BOLD, 6));
        removeButton.addActionListener(e -> removeDestinationPanel(panel));
        panel.add(field);
        panel.add(removeButton);
        return panel;
    }

    void handleSubmit() {
        String start = startField.getText();
        List<String> destinations = new ArrayList<>();
        for (JPanel panel : destinationPanels) {
            JTextField field = (JTextField) panel.getComponent(0);
            destinations.add(field.getText());
        }

        //获取交通方式和排序方式
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
        // 获取起点
        int startId = routePlanningSystem.getPointId(start);
        if (startId == -1) {
            // 在控制台中打印错误信息
            JOptionPane.showMessageDialog(this, "起点不存在", "错误", JOptionPane.ERROR_MESSAGE);
        }

        int[] endArray = new int[destinations.size()];
        for (int i = 0; i < destinations.size(); i++) {
            int endId = routePlanningSystem.getPointId(destinations.get(i));
            if (endId == -1) {
                JOptionPane.showMessageDialog(this, "目的地 " + destinations.get(i) + " 不存在", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            endArray[i] = endId;
        }
        // 调用路线规划系统的方法，获取路径，并在窗口绘画路径
        int[] path = new int[1000];//path[0]存储路径的长度

        //单点路径规划
        if (destinations.size() == 1) {
            if (sorting.equals("length")) {
                double length = routePlanningSystem.dijkstra(startId, endArray[0], transportation, false, path);
                if (length == -1) {
                    //弹出提示框，提示路径不存在
                    JOptionPane.showMessageDialog(this, "路径不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            } else if (sorting.equals("time")) {
                double time = routePlanningSystem.dijkstra(startId, endArray[0], transportation, true, path);
                if (time == -1) {
                    //弹出提示框，提示路径不存在
                    JOptionPane.showMessageDialog(this, "路径不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }
        } else {//多点路径规划
            if (sorting.equals("length")) {
                double length = routePlanningSystem.tsp(startId, endArray, transportation, false, path);
                if (length == -1) {
                    //弹出提示框，提示路径不存在
                    JOptionPane.showMessageDialog(this, "路径不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            } else if (sorting.equals("time")) {
                double time = routePlanningSystem.tsp(startId, endArray, transportation, true, path);
                if (time == -1) {
                    //弹出提示框，提示路径不存在
                    JOptionPane.showMessageDialog(this, "路径不存在", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }
        }
        int[] pointx = new int[path[0]];
        int[] pointy = new int[path[0]];
        JOptionPane.showMessageDialog(this, path[0] - 1, "", JOptionPane.ERROR_MESSAGE);
        for (int i = 0; i < path[0]; i++) {
            pointx[i] = routePlanningSystem.point[path[i + 1]].x;
            pointy[i] = routePlanningSystem.point[path[i + 1]].y;
            System.out.println("x:" + pointx[i] + " y:" + pointy[i] + " id:" + path[i + 1]);
        }

        graphPanel.drawPath(pointx, pointy);
    }
}

class GraphPanel extends JPanel {

    private BufferedImage graphImage;
    private int[] pointx = new int[0];  // Initialize to prevent null pointer issues
    private int[] pointy = new int[0];  // Initialize to prevent null pointer issues

    // Constructor that loads the image from the file path
    public GraphPanel(String graphFile) {
        try {
            graphImage = ImageIO.read(new File(graphFile));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "无法加载图像: " + graphFile, "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to set the path to be drawn and repaint the panel
    public void drawPath(int[] pointx, int[] pointy) {
        if (pointx.length != pointy.length) {
            throw new IllegalArgumentException("pointx and pointy must have the same length");
        }
        this.pointx = pointx;
        this.pointy = pointy;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g; // Convert to Graphics2D
        if (graphImage != null) {
            // Draw the image
            g2d.drawImage(graphImage, 0, 0, this.getWidth(), this.getHeight(), null);
        }

        // Draw the path if it exists
        if (pointx.length > 1 && pointy.length > 1) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3)); // Set line width to 3
            for (int i = 0; i < pointx.length - 1; i++) {
                // Map your path points to coordinates
                int x1 = mapToX(pointx[i]);
                int y1 = mapToY(pointy[i]);
                int x2 = mapToX(pointx[i + 1]);
                int y2 = mapToY(pointy[i + 1]);
                g2d.drawLine(x1, y1, x2, y2);
            }
        }
    }

    // Example mapping method - replace with your actual mapping logic
    private int mapToX(int point) {
        // Example mapping - replace with actual logic
        return point; // Simple example: assuming pointx already in coordinate space
    }

    private int mapToY(int point) {
        // Example mapping - replace with actual logic
        return point; // Simple example: assuming pointy already in coordinate space
    }
}

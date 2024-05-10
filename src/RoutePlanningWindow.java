import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


// 主窗口
public class RoutePlanningWindow extends JFrame {
    public RoutePlanningWindow() {

        setTitle("路线规划"); // 设置窗口标题
        setSize(200, 160); // 设置窗口大小
        setResizable(false);  // 锁定窗口大小
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 设置关闭操作为释放窗口资源
        setLocationRelativeTo(null); // 将窗口置于屏幕中央
        setVisible(true); // 设置窗口可见

        // 创建按钮
        JButton viewMapButton = new JButton("查看地图");
        JButton singleRoutePlanningButton = new JButton("单点路线规划");
        JButton multiPointRoutePlanningButton = new JButton("多点路线规划");

        // 设置布局
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5)); // 使用GridLayout布局，3行1列
        panel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // 添加内边距

        // 添加按钮到面板
        panel.add(viewMapButton);
        panel.add(singleRoutePlanningButton);
        panel.add(multiPointRoutePlanningButton);

        add(panel); // 将面板添加到窗口中



        // 监听器
        viewMapButton.addActionListener(_ -> SwingUtilities.invokeLater(() -> {
            ImageLoader loader = new ImageLoader();
            loader.setVisible(true);
            loader.loadImage("map.jpg"); // 将 "map.jpg" 替换为实际的图片路径
        }));

        singleRoutePlanningButton.addActionListener(_ -> new SinglePointRouteInputWindow().setVisible(true));

        multiPointRoutePlanningButton.addActionListener(_ -> new MultiPointRouteInputWindow().setVisible(true));


    }
}

// 单点路线输入窗口
class SinglePointRouteInputWindow extends JFrame {
    private final JTextField startField;
    private final JTextField endField;
    private final JButton submitButton;

    private String start;
    private String end;

    public SinglePointRouteInputWindow() {

        RoutePlanningSystem routePlanningSystem = new RoutePlanningSystem();

        setTitle("路线输入");
        setSize(300, 240);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create radio buttons
        JRadioButton lengthButton = new JRadioButton("长度");
        JRadioButton timeButton = new JRadioButton("时间");
        JRadioButton walkingButton = new JRadioButton("步行");
        JRadioButton bikingButton = new JRadioButton("自行车");
        JRadioButton drivingButton = new JRadioButton("驾车");

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
        sortingPanel.add(new JLabel("排序方式:"));
        sortingPanel.add(lengthButton);
        sortingPanel.add(timeButton);

        JPanel transportationPanel = new JPanel();
        transportationPanel.add(new JLabel("交通方式:"));
        transportationPanel.add(walkingButton);
        transportationPanel.add(bikingButton);
        transportationPanel.add(drivingButton);

        // Add the radio button panels to the main panel
        panel.add(sortingPanel);
        panel.add(transportationPanel);


        // Create UI components
        startField = new JTextField(20);
        endField = new JTextField(20);
        submitButton = new JButton("提交");


        JPanel startPanel = new JPanel();
        startPanel.add(new JLabel("起点:"));
        startPanel.add(startField);

        JPanel endPanel = new JPanel();
        endPanel.add(new JLabel("终点:"));
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
            System.out.println("起点: " + start);
            System.out.println("终点: " + end);

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
            // 获取起点和终点的ID
            int startId = routePlanningSystem.getPointId(start);
            int endId = routePlanningSystem.getPointId(end);

            if (startId == -1) {
                // 在窗口中打印错误信息，而不是在控制台中打印
                JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "起点不存在");
            } else if (endId == -1) {
                JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "终点不存在");
            } else {
                // 调用路线规划系统的方法，获取路径，并在窗口绘画路径
                int[] path = new int[1000];
                if (sorting.equals("length")) {
                    double length = routePlanningSystem.dijkstraLength(startId, endId, transportation, path);
                    if (length == -1) {
                        JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "无法找到路径");
                        return;
                    }
                    JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "最短路径长度为: " + length);
                } else if (sorting.equals("time")) {
                    double time = routePlanningSystem.dijkstraTime(startId, endId, transportation, path);
                    if (time == -1) {
                        JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "无法找到路径");
                        return;
                    }
                    JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "最短路径时间为: " + time);
                }
                //画出路径，点的位置在routePlanningSystem.points[path[i]]中，两点之间画线，背景图为map.jpg
                SwingUtilities.invokeLater(() -> {
                    PathLoader loader = new PathLoader(path, routePlanningSystem.points);
                    loader.setVisible(true);
                    loader.loadImage("map.jpg"); // 将 "map.jpg" 替换为实际的图片路径
                });
            }
        });
    }

}

// 多点路线输入窗口
class MultiPointRouteInputWindow extends JFrame{
    private final JTextField startField;
    private final JTextField endsField;
    private final JButton submitButton;

    private String start;
    private String ends;

    public MultiPointRouteInputWindow() {

        RoutePlanningSystem routePlanningSystem = new RoutePlanningSystem();

        setTitle("路线输入");
        setSize(300, 240);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create radio buttons
        JRadioButton lengthButton = new JRadioButton("长度");
        JRadioButton timeButton = new JRadioButton("时间");
        JRadioButton walkingButton = new JRadioButton("步行");
        JRadioButton bikingButton = new JRadioButton("自行车");
        JRadioButton drivingButton = new JRadioButton("驾车");

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
        sortingPanel.add(new JLabel("排序方式:"));
        sortingPanel.add(lengthButton);
        sortingPanel.add(timeButton);

        JPanel transportationPanel = new JPanel();
        transportationPanel.add(new JLabel("交通方式:"));
        transportationPanel.add(walkingButton);
        transportationPanel.add(bikingButton);
        transportationPanel.add(drivingButton);

        // Add the radio button panels to the main panel
        panel.add(sortingPanel);
        panel.add(transportationPanel);


        // Create UI components
        startField = new JTextField(20);
        endsField = new JTextField(20);
        submitButton = new JButton("提交");


        JPanel startPanel = new JPanel();
        startPanel.add(new JLabel("起点:"));
        startPanel.add(startField);

        JPanel endsPanel = new JPanel();
        endsPanel.add(new JLabel("终点:"));
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
            System.out.println("起点: " + start);
            System.out.println("终点: " + ends);

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
                // 在窗口中打印错误信息，而不是在控制台中打印
                JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "起点不存在");
            }
            int []endArray = new int[1000];
            String[] endArrayString = ends.split(" ");
            for (int i = 0; i < endArrayString.length; i++) {
                int endId = routePlanningSystem.getPointId(endArrayString[i]);
                if (endId == -1) {
                    JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "终点: "+ endArrayString[i] + " 不存在");
                    return;
                }
                endArray[i] = endId;
            }
            // 调用路线规划系统的方法，获取路径，并在窗口绘画路径
            int[] path = new int[1000];
            if (sorting.equals("length")) {
                double length = routePlanningSystem.tspLength(startId, endArray, transportation, path);
                if(length == -1){
                    JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "无法找到路径");
                    return;
                }
                JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "最短路径长度为: " + length);
            } else if (sorting.equals("time")) {
                double time = routePlanningSystem.tspTime(startId, endArray, transportation, path);
                if(time == -1){
                    JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "无法找到路径");
                    return;
                }
                JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "最短路径时间为: " + time);
            }
            //画出路径，点的位置在routePlanningSystem.points[path[i]]中，两点之间画线，背景图为map.jpg
            SwingUtilities.invokeLater(() -> {
                var loader = new PathLoader(path, routePlanningSystem.points);
                loader.setVisible(true);
                loader.loadImage("map.jpg"); // 将 "map.jpg" 替换为实际的图片路径
            });
        });
    }
}

// 路径加载器，用于显示路径
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

// 图片加载器，用于显示图片
class ImageLoader extends JFrame {

    private ImageIcon imageIcon;
    private final JLabel label;



    public ImageLoader() {
        setTitle("Image Loader"); // 设置窗口标题
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 设置窗口关闭时的操作

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
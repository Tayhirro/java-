import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


// 路径规划主窗口
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



        // 查看地图按钮监听器,点击按钮后弹出地图窗口
        viewMapButton.addActionListener(_ -> SwingUtilities.invokeLater(() -> {
            ImageLoader loader = new ImageLoader();
            loader.setVisible(true);
            loader.loadImage("map.jpg"); // 将 "map.jpg" 替换为实际的图片路径
        }));
        // 单点路线规划按钮监听器,点击按钮后弹出单点路线输入窗口
        singleRoutePlanningButton.addActionListener(_ -> new SinglePointRouteInputWindow().setVisible(true));
        // 多点路线规划按钮监听器,点击按钮后弹出多点路线输入窗口
        multiPointRoutePlanningButton.addActionListener(_ -> new MultiPointRouteInputWindow().setVisible(true));


    }
}

// 单点路线输入窗口
class SinglePointRouteInputWindow extends JFrame {
    private final JTextField startField;//起点输入框
    private final JTextField endField;//终点输入框

    private String start;//起点
    private String end;//终点

    public SinglePointRouteInputWindow() {

        RoutePlanningSystem routePlanningSystem = new RoutePlanningSystem();

        setTitle("路线输入");
        setSize(300, 240);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // 添加单选按钮
        JRadioButton lengthButton = new JRadioButton("长度");
        JRadioButton timeButton = new JRadioButton("时间");
        JRadioButton walkingButton = new JRadioButton("步行");
        JRadioButton bikingButton = new JRadioButton("自行车");
        JRadioButton drivingButton = new JRadioButton("驾车");

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

        // 创建一个面板并添加组件
        JPanel panel = new JPanel(new GridLayout(0, 1));

        // 创建单选按钮的面板
        JPanel sortingPanel = new JPanel();
        sortingPanel.add(new JLabel("排序方式:"));
        sortingPanel.add(lengthButton);
        sortingPanel.add(timeButton);

        JPanel transportationPanel = new JPanel();
        transportationPanel.add(new JLabel("交通方式:"));
        transportationPanel.add(walkingButton);
        transportationPanel.add(bikingButton);
        transportationPanel.add(drivingButton);

        // 将单选按钮面板添加到主面板
        panel.add(sortingPanel);
        panel.add(transportationPanel);


        // 创建输入框
        startField = new JTextField(20);
        endField = new JTextField(20);
        JButton submitButton = new JButton("提交");

        JPanel startPanel = new JPanel();
        startPanel.add(new JLabel("起点:"));
        startPanel.add(startField);

        JPanel endPanel = new JPanel();
        endPanel.add(new JLabel("终点:"));
        endPanel.add(endField);

        panel.add(startPanel);
        panel.add(endPanel);

        // 创建一个带有居中对齐的提交按钮的面板
        JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        submitPanel.add(submitButton);
        panel.add(submitPanel);

        // 将面板添加到窗口中
        add(panel, BorderLayout.CENTER);

        // 为提交按钮添加动作监听器
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
                JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "起点不存在");
            } else if (endId == -1) {
                JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "终点不存在");
            } else {
                // 调用路线规划系统的方法，获取路径，并在窗口绘画路径
                int[] path = new int[1000];
                if (sorting.equals("length")) {
                    double length = routePlanningSystem.dijkstra(startId, endId, transportation,false, path);
                    if (length == -1) {
                        JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "无法找到路径");
                        return;
                    }
                    JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "最短路径长度为: " + length);
                } else if (sorting.equals("time")) {
                    double time = routePlanningSystem.dijkstra(startId, endId, transportation, true, path);
                    if (time == -1) {
                        JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "无法找到路径");
                        return;
                    }
                    JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "最短路径时间为: " + time);
                }
                System.out.println("path: ");
                for(int i = 0; i < path.length; i++){
                    if(path[i] == -1){
                        break;
                    }
                    System.out.print(path[i] + " ");
                }
                //画出路径，点的位置在routePlanningSystem.point[path[i]]中，两点之间画线，背景图为map.jpg
                SwingUtilities.invokeLater(() -> {
                    PathLoader loader = new PathLoader(path, routePlanningSystem.point);
                    loader.setVisible(true);
                    loader.loadImage("map.jpg"); // 将 "map.jpg" 替换为实际的图片路径
                });
            }
        });
    }

}

// 多点路线输入窗口,代码逻辑与单点路线输入窗口类似，只是多了一个终点输入框，以及多个终点的处理，其余逻辑相同，不再赘述
class MultiPointRouteInputWindow extends JFrame{
    private final JTextField startField;
    private final JTextField endsField;

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
        JButton submitButton = new JButton("提交");



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
                double length = routePlanningSystem.tsp(startId, endArray, transportation, false,path);
                if(length == -1){
                    JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "无法找到路径");
                    return;
                }
                JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "最短路径长度为: " + length);
            } else if (sorting.equals("time")) {
                double time = routePlanningSystem.tsp(startId, endArray, transportation,true, path);
                if(time == -1){
                    JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "无法找到路径");
                    return;
                }
                JOptionPane.showMessageDialog(MultiPointRouteInputWindow.this, "最短路径时间为: " + time);
            }
            //画出路径，点的位置在routePlanningSystem.point[path[i]]中，两点之间画线，背景图为map.jpg
            SwingUtilities.invokeLater(() -> {
                var loader = new PathLoader(path, routePlanningSystem.point);
                loader.setVisible(true);
                loader.loadImage("map.jpg"); // 将 "map.jpg" 替换为实际的图片路径
            });
        });
    }
}

// 路径加载器，用于显示路径
class PathLoader extends JFrame {
    private ImageIcon imageIcon; // 用于存储图像的图标
    private final JLabel label; // 用于显示图像的标签
    private int imageWidth; // 存储原始图像宽度
    private int imageHeight; // 存储原始图像高度

    // 构造函数，接收路径和点的数组
    public PathLoader(int[] path, MyPoint[] point) {

        setTitle("Image Loader"); // 设置窗口标题
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 设置窗口关闭时的操作

        // 创建一个标签，重写其paintComponent方法以在其上绘制路径
        label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED); // 设置绘制颜色为红色

                // 遍历路径中的每一对点，绘制一条从第一个点到第二个点的线
                for (int i = 0; i < path.length - 1; i++) {
                    int x1 = (int) (point[path[i]].x * getWidth() / (double) imageWidth);
                    int y1 = (int) (point[path[i]].y * getHeight() / (double) imageHeight);
                    int x2 = (int) (point[path[i + 1]].x * getWidth() / (double) imageWidth);
                    int y2 = (int) (point[path[i + 1]].y * getHeight() / (double) imageHeight);
                    g.drawLine(x1, y1, x2, y2); // 绘制线
                }
            }
        };

        getContentPane().add(label, BorderLayout.CENTER); // 将标签添加到窗口的中心位置

        // 添加一个组件监听器，当窗口大小改变时，调整图像的大小
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeImage();
            }
        });

        setSize(400, 600); // 设置窗口的初始大小
        setLocationRelativeTo(null); // 将窗口位置设置在屏幕中央
    }

    // 加载图像, imagePath为背景图像的路径
    public void loadImage(String imagePath) {
        imageIcon = new ImageIcon(imagePath); // 加载图像
        imageWidth = imageIcon.getIconWidth(); // 存储原始图像的宽度
        imageHeight = imageIcon.getIconHeight(); // 存储原始图像的高度
        label.setIcon(imageIcon); // 将图像设置到标签中
        pack(); // 调整窗口大小以适应标签的首选大小
    }

    // 调整图像的大小以适应标签的大小
    private void resizeImage() {
        if (imageIcon != null) { // 如果图像已经加载
            Dimension size = label.getSize(); // 获取标签的当前大小
            // 将图像缩放以适应标签的大小
            Image scaledImage = imageIcon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(scaledImage); // 创建一个新的图像图标
            label.setIcon(scaledImageIcon); // 将缩放后的图像设置到标签中
        }
    }
}
// 图片加载器，用于显示图片
class ImageLoader extends JFrame {

    private ImageIcon imageIcon; // 用于存储图像的图标
    private final JLabel label; // 用于显示图像的标签

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

        setSize(400, +00); // 设置窗口默认大小为800x1200
        setLocationRelativeTo(null); // 将窗口置于屏幕中央
    }

   //加载并显示图片,imagePath 图片的路径
    public void loadImage(String imagePath) {
        // 加载图片
        imageIcon = new ImageIcon(imagePath);
        label.setIcon(imageIcon); // 将图片设置到标签中
        resizeImage(); // 初始时调整图片大小
        pack(); // 调整窗口大小以适应图片大小
    }

    //调整图片大小以适应标签大小
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
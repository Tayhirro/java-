import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// 主窗口
public class RoutePlanningWindow extends JFrame {
    public RoutePlanningWindow() {
        setTitle("路线规划");
        setSize(320, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create buttons
        JButton viewMapButton = new JButton("查看地图");
        JButton routePlanningButton = new JButton("单点路线规划");
        JButton multiPointRoutePlanningButton = new JButton("多点路线规划");

        // Add an action listener to the viewMapButton
        // When the button is clicked, a new ImageLoader will be created
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

// 单点路线输入窗口
class SinglePointRouteInputWindow extends JFrame {
    private JTextField startField;
    private JTextField endField;
    private JButton submitButton;

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
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    path[0] = startId;
                    path[1] = endId;
                    if (sorting.equals("length")) {
                        double length = routePlanningSystem.dijkstraLength(startId, endId, transportation, path);

                        JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "最短路径长度为: " + length);
                    } else if (sorting.equals("time")) {
                        double time = routePlanningSystem.dijkstraTime(startId, endId, transportation, path);

                        JOptionPane.showMessageDialog(SinglePointRouteInputWindow.this, "最短路径时间为: " + time);
                    }
                    //画出路径，点的位置在routePlanningSystem.points[path[i]]中，两点之间画线，背景图为map.jpg
                    SwingUtilities.invokeLater(() -> {
                        PathLoader loader = new PathLoader(path, routePlanningSystem.points);
                        loader.setVisible(true);
                        loader.loadImage("map.jpg"); // 将 "map.jpg" 替换为实际的图片路径
                    });

                }
            }
        });

    }

}

// 多点路线输入窗口
class MultiPointRouteInputWindow extends JFrame{

}


// 路径加载器，用于显示路径
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

// 图片加载器，用于显示图片
class ImageLoader extends JFrame {

    private ImageIcon imageIcon;
    private JLabel label;



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
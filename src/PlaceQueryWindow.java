import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// 场所查询模块
public class PlaceQueryWindow extends JFrame {

    public PlaceQueryWindow() {
        setTitle("场所查询");
        setSize(200, 160);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JButton nearbyPlacesButton = new JButton("附近场所");
        JButton placeQueryButton = new JButton("场所查询");

        // 创建面板并设置布局
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 添加附近场所按钮
        panel.add(nearbyPlacesButton, gbc);

        // 添加场所查询按钮
        gbc.gridy++;
        panel.add(placeQueryButton, gbc);

        add(panel); // 将面板添加到窗口中

        // 为按钮添加 ActionListener
        nearbyPlacesButton.addActionListener(_ -> new NearbyPlacesFieldWindow().setVisible(true));

        placeQueryButton.addActionListener(_ -> new PlaceSearchFieldWindow().setVisible(true));


    }

    //附件场所
    static class NearbyPlacesFieldWindow extends JFrame {
        private final JTextField positionField;
        private String position;
        public NearbyPlacesFieldWindow() {

            setTitle("附件场所");
            setSize(300, 150);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            // Create UI components
            positionField = new JTextField(20);
            JButton submitButton = new JButton("提交");

            JRadioButton straightDistanceRadioButton = new JRadioButton("直线距离");
            JRadioButton pathDistanceRadioButton = new JRadioButton("路径距离");
            ButtonGroup distanceGroup = new ButtonGroup();
            distanceGroup.add(straightDistanceRadioButton);
            distanceGroup.add(pathDistanceRadioButton);
            straightDistanceRadioButton.setSelected(true); // 默认选择直线距离

            JPanel radioPanel = new JPanel();
            radioPanel.add(straightDistanceRadioButton);
            radioPanel.add(pathDistanceRadioButton);

            JPanel positionPanel = new JPanel();
            positionPanel.add(new JLabel("当前位置:"));
            positionPanel.add(positionField);

            JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            submitPanel.add(submitButton);

            setLayout(new GridLayout(3, 1)); // 设置网格布局，3行1列
            add(radioPanel);
            add(positionPanel);
            add(submitPanel);

        // Add action listener to the submit button
            submitButton.addActionListener(_ -> {
                position = positionField.getText();
                System.out.println("当前位置： "+position);
                if(position.isEmpty()){
                    JOptionPane.showMessageDialog(null, "请输入当前位置");
                    return;
                }
                PlaceQuerySystem placeQuerySystem = new PlaceQuerySystem();
                int positionId = placeQuerySystem.getPointId(position);
                if(positionId == -1) {
                    JOptionPane.showMessageDialog(null, "当前位置不存在");
                    return;
                }
                String[] places = new String[1000];
                double[] distances = new double[1000];
                int num;
                if(straightDistanceRadioButton.isSelected()) {
                    System.out.println("直线距离");
                    num = placeQuerySystem.PlaceQuery(positionId, "", true, places, distances);
                }else {
                    System.out.println("路径距离");
                    num = placeQuerySystem.PlaceQuery(positionId, "", false, places, distances);
                }
                // Test data
                String[] places3 = {
                        "Place A", "Place B", "Place C", "Place D", "Place E",
                        "Place F", "Place G", "Place H", "Place I", "Place J",
                        "Place K", "Place L", "Place M", "Place N", "Place O",
                        "Place A", "Place B", "Place C", "Place D", "Place E",
                        "Place F", "Place G", "Place H", "Place I", "Place J",
                        "Place K", "Place L", "Place M", "Place N", "Place O"
                };

                double[] distances3 = {
                        100.5, 200.7, 150.2, 300.1, 250.8,
                        130.3, 220.6, 180.9, 270.4, 320.0,
                        140.6, 210.9, 190.2, 280.5, 330.7,
                        100.5, 200.7, 150.2, 300.1, 250.8,
                        130.3, 220.6, 180.9, 270.4, 320.0,
                        140.6, 210.9, 190.2, 280.5, 330.7
                };

                PlaceDistanceWindow placeDistanceWindow = new PlaceDistanceWindow(places3, distances3,num);
                placeDistanceWindow.setVisible(true);
            });
        }

    }

    //场所查询
    static class PlaceSearchFieldWindow  extends JFrame {
        private final JTextField positionField;
        private final JTextField placeSearchField;
        private final JRadioButton straightDistanceRadioButton;

        private String position;

        public PlaceSearchFieldWindow() {

            setTitle("场所查询");
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            // Create UI components
            positionField = new JTextField(20);
            placeSearchField = new JTextField(20);
            JButton submitButton = new JButton("查询");

            straightDistanceRadioButton = new JRadioButton("直线距离");
            JRadioButton pathDistanceRadioButton = new JRadioButton("路径距离");
            ButtonGroup distanceGroup = new ButtonGroup();
            distanceGroup.add(straightDistanceRadioButton);
            distanceGroup.add(pathDistanceRadioButton);
            straightDistanceRadioButton.setSelected(true); // 默认选择直线距离

            JPanel radioPanel = new JPanel();
            radioPanel.add(straightDistanceRadioButton);
            radioPanel.add(pathDistanceRadioButton);

            JPanel positionPanel = new JPanel();
            positionPanel.add(new JLabel("当前位置:"));
            positionPanel.add(positionField);

            JPanel placeSearchPanel = new JPanel();
            placeSearchPanel.add(new JLabel("查询场所:"));
            placeSearchPanel.add(placeSearchField);

            JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            submitPanel.add(submitButton);

            setLayout(new GridLayout(4, 1)); // 设置网格布局，3行1列
            add(radioPanel);
            add(positionPanel);
            add(placeSearchPanel);
            add(submitPanel);


            // Add action listener to the submit button
            submitButton.addActionListener(_ -> {
                position = positionField.getText();
                System.out.println("当前位置： " + position);
                String place = placeSearchField.getText();
                System.out.println("查询场所： " + place);
                if(position.isEmpty()){
                    JOptionPane.showMessageDialog(null, "请输入当前位置");
                    return;
                }
                if(place.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "请输入查询场所");
                    return;
                }
                PlaceQuerySystem placeQuerySystem = new PlaceQuerySystem();
                int positionId = placeQuerySystem.getPointId(position);
                if(positionId == -1) {
                    JOptionPane.showMessageDialog(null, "当前位置不存在");
                    return;
                }
                String[] places = new String[1000];
                double[] distances = new double[1000];
                int num;
                if(straightDistanceRadioButton.isSelected()) {
                    System.out.println("直线距离");
                    num = placeQuerySystem.PlaceQuery(positionId, place, true, places, distances);
                }else {
                    System.out.println("路径距离");
                    num = placeQuerySystem.PlaceQuery(positionId, place, false, places, distances);
                }

                PlaceDistanceWindow placeDistanceWindow = new PlaceDistanceWindow(places, distances,num);
                placeDistanceWindow.setVisible(true);
            });
        }
    }

}


class PlaceDistanceWindow extends JFrame {

    public PlaceDistanceWindow(String[] places, double[] distances,int num) {

        // 设置窗口标题
        setTitle("场所距离");
        // 设置窗口大小
        setSize(400, 300);
        // 设置窗口关闭行为
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 将窗口定位于屏幕中央
        setLocationRelativeTo(null);

        // 创建表格模型，包含两列：场所和距离
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("场所");
        tableModel.addColumn("距离/m");

        // 将数据添加到表格模型中
        for (int i = 0; i < num; i++) {
            tableModel.addRow(new Object[]{places[i], distances[i]});
        }

        // 创建包含指定表格模型的 JTable
        JTable table = new JTable(tableModel);

        // 设置首选列宽度
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);

        // 设置表格字体大小
        Font tableFont = table.getFont();
        table.setFont(new Font(tableFont.getName(), Font.PLAIN, 14));

        // 设置自动调整列宽以填充所有列
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // 创建表格的滚动面板
        JScrollPane scrollPane = new JScrollPane(table);

        // 使用 GridBagLayout 设置内容面板
        JPanel contentPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        contentPane.add(scrollPane, gbc);

        // 将内容面板添加到窗口中
        setContentPane(contentPane);

    }

}

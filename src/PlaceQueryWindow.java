import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// 场所查询模块
public class PlaceQueryWindow extends JFrame {

    public PlaceQueryWindow() {
        setTitle("场所查询"); // 设置窗口标题
        setSize(200, 160); // 设置窗口大小
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 设置窗口关闭时的操作
        setLocationRelativeTo(null); // 将窗口置于屏幕中央

        JButton nearbyPlacesButton = new JButton("附近场所"); // 创建"附近场所"按钮
        JButton placeQueryButton = new JButton("场所查询"); // 创建"场所查询"按钮

        // 创建面板并设置布局
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 添加附近场所按钮到面板
        panel.add(nearbyPlacesButton, gbc);

        // 添加场所查询按钮到面板
        gbc.gridy++;
        panel.add(placeQueryButton, gbc);

        add(panel); // 将面板添加到窗口中

        // 为按钮添加 ActionListener
        nearbyPlacesButton.addActionListener(_ -> new NearbyPlacesFieldWindow().setVisible(true));
        placeQueryButton.addActionListener(_ -> new PlaceSearchFieldWindow().setVisible(true));
    }

    // 附近场所窗口
    static class NearbyPlacesFieldWindow extends JFrame {
        private final JTextField positionField; // 位置输入框
        private String position; // 位置字符串

        public NearbyPlacesFieldWindow() {
            setTitle("附件场所"); // 设置窗口标题
            setSize(300, 150); // 设置窗口大小
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 设置窗口关闭时的操作
            setLocationRelativeTo(null); // 将窗口置于屏幕中央

            // 创建UI组件
            positionField = new JTextField(20); // 创建位置输入框
            JButton submitButton = new JButton("提交"); // 创建提交按钮

            JRadioButton straightDistanceRadioButton = new JRadioButton("直线距离"); // 创建直线距离单选按钮
            JRadioButton pathDistanceRadioButton = new JRadioButton("路径距离"); // 创建路径距离单选按钮
            ButtonGroup distanceGroup = new ButtonGroup(); // 创建单选按钮组
            distanceGroup.add(straightDistanceRadioButton); // 将直线距离单选按钮添加到单选按钮组
            distanceGroup.add(pathDistanceRadioButton); // 将路径距离单选按钮添加到单选按钮组
            straightDistanceRadioButton.setSelected(true); // 默认选择直线距离

            JPanel radioPanel = new JPanel(); // 创建单选按钮面板
            radioPanel.add(straightDistanceRadioButton); // 将直线距离单选按钮添加到单选按钮面板
            radioPanel.add(pathDistanceRadioButton); // 将路径距离单选按钮添加到单选按钮面板

            JPanel positionPanel = new JPanel(); // 创建位置面板
            positionPanel.add(new JLabel("当前位置:")); // 将标签添加到位置面板
            positionPanel.add(positionField); // 将位置输入框添加到位置面板

            JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 创建提交面板
            submitPanel.add(submitButton); // 将提交按钮添加到提交面板

            setLayout(new GridLayout(3, 1)); // 设置网格布局，3行1列
            add(radioPanel); // 将单选按钮面板添加到窗口
            add(positionPanel); // 将位置面板添加到窗口
            add(submitPanel); // 将提交面板添加到窗口

            // 为提交按钮添加动作监听器
            submitButton.addActionListener(_ -> {
                position = positionField.getText(); // 获取位置输入框的文本
                System.out.println("当前位置： "+position);
                if(position.isEmpty()){
                    JOptionPane.showMessageDialog(null, "请输入当前位置"); // 如果位置为空，则弹出提示框
                    return;
                }
                PlaceQuerySystem placeQuerySystem = new PlaceQuerySystem(); // 创建场所查询系统对象
                int positionId = placeQuerySystem.getPointId(position); // 获取位置ID
                if(positionId == -1) {
                    JOptionPane.showMessageDialog(null, "当前位置不存在"); // 如果位置不存在，则弹出提示框
                    return;
                }
                String[] places = new String[1000]; // 创建场所数组
                double[] distances = new double[1000]; // 创建距离数组
                int num;
                if(straightDistanceRadioButton.isSelected()) { // 如果选择了直线距离
                    System.out.println("直线距离");
                    num = placeQuerySystem.PlaceQuery(positionId, "", true, places, distances); // 查询附近场所
                }else { // 如果选择了路径距离
                    System.out.println("路径距离");
                    num = placeQuerySystem.PlaceQuery(positionId, "", false, places, distances); // 查询附近场所
                }

                PlaceDistanceWindow placeDistanceWindow = new PlaceDistanceWindow(places, distances,num); // 创建场所距离窗口
                placeDistanceWindow.setVisible(true); // 显示场所距离窗口
            });
        }

    }

    // 场所查询窗口
    static class PlaceSearchFieldWindow  extends JFrame {
        private final JTextField positionField; // 位置输入框
        private final JTextField placeSearchField; // 场所搜索输入框
        private final JRadioButton straightDistanceRadioButton; // 直线距离单选按钮

        private String position; // 位置字符串

        public PlaceSearchFieldWindow() {

            setTitle("场所查询"); // 设置窗口标题
            setSize(300, 200); // 设置窗口大小
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 设置窗口关闭时的操作
            setLocationRelativeTo(null); // 将窗口置于屏幕中央

            // 创建UI组件
            positionField = new JTextField(20); // 创建位置输入框
            placeSearchField = new JTextField(20); // 创建场所搜索输入框
            JButton submitButton = new JButton("查询"); // 创建查询按钮

            straightDistanceRadioButton = new JRadioButton("直线距离"); // 创建直线距离单选按钮
            JRadioButton pathDistanceRadioButton = new JRadioButton("路径距离"); // 创建路径距离单选按钮
            ButtonGroup distanceGroup = new ButtonGroup(); // 创建单选按钮组
            distanceGroup.add(straightDistanceRadioButton); // 将直线距离单选按钮添加到单选按钮组
            distanceGroup.add(pathDistanceRadioButton); // 将路径距离单选按钮添加到单选按钮组
            straightDistanceRadioButton.setSelected(true); // 默认选择直线距离

            JPanel radioPanel = new JPanel(); // 创建单选按钮面板
            radioPanel.add(straightDistanceRadioButton); // 将直线距离单选按钮添加到单选按钮面板
            radioPanel.add(pathDistanceRadioButton); // 将路径距离单选按钮添加到单选按钮面板

            JPanel positionPanel = new JPanel(); // 创建位置面板
            positionPanel.add(new JLabel("当前位置:")); // 将标签添加到位置面板
            positionPanel.add(positionField); // 将位置输入框添加到位置面板

            JPanel placeSearchPanel = new JPanel(); // 创建场所搜索面板
            // 创建场所搜索面板并添加组件
            placeSearchPanel.add(new JLabel("查询场所:")); // 添加标签
            placeSearchPanel.add(placeSearchField); // 添加场所搜索输入框

            // 创建提交面板并设置布局为居中
            JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            submitPanel.add(submitButton); // 添加提交按钮到面板

            // 设置布局为4行1列的网格布局，并添加组件
            setLayout(new GridLayout(4, 1));
            add(radioPanel); // 添加单选按钮面板
            add(positionPanel); // 添加位置面板
            add(placeSearchPanel); // 添加场所搜索面板
            add(submitPanel); // 添加提交面板

            // 为提交按钮添加动作监听器
            submitButton.addActionListener(_ -> {
                position = positionField.getText(); // 获取位置输入框的文本
                System.out.println("当前位置： " + position);
                String place = placeSearchField.getText(); // 获取场所搜索输入框的文本
                System.out.println("查询场所： " + place);
                if(position.isEmpty()){
                    JOptionPane.showMessageDialog(null, "请输入当前位置"); // 如果位置为空，则弹出提示框
                    return;
                }
                if(place.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "请输入查询场所"); // 如果场所为空，则弹出提示框
                    return;
                }
                PlaceQuerySystem placeQuerySystem = new PlaceQuerySystem(); // 创建场所查询系统对象
                int positionId = placeQuerySystem.getPointId(position); // 获取位置ID
                if(positionId == -1) {
                    JOptionPane.showMessageDialog(null, "当前位置不存在"); // 如果位置不存在，则弹出提示框
                    return;
                }
                String[] places = new String[1000]; // 创建场所数组
                double[] distances = new double[1000]; // 创建距离数组
                int num;
                if(straightDistanceRadioButton.isSelected()) { // 如果选择了直线距离
                    System.out.println("直线距离");
                    num = placeQuerySystem.PlaceQuery(positionId, place, true, places, distances); // 查询场所
                }else { // 如果选择了路径距离
                    System.out.println("路径距离");
                    num = placeQuerySystem.PlaceQuery(positionId, place, false, places, distances); // 查询场所
                }

                PlaceDistanceWindow placeDistanceWindow = new PlaceDistanceWindow(places, distances,num); // 创建场所距离窗口
                placeDistanceWindow.setVisible(true); // 显示场所距离窗口
            });
        }
    }

}

// 场所距离窗口
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
            tableModel.addRow(new Object[]{places[i], distances[i]}); // 添加行
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
        gbc.fill = GridBagConstraints.BOTH; // 设置填充方式
        gbc.weightx = 1.0; // 设置水平权重
        gbc.weighty = 1.0; // 设置垂直权重
        contentPane.add(scrollPane, gbc); // 添加滚动面板到内容面板

        // 将内容面板添加到窗口中
        setContentPane(contentPane);

    }

}
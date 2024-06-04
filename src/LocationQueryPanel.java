
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LocationQueryPanel extends JPanel {

    private JTextField locationField; // 所在景区输入框
    private JTextField userLocationField; // 用户位置输入框
    private JComboBox<String> rangeComboBox; // 范围下拉框
    private JComboBox<String> categoryComboBox; // 类别下拉框 
    private JTextField searchField; // 查询文本框
    private JButton searchButton; // 查询按钮

    // 查询数据
    private TablePanel displayPanel;
    public String[] locations;
    public String[] type;
    public double[] distances;
    private int num;

    // 后端数据
    private User currUser;
    private UserManagement userManagement;
    private SpotManagement spotManagement;
    LocationQuerySystem locationQuerySystem = new LocationQuerySystem();

    public LocationQueryPanel(User currUser, UserManagement userManagement, SpotManagement spotManagement) {
        this.currUser = currUser;
        this.userManagement = userManagement;
        this.spotManagement = spotManagement;

        locations = new String[1000];
        type = new String[1000];
        distances = new double[1000];
        // (1) 展示景区或者学校内部设施数量
        num = locationQuerySystem.initData(locations, type, distances);

        // 所在景区输入框
        JPanel regionPanel = new JPanel(new BorderLayout());
        regionPanel.setBorder(BorderFactory.createTitledBorder("所在景区"));
        locationField = new JTextField();
        regionPanel.add(locationField, BorderLayout.CENTER);

        // 用户位置输入框
        JPanel locationPanel = new JPanel(new BorderLayout());
        locationPanel.setBorder(BorderFactory.createTitledBorder("当前位置"));
        userLocationField = new JTextField();
        locationPanel.add(userLocationField, BorderLayout.CENTER);

        // (2) 范围排序功能
        JPanel rangePanel = new JPanel(new BorderLayout());
        rangePanel.setBorder(BorderFactory.createTitledBorder("距离"));
        rangeComboBox = new JComboBox<>(new String[]{"不限", "100米", "200米", "500米", "1000米", "2000米"});
        rangePanel.add(rangeComboBox, BorderLayout.CENTER);

        // (3) 类别过滤
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("类别"));
        categoryComboBox = new JComboBox<>(new String[]{"所有", "生活设施", "教学楼", "公寓楼", "食堂", "体育场所"});
        filterPanel.add(categoryComboBox, BorderLayout.CENTER);

        // (4) 名称查询
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("名称,可不填"));
        searchField = new JTextField();
        searchPanel.add(searchField, BorderLayout.CENTER);

        // 查询按钮
        JPanel buttonPanel = new JPanel(new BorderLayout());
        searchButton = new JButton("查询");
        buttonPanel.add(searchButton, BorderLayout.CENTER);

        // 左侧上部分布局
        JPanel sidebarPanel = new JPanel(new BorderLayout());
        JPanel siderbartopPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;

        gbc.gridy = 0;
        siderbartopPanel.add(regionPanel, gbc);

        gbc.gridy = 1;
        siderbartopPanel.add(locationPanel, gbc);

        gbc.gridy = 2;
        siderbartopPanel.add(rangePanel, gbc);

        gbc.gridy = 3;
        siderbartopPanel.add(filterPanel, gbc);

        gbc.gridy = 4;
        siderbartopPanel.add(searchPanel, gbc);

        gbc.gridy = 5;
        siderbartopPanel.add(buttonPanel, gbc);

        sidebarPanel.add(siderbartopPanel, BorderLayout.NORTH);

        TitlePanel titlePanel = new TitlePanel("设施查询");

        displayPanel = new TablePanel(locations, type, distances, num);

        // 使用 BorderLayout 设置内容面板
        setLayout(new BorderLayout());

        add(titlePanel, BorderLayout.NORTH); // 添加标题面板到内容面板
        add(sidebarPanel, BorderLayout.WEST); // 添加侧边栏到内容面板
        add(displayPanel, BorderLayout.CENTER); // 添加显示面板到内容面板

        // 添加事件监听器（示例，实际功能需要后端支持）
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatelist();
            }
        });
    }

    private void updatelist() {
        String query = searchField.getText();
        String userLocation = userLocationField.getText();
        String location = locationField.getText();
        String category = (String) categoryComboBox.getSelectedItem();
        String range = (String) rangeComboBox.getSelectedItem();
        // 调用后端接口进行查询，并更新listModel

        System.out.println("所在景区: " + location);
        System.out.println("用户位置: " + userLocation);
        System.out.println("范围: " + range);
        System.out.println("类别: " + category);
        System.out.println("查询场所: " + query);
        // 更新数据
        if (!spotManagement.findSpot(location)) {
            JOptionPane.showMessageDialog(null, "景区不存在", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int positionId = locationQuerySystem.getPointId(userLocation); // 获取位置ID
        if (positionId == -1) {
            if (!userLocation.equals("") && userLocation != null) {
                JOptionPane.showMessageDialog(null, "当前位置:" + userLocation + "不存在", "错误", JOptionPane.ERROR_MESSAGE); // 如果位置不存在，则弹出提示框
                return;
            } else {
                num = locationQuerySystem.LocationQuery(query, category, locations, type, distances); // 查询附近场所

            }

        } // 所在地
        else {
            num = locationQuerySystem.LocationQuery(positionId, query, range, category, locations, type, distances); // 查询附近场所
        }
        // 更新显示
        displayPanel.update(locations, type, distances, num);

    }

}

class TablePanel extends JPanel {

    DefaultTableModel tableModel;

    public TablePanel(String[] locations, String[] type, double[] distances, int num) {

        // 创建表格模型，包含三列：场所、类别和距离
        tableModel = new DefaultTableModel();
        tableModel.addColumn("场所");
        tableModel.addColumn("类别");
        tableModel.addColumn("距离/m");

        // 将数据添加到表格模型中
        for (int i = 0; i < num; i++) {
            tableModel.addRow(new Object[]{locations[i], type[i], distances[i]}); // 添加行
        }

        // 创建包含指定表格模型的 JTable
        JTable table = new JTable(tableModel);

        // 设置首选列宽度
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);

        // 设置表格字体大小
        Font tableFont = table.getFont();
        table.setFont(new Font(tableFont.getName(), Font.PLAIN, 14));

        // 设置自动调整列宽以填充所有列
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        // 内容不可编辑，但可以选择
        table.setEnabled(false);

        // 创建表格的滚动面板
        JScrollPane scrollPane = new JScrollPane(table);

        // 使用 BorderLayout 设置内容面板
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("设施展示"));

        add(scrollPane, BorderLayout.CENTER); // 添加滚动面板到内容面板

    }

    public void update(String[] locations, String[] type, double[] distances, int num) {
        // 清空表格
        tableModel.setRowCount(0);
        // 将数据添加到表格模型中
        for (int i = 0; i < num; i++) {
            tableModel.addRow(new Object[]{locations[i], type[i], distances[i]}); // 添加行
        }

    }
}

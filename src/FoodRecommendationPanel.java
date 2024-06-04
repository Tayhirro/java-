
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class FoodRecommendationPanel extends JPanel {

    private final User currUser;// 当前用户
    private final UserManagement userManagement;// 用户管理对象
    private final SpotManagement spotManagement;// 景点管理对象

    private JComboBox<String> categoryComboBox;
    private JComboBox<String> canteenComboBox;
    private JTextField keywordTextField;
    private JTextField scenicSpotTextField;
    private JButton searchButton;
    private JRadioButton viewsRadioButton;
    private JRadioButton RatingRadioButton;
    private JRadioButton overallRadioButton;
    private JTable resultTable;
    private FoodManagement foodManagement;

    public FoodRecommendationPanel(User currUser, UserManagement userManagement, SpotManagement spotManagement) {
        this.currUser = currUser;
        this.userManagement = userManagement;
        this.spotManagement = spotManagement;

        setLayout(new BorderLayout());
        foodManagement = new FoodManagement();

        JPanel titlepanel = new TitlePanel("美食推荐");
        add(titlepanel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(20, 2));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel scenicSpotLabel = new JLabel("所在景区:");
        scenicSpotTextField = new JTextField();
        scenicSpotTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, scenicSpotTextField.getFont().getSize() + 5));
        searchPanel.add(scenicSpotLabel);
        searchPanel.add(scenicSpotTextField);

        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        ButtonGroup sortButtonGroup = new ButtonGroup();

        overallRadioButton = new JRadioButton("综合");
        overallRadioButton.setSelected(true);
        sortButtonGroup.add(overallRadioButton);
        searchPanel.add(overallRadioButton);

        viewsRadioButton = new JRadioButton("热度");
        sortButtonGroup.add(viewsRadioButton);
        searchPanel.add(viewsRadioButton);

        RatingRadioButton = new JRadioButton("评分");
        sortButtonGroup.add(RatingRadioButton);
        searchPanel.add(RatingRadioButton);

        searchButton = new JButton("搜索");
        searchButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        JLabel categoryLabel = new JLabel("类别:");
        categoryComboBox = new JComboBox<>(new String[]{"All", "中餐", "西餐", "日餐", "韩餐", "东南亚餐"});
        searchPanel.add(categoryLabel);
        searchPanel.add(categoryComboBox);

        JLabel canteenLabel = new JLabel("餐厅:");
        canteenComboBox = new JComboBox<>(new String[]{"All", "学生食堂", "学苑风味餐厅", "综合食堂"});
        searchPanel.add(canteenLabel);
        searchPanel.add(canteenComboBox);

        JLabel keywordLabel = new JLabel("关键字:");
        keywordTextField = new JTextField();
        keywordTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, keywordTextField.getFont().getSize() + 5));
        searchPanel.add(keywordLabel);
        searchPanel.add(keywordTextField);

        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.WEST);

        resultTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);

        displayInit();
    }

    public void displayInit() {
        List<Food> result = new ArrayList<>();
        foodManagement.findFoods("All", "All", "", result);
        foodManagement.sortbyComprehension(result);
        displayResult(result);
    }

    private void performSearch() {
        if (!spotManagement.findSpot(scenicSpotTextField.getText())) {
            JOptionPane.showMessageDialog(this, "景区不存在", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String category = (String) categoryComboBox.getSelectedItem();
        String canteen = (String) canteenComboBox.getSelectedItem();
        String keyword = keywordTextField.getText();
        String scenicSpot = scenicSpotTextField.getText();
        List<Food> result = new ArrayList<>();
        foodManagement.findFoods(category, canteen, keyword, result);

        if (viewsRadioButton.isSelected()) {
            foodManagement.sortbyViews(result);
        } else if (RatingRadioButton.isSelected()) {
            foodManagement.sortbyRating(result);
        } else if (overallRadioButton.isSelected()) {
            foodManagement.sortbyComprehension(result);
        }
        displayResult(result);
    }

    private void displayResult(List<Food> result) {
        String[] columnNames = {"名称", "类别", "评分", "热度", "餐厅", "关键字"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 所有单元格不可编辑
            }
        };

        for (Food food : result) {
            Object[] rowData = {
                food.getName(),
                food.getCategory(),
                food.getRating(),
                food.getViews(),
                food.getCanteen(),
                String.join(" ", food.getKeywords())
            };
            tableModel.addRow(rowData);
        }
        resultTable.setModel(tableModel);

        // 设置列的默认宽度并禁用用户调整列宽
        TableColumnModel columnModel = resultTable.getColumnModel();

        // 设置每列的宽度
        columnModel.getColumn(0).setPreferredWidth(100); // 名称列
        columnModel.getColumn(1).setPreferredWidth(50);  // 类别列
        columnModel.getColumn(2).setPreferredWidth(50);  // 评分列
        columnModel.getColumn(3).setPreferredWidth(50);  // 热度列
        columnModel.getColumn(4).setPreferredWidth(100); // 餐厅列
        columnModel.getColumn(5).setPreferredWidth(200); // 标签列
        // 设置列内容居中显示
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }

    }
}

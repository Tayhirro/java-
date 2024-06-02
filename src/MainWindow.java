
import java.awt.*;
import javax.swing.*;

public class MainWindow {

    private final User currUser;// 当前用户
    private final UserManagement userManagement;// 用户管理对象
    private final SpotManagement spotManagement;// 景点管理对象
    private final JFrame frame;// 主窗口
    private final JPanel mainPanel;// 主面板
    private final CardLayout cardLayout;// 卡片布局

    public MainWindow(UserManagement userManagement, User currUser) {
        frame = new JFrame("游学推荐系统");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // 使用CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 添加子页面
        mainPanel.add(createWelcomePanel(), "Welcome");
        mainPanel.add(createRecommendationPanel(), "Recommendation");
        mainPanel.add(createRoutePlanningPanel(), "RoutePlanning");
        mainPanel.add(createLocationQueryPanel(), "LocationQuery");
        mainPanel.add(createFoodRecommendationPanel(), "FoodRecommendation");
        mainPanel.add(createStudyDiaryPanel(), "StudyDiary");

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(createNavigationPanel(), BorderLayout.SOUTH);

        cardLayout.show(mainPanel, "Welcome"); // 默认显示欢迎页面

        frame.setVisible(true);

        this.userManagement = userManagement;
        this.currUser = currUser;
        this.spotManagement = new SpotManagement();
    }

    // 创建欢迎页面
    private JPanel createWelcomePanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 添加边距

        JLabel welcomeLabel = new JLabel("欢迎使用游学推荐系统", JLabel.CENTER);
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));

        JTextArea instructionArea = new JTextArea();
        instructionArea.setText("""
                        欢迎来到游学推荐系统！
                        
                        这个系统可以帮助您规划旅行路线、查询周边场所、推荐美食和记录游学日记。
                        使用下方的按钮可以切换到相应的功能页面。
                        祝您使用愉快！""");
        instructionArea.setEditable(false);
        instructionArea.setLineWrap(true);
        instructionArea.setWrapStyleWord(true);

        JPanel welcomePanel = new JPanel(new GridLayout(3, 1, 0, 10)); // 使用网格布局
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(instructionArea);

        panel.add(welcomePanel, BorderLayout.CENTER);

        return panel;
    }

    // 创建游学推荐页面，在别的类中实现
    private JPanel createRecommendationPanel() {
        RecommendationPanel recommendationPanel = new RecommendationPanel(currUser, userManagement, spotManagement);
        return recommendationPanel;
    }

    // 创建路径规划页面，在别的类中实现
    private JPanel createRoutePlanningPanel() {
        RoutePlanningPanel routePlanningPanel = new RoutePlanningPanel(currUser, userManagement, spotManagement);
        return routePlanningPanel;
    }

    // 创建场所查询页面，在别的类中实现
    private JPanel createLocationQueryPanel() {
        LocationQueryPanel locationQueryPanel = new LocationQueryPanel(currUser, userManagement, spotManagement);
        return locationQueryPanel;
    }

    // 创建美食推荐页面，在别的类中实现
    private JPanel createFoodRecommendationPanel() {
        FoodRecommendationPanel foodRecommendationPanel = new FoodRecommendationPanel(currUser, userManagement, spotManagement);
        return foodRecommendationPanel;
    }

    // 创建游学日记页面，在别的类中实现
    private JPanel createStudyDiaryPanel() {
        StudyDiaryPanel studyDiaryPanel = new StudyDiaryPanel(currUser, userManagement, spotManagement);
        return studyDiaryPanel;

    }

    // 创建导航面板,包含5个按钮
    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 0)); // 设置网格布局，并增加水平间距

        JButton recommendationButton = new JButton("游学推荐");
        recommendationButton.addActionListener(e -> cardLayout.show(mainPanel, "Recommendation")); // 使用Lambda表达式简化代码

        JButton routePlanningButton = new JButton("路径规划");
        routePlanningButton.addActionListener(e -> cardLayout.show(mainPanel, "RoutePlanning"));

        JButton locationQueryButton = new JButton("场所查询");
        locationQueryButton.addActionListener(e -> cardLayout.show(mainPanel, "LocationQuery"));

        JButton foodRecommendationButton = new JButton("美食推荐");
        foodRecommendationButton.addActionListener(e -> cardLayout.show(mainPanel, "FoodRecommendation"));

        JButton studyDiaryButton = new JButton("游学日记");
        studyDiaryButton.addActionListener(e -> cardLayout.show(mainPanel, "StudyDiary"));

        panel.add(recommendationButton);
        panel.add(routePlanningButton);
        panel.add(locationQueryButton);
        panel.add(foodRecommendationButton);
        panel.add(studyDiaryButton);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 添加边距

        return panel;
    }

}

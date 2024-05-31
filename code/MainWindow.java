import java.awt.*;
import javax.swing.*;

public class MainWindow {
    private final User currUser;// 当前用户
    private final UserManagement userManagement;// 用户管理对象
    private final JFrame frame;// 主窗口
    private final JPanel mainPanel;// 主面板
    private final CardLayout cardLayout;// 卡片布局

    public MainWindow(UserManagement userManagement, User currUser) {
        this.userManagement = userManagement;
        this.currUser = currUser;
        frame = new JFrame("游学推荐系统");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

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
    }

    // 创建欢迎页面
    private JPanel createWelcomePanel() {
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 添加边距
    
        JLabel welcomeLabel = new JLabel("欢迎使用游学推荐系统", JLabel.CENTER);
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
    
        JTextArea instructionArea = new JTextArea();
        instructionArea.setText("""
                                \u6b22\u8fce\u6765\u5230\u6e38\u5b66\u63a8\u8350\u7cfb\u7edf\uff01
                                
                                \u8fd9\u4e2a\u7cfb\u7edf\u53ef\u4ee5\u5e2e\u52a9\u60a8\u89c4\u5212\u65c5\u884c\u8def\u7ebf\u3001\u67e5\u8be2\u5468\u8fb9\u573a\u6240\u3001\u63a8\u8350\u7f8e\u98df\u548c\u8bb0\u5f55\u6e38\u5b66\u65e5\u8bb0\u3002
                                \u4f7f\u7528\u4e0b\u65b9\u7684\u6309\u94ae\u53ef\u4ee5\u5207\u6362\u5230\u76f8\u5e94\u7684\u529f\u80fd\u9875\u9762\u3002
                                \u795d\u60a8\u4f7f\u7528\u6109\u5feb\uff01""");
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
        RecommendationPanel recommendationPanel = new RecommendationPanel(currUser);
        return recommendationPanel;
    }

    // 创建路径规划页面，在别的类中实现
    private JPanel createRoutePlanningPanel() {
        RoutePlanningPanel routePlanningPanel = new RoutePlanningPanel();
        return routePlanningPanel;
    }

    // 创建场所查询页面，在别的类中实现
    private JPanel createLocationQueryPanel() {
        LocationQueryPanel locationQueryPanel = new LocationQueryPanel();
        return locationQueryPanel;
    }

    // 创建美食推荐页面，在别的类中实现
    private JPanel createFoodRecommendationPanel() {
        FoodRecommendationPanel foodRecommendationPanel = new FoodRecommendationPanel();
        return foodRecommendationPanel;
    }

    // 创建游学日记页面，在别的类中实现
    private JPanel createStudyDiaryPanel() {
        StudyDiaryPanel studyDiaryPanel = new StudyDiaryPanel(userManagement, currUser);
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

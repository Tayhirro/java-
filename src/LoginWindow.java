import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * LoginWindow 是 JFrame 的子类，代表登录窗口。
 * 它设置了窗口的标题、大小、默认关闭操作和位置。
 * 它还包含了用户名和密码的输入字段，以及登录和注册按钮。
 * 当用户点击登录按钮时，它会调用 UserManagementSystem 的登录方法。
 * 当用户点击注册按钮时，它会调用 UserManagementSystem 的注册方法。
 */
public class LoginWindow extends JFrame {
    private JTextField usernameField; // 用户名输入字段
    private JPasswordField passwordField; // 密码输入字段
    private JButton loginButton; // 登录按钮
    private JButton registerButton; // 注册按钮
    private UserManagementSystem ums; // 用户管理系统
    private RecommendationSystem recommendationSystem; // 推荐系统
    private SearchSystem searchSystem; // 搜索系统
    private List<String> userInterests; // 用户兴趣列表
    private CenterPlaceDataLoader dataLoader; // 数据加载器
    /**
     * LoginWindow 类的构造函数。
     * 它初始化窗口，设置标题、大小、默认关闭操作和位置。
     * 它还初始化了用户名和密码的输入字段，以及登录和注册按钮。
     * 它还加载了用户数据，并创建了数据加载器、推荐系统和搜索系统的实例。
     * @param ums 用户管理系统
     */
    public LoginWindow(UserManagementSystem ums) {
        this.ums = ums;
        ums.loadUsersFromFile("users.txt");

        // 创建数据加载器实例
        this.dataLoader = new CenterPlaceDataLoader("CenterPlace.txt");
        // 创建推荐系统实例
        recommendationSystem = new RecommendationSystem(dataLoader);

        searchSystem= new SearchSystem(dataLoader);

        // 用户兴趣列表
        userInterests = new ArrayList<>();

        setTitle("登录窗口"); // 设置窗口的标题
        setSize(260, 140); // 设置窗口的大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置默认关闭操作
        setLocationRelativeTo(null); // 将窗口居中

        // 创建 UI 组件
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("登录");
        registerButton = new JButton("注册");

        // 创建一个面板并添加组件
        JPanel panel = new JPanel();

        panel.add(new JLabel("用户名:"));
        panel.add(usernameField);
        panel.add(new JLabel("密  码:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        // 将面板添加到框架
        add(panel);

        // 为登录按钮添加动作监听器
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // 调用 UserManagementSystem 的登录方法
                String sessionId = ums.login(username, password);
                if (sessionId != null) {
                    System.out.println("登录成功，会话ID：" + sessionId);
                    // 关闭登录窗口并打开主窗口
                    setVisible(false);
                    dispose();
                    User user = ums.getUserByUsername(username);
                    new MainWindow(recommendationSystem, searchSystem,userInterests,dataLoader).setVisible(true);
                    showPreferenceWindow(user, ums); // 显示偏好窗口

                } else {
                    System.out.println("登录失败");
                }
            }
        });

        // 为注册按钮添加动作监听器
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog("请输入用户名");
                String password = JOptionPane.showInputDialog("请输入密码");
                String email = JOptionPane.showInputDialog("请输入邮箱");
                if(username == null || password == null || email == null) {

                    return;
                }
                if(username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "用户名、密码和邮箱不能为空");
                    return;
                }
                // 调用 UserManagementSystem 的注册方法
                boolean success = ums.register(username, password, email);
                if (success) {
                    JOptionPane.showMessageDialog(null, "注册成功");
                    ums.saveUsersToFile("users.txt"); // 保存用户信息
                } else {
                    JOptionPane.showMessageDialog(null, "注册失败，用户名已存在");
                }
            }
        });

        setVisible(true);
    }

    /**
     * 显示偏好窗口。
     * 用户可以在这个窗口中选择他们的偏好。
     * @param user 用户
     * @param ums 用户管理系统
     */
    private void showPreferenceWindow(User user, UserManagementSystem ums) {
        JFrame frame = new JFrame("选择偏好");
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null); // Center the window

        JPanel preferencePanel = new JPanel();
        preferencePanel.setLayout(new BoxLayout(preferencePanel, BoxLayout.Y_AXIS));

        JCheckBox cbSport = new JCheckBox("运动");
        JCheckBox cbRest = new JCheckBox("休息");
        JCheckBox cbEat = new JCheckBox("吃饭");
        JCheckBox cbStudy = new JCheckBox("学习");

        JButton button = new JButton("确定");
        button.addActionListener(e -> {
            if (cbSport.isSelected()) {
                user.addAdditionalInfo("preference", "运动");
                userInterests.add("运动");
            }
            if (cbRest.isSelected()) {
                user.addAdditionalInfo("preference", "休息");
                userInterests.add("休息");
            }
            if (cbEat.isSelected()) {
                user.addAdditionalInfo("preference", "吃饭");
                userInterests.add("吃饭");
            }
            if (cbStudy.isSelected()) {
                user.addAdditionalInfo("preference", "学习");
                userInterests.add("学习");
            }

            ums.saveUsersToFile("users.txt"); // Save user preferences to file

            frame.setVisible(false);
            frame.dispose();
        });

        preferencePanel.add(cbSport);   
        preferencePanel.add(cbRest);
        preferencePanel.add(cbEat);
        preferencePanel.add(cbStudy);
        preferencePanel.add(button);

        frame.add(preferencePanel);
        frame.setVisible(true);
    }
}
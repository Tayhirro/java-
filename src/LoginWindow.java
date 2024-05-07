import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private UserManagementSystem ums;
    private RecommendationSystem recommendationSystem;
    private SearchSystem searchSystem;
    private List<String> userInterests;

    public LoginWindow(UserManagementSystem ums) {
        this.ums = ums;
        ums.loadUsersFromFile("users.txt");

        // 创建数据加载器实例
        CenterPlaceDataLoader dataLoader = new CenterPlaceDataLoader("CenterPlace.txt");

        // 创建推荐系统实例
        recommendationSystem = new RecommendationSystem(dataLoader);

        searchSystem= new SearchSystem(dataLoader);

        // 用户兴趣列表
        userInterests = new ArrayList<>();

        setTitle("登录窗口");
        setSize(260, 140);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create UI components
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("登录");
        registerButton = new JButton("注册");

        // Create a panel and add the components
        JPanel panel = new JPanel();

        panel.add(new JLabel("用户名:"));
        panel.add(usernameField);
        panel.add(new JLabel("密  码:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        // Add the panel to the frame
        add(panel);

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Call the login method of UserManagementSystem
                String sessionId = ums.login(username, password);
                if (sessionId != null) {
                    System.out.println("登录成功，会话ID：" + sessionId);
                    // Close the login window and open the main window
                    setVisible(false);
                    dispose();
                    User user = ums.getUserByUsername(username);
                    new MainWindow(recommendationSystem, searchSystem,userInterests).setVisible(true);
                    showPreferenceWindow(user, ums); // Show preference window

                } else {
                    System.out.println("登录失败");
                }
            }
        });

        // Add action listener to the register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog("请输入用户名");
                String password = JOptionPane.showInputDialog("请输入密码");
                String email = JOptionPane.showInputDialog("请输入邮箱");

                // Call the register method of UserManagementSystem
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
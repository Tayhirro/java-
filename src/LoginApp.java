
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginApp {

    private final JFrame frame;// 主窗口
    private JPanel panel;// 面板
    private final CardLayout cardLayout;// 卡片布局
    private final UserManagement userManagement; // 用户管理对象,用于注册和登录,从外部传入
    private User currentUser; // 当前用户

    public LoginApp(UserManagement us) {// 构造方法
        userManagement = us; // 创建用户管理对象，加载用户数据
        frame = new JFrame("用户登录系统");// 创建窗口，设置标题，
        panel = new JPanel();// 创建面板，用于切换登录和注册
        cardLayout = new CardLayout();// 创建卡片布局，用于切换面板
        panel.setLayout(cardLayout);// 设置面板布局为卡片布局，

        panel.add(createRegisterPanel(), "Register");// 添加注册面板，命名为Register，用于切换
        panel.add(createLoginPanel(), "Login");// 添加登录面板，命名为Login，用于切换

        frame.add(panel);// 将面板添加到窗口
        frame.setSize(400, 300);// 设置窗口大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置关闭窗口时退出程序
        frame.setLocationRelativeTo(null);// 设置窗口居中
        cardLayout.show(panel, "Login"); // 默认显示登录面板
        frame.pack();// 调整窗口大小
        frame.setResizable(false);// 禁止调整窗口大小
        frame.setVisible(true);// 显示窗口
    }

    public void showWindow() {
        this.frame.setVisible(true);
    }

    // 创建注册面板
    private JPanel createRegisterPanel() {
        JPanel registerPanel = new JPanel(new BorderLayout(10, 10));
        registerPanel.setBorder(new EmptyBorder(20, 50, 20, 50)); // 设置边距

        JLabel headerLabel = new JLabel("注册页面", JLabel.CENTER);
        headerLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 1, 10, 10)); // 创建字段面板
        JPanel namePanel = new JPanel(new BorderLayout(5, 5));
        JPanel accountPanel = new JPanel(new BorderLayout(5, 5));
        JPanel passwordPanel = new JPanel(new BorderLayout(5, 5));

        Font font = new Font("微软雅黑", Font.PLAIN, 16);
        JLabel nameLabel = new JLabel("用户名：");
        nameLabel.setFont(font);
        JTextField nameField = new JTextField(15); // 创建用户名输入框
        nameField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY)); // 底部边框
        nameField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        nameField.setMargin(new Insets(5, 5, 5, 5));

        JLabel accountLabel = new JLabel("账 号：");
        accountLabel.setFont(font);
        JTextField accountField = new JTextField(15); // 创建账号输入框
        accountField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY)); // 底部边框
        accountField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        accountField.setMargin(new Insets(5, 5, 5, 5));

        JLabel passwordLabel = new JLabel("密 码：");
        passwordLabel.setFont(font);
        JPasswordField passwordField = new JPasswordField(15); // 创建密码输入框
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY)); // 底部边框
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        passwordField.setMargin(new Insets(5, 5, 5, 5));

        // 添加组件到对应的面板
        namePanel.add(nameLabel, BorderLayout.WEST);
        namePanel.add(nameField, BorderLayout.CENTER);
        accountPanel.add(accountLabel, BorderLayout.WEST);
        accountPanel.add(accountField, BorderLayout.CENTER);
        passwordPanel.add(passwordLabel, BorderLayout.WEST);
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        fieldsPanel.add(namePanel);
        fieldsPanel.add(accountPanel);
        fieldsPanel.add(passwordPanel);

        JPanel buttonsPanel = new JPanel(); // 创建按钮面板
        JButton registerButton = new JButton("注册"); // 创建注册按钮
        JButton toLoginButton = new JButton("去登录"); // 创建去登录按钮
        buttonsPanel.add(registerButton);
        buttonsPanel.add(toLoginButton);

        // 将组件添加到注册面板
        registerPanel.add(headerLabel, BorderLayout.NORTH);
        registerPanel.add(fieldsPanel, BorderLayout.CENTER);
        registerPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // 注册按钮点击事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String account = accountField.getText();
                char[] password = passwordField.getPassword();

                if (name.isEmpty() || account.isEmpty() || password.length == 0) {
                    JOptionPane.showMessageDialog(frame, "所有字段都是必填的", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (userManagement.registerUser(name, account, password)) {
                    JOptionPane.showMessageDialog(frame, "注册成功!");
                    cardLayout.show(panel, "Login"); // 切换到登录面板
                } else {
                    JOptionPane.showMessageDialog(frame, "账号已存在", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 去登录按钮点击事件
        toLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "Login"); // 切换到登录面板
            }
        });

        return registerPanel;
    }

    // 创建登录面板
    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new BorderLayout(10, 10)); // 创建登录面板,设置布局为边界布局，水平和垂直间距为10
        loginPanel.setBorder(new EmptyBorder(20, 50, 20, 50)); // 设置边距，上左下右

        JLabel headerLabel = new JLabel("登录页面", JLabel.CENTER);// 创建标题标签，居中对齐
        headerLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));// 设置字体

        JPanel fieldsPanel = new JPanel(new GridBagLayout()); // 创建字段面板
        GridBagConstraints gbc = new GridBagConstraints();// 创建网格包约束
        gbc.insets = new Insets(5, 5, 5, 5);// 设置内边距
        gbc.fill = GridBagConstraints.HORIZONTAL;// 设置填充方式

        Font font = new Font("微软雅黑", Font.PLAIN, 16);// 创建字体

        JLabel accountLabel = new JLabel("账 号：");// 创建账号标签
        accountLabel.setFont(font);
        JTextField accountField = new JTextField(15); // 创建账号输入框
        accountField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY)); // 底部边框
        accountField.setFont(new Font("微软雅黑", Font.PLAIN, 14));// 设置字体
        accountField.setMargin(new Insets(5, 5, 5, 5));// 设置边距

        JLabel passwordLabel = new JLabel("密 码：");
        passwordLabel.setFont(font);
        JPasswordField passwordField = new JPasswordField(15); // 创建密码输入框
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY)); // 底部边框
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));// 设置字体
        passwordField.setMargin(new Insets(5, 5, 5, 5));// 设置边距

        // 添加组件到字段面板
        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldsPanel.add(accountLabel, gbc);
        gbc.gridx = 1;
        fieldsPanel.add(accountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldsPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        fieldsPanel.add(passwordField, gbc);

        JPanel buttonsPanel = new JPanel();
        JButton loginButton = new JButton("登录");
        JButton toRegisterButton = new JButton("去注册");
        buttonsPanel.add(loginButton);
        buttonsPanel.add(toRegisterButton);

        loginPanel.add(headerLabel, BorderLayout.NORTH);
        loginPanel.add(fieldsPanel, BorderLayout.CENTER);
        loginPanel.add(buttonsPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(new ActionListener() {// 登录按钮点击事件
            @Override
            public void actionPerformed(ActionEvent e) {
                String account = accountField.getText();
                char[] password = passwordField.getPassword();

                if (account.isEmpty() || password.length == 0) {
                    JOptionPane.showMessageDialog(frame, "账号和密码都是必填的", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                currentUser = userManagement.login(account, password);

                if (currentUser != null) {// 登录成功

                    new MainWindow(userManagement, currentUser);
                    //关闭登录窗口
                    frame.dispose();

                } else {
                    JOptionPane.showMessageDialog(frame, "账号或密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        toRegisterButton.addActionListener(new ActionListener() {// 去注册按钮点击事件,切换到注册面板
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panel, "Register");// 切换到注册面板
            }
        });

        return loginPanel;
    }

    public User getuser() {
        return currentUser;
    }

}

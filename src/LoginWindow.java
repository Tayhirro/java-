import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private UserManagementSystem ums;

    public LoginWindow(UserManagementSystem ums) {
        this.ums = ums;

        setTitle("登录窗口");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create UI components
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("登录");
        registerButton = new JButton("注册");

        // Create a panel and add the components
        JPanel panel = new JPanel();

//        panel.setLayout(null);
//        JButton button = new JButton("按钮");
//        button.setBounds(50, 50, 80, 30); // 设置按钮的位置和大小
//        panel.add(button);

        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("用户名:"));
        panel.add(usernameField);
        panel.add(new JLabel("密码:"));
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
                } else {
                    JOptionPane.showMessageDialog(null, "注册失败，用户名已存在");
                }
            }
        });

        setVisible(true);
    }
}
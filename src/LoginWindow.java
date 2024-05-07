import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private UserManagementSystem ums;
    private RecommendationSystem recommendationSystem;
    private List<String> userInterests;

    public LoginWindow(UserManagementSystem ums) {
        this.ums = ums;
        ums.loadUsersFromFile("users.txt");

        // �������ݼ�����ʵ��
        CenterPlaceDataLoader dataLoader = new CenterPlaceDataLoader("CenterPlace.txt");

        // �����Ƽ�ϵͳʵ��
        recommendationSystem = new RecommendationSystem(dataLoader);

        // �û���Ȥ�б�
        userInterests = List.of("keyword1", "keyword3");

        setTitle("��¼����");
        setSize(320, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create UI components
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("��¼");
        registerButton = new JButton("ע��");

        // Create a panel and add the components
        JPanel panel = new JPanel();

//        panel.setLayout(null);
//        JButton button = new JButton("��ť");
//        button.setBounds(50, 50, 80, 30); // ���ð�ť��λ�úʹ�С
//        panel.add(button);

        //panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("�û���:"));
        panel.add(usernameField);
        panel.add(new JLabel("����:"));
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
                    System.out.println("��¼�ɹ����ỰID��" + sessionId);
                    // Close the login window and open the main window
                    setVisible(false);
                    dispose();
                    new MainWindow(recommendationSystem, userInterests).setVisible(true);
                } else {
                    System.out.println("��¼ʧ��");
                }
            }
        });

        // Add action listener to the register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog("�������û���");
                String password = JOptionPane.showInputDialog("����������");
                String email = JOptionPane.showInputDialog("����������");

                // Call the register method of UserManagementSystem
                boolean success = ums.register(username, password, email);
                if (success) {
                    JOptionPane.showMessageDialog(null, "ע��ɹ�");
                    ums.saveUsersToFile("users.txt"); // �����û���Ϣ
                } else {
                    JOptionPane.showMessageDialog(null, "ע��ʧ�ܣ��û����Ѵ���");
                }
            }
        });

        setVisible(true);
    }
}
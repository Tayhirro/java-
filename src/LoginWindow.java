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

        // �������ݼ�����ʵ��
        CenterPlaceDataLoader dataLoader = new CenterPlaceDataLoader("CenterPlace.txt");

        // �����Ƽ�ϵͳʵ��
        recommendationSystem = new RecommendationSystem(dataLoader);

        searchSystem= new SearchSystem(dataLoader);

        // �û���Ȥ�б�
        userInterests = new ArrayList<>();

        setTitle("��¼����");
        setSize(260, 140);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create UI components
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("��¼");
        registerButton = new JButton("ע��");

        // Create a panel and add the components
        JPanel panel = new JPanel();

        panel.add(new JLabel("�û���:"));
        panel.add(usernameField);
        panel.add(new JLabel("��  ��:"));
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
                    User user = ums.getUserByUsername(username);
                    new MainWindow(recommendationSystem, searchSystem,userInterests).setVisible(true);
                    showPreferenceWindow(user, ums); // Show preference window

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

    private void showPreferenceWindow(User user, UserManagementSystem ums) {
        JFrame frame = new JFrame("ѡ��ƫ��");
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null); // Center the window

        JPanel preferencePanel = new JPanel();
        preferencePanel.setLayout(new BoxLayout(preferencePanel, BoxLayout.Y_AXIS));

        JCheckBox cbSport = new JCheckBox("�˶�");
        JCheckBox cbRest = new JCheckBox("��Ϣ");
        JCheckBox cbEat = new JCheckBox("�Է�");
        JCheckBox cbStudy = new JCheckBox("ѧϰ");

        JButton button = new JButton("ȷ��");
        button.addActionListener(e -> {
            if (cbSport.isSelected()) {
                user.addAdditionalInfo("preference", "�˶�");
                userInterests.add("�˶�");
            }
            if (cbRest.isSelected()) {
                user.addAdditionalInfo("preference", "��Ϣ");
                userInterests.add("��Ϣ");
            }
            if (cbEat.isSelected()) {
                user.addAdditionalInfo("preference", "�Է�");
                userInterests.add("�Է�");
            }
            if (cbStudy.isSelected()) {
                user.addAdditionalInfo("preference", "ѧϰ");
                userInterests.add("ѧϰ");
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
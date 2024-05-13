import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * LoginWindow �� JFrame �����࣬�����¼���ڡ�
 * �������˴��ڵı��⡢��С��Ĭ�Ϲرղ�����λ�á�
 * �����������û���������������ֶΣ��Լ���¼��ע�ᰴť��
 * ���û������¼��ťʱ��������� UserManagementSystem �ĵ�¼������
 * ���û����ע�ᰴťʱ��������� UserManagementSystem ��ע�᷽����
 */
public class LoginWindow extends JFrame {
    private JTextField usernameField; // �û��������ֶ�
    private JPasswordField passwordField; // ���������ֶ�
    private JButton loginButton; // ��¼��ť
    private JButton registerButton; // ע�ᰴť
    private UserManagementSystem ums; // �û�����ϵͳ
    private RecommendationSystem recommendationSystem; // �Ƽ�ϵͳ
    private SearchSystem searchSystem; // ����ϵͳ
    private List<String> userInterests; // �û���Ȥ�б�
    private CenterPlaceDataLoader dataLoader; // ���ݼ�����
    /**
     * LoginWindow ��Ĺ��캯����
     * ����ʼ�����ڣ����ñ��⡢��С��Ĭ�Ϲرղ�����λ�á�
     * ������ʼ�����û���������������ֶΣ��Լ���¼��ע�ᰴť��
     * �����������û����ݣ������������ݼ��������Ƽ�ϵͳ������ϵͳ��ʵ����
     * @param ums �û�����ϵͳ
     */
    public LoginWindow(UserManagementSystem ums) {
        this.ums = ums;
        ums.loadUsersFromFile("users.txt");

        // �������ݼ�����ʵ��
        this.dataLoader = new CenterPlaceDataLoader("CenterPlace.txt");
        // �����Ƽ�ϵͳʵ��
        recommendationSystem = new RecommendationSystem(dataLoader);

        searchSystem= new SearchSystem(dataLoader);

        // �û���Ȥ�б�
        userInterests = new ArrayList<>();

        setTitle("��¼����"); // ���ô��ڵı���
        setSize(260, 140); // ���ô��ڵĴ�С
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ����Ĭ�Ϲرղ���
        setLocationRelativeTo(null); // �����ھ���

        // ���� UI ���
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("��¼");
        registerButton = new JButton("ע��");

        // ����һ����岢������
        JPanel panel = new JPanel();

        panel.add(new JLabel("�û���:"));
        panel.add(usernameField);
        panel.add(new JLabel("��  ��:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        // �������ӵ����
        add(panel);

        // Ϊ��¼��ť��Ӷ���������
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // ���� UserManagementSystem �ĵ�¼����
                String sessionId = ums.login(username, password);
                if (sessionId != null) {
                    System.out.println("��¼�ɹ����ỰID��" + sessionId);
                    // �رյ�¼���ڲ���������
                    setVisible(false);
                    dispose();
                    User user = ums.getUserByUsername(username);
                    new MainWindow(recommendationSystem, searchSystem,userInterests,dataLoader).setVisible(true);
                    showPreferenceWindow(user, ums); // ��ʾƫ�ô���

                } else {
                    System.out.println("��¼ʧ��");
                }
            }
        });

        // Ϊע�ᰴť��Ӷ���������
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog("�������û���");
                String password = JOptionPane.showInputDialog("����������");
                String email = JOptionPane.showInputDialog("����������");
                if(username == null || password == null || email == null) {

                    return;
                }
                if(username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "�û�������������䲻��Ϊ��");
                    return;
                }
                // ���� UserManagementSystem ��ע�᷽��
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

    /**
     * ��ʾƫ�ô��ڡ�
     * �û����������������ѡ�����ǵ�ƫ�á�
     * @param user �û�
     * @param ums �û�����ϵͳ
     */
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
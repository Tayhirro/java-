import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * UserManagementSystem �࣬���ڹ����û���ע�ᡢ��¼����Ϣ���¡�
 */
public class UserManagementSystem {
    private Map<String, User> usersDB = new HashMap<>(); // �û����ݿ�
    private Map<String, String> sessionDB = new HashMap<>(); // �Ự���ݿ�

    /**
     * �û�ע�ᡣ
     * @param username �û���
     * @param password ����
     * @param email ����
     * @return ���ע��ɹ����򷵻� true�����򷵻� false��
     */
    public boolean register(String username, String password, String email){
        if (!usersDB.containsKey(username)) {
            User newUser = new User(username, password, email);
            usersDB.put(username, newUser);
            return true;
        }
        return false;
    }

    /**
     * �û���¼��
     * @param username �û���
     * @param password ����
     * @return �����¼�ɹ����򷵻ػỰ ID�����򷵻� null��
     */
    public String login(String username, String password) {
        if(usersDB.containsKey(username)&&usersDB.get(username).getPassword().equals(password)) {
            String sessionId = generateSessionId();
            sessionDB.put(sessionId, username);
            return sessionId;
        }
        return null;
    }

    /**
     * �����û���Ϣ��
     * @param sessionId �Ự ID
     * @param newEmail �µ�����
     * @param additionalInfo �������Ϣ
     * @return ������³ɹ����򷵻� true�����򷵻� false��
     */
    public boolean updateProfile(String sessionId, String newEmail, Map<String, String> additionalInfo) {
        if (sessionDB.containsKey(sessionId)) {
            String username = sessionDB.get(sessionId);
            if (usersDB.containsKey(username)) {
                User user = usersDB.get(username);
                if (newEmail != null) {
                    user.setEmail(newEmail);
                }
                if (additionalInfo != null) {
                    for (Map.Entry<String, String> entry : additionalInfo.entrySet()) {
                        user.addAdditionalInfo(entry.getKey(), entry.getValue());
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * ���û���Ϣ���浽�ļ���
     * @param filename �ļ���
     */
    public void saveUsersToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            for (User user : usersDB.values()) {
                String additionalInfoStr = user.getAdditionalInfo().isEmpty() ? "" : "," + user.getAdditionalInfo().entrySet().stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .collect(Collectors.joining(";"));
                writer.println(user.getUsername() + "," + user.getPassword() + "," + user.getEmail() + additionalInfoStr);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * ���ļ��м����û���Ϣ��
     * @param filename �ļ���
     */
    public void loadUsersFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String[] userData = scanner.nextLine().split(",");
                register(userData[0], userData[1], userData[2]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * �����û�����ȡ�û���
     * @param username �û���
     * @return �û�
     */
    public User getUserByUsername(String username) {
        return usersDB.get(username);
    }

    /**
     * ���ɻỰ ID��
     * @return �Ự ID
     */
    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class UserManagementSystem {
    private Map<String, User> usersDB = new HashMap<>();
    private Map<String, String> sessionDB = new HashMap<>();

    // 用户注册
    public boolean register(String username, String password, String email){
        if (!usersDB.containsKey(username)) {
            User newUser = new User(username, password, email);
            usersDB.put(username, newUser);
            return true;
        }
        return false;
    }

    // 用户登录
    public String login(String username, String password) {
        if(usersDB.containsKey(username)&&usersDB.get(username).getPassword().equals(password)) {
            String sessionId = generateSessionId();
            sessionDB.put(sessionId, username);
            return sessionId;
        }
        return null;
    }

    // 更新信息
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

    // 保存用户信息到文件
    public void saveUsersToFile(String filename) {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            for (User user : usersDB.values()) {
                writer.println(user.getUsername() + "," + user.getPassword() + "," + user.getEmail());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

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

    // Generate session ID
    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}

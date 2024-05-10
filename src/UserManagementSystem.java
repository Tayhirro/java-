import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * UserManagementSystem 类，用于管理用户的注册、登录和信息更新。
 */
public class UserManagementSystem {
    private Map<String, User> usersDB = new HashMap<>(); // 用户数据库
    private Map<String, String> sessionDB = new HashMap<>(); // 会话数据库

    /**
     * 用户注册。
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @return 如果注册成功，则返回 true，否则返回 false。
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
     * 用户登录。
     * @param username 用户名
     * @param password 密码
     * @return 如果登录成功，则返回会话 ID，否则返回 null。
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
     * 更新用户信息。
     * @param sessionId 会话 ID
     * @param newEmail 新的邮箱
     * @param additionalInfo 额外的信息
     * @return 如果更新成功，则返回 true，否则返回 false。
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
     * 将用户信息保存到文件。
     * @param filename 文件名
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
     * 从文件中加载用户信息。
     * @param filename 文件名
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
     * 根据用户名获取用户。
     * @param username 用户名
     * @return 用户
     */
    public User getUserByUsername(String username) {
        return usersDB.get(username);
    }

    /**
     * 生成会话 ID。
     * @return 会话 ID
     */
    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
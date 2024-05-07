import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    // Generate session ID
    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}

import java.util.HashMap;
import java.util.Map;

class User {
    private final String username;
    private final String password;
    private String email;
    private Map<String, String> additionalInfo;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.additionalInfo = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void addAdditionalInfo(String key, String value) {
        additionalInfo.put(key, value);
    }

    public Map<String, String> getAdditionalInfo() {
        return additionalInfo;
    }


}

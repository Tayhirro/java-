
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

// 游学推荐页面
public class RecommendationPanel extends JPanel {

    private static final long serialVersionUID = 1L;// 序列化版本号
    private final User currUser;// 当前用户
    private final UserManagement userManagement;// 用户管理对象
    private final SpotManagement spotManagement;// 景点管理对象

    public RecommendationPanel(User currUser, UserManagement userManagement, SpotManagement spotManagement) {
        this.currUser = currUser;
        this.userManagement = userManagement;
        this.spotManagement = spotManagement;

        setLayout(new BorderLayout());
        add(new JLabel("游学推荐页面"), BorderLayout.CENTER);
    }

}

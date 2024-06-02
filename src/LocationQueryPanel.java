
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LocationQueryPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    User currUser;
    UserManagement userManagement;
    SpotManagement spotManagement;

    public LocationQueryPanel(User currUser, UserManagement userManagement, SpotManagement spotManagement) {
        this.currUser = currUser;
        this.userManagement = userManagement;
        this.spotManagement = spotManagement;

        setLayout(new BorderLayout());
        add(new JLabel("场所查询页面"), BorderLayout.CENTER);
    }

}

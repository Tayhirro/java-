
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StudyDiaryPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    User currUser;
    UserManagement userManagement;
    SpotManagement spotManagement;

    public StudyDiaryPanel(User currUser, UserManagement userManagement, SpotManagement spotManagement) {
        this.currUser = currUser;
        this.userManagement = userManagement;
        this.spotManagement = spotManagement;

        setLayout(new BorderLayout());
        add(new JLabel("游学日记页面"), BorderLayout.CENTER);
    }

}

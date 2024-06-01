import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class StudyDiaryPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public StudyDiaryPanel(UserManagement userManagement, User currUser) {
        setLayout(new BorderLayout());
        add(new JLabel("游学日记页面"), BorderLayout.CENTER);
    }

}
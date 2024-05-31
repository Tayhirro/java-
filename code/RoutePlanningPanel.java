import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class RoutePlanningPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public RoutePlanningPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("路径规划页面"), BorderLayout.CENTER);
    }

}

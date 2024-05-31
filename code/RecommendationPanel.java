import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class RecommendationPanel extends JPanel {
    private static final long serialVersionUID = 1L;// 序列化版本号

    public RecommendationPanel(User currUser) {
        setLayout(new BorderLayout());
        add(new JLabel("游学推荐页面"), BorderLayout.CENTER);
    }

}
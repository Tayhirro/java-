import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class FoodRecommendationPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public FoodRecommendationPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("美食推荐页面"), BorderLayout.CENTER);
    }

}
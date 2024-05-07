import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RecommendationWindow extends JFrame {
    private RecommendationSystem recommendationSystem;
    private List<String> userInterests;

    public RecommendationWindow(RecommendationSystem recommendationSystem, List<String> userInterests) {
        this.recommendationSystem = recommendationSystem;
        this.userInterests = userInterests;

        setTitle("游学推荐");
        setSize(320, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JButton top10Button = new JButton("Top 10 Recommendations");
        top10Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTop10Recommendations();
            }
        });

        add(top10Button);
    }

    private void showTop10Recommendations() {
        List<CenterPlace> top10CenterPlaces = recommendationSystem.recommendCenterPlaces(0.6, 0.5, userInterests);
        StringBuilder message = new StringBuilder("Top 10 Recommended CenterPlaces:\n");
        for (CenterPlace centerPlace : top10CenterPlaces) {
            message.append(centerPlace.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(this, message.toString());
    }
}
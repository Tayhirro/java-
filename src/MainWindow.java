import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainWindow extends JFrame {
    private JButton recommendationButton;
    private JButton routePlanningButton;
    private JButton placeQueryButton;
    private JButton diaryButton;

    private RecommendationSystem recommendationSystem;
    private List<String> userInterests;

    public MainWindow(RecommendationSystem recommendationSystem, List<String> userInterests) {
        this.recommendationSystem = recommendationSystem;
        this.userInterests = userInterests;

        setTitle("主界面");
        setSize(320, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create UI components
        recommendationButton = new JButton("游学推荐");
        routePlanningButton = new JButton("路线规划");
        placeQueryButton = new JButton("场所查询");
        diaryButton = new JButton("游学日记");

        // Create a panel and add the components
        JPanel panel = new JPanel();
        panel.add(recommendationButton);
        panel.add(routePlanningButton);
        panel.add(placeQueryButton);
        panel.add(diaryButton);

        // Add the panel to the frame
        add(panel);

        // Add action listeners to the buttons
        recommendationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the recommendation window
                new RecommendationWindow(recommendationSystem, userInterests).setVisible(true);
            }
        });

        routePlanningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the route planning window
                new RoutePlanningWindow().setVisible(true);
            }
        });

        placeQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the place query window
                new PlaceQueryWindow().setVisible(true);
            }
        });

        diaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the diary window
                new DiaryWindow().setVisible(true);
            }
        });
    }
}
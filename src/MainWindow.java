import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainWindow extends JFrame {

    public MainWindow(RecommendationSystem recommendationSystem, SearchSystem searchSystem, List<String> userInterests) {

        setTitle("主界面");
        setSize(240, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton recommendationButton = new JButton("游学推荐");
        JButton routePlanningButton = new JButton("路线规划");
        JButton placeQueryButton = new JButton("场所查询");
        JButton diaryButton = new JButton("游学日记");

        // 创建面板并添加组件
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 游学前相关组件面板
        JPanel beforeStudyTourPanel = new JPanel();
        beforeStudyTourPanel.add(new JLabel("游学前："));
        beforeStudyTourPanel.add(recommendationButton);
        panel.add(beforeStudyTourPanel, gbc); // 添加游学前组件到主面板

        // 游学中相关组件面板
        gbc.gridy++;
        JPanel inStudyTourPanel = new JPanel();
        inStudyTourPanel.add(new JLabel("游学中："));
        JPanel inStudyTourButtonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        inStudyTourButtonPanel.add(routePlanningButton);
        inStudyTourButtonPanel.add(placeQueryButton);
        inStudyTourPanel.add(inStudyTourButtonPanel);
        panel.add(inStudyTourPanel, gbc); // 添加游学中组件到主面板

        // 游学后相关组件面板
        gbc.gridy++;
        JPanel afterStudyTourPanel = new JPanel();
        afterStudyTourPanel.add(new JLabel("游学后："));
        afterStudyTourPanel.add(diaryButton);
        panel.add(afterStudyTourPanel, gbc); // 添加游学后组件到主面板

        add(panel); // 将主面板添加到窗口中


        // Add action listeners to the buttons
        recommendationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the recommendation window
                new RecommendationWindow(recommendationSystem, searchSystem, userInterests).setVisible(true);
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
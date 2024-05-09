import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class RecommendationWindow extends JFrame {
    private RecommendationSystem recommendationSystem;
    private List<String> userInterests;
    private SearchSystem searchSystem;

    public RecommendationWindow(RecommendationSystem recommendationSystem,SearchSystem searchSystem, List<String> userInterests) {
        this.recommendationSystem = recommendationSystem;
        this.userInterests = userInterests;
        this.searchSystem = searchSystem;

        setTitle("��ѧ�Ƽ�");
        setSize(320, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JButton top10Button = new JButton("ǰʮ�Ƽ�");
        top10Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTop10Recommendations();
            }
        });

        JButton searchButton = new JButton("����/ѧУ��ѯ");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchResults();
            }
        });

        JButton tagRecommendationButton = new JButton("�����Ƽ�");
        tagRecommendationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRecommendationsByTags();
            }
        });

        JPanel panel = new JPanel();
        panel.add(top10Button);
        panel.add(searchButton);
        panel.add(tagRecommendationButton);
        add(panel);
    }

    private void showTop10Recommendations() {
        List<CenterPlace> top10CenterPlaces = recommendationSystem.recommendCenterPlaces(0.6, 0.5, userInterests);
        StringBuilder message = new StringBuilder("Top 10 Recommended CenterPlaces:\n");
        for (CenterPlace centerPlace : top10CenterPlaces) {
            message.append(centerPlace.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(this, message.toString());
    }

    private void showSearchResults() {
        String query = JOptionPane.showInputDialog(this, "�������ѯ�ؼ���"); // Get user input
        double popularityWeight = 0.6;
        double ratingWeight = 0.5;
        List<CenterPlace> searchResults = searchSystem.searchAndSort(query, popularityWeight, ratingWeight);
        StringBuilder message = new StringBuilder("Search Results:\n");
        for (CenterPlace centerPlace : searchResults) {
            message.append(centerPlace.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(this, message.toString());
    }

    private void showRecommendationsByTags() {
        String input = JOptionPane.showInputDialog(this, "�������ǩ���ö��ŷָ�"); // Get user input
        List<String> tags = Arrays.asList(input.split(","));
        double popularityWeight = 0.6;
        double ratingWeight = 0.5;
        List<CenterPlace> tagRecommendations = recommendationSystem.recommendCenterPlacesByTags(tags, popularityWeight, ratingWeight);
        StringBuilder message = new StringBuilder("Recommendations by Tags:\n");
        for (CenterPlace centerPlace : tagRecommendations) {
            message.append(centerPlace.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(this, message.toString());
    }

}
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

/**
 * RecommendationWindow 是 JFrame 的子类，代表推荐窗口。
 * 它设置了窗口的标题、大小、默认关闭操作和位置。
 * 它还包含了三个按钮，分别用于显示前十推荐、景点/学校查询和个性推荐。
 */
public class RecommendationWindow extends JFrame {
    private RecommendationSystem recommendationSystem; // 推荐系统
    private List<String> userInterests; // 用户兴趣列表
    private SearchSystem searchSystem; // 搜索系统

    /**
     * RecommendationWindow 类的构造函数。
     * @param recommendationSystem 推荐系统
     * @param searchSystem 搜索系统
     * @param userInterests 用户的兴趣列表
     */
    public RecommendationWindow(RecommendationSystem recommendationSystem,SearchSystem searchSystem, List<String> userInterests) {
        this.recommendationSystem = recommendationSystem;
        this.userInterests = userInterests;
        this.searchSystem = searchSystem;

        setTitle("游学推荐"); // 设置窗口的标题
        setSize(320, 200); // 设置窗口的大小
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 设置默认关闭操作
        setLocationRelativeTo(null); // 将窗口居中

        JButton top10Button = new JButton("前十推荐");
        top10Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTop10Recommendations();
            }
        });

        JButton searchButton = new JButton("景点/学校查询");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSearchResults();
            }
        });

        JButton tagRecommendationButton = new JButton("个性推荐");
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

    /**
     * 显示前十推荐的中心地点。
     */
    private void showTop10Recommendations() {
        List<CenterPlace> top10CenterPlaces = recommendationSystem.recommendCenterPlaces(0.6, 0.5, userInterests);
        StringBuilder message = new StringBuilder("Top 10 Recommended CenterPlaces:\n");
        for (CenterPlace centerPlace : top10CenterPlaces) {
            message.append(centerPlace.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(this, message.toString());
    }

    /**
     * 显示搜索结果。
     */
    private void showSearchResults() {
        String query = JOptionPane.showInputDialog(this, "请输入查询关键词"); // 获取用户输入
        double popularityWeight = 0.6;
        double ratingWeight = 0.5;
        List<CenterPlace> searchResults = searchSystem.searchAndSort(query, popularityWeight, ratingWeight);
        StringBuilder message = new StringBuilder("Search Results:\n");
        for (CenterPlace centerPlace : searchResults) {
            message.append(centerPlace.getName()).append("\n");
        }
        JOptionPane.showMessageDialog(this, message.toString());
    }

    /**
     * 根据标签显示推荐的中心地点。
     */
    private void showRecommendationsByTags() {
        String input = JOptionPane.showInputDialog(this, "请输入标签，用逗号分隔"); // 获取用户输入
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
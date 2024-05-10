import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

/**
 * RecommendationWindow �� JFrame �����࣬�����Ƽ����ڡ�
 * �������˴��ڵı��⡢��С��Ĭ�Ϲرղ�����λ�á�
 * ����������������ť���ֱ�������ʾǰʮ�Ƽ�������/ѧУ��ѯ�͸����Ƽ���
 */
public class RecommendationWindow extends JFrame {
    private RecommendationSystem recommendationSystem; // �Ƽ�ϵͳ
    private List<String> userInterests; // �û���Ȥ�б�
    private SearchSystem searchSystem; // ����ϵͳ

    /**
     * RecommendationWindow ��Ĺ��캯����
     * @param recommendationSystem �Ƽ�ϵͳ
     * @param searchSystem ����ϵͳ
     * @param userInterests �û�����Ȥ�б�
     */
    public RecommendationWindow(RecommendationSystem recommendationSystem,SearchSystem searchSystem, List<String> userInterests) {
        this.recommendationSystem = recommendationSystem;
        this.userInterests = userInterests;
        this.searchSystem = searchSystem;

        setTitle("��ѧ�Ƽ�"); // ���ô��ڵı���
        setSize(320, 200); // ���ô��ڵĴ�С
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ����Ĭ�Ϲرղ���
        setLocationRelativeTo(null); // �����ھ���

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

    /**
     * ��ʾǰʮ�Ƽ������ĵص㡣
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
     * ��ʾ���������
     */
    private void showSearchResults() {
        String query = JOptionPane.showInputDialog(this, "�������ѯ�ؼ���"); // ��ȡ�û�����
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
     * ���ݱ�ǩ��ʾ�Ƽ������ĵص㡣
     */
    private void showRecommendationsByTags() {
        String input = JOptionPane.showInputDialog(this, "�������ǩ���ö��ŷָ�"); // ��ȡ�û�����
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
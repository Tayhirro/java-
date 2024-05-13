import java.util.*;

/**
 * �Ƽ�ϵͳ�࣬���ڸ����û���ϲ�ú���Ȥ�Ƽ����ĵص㡣
 */
public class RecommendationSystem {
    private List<CenterPlace> centerPlaces; // ���ĵص��б�

    /**
     * �Ƽ�ϵͳ�Ĺ��캯����
     * @param dataLoader ���ݼ����������ڴ��ļ��м������ĵص����ݡ�
     */
    public RecommendationSystem(CenterPlaceDataLoader dataLoader) {
        centerPlaces = dataLoader.getCenterPlaces(); // ��ʼ�����ĵص�����
    }

    /**
     * �����û���ϲ�ú���Ȥ�Ƽ����ĵص㡣
     * @param userPopularityWeight �û����ȶȵ�Ȩ��
     * @param userRatingWeight �û������ֵ�Ȩ��
     * @param userInterests �û�����Ȥ�б�
     * @return �Ƽ������ĵص��б�
     */
    public List<CenterPlace> recommendCenterPlaces(double userPopularityWeight, double userRatingWeight, List<String> userInterests) {
        // ���������ĵص�洢��һ���б���
        List<CenterPlace> allCenterPlaces = new ArrayList<>();
        for (CenterPlace centerPlace : centerPlaces) {
            allCenterPlaces.add(centerPlace);
        }

        // ���ݵ÷ֶ��������ĵص��������
        Collections.sort(allCenterPlaces, new Comparator<CenterPlace>() {
            @Override
            public int compare(CenterPlace c1, CenterPlace c2) {
                double score1 = calculateScore(c1, userPopularityWeight, userRatingWeight, userInterests);
                double score2 = calculateScore(c2, userPopularityWeight, userRatingWeight, userInterests);
                return Double.compare(score2, score1); // ��������
            }
        });

        // ���������ĵص��л�ȡǰ10��
        List<CenterPlace> top10CenterPlaces = new ArrayList<>();
        for (int i = 0; i < 10 && i < allCenterPlaces.size(); i++) {
            top10CenterPlaces.add(allCenterPlaces.get(i));
        }

        return top10CenterPlaces;
    }

    /**
     * ���ݱ�ǩ�Ƽ����ĵص㡣
     * @param tags ��ǩ�б�
     * @param popularityWeight �ȶ�Ȩ��
     * @param ratingWeight ����Ȩ��
     * @return �Ƽ������ĵص��б�
     */
    public List<CenterPlace> recommendCenterPlacesByTags(List<String> tags, double popularityWeight, double ratingWeight) {
        List<CenterPlace> results = new ArrayList<>();
        for (CenterPlace centerPlace : centerPlaces) {
            for (String keyword : centerPlace.getKeywords()) {
                if (tags.contains(keyword)) {
                    results.add(centerPlace);
                    break;
                }
            }
        }

        // �����ȶȺ����ֶԽ����������
        Collections.sort(results, new Comparator<CenterPlace>() {
            @Override
            public int compare(CenterPlace c1, CenterPlace c2) {
                double score1 = popularityWeight * c1.getPopularity() + ratingWeight * c1.getRating();
                double score2 = popularityWeight * c2.getPopularity() + ratingWeight * c2.getRating();
                return Double.compare(score2, score1); // ��������
            }
        });

        return results;
    }

    /**
     * �����û���ϲ�ú���Ȥ�������ĵص�ĵ÷֡�
     * @param centerPlace ���ĵص�
     * @param popularityWeight �ȶ�Ȩ��
     * @param ratingWeight ����Ȩ��
     * @param userInterests �û�����Ȥ�б�
     * @return ���ĵص�ĵ÷�
     */
    private double calculateScore(CenterPlace centerPlace, double popularityWeight, double ratingWeight, List<String> userInterests) {
        double score = popularityWeight * centerPlace.getPopularity() + ratingWeight * centerPlace.getRating();
        // �����û���Ȥ�����ĵص����Ե�ƥ��̶����Ӷ���÷�
        for (String interest : userInterests) {
            if (centerPlace.getKeywords().contains(interest)) {
                score += 50; // ÿƥ��һ����Ȥ���÷�����50
                System.out.println("ƥ�����Ȥ��" + interest);
            }
        }
        return score;
    }
}
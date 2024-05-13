import java.util.*;

/**
 * 推荐系统类，用于根据用户的喜好和兴趣推荐中心地点。
 */
public class RecommendationSystem {
    private List<CenterPlace> centerPlaces; // 中心地点列表

    /**
     * 推荐系统的构造函数。
     * @param dataLoader 数据加载器，用于从文件中加载中心地点数据。
     */
    public RecommendationSystem(CenterPlaceDataLoader dataLoader) {
        centerPlaces = dataLoader.getCenterPlaces(); // 初始化中心地点数据
    }

    /**
     * 根据用户的喜好和兴趣推荐中心地点。
     * @param userPopularityWeight 用户对热度的权重
     * @param userRatingWeight 用户对评分的权重
     * @param userInterests 用户的兴趣列表
     * @return 推荐的中心地点列表
     */
    public List<CenterPlace> recommendCenterPlaces(double userPopularityWeight, double userRatingWeight, List<String> userInterests) {
        // 将所有中心地点存储在一个列表中
        List<CenterPlace> allCenterPlaces = new ArrayList<>();
        for (CenterPlace centerPlace : centerPlaces) {
            allCenterPlaces.add(centerPlace);
        }

        // 根据得分对所有中心地点进行排序
        Collections.sort(allCenterPlaces, new Comparator<CenterPlace>() {
            @Override
            public int compare(CenterPlace c1, CenterPlace c2) {
                double score1 = calculateScore(c1, userPopularityWeight, userRatingWeight, userInterests);
                double score2 = calculateScore(c2, userPopularityWeight, userRatingWeight, userInterests);
                return Double.compare(score2, score1); // 降序排序
            }
        });

        // 从所有中心地点中获取前10个
        List<CenterPlace> top10CenterPlaces = new ArrayList<>();
        for (int i = 0; i < 10 && i < allCenterPlaces.size(); i++) {
            top10CenterPlaces.add(allCenterPlaces.get(i));
        }

        return top10CenterPlaces;
    }

    /**
     * 根据标签推荐中心地点。
     * @param tags 标签列表
     * @param popularityWeight 热度权重
     * @param ratingWeight 评分权重
     * @return 推荐的中心地点列表
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

        // 根据热度和评分对结果进行排序
        Collections.sort(results, new Comparator<CenterPlace>() {
            @Override
            public int compare(CenterPlace c1, CenterPlace c2) {
                double score1 = popularityWeight * c1.getPopularity() + ratingWeight * c1.getRating();
                double score2 = popularityWeight * c2.getPopularity() + ratingWeight * c2.getRating();
                return Double.compare(score2, score1); // 降序排序
            }
        });

        return results;
    }

    /**
     * 根据用户的喜好和兴趣计算中心地点的得分。
     * @param centerPlace 中心地点
     * @param popularityWeight 热度权重
     * @param ratingWeight 评分权重
     * @param userInterests 用户的兴趣列表
     * @return 中心地点的得分
     */
    private double calculateScore(CenterPlace centerPlace, double popularityWeight, double ratingWeight, List<String> userInterests) {
        double score = popularityWeight * centerPlace.getPopularity() + ratingWeight * centerPlace.getRating();
        // 根据用户兴趣与中心地点特性的匹配程度增加额外得分
        for (String interest : userInterests) {
            if (centerPlace.getKeywords().contains(interest)) {
                score += 50; // 每匹配一个兴趣，得分增加50
                System.out.println("匹配的兴趣：" + interest);
            }
        }
        return score;
    }
}
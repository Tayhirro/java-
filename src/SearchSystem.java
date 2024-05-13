import java.util.*;

/**
 * SearchSystem 类，用于搜索和排序中心地点。
 */
public class SearchSystem {
    private List<CenterPlace> centerPlaces; // 中心地点列表

    /**
     * SearchSystem 类的构造函数。
     * @param dataLoader 数据加载器，用于从文件中加载中心地点数据。
     */
    
    public SearchSystem(CenterPlaceDataLoader dataLoader) {
        centerPlaces = dataLoader.getCenterPlaces(); // 初始化中心地点数据
    }

    /**
     * 根据查询关键词、热度权重和评分权重搜索和排序中心地点。
     * @param query 查询关键词
     * @param popularityWeight 热度权重
     * @param ratingWeight 评分权重
     * @return 搜索和排序后的中心地点列表
     */
    public List<CenterPlace> searchAndSort(String query, double popularityWeight, double ratingWeight) {
        List<CenterPlace> results = new ArrayList<>();
        for (CenterPlace centerPlace : centerPlaces) {
            if (centerPlace.getName().contains(query) ||
                    centerPlace.getCategory().contains(query) ||
                    containsKeyword(centerPlace, query)) {
                results.add(centerPlace);
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
     * 检查中心地点的关键词是否包含查询关键词。
     * @param centerPlace 中心地点
     * @param query 查询关键词
     * @return 如果中心地点的关键词包含查询关键词，则返回 true，否则返回 false。
     */
    private boolean containsKeyword(CenterPlace centerPlace, String query) {
        for (String keyword : centerPlace.getKeywords()) {
            if (keyword.contains(query)) {
                return true;
            }
        }
        return false;
    }
}
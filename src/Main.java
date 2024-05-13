import java.util.*;

public class Main {
    public static void main(String[] args) {
        UserManagementSystem ums = new UserManagementSystem();
        new LoginWindow(ums);   // 创建登录窗口

        // 创建数据加载器实例-- 读取文件
        CenterPlaceDataLoader dataLoader = new CenterPlaceDataLoader("CenterPlace.txt");

        // 创建推荐系统和搜索系统实例   
        RecommendationSystem recommendationSystem = new RecommendationSystem(dataLoader);
        SearchSystem searchSystem = new SearchSystem(dataLoader);

        // 使用推荐系统和搜索系统
        List<String> userInterests =new ArrayList<>();



        // 为用户添加兴趣
        List<CenterPlace> recommendedCenterPlaces = recommendationSystem.recommendCenterPlaces(0.6,0.5,userInterests);


        String searchQuery = "keyword1";
        List<CenterPlace> searchResults = searchSystem.searchAndSort(searchQuery,0.6,0.5);
        System.out.println("Search Results: ");
        for (CenterPlace centerPlace : searchResults) {
            System.out.println(centerPlace.getName());
        }
    }
}//tay
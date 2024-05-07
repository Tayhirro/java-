import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserManagementSystem ums = new UserManagementSystem();
        new LoginWindow(ums);

        // �������ݼ�����ʵ��
        CenterPlaceDataLoader dataLoader = new CenterPlaceDataLoader("CenterPlace.txt");

        // �����Ƽ�ϵͳ������ϵͳʵ��
        RecommendationSystem recommendationSystem = new RecommendationSystem(dataLoader);
        SearchSystem searchSystem = new SearchSystem(dataLoader);

        // ʹ���Ƽ�ϵͳ������ϵͳ
        List<String> userInterests = List.of("keyword1", "keyword3");

        List<CenterPlace> recommendedCenterPlaces = recommendationSystem.recommendCenterPlaces(0.6,0.5,userInterests);
        System.out.println("Top 10 Recommended CenterPlaces: ");
        for (CenterPlace centerPlace : recommendedCenterPlaces) {
            System.out.println(centerPlace.getName());
        }
        String searchQuery = "keyword1";
        List<CenterPlace> searchResults = searchSystem.searchAndSort(searchQuery,0.6,0.5);
        System.out.println("Search Results: ");
        for (CenterPlace centerPlace : searchResults) {
            System.out.println(centerPlace.getName());
        }
    }
}//tay
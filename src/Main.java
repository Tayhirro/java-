import java.util.*;

public class Main {
    public static void main(String[] args) {
        UserManagementSystem ums = new UserManagementSystem();
        new LoginWindow(ums);   // ������¼����

        // �������ݼ�����ʵ��-- ��ȡ�ļ�
        CenterPlaceDataLoader dataLoader = new CenterPlaceDataLoader("CenterPlace.txt");

        // �����Ƽ�ϵͳ������ϵͳʵ��   
        RecommendationSystem recommendationSystem = new RecommendationSystem(dataLoader);
        SearchSystem searchSystem = new SearchSystem(dataLoader);

        // ʹ���Ƽ�ϵͳ������ϵͳ
        List<String> userInterests =new ArrayList<>();



        // Ϊ�û������Ȥ
        List<CenterPlace> recommendedCenterPlaces = recommendationSystem.recommendCenterPlaces(0.6,0.5,userInterests);


        String searchQuery = "keyword1";
        List<CenterPlace> searchResults = searchSystem.searchAndSort(searchQuery,0.6,0.5);
        System.out.println("Search Results: ");
        for (CenterPlace centerPlace : searchResults) {
            System.out.println(centerPlace.getName());
        }
    }
}//tay
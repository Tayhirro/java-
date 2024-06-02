
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendationSystem {

    private final User currUser;// 当前用户
    private final UserManagement userManagement;// 用户管理对象
    private final SpotManagement spotManagement;// 景点管理对象

    private List<Spot> spots;// 所有景点
    private List<Spot> schools;// 学校
    private List<Spot> attractions;// 景点

    RecommendationSystem(User currUser, UserManagement userManagement, SpotManagement spotManagement) {
        this.currUser = currUser;
        this.userManagement = userManagement;
        this.spotManagement = spotManagement;
        // 加载初始数据
        loadInitialData();
    }

    private void loadInitialData() {
        spots = spotManagement.getAllSpots();
        schools = spots.stream().filter(spot -> spot.getType().equals("school")).toList();
        attractions = spots.stream().filter(spot -> spot.getType().equals("attraction")).toList();
    }

    public List<Spot> getInitialSchools() {
        return schools;
    }

    public List<Spot> getInitialAttractions() {
        return attractions;
    }

    public List<Spot> getAllSpots() {
        return spotManagement.getAllSpots();
    }

    public List<Spot> searchSpots(String query) {
        return spotManagement.getAllSpots().stream()
                .filter(spot -> spot.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Spot> sortSpots(List<Spot> spots, String criteria) {
        Comparator<Spot> comparator = null;
        if ("Sort by Popularity".equals(criteria)) {
            comparator = Comparator.comparingLong(Spot::getPopularity).reversed();
        } else if ("Sort by Rating".equals(criteria)) {
            comparator = Comparator.comparingDouble(Spot::getScore).reversed();
        }

        if (comparator != null) {
            return spots.stream().sorted(comparator).collect(Collectors.toList());
        }
        return spots;
    }

    public List<Spot> filterAndSortSpots(List<String> selectedPreferences) {
        List<Spot> filteredSchools = filterAndSort(schools, selectedPreferences);
        List<Spot> filteredAttractions = filterAndSort(attractions, selectedPreferences);

        // 合并学校和景点的列表
        List<Spot> filteredSpots = new ArrayList<>();
        filteredSpots.addAll(filteredSchools);
        filteredSpots.addAll(filteredAttractions);

        return filteredSpots;
    }

    private List<Spot> filterAndSort(List<Spot> spots, List<String> selectedPreferences) {
        // 过滤出符合偏好的景点
        List<Spot> filteredSpots = new ArrayList<>();
        for (Spot spot : spots) {
            if (selectedPreferences.stream().anyMatch(preference -> spot.getTags().contains(preference))) {
                filteredSpots.add(spot);
            }
        }

        // 计算每个景点符合偏好的数量
        Map<Spot, Long> spotPreferenceCount = new HashMap<>();
        for (Spot spot : filteredSpots) {
            long count = spot.getTags().stream().filter(selectedPreferences::contains).count();
            spotPreferenceCount.put(spot, count);
        }

        // 按符合偏好的数量和热度排序
        filteredSpots.sort((spot1, spot2) -> {
            int comparePreferenceCount = spotPreferenceCount.get(spot2).compareTo(spotPreferenceCount.get(spot1));
            if (comparePreferenceCount != 0) {
                return comparePreferenceCount;
            } else {
                return Long.compare(spot2.getPopularity(), spot1.getPopularity());
            }
        });

        // 只保留前10个景点
        if (filteredSpots.size() > 10) {
            filteredSpots = filteredSpots.subList(0, 10);
        }

        return filteredSpots;
    }
}

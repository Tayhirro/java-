
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Food {

    private String name;
    private String category;
    private double rating;
    private int views;
    private String canteen;
    private List<String> keywords;
    private double comprehension;

    public Food(String name, String category, double rating, int views, String canteen, List<String> keywords) {
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.views = views;
        this.canteen = canteen;
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "Food{"
                + "name='" + name + '\''
                + ", category='" + category + '\''
                + ", rating=" + rating
                + ", views=" + views
                + ", canteen='" + canteen + '\''
                + ", keywords=" + keywords
                + '}';
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getRating() {
        return rating;
    }

    public int getViews() {
        return views;
    }

    public String getCanteen() {
        return canteen;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public double getComprehension() {
        return comprehension;
    }

    public void setComprehension(double comprehension) {
        this.comprehension = comprehension;
    }

}

class FoodManagement {

    private List<Food> foodList;

    public FoodManagement() {
        foodList = new ArrayList<>();
        loadUserData("Data/food.txt");

    }

    public void loadUserData(String filePath) {
        double minrating = 5.0;
        double maxrating = 0.0;
        int minviews = Integer.MAX_VALUE;
        int maxviews = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0].trim();
                String category = parts[1].trim();
                double rating = Double.parseDouble(parts[2].trim());
                int views = Integer.parseInt(parts[3].trim());
                String canteen = parts[4].trim();
                List<String> keywords = parseKeywords(parts[5].trim());

                minrating = Math.min(minrating, rating);
                maxrating = Math.max(maxrating, rating);
                minviews = Math.min(minviews, views);
                maxviews = Math.max(maxviews, views);
                Food food = new Food(name, category, rating, views, canteen, keywords);
                foodList.add(food);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Calculate comprehension Rating for each food item
        for (Food food : foodList) {
            double ratingRating = (food.getRating() - minrating) / (maxrating - minrating);
            double viewsRating = (food.getViews() - minviews * 1.0) / (maxviews - minviews * 1.0);
            food.setComprehension(0.6 * ratingRating + 0.4 * viewsRating);
        }

    }

    private List<String> parseKeywords(String keywordsStr) {
        keywordsStr = keywordsStr.replace("{", "").replace("}", "");
        String[] keywordsArray = keywordsStr.split(" ");
        List<String> keywords = new ArrayList<>();
        for (String keyword : keywordsArray) {
            keywords.add(keyword.trim());
        }
        // 打印关键字
        return keywords;
    }

    public int findFoods(String category, String canteen, String keywords, List<Food> result) {
        int count = 0;
        for (Food food : foodList) {
            if ((food.getCategory().equalsIgnoreCase(category) || category.equalsIgnoreCase("All"))
                    && (food.getCanteen().equalsIgnoreCase(canteen) || canteen.equalsIgnoreCase("All"))
                    && (food.getKeywords().contains(keywords) || keywords.equalsIgnoreCase(""))) {
                result.add(food);
                count++;
            }
        }
        return count;
    }

    public int sortbyRating(List<Food> result) {
        result.sort((f1, f2) -> Double.compare(f2.getRating(), f1.getRating()));
        return result.size();
    }

    public int sortbyViews(List<Food> result) {
        result.sort((f1, f2) -> Integer.compare(f2.getViews(), f1.getViews()));
        return result.size();
    }

    public int sortbyComprehension(List<Food> result) {
        result.sort((f1, f2) -> Double.compare(f2.getComprehension(), f1.getComprehension()));
        return result.size();
    }

}

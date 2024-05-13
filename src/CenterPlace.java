import java.util.List;

public class CenterPlace {
    private final String name;  // 地区名字 
    private String category;    // 类别
    private List<String> keywords;  // 关键词
    private double popularity;//热度
    private double rating;//评价

    //-----------------------------------需要修改----------------------//
    private List<String> features; // Features related to user interests

    public CenterPlace(String name, String category, List<String> keywords, double popularity, double rating, List<String> features) {
        this.name = name;
        this.category = category;//类别
        this.keywords = keywords;//关键词
        this.popularity = popularity;//热度
        this.rating = rating;//评价
        this.features = features;//特点
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getPopularity() {
        return popularity;
    }

    public double getRating() {
        return rating;
    }

    public List<String> getFeatures() {
        return features;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public void setFeatures(List<String> features) {
        this.features = features;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

}

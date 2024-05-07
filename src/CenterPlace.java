import java.util.List;

public class CenterPlace {
    private String name;
    private double popularity;//热度
    private double rating;//评价
    private List<String> features; // Features related to user interests

    public CenterPlace(String name, double popularity, double rating, List<String> features) {
        this.name = name;
        this.popularity = popularity;
        this.rating = rating;
        this.features = features;
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

}

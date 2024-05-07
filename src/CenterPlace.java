import java.util.List;

public class CenterPlace {
    private final String name;
    private String category;
    private List<String> keywords;
    private double popularity;//»»∂»
    private double rating;//∆¿º€
    private List<String> features; // Features related to user interests

    public CenterPlace(String name, String category, List<String> keywords, double popularity, double rating, List<String> features) {
        this.name = name;
        this.category = category;
        this.keywords = keywords;
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

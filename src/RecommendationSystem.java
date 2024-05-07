import java.util.*;

public class RecommendationSystem {
    private List<CenterPlace> centerPlaces;

    public RecommendationSystem(CenterPlaceDataLoader dataLoader) {
        centerPlaces = dataLoader.getCenterPlaces();
        // Initialize centerPlaces data
    }

    public List<CenterPlace> recommendCenterPlaces(double userPopularityWeight, double userRatingWeight, List<String> userInterests) {
        PriorityQueue<CenterPlace> maxHeap = new PriorityQueue<>(10, new Comparator<CenterPlace>() {
            @Override
            public int compare(CenterPlace c1, CenterPlace c2) {
                double score1 = calculateScore(c1, userPopularityWeight, userRatingWeight, userInterests);
                double score2 = calculateScore(c2, userPopularityWeight, userRatingWeight, userInterests);
                return Double.compare(score2, score1); // Descending order
            }
        });

        // Insert centerPlaces into max heap
        for (CenterPlace centerPlace : centerPlaces) {
            maxHeap.offer(centerPlace);
            if (maxHeap.size() > 10) {
                maxHeap.poll(); // Remove the lowest score centerPlace if heap size exceeds 10
            }
        }

        // Convert max heap to list
        List<CenterPlace> top10CenterPlaces = new ArrayList<>(maxHeap);
        Collections.sort(top10CenterPlaces, new Comparator<CenterPlace>() {
            @Override
            public int compare(CenterPlace c1, CenterPlace c2) {
                double score1 = calculateScore(c1, userPopularityWeight, userRatingWeight, userInterests);
                double score2 = calculateScore(c2, userPopularityWeight, userRatingWeight, userInterests);
                return Double.compare(score2, score1); // Descending order
            }
        });

        return top10CenterPlaces;
    }

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

        // Sort results based on popularity and rating
        Collections.sort(results, new Comparator<CenterPlace>() {
            @Override
            public int compare(CenterPlace c1, CenterPlace c2) {
                double score1 = popularityWeight * c1.getPopularity() + ratingWeight * c1.getRating();
                double score2 = popularityWeight * c2.getPopularity() + ratingWeight * c2.getRating();
                return Double.compare(score2, score1); // Descending order
            }
        });

        return results;
    }

    // Calculate score for a centerPlace based on user's preferences and interests
    private double calculateScore(CenterPlace centerPlace, double popularityWeight, double ratingWeight, List<String> userInterests) {
        double score = popularityWeight * centerPlace.getPopularity() + ratingWeight * centerPlace.getRating();
        // Additional score based on matching user interests with centerPlace features
        for (String interest : userInterests) {
            if (centerPlace.getFeatures().contains(interest)) {
                score += 1.0; // Increment score for each matching interest
            }
        }
        return score;
    }
}
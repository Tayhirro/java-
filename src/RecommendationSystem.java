import java.util.*;

public class RecommendationSystem {
    private List<CenterPlace> centerPlaces;

    public RecommendationSystem(CenterPlaceDataLoader dataLoader) {
        centerPlaces = dataLoader.getCenterPlaces();
        // Initialize centerPlaces data
    }

    public List<CenterPlace> recommendCenterPlaces(double userPopularityWeight, double userRatingWeight, List<String> userInterests) {
        List<CenterPlace> allCenterPlaces = new ArrayList<>();
        for (CenterPlace centerPlace : centerPlaces) {
            allCenterPlaces.add(centerPlace);
        }

        // Sort allCenterPlaces based on score
        Collections.sort(allCenterPlaces, new Comparator<CenterPlace>() {
            @Override
            public int compare(CenterPlace c1, CenterPlace c2) {
                double score1 = calculateScore(c1, userPopularityWeight, userRatingWeight, userInterests);
                double score2 = calculateScore(c2, userPopularityWeight, userRatingWeight, userInterests);
                return Double.compare(score2, score1); // Descending order
            }
        });

        // Get top 10 centerPlaces from allCenterPlaces
        List<CenterPlace> top10CenterPlaces = new ArrayList<>();
        for (int i = 0; i < 10 && i < allCenterPlaces.size(); i++) {
            top10CenterPlaces.add(allCenterPlaces.get(i));
        }

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
            if (centerPlace.getKeywords().contains(interest)) {
                score += 50; // Increment score for each matching interest
                System.out.println("Matched interest: " + interest);
            }
        }
        return score;
    }
}
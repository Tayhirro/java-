import java.util.*;

public class SearchSystem {
    private List<CenterPlace> centerPlaces;

    public SearchSystem() {
        centerPlaces = new ArrayList<>();
        // Initialize centerPlaces data
    }

    public List<CenterPlace> searchAndSort(String query, double popularityWeight, double ratingWeight) {
        List<CenterPlace> results = new ArrayList<>();
        for (CenterPlace centerPlace : centerPlaces) {
            if (centerPlace.getName().contains(query) ||
                    centerPlace.getCategory().contains(query) ||
                    containsKeyword(centerPlace, query)) {
                results.add(centerPlace);
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

    private boolean containsKeyword(CenterPlace centerPlace, String query) {
        for (String keyword : centerPlace.getKeywords()) {
            if (keyword.contains(query)) {
                return true;
            }
        }
        return false;
    }
}
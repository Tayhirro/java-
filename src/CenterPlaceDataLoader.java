import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CenterPlaceDataLoader {
    private List<CenterPlace> centerPlaces;

    public CenterPlaceDataLoader(String filename) {
        centerPlaces = new ArrayList<>();
        loadCenterPlacesFromFile(filename);
    }

    private void loadCenterPlacesFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String[] centerPlaceData = scanner.nextLine().split(",");
                String name = centerPlaceData[0];
                String category = centerPlaceData[1];
                List<String> features = List.of(centerPlaceData[2].split(" "));
                double popularity = Double.parseDouble(centerPlaceData[3]);
                double rating = Double.parseDouble(centerPlaceData[4]);
                CenterPlace centerPlace = new CenterPlace(name, category, features, popularity, rating, features);
                centerPlaces.add(centerPlace);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<CenterPlace> getCenterPlaces() {
        return centerPlaces;
    }
}
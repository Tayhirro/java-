import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 这个类负责从文件中加载中心地点数据。
 * 它逐行读取文件，并为每一行创建一个CenterPlace对象。
 * 创建的CenterPlace对象存储在一个列表中。
 */
public class CenterPlaceDataLoader {
    // 存储CenterPlace对象的列表
    private List<CenterPlace> centerPlaces;

    /**
     * CenterPlaceDataLoader类的构造函数。
     * 它初始化centerPlaces列表，并调用方法从文件中加载数据。
     * @param filename 从中加载中心地点数据的文件的名称。
     */
    public CenterPlaceDataLoader(String filename) {
        centerPlaces = new ArrayList<>();
        loadCenterPlacesFromFile(filename);
    }

    /**
     * 此方法从指定的文件中加载中心地点数据。
     * 它逐行读取文件，将每一行分割成不同的属性，
     * 为每一行创建一个CenterPlace对象，并将其添加到centerPlaces列表中。
     * @param filename 从中加载中心地点数据的文件的名称。
     */
    private void loadCenterPlacesFromFile(String filename) {
        try (Scanner scanner = new Scanner(new File(filename),"UTF-8")) {
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

    /**
     * 此方法返回CenterPlace对象的列表。
     * @return CenterPlace对象的列表。
     */
    public List<CenterPlace> getCenterPlaces() {
        return centerPlaces;
    }
}
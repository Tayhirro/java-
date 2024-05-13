import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ����ฺ����ļ��м������ĵص����ݡ�
 * �����ж�ȡ�ļ�����Ϊÿһ�д���һ��CenterPlace����
 * ������CenterPlace����洢��һ���б��С�
 */
public class CenterPlaceDataLoader {
    // �洢CenterPlace������б�
    private List<CenterPlace> centerPlaces;

    /**
     * CenterPlaceDataLoader��Ĺ��캯����
     * ����ʼ��centerPlaces�б������÷������ļ��м������ݡ�
     * @param filename ���м������ĵص����ݵ��ļ������ơ�
     */
    public CenterPlaceDataLoader(String filename) {
        centerPlaces = new ArrayList<>();
        loadCenterPlacesFromFile(filename);
    }

    /**
     * �˷�����ָ�����ļ��м������ĵص����ݡ�
     * �����ж�ȡ�ļ�����ÿһ�зָ�ɲ�ͬ�����ԣ�
     * Ϊÿһ�д���һ��CenterPlace���󣬲�������ӵ�centerPlaces�б��С�
     * @param filename ���м������ĵص����ݵ��ļ������ơ�
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
     * �˷�������CenterPlace������б�
     * @return CenterPlace������б�
     */
    public List<CenterPlace> getCenterPlaces() {
        return centerPlaces;
    }
}
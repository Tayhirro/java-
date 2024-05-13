import java.util.*;

/**
 * SearchSystem �࣬�����������������ĵص㡣
 */
public class SearchSystem {
    private List<CenterPlace> centerPlaces; // ���ĵص��б�

    /**
     * SearchSystem ��Ĺ��캯����
     * @param dataLoader ���ݼ����������ڴ��ļ��м������ĵص����ݡ�
     */
    
    public SearchSystem(CenterPlaceDataLoader dataLoader) {
        centerPlaces = dataLoader.getCenterPlaces(); // ��ʼ�����ĵص�����
    }

    /**
     * ���ݲ�ѯ�ؼ��ʡ��ȶ�Ȩ�غ�����Ȩ���������������ĵص㡣
     * @param query ��ѯ�ؼ���
     * @param popularityWeight �ȶ�Ȩ��
     * @param ratingWeight ����Ȩ��
     * @return ���������������ĵص��б�
     */
    public List<CenterPlace> searchAndSort(String query, double popularityWeight, double ratingWeight) {
        List<CenterPlace> results = new ArrayList<>();
        for (CenterPlace centerPlace : centerPlaces) {
            if (centerPlace.getName().contains(query) ||
                    centerPlace.getCategory().contains(query) ||
                    containsKeyword(centerPlace, query)) {
                results.add(centerPlace);
            }
        }

        // �����ȶȺ����ֶԽ����������
        Collections.sort(results, new Comparator<CenterPlace>() {
            @Override
            public int compare(CenterPlace c1, CenterPlace c2) {
                double score1 = popularityWeight * c1.getPopularity() + ratingWeight * c1.getRating();
                double score2 = popularityWeight * c2.getPopularity() + ratingWeight * c2.getRating();
                return Double.compare(score2, score1); // ��������
            }
        });

        return results;
    }

    /**
     * ������ĵص�Ĺؼ����Ƿ������ѯ�ؼ��ʡ�
     * @param centerPlace ���ĵص�
     * @param query ��ѯ�ؼ���
     * @return ������ĵص�Ĺؼ��ʰ�����ѯ�ؼ��ʣ��򷵻� true�����򷵻� false��
     */
    private boolean containsKeyword(CenterPlace centerPlace, String query) {
        for (String keyword : centerPlace.getKeywords()) {
            if (keyword.contains(query)) {
                return true;
            }
        }
        return false;
    }
}
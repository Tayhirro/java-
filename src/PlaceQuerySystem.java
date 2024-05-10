import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PlaceQuerySystem {
    // ͼ��������ͱߣ�ʹ���ڽӱ�洢
    final int MAX_POINT = 1000;
    final int MAX_EDGE = 5000;
    int pointNum = 0;// �������


    MyPoint[] points = new MyPoint[MAX_POINT];// ���

    // �ߵ��ڽӱ�
    int[] heads = new int[MAX_POINT];// ͷָ��
    MyEdge[] edges = new MyEdge[MAX_EDGE];// �߱�
    int edgeNum = 0;// �ߵ�����


    public PlaceQuerySystem() {
        loadFromFile();
    }


    void addPoint(String name, int x, int y, String type) {
        points[pointNum] = new MyPoint(name, x, y, type);
        heads[pointNum] = -1;
        pointNum++;
    }

    void addEdge(int from, int to, double length, double crowding) {
        edges[edgeNum] = new MyEdge(from, to, heads[from],length, crowding);
        heads[from] = edgeNum;
        edgeNum++;
    }

    // ��ȡ��ı��
    int getPointId(String name) {
        for (int i = 0; i < pointNum; i++) {
            if (points[i].name.equals(name)) {
                return i;
            }
        }
        return -1;
    }

    void loadFromFile() {
        try {
            // Load points
            Scanner scanner = new Scanner(new File("points.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                addPoint(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3]);
            }
            scanner.close();

            // Load edges
            scanner = new Scanner(new File("edges.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                addEdge(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("points.txt or edges.txt not found.");
            e.printStackTrace();
        }
    }

    int PlaceQuery(int position, String place, boolean isStraightDistance, String[] ansPlaces, double[] ansDistances) {
       if(isStraightDistance)
           return PlaceQueryOfStraightDistance(position, place, ansPlaces, ansDistances);
       else
           return PlaceQueryOfEdgeDistance(position, place, ansPlaces, ansDistances);
    }

    int PlaceQueryOfStraightDistance(int position, String place, String[] ansPlaces, double[] ansDistances) {
        // �������е㵽 position ��ľ���
        double[] distance=new double[pointNum];
        for(int i=0;i<pointNum;i++)
        {
            distance[i]=Math.sqrt((points[i].x-points[position].x)*(points[i].x-points[position].x)+(points[i].y-points[position].y)*(points[i].y-points[position].y));
        }
        DoubleQuickSort.quickSort(distance,0,pointNum-1);
        

        if(place.isEmpty()){
            for(int i=0;i<pointNum;i++)
            {
                ansPlaces[i]=points[i].name;
                ansDistances[i]=distance[i];
            }
        }
        else{
            for(int i=0;i<pointNum;i++)
            {
                if(KMP.kmpMatch(points[i].name,place)!=-1)
                {
                    ansPlaces[0]=points[i].name;
                    ansDistances[0]=distance[i];
                    return 1;
                }
            }
        }
        return pointNum;
    }


    int PlaceQueryOfEdgeDistance(int position, String place, String[] ansPlaces, double[] ansDistances) {
        double scale = 0.65;
        // ʹ��dij�������е㵽 position ��ľ���
        double[] distance=new double[pointNum];

        boolean[] visited = new boolean[pointNum];
        for (int i = 0; i < pointNum; i++) {
            distance[i] = Double.MAX_VALUE;
            visited[i] = false;
        }
        distance[position] = 0;
        for(int i=1;i<pointNum;i++){//ѭ��n-1��,ÿ���ҵ�һ�����·��
            double mindis = Double.MAX_VALUE;//�ҵ�δ���ʵĵ��о�����С�ĵ�ľ���
            int updateId = -1;//���µĵ�ı��
            for(int nextEdgeId=heads[position];nextEdgeId!=-1;nextEdgeId=edges[nextEdgeId].nextId){
                if(!visited[nextEdgeId] && distance[nextEdgeId]<mindis){
                    mindis = distance[nextEdgeId];
                    updateId = nextEdgeId;
                }
            }
            if(updateId==-1){
                break;
            }
            visited[updateId] = true;
            for(int nextEdgeId=heads[updateId];nextEdgeId!=-1;nextEdgeId=edges[nextEdgeId].nextId){
                if(!visited[nextEdgeId] && distance[updateId]+edges[nextEdgeId].length<distance[nextEdgeId]){
                    distance[nextEdgeId] = distance[updateId]+edges[nextEdgeId].length;
                }
            }
        }

        DoubleQuickSort.quickSort(distance,0,pointNum-1);


        if(place.isEmpty()){
            for(int i=0;i<pointNum;i++)
            {
                ansPlaces[i]=points[i].name;
                ansDistances[i]=distance[i]*scale;
            }
        }
        else{
            for(int i=0;i<pointNum;i++)
            {
                if(KMP.kmpMatch(points[i].name,place)!=-1)
                {
                    ansPlaces[0]=points[i].name;
                    ansDistances[0]=distance[i]*scale;
                    return 1;
                }
            }
        }
        return pointNum;
    }

}

class DoubleQuickSort {

    // ����������
    public static void quickSort(double[] arr, int low, int high) {
        if (low < high) {
            // ��ȡ������
            int pi = partition(arr, low, high);

            // �ݹ�ضԷ����������Ҳ���������������
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    // ��������������ȷ�������㲢������������
    public static int partition(double[] arr, int low, int high) {
        double pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;

                // ���� arr[i] �� arr[j]
                double temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // ��������ŵ���ȷ��λ����
        double temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

}

class KMP {

    // ����ǰ׺���飨������ƥ���
    private static int[] computePrefixArray(String pattern) {
        int[] prefixArray = new int[pattern.length()];
        prefixArray[0] = 0;
        int j = 0;
        for (int i = 1; i < pattern.length(); i++) {
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = prefixArray[j - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            prefixArray[i] = j;
        }
        return prefixArray;
    }

    // KMP ƥ�亯��
    public static int kmpMatch(String text, String pattern) {
        int[] prefixArray = computePrefixArray(pattern);
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = prefixArray[j - 1];
            }
            if (text.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            if (j == pattern.length()) {
                return i - j + 1; // ����ƥ�����ʼ����
            }
        }
        return -1; // û���ҵ�ƥ����Ӵ�
    }

    
}
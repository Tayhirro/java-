import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PlaceQuerySystem {
    // 图，包含点和边，使用邻接表存储
    final int MAX_POINT = 1000;
    final int MAX_EDGE = 5000;
    int pointNum = 0;// 点的数量


    MyPoint[] points = new MyPoint[MAX_POINT];// 点表

    // 边的邻接表
    int[] heads = new int[MAX_POINT];// 头指针
    MyEdge[] edges = new MyEdge[MAX_EDGE];// 边表
    int edgeNum = 0;// 边的数量


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

    // 获取点的编号
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
        // 计算所有点到 position 点的距离
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
        // 使用dij计算所有点到 position 点的距离
        double[] distance=new double[pointNum];

        boolean[] visited = new boolean[pointNum];
        for (int i = 0; i < pointNum; i++) {
            distance[i] = Double.MAX_VALUE;
            visited[i] = false;
        }
        distance[position] = 0;
        for(int i=1;i<pointNum;i++){//循环n-1次,每次找到一个最短路径
            double mindis = Double.MAX_VALUE;//找到未访问的点中距离最小的点的距离
            int updateId = -1;//更新的点的编号
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

    // 快速排序函数
    public static void quickSort(double[] arr, int low, int high) {
        if (low < high) {
            // 获取分区点
            int pi = partition(arr, low, high);

            // 递归地对分区点左侧和右侧的子数组进行排序
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    // 分区函数，用于确定分区点并重新排列数组
    public static int partition(double[] arr, int low, int high) {
        double pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;

                // 交换 arr[i] 和 arr[j]
                double temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // 将分区点放到正确的位置上
        double temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

}

class KMP {

    // 计算前缀数组（即部分匹配表）
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

    // KMP 匹配函数
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
                return i - j + 1; // 返回匹配的起始索引
            }
        }
        return -1; // 没有找到匹配的子串
    }

    
}
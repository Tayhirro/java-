import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LocationQuerySystem {
    // 图，包含点和边，使用邻接表存储
    final int MAX_POINT = 1000;
    final int MAX_EDGE = 5000;
    int pointNum = 0;// 点的数量


    MyPoint[] point = new MyPoint[MAX_POINT];// 点表

    // 边的邻接表
    int[] heads = new int[MAX_POINT];// 头指针
    MyEdge[] edge = new MyEdge[MAX_EDGE];// 边表
    int edgeNum = 0;// 边的数量


    public LocationQuerySystem() {
        loadFromFile();
    }


    void addPoint(String name, int x, int y, String type) {
        point[pointNum] = new MyPoint(name, x, y, type);
        heads[pointNum] = -1;
        pointNum++;
    }

    void addEdge(int from, int to, double length, double crowding) {
        edge[edgeNum] = new MyEdge(from, to, heads[from],length, crowding);
        heads[from] = edgeNum;
        edgeNum++;
    }

    // 获取点的编号
    int getPointId(String name) {
        for (int i = 0; i < pointNum; i++) {
            if (point[i].name.equals(name)) {
                return i;
            }
        }
        return -1;
    }

    void loadFromFile() {
        try {
            // Load point
            Scanner scanner = new Scanner(new File("point.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                addPoint(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3]);
            }
            scanner.close();

            // Load edge
            scanner = new Scanner(new File("edge.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                addEdge(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("point.txt or edge.txt not found.");
            e.printStackTrace();
        }
    }

    int LocationQuery(int position, String Location, boolean isStraightDistance, String[] ansLocations, double[] ansDistances) {
       if(isStraightDistance)
           return LocationQueryOfStraightDistance(position, Location, ansLocations, ansDistances);
       else
           return LocationQueryOfEdgeDistance(position, Location, ansLocations, ansDistances);
    }

    int LocationQueryOfStraightDistance(int position, String Location, String[] ansLocations, double[] ansDistances) {
        // 计算所有点到 position 点的距离
        double[] distance=new double[pointNum];
        for(int i=0;i<pointNum;i++)
        {
            distance[i]=Math.sqrt((point[i].x-point[position].x)*(point[i].x-point[position].x)+(point[i].y-point[position].y)*(point[i].y-point[position].y));
        }
        DoubleQuickSort.quickSort(distance,0,pointNum-1);
        

        if(Location.isEmpty()){
            for(int i=0;i<pointNum;i++)
            {
                ansLocations[i]=point[i].name;
                ansDistances[i]=distance[i];
            }
            return pointNum;
        }
        else{
            int count=0;
            for(int i=0;i<pointNum;i++)
            {
                if(KMP.kmpMatch(point[i].name,Location)!=-1)
                {
                    ansLocations[count]=point[i].name;
                    ansDistances[count]=distance[i];
                    count++;
                }
            }
            return count;
        }

    }

    int LocationQueryOfEdgeDistance(int position, String Location, String[] ansLocations, double[] ansDistances) {
        double scale = 0.65;
        // 使用dij计算所有点到 position 点的距离
        //迪杰斯特拉算法
        double []dist = new double[pointNum];
        boolean []visited = new boolean[pointNum];
        for (int i = 0; i < pointNum; i++) {
            dist[i] = Double.MAX_VALUE;
            visited[i] = false;
        }
        dist[position] = 0;
        for(int i=1;i<pointNum;i++){//循环n-1次,每次找到一个最短路径
            double mindis = Double.MAX_VALUE;//找到未访问的点中距离最小的点的距离
            int updateId = -1;
            for(int j=0;j<pointNum;j++){//找到最近的点
                if(!visited[j] && dist[j]<mindis){
                    mindis = dist[j];
                    updateId = j;
                }
            }
            if(updateId==-1){//找不到未访问的点
                break;
            }
            visited[updateId] = true;
            for(int u=heads[updateId];u!=-1;u=edge[u].nextId){// 根据updateId更新其他点的距离
                if(!visited[edge[u].to] && dist[updateId]+edge[u].length<dist[edge[u].to]){
                    dist[edge[u].to] = dist[updateId]+edge[u].length;
                }
            }
        }

        DoubleQuickSort.quickSort(dist,0,pointNum-1);


        if(Location.isEmpty()){
            for(int i=0;i<pointNum;i++)
            {
                ansLocations[i]=point[i].name;
                ansDistances[i]=dist[i]*scale;
            }
            return pointNum;
        }
        else{
            int count=0;
            for(int i=0;i<pointNum;i++)
            {
                if(KMP.kmpMatch(point[i].name,Location)!=-1)
                {
                    ansLocations[count]=point[i].name;
                    ansDistances[count]=dist[i]*scale;
                    count++;
                }
            }
            return count;
        }

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
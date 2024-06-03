
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
        edge[edgeNum] = new MyEdge(from, to, heads[from], length, crowding);
        heads[from] = edgeNum;
        edgeNum++;
        edge[edgeNum] = new MyEdge(to, from, heads[to], length, crowding);
        heads[to] = edgeNum;
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
            Scanner scanner = new Scanner(new File("data\\point.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                addPoint(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3]);
            }
            scanner.close();

            // Load edge
            scanner = new Scanner(new File("data\\edge.txt"));
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
        System.out.println("LocationQuerySystem Load data successfully.");
        System.out.println("Point number: " + pointNum);
        for (int i = 0; i < pointNum; i++) {
            System.out.println(point[i].name + " " + point[i].x + " " + point[i].y + " " + point[i].type);
        }
        System.out.println("Edge number: " + edgeNum);
        for (int i = 0; i < edgeNum; i++) {
            System.out.println(edge[i].from + " " + edge[i].to + " " + edge[i].length + " " + edge[i].crowding);
        }

    }

    int initData(String[] locations, String[] type, double[] distances) {

        int count = 0;
        for (int i = 0; i < pointNum; i++) {
            if (!point[i].type.equals("道路")) {
                locations[count] = point[i].name;
                type[count] = point[i].type;
                distances[count] = -1.00;

                count++;
            }
        }
        System.out.println("initData count: " + count);
        for (int i = 0; i < count; i++) {
            System.out.println(locations[i] + " " + type[i] + " " + distances[i]);

        }

        return count;
    }

    int LocationQuery(int fromid, String query, String range, String category, String[] ansLocations, String[] ansTypes, double[] ansDistances) {
        double scale = 0.65;// 比例尺
        // 使用dij计算所有点到 fromid 点的距离
        //迪杰斯特拉算法
        System.out.println("from: " + fromid + " query: " + query + " range: " + range + " category: " + category);

        Distwithid distwithid[] = new Distwithid[MAX_POINT];
        boolean[] visited = new boolean[MAX_POINT];
        for (int i = 0; i < pointNum; i++) {
            distwithid[i] = new Distwithid(i, Double.MAX_VALUE);
            visited[i] = false;
        }

        distwithid[fromid].dist = 0;
        for (int i = 1; i < pointNum; i++) {//循环n-1次,每次找到一个最短路径
            double mindis = Double.MAX_VALUE;//找到未访问的点中距离最小的点的距离
            int updateId = -2;
            for (int j = 0; j < pointNum; j++) {//找到最近的点
                if (!visited[j] && distwithid[j].dist < mindis) {
                    mindis = distwithid[j].dist;
                    updateId = j;
                }
            }
            if (updateId == -2) {//找不到未访问的点
                break;
            }
            visited[updateId] = true;
            for (int u = heads[updateId]; u != -1; u = edge[u].nextId) {// 根据updateId更新其他点的距离
                if (!visited[edge[u].to] && distwithid[updateId].dist + edge[u].length < distwithid[edge[u].to].dist) {
                    distwithid[edge[u].to].dist = distwithid[updateId].dist + edge[u].length;
                }
            }
        }

        DoubleQuickSort.quickSort(distwithid, 0, pointNum - 1);
        int count = 0;
        double rangeDouble = -1;
        //range是100米,要转为double
        if (range.equals("100米")) {
            rangeDouble = 100;
        }
        if (range.equals("200米")) {
            rangeDouble = 200;
        }
        if (range.equals("500米")) {
            rangeDouble = 500;
        }
        if (range.equals("1000米")) {
            rangeDouble = 1000;
        }
        if (range.equals("2000米")) {
            rangeDouble = 2000;
        }

        for (int i = 0; i < pointNum; i++) {
            if (rangeDouble != -1 && distwithid[i].dist > rangeDouble) {
                break;
            }
            if (KMP.kmpMatch(point[distwithid[i].id].name, query) != -1 && (category.equals("所有") || point[distwithid[i].id].type.equals(category))) {
                if ((point[distwithid[i].id].type.equals("道路"))) {
                    continue;
                }
                if (distwithid[i].dist == 0) {
                    continue;
                }
                ansLocations[count] = point[distwithid[i].id].name;
                ansDistances[count] = distwithid[i].dist * scale;//保留两位小数
                ansDistances[count] = (double) Math.round(ansDistances[count] * 100) / 100;
                ansTypes[count] = point[distwithid[i].id].type;
                System.out.println(ansLocations[count] + " " + ansTypes[count] + " " + ansDistances[count]);
                count++;
            }
        }
        return count;
    }

    int LocationQuery(String Location, String category, String[] ansLocations, String[] ansTypes, double[] ansDistances) {

        int count = 0;
        for (int i = 0; i < pointNum; i++) {
            if (KMP.kmpMatch(point[i].name, Location) != -1 && ((category.equals("所有") && !point[i].type.equals("道路")) || point[i].type.equals(category))) {
                ansLocations[count] = point[i].name;
                ansDistances[count] = -1.00;

                ansTypes[count] = point[i].type;
                count++;
            }
        }

        System.out.println("LocationQuery count: " + count);
        for (int i = 0; i < count; i++) {
            System.out.println(ansLocations[i] + " " + ansTypes[i] + " " + ansDistances[i]);
        }
        return count;
    }

}

class Distwithid {

    int id;
    double dist;

    public Distwithid(int id, double dist) {
        this.id = id;
        this.dist = dist;
    }
}

class DoubleQuickSort {

    // 快速排序函数
    public static void quickSort(Distwithid[] arr, int low, int high) {
        if (low < high) {
            // 获取分区点
            int pi = partition(arr, low, high);

            // 递归地对分区点左侧和右侧的子数组进行排序
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    // 分区函数，用于确定分区点并重新排列数组
    public static int partition(Distwithid[] arr, int low, int high) {
        double pivot = arr[high].dist;
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j].dist < pivot) {
                i++;

                // 交换 arr[i] 和 arr[j]
                Distwithid temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // 将分区点放到正确的位置上
        Distwithid temp = arr[i + 1];
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
        if (pattern.length() == 0) {
            return 0; // 空字符串是任何字符串的子串
        }
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

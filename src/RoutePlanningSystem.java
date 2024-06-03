
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

// 点类
class MyPoint {

    String name; // 点的名字
    int x; // 点的坐标
    int y; // 点的坐标
    String type; // 点的类型

    public MyPoint(String name, int x, int y, String type) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.type = type;
    }

}

// 边
class MyEdge {

    int from;// 边起点的点编号
    int to;// 边终点的点编号
    int nextId;// 下一条边的边编号,用于邻接表

    // 边的属性
    double length;// 边的长度,像素点长度，比例为1m:0.65像素,用于计算长度,场所查询
    double crowding;// 拥挤度,0-1,1表示无拥挤,0表示很拥挤，用于计算时间，time=(length)/(speed*crowding)

    public MyEdge(int from, int to, int nextId, double length, double crowding) {
        this.from = from;
        this.to = to;
        this.nextId = nextId;
        this.length = length;
        this.crowding = crowding;
    }

}

public class RoutePlanningSystem {

    // 图，包含点和边，使用邻接表存储
    final int MAX_POINT = 1000;
    final int MAX_EDGE = 5000;
    int pointNum = 0;// 点的数量

    MyPoint[] point = new MyPoint[MAX_POINT];// 点表

    // 三种边的邻接表
    int[] sidewalkHeads = new int[MAX_POINT];// 头指针
    MyEdge[] sidewalk = new MyEdge[MAX_EDGE];// 边表
    int sidewalkEdgeNum = 0;
    int[] cyclewayHeads = new int[MAX_POINT];// 头指针
    MyEdge[] cycleway = new MyEdge[MAX_EDGE];// 边表
    int cyclewayEdgeNum = 0;
    int[] roadHeads = new int[MAX_POINT];// 头指针
    MyEdge[] road = new MyEdge[MAX_EDGE];// 边表
    int roadEdgeNum = 0;

    public RoutePlanningSystem() {
        loadFromFile();
    }

    void addPoint(String name, int x, int y, String type) {
        point[pointNum] = new MyPoint(name, x, y, type);
        sidewalkHeads[pointNum] = -1;
        cyclewayHeads[pointNum] = -1;
        roadHeads[pointNum] = -1;
        pointNum++;
    }

    void addEdge(int from, int to, double length, double crowding, String type) {
        switch (type) {
            case "sidewalk":
                sidewalk[sidewalkEdgeNum] = new MyEdge(from, to, sidewalkHeads[from], length, crowding);
                sidewalkHeads[from] = sidewalkEdgeNum;
                sidewalkEdgeNum++;
                break;
            case "cycleway":
                cycleway[cyclewayEdgeNum] = new MyEdge(from, to, cyclewayHeads[from], length, crowding);
                cyclewayHeads[from] = cyclewayEdgeNum;
                cyclewayEdgeNum++;
                break;
            case "road":
                road[roadEdgeNum] = new MyEdge(from, to, roadHeads[from], length, crowding);
                roadHeads[from] = roadEdgeNum;
                roadEdgeNum++;
                break;
            default:
                System.out.println("Error: addEdge :unknown type");
        }
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
                addEdge(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), parts[4]);
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // //打印读入的数据
        // System.out.println("point:" + pointNum);
        // for (int i = 0; i < pointNum; i++) {
        //     System.out.println(point[i].name + " " + point[i].x + " " + point[i].y + " " + point[i].type);
        //     System.out.println("sidewalkHeads:" + sidewalkHeads[i] + " cyclewayHeads:" + cyclewayHeads[i] + " roadHeads:" + roadHeads[i]);
        // }
        // System.out.println("sidewalkEdgeNum:" + sidewalkEdgeNum);
        // for (int i = 0; i < sidewalkEdgeNum; i++) {
        //     System.out.println(sidewalk[i].from + " " + sidewalk[i].to + " " + sidewalk[i].length + " " + sidewalk[i].crowding);
        // }

        // System.out.println("cyclewayEdgeNum:" + cyclewayEdgeNum);
        // for (int i = 0; i < cyclewayEdgeNum; i++) {
        //     System.out.println(cycleway[i].from + " " + cycleway[i].to + " " + cycleway[i].length + " " + cycleway[i].crowding);
        // }
        // System.out.println("roadEdgeNum:" + roadEdgeNum);
        // for (int i = 0; i < roadEdgeNum; i++) {
        //     System.out.println(road[i].from + " " + road[i].to + " " + road[i].length + " " + road[i].crowding);
        // }
    }

    //迪杰斯特拉算法,返回最短时间,路径存储在path数组中 time=(length*crowding)/speed，处理函数
    double dijkstra(int startId, int endId, String edgeType, boolean isTime, int[] path) {

        //传入数据初始化
        MyEdge[] edge;
        int[] heads;
        int speed;
        int edgeNum;
        switch (edgeType) {
            case "sidewalk":
                edge = sidewalk;
                heads = sidewalkHeads;
                edgeNum = sidewalkEdgeNum;
                speed = 1;
                break;
            case "cycleway":
                edge = cycleway;
                heads = cyclewayHeads;
                edgeNum = cyclewayEdgeNum;
                speed = 5;
                break;
            case "road":
                edge = road;
                heads = roadHeads;
                edgeNum = roadEdgeNum;
                speed = 10;
                break;
            default:
                System.out.println("Error: dijkstraLength :unknown type");
                return -1;
        }
        double[] weight = new double[edgeNum];
        if (isTime) {
            for (int i = 0; i < edgeNum; i++) {
                weight[i] = edge[i].length / edge[i].crowding / speed;
            }
        } else {
            for (int i = 0; i < edgeNum; i++) {
                weight[i] = edge[i].length;
            }
        }
        double[] dist = new double[pointNum];
        double ret = dijkstra(startId, endId, edge, heads, weight, path, dist);
        return ret;
    }

    //迪杰斯特拉算法
    double dijkstra(int startId, int endId, MyEdge[] edge, int[] heads, double[] weight, int[] path, double[] dist) {

        int[] prev = new int[pointNum];
        boolean[] visited = new boolean[pointNum];
        for (int i = 0; i < pointNum; i++) {
            dist[i] = Double.MAX_VALUE;
            prev[i] = -1;
            visited[i] = false;
        }
        dist[startId] = 0;
        for (int i = 1; i <= pointNum; i++) {//循环n-1次,每次找到一个最短路径
            double mindis = Double.MAX_VALUE;//找到未访问的点中距离最小的点的距离
            int updateId = -2;
            for (int j = 0; j < pointNum; j++) {//找到最近的点
                if (!visited[j] && dist[j] < mindis) {
                    mindis = dist[j];
                    updateId = j;
                }
            }
            if (updateId == -2) {//找不到未访问的点
                break;
            }
            if (updateId == endId) {//找到终点,开启提前退出
                path[0] = 0;

                int[] temppath = new int[pointNum];
                for (int j = endId; j != -1; j = prev[j]) {
                    temppath[path[0]++] = j;
                }

                //倒置path
                for (int j = path[0] - 1; j >= 0; j--) {
                    path[path[0] - j] = temppath[j];
                }
                return dist[endId];
            }

            visited[updateId] = true;
            for (int u = heads[updateId]; u != -1; u = edge[u].nextId) {// 根据updateId更新其他点的距离
                if (!visited[edge[u].to] && dist[updateId] + weight[u] < dist[edge[u].to]) {
                    dist[edge[u].to] = dist[updateId] + weight[u];
                    prev[edge[u].to] = updateId;
                }
            }
        }
        if (endId != -1) {
            System.out.println("warning: dij : there is no path between " + point[startId].name + " and " + point[endId].name);
        }
        return -1;

    }

    //旅行商问题,返回最短路径长度,路径存储在path数组中,预处理函数
    double tsp(int startId, int[] endId, String edgeType, boolean isTime, int[] path) {
        //传入数据预处理
        MyEdge[] edge;
        int[] heads;
        int speed;
        switch (edgeType) {
            case "sidewalk":
                edge = sidewalk;
                heads = sidewalkHeads;
                speed = 1;
                break;
            case "cycleway":
                edge = cycleway;
                heads = cyclewayHeads;
                speed = 5;
                break;
            case "road":
                edge = road;
                heads = roadHeads;
                speed = 10;
                break;
            default:
                System.out.println("Error: tsp :unknown type");
                return -1;
        }
        double[] weight = new double[MAX_POINT];
        if (isTime) {
            for (int i = 0; i < pointNum; i++) {
                weight[i] = edge[i].length / edge[i].crowding / speed;
            }
        } else {
            for (int i = 0; i < pointNum; i++) {
                weight[i] = edge[i].length;
            }
        }
        int cycleNum = endId.length + 1;
        int[] cyclepointId = new int[cycleNum];
        double[][] shortestDist = new double[cycleNum][cycleNum];
        for (int i = 0; i < cycleNum - 1; i++) {
            cyclepointId[i] = endId[i];
        }
        cyclepointId[endId.length] = startId;
        //利用dij计算两点间最短路径，存储在shortestDist中
        for (int cycleI = 0; cycleI < cycleNum; cycleI++) {
            //迪杰斯特拉算法
            double[] dist = new double[pointNum];

            dijkstra(cyclepointId[cycleI], -1, edge, heads, weight, path, dist);

            for (int cycleJ = 0; cycleJ < cycleNum; cycleJ++) {
                shortestDist[cycleI][cycleJ] = dist[cyclepointId[cycleJ]];
            }
        }//√

        int[] shortPath = new int[cycleNum + 1];
        double dist = tsp(cycleNum, shortestDist, shortPath);
        //将编号转换为点的编号
        for (int i = 0; i <= cycleNum; i++) {
            shortPath[i] = cyclepointId[shortPath[i]];
        }
        System.out.println("shortpath:");
        for (int i = 0; i <= cycleNum; i++) {
            System.out.print(shortPath[i] + " ");
        }
        System.out.println();

        //path填充
        int[] segPath = new int[pointNum + 5];
        double[] distTemp = new double[pointNum + 5];
        path[0] = 0;
        path[++path[0]] = shortPath[0];
        for (int i = 0; i < cycleNum; i++) {
            dijkstra(shortPath[i], shortPath[i + 1], edge, heads, weight, segPath, distTemp);

            for (int j = 2; j <= segPath[0]; j++) {
                path[++path[0]] = segPath[j];
            }
        }

        System.err.println("path:" + path[0]);
        for (int i = 1; i <= path[0]; i++) {
            System.err.print(point[path[i]].name + " ");
        }
        System.err.println("tsp:" + dist);
        return dist;
    }

    //动态规划求解旅行商问题,有bug，待修复
    static double tsp(int N, double[][] dist, int[] path) {
        System.out.println("环路节点数:" + N);
        final int M = 1 << (N - 1);
        final double INF = 1e7;
        double[][] dp = new double[N][M];
        int[][] parent = new int[N][M];

        // Initialize DP table
        for (int i = 0; i < N; i++) {
            Arrays.fill(dp[i], INF);
            dp[i][0] = dist[i][0];
        }

        // Dynamic programming to solve TSP
        for (int j = 1; j < M; j++) {
            for (int i = 0; i < N; i++) {
                if (((j >> (i - 1)) & 1) == 1) {
                    continue;
                }
                for (int k = 1; k < N; k++) {
                    if (((j >> (k - 1)) & 1) == 0) {
                        continue;
                    }
                    double cost = dist[i][k] + dp[k][j ^ (1 << (k - 1))];
                    if (dp[i][j] > cost) {
                        dp[i][j] = cost;
                        parent[i][j] = k;
                    }
                }
            }
        }

        // Reconstruct the path
        int last = 0;
        int S = M - 1;
        path[0] = 0;
        for (int i = 1; i < N; i++) {
            int next = parent[last][S];
            path[i] = next;
            S ^= (1 << (next - 1));
            last = next;
        }

        return dp[0][M - 1];
    }

    //判断结点是否都以访问,不包括0号结点
    static boolean isVisited(boolean visited[], int N) {
        for (int i = 1; i < N; i++) {
            if (visited[i] == false) {
                return false;
            }
        }
        return true;
    }

}

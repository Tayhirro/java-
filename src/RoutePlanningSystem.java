import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RoutePlanningSystem {

    // 点的类型，路口，建筑，地铁站，公交站，景点等

    // 点类
    class MyPoint {

        String name; // 点的名字
        double x; // 点的坐标
        double y; // 点的坐标
        String type; // 点的类型

        public MyPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public MyPoint(String name, double x, double y, String type) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.type = type;
        }

    }

    // 边的类型，人行道，自行车道，道路
    enum EdgeType {
        SIDEWALK,
        BIKEWAY,
        ROAD
    }

    // 边
    class MyEdge {

        int from;// 边起点的点编号
        int to;// 边终点的点编号
        int nextId;// 下一条边的边编号

        // 边的属性
        double weight;// 边的宽度，承载能力
        double length;// 边的长度
        double crowding;// 拥挤度
        EdgeType type; // 道路种类

        static final int SIDEWALK = 1;
        static final int BIKEWAY = 2;
        static final int ROAD = 3;

        public MyEdge(int from, int to, int nextId, double weight, double length, double crowding, EdgeType type) {
            this.from = from;
            this.to = to;
            this.nextId = nextId;
            this.weight = weight;
            this.length = length;
            this.crowding = crowding;
            this.type = type;
        }

    }

    // 图，包含点和边，使用邻接表存储
    final int MAX_POINT = 1000;
    final int MAX_EDGE = 50000;
    int pointNum;// 点的数量
    int edgeNum;// 边的数量
    int[] heads = new int[MAX_POINT];// 头指针
    MyEdge[] edges = new MyEdge[MAX_EDGE];// 边表
    MyPoint[] points = new MyPoint[MAX_POINT];// 点表

    public RoutePlanningSystem() {
        pointNum = 0;
        edgeNum = 0;
        for (int i = 0; i < pointNum; i++) {
            heads[i] = -1;
        }
        loadFromFile();
    }

    void addPoint(double x, double y) {
        points[pointNum] = new MyPoint(x, y);
        pointNum++;
    }

    void addPoint(String name, double x, double y, String type) {
        points[pointNum] = new MyPoint(name, x, y, type);
        pointNum++;
    }

    void addEdge(int from, int to, double weight, double length, double crowding, EdgeType type) {
        edges[edgeNum] = new MyEdge(from, to, heads[from], weight, length, crowding, type);
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

    // 获取边的编号
    int getEdgeId(int from, int to) {
        // 遍历from的边
        for (int i = heads[from]; i != -1; i = edges[i].nextId) {
            if (edges[i].to == to) {
                return i;
            }
        }
        return -1;
    }

    // 从文件加载数据
    void loadFromFile() {
        try {
            // Load points
            Scanner pointsScanner = new Scanner(new File("points.txt"));
            while (pointsScanner.hasNextLine()) {
                String line = pointsScanner.nextLine();
                String[] parts = line.split(",");
                String name = parts[0];
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                PointType type = PointType.valueOf(parts[3]);
                addPoint(name, x, y, type);
            }
            pointsScanner.close();

            // Load edges
            Scanner edgesScanner = new Scanner(new File("edges.txt"));
            while (edgesScanner.hasNextLine()) {
                String line = edgesScanner.nextLine();
                String[] parts = line.split(",");
                int from = Integer.parseInt(parts[0]);
                int to = Integer.parseInt(parts[1]);
                double weight = Double.parseDouble(parts[2]);
                double length = Double.parseDouble(parts[3]);
                double crowding = Double.parseDouble(parts[4]);
                EdgeType type = EdgeType.valueOf(parts[5]);
                addEdge(from, to, weight, length, crowding, type);
            }
            edgesScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    // 选择边的类型使用Dijkstra算法,返回最短的length,并记录路径
    double dijkstraLength(int start, int end, EdgeType type, int path[]) {
        double[] dist = new double[pointNum];
        int[] pre = new int[pointNum];
        boolean[] vis = new boolean[pointNum];
        for (int i = 0; i < pointNum; i++) {
            dist[i] = Double.MAX_VALUE;
            pre[i] = -1;
            vis[i] = false;
        }
        dist[start] = 0;
        for (int i = 0; i < pointNum; i++) {
            double minDist = Double.MAX_VALUE;
            int u = -1;
            for (int j = 0; j < pointNum; j++) {
                if (!vis[j] && dist[j] < minDist) {
                    minDist = dist[j];
                    u = j;
                }
            }
            if (u == -1) {
                break;
            }
            vis[u] = true;
            for (int j = heads[u]; j != -1; j = edges[j].nextId) {
                if (edges[j].type == type && dist[u] + edges[j].length < dist[edges[j].to]) {
                    dist[edges[j].to] = dist[u] + edges[j].length;
                    pre[edges[j].to] = u;
                }
            }
        }
        int pathLen = 0;
        int p = end;
        while (p != -1) {
            path[pathLen] = p;
            pathLen++;
            p = pre[p];
        }
        return dist[end];
    }

    // 选择边的类型使用Dijkstra算法,返回最短的 (ValidLength=length / crowding),并记录路径
    double dijkstraValidLength(int start, int end, EdgeType type, int[] path) {
        double[] dist = new double[pointNum];
        int[] pre = new int[pointNum];
        boolean[] vis = new boolean[pointNum];
        for (int i = 0; i < pointNum; i++) {
            dist[i] = Double.MAX_VALUE;
            pre[i] = -1;
            vis[i] = false;
        }
        dist[start] = 0;
        for (int i = 0; i < pointNum; i++) {
            double minDist = Double.MAX_VALUE;
            int u = -1;
            for (int j = 0; j < pointNum; j++) {
                if (!vis[j] && dist[j] < minDist) {
                    minDist = dist[j];
                    u = j;
                }
            }
            if (u == -1) {
                break;
            }
            vis[u] = true;
            for (int j = heads[u]; j != -1; j = edges[j].nextId) {
                if (edges[j].type == type && dist[u] + edges[j].length / edges[j].crowding < dist[edges[j].to]) {
                    dist[edges[j].to] = dist[u] + edges[j].length / edges[j].crowding;
                    pre[edges[j].to] = u;
                }
            }
        }
        int pathLen = 0;
        int p = end;
        while (p != -1) {
            path[pathLen] = p;
            pathLen++;
            p = pre[p];
        }
        return dist[end];
    }

    // 旅行商问题，从start出发，经过所有的点，再回到start，返回最短的length，并记录路径
    double tspLength(int start, int[] ends, EdgeType type, int[] path) {
        double[][] dist = new double[pointNum][pointNum];
        for (int i = 0; i < pointNum; i++) {
            for (int j = 0; j < pointNum; j++) {
                dist[i][j] = Double.MAX_VALUE;
            }
        }
        for (int i = 0; i < edgeNum; i++) {
            if (edges[i].type == type) {
                dist[edges[i].from][edges[i].to] = edges[i].length;
            }
        }
        for (int k = 0; k < pointNum; k++) {
            for (int i = 0; i < pointNum; i++) {
                for (int j = 0; j < pointNum; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
        double minDist = Double.MAX_VALUE;
        int[] perm = new int[pointNum];
        for (int i = 0; i < pointNum; i++) {
            perm[i] = i;
        }
        do {
            double curDist = 0;
            for (int i = 0; i < ends.length - 1; i++) {
                curDist += dist[ends[i]][ends[i + 1]];
            }
            curDist += dist[ends[ends.length - 1]][start];
            if (curDist < minDist) {
                minDist = curDist;
                for (int i = 0; i < ends.length; i++) {
                    path[i] = ends[perm[i]];
                }
            }
        } while (next_permutation(perm, ends.length));
        return minDist;
    }

    // 旅行商问题，从start出发，经过所有的点，再回到start，返回最短的 (ValidLength=length / crowding)，并记录路径
    double tspValidLength(int start, int[] ends, EdgeType type, int[] path) {
        double[][] dist = new double[pointNum][pointNum];
        for (int i = 0; i < pointNum; i++) {
            for (int j = 0; j < pointNum; j++) {
                dist[i][j] = Double.MAX_VALUE;
            }
        }
        for (int i = 0; i < edgeNum; i++) {
            if (edges[i].type == type) {
                dist[edges[i].from][edges[i].to] = edges[i].length / edges[i].crowding;
            }
        }
        for (int k = 0; k < pointNum; k++) {
            for (int i = 0; i < pointNum; i++) {
                for (int j = 0; j < pointNum; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
        double minDist = Double.MAX_VALUE;
        int[] perm = new int[pointNum];
        for (int i = 0; i < pointNum; i++) {
            perm[i] = i;
        }
        do {
            double curDist = 0;
            for (int i = 0; i < ends.length - 1; i++) {
                curDist += dist[ends[i]][ends[i + 1]];
            }
            curDist += dist[ends[ends.length - 1]][start];
            if (curDist < minDist) {
                minDist = curDist;
                for (int i = 0; i < ends.length; i++) {
                    path[i] = ends[perm[i]];
                }
            }
        } while (next_permutation(perm, ends.length));
        return minDist;
    }

}

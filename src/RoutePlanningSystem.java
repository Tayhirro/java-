import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RoutePlanningSystem {

    // 点的类型，路口，建筑，地铁站，公交站，景点等
    enum PointType {
        INTERSECTION,
        BUILDING,
        SUBWAY_STATION,
        BUS_STATION,
        LANDMARK
    }
    // 点类
    class MyPoint {

        String name;    //点的名字
        int x;          //点的坐标
        int y;           //点的坐标
        PointType type; // 点的类型

        public MyPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public MyPoint(String name, int x, int y, PointType type) {
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
    class MyEdge{

        int from;//边起点的点编号
        int to;//边终点的点编号
        int nextId;//下一条边的边编号

        //边的属性
        int weight;//边的宽度，承载能力
        int length;//边的长度
        int crowding;//拥挤度
        EdgeType type; //道路种类

        static final int SIDEWALK = 1;
        static final int BIKEWAY = 2;
        static final int ROAD = 3;

        public MyEdge( int from, int to, int nextId, int weight, int length, int crowding,EdgeType type) {
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
    int pointNum;
    int edgeNum;
    int[] heads = new int[MAX_POINT];//头指针
    MyEdge[] edges = new MyEdge[MAX_EDGE];//边表
    MyPoint[] points = new MyPoint[MAX_POINT];//点表

    public RoutePlanningSystem() {
        pointNum = 0;
        edgeNum = 0;
        for (int i = 0; i < MAX_POINT; i++) {
            heads[i] = -1;
        }
        loadFromFile();
    }

    void addPoint(int x, int y) {
        points[pointNum] = new MyPoint(x, y);
        pointNum++;
    }
    void addPoint(String name, int x, int y, PointType type) {
        points[pointNum] = new MyPoint(name, x, y, type);
        pointNum++;
    }
    void addEdge(int from, int to, int weight, int length, int crowding, EdgeType type) {
        edges[edgeNum] = new MyEdge(from, to, heads[from] , weight, length, crowding, type);
        heads[from] = edgeNum;
        edgeNum++;
    }

    // 获取点的编号
    int getPointId(String name){
        for(int i = 0;i<pointNum;i++){
            if(points[i].name.equals(name)){
                return i;
            }
        }
        return -1;
    }

    // 获取边的编号
    int getEdgeId (int from,int to){
        //遍历from的边
        for(int i = heads[from];i!=-1;i = edges[i].nextId){
            if(edges[i].to == to){
                return i;
            }
        }
        return -1;
    }
    void loadFromFile() {
        try {
            // Load points
            Scanner pointsScanner = new Scanner(new File("points.txt"));
            while (pointsScanner.hasNextLine()) {
                String line = pointsScanner.nextLine();
                String[] parts = line.split(",");
                String name = parts[0];
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
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
                int weight = Integer.parseInt(parts[2]);
                int length = Integer.parseInt(parts[3]);
                int crowding = Integer.parseInt(parts[4]);
                EdgeType type = EdgeType.valueOf(parts[5]);
                addEdge(from, to, weight, length, crowding, type);
            }
            edgesScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    // Dijkstra算法
    void dijkstra(int start, int end) {
        // 初始化
        int[] dist = new int[MAX_POINT];
        int[] prev = new int[MAX_POINT];
        boolean[] visited = new boolean[MAX_POINT];
        for (int i = 0; i < pointNum; i++) {
            dist[i] = Integer.MAX_VALUE;
            prev[i] = -1;
            visited[i] = false;
        }
        dist[start] = 0;

        // 循环
        for (int i = 0; i < pointNum; i++) {
            int minDist = Integer.MAX_VALUE;
            int minIndex = -1;
            for (int j = 0; j < pointNum; j++) {
                if (!visited[j] && dist[j] < minDist) {
                    minDist = dist[j];
                    minIndex = j;
                }
            }
            if (minIndex == -1) {
                break;
            }
            visited[minIndex] = true;
            for (int j = heads[minIndex]; j != -1; j = edges[j].nextId) {
                int to = edges[j].to;
                int newDist = dist[minIndex] + edges[j].length;
                if (newDist < dist[to]) {
                    dist[to] = newDist;
                    prev[to] = minIndex;
                }
            }
        }

        // 输出结果
        if (dist[end] == Integer.MAX_VALUE) {
            System.out.println("无法到达");
        } else {
            System.out.println("最短路径长度: " + dist[end]);
            System.out.print("路径: ");
            int cur = end;
            while (cur != -1) {
                System.out.print(points[cur].name + " ");
                cur = prev[cur];
            }
            System.out.println();
        }
    }



}

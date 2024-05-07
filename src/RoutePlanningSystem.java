import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RoutePlanningSystem {

    // ������ͣ�·�ڣ�����������վ������վ�������
    enum PointType {
        INTERSECTION,
        BUILDING,
        SUBWAY_STATION,
        BUS_STATION,
        LANDMARK
    }
    // ����
    class MyPoint {

        String name;    //�������
        int x;          //�������
        int y;           //�������
        PointType type; // �������

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
    // �ߵ����ͣ����е������г�������·
    enum EdgeType {
        SIDEWALK,
        BIKEWAY,
        ROAD
    }
    // ��
    class MyEdge{

        int from;//�����ĵ���
        int to;//���յ�ĵ���
        int nextId;//��һ���ߵı߱��

        //�ߵ�����
        int weight;//�ߵĿ�ȣ���������
        int length;//�ߵĳ���
        int crowding;//ӵ����
        EdgeType type; //��·����

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

    // ͼ��������ͱߣ�ʹ���ڽӱ�洢
    final int MAX_POINT = 1000;
    final int MAX_EDGE = 50000;
    int pointNum;
    int edgeNum;
    int[] heads = new int[MAX_POINT];//ͷָ��
    MyEdge[] edges = new MyEdge[MAX_EDGE];//�߱�
    MyPoint[] points = new MyPoint[MAX_POINT];//���

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

    // ��ȡ��ı��
    int getPointId(String name){
        for(int i = 0;i<pointNum;i++){
            if(points[i].name.equals(name)){
                return i;
            }
        }
        return -1;
    }

    // ��ȡ�ߵı��
    int getEdgeId (int from,int to){
        //����from�ı�
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

    // Dijkstra�㷨
    void dijkstra(int start, int end) {
        // ��ʼ��
        int[] dist = new int[MAX_POINT];
        int[] prev = new int[MAX_POINT];
        boolean[] visited = new boolean[MAX_POINT];
        for (int i = 0; i < pointNum; i++) {
            dist[i] = Integer.MAX_VALUE;
            prev[i] = -1;
            visited[i] = false;
        }
        dist[start] = 0;

        // ѭ��
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

        // ������
        if (dist[end] == Integer.MAX_VALUE) {
            System.out.println("�޷�����");
        } else {
            System.out.println("���·������: " + dist[end]);
            System.out.print("·��: ");
            int cur = end;
            while (cur != -1) {
                System.out.print(points[cur].name + " ");
                cur = prev[cur];
            }
            System.out.println();
        }
    }



}

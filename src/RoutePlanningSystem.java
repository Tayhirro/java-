import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// ����
class MyPoint {

    String name; // �������
    int x; // �������
    int y; // �������
    String type; // �������

    public MyPoint(String name, int x, int y, String type) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.type = type;
    }

}

// ��
class MyEdge {

    int from;// �����ĵ���
    int to;// ���յ�ĵ���
    int nextId;// ��һ���ߵı߱��,�����ڽӱ�

    // �ߵ�����
    double length;// �ߵĳ���,���ص㳤�ȣ�����Ϊ1m:0.65����,���ڼ��㳤��,������ѯ
    double crowding;// ӵ����,0-1,1��ʾ��ӵ��,0��ʾ��ӵ�������ڼ���ʱ�䣬time=(length)/(speed*crowding)


    public MyEdge(int from, int to, int nextId, double length, double crowding) {
        this.from = from;
        this.to = to;
        this.nextId = nextId;
        this.length = length;
        this.crowding = crowding;
    }

}

public class RoutePlanningSystem {

    // ͼ��������ͱߣ�ʹ���ڽӱ�洢
    final int MAX_POINT = 1000;
    final int MAX_EDGE = 5000;
    int pointNum = 0;// �������


    MyPoint[] point = new MyPoint[MAX_POINT];// ���

    // ���ֱߵ��ڽӱ�
    int[] sidewalkHeads = new int[MAX_POINT];// ͷָ��
    MyEdge[] sidewalk = new MyEdge[MAX_EDGE];// �߱�
    int sidewalkEdgeNum = 0;
    int[] cyclewayHeads = new int[MAX_POINT];// ͷָ��
    MyEdge[] cycleway = new MyEdge[MAX_EDGE];// �߱�
    int cyclewayEdgeNum = 0;
    int[] roadHeads = new int[MAX_POINT];// ͷָ��
    MyEdge[] road = new MyEdge[MAX_EDGE];// �߱�
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

    // ��ȡ��ı��
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
                addEdge(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), parts[4]);
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //�Ͻ�˹�����㷨,�������ʱ��,·���洢��path������ time=(length*crowding)/speed
    double dijkstra(int startId, int endId, String edgeType,boolean isTime,int[] path) {

        //�������ݳ�ʼ��
        MyEdge[] edge;
        int []heads;
        int speed;
        int edgeNum;
        switch (edgeType) {
            case "sidewalk":
                edge = sidewalk;
                heads = sidewalkHeads;
                edgeNum = sidewalkEdgeNum;
                speed=1;
                break;
            case "cycleway":
                edge = cycleway;
                heads = cyclewayHeads;
                edgeNum = cyclewayEdgeNum;
                speed=5;
                break;
            case "road":
                edge = road;
                heads = roadHeads;
                edgeNum = roadEdgeNum;
                speed=10;
                break;
            default:
                System.out.println("Error: dijkstraLength :unknown type");
                return -1;
        }
        double[] weight = new double[edgeNum];
        if(isTime){
            for(int i=0;i<edgeNum;i++){
                weight[i] = edge[i].length/edge[i].crowding/speed;
            }
        }
        else{
            for(int i=0;i<edgeNum;i++){
                weight[i] = edge[i].length;
            }
        }

        //�Ͻ�˹�����㷨
        int []prev = new int[pointNum];
        double []dist = new double[pointNum];
        boolean []visited = new boolean[pointNum];
        for (int i = 0; i < pointNum; i++) {
            dist[i] = Double.MAX_VALUE;
            prev[i] = -1;
            visited[i] = false;
        }
        dist[startId] = 0;
        for(int i=1;i<pointNum;i++){//ѭ��n-1��,ÿ���ҵ�һ�����·��
            double mindis = Double.MAX_VALUE;//�ҵ�δ���ʵĵ��о�����С�ĵ�ľ���
            int updateId = -1;
            for(int j=0;j<pointNum;j++){//�ҵ�����ĵ�
                if(!visited[j] && dist[j]<mindis){
                    mindis = dist[j];
                    updateId = j;
                }
            }
            if(updateId==-1){//�Ҳ���δ���ʵĵ�
                break;
            }
            if(updateId==endId){//�ҵ��յ�
                int pathId = 0;

                System.out.println("path:");
                for(int j=endId;j!=-1;j=prev[j]){
                    path[pathId++] = j;
                    System.out.println(point[j].name);
                }
                System.out.println("dist:"+dist[endId]);
                return dist[endId];
            }
            visited[updateId] = true;
            for(int u=heads[updateId];u!=-1;u=edge[u].nextId){// ����updateId����������ľ���
                if(!visited[edge[u].to] && dist[updateId]+weight[u]<dist[edge[u].to]){
                    dist[edge[u].to] = dist[updateId]+weight[u];
                    prev[edge[u].to] = updateId;
                }
            }
        }
        System.out.println("warning: dij : there is no path");
        return -1;
    }

    //����������,�������·������,·���洢��path������
    double tsp(int startId, int[] endId, String edgeType,boolean isTime, int[] path) {
        //�������ݳ�ʼ��
        MyEdge[] edge;
        int []heads;
        int speed;
        switch (edgeType) {
            case "sidewalk":
                edge = sidewalk;
                heads = sidewalkHeads;
                speed=1;
                break;
            case "cycleway":
                edge = cycleway;
                heads = cyclewayHeads;
                speed=5;
                break;
            case "road":
                edge = road;
                heads = roadHeads;
                speed=10;
                break;
            default:
                System.out.println("Error: tsp :unknown type");
                return -1;
        }
        double[] weight = new double[MAX_POINT];
        if(isTime){
            for(int i=0;i<pointNum;i++){
                weight[i] = edge[i].length/edge[i].crowding/speed;
            }
        }
        else{
            for(int i=0;i<pointNum;i++){
                weight[i] = edge[i].length;
            }
        }




        System.out.println("Error: tsp :return -1");
        return -1;
    }




}

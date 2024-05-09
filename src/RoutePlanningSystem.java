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
    double length;// �ߵĳ���
    double crowding;// ӵ����


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


    MyPoint[] points = new MyPoint[MAX_POINT];// ���

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
        points[pointNum] = new MyPoint(name, x, y, type);
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
                addEdge(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), parts[4]);
            }
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //�Ͻ�˹�����㷨,�������·������,·���洢��path������
    double dijkstraLength(int startId, int endId, String edgeType, int[] path) {


        System.out.println("Error: dijkstraLength :return -1");
        return -1;
    }

    //�Ͻ�˹�����㷨,�������ʱ��,·���洢��path������ time=(length*crowding)/speed
    double dijkstraTime(int startId, int endId, String edgeType, int[] path) {


        System.out.println("Error: dijkstraTime :return -1");
        return -1;
    }

    //����������,�������·������,·���洢��path������
    double tspLength(int[] endId, String edgeType, int[] path) {


        System.out.println("Error: tspLength :return -1");
        return -1;
    }

    //����������,�������ʱ��,·���洢��path������
    double tspTime(int startId, int[] endId, String edgeType, int[] path) {


        System.out.println("Error: tspLength :return -1");
        return -1;
    }


}
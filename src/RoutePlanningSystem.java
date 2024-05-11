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

    //�Ͻ�˹�����㷨,�������ʱ��,·���洢��path������ time=(length*crowding)/speed��������
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
        double []dist = new double[pointNum];
        return dijkstra(startId,endId,edge,heads,weight,path,dist);
    }

    //�Ͻ�˹�����㷨
    double dijkstra(int startId, int endId,MyEdge[] edge, int []heads ,double[] weight,int[] path,double []dist) {

        int []prev = new int[pointNum];
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
            if(updateId==endId){//�ҵ��յ�,������ǰ�˳�
                int pathId = 0;

                int []temppath = new int[pointNum];
                for(int j=endId;j!=-1;j=prev[j]){
                    temppath[pathId++] = j;
                }
                //����path
                for(int j=pathId-1;j>=0;j--){
                    path[pathId-j-1] = temppath[j];
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
        if(endId!=-1)
            System.out.println("warning: dij : there is no path between "+point[startId].name+" and "+point[endId].name);
        return -1;

    }

    //����������,�������·������,·���洢��path������,Ԥ������
    double tsp(int startId, int[] endId, String edgeType,boolean isTime, int[] path) {
        //��������Ԥ����
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
        int cycleNum = endId.length+1;
        int []pointId = new int[cycleNum];
        double [][]shortestDist = new double[cycleNum][cycleNum];
        for(int i=0;i<cycleNum-1;i++){
            pointId[i] = endId[i];
        }
        pointId[endId.length] = startId;
        //����dij������������·�����洢��shortestDist��
        for(int cycleI=0;cycleI<cycleNum;cycleI++) {
            //�Ͻ�˹�����㷨
            double[] dist = new double[pointNum];

            dijkstra(pointId[cycleI],-1,edge,heads,weight,path,dist);

            for(int cycleJ=0;cycleJ<cycleNum;cycleJ++){
                shortestDist[cycleI][cycleJ] = dist[pointId[cycleJ]];
            }
        }
        int []shortPath = new int[cycleNum+1];
        double dist= tsp(cycleNum,shortestDist,shortPath);
        //�����ת��Ϊ��ı��
        for(int i=0;i<=cycleNum;i++){
            shortPath[i]=pointId[shortPath[i]];
        }
        System.out.println("path:");
        for(int i=0;i<=cycleNum;i++){
            System.out.print(point[shortPath[i]].name+" ");
        }
        System.out.println();
        //path���
        int[] segPath=new int[pointNum];
        double[] distTemp=new double[pointNum];
        int pathId=0;
        for(int i=0;i<cycleNum;i++){
            dijkstra(shortPath[i],shortPath[i+1],edge,heads,weight,segPath,distTemp);
            for(int j=0;j<pointNum;j++){
                path[pathId++]=segPath[j];
            }
        }

        return dist;
    }

    //��̬�滮�������������
    static double tsp(int N,double[][] dist,int[] path){
        System.out.println("��·�ڵ���:"+N);
        final int M = 1 << (N - 1);
        final double INF = 1e7;
        double[][] dp = new double[N][M];
        for (int i = 0; i < N; i++) {
            dp[i][0] = dist[i][0];
        }
        //��̬�滮���
        for (int j = 1; j < M; j++) {
            for (int i = 0; i < N; i++) {
                dp[i][j] = INF;
                if (((j >> (i - 1)) & 1) == 1) {
                    continue;
                }
                for (int k = 1; k < N; k++) {
                    if (((j >> (k - 1)) & 1) == 0) {
                        continue;
                    }
                    if (dp[i][j] > dist[i][k] + dp[k][j ^ (1 << (k - 1))]) {
                        dp[i][j] = dist[i][k] + dp[k][j ^ (1 << (k - 1))];
                    }
                }
            }
        }
        //����·��
        boolean[] visited=new boolean[N];
        for(int i=0;i<N;i++){
            visited[i]=false;
        }
        //ǰ���ڵ���
        int pioneer = 0, S = M - 1, temp=0;
        double min = INF;
        int pathId = 0;
        //��������ż�������
        path[pathId++] = 0;

        while (!isVisited(visited,N)) {
            for (int i = 1; i < N; i++) {
                if (visited[i] == false && (S & (1 << (i - 1))) != 0) {
                    if (min > dist[i][pioneer] + dp[i][(S ^ (1 << (i - 1)))]) {
                        min = dist[i][pioneer] + dp[i][(S ^ (1 << (i - 1)))];
                        temp = i;
                    }
                }
            }
            pioneer = temp;
            path[pathId++] = pioneer;
            visited[pioneer] = true;
            S = S ^ (1 << (pioneer - 1));
            min = INF;
        }

        return  dp[0][M - 1];
    }
    //�жϽ���Ƿ��Է���,������0�Ž��
    static boolean isVisited(boolean visited[],int N) {
        for (int i = 1; i < N; i++) {
            if (visited[i] == false) {
                return false;
            }
        }
        return true;
    }


}

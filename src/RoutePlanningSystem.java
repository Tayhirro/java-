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
        int x;          //�������
        int y;           //�������
        PointType type; // �������

        public MyPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public MyPoint(int x, int y, PointType type) {
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
    class MyGraph {
        final int MAX_POINT = 1000;
        final int MAX_EDGE = 50000;
        int pointNum;
        int edgeNum;
        int[] heads = new int[MAX_POINT];//ͷָ��
        MyEdge[] edges = new MyEdge[MAX_EDGE];//�߱�
        MyPoint[] points = new MyPoint[MAX_POINT];//���

        public MyGraph() {
            pointNum = 0;
            edgeNum = 0;
            for (int i = 0; i < MAX_POINT; i++) {
                heads[i] = -1;
            }
        }

        void addPoint(int x, int y) {
            points[pointNum] = new MyPoint(x, y);
            pointNum++;
        }
        void addEdge(int from, int to, int weight, int length, int crowding, EdgeType type) {
            edges[edgeNum] = new MyEdge(from, to, heads[from] , weight, length, crowding, type);
            heads[from] = edgeNum;
            edgeNum++;
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

    }


}

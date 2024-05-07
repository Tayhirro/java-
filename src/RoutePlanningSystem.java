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
        int x;          //点的坐标
        int y;           //点的坐标
        PointType type; // 点的类型

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
    class MyGraph {
        final int MAX_POINT = 1000;
        final int MAX_EDGE = 50000;
        int pointNum;
        int edgeNum;
        int[] heads = new int[MAX_POINT];//头指针
        MyEdge[] edges = new MyEdge[MAX_EDGE];//边表
        MyPoint[] points = new MyPoint[MAX_POINT];//点表

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

    }


}

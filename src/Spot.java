import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// 景区学校类。
public class Spot {

    private final String name;// 名称
    private final String type;// school or attraction
    private long popularity;// 人气
    private double score;// 评分
    private final String location;// 所属地
    private String description;// 描述
    private List<String> tags;// 标签
    private final String[] images;// 图片
    private Spot[] relatedSpots;// 相关景点

    Spot(String name, String type, long popularity, double score, String location, String description, List<String> tags, String[] images, Spot[] relatedSpots) {
        this.name = name;
        this.type = type;
        this.popularity = popularity;
        this.score = score;
        this.location = location;
        this.description = description;
        this.tags = tags;
        this.images = images;
        this.relatedSpots = relatedSpots;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public long getPopularity() {
        return popularity;
    }
    public double getScore() {
        return score;
    }
    public String getLocation() {
        return location;
    }
    public String getDescription() {
        return description;
    }
    public String[] getImages() {
        return images;
    }
    public Spot[] getRelatedSpots() {
        return relatedSpots;
    }
    public List<String> getTags() {
        return this.tags;
    }
}

class SpotManagement {


    private String path;// 地点数据文件
    private ArrayList<Spot> spots; // 地点数据

    public SpotManagement() {
        // 从文件中读取地点信息
        path="data\\spotData.ser";
        spots = new ArrayList<>();
        loadSpotData();
    }

    // 添加地点
    public void addSpot(String name, String type, long popularity, double score, String location, String description, List<String> tags, String[] images, Spot[] relatedSpots) {
        if(name==null||type==null){
            System.out.println("添加地点失败:参数不能为空");
            return;
        }
        for (Spot spot : spots) {
            if (spot.getName().equals(name)) {
                System.out.println("添加地点失败:已经存在具有相同名称的地点");
                return;
            }
        }
        Spot spot=new Spot(name, type, popularity, score, location, description, tags, images, relatedSpots);
        spots.add(spot);
        saveSpotData();
    }

    // 获取地点
    public Spot getSpot(String name) {
        for (Spot spot : spots) {
            if (spot.getName().equals(name)) {
                return spot;
            }
        }
        return null;
    }

    // 获取所有地点
    public ArrayList<Spot> getAllSpots() {
        return spots;
    }

    // 加载地点数据
    private void loadSpotData() {
        spots.add(new Spot("故宫", "attraction", 2000, 4.8, "北京", "故宫是中国著名的景点", Arrays.asList("景点", "古建筑"), new String[]{"source/gugong.jpg"}, new Spot[]{}));
        spots.add(new Spot("颐和园", "attraction", 1500, 4.7, "北京", "颐和园是中国著名的景点", Arrays.asList("景点", "古建筑"), new String[]{"source/yiheyuan.jpg"}, new Spot[]{}));
        spots.add(new Spot("长城", "attraction", 3000, 4.9, "北京", "长城是中国著名的景点", Arrays.asList("景点", "古建筑"), new String[]{"source/changcheng.jpg"}, new Spot[]{}));
        spots.add(new Spot("天安门", "attraction", 2500, 4.8, "北京", "天安门是中国著名的景点", Arrays.asList("景点", "古建筑"), new String[]{"source/tiananmen.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京邮电大学", "school", 1000, 4.5, "北京", "北京邮电大学是中国著名的学府", Arrays.asList("学府", "名校","211"), new String[]{"source/bupt.jpg"}, new Spot[]{spots.get(0)}));
        spots.add(new Spot("清华大学", "school", 3000, 4.9, "北京", "清华大学是中国著名的学府", Arrays.asList("学府", "名校","985"), new String[]{"source/thu.jpg"}, new Spot[]{spots.get(1)}));
        spots.add(new Spot("北京大学", "school", 2500, 4.8, "北京", "北京大学是中国著名的学府", Arrays.asList("学府", "名校","985"), new String[]{"source/pku.jpg"}, new Spot[]{spots.get(2)}));
        try (ObjectInputStream pis = new ObjectInputStream(new FileInputStream(path))) {// 读取地点数据文件
            spots = (ArrayList<Spot>) pis.readObject();// 读取地点数据库
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("没有找到地点数据文件或读取失败：" + e.getMessage());
        }
    }

    private void saveSpotData() {// 保存地点数据
        try (ObjectOutputStream pos = new ObjectOutputStream(new FileOutputStream(path))) {// 写入用户数据文件
            pos.writeObject(spots);// 写入地点数据库
        } catch (IOException e) {
            System.out.println("保存用户数据失败：" + e.getMessage());
        }
    }

    public boolean findSpot(String name) {

        return true;
    }

}

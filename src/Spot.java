import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// 景区学校类。
public class Spot implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
        path="data/spotData.ser";
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
        spots.add(new Spot("八达岭长城", "attraction", 3000, 0.1, "北京", "八达岭长城是中国著名的景点", Arrays.asList("景点", "古建筑"), new String[]{"source/badaling.jpg"}, new Spot[]{}));
        spots.add(new Spot("天安门广场", "attraction", 2500, 4.8, "北京", "天安门广场是中国著名的景点", Arrays.asList("景点", "历史地标"), new String[]{"source/tiananmen.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京邮电大学", "school", 10000, 10, "北京", "北京邮电大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/bupt.jpg"}, new Spot[]{}));
        spots.add(new Spot("清华大学", "school", 3000, 4.9, "北京", "清华大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/thu.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京大学", "school", 2500, 4.8, "北京", "北京大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/pku.jpg"}, new Spot[]{}));
        spots.add(new Spot("圆明园", "attraction", 1700, 4.6, "北京", "圆明园是中国著名的景点", Arrays.asList("景点", "遗址"), new String[]{"source/yuanmingyuan.jpg"}, new Spot[]{}));
        spots.add(new Spot("北海公园", "attraction", 1600, 4.7, "北京", "北海公园是中国著名的景点", Arrays.asList("景点", "公园"), new String[]{"source/beihai.jpg"}, new Spot[]{}));
        spots.add(new Spot("王府井大街", "attraction", 2000, 4.5, "北京", "王府井大街是中国著名的购物街", Arrays.asList("景点", "购物"), new String[]{"source/wangfujing.jpg"}, new Spot[]{}));
        spots.add(new Spot("上海交通大学", "school", 2900, 4.8, "上海", "上海交通大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/shjtu.jpg"}, new Spot[]{}));
        spots.add(new Spot("复旦大学", "school", 2800, 4.8, "上海", "复旦大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/fudan.jpg"}, new Spot[]{}));
        spots.add(new Spot("外滩", "attraction", 2600, 4.9, "上海", "外滩是中国著名的景点", Arrays.asList("景点", "城市风光"), new String[]{"source/waitan.jpg"}, new Spot[]{}));
        spots.add(new Spot("东方明珠", "attraction", 2500, 4.8, "上海", "东方明珠是中国著名的景点", Arrays.asList("景点", "电视塔"), new String[]{"source/dongfangmingzhu.jpg"}, new Spot[]{}));
        spots.add(new Spot("南京路步行街", "attraction", 2300, 4.7, "上海", "南京路步行街是中国著名的购物街", Arrays.asList("景点", "购物"), new String[]{"source/nanjinglu.jpg"}, new Spot[]{}));
        spots.add(new Spot("杭州西湖", "attraction", 2700, 4.9, "浙江", "杭州西湖是中国著名的景点", Arrays.asList("景点", "湖泊"), new String[]{"source/xihu.jpg"}, new Spot[]{}));
        spots.add(new Spot("苏州大学", "school", 2200, 4.7, "江苏", "苏州大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/suda.jpg"}, new Spot[]{}));
        spots.add(new Spot("黄山", "attraction", 2500, 4.8, "安徽", "黄山是中国著名的景点", Arrays.asList("景点", "山"), new String[]{"source/huangshan.jpg"}, new Spot[]{}));
        spots.add(new Spot("南开大学", "school", 2400, 4.8, "天津", "南开大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/nankai.jpg"}, new Spot[]{}));
        spots.add(new Spot("天津大学", "school", 2350, 4.7, "天津", "天津大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/tju.jpg"}, new Spot[]{}));
        spots.add(new Spot("鼓楼", "attraction", 1800, 4.5, "北京", "鼓楼是中国著名的景点", Arrays.asList("景点", "古建筑"), new String[]{"source/gulou.jpg"}, new Spot[]{}));
        spots.add(new Spot("南锣鼓巷", "attraction", 1600, 4.4, "北京", "南锣鼓巷是中国著名的景点", Arrays.asList("景点", "胡同"), new String[]{"source/nanluoguxiang.jpg"}, new Spot[]{}));
        spots.add(new Spot("798艺术区", "attraction", 1900, 4.6, "北京", "798艺术区是中国著名的景点", Arrays.asList("景点", "艺术区"), new String[]{"source/798.jpg"}, new Spot[]{}));
        spots.add(new Spot("西安交通大学", "school", 2500, 4.8, "陕西", "西安交通大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/xjtu.jpg"}, new Spot[]{}));
        spots.add(new Spot("西安电子科技大学", "school", 2300, 4.7, "陕西", "西安电子科技大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/xidian.jpg"}, new Spot[]{}));
        spots.add(new Spot("钟楼", "attraction", 2100, 4.7, "陕西", "钟楼是中国著名的景点", Arrays.asList("景点", "历史地标"), new String[]{"source/zhonglou.jpg"}, new Spot[]{}));
        spots.add(new Spot("大雁塔", "attraction", 2200, 4.8, "陕西", "大雁塔是中国著名的景点", Arrays.asList("景点", "古建筑"), new String[]{"source/dayanta.jpg"}, new Spot[]{}));
        spots.add(new Spot("华山", "attraction", 2400, 4.9, "陕西", "华山是中国著名的景点", Arrays.asList("景点", "山"), new String[]{"source/huashan.jpg"}, new Spot[]{}));
        spots.add(new Spot("南京大学", "school", 2700, 4.8, "江苏", "南京大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/nju.jpg"}, new Spot[]{}));
        spots.add(new Spot("东南大学", "school", 2600, 4.7, "江苏", "东南大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/seu.jpg"}, new Spot[]{}));
        spots.add(new Spot("中山陵", "attraction", 2500, 4.8, "江苏", "中山陵是中国著名的景点", Arrays.asList("景点", "陵墓"), new String[]{"source/zhongshanling.jpg"}, new Spot[]{}));
        spots.add(new Spot("夫子庙", "attraction", 2400, 4.7, "江苏", "夫子庙是中国著名的景点", Arrays.asList("景点", "历史地标"), new String[]{"source/fuzimiao.jpg"}, new Spot[]{}));
        spots.add(new Spot("中山大学", "school", 2800, 4.8, "广东", "中山大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/sysu.jpg"}, new Spot[]{}));
        spots.add(new Spot("广州塔", "attraction", 2600, 4.8, "广东", "广州塔是中国著名的景点", Arrays.asList("景点", "电视塔"), new String[]{"source/canton_tower.jpg"}, new Spot[]{}));
        spots.add(new Spot("越秀公园", "attraction", 2400, 4.7, "广东", "越秀公园是中国著名的景点", Arrays.asList("景点", "公园"), new String[]{"source/yuexiu.jpg"}, new Spot[]{}));
        spots.add(new Spot("长隆野生动物世界", "attraction", 2700, 4.9, "广东", "长隆野生动物世界是中国著名的景点", Arrays.asList("景点", "动物园"), new String[]{"source/chimelong.jpg"}, new Spot[]{}));
        spots.add(new Spot("哈尔滨工业大学", "school", 2700, 4.8, "黑龙江", "哈尔滨工业大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/hit.jpg"}, new Spot[]{}));
        spots.add(new Spot("中央大街", "attraction", 2500, 4.8, "黑龙江", "中央大街是中国著名的景点", Arrays.asList("景点", "购物街"), new String[]{"source/zhongyangdajie.jpg"}, new Spot[]{}));
        spots.add(new Spot("圣索菲亚教堂", "attraction", 2400, 4.7, "黑龙江", "圣索菲亚教堂是中国著名的景点", Arrays.asList("景点", "教堂"), new String[]{"source/saint_sofia.jpg"}, new Spot[]{}));
        spots.add(new Spot("冰雪大世界", "attraction", 2600, 4.9, "黑龙江", "冰雪大世界是中国著名的景点", Arrays.asList("景点", "主题公园"), new String[]{"source/ice_world.jpg"}, new Spot[]{}));
        spots.add(new Spot("海南大学", "school", 2200, 4.7, "海南", "海南大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/hainu.jpg"}, new Spot[]{}));
        spots.add(new Spot("亚龙湾", "attraction", 2500, 4.8, "海南", "亚龙湾是中国著名的景点", Arrays.asList("景点", "海湾"), new String[]{"source/yalongwan.jpg"}, new Spot[]{}));
        spots.add(new Spot("天涯海角", "attraction", 2400, 4.7, "海南", "天涯海角是中国著名的景点", Arrays.asList("景点", "海滩"), new String[]{"source/tianyahaijiao.jpg"}, new Spot[]{}));
        spots.add(new Spot("南京农业大学", "school", 2200, 4.6, "江苏", "南京农业大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/njau.jpg"}, new Spot[]{}));
        spots.add(new Spot("东南大学", "school", 2300, 4.7, "江苏", "东南大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/seu.jpg"}, new Spot[]{}));
        spots.add(new Spot("中国科学技术大学", "school", 2500, 4.8, "安徽", "中国科学技术大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/ustc.jpg"}, new Spot[]{}));
        spots.add(new Spot("芜湖方特欢乐世界", "attraction", 2200, 4.7, "安徽", "芜湖方特欢乐世界是中国著名的景点", Arrays.asList("景点", "主题公园"), new String[]{"source/fangte.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京师范大学", "school", 2400, 4.8, "北京", "北京师范大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/bnu.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京交通大学", "school", 2300, 4.7, "北京", "北京交通大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/bjtu.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京化工大学", "school", 2200, 4.6, "北京", "北京化工大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/buct.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京林业大学", "school", 2100, 4.5, "北京", "北京林业大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/bfu.jpg"}, new Spot[]{}));
        spots.add(new Spot("天津医科大学", "school", 2000, 4.4, "天津", "天津医科大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/tmu.jpg"}, new Spot[]{}));
        spots.add(new Spot("东北大学", "school", 2400, 4.7, "辽宁", "东北大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/neu.jpg"}, new Spot[]{}));
        spots.add(new Spot("大连理工大学", "school", 2500, 4.8, "辽宁", "大连理工大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/dlut.jpg"}, new Spot[]{}));
        spots.add(new Spot("渤海大学", "school", 2200, 4.6, "辽宁", "渤海大学是中国著名的学府", Arrays.asList("学府", "普通高校"), new String[]{"source/bohaiu.jpg"}, new Spot[]{}));
        spots.add(new Spot("辽宁大学", "school", 2100, 4.5, "辽宁", "辽宁大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/lnu.jpg"}, new Spot[]{}));
        spots.add(new Spot("吉林大学", "school", 2500, 4.8, "吉林", "吉林大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/jlu.jpg"}, new Spot[]{}));
        spots.add(new Spot("延边大学", "school", 2100, 4.5, "吉林", "延边大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/ybu.jpg"}, new Spot[]{}));
        spots.add(new Spot("中国人民大学", "school", 2600, 4.8, "北京", "中国人民大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/ruc.jpg"}, new Spot[]{}));
        spots.add(new Spot("中国传媒大学", "school", 2400, 4.7, "北京", "中国传媒大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/cuc.jpg"}, new Spot[]{}));
        spots.add(new Spot("中央音乐学院", "school", 2500, 4.8, "北京", "中央音乐学院是中国著名的学府", Arrays.asList("学府", "名校", "艺术"), new String[]{"source/ccom.jpg"}, new Spot[]{}));
        spots.add(new Spot("中国政法大学", "school", 2300, 4.6, "北京", "中国政法大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/cupl.jpg"}, new Spot[]{}));
        spots.add(new Spot("对外经济贸易大学", "school", 2200, 4.5, "北京", "对外经济贸易大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/uibe.jpg"}, new Spot[]{}));
        spots.add(new Spot("中央财经大学", "school", 2300, 4.7, "北京", "中央财经大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/cufe.jpg"}, new Spot[]{}));
        spots.add(new Spot("中国矿业大学（北京）", "school", 2000, 4.4, "北京", "中国矿业大学（北京）是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/cumtb.jpg"}, new Spot[]{}));
        spots.add(new Spot("中国地质大学（北京）", "school", 2100, 4.5, "北京", "中国地质大学（北京）是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/cugb.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京外国语大学", "school", 2400, 4.7, "北京", "北京外国语大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/bfsu.jpg"}, new Spot[]{}));
        spots.add(new Spot("南京大学", "school", 2700, 4.8, "江苏", "南京大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/nju.jpg"}, new Spot[]{}));
        spots.add(new Spot("南京师范大学", "school", 2200, 4.6, "江苏", "南京师范大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/njnu.jpg"}, new Spot[]{}));
        spots.add(new Spot("湖南大学", "school", 2400, 4.7, "湖南", "湖南大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/hnu.jpg"}, new Spot[]{}));
        spots.add(new Spot("中国矿业大学（徐州）", "school", 2000, 4.4, "江苏", "中国矿业大学（徐州）是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/cumt.jpg"}, new Spot[]{}));
        spots.add(new Spot("南京航空航天大学", "school", 2400, 4.7, "江苏", "南京航空航天大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/nuaa.jpg"}, new Spot[]{}));
        spots.add(new Spot("南京理工大学", "school", 2300, 4.6, "江苏", "南京理工大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/njust.jpg"}, new Spot[]{}));
        spots.add(new Spot("合肥工业大学", "school", 2200, 4.5, "安徽", "合肥工业大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/hfut.jpg"}, new Spot[]{}));
        spots.add(new Spot("华中科技大学", "school", 2500, 4.8, "湖北", "华中科技大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/hust.jpg"}, new Spot[]{}));
        spots.add(new Spot("华中师范大学", "school", 2300, 4.7, "湖北", "华中师范大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/ccnu.jpg"}, new Spot[]{}));
        spots.add(new Spot("武汉理工大学", "school", 2200, 4.6, "湖北", "武汉理工大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/wut.jpg"}, new Spot[]{}));
        spots.add(new Spot("武汉大学", "school", 2600, 4.8, "湖北", "武汉大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/whu.jpg"}, new Spot[]{}));
        spots.add(new Spot("西南大学", "school", 2300, 4.7, "重庆", "西南大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/swu.jpg"}, new Spot[]{}));
        spots.add(new Spot("西南财经大学", "school", 2200, 4.6, "四川", "西南财经大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/swufe.jpg"}, new Spot[]{}));
        spots.add(new Spot("电子科技大学", "school", 2400, 4.7, "四川", "电子科技大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/uestc.jpg"}, new Spot[]{}));
        spots.add(new Spot("四川农业大学", "school", 2000, 4.4, "四川", "四川农业大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/sicau.jpg"}, new Spot[]{}));
        spots.add(new Spot("四川大学", "school", 2600, 4.8, "四川", "四川大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/scu.jpg"}, new Spot[]{}));
        spots.add(new Spot("中南财经政法大学", "school", 2200, 4.6, "湖北", "中南财经政法大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/znufe.jpg"}, new Spot[]{}));
        spots.add(new Spot("中南大学", "school", 2500, 4.8, "湖南", "中南大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/csu.jpg"}, new Spot[]{}));
        spots.add(new Spot("湖南师范大学", "school", 2100, 4.5, "湖南", "湖南师范大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/hunnu.jpg"}, new Spot[]{}));
        spots.add(new Spot("广西大学", "school", 2000, 4.4, "广西", "广西大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/gxu.jpg"}, new Spot[]{}));
        spots.add(new Spot("郑州大学", "school", 2400, 4.7, "河南", "郑州大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/zzu.jpg"}, new Spot[]{}));
        spots.add(new Spot("中国地质大学（武汉）", "school", 2000, 4.4, "湖北", "中国地质大学（武汉）是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/cugw.jpg"}, new Spot[]{}));
        spots.add(new Spot("重庆大学", "school", 2500, 4.8, "重庆", "重庆大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/cqu.jpg"}, new Spot[]{}));
        spots.add(new Spot("厦门大学", "school", 2600, 4.8, "福建", "厦门大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/xmu.jpg"}, new Spot[]{}));
        spots.add(new Spot("南京中医药大学", "school", 2100, 4.5, "江苏", "南京中医药大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/njutcm.jpg"}, new Spot[]{}));
        spots.add(new Spot("首都医科大学", "school", 2200, 4.6, "北京", "首都医科大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/ccmu.jpg"}, new Spot[]{}));
        spots.add(new Spot("华南理工大学", "school", 2500, 4.8, "广东", "华南理工大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/scut.jpg"}, new Spot[]{}));
        spots.add(new Spot("深圳大学", "school", 2400, 4.7, "广东", "深圳大学是中国著名的学府", Arrays.asList("学府", "名校", "普通高校"), new String[]{"source/szu.jpg"}, new Spot[]{}));
        spots.add(new Spot("暨南大学", "school", 2200, 4.6, "广东", "暨南大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/jnu.jpg"}, new Spot[]{}));
        spots.add(new Spot("华南师范大学", "school", 2100, 4.5, "广东", "华南师范大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/scnu.jpg"}, new Spot[]{}));
        spots.add(new Spot("华中农业大学", "school", 2200, 4.6, "湖北", "华中农业大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/hzaul.jpg"}, new Spot[]{}));
        spots.add(new Spot("云南大学", "school", 2100, 4.5, "云南", "云南大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/ynu.jpg"}, new Spot[]{}));
        spots.add(new Spot("贵州大学", "school", 2000, 4.4, "贵州", "贵州大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/gzu.jpg"}, new Spot[]{}));
        spots.add(new Spot("西北工业大学", "school", 2500, 4.8, "陕西", "西北工业大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/nwpu.jpg"}, new Spot[]{}));
        spots.add(new Spot("西南交通大学", "school", 2400, 4.7, "四川", "西南交通大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/swju.jpg"}, new Spot[]{}));
        spots.add(new Spot("成都理工大学", "school", 2200, 4.6, "四川", "成都理工大学是中国著名的学府", Arrays.asList("学府", "名校", "普通高校"), new String[]{"source/cdu.jpg"}, new Spot[]{}));
        spots.add(new Spot("成都中医药大学", "school", 2100, 4.5, "四川", "成都中医药大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/cdtcm.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京中医药大学", "school", 2300, 4.7, "北京", "北京中医药大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/bucm.jpg"}, new Spot[]{}));
        spots.add(new Spot("云南师范大学", "school", 2000, 4.4, "云南", "云南师范大学是中国著名的学府", Arrays.asList("学府", "普通高校"), new String[]{"source/ynnu.jpg"}, new Spot[]{}));
        spots.add(new Spot("中国人民公安大学", "school", 2000, 4.4, "北京", "中国人民公安大学是中国著名的学府", Arrays.asList("学府", "名校", "公安"), new String[]{"source/ppu.jpg"}, new Spot[]{}));
        spots.add(new Spot("上海师范大学", "school", 2200, 4.6, "上海", "上海师范大学是中国著名的学府", Arrays.asList("学府", "普通高校"), new String[]{"source/shnu.jpg"}, new Spot[]{}));
        spots.add(new Spot("湖南科技大学", "school", 2000, 4.4, "湖南", "湖南科技大学是中国著名的学府", Arrays.asList("学府", "普通高校"), new String[]{"source/hhust.jpg"}, new Spot[]{}));
        spots.add(new Spot("南昌大学", "school", 2200, 4.6, "江西", "南昌大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/ncu.jpg"}, new Spot[]{}));
        spots.add(new Spot("南方医科大学", "school", 2400, 4.7, "广东", "南方医科大学是中国著名的学府", Arrays.asList("学府", "普通高校", "医学院"), new String[]{"source/smu.jpg"}, new Spot[]{}));
        spots.add(new Spot("哈尔滨工程大学", "school", 2500, 4.8, "黑龙江", "哈尔滨工程大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/hrbeu.jpg"}, new Spot[]{}));
        spots.add(new Spot("华东理工大学", "school", 2500, 4.8, "上海", "华东理工大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/ecust.jpg"}, new Spot[]{}));
        spots.add(new Spot("中国药科大学", "school", 2300, 4.7, "江苏", "中国药科大学是中国著名的学府", Arrays.asList("学府", "名校", "211"), new String[]{"source/cpu.jpg"}, new Spot[]{}));
        spots.add(new Spot("中国美术学院", "school", 2200, 4.6, "浙江", "中国美术学院是中国著名的学府", Arrays.asList("学府", "名校", "艺术"), new String[]{"source/caa.jpg"}, new Spot[]{}));
        spots.add(new Spot("中国音乐学院", "school", 2100, 4.5, "北京", "中国音乐学院是中国著名的学府", Arrays.asList("学府", "名校", "艺术"), new String[]{"source/ccom.jpg"}, new Spot[]{}));
        spots.add(new Spot("中国戏曲学院", "school", 2200, 4.6, "北京", "中国戏曲学院是中国著名的学府", Arrays.asList("学府", "名校", "艺术"), new String[]{"source/nacta.jpg"}, new Spot[]{}));
        spots.add(new Spot("中国科学院大学", "school", 2600, 4.8, "北京", "中国科学院大学是中国著名的学府", Arrays.asList("学府", "名校", "985"), new String[]{"source/ucas.jpg"}, new Spot[]{}));
        spots.add(new Spot("中央戏剧学院", "school", 2400, 4.7, "北京", "中央戏剧学院是中国著名的学府", Arrays.asList("学府", "名校", "艺术"), new String[]{"source/cca.jpg"}, new Spot[]{}));
        spots.add(new Spot("张家界", "attraction", 2300, 4.9, "湖南", "张家界是中国的自然风景区，以其独特的石柱地貌而闻名", Arrays.asList("自然风光", "风景区", "地质奇观"), new String[]{"source/zhangjiajie.jpg"}, new Spot[]{}));
        spots.add(new Spot("九寨沟", "attraction", 2400, 4.9, "四川", "九寨沟是中国的自然保护区，以其多彩的湖泊和瀑布而著名", Arrays.asList("自然风光", "风景区", "湖泊"), new String[]{"source/jiuzhaigou.jpg"}, new Spot[]{}));
        spots.add(new Spot("阳朔", "attraction", 2000, 4.8, "广西", "阳朔是中国的旅游胜地，以其壮美的山水风光吸引着众多游客", Arrays.asList("自然风光", "风景区", "山水"), new String[]{"source/yangshuo.jpg"}, new Spot[]{}));
        spots.add(new Spot("三亚", "attraction", 2200, 4.8, "海南", "三亚是中国的热带海滨城市，拥有优美的海滩和清澈的海水", Arrays.asList("自然风光", "旅游胜地", "海滨"), new String[]{"source/sanya.jpg"}, new Spot[]{}));
        spots.add(new Spot("珠穆朗玛峰", "attraction", 2500, 4.9, "西藏", "珠穆朗玛峰是世界上海拔最高的山峰，吸引着众多登山爱好者", Arrays.asList("自然奇观", "登山", "山峰"), new String[]{"source/everest.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京天坛", "attraction", 1800, 4.8, "北京", "北京天坛是中国明清时期的皇家建筑，具有重要的历史和文化价值", Arrays.asList("历史", "文化", "古迹"), new String[]{"source/temple_of_heaven.jpg"}, new Spot[]{}));
        spots.add(new Spot("西安兵马俑", "attraction", 1900, 4.8, "陕西", "西安兵马俑是中国古代秦始皇陵的一部分，是世界上保存最完整的古代军事遗址之一", Arrays.asList("历史", "文化", "古迹"), new String[]{"source/terracotta_army.jpg"}, new Spot[]{}));
        spots.add(new Spot("张家口赤城山", "attraction", 1600, 4.7, "河北", "张家口赤城山是中国的自然风景区，四季景色各异，风景优美", Arrays.asList("自然风光", "风景区", "山脉"), new String[]{"source/chichengshan.jpg"}, new Spot[]{}));
        spots.add(new Spot("重庆洪崖洞", "attraction", 1700, 4.6, "重庆", "重庆洪崖洞是一处历史文化景区，具有独特的山城风貌和江边景观", Arrays.asList("历史", "文化", "地标"), new String[]{"source/hongya_cave.jpg"}, new Spot[]{}));
        spots.add(new Spot("丽江古城", "attraction", 1800, 4.7, "云南", "丽江古城是中国的历史文化名城之一，保留了许多古建筑和传统文化", Arrays.asList("历史", "文化", "古城"), new String[]{"source/lijiang_old_town.jpg"}, new Spot[]{}));
        spots.add(new Spot("乌鲁木齐天山天池", "attraction", 1900, 4.8, "新疆", "乌鲁木齐天山天池是中国的自然风景区，以其高山湖泊和原始森林而著名", Arrays.asList("自然风光", "风景区", "湖泊"), new String[]{"source/tianshan_tianchi.jpg"}, new Spot[]{}));
        spots.add(new Spot("成都武侯祠", "attraction", 1700, 4.7, "四川", "成都武侯祠是中国的历史文化景区，是中国三国时期蜀汉丞相诸葛亮的祠庙", Arrays.asList("历史", "文化", "古迹"), new String[]{"source/wuhou_temple.jpg"}, new Spot[]{}));
        spots.add(new Spot("青岛栈桥", "attraction", 1500, 4.7, "山东", "青岛栈桥是中国的海滨景点之一，是青岛的地标建筑之一", Arrays.asList("自然风光", "风景区", "海滨"), new String[]{"source/qingdao_zhanqiao.jpg"}, new Spot[]{}));
        spots.add(new Spot("厦门鼓浪屿", "attraction", 1700, 4.8, "福建", "厦门鼓浪屿是中国的历史文化名岛，保留了许多西方殖民地时期的建筑", Arrays.asList("历史", "文化", "岛屿"), new String[]{"source/gulangyu.jpg"}, new Spot[]{}));
        spots.add(new Spot("苏州园林", "attraction", 1600, 4.8, "江苏", "苏州园林是中国的古典园林艺术代表，具有独特的设计和美丽的景观", Arrays.asList("历史", "文化", "园林"), new String[]{"source/suzhou_gardens.jpg"}, new Spot[]{}));
        spots.add(new Spot("武汉黄鹤楼", "attraction", 1800, 4.7, "湖北", "武汉黄鹤楼是中国的历史文化名楼，是中国三大名楼之一", Arrays.asList("历史", "文化", "古迹"), new String[]{"source/yellow_crane_tower.jpg"}, new Spot[]{}));
        spots.add(new Spot("杭州西湖", "attraction", 1900, 4.9, "浙江", "杭州西湖是中国的著名湖泊景点，被誉为“人间天堂”", Arrays.asList("自然风光", "风景区", "湖泊"), new String[]{"source/west_lake_hangzhou.jpg"}, new Spot[]{}));
        spots.add(new Spot("成都大熊猫基地", "attraction", 1500, 4.8, "四川", "成都大熊猫基地是中国的熊猫保护研究中心，是世界上最重要的大熊猫繁育基地之一", Arrays.asList("自然保护", "动物园", "熊猫"), new String[]{"source/chengdu_panda_base.jpg"}, new Spot[]{}));
        spots.add(new Spot("长沙橘子洲头", "attraction", 1400, 4.7, "湖南", "长沙橘子洲头是中国的历史文化景点，是中国三国时期诸葛亮筑橘园之地", Arrays.asList("历史", "文化", "古迹"), new String[]{"source/juzizoutou.jpg"}, new Spot[]{}));
        spots.add(new Spot("九华山", "attraction", 1800, 4.7, "安徽", "九华山是中国的佛教圣地，以其险峻的山峰和古寺而著名", Arrays.asList("宗教", "风景区", "山脉"), new String[]{"source/jiuhuashan.jpg"}, new Spot[]{}));
        spots.add(new Spot("云冈石窟", "attraction", 1700, 4.7, "山西", "云冈石窟是中国的佛教艺术宝库，保存有大量精美的石刻艺术品", Arrays.asList("历史", "文化", "石窟"), new String[]{"source/yungang_grottoes.jpg"}, new Spot[]{}));
        spots.add(new Spot("南京夫子庙", "attraction", 1600, 4.7, "江苏", "南京夫子庙是中国的历史文化景点，是中国古代最著名的文庙之一", Arrays.asList("历史", "文化", "古迹"), new String[]{"source/confucius_temple_nanjing.jpg"}, new Spot[]{}));
        spots.add(new Spot("广州珠江夜游", "attraction", 1600, 4.6, "广东", "广州珠江夜游是中国的城市夜景之一，夜晚灯光璀璨，美不胜收", Arrays.asList("城市夜景", "游船", "珠江"), new String[]{"source/pearl_river_guangzhou.jpg"}, new Spot[]{}));
        spots.add(new Spot("昆明翠湖", "attraction", 1500, 4.7, "云南", "昆明翠湖是中国的人工湖泊，周围风景优美，是休闲游玩的好去处", Arrays.asList("自然风光", "风景区", "湖泊"), new String[]{"source/cuihu_kunming.jpg"}, new Spot[]{}));
        spots.add(new Spot("西藏布达拉宫", "attraction", 2000, 4.8, "西藏", "西藏布达拉宫是中国的宗教建筑之一，也是世界上海拔最高的宫殿", Arrays.asList("历史", "宗教", "古建筑"), new String[]{"source/potala_palace.jpg"}, new Spot[]{}));
        spots.add(new Spot("青海湖", "attraction", 1800, 4.8, "青海", "青海湖是中国的内陆湖泊，是中国最大的咸水湖，也是高原湖泊中海拔最高的", Arrays.asList("自然风光", "风景区", "湖泊"), new String[]{"source/qinghai_lake.jpg"}, new Spot[]{}));
        spots.add(new Spot("大连星海广场", "attraction", 1600, 4.6, "辽宁", "大连星海广场是中国的海滨广场之一，是大连的地标建筑之一", Arrays.asList("城市夜景", "地标", "海滨"), new String[]{"source/xinghai_square_dalian.jpg"}, new Spot[]{}));
        spots.add(new Spot("杭州灵隐寺", "attraction", 1700, 4.7, "浙江", "杭州灵隐寺是中国的佛教名胜之一，寺庙建筑古朴典雅，景色优美", Arrays.asList("宗教", "历史", "古迹"), new String[]{"source/lingyin_temple.jpg"}, new Spot[]{}));
        spots.add(new Spot("广州珠江塔", "attraction", 1900, 4.8, "广东", "广州珠江塔是中国的地标性建筑，是广州市的标志之一，也是世界第二高塔", Arrays.asList("地标", "城市夜景", "观光"), new String[]{"source/canton_tower.jpg"}, new Spot[]{}));
        spots.add(new Spot("厦门环岛路", "attraction", 1600, 4.6, "福建", "厦门环岛路是中国的滨海景观大道之一，沿途风景秀美，适合散步和观光", Arrays.asList("海滨", "观光", "散步"), new String[]{"source/huanqiu_road_xiamen.jpg"}, new Spot[]{}));
        spots.add(new Spot("西安城墙", "attraction", 1800, 4.8, "陕西", "西安城墙是中国现存规模最大、保存最完整的古代城垣建筑之一，是中国四大古城墙之一", Arrays.asList("历史", "文化", "古建筑"), new String[]{"source/xian_city_wall.jpg"}, new Spot[]{}));
        spots.add(new Spot("福建土楼", "attraction", 1700, 4.7, "福建", "福建土楼是中国的古建筑群，是世界上最具代表性的农村建筑之一，被誉为世界建筑史上的奇迹", Arrays.asList("历史", "文化", "古迹"), new String[]{"source/fujian_tulou.jpg"}, new Spot[]{}));
        spots.add(new Spot("广州长隆野生动物园", "attraction", 1700, 4.7, "广东", "广州长隆野生动物园是中国的知名动物园，拥有丰富的野生动物资源和精美的园林景观", Arrays.asList("动物园", "野生动物", "观光"), new String[]{"source/chimelong_safari_park.jpg"}, new Spot[]{}));
        spots.add(new Spot("青岛啤酒博物馆", "attraction", 1600, 4.6, "山东", "青岛啤酒博物馆是中国的啤酒文化景点，展示了青岛啤酒的历史和酿造工艺", Arrays.asList("博物馆", "啤酒文化", "观光"), new String[]{"source/qingdao_beer_museum.jpg"}, new Spot[]{}));
        spots.add(new Spot("苏州园林网师园", "attraction", 1500, 4.7, "江苏", "苏州园林网师园是中国的古典园林之一，以其精美的建筑和优美的景观而闻名", Arrays.asList("历史", "文化", "园林"), new String[]{"source/wangshi_garden_suzhou.jpg"}, new Spot[]{}));
        spots.add(new Spot("杭州宋城景区", "attraction", 1800, 4.8, "浙江", "杭州宋城景区是中国的历史文化主题公园，展示了宋代文化和建筑风格", Arrays.asList("历史", "文化", "主题公园"), new String[]{"source/songcheng.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京798艺术区", "attraction", 1700, 4.7, "北京", "北京798艺术区是中国的艺术文化景点，是艺术家和文化爱好者的聚集地", Arrays.asList("艺术区", "文化", "艺术品"), new String[]{"source/798_art_zone.jpg"}, new Spot[]{}));
        spots.add(new Spot("上海外滩", "attraction", 1900, 4.8, "上海", "上海外滩是中国的城市地标之一，是上海的标志性景点，夜景尤为迷人", Arrays.asList("城市夜景", "观光", "地标"), new String[]{"source/the_bund_shanghai.jpg"}, new Spot[]{}));
        spots.add(new Spot("哈尔滨冰雪大世界", "attraction", 1800, 4.7, "黑龙江", "哈尔滨冰雪大世界是中国的冰雪景观景点，每年冬季都会举办冰雪艺术节", Arrays.asList("冰雪景观", "冰雪艺术", "冰雕"), new String[]{"source/harbin_ice_and_snow_world.jpg"}, new Spot[]{}));
        spots.add(new Spot("成都熊猫基地", "attraction", 1700, 4.7, "四川", "成都熊猫基地是中国的大熊猫保护研究中心，游客可以近距离观赏熊猫", Arrays.asList("动物园", "野生动物", "观光"), new String[]{"source/chengdu_panda_base.jpg"}, new Spot[]{}));
        spots.add(new Spot("杭州西湖音乐喷泉", "attraction", 1600, 4.6, "浙江", "杭州西湖音乐喷泉是中国的音乐喷泉表演景点，融合了音乐和水的艺术", Arrays.asList("音乐喷泉", "观光", "西湖"), new String[]{"source/west_lake_fountain.jpg"}, new Spot[]{}));
        spots.add(new Spot("张家界天门山", "attraction", 1800, 4.8, "湖南", "张家界天门山是中国的自然景观景点，有着险峻的山峰和玻璃栈道吸引着众多游客", Arrays.asList("自然景观", "山峰", "玻璃栈道"), new String[]{"source/tianmen_mountain_zhangjiajie.jpg"}, new Spot[]{}));
        spots.add(new Spot("西安大雁塔", "attraction", 1800, 4.8, "陕西", "西安大雁塔是中国的古建筑之一，是唐代的佛塔，也是丝绸之路的标志性建筑", Arrays.asList("历史", "文化", "古迹"), new String[]{"source/big_goose_pagoda_xian.jpg"}, new Spot[]{}));
        spots.add(new Spot("桂林漓江", "attraction", 1900, 4.8, "广西", "桂林漓江是中国的风景名胜之一，以清澈的水质、奇峰异石和田园风光而著称", Arrays.asList("自然风光", "风景区", "河流"), new String[]{"source/lijiang_guilin.jpg"}, new Spot[]{}));
        spots.add(new Spot("山东泰山", "attraction", 1700, 4.7, "山东", "山东泰山是中国的五岳之一，是中国传统文化中的重要象征之一", Arrays.asList("自然风光", "风景区", "山脉"), new String[]{"source/taishan.jpg"}, new Spot[]{}));
        spots.add(new Spot("苏州拙政园", "attraction", 1600, 4.7, "江苏", "苏州拙政园是中国的古典园林之一，以其精致的设计和美丽的景观而著称", Arrays.asList("历史", "文化", "园林"), new String[]{"source/zhuozhengyuan_suzhou.jpg"}, new Spot[]{}));
        spots.add(new Spot("重庆洪崖洞", "attraction", 1800, 4.8, "重庆", "重庆洪崖洞是中国的历史文化景点，位于长江边，具有独特的山城风情", Arrays.asList("历史", "文化", "古建筑"), new String[]{"source/hongya_cave.jpg"}, new Spot[]{}));
        spots.add(new Spot("张家界武陵源", "attraction", 1900, 4.8, "湖南", "张家界武陵源是中国的自然景观景点，以其奇特的石柱地貌和植被类型而著名", Arrays.asList("自然景观", "石柱地貌", "国家公园"), new String[]{"source/wulingyuan_zhangjiajie.jpg"}, new Spot[]{}));
        spots.add(new Spot("青岛栈桥", "attraction", 1600, 4.7, "山东", "青岛栈桥是中国的海滨景点之一，是青岛的地标建筑之一", Arrays.asList("自然风光", "风景区", "海滨"), new String[]{"source/qingdao_zhanqiao.jpg"}, new Spot[]{}));
        spots.add(new Spot("苏州园林", "attraction", 1600, 4.8, "江苏", "苏州园林是中国的古典园林之一，以其精美的建筑和优美的景观而闻名", Arrays.asList("历史", "文化", "园林"), new String[]{"source/suzhou_gardens.jpg"}, new Spot[]{}));
        spots.add(new Spot("九寨沟", "attraction", 1900, 4.8, "四川", "九寨沟是中国的自然风景区，以其多姿多彩的湖泊、瀑布和秋色而著名", Arrays.asList("自然风光", "风景区", "湖泊"), new String[]{"source/jiuzhaigou.jpg"}, new Spot[]{}));
        spots.add(new Spot("敦煌莫高窟", "attraction", 1800, 4.7, "甘肃", "敦煌莫高窟是中国的佛教艺术宝库，保存有大量的壁画和雕塑", Arrays.asList("历史", "文化", "石窟"), new String[]{"source/mogao_caves_dunhuang.jpg"}, new Spot[]{}));
        spots.add(new Spot("西安钟楼", "attraction", 1800, 4.7, "陕西", "西安钟楼是中国的历史文化景点之一，建于明朝，是古代的报时中心", Arrays.asList("历史", "文化", "古迹"), new String[]{"source/xian_bell_tower.jpg"}, new Spot[]{}));
        spots.add(new Spot("扬州瘦西湖", "attraction", 1700, 4.8, "江苏", "扬州瘦西湖是中国的古典园林之一，以其秀丽的自然景观和悠久的历史而著称", Arrays.asList("历史", "文化", "园林"), new String[]{"source/slender_west_lake_yangzhou.jpg"}, new Spot[]{}));
        spots.add(new Spot("张家界天门山玻璃栈道", "attraction", 2000, 4.8, "湖南", "张家界天门山玻璃栈道是中国的玻璃栈道景点，位于天门山上，是世界上最长的玻璃栈道", Arrays.asList("玻璃栈道", "自然景观", "观光"), new String[]{"source/tianmen_mountain_glass_bridge.jpg"}, new Spot[]{}));
        spots.add(new Spot("广州番禺长隆欢乐世界", "attraction", 1900, 4.7, "广东", "广州番禺长隆欢乐世界是中国的主题乐园，拥有丰富多样的游乐设施和表演节目", Arrays.asList("主题乐园", "游乐设施", "观光"), new String[]{"source/chimelong_paradise.jpg"}, new Spot[]{}));
        spots.add(new Spot("西藏布达拉宫", "attraction", 2100, 4.8, "西藏", "西藏布达拉宫是中国的宗教建筑之一，也是世界上海拔最高的宫殿", Arrays.asList("历史", "宗教", "古建筑"), new String[]{"source/potala_palace.jpg"}, new Spot[]{}));
        spots.add(new Spot("张家界天门山溶洞", "attraction", 1800, 4.7, "湖南", "张家界天门山溶洞是中国的地下溶洞景点，内部钟乳石形态各异，壮观美丽", Arrays.asList("溶洞", "自然景观", "探险"), new String[]{"source/tianmen_mountain_cave.jpg"}, new Spot[]{}));
        spots.add(new Spot("广州珠江夜游", "attraction", 1900, 4.8, "广东", "广州珠江夜游是中国的城市夜景之一，夜晚灯光璀璨，美不胜收", Arrays.asList("城市夜景", "游船", "珠江"), new String[]{"source/pearl_river_guangzhou.jpg"}, new Spot[]{}));
        spots.add(new Spot("杭州西湖龙井茶园", "attraction", 1700, 4.7, "浙江", "杭州西湖龙井茶园是中国的著名茶园，盛产龙井茶，景色宜人", Arrays.asList("茶园", "自然风光", "茶文化"), new String[]{"source/longjing_tea_plantation.jpg"}, new Spot[]{}));
        spots.add(new Spot("青海湖", "attraction", 2000, 4.8, "青海", "青海湖是中国的内陆湖泊，是中国最大的咸水湖，也是高原湖泊中海拔最高的", Arrays.asList("自然风光", "风景区", "湖泊"), new String[]{"source/qinghai_lake.jpg"}, new Spot[]{}));
        spots.add(new Spot("厦门鼓浪屿", "attraction", 1800, 4.8, "福建", "厦门鼓浪屿是中国的历史文化景点，有着悠久的历史和独特的建筑风格", Arrays.asList("历史", "文化", "岛屿"), new String[]{"source/gulangyu_xiamen.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京颐和园", "attraction", 2000, 4.8, "北京", "北京颐和园是中国的皇家园林，以其优美的自然景观和丰富的文化遗产而闻名", Arrays.asList("皇家园林", "历史", "文化"), new String[]{"source/summer_palace_beijing.jpg"}, new Spot[]{}));
        spots.add(new Spot("张家界张家界国家森林公园", "attraction", 1900, 4.8, "湖南", "张家界国家森林公园是中国的自然保护区，以其奇特的石柱地貌和植被类型而著名", Arrays.asList("自然保护区", "石柱地貌", "国家公园"), new String[]{"source/zhangjiajie_national_forest_park.jpg"}, new Spot[]{}));
        spots.add(new Spot("广西漓江", "attraction", 1800, 4.7, "广西", "广西漓江是中国的河流景点，以其清澈的水质、奇峰异石和田园风光而著称", Arrays.asList("自然风光", "风景区", "河流"), new String[]{"source/lijiang_guilin.jpg"}, new Spot[]{}));
        spots.add(new Spot("武汉黄鹤楼", "attraction", 1800, 4.8, "湖北", "武汉黄鹤楼是中国的古建筑之一，以其优美的建筑风格和丰富的历史文化而著称", Arrays.asList("历史", "文化", "古迹"), new String[]{"source/yellow_crane_tower_wuhan.jpg"}, new Spot[]{}));
        spots.add(new Spot("上海迪士尼乐园", "attraction", 1900, 4.8, "上海", "上海迪士尼乐园是中国的主题乐园，吸引着无数游客前来游玩", Arrays.asList("主题乐园", "游乐设施", "观光"), new String[]{"source/shanghai_disneyland.jpg"}, new Spot[]{}));
        spots.add(new Spot("九寨沟五花海", "attraction", 1900, 4.8, "四川", "九寨沟五花海是中国的自然景观景点，以其多彩的湖水和秀美的自然风光而著名", Arrays.asList("自然风光", "湖泊", "国家公园"), new String[]{"source/jiuzhaigou_wuhuahai.jpg"}, new Spot[]{}));
        spots.add(new Spot("杭州西湖雷峰塔", "attraction", 1800, 4.8, "浙江", "杭州西湖雷峰塔是中国的古建筑之一，是西湖的标志性建筑", Arrays.asList("历史", "文化", "古迹"), new String[]{"source/leifeng_pagoda_west_lake.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京天坛", "attraction", 1900, 4.8, "北京", "北京天坛是中国的古代建筑，是明清两代帝王祭祀天地之处", Arrays.asList("历史", "文化", "古迹"), new String[]{"source/temple_of_heaven_beijing.jpg"}, new Spot[]{}));
        spots.add(new Spot("长江三峡", "attraction", 2000, 4.8, "湖北", "长江三峡是中国的自然景观，以其雄伟的峡谷景色和丰富的文化遗产而著称", Arrays.asList("自然风光", "峡谷", "游船"), new String[]{"source/three_gorges_yangtze_river.jpg"}, new Spot[]{}));
        spots.add(new Spot("西安华清池", "attraction", 1800, 4.7, "陕西", "西安华清池是中国的温泉景点，以其优美的风景和悠久的历史而著称", Arrays.asList("温泉", "自然风光", "历史"), new String[]{"source/huaqing_palace_xian.jpg"}, new Spot[]{}));
        spots.add(new Spot("成都武侯祠", "attraction", 1800, 4.7, "四川", "成都武侯祠是中国的历史文化景点，是纪念三国时期蜀汉丞相诸葛亮的祠庙", Arrays.asList("历史", "文化", "古迹"), new String[]{"source/wuhou_temple_chengdu.jpg"}, new Spot[]{}));
        spots.add(new Spot("黄山西海景区", "attraction", 1900, 4.8, "安徽", "黄山西海景区是中国的黄山风景区之一，以其险峻的山峰和奇特的石柱而著名", Arrays.asList("自然风光", "山峰", "石柱"), new String[]{"source/yellow_mountain_xihai.jpg"}, new Spot[]{}));
        spots.add(new Spot("广州白云山", "attraction", 1800, 4.7, "广东", "广州白云山是中国的自然风光景点，以其秀美的山峰和浓厚的人文历史而著称", Arrays.asList("自然风光", "山峰", "徒步"), new String[]{"source/baiyun_mountain_guangzhou.jpg"}, new Spot[]{}));
        spots.add(new Spot("九寨沟双龙海", "attraction", 1900, 4.8, "四川", "九寨沟双龙海是中国的自然景观景点，以其清澈的湖水和多彩的水景而著名", Arrays.asList("自然风光", "湖泊", "国家公园"), new String[]{"source/jiuzhaigou_shuanglonghai.jpg"}, new Spot[]{}));
        spots.add(new Spot("山东青岛啤酒博物馆", "attraction", 1800, 4.7, "山东", "山东青岛啤酒博物馆是中国的啤酒文化景点，展示了啤酒的历史和制作过程", Arrays.asList("文化博物馆", "啤酒文化", "观光"), new String[]{"source/qingdao_beer_museum.jpg"}, new Spot[]{}));
        spots.add(new Spot("云南石林", "attraction", 1900, 4.8, "云南", "云南石林是中国的石灰岩地貌景点，以其奇特的岩石群和多样的地形而著称", Arrays.asList("自然风光", "岩石地貌", "探险"), new String[]{"source/stone_forest_yunnan.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京故宫", "attraction", 2000, 4.9, "北京", "北京故宫是中国的宫殿建筑，是中国明清两代的皇家宫殿，也是世界上最大的古代木质结构建筑群", Arrays.asList("历史", "文化", "宫殿"), new String[]{"source/forbidden_city_beijing.jpg"}, new Spot[]{}));
        spots.add(new Spot("青海可可西里", "attraction", 2000, 4.8, "青海", "青海可可西里是中国的自然保护区，是藏羚羊的主要栖息地，也是中国西部生态保护的重要区域", Arrays.asList("自然保护区", "生态保护", "野生动物"), new String[]{"source/hoh_xil_qinghai.jpg"}, new Spot[]{}));
        spots.add(new Spot("江苏苏州博物馆", "attraction", 1800, 4.7, "江苏", "江苏苏州博物馆是中国的文化艺术博物馆，收藏展示了丰富的文物和艺术品", Arrays.asList("文化博物馆", "艺术展览", "观光"), new String[]{"source/suzhou_museum.jpg"}, new Spot[]{}));
        spots.add(new Spot("贵州黄果树瀑布", "attraction", 1900, 4.8, "贵州", "贵州黄果树瀑布是中国的瀑布景点，以其水势雄伟、气势磅礴而著名", Arrays.asList("自然风光", "瀑布", "观光"), new String[]{"source/huangguoshu_waterfall_guizhou.jpg"}, new Spot[]{}));
        spots.add(new Spot("北京北海公园", "attraction", 1800, 4.7, "北京", "北京北海公园是中国的园林景点，以其古典园林风格和美丽的湖泊而著名", Arrays.asList("古典园林", "自然风光", "湖泊"), new String[]{"source/beihai_park_beijing.jpg"}, new Spot[]{}));
        spots.add(new Spot("陕西华山", "attraction", 2000, 4.8, "陕西", "陕西华山是中国的风景名胜之一，以其险峻的山峰和悠久的历史而著称", Arrays.asList("自然风光", "山峰", "徒步"), new String[]{"source/mount_hua_shaanxi.jpg"}, new Spot[]{}));
        spots.add(new Spot("重庆长江索道", "attraction", 1900, 4.8, "重庆", "重庆长江索道是中国的索道景点，连接江北和江南，是观赏重庆夜景的最佳地点之一", Arrays.asList("索道", "夜景", "观光"), new String[]{"source/yangtze_river_cable_car_chongqing.jpg"}, new Spot[]{}));
        spots.add(new Spot("福建武夷山", "attraction", 1800, 4.7, "福建", "福建武夷山是中国的自然风景区，以其险峻的山峰和茶园而著名", Arrays.asList("自然风光", "山脉", "茶园"), new String[]{"source/wuyi_mountains_fujian.jpg"}, new Spot[]{}));
//
//
        saveSpotData();
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
        for (Spot spot : spots) {
            if (spot.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

}


import com.formdev.flatlaf.*;
import com.formdev.flatlaf.FlatDarkLaf;

public class Main {

    public static void main(String[] args) {
        FlatDarkLaf.install();
        UserManagement userManagement = new UserManagement();// 创建用户管理对象
        SpotManagement spotManagement = new SpotManagement();// 创建景点管理对象
        //RecommendationPanel recommendationPanel = new RecommendationPanel(spotManagement);// 创建推荐面板对象
        LoginApp loginApp = new LoginApp(userManagement, spotManagement);// 创建登录系统对象

    }
}


public class Main {

    public static void main(String[] args) {
        UserManagement userManagement = new UserManagement();// 创建用户管理对象
        new LoginApp(userManagement);// 创建登录界面对象,并传入用户管理对象
    }

}

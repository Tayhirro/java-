public class StudyDiarySystem {
    private final User currUser;// 当前用户
    private final UserManagement userManagement;// 用户管理对象
    private final SpotManagement spotManagement;// 景点管理对象
    public StudyDiarySystem(User currUser, UserManagement userManagement, SpotManagement spotManagement) {
        this.currUser = currUser;
        this.userManagement = userManagement;
        this.spotManagement = spotManagement;
    }
    public User getCurrUser() {
        return currUser;
    }
    public UserManagement getUserManagement() {
        return userManagement;
    }
    public SpotManagement getSpotManagement() {
        return spotManagement;
    }
}

import javax.swing.JPanel;
public class StudyDiaryRecommendationPanel extends JPanel{
    private final User currUser;// 当前用户
    private final UserManagement userManagement;// 用户管理对象
    private final SpotManagement spotManagement;// 景点管理对象
    public StudyDiaryRecommendationPanel(StudyDiarySystem studyDiarySystem) {
        this.currUser = studyDiarySystem.getCurrUser();
        this.userManagement = studyDiarySystem.getUserManagement();
        this.spotManagement = studyDiarySystem.getSpotManagement();
    }
}

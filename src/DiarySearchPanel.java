import javax.swing.*;

public class DiarySearchPanel extends JPanel {
    private final User currUser;// 当前用户
    private final UserManagement userManagement;// 用户管理对象

    public DiarySearchPanel(StudyDiarySystem studyDiarySystem) {
        this.currUser = studyDiarySystem.getCurrUser();
        this.userManagement = studyDiarySystem.getUserManagement();
    }
}

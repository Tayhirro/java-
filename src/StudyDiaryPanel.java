
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class StudyDiaryPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    User currUser;
    UserManagement userManagement;
    //创建jpanel
    private JPanel  leftPanel;  //放置在左边
    private CardLayout cardLayout; // 用于切换右边的页面
    private JPanel rightPanel; // 右边的页面
    private StudyDiarySystem studyDiarySystem;

    public StudyDiaryPanel(User currUser, UserManagement userManagement,SpotManagement spotManagement) {
        this.currUser = currUser;
        this.userManagement = userManagement;
        setLayout(new BorderLayout());
        leftPanel = new JPanel(new GridLayout(5, 1));
        studyDiarySystem = new StudyDiarySystem(currUser, userManagement,spotManagement);
        String[] buttonNames = {"游学日记", "创作日记", "日记搜索", "Button 5"};
        JButton buttonName0=new JButton(buttonNames[0]);
        buttonName0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在这里添加你的代码
                // 例如，你可以在这里添加切换到新的 JPanel 的代码
                        rightPanel.add(CreateDiaryPanel(), "游学日记");
                        cardLayout.show(rightPanel, buttonNames[0]);
                // 你也可以添加其他的逻辑，例如打印一条消息
                System.out.println("Button " + buttonName0 + " was clicked");
            }
        });
        leftPanel.add(buttonName0);
        JButton buttonName1=new JButton(buttonNames[1]);
        buttonName1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在这里添加你的代码
                // 例如，你可以在这里添加切换到新的 JPanel 的代码
                cardLayout.show(rightPanel, buttonNames[1]);
                // 你也可以添加其他的逻辑，例如打印一条消息
                System.out.println("Button " + buttonName1 + " was clicked");
            }
        });
        leftPanel.add(buttonName1);
        JButton buttonName2=new JButton(buttonNames[2]);
        buttonName2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在这里添加你的代码
                // 例如，你可以在这里添加切换到新的 JPanel 的代码
                cardLayout.show(rightPanel, buttonNames[2]);
                // 你也可以添加其他的逻辑，例如打印一条消息
                System.out.println("Button " + buttonName2 + " was clicked");
            }
        });
        leftPanel.add(buttonName2);


        rightPanel = new JPanel();
        cardLayout = new CardLayout();
        rightPanel.setLayout(cardLayout);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        rightPanel.add(CreateComposeDiaryPanel(), "创作日记");

        rightPanel.add(CreateDiaryPanel(), "游学日记");


        rightPanel.add(CreateDiarySearchPanel(), "日记搜索");

    }
    private JPanel CreateComposeDiaryPanel() {
        ComposeDiaryPanel  composeDiaryPanel = new ComposeDiaryPanel(studyDiarySystem);
        return composeDiaryPanel;
    }
    private JPanel CreateDiaryPanel() {
        DiaryPanel diaryPanel = new DiaryPanel();
        return diaryPanel;
    }
    private JPanel CreateDiarySearchPanel() {
        DiarySearchPanel diarySearchPanel = new DiarySearchPanel(studyDiarySystem);
        return diarySearchPanel;
    }

}

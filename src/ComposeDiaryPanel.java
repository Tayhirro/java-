import javax.swing.*;
import java.awt.*;

public class ComposeDiaryPanel extends JPanel {
    private final User currUser;// 当前用户
    private final UserManagement userManagement;// 用户管理对象
    private final ComposeDiary composeDiary;
    private final SpotManagement spotManagement;
    public ComposeDiaryPanel(StudyDiarySystem studyDiarySystem) {
        this.currUser = studyDiarySystem.getCurrUser();
        this.userManagement = studyDiarySystem.getUserManagement();
        this.composeDiary = new ComposeDiary(studyDiarySystem);
        this.spotManagement = studyDiarySystem.getSpotManagement();



        setLayout(new BorderLayout());
        // 创建一个文本区域用于输入标题
        JTextArea titleTextArea = new JTextArea();
        titleTextArea.setText("在这里输入您的日记标题");
        add(new JScrollPane(titleTextArea), BorderLayout.NORTH);

        // 创建一个文本区域用于输入正文
        JTextArea contentTextArea = new JTextArea();
        contentTextArea.setText("在这里输入您的日记内容");
        add(new JScrollPane(contentTextArea), BorderLayout.CENTER);




        // 创建一个面板用于放置评分单选框和位置下拉框
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));



        // 创建提交按钮
        JButton submitButton = new JButton("提交");

        // 创建位置输入框
        JTextField locationField = new JTextField(10); // 10列宽
        bottomPanel.add(locationField);


        // 设置按钮的点击事件
        submitButton.addActionListener(e -> {
            String title = titleTextArea.getText();
            String content = contentTextArea.getText();
            String location = locationField.getText();
            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(null, "标题和内容不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int rating = 0;
            for (Component component : bottomPanel.getComponents()) {
                if (component instanceof JRadioButton) {
                    JRadioButton radioButton = (JRadioButton) component;
                    if (radioButton.isSelected()) {
                        rating = Integer.parseInt(radioButton.getText());
                        break;
                    }
                }
            }
            composeDiary.addDiary(title, content, rating, location);
            titleTextArea.setText("");
            contentTextArea.setText("");
            JOptionPane.showMessageDialog(null, "日记提交成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        });

        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置按钮居中
        bottomPanel.add(submitButton);

        // 创建评分单选框
        ButtonGroup ratingGroup = new ButtonGroup();
        for (int i = 1; i <= 5; i++) {
            JRadioButton radioButton = new JRadioButton(String.valueOf(i));
            ratingGroup.add(radioButton);
            bottomPanel.add(radioButton);
        }



        add(bottomPanel, BorderLayout.SOUTH);
    }




}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ComposeDiaryPanel extends JPanel {
    private final User currUser;// 当前用户
    private final UserManagement userManagement;// 用户管理对象
    private final ComposeDiary composeDiary;
    private final SpotManagement spotManagement;
    private  int interest=0;
    private  int rating=0;

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

        //创建一个单选框，内容为：自然风光，人文地理，特色美食
        JRadioButton naturalScenery = new JRadioButton("自然风光");
        JRadioButton humanGeography = new JRadioButton("人文地理");
        JRadioButton specialFood = new JRadioButton("特色美食");
        ButtonGroup group = new ButtonGroup();
        group.add(naturalScenery);
        group.add(humanGeography);
        group.add(specialFood);
        bottomPanel.add(naturalScenery);
        bottomPanel.add(humanGeography);
        bottomPanel.add(specialFood);
        naturalScenery.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interest=1;

            }
        });

        humanGeography.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interest=2;
            }
        });

        specialFood.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interest=3;
            }
        });



        // 创建提交按钮
        JButton submitButton = new JButton("提交");

        // 创建位置输入框
        JTextField locationField = new JTextField(10); // 10列宽
        bottomPanel.add(locationField);

        // 创建评分单选框
        ButtonGroup ratingGroup = new ButtonGroup();
        for (int i = 1; i <= 5; i++) {
            JRadioButton radioButton = new JRadioButton(String.valueOf(i));
            ratingGroup.add(radioButton);
            bottomPanel.add(radioButton);
            //设置点击事件
            radioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    rating = Integer.parseInt(radioButton.getText());
                }
            });
        }

        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT); // 设置按钮居中
        bottomPanel.add(submitButton);


        add(bottomPanel, BorderLayout.SOUTH);
        // 设置按钮的点击事件
        submitButton.addActionListener(e -> {
            String title = titleTextArea.getText();
            String content = contentTextArea.getText();
            String location = locationField.getText();
            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(null, "标题和内容不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //如果没有选择单选框和输入位置和评分，弹出提示框
            if(!naturalScenery.isSelected()&&!humanGeography.isSelected()&&!specialFood.isSelected()){
                JOptionPane.showMessageDialog(null, "未输入完", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }



            composeDiary.addDiary(title, content, rating, location,interest);
            titleTextArea.setText("");
            contentTextArea.setText("");
            JOptionPane.showMessageDialog(null, "日记提交成功", "成功", JOptionPane.INFORMATION_MESSAGE);
        });


    }




}

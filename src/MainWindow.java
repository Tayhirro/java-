import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainWindow extends JFrame {

    public MainWindow(RecommendationSystem recommendationSystem, SearchSystem searchSystem, List<String> userInterests) {

        setTitle("������");
        setSize(240, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton recommendationButton = new JButton("��ѧ�Ƽ�");
        JButton routePlanningButton = new JButton("·�߹滮");
        JButton placeQueryButton = new JButton("������ѯ");
        JButton diaryButton = new JButton("��ѧ�ռ�");

        // ������岢������
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ��ѧǰ���������
        JPanel beforeStudyTourPanel = new JPanel();
        beforeStudyTourPanel.add(new JLabel("��ѧǰ��"));
        beforeStudyTourPanel.add(recommendationButton);
        panel.add(beforeStudyTourPanel, gbc); // �����ѧǰ����������

        // ��ѧ�����������
        gbc.gridy++;
        JPanel inStudyTourPanel = new JPanel();
        inStudyTourPanel.add(new JLabel("��ѧ�У�"));
        JPanel inStudyTourButtonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        inStudyTourButtonPanel.add(routePlanningButton);
        inStudyTourButtonPanel.add(placeQueryButton);
        inStudyTourPanel.add(inStudyTourButtonPanel);
        panel.add(inStudyTourPanel, gbc); // �����ѧ������������

        // ��ѧ�����������
        gbc.gridy++;
        JPanel afterStudyTourPanel = new JPanel();
        afterStudyTourPanel.add(new JLabel("��ѧ��"));
        afterStudyTourPanel.add(diaryButton);
        panel.add(afterStudyTourPanel, gbc); // �����ѧ������������

        add(panel); // ���������ӵ�������


        // Add action listeners to the buttons
        recommendationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the recommendation window
                new RecommendationWindow(recommendationSystem, searchSystem, userInterests).setVisible(true);
            }
        });

        routePlanningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the route planning window
                new RoutePlanningWindow().setVisible(true);
            }
        });

        placeQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the place query window
                new PlaceQueryWindow().setVisible(true);
            }
        });

        diaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the diary window
                new DiaryWindow().setVisible(true);
            }
        });
    }
}
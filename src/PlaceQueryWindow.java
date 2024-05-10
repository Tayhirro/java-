import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// ������ѯģ��
public class PlaceQueryWindow extends JFrame {

    public PlaceQueryWindow() {
        setTitle("������ѯ");
        setSize(200, 160);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        JButton nearbyPlacesButton = new JButton("��������");
        JButton placeQueryButton = new JButton("������ѯ");

        // ������岢���ò���
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ��Ӹ���������ť
        panel.add(nearbyPlacesButton, gbc);

        // ��ӳ�����ѯ��ť
        gbc.gridy++;
        panel.add(placeQueryButton, gbc);

        add(panel); // �������ӵ�������

        // Ϊ��ť��� ActionListener
        nearbyPlacesButton.addActionListener(_ -> new NearbyPlacesFieldWindow().setVisible(true));

        placeQueryButton.addActionListener(_ -> new PlaceSearchFieldWindow().setVisible(true));


    }

    //��������
    static class NearbyPlacesFieldWindow extends JFrame {
        private final JTextField positionField;
        private String position;
        public NearbyPlacesFieldWindow() {

            setTitle("��������");
            setSize(300, 150);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            // Create UI components
            positionField = new JTextField(20);
            JButton submitButton = new JButton("�ύ");

            JRadioButton straightDistanceRadioButton = new JRadioButton("ֱ�߾���");
            JRadioButton pathDistanceRadioButton = new JRadioButton("·������");
            ButtonGroup distanceGroup = new ButtonGroup();
            distanceGroup.add(straightDistanceRadioButton);
            distanceGroup.add(pathDistanceRadioButton);
            straightDistanceRadioButton.setSelected(true); // Ĭ��ѡ��ֱ�߾���

            JPanel radioPanel = new JPanel();
            radioPanel.add(straightDistanceRadioButton);
            radioPanel.add(pathDistanceRadioButton);

            JPanel positionPanel = new JPanel();
            positionPanel.add(new JLabel("��ǰλ��:"));
            positionPanel.add(positionField);

            JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            submitPanel.add(submitButton);

            setLayout(new GridLayout(3, 1)); // �������񲼾֣�3��1��
            add(radioPanel);
            add(positionPanel);
            add(submitPanel);

        // Add action listener to the submit button
            submitButton.addActionListener(_ -> {
                position = positionField.getText();
                System.out.println("��ǰλ�ã� "+position);
                if(position.isEmpty()){
                    JOptionPane.showMessageDialog(null, "�����뵱ǰλ��");
                    return;
                }
                PlaceQuerySystem placeQuerySystem = new PlaceQuerySystem();
                int positionId = placeQuerySystem.getPointId(position);
                if(positionId == -1) {
                    JOptionPane.showMessageDialog(null, "��ǰλ�ò�����");
                    return;
                }
                String[] places = new String[1000];
                double[] distances = new double[1000];
                int num;
                if(straightDistanceRadioButton.isSelected()) {
                    System.out.println("ֱ�߾���");
                    num = placeQuerySystem.PlaceQuery(positionId, "", true, places, distances);
                }else {
                    System.out.println("·������");
                    num = placeQuerySystem.PlaceQuery(positionId, "", false, places, distances);
                }
                // Test data
                String[] places3 = {
                        "Place A", "Place B", "Place C", "Place D", "Place E",
                        "Place F", "Place G", "Place H", "Place I", "Place J",
                        "Place K", "Place L", "Place M", "Place N", "Place O",
                        "Place A", "Place B", "Place C", "Place D", "Place E",
                        "Place F", "Place G", "Place H", "Place I", "Place J",
                        "Place K", "Place L", "Place M", "Place N", "Place O"
                };

                double[] distances3 = {
                        100.5, 200.7, 150.2, 300.1, 250.8,
                        130.3, 220.6, 180.9, 270.4, 320.0,
                        140.6, 210.9, 190.2, 280.5, 330.7,
                        100.5, 200.7, 150.2, 300.1, 250.8,
                        130.3, 220.6, 180.9, 270.4, 320.0,
                        140.6, 210.9, 190.2, 280.5, 330.7
                };

                PlaceDistanceWindow placeDistanceWindow = new PlaceDistanceWindow(places3, distances3,num);
                placeDistanceWindow.setVisible(true);
            });
        }

    }

    //������ѯ
    static class PlaceSearchFieldWindow  extends JFrame {
        private final JTextField positionField;
        private final JTextField placeSearchField;
        private final JRadioButton straightDistanceRadioButton;

        private String position;

        public PlaceSearchFieldWindow() {

            setTitle("������ѯ");
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            // Create UI components
            positionField = new JTextField(20);
            placeSearchField = new JTextField(20);
            JButton submitButton = new JButton("��ѯ");

            straightDistanceRadioButton = new JRadioButton("ֱ�߾���");
            JRadioButton pathDistanceRadioButton = new JRadioButton("·������");
            ButtonGroup distanceGroup = new ButtonGroup();
            distanceGroup.add(straightDistanceRadioButton);
            distanceGroup.add(pathDistanceRadioButton);
            straightDistanceRadioButton.setSelected(true); // Ĭ��ѡ��ֱ�߾���

            JPanel radioPanel = new JPanel();
            radioPanel.add(straightDistanceRadioButton);
            radioPanel.add(pathDistanceRadioButton);

            JPanel positionPanel = new JPanel();
            positionPanel.add(new JLabel("��ǰλ��:"));
            positionPanel.add(positionField);

            JPanel placeSearchPanel = new JPanel();
            placeSearchPanel.add(new JLabel("��ѯ����:"));
            placeSearchPanel.add(placeSearchField);

            JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            submitPanel.add(submitButton);

            setLayout(new GridLayout(4, 1)); // �������񲼾֣�3��1��
            add(radioPanel);
            add(positionPanel);
            add(placeSearchPanel);
            add(submitPanel);


            // Add action listener to the submit button
            submitButton.addActionListener(_ -> {
                position = positionField.getText();
                System.out.println("��ǰλ�ã� " + position);
                String place = placeSearchField.getText();
                System.out.println("��ѯ������ " + place);
                if(position.isEmpty()){
                    JOptionPane.showMessageDialog(null, "�����뵱ǰλ��");
                    return;
                }
                if(place.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "�������ѯ����");
                    return;
                }
                PlaceQuerySystem placeQuerySystem = new PlaceQuerySystem();
                int positionId = placeQuerySystem.getPointId(position);
                if(positionId == -1) {
                    JOptionPane.showMessageDialog(null, "��ǰλ�ò�����");
                    return;
                }
                String[] places = new String[1000];
                double[] distances = new double[1000];
                int num;
                if(straightDistanceRadioButton.isSelected()) {
                    System.out.println("ֱ�߾���");
                    num = placeQuerySystem.PlaceQuery(positionId, place, true, places, distances);
                }else {
                    System.out.println("·������");
                    num = placeQuerySystem.PlaceQuery(positionId, place, false, places, distances);
                }

                PlaceDistanceWindow placeDistanceWindow = new PlaceDistanceWindow(places, distances,num);
                placeDistanceWindow.setVisible(true);
            });
        }
    }

}


class PlaceDistanceWindow extends JFrame {

    public PlaceDistanceWindow(String[] places, double[] distances,int num) {

        // ���ô��ڱ���
        setTitle("��������");
        // ���ô��ڴ�С
        setSize(400, 300);
        // ���ô��ڹر���Ϊ
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // �����ڶ�λ����Ļ����
        setLocationRelativeTo(null);

        // �������ģ�ͣ��������У������;���
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("����");
        tableModel.addColumn("����/m");

        // ��������ӵ����ģ����
        for (int i = 0; i < num; i++) {
            tableModel.addRow(new Object[]{places[i], distances[i]});
        }

        // ��������ָ�����ģ�͵� JTable
        JTable table = new JTable(tableModel);

        // ������ѡ�п��
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);

        // ���ñ�������С
        Font tableFont = table.getFont();
        table.setFont(new Font(tableFont.getName(), Font.PLAIN, 14));

        // �����Զ������п������������
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // �������Ĺ������
        JScrollPane scrollPane = new JScrollPane(table);

        // ʹ�� GridBagLayout �����������
        JPanel contentPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        contentPane.add(scrollPane, gbc);

        // �����������ӵ�������
        setContentPane(contentPane);

    }

}

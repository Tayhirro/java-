import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// ������ѯģ��
public class PlaceQueryWindow extends JFrame {

    public PlaceQueryWindow() {
        setTitle("������ѯ"); // ���ô��ڱ���
        setSize(200, 160); // ���ô��ڴ�С
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ���ô��ڹر�ʱ�Ĳ���
        setLocationRelativeTo(null); // ������������Ļ����

        JButton nearbyPlacesButton = new JButton("��������"); // ����"��������"��ť
        JButton placeQueryButton = new JButton("������ѯ"); // ����"������ѯ"��ť

        // ������岢���ò���
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ��Ӹ���������ť�����
        panel.add(nearbyPlacesButton, gbc);

        // ��ӳ�����ѯ��ť�����
        gbc.gridy++;
        panel.add(placeQueryButton, gbc);

        add(panel); // �������ӵ�������

        // Ϊ��ť��� ActionListener
        nearbyPlacesButton.addActionListener(_ -> new NearbyPlacesFieldWindow().setVisible(true));
        placeQueryButton.addActionListener(_ -> new PlaceSearchFieldWindow().setVisible(true));
    }

    // ������������
    static class NearbyPlacesFieldWindow extends JFrame {
        private final JTextField positionField; // λ�������
        private String position; // λ���ַ���

        public NearbyPlacesFieldWindow() {
            setTitle("��������"); // ���ô��ڱ���
            setSize(300, 150); // ���ô��ڴ�С
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ���ô��ڹر�ʱ�Ĳ���
            setLocationRelativeTo(null); // ������������Ļ����

            // ����UI���
            positionField = new JTextField(20); // ����λ�������
            JButton submitButton = new JButton("�ύ"); // �����ύ��ť

            JRadioButton straightDistanceRadioButton = new JRadioButton("ֱ�߾���"); // ����ֱ�߾��뵥ѡ��ť
            JRadioButton pathDistanceRadioButton = new JRadioButton("·������"); // ����·�����뵥ѡ��ť
            ButtonGroup distanceGroup = new ButtonGroup(); // ������ѡ��ť��
            distanceGroup.add(straightDistanceRadioButton); // ��ֱ�߾��뵥ѡ��ť��ӵ���ѡ��ť��
            distanceGroup.add(pathDistanceRadioButton); // ��·�����뵥ѡ��ť��ӵ���ѡ��ť��
            straightDistanceRadioButton.setSelected(true); // Ĭ��ѡ��ֱ�߾���

            JPanel radioPanel = new JPanel(); // ������ѡ��ť���
            radioPanel.add(straightDistanceRadioButton); // ��ֱ�߾��뵥ѡ��ť��ӵ���ѡ��ť���
            radioPanel.add(pathDistanceRadioButton); // ��·�����뵥ѡ��ť��ӵ���ѡ��ť���

            JPanel positionPanel = new JPanel(); // ����λ�����
            positionPanel.add(new JLabel("��ǰλ��:")); // ����ǩ��ӵ�λ�����
            positionPanel.add(positionField); // ��λ���������ӵ�λ�����

            JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // �����ύ���
            submitPanel.add(submitButton); // ���ύ��ť��ӵ��ύ���

            setLayout(new GridLayout(3, 1)); // �������񲼾֣�3��1��
            add(radioPanel); // ����ѡ��ť�����ӵ�����
            add(positionPanel); // ��λ�������ӵ�����
            add(submitPanel); // ���ύ�����ӵ�����

            // Ϊ�ύ��ť��Ӷ���������
            submitButton.addActionListener(_ -> {
                position = positionField.getText(); // ��ȡλ���������ı�
                System.out.println("��ǰλ�ã� "+position);
                if(position.isEmpty()){
                    JOptionPane.showMessageDialog(null, "�����뵱ǰλ��"); // ���λ��Ϊ�գ��򵯳���ʾ��
                    return;
                }
                PlaceQuerySystem placeQuerySystem = new PlaceQuerySystem(); // ����������ѯϵͳ����
                int positionId = placeQuerySystem.getPointId(position); // ��ȡλ��ID
                if(positionId == -1) {
                    JOptionPane.showMessageDialog(null, "��ǰλ�ò�����"); // ���λ�ò����ڣ��򵯳���ʾ��
                    return;
                }
                String[] places = new String[1000]; // ������������
                double[] distances = new double[1000]; // ������������
                int num;
                if(straightDistanceRadioButton.isSelected()) { // ���ѡ����ֱ�߾���
                    System.out.println("ֱ�߾���");
                    num = placeQuerySystem.PlaceQuery(positionId, "", true, places, distances); // ��ѯ��������
                }else { // ���ѡ����·������
                    System.out.println("·������");
                    num = placeQuerySystem.PlaceQuery(positionId, "", false, places, distances); // ��ѯ��������
                }

                PlaceDistanceWindow placeDistanceWindow = new PlaceDistanceWindow(places, distances,num); // �����������봰��
                placeDistanceWindow.setVisible(true); // ��ʾ�������봰��
            });
        }

    }

    // ������ѯ����
    static class PlaceSearchFieldWindow  extends JFrame {
        private final JTextField positionField; // λ�������
        private final JTextField placeSearchField; // �������������
        private final JRadioButton straightDistanceRadioButton; // ֱ�߾��뵥ѡ��ť

        private String position; // λ���ַ���

        public PlaceSearchFieldWindow() {

            setTitle("������ѯ"); // ���ô��ڱ���
            setSize(300, 200); // ���ô��ڴ�С
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ���ô��ڹر�ʱ�Ĳ���
            setLocationRelativeTo(null); // ������������Ļ����

            // ����UI���
            positionField = new JTextField(20); // ����λ�������
            placeSearchField = new JTextField(20); // �����������������
            JButton submitButton = new JButton("��ѯ"); // ������ѯ��ť

            straightDistanceRadioButton = new JRadioButton("ֱ�߾���"); // ����ֱ�߾��뵥ѡ��ť
            JRadioButton pathDistanceRadioButton = new JRadioButton("·������"); // ����·�����뵥ѡ��ť
            ButtonGroup distanceGroup = new ButtonGroup(); // ������ѡ��ť��
            distanceGroup.add(straightDistanceRadioButton); // ��ֱ�߾��뵥ѡ��ť��ӵ���ѡ��ť��
            distanceGroup.add(pathDistanceRadioButton); // ��·�����뵥ѡ��ť��ӵ���ѡ��ť��
            straightDistanceRadioButton.setSelected(true); // Ĭ��ѡ��ֱ�߾���

            JPanel radioPanel = new JPanel(); // ������ѡ��ť���
            radioPanel.add(straightDistanceRadioButton); // ��ֱ�߾��뵥ѡ��ť��ӵ���ѡ��ť���
            radioPanel.add(pathDistanceRadioButton); // ��·�����뵥ѡ��ť��ӵ���ѡ��ť���

            JPanel positionPanel = new JPanel(); // ����λ�����
            positionPanel.add(new JLabel("��ǰλ��:")); // ����ǩ��ӵ�λ�����
            positionPanel.add(positionField); // ��λ���������ӵ�λ�����

            JPanel placeSearchPanel = new JPanel(); // ���������������
            // ��������������岢������
            placeSearchPanel.add(new JLabel("��ѯ����:")); // ��ӱ�ǩ
            placeSearchPanel.add(placeSearchField); // ��ӳ������������

            // �����ύ��岢���ò���Ϊ����
            JPanel submitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            submitPanel.add(submitButton); // ����ύ��ť�����

            // ���ò���Ϊ4��1�е����񲼾֣���������
            setLayout(new GridLayout(4, 1));
            add(radioPanel); // ��ӵ�ѡ��ť���
            add(positionPanel); // ���λ�����
            add(placeSearchPanel); // ��ӳ����������
            add(submitPanel); // ����ύ���

            // Ϊ�ύ��ť��Ӷ���������
            submitButton.addActionListener(_ -> {
                position = positionField.getText(); // ��ȡλ���������ı�
                System.out.println("��ǰλ�ã� " + position);
                String place = placeSearchField.getText(); // ��ȡ���������������ı�
                System.out.println("��ѯ������ " + place);
                if(position.isEmpty()){
                    JOptionPane.showMessageDialog(null, "�����뵱ǰλ��"); // ���λ��Ϊ�գ��򵯳���ʾ��
                    return;
                }
                if(place.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "�������ѯ����"); // �������Ϊ�գ��򵯳���ʾ��
                    return;
                }
                PlaceQuerySystem placeQuerySystem = new PlaceQuerySystem(); // ����������ѯϵͳ����
                int positionId = placeQuerySystem.getPointId(position); // ��ȡλ��ID
                if(positionId == -1) {
                    JOptionPane.showMessageDialog(null, "��ǰλ�ò�����"); // ���λ�ò����ڣ��򵯳���ʾ��
                    return;
                }
                String[] places = new String[1000]; // ������������
                double[] distances = new double[1000]; // ������������
                int num;
                if(straightDistanceRadioButton.isSelected()) { // ���ѡ����ֱ�߾���
                    System.out.println("ֱ�߾���");
                    num = placeQuerySystem.PlaceQuery(positionId, place, true, places, distances); // ��ѯ����
                }else { // ���ѡ����·������
                    System.out.println("·������");
                    num = placeQuerySystem.PlaceQuery(positionId, place, false, places, distances); // ��ѯ����
                }

                PlaceDistanceWindow placeDistanceWindow = new PlaceDistanceWindow(places, distances,num); // �����������봰��
                placeDistanceWindow.setVisible(true); // ��ʾ�������봰��
            });
        }
    }

}

// �������봰��
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
            tableModel.addRow(new Object[]{places[i], distances[i]}); // �����
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
        gbc.fill = GridBagConstraints.BOTH; // ������䷽ʽ
        gbc.weightx = 1.0; // ����ˮƽȨ��
        gbc.weighty = 1.0; // ���ô�ֱȨ��
        contentPane.add(scrollPane, gbc); // ��ӹ�����嵽�������

        // �����������ӵ�������
        setContentPane(contentPane);

    }

}
import javax.swing.*;

import javax.swing.*;

/**
 * DiaryWindow �� JFrame �����࣬�����ռ�Ӧ�ó���������ڡ�
 * �������˴��ڵı��⡢��С��Ĭ�Ϲرղ�����λ�á�
 */
public class DiaryWindow extends JFrame {
    /**
     * DiaryWindow ��Ĺ��캯����
     * ����ʼ�����ڣ����ñ��⡢��С��Ĭ�Ϲرղ�����λ�á�
     * Ӧ��ָ��λ��������������
     */
    public DiaryWindow() {
        setTitle("��ѧ�ռ�"); // ���ô��ڵı���
        setSize(320, 200); // ���ô��ڵĴ�С
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ����Ĭ�Ϲرղ���
        setLocationRelativeTo(null); // �����ھ���
        // ���������������
    }
}